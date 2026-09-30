package RunnerClass;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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
import PageObject.JobNotificationPage;
import PageObject.SettingsPage;
import TestUtility.TestExecutionListener;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

/**
 * Settings > Jobs > Job Notifications (Internal Notifications > Job Reminder) - complete notification lifecycle:
 * Login -> Settings -> Job Notification -> Create -> Verify -> Edit -> Update -> Verify -> Delete -> Verify
 * plus required-field and delete-confirmation checks.
 *
 * The test reminder is unique per run (timestamp suffix) and is removed in @AfterClass if a test fails midway.
 *
 * Execution log format (Baseclass log4j2 logger -> console + logs/automation-log.log):
 * [MODULE START] / [TEST START] / [STEP n] / [ACTION] / [VERIFY] / [PASS] / [WARN] / [FAIL] / [TEST END] / [MODULE END]
 */
@Listeners({ TestExecutionListener.class, TC_Job_Notification.SkippedTestLogger.class })
public class TC_Job_Notification extends Baseclass {

	private DashboardPage dashboardPage;
	private SettingsPage settingsPage;
	private JobNotificationPage jobNotificationPage;

	// Test data - unique per run so the automation reminder is always identifiable
	private final String uniqueSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
	private String reminderName;
	private String updatedReminderName;
	private String remindType;
	private String remindBefore;
	private String updatedRemindBefore;
	private String alertTemplate;
	private String updatedAlertTemplate;
	private String currentReminderName;   // name currently saved in the application - changes after edit
	private int reminderCountBeforeDelete = -1;

	private boolean reminderCreated = false;
	private boolean reminderDeleted = false;

	// Execution log state
	private String currentTestName;
	private int stepNumber;
	private String currentStep;
	private String failedExpectation;
	private String failedActual;

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		logger.info("==================================================================");
		logger.info("[MODULE START] Settings > Job Notification");
		// shared browser session - launched and logged in once by SuiteSession (TestExecutionListener)
		dashboardPage = new DashboardPage();
		settingsPage = new SettingsPage();
		jobNotificationPage = new JobNotificationPage();

		reminderName = prop.getProperty("jobNotification_NamePrefix") + " " + uniqueSuffix;
		updatedReminderName = prop.getProperty("jobNotification_UpdatedNamePrefix") + " " + uniqueSuffix;
		remindType = prop.getProperty("jobNotification_RemindType");
		remindBefore = prop.getProperty("jobNotification_RemindBefore");
		updatedRemindBefore = prop.getProperty("jobNotification_UpdatedRemindBefore");
		alertTemplate = prop.getProperty("jobNotification_AlertTemplate");
		updatedAlertTemplate = prop.getProperty("jobNotification_UpdatedAlertTemplate");

