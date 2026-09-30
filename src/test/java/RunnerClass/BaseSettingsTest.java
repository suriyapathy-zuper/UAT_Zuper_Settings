package RunnerClass;

import java.lang.reflect.Method;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import BaseTest.Baseclass;
import PageObject.DashboardPage;
import PageObject.SettingsPage;
import TestUtility.TestExecutionListener;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

/**
 * Common base for the Settings module test classes (User Management, Job Category, ...).
 * - ONE shared browser + login for the whole run (BaseTest.SuiteSession via TestExecutionListener):
 *   every class starts from the Dashboard and returns to it; failures get a screenshot attached to Allure
 * - Dashboard -> Settings navigation
 * - Execution logging through the Baseclass log4j2 logger (console + logs/automation-log.log):
 *   [MODULE START] / [TEST START] / [STEP n] / [ACTION] / [PASS] / [WARN] / [FAIL] / [TEST END] / [MODULE END]
 */
@Listeners({ TestExecutionListener.class, BaseSettingsTest.SkippedTestLogger.class })
public abstract class BaseSettingsTest extends Baseclass {

	protected DashboardPage dashboardPage;
	protected SettingsPage settingsPage;

	// Execution log state
	private String currentTestName;
	private int stepNumber;
	private String currentStep;
	private String failedExpectation;
	private String failedActual;

	// Name shown in [MODULE START] / [MODULE END]
	protected abstract String getSuiteName();

	@BeforeClass(alwaysRun = true)
	public void setUpPages() {
		logger.info("==================================================================");
		logger.info("[MODULE START] " + getSuiteName());
		// shared browser session - launched and logged in once by SuiteSession
		dashboardPage = new DashboardPage();
		settingsPage = new SettingsPage();
		logger.info("[TEST DATA] Environment: " + prop.getProperty("baseURL") + " | Company: " + prop.getProperty("company_Name")
				+ " | Browser: " + prop.getProperty("browser"));
	}

	@AfterClass(alwaysRun = true)
	public void endModule() {
		// browser stays open for the next test class - TestExecutionListener returns to the Dashboard
		logger.info("[MODULE END] " + getSuiteName());
		logger.info("==================================================================");
	}

	// ==========================================
	// Shared navigation
	// ==========================================
	// Every test class starts from the Dashboard of the shared, already logged-in session - then opens Settings home
	protected void openSettingsFromDashboard() {
		step("Starting from Dashboard (shared authenticated session)");
		verify(dashboardPage.isDashboardDisplayed(), "Dashboard is displayed - test starts from Dashboard",
				"Dashboard is not displayed at the start of the test");

		step("Navigating to Settings through the Profile menu");
		settingsPage.navigateToSettings();
		verify(settingsPage.isSettingsHomeDisplayed(), "Settings home page is displayed", "Settings home page is not displayed");
		// the dialer can open late after login and cover the right side of the page
		dashboardPage.minimizeZuperConnectDialer(2);
	}

	// ==========================================
	// Execution logging helpers
	// ==========================================
	@BeforeMethod(alwaysRun = true)
	public void logTestStart(Method method) {
		Test test = method.getAnnotation(Test.class);
		currentTestName = (test != null && !test.description().isEmpty()) ? test.description() : method.getName();
		stepNumber = 0;
		currentStep = "Test setup";
		failedExpectation = null;
		failedActual = null;
		logger.info("------------------------------------------------------------------");
		logger.info("[TEST START] " + currentTestName + " (" + method.getName() + ")");
	}

