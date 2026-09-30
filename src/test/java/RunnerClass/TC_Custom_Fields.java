package RunnerClass;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import TestUtility.Non_WebDriver_Util;
import TestUtility.TestExecutionListener;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

/**
 * Settings > Custom Fields - complete Custom Field lifecycle for all four modules in ONE class:
 *   Project -> Settings > Projects > Project Custom Fields
 *   Job     -> Settings > Jobs > Job Custom Fields
 *   Quote   -> Settings > Quotes & Invoices > Quote Custom Fields
 *   Invoice -> Settings > Quotes & Invoices > Invoice Custom Fields
 * Each lifecycle: Navigate -> Create -> Verify Created -> Edit -> Update -> Verify Updated -> Delete -> Verify Deleted.
 *
 * All four modules share the same field builder: a 'Single Line Text' field is dragged from the 'Fields' palette into
 * the 'Default field groups' group, configured in the 'Edit ...' side panel (Field Name / Description) and saved.
 * The builder shows no success toasts, so every result is verified from the field list, the group field count and a reload.
 *
 * Every module uses its own unique field name (timestamp suffix); @AfterClass deletes any field a failed test left behind.
 * Reuses: Baseclass (driver/config/logger), DashboardPage (Dashboard check), SettingsPage (Settings navigation),
 * Non_WebDriver_Util (waits/clicks).
 *
 * Execution log format (Baseclass log4j2 logger -> console + logs/automation-log.log), every line tagged with the module:
 * [MODULE START] / [TEST START] / [STEP n] / [ACTION] / [VERIFY] / [PASS] / [WARN] / [FAIL] / [TEST END] / [MODULE END]
 */
@Listeners({ TestExecutionListener.class, TC_Custom_Fields.SkippedTestLogger.class })
public class TC_Custom_Fields extends Baseclass {

	private DashboardPage dashboardPage;
	private SettingsPage settingsPage;
	private WebDriver driver;

	// ==========================================
	// Custom Field builder locators (shared by all four modules)
	// ==========================================
	private static final String MENU_GROUP = "//a[contains(@class,'group') and normalize-space()='%s']";
	private static final String MENU_ITEM = "//a[@href='%s']";
	private static final By BUILDER_TITLE = By.xpath("//i[contains(@class,'ti-arrow-left')]/following::h2[1]");
	private static final By BUILDER_BACK_BUTTON = By.xpath("//i[contains(@class,'ti-arrow-left')]/parent::button");
	private static final By FIELDS_PALETTE = By.xpath("//h2[normalize-space()='Fields']");
	private static final String PALETTE_ITEM = "//h2[normalize-space()='Fields']/following::li[contains(@class,'cdk-drag')][.//p[normalize-space()='%s']][1]";
	private static final String DEFAULT_GROUP = "Default field groups";
	private static final By DEFAULT_GROUP_CARD = By.xpath("//div[@role='button'][.//span[normalize-space()='" + DEFAULT_GROUP + "']]");
	private static final By DEFAULT_GROUP_FIELD_COUNT = By.xpath("//div[@role='button'][.//span[normalize-space()='" + DEFAULT_GROUP + "']]//span[contains(@class,'field-count')]");
	private static final String FIELD_LIST = "//ul[contains(@class,'created-field-container')]";
	private static final By FIELD_CARDS = By.xpath(FIELD_LIST + "/li");
	private static final String FIELD_CARD_BY_NAME = FIELD_LIST + "/li[.//span[normalize-space()='%s']]";
	private static final String FIELD_EDIT_BUTTON = ".//button[contains(@class,'edit-field-btn')]";
	private static final String FIELD_DELETE_BUTTON = ".//button[contains(@class,'delete-field-btn')]";
	private static final String FIELD_DESCRIPTION = ".//p[@title]";
	// 'Edit <field>' side panel
	private static final By FIELD_NAME_INPUT = By.id("fieldName");
	private static final By FIELD_DESCRIPTION_INPUT = By.id("description");
	private static final String PANEL_TITLE = "//*[normalize-space(text())='Edit %s']";
	private static final By PANEL_SAVE_BUTTON = By.xpath("//button[normalize-space()='Save' and contains(@class,'bg-primary')]");
	// delete confirmation
	private static final By DIALOG = By.xpath("//mat-dialog-container");
	private static final String DIALOG_BUTTON = "//mat-dialog-container//button[normalize-space()='%s']";

