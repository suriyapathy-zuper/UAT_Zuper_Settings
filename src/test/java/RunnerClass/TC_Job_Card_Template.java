package RunnerClass;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import PageObject.JobCardTemplatePage;
import PageObject.SettingsPage;
import TestUtility.TestExecutionListener;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

/**
 * Settings > Jobs > Job Card Templates - complete Job Card Template lifecycle:
 * Login -> Settings -> Job Card Template -> Create -> Verify -> Edit -> Update -> Verify -> Delete -> Verify
 * plus required-field and delete-confirmation checks.
 *
 * The test template is unique per run (timestamp suffix) and is removed in @AfterClass if a test fails midway.
 *
 * Execution log format (Baseclass log4j2 logger -> console + logs/automation-log.log):
 * [MODULE START] / [TEST START] / [STEP n] / [ACTION] / [VERIFY] / [PASS] / [WARN] / [FAIL] / [TEST END] / [MODULE END]
 */
@Listeners({ TestExecutionListener.class, TC_Job_Card_Template.SkippedTestLogger.class })
public class TC_Job_Card_Template extends Baseclass {

	private DashboardPage dashboardPage;
	private SettingsPage settingsPage;
	private JobCardTemplatePage jobCardTemplatePage;

	// Test data - unique per run so the automation template is always identifiable
	private final String uniqueSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
	private String templateName;
	private String updatedTemplateName;
	private String description;
	private String updatedDescription;
	private String jobCategory;
	private String format;
	private String orientation;
	private String updatedOrientation;
	private String border;
	private String content;
	private String currentTemplateName;   // name currently saved in the application - changes after edit

	private boolean templateCreated = false;
	private boolean templateDeleted = false;

	// Execution log state
	private String currentTestName;
	private int stepNumber;
	private String currentStep;
	private String failedExpectation;
	private String failedActual;

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		logger.info("==================================================================");
		logger.info("[MODULE START] Settings > Job Card Template");
		// shared browser session - launched and logged in once by SuiteSession (TestExecutionListener)
		dashboardPage = new DashboardPage();
		settingsPage = new SettingsPage();
		jobCardTemplatePage = new JobCardTemplatePage();

		templateName = prop.getProperty("jobCardTemplate_NamePrefix") + " " + uniqueSuffix;
		updatedTemplateName = prop.getProperty("jobCardTemplate_UpdatedNamePrefix") + " " + uniqueSuffix;
		description = prop.getProperty("jobCardTemplate_Description") + " " + uniqueSuffix;
		updatedDescription = prop.getProperty("jobCardTemplate_UpdatedDescription") + " " + uniqueSuffix;
		jobCategory = prop.getProperty("jobCardTemplate_JobCategory");
		format = prop.getProperty("jobCardTemplate_Format");
		orientation = prop.getProperty("jobCardTemplate_Orientation");
		updatedOrientation = prop.getProperty("jobCardTemplate_UpdatedOrientation");
		border = prop.getProperty("jobCardTemplate_Border");
		content = prop.getProperty("jobCardTemplate_Content");

