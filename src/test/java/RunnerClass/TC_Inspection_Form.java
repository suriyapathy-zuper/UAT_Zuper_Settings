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
import PageObject.InspectionFormPage;
import PageObject.SettingsPage;
import TestUtility.TestExecutionListener;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

/**
 * Settings > Jobs > Inspection Forms - complete Inspection Form lifecycle:
 * Login -> Settings -> Inspection Form -> Create -> Verify -> Edit -> Update -> Verify -> Delete -> Verify
 * plus required-field, duplicate-name and delete-confirmation checks.
 *
 * The test form is unique per run (timestamp suffix) and is removed in @AfterClass if a test fails midway.
 *
 * Execution log format (Baseclass log4j2 logger -> console + logs/automation-log.log):
 * [MODULE START] / [TEST START] / [STEP n] / [ACTION] / [VERIFY] / [PASS] / [WARN] / [FAIL] / [TEST END] / [MODULE END]
 */
@Listeners({ TestExecutionListener.class, TC_Inspection_Form.SkippedTestLogger.class })
public class TC_Inspection_Form extends Baseclass {

	private DashboardPage dashboardPage;
	private SettingsPage settingsPage;
	private InspectionFormPage inspectionFormPage;

	// Test data - unique per run so the automation form is always identifiable
	private final String uniqueSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
	private String formName;
	private String updatedFormName;
	private String description;
	private String updatedDescription;
	private String currentFormName;   // name currently saved in the application - changes after edit
	private int totalFormsBeforeDelete = -1;

	private boolean formCreated = false;
	private boolean formDeleted = false;

	// Execution log state
	private String currentTestName;
	private int stepNumber;
	private String currentStep;
	private String failedExpectation;
	private String failedActual;

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		logger.info("==================================================================");
		logger.info("[MODULE START] Settings > Inspection Form");
		// shared browser session - launched and logged in once by SuiteSession (TestExecutionListener)
		dashboardPage = new DashboardPage();
		settingsPage = new SettingsPage();
		inspectionFormPage = new InspectionFormPage();

		formName = prop.getProperty("inspectionForm_NamePrefix") + " " + uniqueSuffix;
		updatedFormName = prop.getProperty("inspectionForm_UpdatedNamePrefix") + " " + uniqueSuffix;
		description = prop.getProperty("inspectionForm_Description") + " " + uniqueSuffix;
		updatedDescription = prop.getProperty("inspectionForm_UpdatedDescription") + " " + uniqueSuffix;

