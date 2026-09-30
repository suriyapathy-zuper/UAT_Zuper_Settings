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
import PageObject.JobCategoryPage;
import PageObject.SettingsPage;
import TestUtility.TestExecutionListener;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

/**
 * Settings > Jobs > Job Category Hub - complete Job Category lifecycle:
 * Login -> Settings -> Job Category -> Create -> Verify -> Edit -> Verify -> Delete -> Verify
 * plus required-field / max-length / duplicate / delete-confirmation checks.
 *
 * The test category is unique per run (timestamp suffix) and is removed in @AfterClass if a test fails midway.
 *
 * Execution log format (Baseclass log4j2 logger -> console + logs/automation-log.log):
 * [MODULE START] / [TEST START] / [STEP n] / [ACTION] / [PASS] / [WARN] / [FAIL] / [TEST END] / [MODULE END]
 */
@Listeners({ TestExecutionListener.class, TC_Job_Category.SkippedTestLogger.class })
public class TC_Job_Category extends Baseclass {

	private static final int CATEGORY_NAME_MAX_LENGTH = 26;

	private DashboardPage dashboardPage;
	private SettingsPage settingsPage;
	private JobCategoryPage jobCategoryPage;

	// Test data - unique per run so the automation category is always identifiable
	private final String uniqueSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
	private String categoryName;
	private String updatedCategoryName;
	private String days;
	private String hours;
	private String minutes;
	private String updatedHours;
	private String updatedMinutes;
	private String currentCategoryName;   // name currently saved in the application - changes after edit

	private boolean categoryCreated = false;
	private boolean categoryDeleted = false;

	// Execution log state
	private String currentTestName;
	private int stepNumber;
	private String currentStep;
	private String failedExpectation;
	private String failedActual;

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		logger.info("==================================================================");
		logger.info("[MODULE START] Settings > Job Category");
		// shared browser session - launched and logged in once by SuiteSession (TestExecutionListener)
		dashboardPage = new DashboardPage();
		settingsPage = new SettingsPage();
		jobCategoryPage = new JobCategoryPage();
		logger.info("[TEST DATA] Environment: " + prop.getProperty("baseURL") + " | Company: " + prop.getProperty("company_Name")
				+ " | Browser: " + prop.getProperty("browser"));

		categoryName = prop.getProperty("jobCategory_NamePrefix") + " " + uniqueSuffix;
		updatedCategoryName = prop.getProperty("jobCategory_UpdatedNamePrefix") + " " + uniqueSuffix;
		days = prop.getProperty("jobCategory_Days");
		hours = prop.getProperty("jobCategory_Hours");
		minutes = prop.getProperty("jobCategory_Minutes");
		updatedHours = prop.getProperty("jobCategory_UpdatedHours");
		updatedMinutes = prop.getProperty("jobCategory_UpdatedMinutes");

