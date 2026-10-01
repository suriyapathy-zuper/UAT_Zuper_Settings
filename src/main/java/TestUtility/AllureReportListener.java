package TestUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IExecutionListener;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import BaseTest.Baseclass;

/**
 * Final reporting after every TestNG execution (Eclipse: right-click testng.xml > Run As > TestNG Suite, or mvn test):
 * latest-run Allure report -> ONE final Slack report (TestUtility.ReportToSlack).
 *
 * Registered through META-INF/services/org.testng.ITestNGListener (like Allure itself), so TestNG calls it
 * once before the first suite starts and once after the last suite - and every other listener - has finished.
 *
 *   onExecutionStart  -> delete the previous run's raw results (target/allure-results)
 *   ... TestNG runs every test; Allure writes the CURRENT run's results + failure screenshots ...
 *   generateReport    -> (IReporter, after all suites) collect the actual TestNG results
 *   onExecutionFinish -> generate the report into a temporary folder, replace <project>/allure with it,
 *                        then send the final Slack report (once) with the current allure/index.html attached
 *
 * <project>/allure therefore always holds only the latest completed execution. allure/index.html is the
 * self-contained (single-file) report, so it opens with a double-click; data/, widgets/, history/, plugins/
 * are the standard Allure report folders.
 * The report is built with the Allure command line from the local Maven repository (downloaded by allure-maven).
 * No PDF summary is configured in this project, so none is generated or attached.
 */
public class AllureReportListener implements IExecutionListener, IReporter {

	private static final Logger log = LogManager.getLogger(AllureReportListener.class);
	private static final String REPORT_DIRECTORY = "allure";
	private static final String DEFAULT_ALLURE_VERSION = "2.29.0";
	private static final long GENERATE_TIMEOUT_MINUTES = 5;
	// TestNG's name for the synthetic suite of an ad-hoc class/method run (Eclipse: Run As > TestNG Test)
	private static final String AD_HOC_SUITE_NAME = "Default suite";

	// one final report per JVM, even if the listener is registered more than once
	private static final AtomicBoolean finalReportDone = new AtomicBoolean(false);
	private static volatile List<ISuite> executedSuites;
	private static volatile LocalDateTime executionStart = LocalDateTime.now();

	private final Path projectDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath();

	@Override
	public void onExecutionStart() {
		executionStart = LocalDateTime.now();
		Path results = resultsDirectory();
		try {
			deleteDirectory(results);
			Files.createDirectories(results);
			log.info("[REPORT] Old Allure results cleaned: " + results);
		} catch (IOException e) {
			log.warn("[REPORT] Could not clean old Allure results " + results + " - " + e.getMessage());
		}
	}

