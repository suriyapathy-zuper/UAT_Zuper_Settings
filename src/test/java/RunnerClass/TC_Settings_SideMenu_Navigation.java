package RunnerClass;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import TestUtility.Non_WebDriver_Util;

/**
 * Settings > side menu navigation:
 * Dashboard -> Settings -> side menu Search 'Project' (results waited for and verified) -> Search 'Custom Fields'
 * -> Project Custom Fields (page + side menu location Projects > Project Custom Fields) -> Back -> Projects > General Settings
 * -> 'Enable Timelog for Projects' and 'Enable Secondary Contacts': current state -> opposite -> Save -> reload (persisted)
 * -> original state restored -> Dashboard (TestExecutionListener).
 *
 * In UAT the side menu search for 'Project' lists Projects > General Settings / Project Category, while Project Custom
 * Fields is listed for 'Custom Fields' - so the Custom Fields page is opened from the 'Custom Fields' search results.
 * Both switches are validated from their CURRENT state; @AfterClass restores any switch a failed test left changed.
 * Shared browser session, Dashboard navigation and execution logging come from BaseSettingsTest.
 */
public class TC_Settings_SideMenu_Navigation extends BaseSettingsTest {

	private static final int SEARCH_RESULT_TIMEOUT_SECONDS = 20;
	private static final String PROJECT_SEARCH = "Project";
	private static final String CUSTOM_FIELDS_SEARCH = "Custom Fields";
	private static final String PROJECTS_GROUP = "Projects";
	private static final String PROJECT_CUSTOM_FIELDS = "Project Custom Fields";
	private static final String PROJECT_CUSTOM_FIELDS_PATH = "/settings_new/custom_fields/project";
	private static final String PROJECT_CUSTOM_FIELDS_HEADING = "Projects Fields";
	private static final String GENERAL_SETTINGS_PATH = "/settings_new/project/configuration";
	private static final String TIMELOG = "Enable Timelog for Projects";
	private static final String SECONDARY_CONTACTS = "Enable Secondary Contacts";

	private String pageBeforeCustomFields;
	// original switch state while a switch is changed (null = nothing to restore)
	private Boolean timelogToRestore;
	private Boolean secondaryContactsToRestore;

	@Override
	protected String getSuiteName() {
		return "Settings > Side Menu Navigation";
	}

