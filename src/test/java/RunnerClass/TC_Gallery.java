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
import PageObject.GalleryPage;
import PageObject.SettingsPage;
import TestUtility.TestExecutionListener;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

/**
 * Settings > Jobs > Gallery Settings (Albums) - complete gallery album lifecycle:
 * Login -> Settings -> Gallery -> Create Album -> Verify -> Edit -> Update -> Verify -> Delete -> Verify
 * plus required-field, duplicate-name and delete-confirmation checks.
 *
 * The test album is unique per run (timestamp suffix) and is removed in @AfterClass if a test fails midway.
 *
 * Execution log format (Baseclass log4j2 logger -> console + logs/automation-log.log):
 * [MODULE START] / [TEST START] / [STEP n] / [ACTION] / [VERIFY] / [PASS] / [WARN] / [FAIL] / [TEST END] / [MODULE END]
 */
@Listeners({ TestExecutionListener.class, TC_Gallery.SkippedTestLogger.class })
public class TC_Gallery extends Baseclass {

	private DashboardPage dashboardPage;
	private SettingsPage settingsPage;
	private GalleryPage galleryPage;

	// Test data - unique per run so the automation album is always identifiable
	private final String uniqueSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
	private String albumName;
	private String updatedAlbumName;
	private String currentAlbumName;   // name currently saved in the application - changes after edit

	private boolean albumCreated = false;
	private boolean albumDeleted = false;

	// Execution log state
	private String currentTestName;
	private int stepNumber;
	private String currentStep;
	private String failedExpectation;
	private String failedActual;

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		logger.info("==================================================================");
		logger.info("[MODULE START] Settings > Gallery");
		// shared browser session - launched and logged in once by SuiteSession (TestExecutionListener)
		dashboardPage = new DashboardPage();
		settingsPage = new SettingsPage();
		galleryPage = new GalleryPage();

		albumName = prop.getProperty("gallery_AlbumNamePrefix") + " " + uniqueSuffix;
		updatedAlbumName = prop.getProperty("gallery_UpdatedAlbumNamePrefix") + " " + uniqueSuffix;