	// called by TestNG after ALL suites have finished (before onExecutionFinish) - keeps the actual results
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		executedSuites = suites;
	}

	@Override
	public void onExecutionFinish() {
		if (!finalReportDone.compareAndSet(false, true)) {
			return;
		}
		LocalDateTime executionEnd = LocalDateTime.now();
		log.info("[REPORT] TestNG suite execution completed");
		Path report = projectDir.resolve(REPORT_DIRECTORY);
		boolean reportGenerated = generateAllureReport(report);

		log.info("[REPORT] PDF summary: not configured in this project - skipped");
		sendSlackReport(executionEnd, reportGenerated ? report.resolve("index.html").toFile() : null);
	}

	// latest Allure report -> <project>/allure; returns true only when it was generated for THIS execution
	private boolean generateAllureReport(Path report) {
		Path results = resultsDirectory();
		try {
			if (!Files.isDirectory(results) || isEmpty(results)) {
				log.warn("[REPORT] No Allure results in " + results + " - Allure report not generated");
				return false;
			}
			log.info("[REPORT] Generating Allure report from " + results);
			Path allure = allureCommandLine();
			Path buildDir = projectDir.resolve("target");
			Path newReport = buildDir.resolve("allure-report-latest");
			Path singleFile = buildDir.resolve("allure-report-single-file");

			generate(allure, results, newReport, false);
			generate(allure, results, singleFile, true);
			// self-contained index.html (opens from disk without a web server)
			Files.copy(singleFile.resolve("index.html"), newReport.resolve("index.html"), StandardCopyOption.REPLACE_EXISTING);
			deleteDirectory(singleFile);

			// replace the old report only after the new one was generated successfully
			deleteDirectory(report);
			log.info("[REPORT] Old Allure report removed: " + report);
			copyDirectory(newReport, report);
			deleteDirectory(newReport);
			if (!Files.isRegularFile(report.resolve("index.html"))) {
				throw new IOException("allure/index.html was not created");
			}
			log.info("[REPORT] Allure report generated successfully: " + report.resolve("index.html"));
			return true;
		} catch (Exception e) {
			log.error("[REPORT] Allure report generation failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
			return false;
		}
	}

	private void sendSlackReport(LocalDateTime executionEnd, File currentReport) {
		List<ISuite> suites = executedSuites;
		if (suites == null || suites.isEmpty()) {
			log.warn("[SLACK] No TestNG suite results available - final Slack report not sent");
			return;
		}
		if (suites.stream().allMatch(s -> AD_HOC_SUITE_NAME.equals(s.getName()))) {
			log.info("[SLACK] Ad-hoc class/method run (no suite XML) - final Slack report sent only for suite runs such as testng.xml");
			return;
		}
		try {
			Properties config = new Baseclass().prop;
			ReportToSlack.ExecutionSummary summary = summarize(suites, config, executionEnd);
			if (summary.total() == 0) {
				log.warn("[SLACK] No tests were executed - final Slack report not sent");
				return;
			}
			log.info("[SLACK] Summary: " + summary.execution + " | Total " + summary.total() + " | Passed " + summary.passed
					+ " | Failed " + summary.failed + " | Skipped " + summary.skipped + " | Pass rate " + summary.passRate() + "%");
			ReportToSlack.sendFinalReport(config, summary, currentReport);
		} catch (Exception e) {
			log.error("[SLACK FAIL] Unable to send final report: " + e.getClass().getSimpleName() + " - " + e.getMessage());
		}
	}

	// actual counts / failed tests of this execution from the TestNG results
	private ReportToSlack.ExecutionSummary summarize(List<ISuite> suites, Properties config, LocalDateTime executionEnd) {
		ReportToSlack.ExecutionSummary summary = new ReportToSlack.ExecutionSummary();
		summary.start = executionStart;
		summary.end = executionEnd;
		summary.environment = environment(config.getProperty("baseURL", ""));
		summary.account = config.getProperty("company_Name", "") + " (" + config.getProperty("username", "") + ")";
		List<String> executions = new ArrayList<>();
		for (ISuite suite : suites) {
			executions.add(executionType(suite));
			for (ISuiteResult suiteResult : suite.getResults().values()) {
				ITestContext context = suiteResult.getTestContext();
				summary.passed += context.getPassedTests().size();
				summary.skipped += context.getSkippedTests().size();
				summary.failed += context.getFailedTests().size() + context.getFailedButWithinSuccessPercentageTests().size();
				context.getFailedTests().getAllResults().stream()
						.sorted(Comparator.comparingLong(ITestResult::getStartMillis))
						.forEach(result -> summary.failedTests.add(describe(result)));
			}
		}
		summary.execution = String.join(" + ", executions);
		return summary;
	}

	// "Regression" / "Sanity" from the groups the suite runs, otherwise the suite name
	private static String executionType(ISuite suite) {
		List<String> groups = new ArrayList<>();
		suite.getXmlSuite().getTests().forEach(test -> groups.addAll(test.getIncludedGroups()));
		if (groups.contains("sanity") && !groups.contains("regression")) {
			return "Sanity";
		}
		if (groups.contains("regression")) {
			return "Regression";
		}
		return suite.getName();
	}

	// UAT / STAGING / QA from the application host (config baseURL), otherwise the host itself
	private static String environment(String baseUrl) {
		String host = baseUrl.replaceFirst("^[a-zA-Z]+://", "").replaceFirst("[/:].*$", "");
		String lower = host.toLowerCase();
		String name = lower.contains("uat") ? "UAT" : lower.contains("stag") ? "STAGING" : lower.contains("qa") ? "QA" : host;
		return host.isEmpty() || name.equals(host) ? host : name + " (" + host + ")";
	}

	// "Job Category > Create Job Category Test - <first line of the failure>"
	private static String describe(ITestResult result) {
		String module = result.getTestClass().getRealClass().getSimpleName().replaceFirst("^TC_", "").replace('_', ' ');
		String description = result.getMethod().getDescription();
		String test = description != null && !description.trim().isEmpty() ? description : result.getName();
		String error = "";
		if (result.getThrowable() != null) {
			String message = String.valueOf(result.getThrowable().getMessage()).split("\\R")[0].trim();
			error = result.getThrowable().getClass().getSimpleName() + (message.isEmpty() || "null".equals(message) ? "" : ": " + message);
			if (error.length() > 160) {
				error = error.substring(0, 157) + "...";
			}
		}
		return module + " > " + test + (error.isEmpty() ? "" : " - " + error);
	}
	// same lookup as Allure: system property (mvn test / surefire), then allure.properties on the classpath
	private Path resultsDirectory() {
		String directory = System.getProperty("allure.results.directory");
		if (directory == null) {
			Properties properties = new Properties();
			try (InputStream in = getClass().getClassLoader().getResourceAsStream("allure.properties")) {
				if (in != null) {
					properties.load(in);
				}
			} catch (IOException e) {
				// defaults below
			}
			directory = properties.getProperty("allure.results.directory", "allure-results");
		}
		Path path = Paths.get(directory);
		return path.isAbsolute() ? path : projectDir.resolve(path).normalize();
	}

	// Allure command line <version>: unpacked once from the local Maven repository into target/allure-commandline
	private Path allureCommandLine() throws IOException {
		String version = allureVersion();
		boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
		Path home = projectDir.resolve("target").resolve("allure-commandline");
		Path executable = home.resolve("allure-" + version).resolve("bin").resolve(windows ? "allure.bat" : "allure");
		if (Files.isRegularFile(executable)) {
			return executable;
		}
		String repository = System.getProperty("maven.repo.local",
				Paths.get(System.getProperty("user.home"), ".m2", "repository").toString());
		Path zip = Paths.get(repository, "io", "qameta", "allure", "allure-commandline", version,
				"allure-commandline-" + version + ".zip");
		if (!Files.isRegularFile(zip)) {
			throw new IOException("Allure command line not found: " + zip
					+ " - download it once with: mvn dependency:get -Dartifact=io.qameta.allure:allure-commandline:" + version + ":zip");
		}
		log.info("[REPORT] Unpacking Allure command line " + version + " into " + home);
		unzip(zip, home);
		executable.toFile().setExecutable(true);
		return executable;
	}

	// version of the allure-testng dependency on the classpath (pom.xml allure.version)
	private String allureVersion() {
		try (InputStream in = getClass().getClassLoader()
				.getResourceAsStream("META-INF/maven/io.qameta.allure/allure-testng/pom.properties")) {
			if (in != null) {
				Properties properties = new Properties();
				properties.load(in);
				return properties.getProperty("version", DEFAULT_ALLURE_VERSION);
			}
		} catch (IOException e) {
			// default below
		}
		return DEFAULT_ALLURE_VERSION;
	}

	private void generate(Path allure, Path results, Path output, boolean singleFile) throws IOException, InterruptedException {
		List<String> command = new ArrayList<>();
		if (allure.toString().endsWith(".bat")) {
			command.add("cmd.exe");
			command.add("/c");
		}
		command.add(allure.toString());
		command.add("generate");
		command.add(results.toString());
		command.add("--clean");
		if (singleFile) {
			command.add("--single-file");
		}
		command.add("-o");
		command.add(output.toString());

		ProcessBuilder builder = new ProcessBuilder(command).directory(projectDir.toFile()).redirectErrorStream(true);
		// run on the same Java as TestNG (the system JAVA_HOME may be missing or invalid)
		builder.environment().put("JAVA_HOME", System.getProperty("java.home"));
		Process process = builder.start();
		StringBuilder processOutput = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				processOutput.append(line).append(System.lineSeparator());
			}
		}
		if (!process.waitFor(GENERATE_TIMEOUT_MINUTES, TimeUnit.MINUTES)) {
			process.destroyForcibly();
			throw new IOException("allure generate timed out after " + GENERATE_TIMEOUT_MINUTES + " minutes");
		}
		if (process.exitValue() != 0 || !Files.isRegularFile(output.resolve("index.html"))) {
			throw new IOException("allure generate failed (exit " + process.exitValue() + "): " + processOutput.toString().trim());
		}
	}

	private static boolean isEmpty(Path directory) throws IOException {
		try (Stream<Path> files = Files.list(directory)) {
			return !files.findAny().isPresent();
		}
	}

	private static void deleteDirectory(Path directory) throws IOException {
		if (!Files.exists(directory)) {
			return;
		}
		try (Stream<Path> paths = Files.walk(directory)) {
			for (Path path : (Iterable<Path>) paths.sorted(Comparator.reverseOrder())::iterator) {
				Files.delete(path);
			}
		}
	}

	private static void copyDirectory(Path source, Path target) throws IOException {
		try (Stream<Path> paths = Files.walk(source)) {
			for (Path path : (Iterable<Path>) paths::iterator) {
				Path destination = target.resolve(source.relativize(path).toString());
				if (Files.isDirectory(path)) {
					Files.createDirectories(destination);
				} else {
					Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	private static void unzip(Path zip, Path target) throws IOException {
		try (ZipFile zipFile = new ZipFile(zip.toFile())) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				Path path = target.resolve(entry.getName()).normalize();
				if (!path.startsWith(target)) {
					throw new IOException("Invalid zip entry: " + entry.getName());
				}
				if (entry.isDirectory()) {
					Files.createDirectories(path);
				} else {
					Files.createDirectories(path.getParent());
					try (InputStream in = zipFile.getInputStream(entry)) {
						Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}
		}
	}
}