	// Runs after BaseSettingsTest.setUpPages()
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		logger.info("[TEST DATA] Search: '" + PROJECT_SEARCH + "', '" + CUSTOM_FIELDS_SEARCH + "' | Switches: '" + TIMELOG + "', '"
				+ SECONDARY_CONTACTS + "'");
		logger.info("==================================================================");
	}

	@Test(groups = { "sanity", "regression" }, priority = 1, description = "Side Menu Search - Project Test")
	public void verify_SideMenuSearchProject() {
		openSettingsFromDashboard();
		pass("Settings page loaded");

		step("Searching for '" + PROJECT_SEARCH + "' in the Settings side menu");
		verify(settingsPage.isSideMenuSearchDisplayed(), "Settings side menu Search field is displayed", "Settings side menu Search field is not available");
		verifyEquals(settingsPage.searchSideMenu(PROJECT_SEARCH), PROJECT_SEARCH, "Side menu Search text");

		logger.info("[INFO] Waiting for Project search result (up to " + SEARCH_RESULT_TIMEOUT_SECONDS + "s)");
		WebElement generalSettings = settingsPage.waitForSideMenuResult(PROJECTS_GROUP, "General Settings", SEARCH_RESULT_TIMEOUT_SECONDS);
		verify(generalSettings != null, "Project search result 'Projects > General Settings' is displayed and clickable",
				"Project search result 'Projects > General Settings' not loaded within " + SEARCH_RESULT_TIMEOUT_SECONDS + "s - results: "
						+ settingsPage.getSideMenuEntries());
		verify(settingsPage.waitForSideMenuResult(PROJECTS_GROUP, "Project Category", 5) != null,
				"Project search result 'Projects > Project Category' is displayed and clickable",
				"'Projects > Project Category' is not listed for '" + PROJECT_SEARCH + "'");
		logger.info("[INFO] Search results for '" + PROJECT_SEARCH + "': " + settingsPage.getSideMenuEntries());
		pass("Project search result loaded");
	}

	@Test(groups = { "sanity", "regression" }, priority = 2, dependsOnMethods = "verify_SideMenuSearchProject",
			description = "Open Project Custom Fields from Side Menu Search Test")
	public void verify_OpenProjectCustomFields() {
		step("Searching for '" + CUSTOM_FIELDS_SEARCH + "' to reach Project Custom Fields");
		verifyEquals(settingsPage.searchSideMenu(CUSTOM_FIELDS_SEARCH), CUSTOM_FIELDS_SEARCH, "Side menu Search text");

		step("Verifying Project Custom Fields search result");
		logger.info("[INFO] Waiting for '" + PROJECTS_GROUP + " > " + PROJECT_CUSTOM_FIELDS + "' search result");
		WebElement result = settingsPage.waitForSideMenuResult(PROJECTS_GROUP, PROJECT_CUSTOM_FIELDS, SEARCH_RESULT_TIMEOUT_SECONDS);
		verify(result != null, "Project Custom Fields result displayed and clickable",
				"'" + PROJECTS_GROUP + " > " + PROJECT_CUSTOM_FIELDS + "' not loaded within " + SEARCH_RESULT_TIMEOUT_SECONDS + "s - results: "
						+ settingsPage.getSideMenuEntries());

		step("Opening Project Custom Fields");
		pageBeforeCustomFields = pageUrl();
		settingsPage.openSideMenuResult(PROJECTS_GROUP, PROJECT_CUSTOM_FIELDS);
		verify(pageUrl().contains(PROJECT_CUSTOM_FIELDS_PATH), "Project Custom Fields page URL contains '" + PROJECT_CUSTOM_FIELDS_PATH + "'",
				"Unexpected page opened: " + pageUrl());
		verifyEquals(settingsPage.getPageHeading(), PROJECT_CUSTOM_FIELDS_HEADING, "Project Custom Fields page heading");
		pass("Project Custom Fields page loaded");

		step("Verifying breadcrumb / navigation location: " + PROJECTS_GROUP + " > Custom Fields");
		verify(settingsPage.isSideMenuItemActive(PROJECTS_GROUP, PROJECT_CUSTOM_FIELDS),
				"Side menu highlights '" + PROJECTS_GROUP + " > " + PROJECT_CUSTOM_FIELDS + "' (Project > Custom Fields)",
				"'" + PROJECTS_GROUP + " > " + PROJECT_CUSTOM_FIELDS + "' is not the active side menu item");
		pass("Project > Custom Fields verified");
	}

	@Test(groups = { "regression" }, priority = 3, dependsOnMethods = "verify_OpenProjectCustomFields",
			description = "Back Navigation from Project Custom Fields Test")
	public void verify_BackFromProjectCustomFields() {
		step("Clicking Back on the Project Custom Fields page");
		settingsPage.clickPageBack();

		step("Verifying Back returns to the previous page: " + pageBeforeCustomFields);
		verify(waitForPageUrl(pageBeforeCustomFields), "Back navigation returned to " + pageBeforeCustomFields,
				"Back navigation opened " + pageUrl() + " instead of " + pageBeforeCustomFields);
		verify(!pageUrl().contains(PROJECT_CUSTOM_FIELDS_PATH), "Project Custom Fields page was left",
				"Still on the Project Custom Fields page after Back");
		verify(settingsPage.isSettingsHomeDisplayed() && settingsPage.isSideMenuSearchDisplayed(),
				"Returned page is loaded (Settings home content and side menu displayed - not blank)",
				"Returned page is blank or broken: " + pageUrl());
		pass("Correct page loaded after Back");
	}

	@Test(groups = { "sanity", "regression" }, priority = 4, dependsOnMethods = "verify_SideMenuSearchProject",
			description = "Open Project General Settings Test")
	public void verify_OpenProjectGeneralSettings() {
		step("Opening Projects > General Settings from the Settings side menu");
		settingsPage.clearSideMenuSearch();
		settingsPage.openProjectGeneralSettings();
		verify(pageUrl().contains(GENERAL_SETTINGS_PATH), "Project General Settings URL contains '" + GENERAL_SETTINGS_PATH + "'",
				"Unexpected page opened: " + pageUrl());
		verify(settingsPage.isSideMenuItemActive(PROJECTS_GROUP, "General Settings"), "Side menu highlights 'Projects > General Settings'",
				"'Projects > General Settings' is not the active side menu item");
		verify(settingsPage.isSettingSwitchDisplayed(TIMELOG) && settingsPage.isSettingSwitchDisplayed(SECONDARY_CONTACTS),
				"Project General Settings loaded ('" + TIMELOG + "' and '" + SECONDARY_CONTACTS + "' displayed)",
				"Project General Settings switches are not displayed");
		pass("Project General Settings loaded");
	}

	@Test(groups = { "regression" }, priority = 5, dependsOnMethods = "verify_OpenProjectGeneralSettings",
			description = "Enable Timelog for Projects Toggle Test")
	public void verify_TimelogToggle() {
		validateSwitch(TIMELOG, "Timelog", "Yes", "No", true);
	}

	@Test(groups = { "regression" }, priority = 6, dependsOnMethods = "verify_OpenProjectGeneralSettings",
			description = "Enable Secondary Contacts Toggle Test")
	public void verify_SecondaryContactsToggle() {
		validateSwitch(SECONDARY_CONTACTS, "Secondary Contacts", "Enabled", "Disabled", false);
	}

	// Current state -> opposite -> Save -> reload (persisted) -> original state restored -> Save -> reload (restored)
	private void validateSwitch(String label, String name, String onText, String offText, boolean isTimelog) {
		step("Checking " + label);
		verify(settingsPage.isSettingSwitchDisplayed(label), "'" + label + "' control is displayed", "'" + label + "' is missing");
		boolean original = settingsPage.isSettingSwitchOn(label);
		String originalText = settingsPage.getSettingSwitchText(label);
		verifyEquals(originalText, original ? onText : offText, label + " state text");
		logger.info("[INFO] Current state: " + originalText);

		step("Changing " + name + " to " + (original ? offText : onText));
		settingsPage.clickSettingSwitch(label);
		rememberOriginal(isTimelog, original);
		verifyEquals(settingsPage.isSettingSwitchOn(label), !original, label + " switch on");
		verifyEquals(settingsPage.getSettingSwitchText(label), original ? offText : onText, label + " state text");
		saveIfRequired(label);

		step("Reloading Project General Settings to verify the " + name + " change persists");
		settingsPage.reloadSettingsPage(label);
		verifyEquals(settingsPage.isSettingSwitchOn(label), !original, label + " switch on after reload");
		verifyEquals(settingsPage.getSettingSwitchText(label), original ? offText : onText, label + " state text after reload");
		pass(name + " state changed and verified");

		step("Restoring original " + name + " state (" + originalText + ")");
		settingsPage.clickSettingSwitch(label);
		saveIfRequired(label);
		settingsPage.reloadSettingsPage(label);
		verifyEquals(settingsPage.isSettingSwitchOn(label), original, label + " switch on after restore");
		verifyEquals(settingsPage.getSettingSwitchText(label), originalText, label + " state text after restore");
		rememberOriginal(isTimelog, null);
		pass("Original " + name + " state restored");
	}

	private void saveIfRequired(String label) {
		if (settingsPage.isSaveEnabled()) {
			logger.info("[INFO] Save is required for '" + label + "' - saving");
			verify(settingsPage.saveSettings(), "'" + label + "' change saved", "Save did not complete for '" + label + "'");
		} else {
			logger.info("[INFO] Save button not enabled - '" + label + "' change is applied without Save");
		}
	}

	private void rememberOriginal(boolean isTimelog, Boolean original) {
		if (isTimelog) {
			timelogToRestore = original;
		} else {
			secondaryContactsToRestore = original;
		}
	}

	private String pageUrl() {
		return getDriver().getCurrentUrl();
	}

	private boolean waitForPageUrl(String url) {
		return Non_WebDriver_Util.waitWithoutImplicitWait(getDriver(), 20, d -> d.getCurrentUrl().equals(url)) != null;
	}

	// A test that failed between change and restore leaves a switch changed - put the original state back
	@AfterClass(alwaysRun = true)
	public void setdown() {
		if (timelogToRestore == null && secondaryContactsToRestore == null) {
			return;
		}
		logger.warn("[CLEANUP] Restoring Project General Settings switches left changed by a failed test");
		try {
			if (!pageUrl().contains(GENERAL_SETTINGS_PATH)) {
				getDriver().get(pageUrl().replaceAll("^(https?://[^/]+).*$", "$1") + GENERAL_SETTINGS_PATH);
			}
			restore(TIMELOG, timelogToRestore);
			restore(SECONDARY_CONTACTS, secondaryContactsToRestore);
		} catch (Exception e) {
			logger.error("[CLEANUP] Could not restore Project General Settings: " + e.getClass().getSimpleName() + " - " + firstLine(e.getMessage()));
		}
	}

	private void restore(String label, Boolean original) {
		if (original == null) {
			return;
		}
		settingsPage.reloadSettingsPage(label);
		if (settingsPage.isSettingSwitchOn(label) != original) {
			settingsPage.clickSettingSwitch(label);
			if (settingsPage.isSaveEnabled()) {
				settingsPage.saveSettings();
			}
		}
		settingsPage.reloadSettingsPage(label);
		logger.info("[CLEANUP] '" + label + "' restored to " + settingsPage.getSettingSwitchText(label));
	}
}