		logger.info("[TEST DATA] Environment: " + prop.getProperty("baseURL") + " | Company: " + prop.getProperty("company_Name")
				+ " | Browser: " + prop.getProperty("browser"));
		logger.info("[TEST DATA] Job Reminder -> Name: '" + reminderName + "' | Remind Type: " + remindType + " | Remind Before: "
				+ remindBefore + " min | Alert Template: '" + alertTemplate + "'");
		logger.info("[TEST DATA] Update values -> Name: '" + updatedReminderName + "' | Remind Before: " + updatedRemindBefore
				+ " min | Alert Template: '" + updatedAlertTemplate + "'");
		logger.info("==================================================================");
	}

	@Test(groups = { "sanity", "regression" }, priority = 1, description = "Navigate to Job Notification Test")
	public void verify_NavigateToJobNotification() {
		step("Starting from Dashboard (shared authenticated session)");
		verify(dashboardPage.isDashboardDisplayed(), "Dashboard is displayed - test starts from Dashboard",
				"Dashboard is not displayed at the start of the test");

		step("Navigating to Settings through the Profile menu");
		settingsPage.navigateToSettings();
		verify(settingsPage.isSettingsHomeDisplayed(), "Settings home page is displayed", "Settings home page is not displayed");
		// the dialer can open late after login and cover the right side of the page
		dashboardPage.minimizeZuperConnectDialer(2);

		step("Opening Job Notification (Settings > Jobs > Job Notifications > Internal Notifications)");
		settingsPage.openJobNotifications();
		jobNotificationPage.waitForJobNotificationPageToLoad();
		jobNotificationPage.openInternalNotificationsTab();
		verify(jobNotificationPage.isJobNotificationPageDisplayed(), "Job Notifications page loaded - Job Reminder section displayed",
				"Job Notifications page is not displayed");
	}

	@Test(groups = { "regression" }, priority = 2, dependsOnMethods = "verify_NavigateToJobNotification",
			description = "Create Job Notification - Required Field Validation Test")
	public void verify_CreateJobNotificationRequiredFieldValidation() {
		step("Verifying the 'New Job Reminder' option is available");
		verify(jobNotificationPage.isNewJobReminderButtonAvailable(), "'New Job Reminder' button is displayed and enabled",
				"'New Job Reminder' button is not available");

		step("Clicking New Job Reminder");
		jobNotificationPage.clickNewJobReminder();
		verifyEquals(jobNotificationPage.getDrawerTitle(), "New Job Reminder", "Drawer title");

		step("Clicking Save Reminder with the required fields empty");
		jobNotificationPage.clickSaveReminder();

		step("Verifying required field validation messages");
		verifyEquals(jobNotificationPage.getValidationMessageCount(), 3, "Number of 'This field is required' messages");
		for (String field : new String[] { "Reminder Name", "Remind Type", "Alert Template" }) {
			verifyEquals(jobNotificationPage.getValidationMessage(field), "This field is required", "Validation message for '" + field + "'");
		}
		verify(jobNotificationPage.isDrawerOpen(), "Empty notification was not accepted - 'New Job Reminder' drawer is still open",
				"Drawer closed although required fields are empty");

		step("Cancelling the New Job Reminder drawer");
		jobNotificationPage.cancelDrawer();
		pass("Required field validation works for Reminder Name, Remind Type and Alert Template");
	}

	@Test(groups = { "sanity", "regression" }, priority = 3, dependsOnMethods = "verify_NavigateToJobNotification", description = "Create Job Notification Test")
	public void verify_CreateJobNotification() {
		step("Clicking Create Notification (New Job Reminder)");
		jobNotificationPage.clickNewJobReminder();

		step("Entering notification details -> Name: '" + reminderName + "', Remind Type: " + remindType
				+ ", Remind Before: " + remindBefore + " min, Alert Template: '" + alertTemplate + "'");
		jobNotificationPage.enterJobReminderDetails(reminderName, remindType, remindBefore, alertTemplate);

		step("Saving the notification (Save Reminder)");
		jobNotificationPage.clickSaveReminder();

		step("Verifying Job Notification creation");
		verify(jobNotificationPage.isToastDisplayed("Job Reminder created successfully"),
				"Notification created successfully (toast 'Job Reminder created successfully' displayed)",
				"Success toast 'Job Reminder created successfully' was not displayed");
		reminderCreated = true;
		currentReminderName = reminderName;
		verify(jobNotificationPage.isDrawerClosed(), "New Job Reminder drawer closed", "New Job Reminder drawer is still open");

		step("Verifying the created notification in the Job Reminder list");
		verify(jobNotificationPage.isJobReminderDisplayed(reminderName), "Notification displayed in the Job Reminder list",
				"Created notification '" + reminderName + "' was not displayed in the Job Reminder list");
		Map<String, String> row = jobNotificationPage.getJobReminderRowDetails(reminderName);
		verifyEquals(row.get("name"), reminderName, "Reminder Name");
		verifyEquals(row.get("remindAt"), "Before " + remindBefore + " Mins", "Remind At");
		verifyEquals(row.get("remindTo"), "All Assigned Users", "Remind To");
		verifyEquals(row.get("status"), "Active", "Status");

		step("Verifying the row actions offered for the notification");
		List<String> menuOptions = jobNotificationPage.getRowMenuOptions(reminderName);
		verify(menuOptions.contains("Edit Job Reminder"), "'Edit Job Reminder' option is offered", "'Edit Job Reminder' option missing");
		verify(menuOptions.contains("Delete Job Reminder"), "'Delete Job Reminder' option is offered (Delete is supported)",
				"'Delete Job Reminder' option missing");
	}

	@Test(groups = { "regression" }, priority = 4, dependsOnMethods = "verify_CreateJobNotification", description = "Edit Job Notification Test")
	public void verify_EditJobNotification() {
		step("Locating created notification '" + reminderName + "'");
		jobNotificationPage.openJobNotificationPage();
		verify(jobNotificationPage.isJobReminderDisplayed(reminderName), "Created notification found in the Job Reminder list",
				"Created notification '" + reminderName + "' was not found");

		step("Opening Edit (Edit Job Reminder)");
		jobNotificationPage.clickEditJobReminder(reminderName);
		verifyEquals(jobNotificationPage.getDrawerTitle(), "Edit Job Reminder", "Drawer title");

		step("Verifying the drawer shows the saved values");
		Map<String, String> savedValues = jobNotificationPage.getDrawerValues();
		verifyEquals(savedValues.get("name"), reminderName, "Edit drawer Reminder Name");
		verifyEquals(savedValues.get("remindType"), remindType, "Edit drawer Remind Type");
		verifyEquals(savedValues.get("remindBefore"), remindBefore, "Edit drawer Remind Before");
		verifyEquals(savedValues.get("alertTemplate"), alertTemplate, "Edit drawer Alert Template");
		verifyEquals(savedValues.get("notificationType"), "Push", "Edit drawer Notification Type (default)");
		verifyEquals(savedValues.get("sendReminderTo"), "All Assigned Users", "Edit drawer Send Reminder To (default)");

		step("Updating notification -> Name: '" + updatedReminderName + "', Remind Before: " + updatedRemindBefore
				+ " min, Alert Template: '" + updatedAlertTemplate + "'");
		jobNotificationPage.updateJobReminderDetails(updatedReminderName, updatedRemindBefore, updatedAlertTemplate);

		step("Saving the update (Update Reminder)");
		jobNotificationPage.clickUpdateReminder();
		verify(jobNotificationPage.isToastDisplayed("Job Reminder updated successfully"),
				"Notification updated successfully (toast 'Job Reminder updated successfully' displayed)",
				"Success toast 'Job Reminder updated successfully' was not displayed");
		currentReminderName = updatedReminderName;
		verify(jobNotificationPage.isDrawerClosed(), "Edit Job Reminder drawer closed", "Edit Job Reminder drawer is still open");

		step("Verifying the updated notification in the list");
		verifyUpdatedReminderRow();
		verify(jobNotificationPage.getMatchingReminderCount(reminderName) == 0, "Old name '" + reminderName + "' was replaced by the updated name",
				"Old notification name '" + reminderName + "' is still displayed");

		step("Reloading the page and verifying the update is persisted");
		jobNotificationPage.refreshJobNotificationPage();
		verifyUpdatedReminderRow();

		step("Re-opening Edit and verifying the saved values");
		jobNotificationPage.clickEditJobReminder(updatedReminderName);
		Map<String, String> updatedValues = jobNotificationPage.getDrawerValues();
		verifyEquals(updatedValues.get("name"), updatedReminderName, "Persisted Reminder Name");
		verifyEquals(updatedValues.get("remindBefore"), updatedRemindBefore, "Persisted Remind Before");
		verifyEquals(updatedValues.get("alertTemplate"), updatedAlertTemplate, "Persisted Alert Template");
		verifyEquals(updatedValues.get("remindType"), remindType, "Persisted Remind Type (unchanged)");
		jobNotificationPage.cancelDrawer();
		pass("Updated notification values are persisted after reload");
	}

	@Test(groups = { "regression" }, priority = 5, dependsOnMethods = "verify_CreateJobNotification", description = "Cancel Delete Job Notification Test")
	public void verify_CancelDeleteKeepsJobNotification() {
		step("Locating test notification '" + currentReminderName + "'");
		jobNotificationPage.openJobNotificationPage();
		verify(jobNotificationPage.isJobReminderDisplayed(currentReminderName), "Test notification found in the Job Reminder list",
				"Test notification '" + currentReminderName + "' was not found");

		step("Clicking Delete (Delete Job Reminder)");
		String dialogText = jobNotificationPage.clickDeleteJobReminder(currentReminderName);
		verify(dialogText.contains("Are you sure want to delete this Job Reminder \"" + currentReminderName + "\""),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Clicking Cancel on the delete confirmation");
		verify(jobNotificationPage.cancelDelete(), "Delete confirmation dialog closed on Cancel",
				"'Cancel' did not close the 'Delete Job Reminder' confirmation dialog");

		step("Verifying the notification was not deleted");
		jobNotificationPage.refreshJobNotificationPage();
		verify(jobNotificationPage.isJobReminderDisplayed(currentReminderName), "Notification still exists after cancelling the delete",
				"Notification '" + currentReminderName + "' was deleted although delete was cancelled");
	}

	@Test(groups = { "sanity", "regression" }, priority = 6, dependsOnMethods = "verify_CreateJobNotification", description = "Delete Job Notification Test")
	public void verify_DeleteJobNotification() {
		step("Locating test notification '" + currentReminderName + "'");
		jobNotificationPage.openJobNotificationPage();
		verify(jobNotificationPage.isJobReminderDisplayed(currentReminderName), "Test notification found in the Job Reminder list",
				"Test notification '" + currentReminderName + "' was not found");
		reminderCountBeforeDelete = jobNotificationPage.getJobReminderCount();

		step("Clicking Delete (Delete Job Reminder)");
		String dialogText = jobNotificationPage.clickDeleteJobReminder(currentReminderName);

		step("Delete confirmation dialog displayed");
		verify(dialogText.contains("Are you sure want to delete this Job Reminder \"" + currentReminderName + "\""),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Confirming deletion (Delete)");
		jobNotificationPage.confirmDelete();
		verify(jobNotificationPage.isToastDisplayed("Job Reminder deleted successfully"),
				"Notification deleted successfully (toast 'Job Reminder deleted successfully' displayed)",
				"Success toast 'Job Reminder deleted successfully' was not displayed");
		reminderDeleted = true;

		step("Verifying Job Notification deletion");
		jobNotificationPage.refreshJobNotificationPage();
		verify(jobNotificationPage.isJobReminderRemoved(currentReminderName),
				"Deleted notification is no longer displayed in the Job Reminder list",
				"Deleted notification '" + currentReminderName + "' is still displayed in the Job Reminder list");
		int reminderCountAfterDelete = jobNotificationPage.getJobReminderCount();
		if (reminderCountBeforeDelete >= 0 && reminderCountAfterDelete >= 0) {
			verifyEquals(reminderCountAfterDelete, reminderCountBeforeDelete - 1, "Job Reminder count after delete");
		} else {
			logger.warn("[WARN] Job Reminder count badge could not be read - count check skipped");
		}
	}

	private void verifyUpdatedReminderRow() {
		verify(jobNotificationPage.isJobReminderDisplayed(updatedReminderName), "Updated notification displayed in the Job Reminder list",
				"Updated notification '" + updatedReminderName + "' was not displayed");
		Map<String, String> row = jobNotificationPage.getJobReminderRowDetails(updatedReminderName);
		verifyEquals(row.get("name"), updatedReminderName, "Updated Reminder Name");
		verifyEquals(row.get("remindAt"), "Before " + updatedRemindBefore + " Mins", "Updated Remind At");
		verifyEquals(row.get("status"), "Active", "Status (unchanged)");
	}

	// Best-effort cleanup so a failed run does not leave the automation reminder behind
	private void cleanUpTestReminder() {
		if (!reminderCreated || reminderDeleted) {
			logger.info("[CLEANUP] No cleanup needed - automation Job Reminder " + (reminderCreated ? "already deleted" : "was not created"));
			return;
		}
		try {
			logger.info("[CLEANUP] Test Job Reminder was not deleted by the tests - cleaning up: " + currentReminderName);
			jobNotificationPage.openJobNotificationPage();
			for (String name : new String[] { currentReminderName, reminderName, updatedReminderName }) {
				if (jobNotificationPage.getMatchingReminderCount(name) > 0) {
					jobNotificationPage.clickDeleteJobReminder(name);
					jobNotificationPage.confirmDelete();
					logger.info("[CLEANUP] Job Reminder '" + name + "' deleted: " + jobNotificationPage.isToastDisplayed("Job Reminder deleted successfully"));
					return;
				}
			}
			logger.info("[CLEANUP] Test Job Reminder not found - nothing to clean up");
		} catch (Exception e) {
			logger.error("[CLEANUP] Cleanup failed - delete Job Reminder manually: " + currentReminderName + " | " + firstLine(e.getMessage()));
		}
	}

	@AfterClass(alwaysRun = true)
	public void setdown() {
		cleanUpTestReminder();
		logger.info("[MODULE END] Settings > Job Notification");
		logger.info("==================================================================");
	}

	// ==========================================
	// Execution logging (Baseclass logger)
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

	private void step(String description) {
		stepNumber++;
		currentStep = description;
		logger.info("[STEP " + stepNumber + "] " + description);
		Allure.step(description);
	}

	private void pass(String message) {
		logger.info("[PASS] " + message);
	}

	// Logs "[VERIFY] expected", asserts 'condition', then logs "[PASS] expected" or "[FAIL] Expected / Actual" before failing
	private void verify(boolean condition, String expected, String actual) {
		logger.info("[VERIFY] " + expected);
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

	private void verifyEquals(Object actual, Object expected, String what) {
		logger.info("[VERIFY] " + what + " = '" + expected + "'");
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
	private static String firstLine(String message) {
		if (message == null) {
			return "";
		}
		String line = message.split("\\R")[0].replace("❌", "").trim()
				.replaceAll("(?i)([a-z_]*token=)[^&\"'\\s>]+", "$1*****");
		return line.length() > 500 ? line.substring(0, 500) + "..." : line;
	}

	// Tests skipped because a dependency failed get no @BeforeMethod/@AfterMethod - log them here
	public static class SkippedTestLogger implements ITestListener {
		private static final Logger log = LogManager.getLogger(TC_Job_Notification.class);

		@Override
		public void onTestSkipped(ITestResult result) {
			// listeners are suite-wide in TestNG - log only this class's skipped tests
			if (result.getTestClass().getRealClass() != TC_Job_Notification.class) {
				return;
			}
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