		logger.info("[TEST DATA] Environment: " + prop.getProperty("baseURL") + " | Company: " + prop.getProperty("company_Name")
				+ " | Browser: " + prop.getProperty("browser"));
		logger.info("[TEST DATA] Job Card Template -> Name: '" + templateName + "' | Job Category: " + jobCategory
				+ " | Format: " + format + " | Orientation: " + orientation + " | Border: " + border);
		logger.info("[TEST DATA] Update values -> Name: '" + updatedTemplateName + "' | Description: '" + updatedDescription
				+ "' | Orientation: " + updatedOrientation);
		logger.info("==================================================================");
	}

	@Test(groups = { "sanity", "regression" }, priority = 1, description = "Navigate to Job Card Template Test")
	public void verify_NavigateToJobCardTemplate() {
		step("Starting from Dashboard (shared authenticated session)");
		verify(dashboardPage.isDashboardDisplayed(), "Dashboard is displayed - test starts from Dashboard",
				"Dashboard is not displayed at the start of the test");

		step("Navigating to Settings through the Profile menu");
		settingsPage.navigateToSettings();
		verify(settingsPage.isSettingsHomeDisplayed(), "Settings home page is displayed", "Settings home page is not displayed");
		// the dialer can open late after login and cover the right side of the page
		dashboardPage.minimizeZuperConnectDialer(2);

		step("Opening Job Card Template section (Settings > Jobs > Job Card Templates)");
		settingsPage.openJobCardTemplates();
		jobCardTemplatePage.waitForJobCardTemplatePageToLoad();
		verify(jobCardTemplatePage.isJobCardTemplatePageDisplayed(), "Job Card Templates page loaded successfully",
				"Job Card Templates page is not displayed");
	}

	@Test(groups = { "regression" }, priority = 2, dependsOnMethods = "verify_NavigateToJobCardTemplate",
			description = "Create Job Card Template - Required Field Validation Test")
	public void verify_CreateTemplateRequiredFieldValidation() {
		step("Verifying the 'New Template' option is available");
		verify(jobCardTemplatePage.isNewTemplateButtonAvailable(), "'New Template' button is displayed and enabled",
				"'New Template' button is not available");

		step("Clicking New Template");
		jobCardTemplatePage.clickNewTemplate();
		verifyEquals(jobCardTemplatePage.getCreateDialogTitle(), "New Job Card Template", "Dialog title");

		step("Clicking Proceed with all fields empty");
		jobCardTemplatePage.clickProceed();

		step("Verifying required field validation messages");
		verifyEquals(jobCardTemplatePage.getValidationMessageCount(), 9, "Number of 'This field is required' messages (4 of them for Border)");
		for (String field : new String[] { "Template Name", "Job Category", "Template Description", "Format", "Orientation", "Border" }) {
			verifyEquals(jobCardTemplatePage.getValidationMessage(field), "This field is required", "Validation message for '" + field + "'");
		}
		verify(jobCardTemplatePage.isCreateDialogOpen(), "Empty template was not accepted - 'New Job Card Template' dialog is still open",
				"Dialog closed although required fields are empty");

		step("Cancelling the 'New Job Card Template' dialog");
		jobCardTemplatePage.cancelCreateDialog();
		pass("Required field validation works for all mandatory template fields");
	}

	@Test(groups = { "sanity", "regression" }, priority = 3, dependsOnMethods = "verify_NavigateToJobCardTemplate", description = "Create Job Card Template Test")
	public void verify_CreateJobCardTemplate() {
		step("Clicking Create Template (New Template)");
		jobCardTemplatePage.clickNewTemplate();

		step("Entering template details -> Name: '" + templateName + "', Job Category: " + jobCategory + ", Format: " + format
				+ ", Orientation: " + orientation + ", Border: " + border);
		jobCardTemplatePage.enterTemplateDetails(templateName, jobCategory, description, format, orientation, border);

		step("Clicking Proceed to open the template designer");
		jobCardTemplatePage.clickProceed();
		jobCardTemplatePage.waitForTemplateDesigner();
		verifyEquals(jobCardTemplatePage.getDesignerHeaderText(), templateName, "Template designer header");

		step("Entering template content: '" + content + "'");
		jobCardTemplatePage.enterTemplateContent(content);
		verifyEquals(jobCardTemplatePage.getTemplateContentText(), content, "Template content in the editor");

		step("Saving the template");
		jobCardTemplatePage.clickSaveTemplate();

		step("Verifying Job Card Template creation");
		verify(jobCardTemplatePage.isToastDisplayed("Job Template created successfully", 30),
				"Job Card Template created successfully (toast 'Job Template created successfully' displayed)",
				"Success toast 'Job Template created successfully' was not displayed");
		templateCreated = true;
		currentTemplateName = templateName;
		verify(jobCardTemplatePage.waitForTemplateDesignerToClose(), "Template designer closed after saving",
				"Template designer is still open after saving");

		step("Searching for the created template in the list");
		jobCardTemplatePage.searchTemplate(templateName);
		verify(jobCardTemplatePage.isTemplateDisplayed(templateName), "Created Job Card Template is displayed in the list",
				"Created Job Card Template '" + templateName + "' was not displayed in the list");

		step("Verifying the created template details in the list");
		Map<String, String> row = jobCardTemplatePage.getTemplateRowDetails(templateName);
		verifyEquals(row.get("name"), templateName, "Template Name");
		verifyEquals(row.get("description"), description, "Template Description");
		verify(!row.get("createdOn").isEmpty(), "Created On = '" + row.get("createdOn") + "'", "Created On is empty");
	}

	@Test(groups = { "regression" }, priority = 4, dependsOnMethods = "verify_CreateJobCardTemplate", description = "Edit Job Card Template Test")
	public void verify_EditJobCardTemplate() {
		step("Locating created Job Card Template '" + templateName + "'");
		jobCardTemplatePage.openJobCardTemplateList();
		jobCardTemplatePage.searchTemplate(templateName);
		verify(jobCardTemplatePage.isTemplateDisplayed(templateName), "Created Job Card Template found in the list",
				"Created Job Card Template '" + templateName + "' was not found");

		step("Clicking Edit");
		jobCardTemplatePage.clickEditTemplate(templateName);

		step("Verifying the template designer shows the saved values");
		Map<String, String> savedValues = jobCardTemplatePage.getTemplateFormValues();
		verifyEquals(savedValues.get("name"), templateName, "Edit form Template Name");
		verifyEquals(savedValues.get("description"), description, "Edit form Template Description");
		verifyEquals(savedValues.get("jobCategory"), jobCategory, "Edit form Job Category");
		verifyEquals(savedValues.get("format"), format, "Edit form Format");
		verifyEquals(savedValues.get("orientation"), orientation, "Edit form Orientation");
		verifyEquals(savedValues.get("content"), content, "Edit form template content");

		step("Updating template -> Name: '" + updatedTemplateName + "', Description: '" + updatedDescription
				+ "', Orientation: " + updatedOrientation);
		jobCardTemplatePage.updateTemplateDetails(updatedTemplateName, updatedDescription, updatedOrientation);

		step("Saving the updated template");
		jobCardTemplatePage.clickSaveTemplate();
		verify(jobCardTemplatePage.isToastDisplayed("The Template has been updated successfully", 30),
				"Job Card Template updated successfully (toast 'The Template has been updated successfully' displayed)",
				"Success toast 'The Template has been updated successfully' was not displayed");
		currentTemplateName = updatedTemplateName;
		verify(jobCardTemplatePage.waitForTemplateDesignerToClose(), "Template designer closed after saving",
				"Template designer is still open after saving");

		step("Verifying updated template in the list");
		verifyUpdatedTemplateRow();

		step("Verifying the old template name no longer exists");
		jobCardTemplatePage.searchTemplate(templateName);
		verify(jobCardTemplatePage.getMatchingTemplateCount(templateName) == 0, "Old name '" + templateName + "' was replaced by the updated name",
				"Old Job Card Template name '" + templateName + "' is still displayed");

		step("Reloading the page and verifying the update is persisted");
		jobCardTemplatePage.refreshJobCardTemplatePage();
		verifyUpdatedTemplateRow();

		step("Re-opening the updated template and verifying the saved values");
		jobCardTemplatePage.clickEditTemplate(updatedTemplateName);
		Map<String, String> updatedValues = jobCardTemplatePage.getTemplateFormValues();
		verifyEquals(updatedValues.get("name"), updatedTemplateName, "Persisted Template Name");
		verifyEquals(updatedValues.get("description"), updatedDescription, "Persisted Template Description");
		verifyEquals(updatedValues.get("orientation"), updatedOrientation, "Persisted Orientation");
		verifyEquals(updatedValues.get("format"), format, "Persisted Format (unchanged)");
		verifyEquals(updatedValues.get("jobCategory"), jobCategory, "Persisted Job Category (unchanged)");
		jobCardTemplatePage.cancelTemplateDesigner();
		pass("Updated Job Card Template values are persisted after reload");
	}

	@Test(groups = { "regression" }, priority = 5, dependsOnMethods = "verify_CreateJobCardTemplate", description = "Cancel Delete Job Card Template Test")
	public void verify_CancelDeleteKeepsTemplate() {
		step("Locating test Job Card Template '" + currentTemplateName + "'");
		jobCardTemplatePage.openJobCardTemplateList();
		jobCardTemplatePage.searchTemplate(currentTemplateName);

		step("Clicking Delete");
		String dialogText = jobCardTemplatePage.clickDeleteTemplate(currentTemplateName);
		verify(dialogText.contains("Are you sure want to delete this job template '" + currentTemplateName + "'"),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Clicking Cancel on the delete confirmation");
		verify(jobCardTemplatePage.cancelDelete(), "Delete confirmation dialog closed on Cancel",
				"'Cancel' did not close the 'Delete Job Template' confirmation dialog");

		step("Verifying the template was not deleted");
		jobCardTemplatePage.refreshJobCardTemplatePage();
		jobCardTemplatePage.searchTemplate(currentTemplateName);
		verify(jobCardTemplatePage.isTemplateDisplayed(currentTemplateName), "Job Card Template still exists after cancelling the delete",
				"Job Card Template '" + currentTemplateName + "' was deleted although delete was cancelled");
	}

	@Test(groups = { "sanity", "regression" }, priority = 6, dependsOnMethods = "verify_CreateJobCardTemplate", description = "Delete Job Card Template Test")
	public void verify_DeleteJobCardTemplate() {
		step("Locating test Job Card Template '" + currentTemplateName + "'");
		jobCardTemplatePage.openJobCardTemplateList();
		jobCardTemplatePage.searchTemplate(currentTemplateName);
		verify(jobCardTemplatePage.isTemplateDisplayed(currentTemplateName), "Test Job Card Template found in the list",
				"Test Job Card Template '" + currentTemplateName + "' was not found");

		step("Clicking Delete");
		String dialogText = jobCardTemplatePage.clickDeleteTemplate(currentTemplateName);

		step("Delete confirmation dialog displayed");
		verify(dialogText.contains("Are you sure want to delete this job template '" + currentTemplateName + "'"),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Confirming deletion (Delete)");
		jobCardTemplatePage.confirmDelete();
		verify(jobCardTemplatePage.isToastDisplayed("Job Template deleted successfully"),
				"Job Card Template deleted successfully (toast 'Job Template deleted successfully' displayed)",
				"Success toast 'Job Template deleted successfully' was not displayed");
		templateDeleted = true;

		step("Verifying Job Card Template deletion");
		jobCardTemplatePage.refreshJobCardTemplatePage();
		jobCardTemplatePage.searchTemplate(currentTemplateName);
		verify(jobCardTemplatePage.isTemplateDeleted(currentTemplateName),
				"Deleted Job Card Template is no longer displayed in the list ('No templates available')",
				"Deleted Job Card Template '" + currentTemplateName + "' is still displayed in the list");
	}

	private void verifyUpdatedTemplateRow() {
		jobCardTemplatePage.searchTemplate(updatedTemplateName);
		verify(jobCardTemplatePage.isTemplateDisplayed(updatedTemplateName), "Updated Job Card Template is displayed in the list",
				"Updated Job Card Template '" + updatedTemplateName + "' was not displayed");
		Map<String, String> row = jobCardTemplatePage.getTemplateRowDetails(updatedTemplateName);
		verifyEquals(row.get("name"), updatedTemplateName, "Updated Template Name");
		verifyEquals(row.get("description"), updatedDescription, "Updated Template Description");
	}

	// Best-effort cleanup so a failed run does not leave the automation template behind
	private void cleanUpTestTemplate() {
		if (!templateCreated || templateDeleted) {
			logger.info("[CLEANUP] No cleanup needed - automation Job Card Template " + (templateCreated ? "already deleted" : "was not created"));
			return;
		}
		try {
			logger.info("[CLEANUP] Test Job Card Template was not deleted by the tests - cleaning up: " + currentTemplateName);
			jobCardTemplatePage.openJobCardTemplateList();
			for (String name : new String[] { currentTemplateName, templateName, updatedTemplateName }) {
				jobCardTemplatePage.searchTemplate(name);
				if (jobCardTemplatePage.getMatchingTemplateCount(name) > 0) {
					jobCardTemplatePage.clickDeleteTemplate(name);
					jobCardTemplatePage.confirmDelete();
					logger.info("[CLEANUP] Job Card Template '" + name + "' deleted: " + jobCardTemplatePage.isToastDisplayed("Job Template deleted successfully"));
					return;
				}
			}
			logger.info("[CLEANUP] Test Job Card Template not found - nothing to clean up");
		} catch (Exception e) {
			logger.error("[CLEANUP] Cleanup failed - delete Job Card Template manually: " + currentTemplateName + " | " + firstLine(e.getMessage()));
		}
	}

	@AfterClass(alwaysRun = true)
	public void setdown() {
		cleanUpTestTemplate();
		logger.info("[MODULE END] Settings > Job Card Template");
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
		private static final Logger log = LogManager.getLogger(TC_Job_Card_Template.class);

		@Override
		public void onTestSkipped(ITestResult result) {
			// listeners are suite-wide in TestNG - log only this class's skipped tests
			if (result.getTestClass().getRealClass() != TC_Job_Card_Template.class) {
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
