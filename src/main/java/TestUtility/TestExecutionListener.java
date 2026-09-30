package TestUtility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.IClassListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestResult;

import BaseTest.Baseclass;
import BaseTest.SuiteSession;
import io.qameta.allure.Allure;

/**
 * Central TestNG listener for all existing and future test classes:
 *
 *  Suite start  -> SuiteSession.start()               ONE browser launch + ONE company login + Dashboard verified
 *  Class start  -> SuiteSession.returnToDashboard()   every test class starts from the Dashboard
 *  Test failure -> screenshot saved to screenshots/FAILED_<Class>_<test>_<timestamp>.png
 *                  + attached (with failure details) to the Allure report
 *  Class end    -> SuiteSession.returnToDashboard()   every test class returns to the Dashboard (also after failures)
 *  Suite end    -> SuiteSession.end()                 ONE logout + ONE browser close
 *
 * Registered on every test class via @Listeners; the callbacks are idempotent, so the listener is safe if TestNG
 * receives it more than once (several classes / testng.xml).
 */
public class TestExecutionListener implements ISuiteListener, IClassListener, IInvokedMethodListener {

	private static final Logger log = LogManager.getLogger(TestExecutionListener.class);
	public static final String SCREENSHOT_DIRECTORY = "screenshots";
	private static final String SCREENSHOT_ATTRIBUTE = "failureScreenshot";
	private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

	private static final Set<String> startedClasses = new HashSet<>();
	private static final Set<String> finishedClasses = new HashSet<>();
	private static boolean suiteFinished = false;

	// ==========================================
	// Suite: one browser + one login / one logout + one browser close
	// ==========================================
	@Override
	public synchronized void onStart(ISuite suite) {
		if (!SuiteSession.isStarted()) {
			log.info("[SUITE] Starting '" + suite.getName() + "'");
			SuiteSession.start();
		}
	}

	@Override
	public synchronized void onFinish(ISuite suite) {
		if (!suiteFinished && SuiteSession.isStarted()) {
			suiteFinished = true;
			log.info("[SUITE] Finished '" + suite.getName() + "'");
			SuiteSession.end();
		}
	}

	// ==========================================
	// Test class: start from Dashboard / return to Dashboard
	// ==========================================
	@Override
	public synchronized void onBeforeClass(ITestClass testClass) {
		String name = testClass.getRealClass().getName();
		if (!startedClasses.add(name)) {
			return;
		}
		String label = testLabel(testClass);
		log.info("==================================================================");
		log.info("[TEST START] " + label);
		log.info("[STEP] Starting from Dashboard (shared browser / authenticated session)");
		try {
			SuiteSession.returnToDashboard();
			log.info("[PASS] Dashboard loaded - '" + label + "' starts from Dashboard");
		} catch (Exception e) {
			// the test class' own Dashboard verification will fail and report it
			log.error("[FAIL] Dashboard could not be loaded before '" + label + "': " + firstLine(e));
		}
	}

	@Override
	public synchronized void onAfterClass(ITestClass testClass) {
		String name = testClass.getRealClass().getName();
		if (!finishedClasses.add(name)) {
			return;
		}
		String label = testLabel(testClass);
		log.info("[STEP] Returning to Dashboard after '" + label + "'");
		try {
			SuiteSession.returnToDashboard();
			log.info("[PASS] Dashboard loaded - ready for the next test");
		} catch (Exception e) {
			log.error("[FAIL] Could not return to Dashboard after '" + label + "': " + firstLine(e));
		}
		log.info("[TEST END] " + label);
		log.info("==================================================================");
	}

	// ==========================================
	// Failed test: screenshot + Allure attachment
	// ==========================================
	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		if (!method.isTestMethod() || result.getStatus() != ITestResult.FAILURE || result.getAttribute(SCREENSHOT_ATTRIBUTE) != null) {
			return;
		}
		String testName = result.getTestClass().getRealClass().getSimpleName().replaceFirst("^TC_", "") + "_" + result.getMethod().getMethodName();
		String fileName = "FAILED_" + testName + "_" + LocalDateTime.now().format(TIMESTAMP);
		result.setAttribute(SCREENSHOT_ATTRIBUTE, fileName);
		WebDriver driver = Baseclass.getDriver() != null ? Baseclass.getDriver() : SuiteSession.getDriver();

		Allure.addAttachment("Failure details", "text/plain", failureDetails(result, driver), "txt");
		if (driver == null) {
			log.error("[FAIL] No browser session - screenshot not captured for " + testName);
			return;
		}
		try {
			byte[] image = Non_WebDriver_Util.captureScreenshot(driver, SCREENSHOT_DIRECTORY, fileName);
			Allure.getLifecycle().addAttachment("Failure Screenshot - " + testName, "image/png", "png", image);
			log.error("[FAIL] Screenshot captured: " + SCREENSHOT_DIRECTORY + "/" + fileName + ".png (attached to Allure)");
		} catch (Exception e) {
			log.error("[FAIL] Screenshot could not be captured for " + testName + ": " + firstLine(e));
		}
	}

	private static String failureDetails(ITestResult result, WebDriver driver) {
		StringBuilder details = new StringBuilder();
		details.append("Test class : ").append(result.getTestClass().getRealClass().getSimpleName()).append('\n');
		details.append("Test       : ").append(result.getMethod().getMethodName());
		if (result.getMethod().getDescription() != null) {
			details.append(" (").append(result.getMethod().getDescription()).append(')');
		}
		details.append('\n');
		details.append("Last action: ").append(Baseclass.getLastAction()).append('\n');
		try {
			details.append("Page URL   : ").append(driver == null ? "n/a" : driver.getCurrentUrl()).append('\n');
		} catch (Exception e) {
			details.append("Page URL   : n/a\n");
		}
		Throwable error = result.getThrowable();
		if (error != null) {
			StringWriter stack = new StringWriter();
			error.printStackTrace(new PrintWriter(stack));
			details.append("\nException  : ").append(maskTokens(stack.toString()));
		}
		return details.toString();
	}

	// 'TC_Settings_UserManagement' -> 'Settings UserManagement'
	private static String testLabel(ITestClass testClass) {
		return testClass.getRealClass().getSimpleName().replaceFirst("^TC_", "").replace('_', ' ');
	}

	private static String firstLine(Exception e) {
		return e.getClass().getSimpleName() + " - " + maskTokens(String.valueOf(e.getMessage()).split("\\R")[0]);
	}

	// Selenium messages can contain element HTML such as the dialer iframe URL with an auth token
	private static String maskTokens(String text) {
		return text.replaceAll("(?i)([a-z_]*token=)[^&\"'\\s>]+", "$1*****");
	}
}