		logger.info("[TEST DATA] Environment: " + prop.getProperty("baseURL") + " | Company: " + prop.getProperty("company_Name")
				+ " | Browser: " + prop.getProperty("browser"));
		logger.info("[TEST DATA] Inspection Form -> Name: '" + formName + "' | Description: '" + description + "'");
		logger.info("[TEST DATA] Update values -> Name: '" + updatedFormName + "' | Description: '" + updatedDescription + "'");
		logger.info("==================================================================");
	}

	@Test(groups = { "sanity", "regression" }, priority = 1, description = "Navigate to Inspection Form Test")
	public void verify_NavigateToInspectionForm() {
		step("Starting from Dashboard (shared authenticated session)");
		verify(dashboardPage.isDashboardDisplayed(), "Dashboard is displayed - test starts from Dashboard",
				"Dashboard is not displayed at the start of the test");

		step("Navigating to Settings through the Profile menu");
		settingsPage.navigateToSettings();
		verify(settingsPage.isSettingsHomeDisplayed(), "Settings home page is displayed", "Settings home page is not displayed");
		// the dialer can open late after login and cover the right side of the page
		dashboardPage.minimizeZuperConnectDialer(2);

		step("Opening Inspection Form section (Settings > Jobs > Inspection Forms)");
		settingsPage.openInspectionForms();
		inspectionFormPage.waitForInspectionFormPageToLoad();
		verify(inspectionFormPage.isInspectionFormPageDisplayed(), "Inspection Forms page loaded successfully",
				"Inspection Forms page is not displayed");
	}

	@Test(groups = { "regression" }, priority = 2, dependsOnMethods = "verify_NavigateToInspectionForm",
			description = "Create Inspection Form - Required Field Validation Test")
	public void verify_CreateInspectionFormRequiredFieldValidation() {
		step("Verifying the 'New Form' option is available");
		verify(inspectionFormPage.isNewFormButtonAvailable(), "'New Form' button is displayed and enabled",
				"'New Form' button is not available");

		step("Clicking New Form");
		inspectionFormPage.clickNewForm();
		verifyEquals(inspectionFormPage.getDialogTitle(), "Create New Inspection Form", "Dialog title");

		step("Verifying Proceed is disabled while Inspection Form Name is empty");
		verify(!inspectionFormPage.isDialogButtonEnabled("Proceed"), "'Proceed' is disabled with an empty Inspection Form Name",
				"'Proceed' is enabled although Inspection Form Name is empty");

		step("Entering and clearing Inspection Form Name");
		inspectionFormPage.enterInspectionFormName(formName);
		verify(inspectionFormPage.isDialogButtonEnabled("Proceed"), "'Proceed' is enabled once Inspection Form Name is entered",
				"'Proceed' stays disabled although Inspection Form Name is entered");
		inspectionFormPage.clearInspectionFormName();

		step("Verifying required field validation message");
		verifyEquals(inspectionFormPage.getNameValidationMessage(), "This field is required", "Validation message for 'Inspection Form Name'");
		verify(!inspectionFormPage.isDialogButtonEnabled("Proceed"), "Empty Inspection Form Name is not accepted - 'Proceed' is disabled again",
				"'Proceed' is enabled although Inspection Form Name was cleared");

		step("Cancelling the Create New Inspection Form dialog");
		inspectionFormPage.cancelFormDialog();
		pass("Required field validation works for Inspection Form Name");
	}

	@Test(groups = { "sanity", "regression" }, priority = 3, dependsOnMethods = "verify_NavigateToInspectionForm", description = "Create Inspection Form Test")
	public void verify_CreateInspectionForm() {
		step("Clicking Create Inspection Form (New Form)");
		inspectionFormPage.clickNewForm();

		step("Entering Inspection Form details -> Name: '" + formName + "', Description: '" + description + "'");
		inspectionFormPage.enterInspectionFormDetails(formName, description);

		step("Clicking Proceed (creates the form and opens the form builder)");
		inspectionFormPage.clickProceed();

		step("Verifying Inspection Form creation");
		verify(inspectionFormPage.isToastDisplayed("Inspection Form created successfully"),
				"Inspection Form created successfully (toast 'Inspection Form created successfully' displayed)",
				"Success toast 'Inspection Form created successfully' was not displayed");
		formCreated = true;
		currentFormName = formName;
		inspectionFormPage.waitForFormBuilder();
		verify(inspectionFormPage.isFormBuilderDisplayed(), "Form builder opened for the new Inspection Form",
				"Form builder did not open after creating the Inspection Form");
		verifyEquals(inspectionFormPage.getBuilderFormName(), formName, "Form builder heading");

		step("Returning to the Inspection Forms list");
		inspectionFormPage.backToInspectionFormList();

		step("Verifying the created Inspection Form is displayed in the list");
		verify(inspectionFormPage.isInspectionFormDisplayed(formName), "Created Inspection Form is displayed in the list",
				"Created Inspection Form '" + formName + "' was not displayed in the list");
		Map<String, String> row = inspectionFormPage.getInspectionFormRowDetails(formName);
		verifyEquals(row.get("name"), formName, "Form Name");
		verifyEquals(row.get("description"), description, "Description");
		verify(!row.get("createdBy").isEmpty(), "Created By = '" + row.get("createdBy") + "'", "Created By is empty");
		verify(!row.get("createdOn").isEmpty(), "Created On = '" + row.get("createdOn") + "'", "Created On is empty");
	}

	@Test(groups = { "regression" }, priority = 4, dependsOnMethods = "verify_CreateInspectionForm", description = "Duplicate Inspection Form Test")
	public void verify_DuplicateInspectionFormIsPrevented() {
		step("Creating another Inspection Form with the same name '" + formName + "'");
		inspectionFormPage.clickNewForm();
		inspectionFormPage.enterInspectionFormName(formName);
		inspectionFormPage.clickProceed();

		step("Verifying duplicate Inspection Form is rejected");
		verify(inspectionFormPage.isToastDisplayed("Inspection Form Name Already Exists"),
				"Error toast 'Inspection Form Name Already Exists' displayed", "Duplicate Inspection Form error toast was not displayed");
		if (!inspectionFormPage.isFormDialogClosed()) {
			inspectionFormPage.cancelFormDialog();
		}
		verify(inspectionFormPage.isInspectionFormPageDisplayed(), "Still on the Inspection Forms list - no form builder opened",
				"Navigated away from the list although the name already exists");

		step("Reloading the list and confirming only one Inspection Form has this name");
		inspectionFormPage.refreshInspectionFormPage();
		verifyEquals(inspectionFormPage.getMatchingFormCount(formName), 1, "Number of Inspection Forms named '" + formName + "'");
		pass("Duplicate Inspection Form creation is prevented");
	}

	@Test(groups = { "regression" }, priority = 5, dependsOnMethods = "verify_CreateInspectionForm", description = "Edit Inspection Form Test")
	public void verify_EditInspectionForm() {
		step("Locating created Inspection Form '" + formName + "'");
		inspectionFormPage.openInspectionFormList();
		verify(inspectionFormPage.isInspectionFormDisplayed(formName), "Created Inspection Form found in the list",
				"Created Inspection Form '" + formName + "' was not found");

		step("Clicking Edit (opens the form builder)");
		inspectionFormPage.clickEditInspectionForm(formName);
		verifyEquals(inspectionFormPage.getBuilderFormName(), formName, "Form builder heading");

		step("Opening form settings (gear) to edit the name and description");
		inspectionFormPage.openFormSettings();
		verifyEquals(inspectionFormPage.getDialogTitle(), "Update Inspection Form", "Dialog title");
		Map<String, String> savedValues = inspectionFormPage.getFormDialogValues();
		verifyEquals(savedValues.get("name"), formName, "Edit dialog Inspection Form Name");
		verifyEquals(savedValues.get("description"), description, "Edit dialog Description");

		step("Updating Inspection Form -> Name: '" + updatedFormName + "', Description: '" + updatedDescription + "'");
		inspectionFormPage.enterInspectionFormDetails(updatedFormName, updatedDescription);

		step("Saving the update (Update)");
		inspectionFormPage.clickUpdate();
		verify(inspectionFormPage.isToastDisplayed("The Inspection Form has been updated successfully"),
				"Inspection Form updated successfully (toast 'The Inspection Form has been updated successfully' displayed)",
				"Success toast 'The Inspection Form has been updated successfully' was not displayed");
		currentFormName = updatedFormName;
		verify(inspectionFormPage.isFormDialogClosed(), "Update Inspection Form dialog closed", "Update Inspection Form dialog is still open");
		verify(inspectionFormPage.waitForBuilderFormName(updatedFormName), "Form builder heading shows the updated name '" + updatedFormName + "'",
				"Form builder heading is '" + inspectionFormPage.getBuilderFormName() + "' instead of '" + updatedFormName + "'");

		step("Returning to the list and verifying the updated Inspection Form");
		inspectionFormPage.backToInspectionFormList();
		verifyUpdatedFormRow();
		verify(inspectionFormPage.getMatchingFormCount(formName) == 0, "Old name '" + formName + "' was replaced by the updated name",
				"Old Inspection Form name '" + formName + "' is still displayed");

		step("Reloading the page and verifying the update is persisted");
		inspectionFormPage.refreshInspectionFormPage();
		verifyUpdatedFormRow();

		step("Re-opening the form settings and verifying the saved values");
		inspectionFormPage.clickEditInspectionForm(updatedFormName);
		inspectionFormPage.openFormSettings();
		Map<String, String> updatedValues = inspectionFormPage.getFormDialogValues();
		verifyEquals(updatedValues.get("name"), updatedFormName, "Persisted Inspection Form Name");
		verifyEquals(updatedValues.get("description"), updatedDescription, "Persisted Description");
		inspectionFormPage.cancelFormDialog();
		inspectionFormPage.backToInspectionFormList();
		pass("Updated Inspection Form values are persisted after reload");
	}

	@Test(groups = { "regression" }, priority = 6, dependsOnMethods = "verify_CreateInspectionForm", description = "Cancel Delete Inspection Form Test")
	public void verify_CancelDeleteKeepsInspectionForm() {
		step("Locating test Inspection Form '" + currentFormName + "'");
		inspectionFormPage.openInspectionFormList();
		verify(inspectionFormPage.isInspectionFormDisplayed(currentFormName), "Test Inspection Form found in the list",
				"Test Inspection Form '" + currentFormName + "' was not found");

		step("Clicking Delete");
		String dialogText = inspectionFormPage.clickDeleteInspectionForm(currentFormName);
		verify(dialogText.contains("Are you sure want to delete Inspection Form '" + currentFormName + "'"),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Clicking Cancel on the delete confirmation");
		verify(inspectionFormPage.cancelDelete(), "Delete confirmation dialog closed on Cancel",
				"'Cancel' did not close the 'Delete Inspection Form' confirmation dialog");

		step("Verifying the Inspection Form was not deleted");
		inspectionFormPage.refreshInspectionFormPage();
		verify(inspectionFormPage.isInspectionFormDisplayed(currentFormName), "Inspection Form still exists after cancelling the delete",
				"Inspection Form '" + currentFormName + "' was deleted although delete was cancelled");
	}

	@Test(groups = { "sanity", "regression" }, priority = 7, dependsOnMethods = "verify_CreateInspectionForm", description = "Delete Inspection Form Test")
	public void verify_DeleteInspectionForm() {
		step("Locating test Inspection Form '" + currentFormName + "'");
		inspectionFormPage.openInspectionFormList();
		verify(inspectionFormPage.isInspectionFormDisplayed(currentFormName), "Test Inspection Form found in the list",
				"Test Inspection Form '" + currentFormName + "' was not found");
		totalFormsBeforeDelete = inspectionFormPage.getTotalFormCount();

		step("Clicking Delete");
		String dialogText = inspectionFormPage.clickDeleteInspectionForm(currentFormName);

		step("Delete confirmation dialog displayed");
		verify(dialogText.contains("Are you sure want to delete Inspection Form '" + currentFormName + "'"),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Confirming deletion (Delete)");
		inspectionFormPage.confirmDelete();
		verify(inspectionFormPage.isToastDisplayed("Inspection Form deleted successfully"),
				"Inspection Form deleted successfully (toast 'Inspection Form deleted successfully' displayed)",
				"Success toast 'Inspection Form deleted successfully' was not displayed");
		formDeleted = true;

		step("Verifying Inspection Form deletion");
		inspectionFormPage.refreshInspectionFormPage();
		verify(inspectionFormPage.isInspectionFormRemoved(currentFormName),
				"Deleted Inspection Form is no longer displayed in the list",
				"Deleted Inspection Form '" + currentFormName + "' is still displayed in the list");
		int totalFormsAfterDelete = inspectionFormPage.getTotalFormCount();
		if (totalFormsBeforeDelete >= 0 && totalFormsAfterDelete >= 0) {
			verifyEquals(totalFormsAfterDelete, totalFormsBeforeDelete - 1, "Total Inspection Forms count after delete");
		} else {
			logger.warn("[WARN] Total Inspection Forms count could not be read - count check skipped");
		}
	}

	private void verifyUpdatedFormRow() {
		verify(inspectionFormPage.isInspectionFormDisplayed(updatedFormName), "Updated Inspection Form is displayed in the list",
				"Updated Inspection Form '" + updatedFormName + "' was not displayed");
		Map<String, String> row = inspectionFormPage.getInspectionFormRowDetails(updatedFormName);
		verifyEquals(row.get("name"), updatedFormName, "Updated Form Name");
		verifyEquals(row.get("description"), updatedDescription, "Updated Description");
	}

	// Best-effort cleanup so a failed run does not leave the automation form behind
	private void cleanUpTestForm() {
		if (!formCreated || formDeleted) {
			logger.info("[CLEANUP] No cleanup needed - automation Inspection Form " + (formCreated ? "already deleted" : "was not created"));
			return;
		}
		try {
			logger.info("[CLEANUP] Test Inspection Form was not deleted by the tests - cleaning up: " + currentFormName);
			inspectionFormPage.openInspectionFormList();
			for (String name : new String[] { currentFormName, formName, updatedFormName }) {
				if (inspectionFormPage.getMatchingFormCount(name) > 0) {
					inspectionFormPage.clickDeleteInspectionForm(name);
					inspectionFormPage.confirmDelete();
					logger.info("[CLEANUP] Inspection Form '" + name + "' deleted: " + inspectionFormPage.isToastDisplayed("Inspection Form deleted successfully"));
					return;
				}
			}
			logger.info("[CLEANUP] Test Inspection Form not found - nothing to clean up");
		} catch (Exception e) {
			logger.error("[CLEANUP] Cleanup failed - delete Inspection Form manually: " + currentFormName + " | " + firstLine(e.getMessage()));
		}
	}

	@AfterClass(alwaysRun = true)
	public void setdown() {
		cleanUpTestForm();
		logger.info("[MODULE END] Settings > Inspection Form");
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
		private static final Logger log = LogManager.getLogger(TC_Inspection_Form.class);

		@Override
		public void onTestSkipped(ITestResult result) {
			// listeners are suite-wide in TestNG - log only this class's skipped tests
			if (result.getTestClass().getRealClass() != TC_Inspection_Form.class) {
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