	// ==========================================
	// Module definitions + per-module test data
	// ==========================================
	private static class CustomFieldModule {
		final String name;           // Project / Job / Quote / Invoice
		final String menuGroup;      // Settings side-menu group
		final String menuItem;       // Settings side-menu item text
		final String path;           // builder URL path
		final String builderTitle;   // builder heading
		String fieldName;
		String updatedFieldName;
		String currentFieldName;     // name currently saved in the application
		String builderUrl;
		boolean created;
		boolean deleted;

		CustomFieldModule(String name, String menuGroup, String menuItem, String path, String builderTitle) {
			this.name = name;
			this.menuGroup = menuGroup;
			this.menuItem = menuItem;
			this.path = path;
			this.builderTitle = builderTitle;
		}
	}

	private final Map<String, CustomFieldModule> modules = new LinkedHashMap<>();
	private final String uniqueSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
	private String fieldType;
	private String description;
	private String updatedDescription;

	// Execution log state
	private String currentModule = "Settings";
	private String currentTestName;
	private int stepNumber;
	private String currentStep;
	private String failedExpectation;
	private String failedActual;

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		logger.info("==================================================================");
		logger.info("[MODULE START] Settings > Custom Fields (Project / Job / Quote / Invoice)");
		// shared browser session - launched and logged in once by SuiteSession (TestExecutionListener)
		driver = Baseclass.getDriver();
		dashboardPage = new DashboardPage();
		settingsPage = new SettingsPage();

		modules.put("Project", new CustomFieldModule("Project", "Projects", "Project Custom Fields", "/settings_new/custom_fields/project", "Projects Fields"));
		modules.put("Job", new CustomFieldModule("Job", "Jobs", "Job Custom Fields", "/settings_new/custom_fields/jobs", "Job Fields"));
		modules.put("Quote", new CustomFieldModule("Quote", "Quotes & Invoices", "Quote Custom Fields", "/settings_new/custom_fields/estimates", "Quotation Fields"));
		modules.put("Invoice", new CustomFieldModule("Invoice", "Quotes & Invoices", "Invoice Custom Fields", "/settings_new/custom_fields/invoices", "Invoice Fields"));

		fieldType = prop.getProperty("customField_FieldType");
		description = prop.getProperty("customField_Description") + " " + uniqueSuffix;
		updatedDescription = prop.getProperty("customField_UpdatedDescription") + " " + uniqueSuffix;
		String updatedWord = prop.getProperty("customField_UpdatedNameWord");
		for (CustomFieldModule module : modules.values()) {
			String prefix = prop.getProperty("customField_" + module.name + "_NamePrefix");
			module.fieldName = prefix + " " + uniqueSuffix;
			module.updatedFieldName = prefix + " " + updatedWord + " " + uniqueSuffix;
		}