		if (categoryName.length() > CATEGORY_NAME_MAX_LENGTH || updatedCategoryName.length() > CATEGORY_NAME_MAX_LENGTH) {
			throw new IllegalArgumentException("Job Category name prefixes in configure.properties are too long - '"
					+ updatedCategoryName + "' exceeds " + CATEGORY_NAME_MAX_LENGTH + " characters");
		}
		logger.info("[TEST DATA] Job Category -> Name: '" + categoryName + "' | Duration: " + durationText(days, hours, minutes));
		logger.info("[TEST DATA] Update values -> Name: '" + updatedCategoryName + "' | Duration: " + durationText(days, updatedHours, updatedMinutes));
		logger.info("==================================================================");
	}

	@Test(groups = { "sanity", "regression" }, priority = 1, description = "Navigate to Job Category Test")
	public void verify_NavigateToJobCategory() {
		step("Starting from Dashboard (shared authenticated session)");
		verify(dashboardPage.isDashboardDisplayed(), "Dashboard is displayed - test starts from Dashboard",
				"Dashboard is not displayed at the start of the test");

		step("Navigating to Settings through the Profile menu");
		settingsPage.navigateToSettings();
		verify(settingsPage.isSettingsHomeDisplayed(), "Settings home page is displayed", "Settings home page is not displayed");
		// the dialer can open late after login and cover the right side of the page
		dashboardPage.minimizeZuperConnectDialer(2);

		step("Opening Job Category section (Settings > Jobs > Job Category Hub)");
		settingsPage.openJobCategory();
		jobCategoryPage.waitForJobCategoryPageToLoad();
		verify(jobCategoryPage.isJobCategoryPageDisplayed(), "Job Category page loaded successfully",
				"Job Category page is not displayed");
	}

	@Test(groups = { "regression" }, priority = 2, dependsOnMethods = "verify_NavigateToJobCategory",
			description = "Create Job Category - Required Field Validation Test")
	public void verify_CreateJobCategoryRequiredFieldValidation() {
		step("Verifying the 'New Category' option is available");
		verify(jobCategoryPage.isNewCategoryButtonAvailable(), "'New Category' button is displayed and enabled",
				"'New Category' button is not available");

		step("Clicking New Category");
		jobCategoryPage.clickNewCategory();
		verifyEquals(jobCategoryPage.getDialogTitle(), "Create New Job Category", "Dialog title");

		step("Clicking Create with empty Category Name and Estimated Duration");
		jobCategoryPage.clickCreate();

		step("Verifying required field validation messages");
		verifyEquals(jobCategoryPage.getValidationMessage("Category Name"), "This field is required", "Validation message for 'Category Name'");
		verifyEquals(jobCategoryPage.getValidationMessage("Estimated Duration"), "This field is required",
				"Validation message for 'Estimated Duration'");
		verify(jobCategoryPage.isCategoryDialogOpen(), "Empty Job Category was not accepted - Create dialog is still open",
				"Create dialog closed although required fields are empty");

		step("Cancelling the Create Job Category dialog");
		jobCategoryPage.cancelCategoryDialog();
		pass("Required field validation works for Category Name and Estimated Duration");
	}

	@Test(groups = { "regression" }, priority = 3, dependsOnMethods = "verify_NavigateToJobCategory",
			description = "Create Job Category - Name Max Length Validation Test")
	public void verify_CategoryNameMaxLength() {
		String longName = (categoryName + " ABCDEFGHIJKLMNOPQRSTUVWXYZ").substring(0, CATEGORY_NAME_MAX_LENGTH + 10);

		step("Clicking New Category");
		jobCategoryPage.clickNewCategory();

		step("Entering a " + longName.length() + "-character Category Name: '" + longName + "'");
		verifyEquals(jobCategoryPage.getCategoryNameMaxLength(), String.valueOf(CATEGORY_NAME_MAX_LENGTH), "Category Name maxlength attribute");
		jobCategoryPage.enterCategoryName(longName);

		step("Verifying the Category Name is limited to " + CATEGORY_NAME_MAX_LENGTH + " characters");
		verifyEquals(jobCategoryPage.getCategoryNameValue(), longName.substring(0, CATEGORY_NAME_MAX_LENGTH), "Category Name value");

		step("Cancelling the Create Job Category dialog");
		jobCategoryPage.cancelCategoryDialog();
		pass("Category Name accepts at most " + CATEGORY_NAME_MAX_LENGTH + " characters");
	}

	@Test(groups = { "sanity", "regression" }, priority = 4, dependsOnMethods = "verify_NavigateToJobCategory", description = "Create Job Category Test")
	public void verify_CreateJobCategory() {
		step("Clicking Create Job Category (New Category)");
		jobCategoryPage.clickNewCategory();

		step("Entering Job Category name: '" + categoryName + "', Estimated Duration: " + durationText(days, hours, minutes));
		jobCategoryPage.enterJobCategoryDetails(categoryName, days, hours, minutes);

		step("Clicking Create");
		jobCategoryPage.clickCreate();

		step("Verifying Job Category creation");
		verify(jobCategoryPage.isToastDisplayed("Job Category created successfully"),
				"Job Category created successfully (toast 'Job Category created successfully' displayed)",
				"Success toast 'Job Category created successfully' was not displayed");
		categoryCreated = true;
		currentCategoryName = categoryName;
		verify(jobCategoryPage.isCategoryDialogClosed(), "Create Job Category dialog closed", "Create Job Category dialog is still open");

		step("Searching for the created Job Category in the list");
		jobCategoryPage.searchJobCategory(categoryName);
		verify(jobCategoryPage.isJobCategoryDisplayed(categoryName), "Created Job Category is displayed in the list",
				"Created Job Category '" + categoryName + "' was not displayed in the list");

		step("Verifying the created Job Category details in the list");
		Map<String, String> row = jobCategoryPage.getJobCategoryRowDetails(categoryName);
		verifyEquals(row.get("name"), categoryName, "Category Name");
		verifyEquals(row.get("estimatedDuration"), durationText(days, hours, minutes), "Estimated Duration");
		verify(!row.get("createdOn").isEmpty(), "Created On = '" + row.get("createdOn") + "'", "Created On is empty");
	}

	@Test(groups = { "regression" }, priority = 5, dependsOnMethods = "verify_CreateJobCategory", description = "Duplicate Job Category Test")
	public void verify_DuplicateJobCategoryIsPrevented() {
		step("Creating another Job Category with the same name '" + categoryName + "'");
		jobCategoryPage.clickNewCategory();
		jobCategoryPage.enterJobCategoryDetails(categoryName, days, hours, minutes);
		jobCategoryPage.clickCreate();

		step("Verifying duplicate Job Category is rejected");
		verify(jobCategoryPage.isToastDisplayed("Category Name Already Exists"),
				"Error toast 'Category Name Already Exists' displayed", "Duplicate Job Category error toast was not displayed");
		if (!jobCategoryPage.isCategoryDialogClosed()) {
			jobCategoryPage.cancelCategoryDialog();
		}

		step("Searching the list and confirming only one Job Category has this name");
		jobCategoryPage.searchJobCategory(categoryName);
		verifyEquals(jobCategoryPage.getMatchingCategoryCount(categoryName), 1, "Number of Job Categories named '" + categoryName + "'");
		pass("Duplicate Job Category creation is prevented");
	}

	@Test(groups = { "regression" }, priority = 6, dependsOnMethods = "verify_CreateJobCategory", description = "Edit Job Category Test")
	public void verify_EditJobCategory() {
		step("Locating created Job Category '" + categoryName + "'");
		jobCategoryPage.openJobCategoryList();
		jobCategoryPage.searchJobCategory(categoryName);
		verify(jobCategoryPage.isJobCategoryDisplayed(categoryName), "Created Job Category found in the list",
				"Created Job Category '" + categoryName + "' was not found");

		step("Clicking Edit");
		jobCategoryPage.clickEditJobCategory(categoryName);
		verifyEquals(jobCategoryPage.getDialogTitle(), "Edit Job Category", "Dialog title");
		Map<String, String> formValues = jobCategoryPage.getCategoryFormValues();
		verifyEquals(formValues.get("name"), categoryName, "Edit form Category Name");
		verifyEquals(formValues.get("hours"), hours, "Edit form Hours");
		verifyEquals(formValues.get("minutes"), minutes, "Edit form Minutes");

		step("Updating Job Category details -> Name: '" + updatedCategoryName + "', Estimated Duration: "
				+ durationText(days, updatedHours, updatedMinutes));
		jobCategoryPage.enterJobCategoryDetails(updatedCategoryName, days, updatedHours, updatedMinutes);

		step("Saving updated Job Category (Update)");
		jobCategoryPage.saveJobCategoryUpdate();
		verify(jobCategoryPage.isToastDisplayed("The Category has been updated successfully"),
				"Job Category updated successfully (toast 'The Category has been updated successfully' displayed)",
				"Success toast 'The Category has been updated successfully' was not displayed");
		currentCategoryName = updatedCategoryName;

		step("Verifying updated Job Category in the list");
		verifyUpdatedCategory();

		step("Verifying the old Job Category name no longer exists");
		jobCategoryPage.searchJobCategory(categoryName);
		verify(jobCategoryPage.getMatchingCategoryCount(categoryName) == 0, "Old name '" + categoryName + "' was replaced by the updated name",
				"Old Job Category name '" + categoryName + "' is still displayed");

		step("Reloading the page and verifying the update is persisted");
		jobCategoryPage.refreshJobCategoryPage();
		verifyUpdatedCategory();
		pass("Updated Job Category value is displayed correctly after reload");
	}

	@Test(groups = { "regression" }, priority = 7, dependsOnMethods = "verify_CreateJobCategory", description = "Cancel Delete Job Category Test")
	public void verify_CancelDeleteKeepsJobCategory() {
		step("Locating test Job Category '" + currentCategoryName + "'");
		jobCategoryPage.openJobCategoryList();
		jobCategoryPage.searchJobCategory(currentCategoryName);

		step("Clicking Delete");
		String dialogText = jobCategoryPage.clickDeleteJobCategory(currentCategoryName);
		verify(dialogText.contains("Are you sure you want to delete the job category '" + currentCategoryName + "'"),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Clicking Cancel on the delete confirmation");
		verify(jobCategoryPage.cancelDelete(), "Delete confirmation dialog closed on Cancel",
				"'Cancel' did not close the 'Delete Job Category' confirmation dialog");

		step("Verifying the Job Category was not deleted");
		jobCategoryPage.refreshJobCategoryPage();
		jobCategoryPage.searchJobCategory(currentCategoryName);
		verify(jobCategoryPage.isJobCategoryDisplayed(currentCategoryName), "Job Category still exists after cancelling the delete",
				"Job Category '" + currentCategoryName + "' was deleted although delete was cancelled");
	}

	@Test(groups = { "sanity", "regression" }, priority = 8, dependsOnMethods = "verify_CreateJobCategory", description = "Delete Job Category Test")
	public void verify_DeleteJobCategory() {
		step("Locating test Job Category '" + currentCategoryName + "'");
		jobCategoryPage.openJobCategoryList();
		jobCategoryPage.searchJobCategory(currentCategoryName);
		verify(jobCategoryPage.isJobCategoryDisplayed(currentCategoryName), "Test Job Category found in the list",
				"Test Job Category '" + currentCategoryName + "' was not found");

		step("Clicking Delete");
		String dialogText = jobCategoryPage.clickDeleteJobCategory(currentCategoryName);

		step("Delete confirmation dialog displayed");
		verify(dialogText.contains("Are you sure you want to delete the job category '" + currentCategoryName + "'"),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Confirming deletion (Delete)");
		jobCategoryPage.confirmDelete();
		verify(jobCategoryPage.isToastDisplayed("Job Category deleted successfully"),
				"Job Category deleted successfully (toast 'Job Category deleted successfully' displayed)",
				"Success toast 'Job Category deleted successfully' was not displayed");
		categoryDeleted = true;

		step("Verifying Job Category deletion");
		jobCategoryPage.refreshJobCategoryPage();
		jobCategoryPage.searchJobCategory(currentCategoryName);
		verify(jobCategoryPage.isJobCategoryDeleted(currentCategoryName),
				"Deleted Job Category is no longer displayed in the list ('No Job Categories found')",
				"Deleted Job Category '" + currentCategoryName + "' is still displayed in the list");
	}

	private void verifyUpdatedCategory() {
		jobCategoryPage.searchJobCategory(updatedCategoryName);
		verify(jobCategoryPage.isJobCategoryDisplayed(updatedCategoryName), "Updated Job Category is displayed in the list",
				"Updated Job Category '" + updatedCategoryName + "' was not displayed");
		Map<String, String> row = jobCategoryPage.getJobCategoryRowDetails(updatedCategoryName);
		verifyEquals(row.get("name"), updatedCategoryName, "Updated Category Name");
		verifyEquals(row.get("estimatedDuration"), durationText(days, updatedHours, updatedMinutes), "Updated Estimated Duration");
	}

	// Duration as shown in the list, e.g. "1d 4h 5m", "2h 30m", "3h"
	private static String durationText(String days, String hours, String minutes) {
		StringBuilder text = new StringBuilder();
		if (Integer.parseInt(days) > 0) {
			text.append(Integer.parseInt(days)).append("d ");
		}
		if (Integer.parseInt(hours) > 0) {
			text.append(Integer.parseInt(hours)).append("h ");
		}
		if (Integer.parseInt(minutes) > 0) {
			text.append(Integer.parseInt(minutes)).append("m");
		}
		return text.toString().trim();
	}

	// Best-effort cleanup so a failed run does not leave the automation category behind
	private void cleanUpTestCategory() {
		if (!categoryCreated || categoryDeleted) {
			logger.info("[CLEANUP] No cleanup needed - automation Job Category " + (categoryCreated ? "already deleted" : "was not created"));
			return;
		}
		try {
			logger.info("[CLEANUP] Test Job Category was not deleted by the tests - cleaning up: " + currentCategoryName);
			jobCategoryPage.openJobCategoryList();
			for (String name : new String[] { currentCategoryName, categoryName, updatedCategoryName }) {
				jobCategoryPage.searchJobCategory(name);
				if (jobCategoryPage.getMatchingCategoryCount(name) > 0) {
					jobCategoryPage.clickDeleteJobCategory(name);
					jobCategoryPage.confirmDelete();
					logger.info("[CLEANUP] Job Category '" + name + "' deleted: " + jobCategoryPage.isToastDisplayed("Job Category deleted successfully"));
					return;
				}
			}
			logger.info("[CLEANUP] Test Job Category not found - nothing to clean up");
		} catch (Exception e) {
			logger.error("[CLEANUP] Cleanup failed - delete Job Category manually: " + currentCategoryName + " | " + firstLine(e.getMessage()));
		}
	}

	@AfterClass(alwaysRun = true)
	public void setdown() {
		cleanUpTestCategory();
		logger.info("[MODULE END] Settings > Job Category");
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

	// Assert 'condition'; logs "[PASS] expected" or "[FAIL] Expected / Actual" before failing
	private void verify(boolean condition, String expected, String actual) {
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
		private static final Logger log = LogManager.getLogger(TC_Job_Category.class);

		@Override
		public void onTestSkipped(ITestResult result) {
			// listeners are suite-wide in TestNG - log only this class's skipped tests
			if (result.getTestClass().getRealClass() != TC_Job_Category.class) {
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