	@AfterMethod(alwaysRun = true)
	public void logTestEnd(ITestResult result) {
		long seconds = (result.getEndMillis() - result.getStartMillis()) / 1000;
		switch (result.getStatus()) {
			case ITestResult.SUCCESS:
				logger.info("[TEST END] " + currentTestName + " - PASSED (" + seconds + "s)");
				break;
			case ITestResult.FAILURE:
				Throwable error = result.getThrowable();
				logger.error("[FAIL] Test case : " + currentTestName + " (" + result.getMethod().getMethodName() + ")");
				logger.error("[FAIL] Failed at : [STEP " + stepNumber + "] " + currentStep);
				logger.error("[FAIL] Action    : " + Baseclass.getLastAction());
				if (failedExpectation != null) {
					logger.error("[FAIL] Expected  : " + failedExpectation);
					logger.error("[FAIL] Actual    : " + failedActual);
				}
				logger.error("[ERROR] " + (error == null ? "unknown" : error.getClass().getSimpleName() + " - " + firstLine(error.getMessage())));
				logger.error("[FAIL] Page URL  : " + currentUrl());
				logger.error("[TEST END] " + currentTestName + " - FAILED (" + seconds + "s)");
				break;
			default:
				logger.warn("[TEST END] " + currentTestName + " - SKIPPED");
		}
	}

	protected void step(String description) {
		stepNumber++;
		currentStep = description;
		logger.info("[STEP " + stepNumber + "] " + description);
		Allure.step(description);
	}

	protected void pass(String message) {
		logger.info("[PASS] " + message);
	}

	protected void warn(String message) {
		logger.warn("[WARN] " + message);
	}

	// Assert 'condition'; logs "[PASS] expected" or "[FAIL] Expected / Actual" before failing
	protected void verify(boolean condition, String expected, String actual) {
		if (!condition) {
			failedExpectation = expected;
			failedActual = actual;
			logger.error("[FAIL] Expected: " + expected);
			logger.error("[FAIL] Actual  : " + actual);
			Allure.step("FAILED - Expected: " + expected + " | Actual: " + actual, Status.FAILED);
			Assert.fail(actual);
		}
		logger.info("[PASS] " + expected);
	}

	protected void verifyEquals(Object actual, Object expected, String what) {
		if (!Objects.equals(actual, expected)) {
			failedExpectation = what + " = '" + expected + "'";
			failedActual = what + " = '" + actual + "'";
			logger.error("[FAIL] Expected: " + failedExpectation);
			logger.error("[FAIL] Actual  : " + failedActual);
			Allure.step("FAILED - Expected: " + failedExpectation + " | Actual: " + failedActual, Status.FAILED);
			Assert.assertEquals(actual, expected, what + " mismatch");
		}
		logger.info("[PASS] " + what + " = '" + actual + "'");
	}

	private String currentUrl() {
		try {
			return Objects.toString(Baseclass.getDriver().getCurrentUrl());
		} catch (Exception e) {
			return "n/a";
		}
	}

	// First line of an exception message for the log: without the ❌ marker (prints as '?' on the Windows console),
	// with auth tokens masked (Selenium messages can contain element HTML such as the dialer iframe URL) and shortened
	protected static String firstLine(String message) {
		if (message == null) {
			return "";
		}
		String line = message.split("\\R")[0].replace("❌", "").trim()
				.replaceAll("(?i)([a-z_]*token=)[^&\"'\\s>]+", "$1*****");
		return line.length() > 500 ? line.substring(0, 500) + "..." : line;
	}

	// Tests skipped because a dependency failed get no @BeforeMethod/@AfterMethod - log them here
	public static class SkippedTestLogger implements ITestListener {

		@Override
		public void onTestSkipped(ITestResult result) {
			// listeners are suite-wide in TestNG - log only skipped tests of BaseSettingsTest subclasses
			if (!BaseSettingsTest.class.isAssignableFrom(result.getTestClass().getRealClass())) {
				return;
			}
			Logger log = LogManager.getLogger(result.getTestClass().getRealClass());
			String name = result.getMethod().getDescription() != null ? result.getMethod().getDescription() : result.getName();
			String[] dependsOn = result.getMethod().getMethodsDependedUpon();
			String reason = dependsOn.length > 0
					? "depends on failed/skipped test: " + String.join(", ", dependsOn).replaceAll("[\\w.]+\\.", "")
					: (result.getThrowable() == null ? "skipped by TestNG" : firstLine(result.getThrowable().getMessage()));
			log.warn("[TEST SKIPPED] " + name + " (" + result.getName() + ") - " + reason);
			log.warn("[TEST END] " + name + " - SKIPPED");
		}
	}
}