		logger.info("[TEST DATA] Environment: " + prop.getProperty("baseURL") + " | Company: " + prop.getProperty("company_Name")
				+ " | Browser: " + prop.getProperty("browser"));
		logger.info("[TEST DATA] Field type: '" + fieldType + "' | Group: '" + DEFAULT_GROUP + "' | Description: '" + description
				+ "' -> '" + updatedDescription + "'");
		for (CustomFieldModule module : modules.values()) {
			logger.info("[TEST DATA] [" + module.name + "] Field Name: '" + module.fieldName + "' -> '" + module.updatedFieldName + "'");
		}
		logger.info("==================================================================");
	}

	// ==========================================
	// Tests
	// ==========================================
	@Test(groups = { "sanity", "regression" }, priority = 1, description = "Navigate to Settings Test")
	public void verify_NavigateToSettings() {
		currentModule = "Settings";
		step("Starting from Dashboard (shared authenticated session)");
		verify(dashboardPage.isDashboardDisplayed(), "Dashboard is displayed - test starts from Dashboard",
				"Dashboard is not displayed at the start of the test");

		step("Navigating to Settings through the Profile menu");
		settingsPage.navigateToSettings();
		verify(settingsPage.isSettingsHomeDisplayed(), "Settings home page is displayed", "Settings home page is not displayed");
		// the dialer can open late after login and cover the right side of the page
		dashboardPage.minimizeZuperConnectDialer(2);
	}

	@Test(groups = { "sanity", "regression" }, priority = 2, dependsOnMethods = "verify_NavigateToSettings", description = "Project Custom Field Lifecycle")
	public void projectCustomFieldLifecycle() {
		runCustomFieldLifecycle(modules.get("Project"));
	}

	@Test(groups = { "regression" }, priority = 3, dependsOnMethods = "verify_NavigateToSettings", description = "Job Custom Field Lifecycle")
	public void jobCustomFieldLifecycle() {
		runCustomFieldLifecycle(modules.get("Job"));
	}

	@Test(groups = { "regression" }, priority = 4, dependsOnMethods = "verify_NavigateToSettings", description = "Quote Custom Field Lifecycle")
	public void quoteCustomFieldLifecycle() {
		runCustomFieldLifecycle(modules.get("Quote"));
	}

	@Test(groups = { "regression" }, priority = 5, dependsOnMethods = "verify_NavigateToSettings", description = "Invoice Custom Field Lifecycle")
	public void invoiceCustomFieldLifecycle() {
		runCustomFieldLifecycle(modules.get("Invoice"));
	}

	// Navigate -> Create -> Verify -> Edit -> Update -> Verify -> Delete -> Verify for one module
	private void runCustomFieldLifecycle(CustomFieldModule m) {
		currentModule = m.name;
		String cf = m.name + " Custom Field";

		// ---------- Navigate ----------
		step("Navigate to " + m.name + " Custom Fields (Settings > " + m.menuGroup + " > " + m.menuItem + ")");
		openCustomFieldBuilder(m);
		verifyEquals(getBuilderTitle(), m.builderTitle, m.name + " Custom Fields builder heading");
		selectDefaultGroup();
		int initialFieldCount = getDefaultGroupFieldCount();
		logger.info("[INFO] [" + m.name + "] '" + DEFAULT_GROUP + "' has " + initialFieldCount + " fields before the test");

		// ---------- Create ----------
		step("Create " + cf + " - dragging '" + fieldType + "' into '" + DEFAULT_GROUP + "'");
		dragFieldTypeIntoGroup(fieldType);
		verify(isFieldPanelOpen(fieldType), "'Edit " + fieldType + "' panel opened for the new " + cf,
				"'Edit " + fieldType + "' panel did not open after dropping the field");

		step("Enter " + cf + " details -> Field Name: '" + m.fieldName + "', Description: '" + description + "'");
		typeInto(FIELD_NAME_INPUT, "Field Name", m.fieldName);
		typeInto(FIELD_DESCRIPTION_INPUT, "Description", description);

		step("Save " + cf);
		clickPanelSave();
		verify(isFieldPanelClosed(), cf + " panel closed after Save", cf + " panel is still open after Save");
		m.created = true;
		m.currentFieldName = m.fieldName;

		step("Verify " + cf + " created");
		verify(isFieldDisplayed(m.fieldName), cf + " created - '" + m.fieldName + "' displayed in '" + DEFAULT_GROUP + "'",
				cf + " '" + m.fieldName + "' was not displayed after saving");
		verifyEquals(getFieldDescription(m.fieldName), description, cf + " description");
		verifyEquals(waitForDefaultGroupFieldCount(initialFieldCount + 1), initialFieldCount + 1, "'" + DEFAULT_GROUP + "' field count after create");

		step("Reload and verify " + cf + " persisted");
		reloadBuilder(m);
		verify(isFieldDisplayed(m.fieldName), cf + " still displayed after reload", cf + " '" + m.fieldName + "' disappeared after reload");
		pass(cf + " created successfully");

		// ---------- Edit / Update ----------
		step("Edit " + cf + " '" + m.fieldName + "'");
		clickEditField(m.fieldName);
		verify(isFieldPanelOpen(m.fieldName), "'Edit " + m.fieldName + "' panel opened", "'Edit " + m.fieldName + "' panel did not open");
		verifyEquals(getFieldValue(FIELD_NAME_INPUT), m.fieldName, cf + " edit panel Field Name");
		verifyEquals(getFieldValue(FIELD_DESCRIPTION_INPUT), description, cf + " edit panel Description");

		step("Update " + cf + " -> Field Name: '" + m.updatedFieldName + "', Description: '" + updatedDescription + "'");
		typeInto(FIELD_NAME_INPUT, "Field Name", m.updatedFieldName);
		typeInto(FIELD_DESCRIPTION_INPUT, "Description", updatedDescription);
		clickPanelSave();
		verify(isFieldPanelClosed(), cf + " panel closed after Save", cf + " panel is still open after Save");
		m.currentFieldName = m.updatedFieldName;

		step("Verify updated " + cf);
		verify(isFieldDisplayed(m.updatedFieldName), "Updated " + cf + " '" + m.updatedFieldName + "' displayed",
				"Updated " + cf + " '" + m.updatedFieldName + "' was not displayed");
		verifyEquals(getFieldDescription(m.updatedFieldName), updatedDescription, "Updated " + cf + " description");
		verify(getMatchingFieldCount(m.fieldName) == 0, "Old name '" + m.fieldName + "' was replaced by the updated name",
				"Old " + cf + " name '" + m.fieldName + "' is still displayed");

		step("Reload and verify updated " + cf + " persisted");
		reloadBuilder(m);
		verify(isFieldDisplayed(m.updatedFieldName), "Updated " + cf + " still displayed after reload",
				"Updated " + cf + " '" + m.updatedFieldName + "' disappeared after reload");
		verifyEquals(getFieldDescription(m.updatedFieldName), updatedDescription, "Persisted " + cf + " description");
		verify(getMatchingFieldCount(m.fieldName) == 0, "Old " + cf + " name not shown after reload",
				"Old " + cf + " name '" + m.fieldName + "' is displayed again after reload");
		pass(cf + " updated successfully");

		// ---------- Delete ----------
		step("Delete " + cf + " - verifying Cancel keeps the field");
		String dialogText = clickDeleteField(m.updatedFieldName);
		verify(dialogText.contains("Are you sure you want to delete \"" + m.updatedFieldName + "\""),
				cf + " delete confirmation displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);
		verify(clickDialogButton("Cancel"), cf + " delete confirmation closed on Cancel", "'Cancel' did not close the delete confirmation");
		verify(isFieldDisplayed(m.updatedFieldName), cf + " not deleted after Cancel", cf + " was deleted although delete was cancelled");

		step("Delete " + cf + " '" + m.updatedFieldName + "' and confirm");
		clickDeleteField(m.updatedFieldName);
		verify(clickDialogButton("Delete"), cf + " delete confirmation closed after 'Delete'", "Delete confirmation is still open after 'Delete'");

		step("Verify " + cf + " deleted");
		verify(isFieldRemoved(m.updatedFieldName), cf + " deleted - no longer displayed in '" + DEFAULT_GROUP + "'",
				cf + " '" + m.updatedFieldName + "' is still displayed after delete");
		m.deleted = true;
		verifyEquals(waitForDefaultGroupFieldCount(initialFieldCount), initialFieldCount, "'" + DEFAULT_GROUP + "' field count after delete");

		step("Reload and verify " + cf + " stays deleted");
		reloadBuilder(m);
		verify(getMatchingFieldCount(m.updatedFieldName) == 0, cf + " not displayed after reload",
				cf + " '" + m.updatedFieldName + "' is displayed again after reload");
		pass(cf + " deleted successfully");
	}

	// ==========================================
	// Custom Field builder actions
	// ==========================================
	// Settings side menu -> <group> -> '<Module> Custom Fields'
	private void openCustomFieldBuilder(CustomFieldModule m) {
		leaveBuilderIfOpen();
		By menuItem = By.xpath(String.format(MENU_ITEM, m.path));
		if (Non_WebDriver_Util.waitWithoutImplicitWait(driver, 2, ExpectedConditions.presenceOfElementLocated(menuItem)) == null) {
			if (Non_WebDriver_Util.waitWithoutImplicitWait(driver, 2, ExpectedConditions.presenceOfElementLocated(By.xpath(String.format(MENU_GROUP, m.menuGroup)))) == null) {
				logAction("[" + m.name + "] Settings side menu not shown - opening Settings");
				settingsPage.navigateToSettings();
			}
			logAction("[" + m.name + "] Expanding '" + m.menuGroup + "' group in the Settings side menu");
			clickElement(driver.findElement(By.xpath(String.format(MENU_GROUP, m.menuGroup))), "'" + m.menuGroup + "' menu group");
			if (Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, ExpectedConditions.presenceOfElementLocated(menuItem)) == null) {
				throw new IllegalStateException("❌ '" + m.menuItem + "' menu item not found under '" + m.menuGroup + "' (locator: " + menuItem + ")");
			}
		}
		logAction("[" + m.name + "] Clicking '" + m.menuItem + "' in the Settings side menu");
		clickElement(driver.findElement(menuItem), "'" + m.menuItem + "' menu item");
		waitForBuilder(m);
	}

	// The builder is a full-screen page without the Settings side menu - its back arrow returns to Settings
	private void leaveBuilderIfOpen() {
		if (driver.getCurrentUrl().contains("/settings_new/custom_fields/")) {
			WebElement back = Non_WebDriver_Util.findIfVisible(driver, BUILDER_BACK_BUTTON, 5);
			if (back != null) {
				logAction("[" + currentModule + "] Leaving the current Custom Fields builder (back arrow)");
				back.click();
				Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, d -> !d.getCurrentUrl().contains("/settings_new/custom_fields/"));
			}
		}
	}

	private void waitForBuilder(CustomFieldModule m) {
		logAction("[" + m.name + "] Waiting for the " + m.name + " Custom Fields builder to load");
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.withMessage(m.name + " Custom Fields URL (" + m.path + ") - current URL: " + driver.getCurrentUrl())
				.until(ExpectedConditions.urlContains(m.path));
		if (Non_WebDriver_Util.findIfVisible(driver, FIELDS_PALETTE, 30) == null) {
			throw new IllegalStateException("❌ 'Fields' palette did not load (locator: " + FIELDS_PALETTE + ")");
		}
		if (Non_WebDriver_Util.findIfVisible(driver, DEFAULT_GROUP_CARD, 30) == null) {
			throw new IllegalStateException("❌ '" + DEFAULT_GROUP + "' group did not load (locator: " + DEFAULT_GROUP_CARD + ")");
		}
		// field cards are rendered after the API call (loading skeleton first)
		if (Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, d -> !d.findElements(FIELD_CARDS).isEmpty()) == null) {
			logger.warn("[ACTION] [" + m.name + "] No field cards rendered within 20s - the group may be empty");
		}
		m.builderUrl = driver.getCurrentUrl().split("\\?")[0];
		logAction("[" + m.name + "] Builder loaded: '" + getBuilderTitle() + "' (" + getDefaultGroupFieldCount() + " fields in '" + DEFAULT_GROUP + "')");
	}

	private void reloadBuilder(CustomFieldModule m) {
		logAction("[" + m.name + "] Reloading the " + m.name + " Custom Fields builder");
		Non_WebDriver_Util.refreshPage(driver);
		waitForBuilder(m);
		selectDefaultGroup();
	}

	private String getBuilderTitle() {
		WebElement title = Non_WebDriver_Util.findIfVisible(driver, BUILDER_TITLE, 10);
		return title == null ? "" : title.getText().trim();
	}

	private void selectDefaultGroup() {
		WebElement card = driver.findElement(DEFAULT_GROUP_CARD);
		if (!card.getAttribute("class").contains("custom-fieldsgroup-active")) {
			logAction("[" + currentModule + "] Selecting field group '" + DEFAULT_GROUP + "'");
			clickElement(card, "'" + DEFAULT_GROUP + "' group");
			Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, d -> d.findElement(DEFAULT_GROUP_CARD).getAttribute("class").contains("custom-fieldsgroup-active"));
		}
	}

	// The group card count refreshes a few seconds after a save/delete - wait for the expected value, return the last value read
	private int waitForDefaultGroupFieldCount(int expected) {
		Integer matched = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> getDefaultGroupFieldCount() == expected ? expected : null);
		return matched != null ? matched : getDefaultGroupFieldCount();
	}

	// '6 Fields' shown on the group card (-1 when it cannot be read)
	private int getDefaultGroupFieldCount() {
		List<WebElement> count = driver.findElements(DEFAULT_GROUP_FIELD_COUNT);
		if (count.isEmpty()) {
			return -1;
		}
		String text = Non_WebDriver_Util.getInnerText(driver, count.get(0)).replaceAll("[^0-9]", "");
		return text.isEmpty() ? -1 : Integer.parseInt(text);
	}

	// CDK drag & drop: palette item -> top of the group's field list
	private void dragFieldTypeIntoGroup(String type) {
		By paletteItem = By.xpath(String.format(PALETTE_ITEM, type));
		for (int attempt = 1; attempt <= 2; attempt++) {
			WebElement source = Non_WebDriver_Util.findIfVisible(driver, paletteItem, 15);
			if (source == null) {
				throw new IllegalStateException("❌ '" + type + "' not found in the Fields palette (locator: " + paletteItem + ")");
			}
			List<WebElement> cards = driver.findElements(FIELD_CARDS);
			WebElement target = cards.isEmpty() ? driver.findElement(By.xpath(FIELD_LIST)) : cards.get(0);
			logAction("[" + currentModule + "] Dragging '" + type + "' from the Fields palette into '" + DEFAULT_GROUP + "'"
					+ (attempt > 1 ? " (attempt " + attempt + ")" : ""));
			new Actions(driver).moveToElement(source).clickAndHold().pause(Duration.ofMillis(300))
					.moveByOffset(15, 15).pause(Duration.ofMillis(300))
					.moveToElement(target).pause(Duration.ofMillis(500))
					.moveByOffset(0, 10).pause(Duration.ofMillis(500))
					.release().perform();
			if (Non_WebDriver_Util.findIfVisible(driver, FIELD_NAME_INPUT, 10) != null) {
				logAction("[" + currentModule + "] '" + type + "' dropped - field panel opened");
				return;
			}
			logger.warn("[ACTION] [" + currentModule + "] Field panel did not open after the drop - retrying the drag");
		}
		throw new IllegalStateException("❌ Dragging '" + type + "' into '" + DEFAULT_GROUP + "' did not add a field (field panel not opened)");
	}

	private boolean isFieldPanelOpen(String fieldName) {
		boolean titleShown = Non_WebDriver_Util.findIfVisible(driver, By.xpath(String.format(PANEL_TITLE, fieldName)), 10) != null;
		return titleShown && Non_WebDriver_Util.findIfVisible(driver, FIELD_NAME_INPUT, 5) != null;
	}

	private boolean isFieldPanelClosed() {
		return Non_WebDriver_Util.waitForInvisibility(driver, FIELD_NAME_INPUT, 15);
	}

	private void clickPanelSave() {
		logAction("[" + currentModule + "] Clicking 'Save' in the field panel");
		WebElement save = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, ExpectedConditions.presenceOfElementLocated(PANEL_SAVE_BUTTON));
		if (save == null) {
			throw new IllegalStateException("❌ Field panel 'Save' button not found (locator: " + PANEL_SAVE_BUTTON + ")");
		}
		clickElement(save, "'Save' button");
	}

	private By fieldCard(String fieldName) {
		return By.xpath(String.format(FIELD_CARD_BY_NAME, fieldName));
	}

	private boolean isFieldDisplayed(String fieldName) {
		return Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, ExpectedConditions.presenceOfElementLocated(fieldCard(fieldName))) != null;
	}

	private int getMatchingFieldCount(String fieldName) {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(fieldCard(fieldName)).size());
		return count == null ? 0 : count;
	}

	private boolean isFieldRemoved(String fieldName) {
		return Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> d.findElements(fieldCard(fieldName)).isEmpty()) != null;
	}

	private String getFieldDescription(String fieldName) {
		List<WebElement> descriptions = driver.findElement(fieldCard(fieldName)).findElements(By.xpath(FIELD_DESCRIPTION));
		String text = descriptions.isEmpty() ? "" : descriptions.get(0).getAttribute("title").trim();
		logAction("[" + currentModule + "] Field card '" + fieldName + "' description: '" + text + "'");
		return text;
	}

	private void clickEditField(String fieldName) {
		logAction("[" + currentModule + "] Clicking Edit (pencil) on field '" + fieldName + "'");
		WebElement edit = driver.findElement(fieldCard(fieldName)).findElement(By.xpath(FIELD_EDIT_BUTTON));
		clickElement(edit, "Edit button of '" + fieldName + "'");
		// panel is pre-filled asynchronously
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> getFieldValue(FIELD_NAME_INPUT).equals(fieldName));
	}

	// Opens the delete confirmation; returns its message
	private String clickDeleteField(String fieldName) {
		logAction("[" + currentModule + "] Clicking Delete (trash) on field '" + fieldName + "'");
		WebElement delete = driver.findElement(fieldCard(fieldName)).findElement(By.xpath(FIELD_DELETE_BUTTON));
		clickElement(delete, "Delete button of '" + fieldName + "'");
		WebElement dialog = Non_WebDriver_Util.findIfVisible(driver, DIALOG, 15);
		if (dialog == null) {
			throw new IllegalStateException("❌ Delete confirmation dialog did not appear (locator: " + DIALOG + ")");
		}
		String text = Non_WebDriver_Util.getInnerText(driver, dialog).replaceAll("\\s+", " ").trim();
		logAction("[" + currentModule + "] Dialog displayed: \"" + text + "\"");
		return text;
	}

	// Clicks a confirmation dialog button and waits for the dialog to close; returns false when it stays open
	private boolean clickDialogButton(String buttonText) {
		By button = By.xpath(String.format(DIALOG_BUTTON, buttonText));
		logAction("[" + currentModule + "] Clicking dialog button '" + buttonText + "'");
		WebElement buttonElement = Non_WebDriver_Util.findIfVisible(driver, button, 15);
		if (buttonElement == null) {
			throw new IllegalStateException("❌ Dialog button not found: '" + buttonText + "' (locator: " + button + ")");
		}
		Non_WebDriver_Util.waitForBeClickable(driver, buttonElement, 10);
		buttonElement.click();
		boolean closed = Non_WebDriver_Util.waitForInvisibility(driver, DIALOG, 10);
		if (closed) {
			logAction("[" + currentModule + "] Dialog closed after clicking '" + buttonText + "'");
		} else {
			logger.warn("[ACTION] [" + currentModule + "] Dialog is still open 10s after clicking '" + buttonText + "' (locator: " + button + ")");
		}
		return closed;
	}

	// Scrolls the element to the middle (sticky side-menu headers / small window) and clicks; JS click when intercepted
	private void clickElement(WebElement element, String elementName) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
		try {
			Non_WebDriver_Util.waitForBeClickable(driver, element, 10);
			element.click();
		} catch (ElementNotInteractableException | org.openqa.selenium.TimeoutException e) {   // includes ElementClickInterceptedException
			logAction("[" + currentModule + "] " + elementName + " click was blocked (" + e.getClass().getSimpleName() + ") - clicking with JavaScript");
			Non_WebDriver_Util.jsClick(driver, element);
		}
	}

	private void typeInto(By locator, String fieldName, String value) {
		logAction("[" + currentModule + "] Entering '" + fieldName + "' = '" + value + "'");
		WebElement field = Non_WebDriver_Util.findIfVisible(driver, locator, 15);
		if (field == null) {
			throw new IllegalStateException("❌ Field '" + fieldName + "' not displayed (locator: " + locator + ")");
		}
		field.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
		field.sendKeys(value);
	}

	private String getFieldValue(By locator) {
		List<WebElement> fields = driver.findElements(locator);
		if (fields.isEmpty()) {
			return "";
		}
		String value = fields.get(0).getAttribute("value");
		return value == null ? "" : value.trim();
	}

	// ==========================================
	// Cleanup
	// ==========================================
	// Best-effort cleanup so a failed module does not leave its automation field behind
	private void cleanUpTestFields() {
		for (CustomFieldModule m : modules.values()) {
			currentModule = m.name;
			if (!m.created || m.deleted) {
				logger.info("[CLEANUP] [" + m.name + "] No cleanup needed - " + m.name + " Custom Field "
						+ (m.created ? "already deleted" : "was not created"));
				continue;
			}
			try {
				logger.info("[CLEANUP] [" + m.name + "] " + m.name + " Custom Field was not deleted by the test - cleaning up: " + m.currentFieldName);
				driver.get(m.builderUrl);
				waitForBuilder(m);
				selectDefaultGroup();
				boolean removed = false;
				for (String name : new String[] { m.currentFieldName, m.fieldName, m.updatedFieldName }) {
					if (getMatchingFieldCount(name) > 0) {
						clickDeleteField(name);
						clickDialogButton("Delete");
						removed = isFieldRemoved(name);
						logger.info("[CLEANUP] [" + m.name + "] Custom Field '" + name + "' deleted: " + removed);
						break;
					}
				}
				if (!removed) {
					logger.info("[CLEANUP] [" + m.name + "] Test Custom Field not found - nothing to clean up");
				}
			} catch (Exception e) {
				logger.error("[CLEANUP] [" + m.name + "] Cleanup failed - delete Custom Field manually: " + m.currentFieldName + " | " + firstLine(e.getMessage()));
			}
		}
	}

	@AfterClass(alwaysRun = true)
	public void setdown() {
		cleanUpTestFields();
		logger.info("[MODULE END] Settings > Custom Fields");
		logger.info("==================================================================");
	}

	// ==========================================
	// Execution logging (Baseclass logger) - every line tagged with the module
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
				logger.error("[FAIL] Module    : " + currentModule);
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
				// discard a half-finished (unsaved) field so the next module starts clean
				discardUnsavedChanges();
				break;
			default:
				logger.warn("[TEST END] " + currentTestName + " - SKIPPED");
		}
	}

	private void discardUnsavedChanges() {
		try {
			if (driver.getCurrentUrl().contains("/settings_new/custom_fields/")) {
				Non_WebDriver_Util.refreshPage(driver);
			}
		} catch (Exception e) {
			logger.warn("[WARN] [" + currentModule + "] Could not reload the builder after the failure: " + firstLine(e.getMessage()));
		}
	}

	private void step(String description) {
		stepNumber++;
		currentStep = description;
		logger.info("[STEP " + stepNumber + "] [" + currentModule + "] " + description);
		Allure.step("[" + currentModule + "] " + description);
	}

	private void pass(String message) {
		logger.info("[PASS] [" + currentModule + "] " + message);
	}

	// Logs "[VERIFY] expected", asserts 'condition', then logs "[PASS] expected" or "[FAIL] Expected / Actual" before failing
	private void verify(boolean condition, String expected, String actual) {
		logger.info("[VERIFY] [" + currentModule + "] " + expected);
		if (!condition) {
			failedExpectation = expected;
			failedActual = actual;
			logger.error("[FAIL] [" + currentModule + "] Expected: " + expected);
			logger.error("[FAIL] [" + currentModule + "] Actual  : " + actual);
			Allure.step("[" + currentModule + "] FAILED - Expected: " + expected + " | Actual: " + actual, Status.FAILED);
			Assert.fail("[" + currentModule + "] " + actual);
		}
		logger.info("[PASS] [" + currentModule + "] " + expected);
	}

	private void verifyEquals(Object actual, Object expected, String what) {
		logger.info("[VERIFY] [" + currentModule + "] " + what + " = '" + expected + "'");
		if (!Objects.equals(actual, expected)) {
			failedExpectation = what + " = '" + expected + "'";
			failedActual = what + " = '" + actual + "'";
			logger.error("[FAIL] [" + currentModule + "] Expected: " + failedExpectation);
			logger.error("[FAIL] [" + currentModule + "] Actual  : " + failedActual);
			Allure.step("[" + currentModule + "] FAILED - Expected: " + failedExpectation + " | Actual: " + failedActual, Status.FAILED);
			Assert.assertEquals(actual, expected, "[" + currentModule + "] " + what + " mismatch");
		}
		logger.info("[PASS] [" + currentModule + "] " + what + " = '" + actual + "'");
	}

	private String currentUrl() {
		try {
			return Objects.toString(driver.getCurrentUrl());
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
		private static final Logger log = LogManager.getLogger(TC_Custom_Fields.class);

		@Override
		public void onTestSkipped(ITestResult result) {
			// listeners are suite-wide in TestNG - log only this class's skipped tests
			if (result.getTestClass().getRealClass() != TC_Custom_Fields.class) {
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