		logger.info("[TEST DATA] Environment: " + prop.getProperty("baseURL") + " | Company: " + prop.getProperty("company_Name")
				+ " | Browser: " + prop.getProperty("browser"));
		logger.info("[TEST DATA] Gallery album -> Name: '" + albumName + "' | Updated name: '" + updatedAlbumName + "'");
		logger.info("==================================================================");
	}

	@Test(groups = { "sanity", "regression" }, priority = 1, description = "Navigate to Gallery Test")
	public void verify_NavigateToGallery() {
		step("Starting from Dashboard (shared authenticated session)");
		verify(dashboardPage.isDashboardDisplayed(), "Dashboard is displayed - test starts from Dashboard",
				"Dashboard is not displayed at the start of the test");

		step("Navigating to Settings through the Profile menu");
		settingsPage.navigateToSettings();
		verify(settingsPage.isSettingsHomeDisplayed(), "Settings home page is displayed", "Settings home page is not displayed");
		// the dialer can open late after login and cover the right side of the page
		dashboardPage.minimizeZuperConnectDialer(2);

		step("Opening Gallery (Settings > Jobs > Gallery Settings)");
		settingsPage.openGallerySettings();
		galleryPage.waitForGalleryPageToLoad();
		verify(galleryPage.isGalleryPageDisplayed(), "Gallery Settings page loaded - 'Add New Album' section displayed",
				"Gallery Settings page is not displayed");
	}

	@Test(groups = { "regression" }, priority = 2, dependsOnMethods = "verify_NavigateToGallery", description = "Create Gallery Album - Required Field Validation Test")
	public void verify_CreateAlbumRequiredFieldValidation() {
		step("Verifying 'Add Album' is disabled while Album Name is empty");
		galleryPage.clearAlbumName();
		verify(!galleryPage.isAddAlbumButtonEnabled(), "'Add Album' is disabled with an empty Album Name",
				"'Add Album' is enabled although Album Name is empty");

		step("Entering Album Name '" + albumName + "'");
		galleryPage.enterAlbumName(albumName);
		verify(galleryPage.isAddAlbumButtonEnabled(), "'Add Album' is enabled once Album Name is entered",
				"'Add Album' stays disabled although Album Name is entered");

		step("Clearing Album Name");
		galleryPage.clearAlbumName();
		verify(!galleryPage.isAddAlbumButtonEnabled(), "Empty Album Name is not accepted - 'Add Album' is disabled again",
				"'Add Album' is enabled although Album Name was cleared");
		pass("Required field validation works for Album Name");
	}

	@Test(groups = { "sanity", "regression" }, priority = 3, dependsOnMethods = "verify_NavigateToGallery", description = "Create Gallery Album Test")
	public void verify_CreateGalleryAlbum() {
		step("Entering Album Name: '" + albumName + "'");
		galleryPage.enterAlbumName(albumName);

		step("Clicking Add Album");
		galleryPage.clickAddAlbum();

		step("Verifying gallery album creation");
		verify(galleryPage.isToastDisplayed("Album created successfully"),
				"Gallery album created successfully (toast 'Album created successfully' displayed)",
				"Success toast 'Album created successfully' was not displayed");
		albumCreated = true;
		currentAlbumName = albumName;
		verifyEquals(galleryPage.getAlbumNameValue(), "", "'Enter album name' box after adding (cleared)");

		step("Searching for the created album in the list");
		galleryPage.searchAlbum(albumName);
		verify(galleryPage.isAlbumDisplayed(albumName), "Created album is displayed in the Albums list",
				"Created album '" + albumName + "' was not displayed in the Albums list");
		Map<String, String> row = galleryPage.getAlbumRowDetails(albumName);
		verifyEquals(row.get("name"), albumName, "Album Name");
		verify(!row.get("createdOn").isEmpty(), "Created On = '" + row.get("createdOn") + "'", "Created On is empty");
	}

	@Test(groups = { "regression" }, priority = 4, dependsOnMethods = "verify_CreateGalleryAlbum", description = "Duplicate Gallery Album Test")
	public void verify_DuplicateAlbumIsPrevented() {
		step("Adding another album with the same name '" + albumName + "'");
		galleryPage.addAlbum(albumName);

		step("Verifying duplicate album is rejected");
		verify(galleryPage.isToastDisplayed("Album with this name already exists"),
				"Error toast 'Album with this name already exists' displayed", "Duplicate album error toast was not displayed");

		step("Reloading and confirming only one album has this name");
		galleryPage.refreshGalleryPage();
		galleryPage.searchAlbum(albumName);
		verifyEquals(galleryPage.getMatchingAlbumCount(albumName), 1, "Number of albums named '" + albumName + "'");
		pass("Duplicate album creation is prevented");
	}

	@Test(groups = { "regression" }, priority = 5, dependsOnMethods = "verify_CreateGalleryAlbum", description = "Edit Gallery Album Test")
	public void verify_EditGalleryAlbum() {
		step("Locating created album '" + albumName + "'");
		galleryPage.openGalleryPage();
		galleryPage.searchAlbum(albumName);
		verify(galleryPage.isAlbumDisplayed(albumName), "Created album found in the list", "Created album '" + albumName + "' was not found");

		step("Clicking Edit (inline edit)");
		galleryPage.clickEditAlbum(albumName);
		verifyEquals(galleryPage.getInlineEditValue(), albumName, "Inline edit box pre-filled with the album name");

		step("Updating album name -> '" + updatedAlbumName + "'");
		galleryPage.updateAlbumName(updatedAlbumName);

		step("Saving the update (Save)");
		galleryPage.clickSaveInlineEdit();
		verify(galleryPage.isToastDisplayed("Updated successfully"), "Gallery album updated successfully (toast 'Updated successfully' displayed)",
				"Success toast 'Updated successfully' was not displayed");
		currentAlbumName = updatedAlbumName;
		verify(galleryPage.isInlineEditClosed(), "Inline edit closed after saving", "Inline edit box is still open after saving");

		step("Verifying the updated album in the list");
		verifyUpdatedAlbum();

		step("Verifying the old album name no longer exists");
		galleryPage.searchAlbum(albumName);
		verify(galleryPage.getMatchingAlbumCount(albumName) == 0, "Old name '" + albumName + "' was replaced by the updated name",
				"Old album name '" + albumName + "' is still displayed");

		step("Reloading the page and verifying the update is persisted");
		galleryPage.refreshGalleryPage();
		verifyUpdatedAlbum();

		step("Re-opening inline edit, verifying the saved value and cancelling");
		galleryPage.clickEditAlbum(updatedAlbumName);
		verifyEquals(galleryPage.getInlineEditValue(), updatedAlbumName, "Persisted album name in the inline edit box");
		galleryPage.cancelInlineEdit();
		verify(galleryPage.isInlineEditClosed(), "Inline edit closed on Cancel", "Inline edit box is still open after Cancel");
		verify(galleryPage.isAlbumDisplayed(updatedAlbumName), "Album name unchanged after Cancel",
				"Album '" + updatedAlbumName + "' is not displayed after cancelling the edit");
		pass("Updated gallery album is persisted after reload");
	}

	@Test(groups = { "regression" }, priority = 6, dependsOnMethods = "verify_CreateGalleryAlbum", description = "Cancel Delete Gallery Album Test")
	public void verify_CancelDeleteKeepsAlbum() {
		step("Locating test album '" + currentAlbumName + "'");
		galleryPage.openGalleryPage();
		galleryPage.searchAlbum(currentAlbumName);

		step("Clicking Delete");
		String dialogText = galleryPage.clickDeleteAlbum(currentAlbumName);
		verify(dialogText.contains("Are you sure you want to delete \"" + currentAlbumName + "\""),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Clicking Cancel on the delete confirmation");
		verify(galleryPage.cancelDelete(), "Delete confirmation dialog closed on Cancel",
				"'Cancel' did not close the 'Delete Album' confirmation dialog");

		step("Verifying the album was not deleted");
		galleryPage.refreshGalleryPage();
		galleryPage.searchAlbum(currentAlbumName);
		verify(galleryPage.isAlbumDisplayed(currentAlbumName), "Album still exists after cancelling the delete",
				"Album '" + currentAlbumName + "' was deleted although delete was cancelled");
	}

	@Test(groups = { "sanity", "regression" }, priority = 7, dependsOnMethods = "verify_CreateGalleryAlbum", description = "Delete Gallery Album Test")
	public void verify_DeleteGalleryAlbum() {
		step("Locating test album '" + currentAlbumName + "'");
		galleryPage.openGalleryPage();
		galleryPage.searchAlbum(currentAlbumName);
		verify(galleryPage.isAlbumDisplayed(currentAlbumName), "Test album found in the list",
				"Test album '" + currentAlbumName + "' was not found");

		step("Clicking Delete");
		String dialogText = galleryPage.clickDeleteAlbum(currentAlbumName);

		step("Delete confirmation dialog displayed");
		verify(dialogText.contains("Are you sure you want to delete \"" + currentAlbumName + "\""),
				"Delete confirmation dialog displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Confirming deletion (Delete)");
		galleryPage.confirmDelete();
		verify(galleryPage.isToastDisplayed("Album deleted successfully"),
				"Gallery album deleted successfully (toast 'Album deleted successfully' displayed)",
				"Success toast 'Album deleted successfully' was not displayed");
		albumDeleted = true;

		step("Verifying gallery album deletion");
		galleryPage.refreshGalleryPage();
		galleryPage.searchAlbum(currentAlbumName);
		verify(galleryPage.isAlbumDeleted(currentAlbumName), "Deleted album is no longer displayed in the list ('No albums found')",
				"Deleted album '" + currentAlbumName + "' is still displayed in the list");
	}

	private void verifyUpdatedAlbum() {
		galleryPage.searchAlbum(updatedAlbumName);
		verify(galleryPage.isAlbumDisplayed(updatedAlbumName), "Updated album is displayed in the list",
				"Updated album '" + updatedAlbumName + "' was not displayed");
		Map<String, String> row = galleryPage.getAlbumRowDetails(updatedAlbumName);
		verifyEquals(row.get("name"), updatedAlbumName, "Updated Album Name");
	}

	// Best-effort cleanup so a failed run does not leave the automation album behind
	private void cleanUpTestAlbum() {
		if (!albumCreated || albumDeleted) {
			logger.info("[CLEANUP] No cleanup needed - automation album " + (albumCreated ? "already deleted" : "was not created"));
			return;
		}
		try {
			logger.info("[CLEANUP] Test album was not deleted by the tests - cleaning up: " + currentAlbumName);
			galleryPage.openGalleryPage();
			for (String name : new String[] { currentAlbumName, albumName, updatedAlbumName }) {
				galleryPage.searchAlbum(name);
				if (galleryPage.getMatchingAlbumCount(name) > 0) {
					galleryPage.clickDeleteAlbum(name);
					galleryPage.confirmDelete();
					logger.info("[CLEANUP] Album '" + name + "' deleted: " + galleryPage.isToastDisplayed("Album deleted successfully"));
					return;
				}
			}
			logger.info("[CLEANUP] Test album not found - nothing to clean up");
		} catch (Exception e) {
			logger.error("[CLEANUP] Cleanup failed - delete album manually: " + currentAlbumName + " | " + firstLine(e.getMessage()));
		}
	}

	@AfterClass(alwaysRun = true)
	public void setdown() {
		cleanUpTestAlbum();
		logger.info("[MODULE END] Settings > Gallery");
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
		private static final Logger log = LogManager.getLogger(TC_Gallery.class);

		@Override
		public void onTestSkipped(ITestResult result) {
			// listeners are suite-wide in TestNG - log only this class's skipped tests
			if (result.getTestClass().getRealClass() != TC_Gallery.class) {
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
