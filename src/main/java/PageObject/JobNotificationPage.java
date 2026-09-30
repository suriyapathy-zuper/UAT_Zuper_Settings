package PageObject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

/**
 * Settings > Jobs > Job Notifications > Internal Notifications > Job Reminder
 * - Job Reminder table (the page also holds 'Job Delay Alerts' and 'Job Status Alerts' tables - every row lookup
 *   is scoped to the Job Reminder section)
 * - Row menu: Edit / Clone / Deactivate / Delete Job Reminder
 * - 'New Job Reminder' / 'Edit Job Reminder' side drawer
 * - Delete confirmation dialog and toast messages
 *
 * Every UI action is logged as "[ACTION] ..." through Baseclass.logAction().
 */
public class JobNotificationPage extends Baseclass {

	private WebDriver driver;
	private String jobNotificationUrl;

	public JobNotificationPage() {
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	// ==========================================
	// Job Notifications page elements
	// ==========================================
	@FindBy(xpath = "//button[normalize-space()='Internal Notifications']")
	private WebElement internalNotificationsTab;

	@FindBy(xpath = "//h3[normalize-space()='Job Reminder']")
	private WebElement jobReminderHeader;

	private final By newJobReminderButton = By.xpath("//button[normalize-space()='New Job Reminder']");
	private final By jobReminderCountBadge = By.xpath("//h3[normalize-space()='Job Reminder']/following-sibling::span[1]");

	// Job Reminder table = the block right after the 'Job Reminder' header block
	private static final String REMINDER_SECTION = "//h3[normalize-space()='Job Reminder']/ancestor::div[contains(@class,'py-4')][1]/following-sibling::div[1]";
	private final By reminderRows = By.xpath(REMINDER_SECTION + "//cdk-row");
	private static final String ROW_BY_NAME = REMINDER_SECTION + "//cdk-row[hlm-td[1]//div[normalize-space()='%s']]";
	private static final String ROW_MENU_TRIGGER = ".//button[contains(@class,'cdk-menu-trigger')]";
	private final By rowMenuItems = By.xpath("//button[@role='menuitem']");
	private static final String ROW_MENU_ITEM = "//button[@role='menuitem' and normalize-space()='%s']";

	// ==========================================
	// Job Reminder drawer elements
	// ==========================================
	private static final String DRAWER = "//zuper-drawer[contains(@class,'notification-setup-drawer')]";
	private final By drawerTitle = By.xpath(DRAWER + "//h6");
	private final By reminderNameInput = By.xpath(DRAWER + "//input[@id='reminder_name']");
	private final By remindBeforeInput = By.xpath(DRAWER + "//input[@id='remind_before']");
	private final By alertTemplateInput = By.xpath(DRAWER + "//textarea[@id='sms_body']");
	private static final String SELECT_TRIGGER = DRAWER + "//brn-select[@id='%s']//button[@role='combobox']";
	private static final String DROPDOWN_OPTION = "//hlm-option[normalize-space()='%s']";
	private static final String DRAWER_BUTTON = DRAWER + "//button[normalize-space()='%s']";
	private final By validationMessages = By.xpath(DRAWER + "//p[contains(@class,'text-red-500') and normalize-space()]");

	// ==========================================
	// Dialogs & toasts
	// ==========================================
	private final By dialog = By.xpath("//mat-dialog-container");
	private static final String DIALOG_BUTTON = "//mat-dialog-container//button[normalize-space()='%s']";
	private static final String TOAST_MESSAGE = "//div[contains(@class,'hot-toast-message')][contains(normalize-space(),'%s')]";

	// ==========================================
	// Page actions
	// ==========================================
	public void waitForJobNotificationPageToLoad() {
		logAction("Waiting for Job Notifications page to load");
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.withMessage("Job Notifications URL (/settings_new/job/notifications) - current URL: " + driver.getCurrentUrl())
				.until(ExpectedConditions.urlContains("/settings_new/job/notifications"));
		Non_WebDriver_Util.waitForVisible(driver, internalNotificationsTab, 30);
		Non_WebDriver_Util.waitForVisible(driver, jobReminderHeader, 30);
		// reminder rows are rendered after the API call (the section may legitimately be empty)
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, d -> !d.findElements(reminderRows).isEmpty());
		jobNotificationUrl = driver.getCurrentUrl().split("\\?")[0];
		logAction("Job Notifications page loaded: " + jobNotificationUrl + " (Job Reminder rows: " + getVisibleReminderCount()
				+ ", count badge: " + getJobReminderCount() + ")");
	}

	public boolean isJobNotificationPageDisplayed() {
		return driver.getCurrentUrl().contains("/settings_new/job/notifications") && jobReminderHeader.isDisplayed();
	}

	// Job Reminder lives under the 'Internal Notifications' tab (default tab)
	public void openInternalNotificationsTab() {
		logAction("Clicking 'Internal Notifications' tab");
		Non_WebDriver_Util.waitForBeClickable(driver, internalNotificationsTab, 20);
		internalNotificationsTab.click();
		Non_WebDriver_Util.waitForVisible(driver, jobReminderHeader, 20);
	}

	public boolean isNewJobReminderButtonAvailable() {
		WebElement button = Non_WebDriver_Util.findIfVisible(driver, newJobReminderButton, 10);
		return button != null && button.isEnabled();
	}

	// Re-open the page through the URL captured when it was first opened from the UI
	public void openJobNotificationPage() {
		logAction("Opening Job Notifications page: " + jobNotificationUrl);
		driver.get(jobNotificationUrl);
		waitForJobNotificationPageToLoad();
	}

	public void refreshJobNotificationPage() {
		logAction("Reloading Job Notifications page");
		Non_WebDriver_Util.refreshPage(driver);
		waitForJobNotificationPageToLoad();
	}

	// Number shown next to the 'Job Reminder' heading (-1 when it cannot be read)
	public int getJobReminderCount() {
		List<WebElement> badge = driver.findElements(jobReminderCountBadge);
		if (badge.isEmpty()) {
			return -1;
		}
		String text = Non_WebDriver_Util.getInnerText(driver, badge.get(0)).trim();
		return text.matches("\\d+") ? Integer.parseInt(text) : -1;
	}

	public boolean isJobReminderDisplayed(String reminderName) {
		return Non_WebDriver_Util.findIfVisible(driver, rowByName(reminderName), 5) != null;
	}

	public int getMatchingReminderCount(String reminderName) {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(rowByName(reminderName)).size());
		return count == null ? 0 : count;
	}

	// Waits until the row is gone (the table refreshes after a delete)
	public boolean isJobReminderRemoved(String reminderName) {
		return Non_WebDriver_Util.waitForInvisibility(driver, rowByName(reminderName), 15);
	}

	// Reads the Job Reminder columns: Reminder Name | Remind At | Remind To | Status
	public Map<String, String> getJobReminderRowDetails(String reminderName) {
		WebElement row = driver.findElement(rowByName(reminderName));
		Map<String, String> details = new LinkedHashMap<>();
		details.put("name", cellText(row, "./hlm-td[1]"));
		details.put("remindAt", cellText(row, "./hlm-td[2]"));
		details.put("remindTo", cellText(row, "./hlm-td[3]"));
		details.put("status", row.findElement(By.xpath("./hlm-td[4]//badge")).getAttribute("title").trim());
		logAction("Read Job Reminder row: " + details);
		return details;
	}

	public List<String> getRowMenuOptions(String reminderName) {
		openRowMenu(reminderName);
		List<String> options = new ArrayList<>();
		for (WebElement item : driver.findElements(rowMenuItems)) {
			options.add(item.getText().trim());
		}
		new org.openqa.selenium.interactions.Actions(driver).sendKeys(Keys.ESCAPE).perform();
		Non_WebDriver_Util.waitForInvisibility(driver, rowMenuItems, 5);
		logAction("Row menu options for '" + reminderName + "': " + options);
		return options;
	}

	// ==========================================
	// Create / Edit drawer
	// ==========================================
	public void clickNewJobReminder() {
		clickButton(newJobReminderButton, "New Job Reminder");
		waitForDrawer("New Job Reminder");
	}

	public String getDrawerTitle() {
		WebElement title = Non_WebDriver_Util.findIfVisible(driver, drawerTitle, 10);
		return title == null ? "" : title.getText().trim();
	}

	public boolean isDrawerOpen() {
		return Non_WebDriver_Util.findIfVisible(driver, reminderNameInput, 2) != null;
	}

	public boolean isDrawerClosed() {
		return Non_WebDriver_Util.waitForInvisibility(driver, reminderNameInput, 15);
	}

	// Remind Type 'Relative to Start Time' adds the 'Remind Before (Minutes)' field
	public void enterJobReminderDetails(String reminderName, String remindType, String remindBeforeMinutes, String alertTemplate) {
		typeInto(reminderNameInput, "Reminder Name", reminderName);
		selectOption("remind_type", "Remind Type", remindType);
		if (Non_WebDriver_Util.findIfVisible(driver, remindBeforeInput, 5) != null) {
			typeInto(remindBeforeInput, "Remind Before (Minutes)", remindBeforeMinutes);
		}
		typeInto(alertTemplateInput, "Alert Template", alertTemplate);
	}

	public void updateJobReminderDetails(String reminderName, String remindBeforeMinutes, String alertTemplate) {
		typeInto(reminderNameInput, "Reminder Name", reminderName);
		typeInto(remindBeforeInput, "Remind Before (Minutes)", remindBeforeMinutes);
		typeInto(alertTemplateInput, "Alert Template", alertTemplate);
	}

	public Map<String, String> getDrawerValues() {
		Map<String, String> values = new LinkedHashMap<>();
		values.put("name", getFieldValue(reminderNameInput));
		values.put("notificationType", selectText("notification_type"));
		values.put("sendReminderTo", selectText("send_remincer"));
		values.put("remindType", selectText("remind_type"));
		values.put("remindBefore", getFieldValue(remindBeforeInput));
		values.put("alertTemplate", getFieldValue(alertTemplateInput));
		logAction("Read Job Reminder drawer values: " + values);
		return values;
	}

	public void clickSaveReminder() {
		clickButton(By.xpath(String.format(DRAWER_BUTTON, "Save Reminder")), "Save Reminder");
	}

	public void clickUpdateReminder() {
		clickButton(By.xpath(String.format(DRAWER_BUTTON, "Update Reminder")), "Update Reminder");
	}

	// Cancel closes the drawer without saving; confirms a discard prompt if the application shows one
	public void cancelDrawer() {
		clickButton(By.xpath(String.format(DRAWER_BUTTON, "Cancel")), "Cancel");
		WebElement confirmDialog = Non_WebDriver_Util.findIfVisible(driver, dialog, 3);
		if (confirmDialog != null) {
			String text = Non_WebDriver_Util.getInnerText(driver, confirmDialog).replaceAll("\\s+", " ").trim();
			logger.warn("[ACTION] Confirmation shown after Cancel: \"" + text + "\" - confirming to discard changes");
			List<WebElement> buttons = confirmDialog.findElements(By.tagName("button"));
			Non_WebDriver_Util.jsClick(driver, buttons.get(buttons.size() - 1));
			Non_WebDriver_Util.waitForInvisibility(driver, dialog, 10);
		}
		if (!isDrawerClosed()) {
			throw new IllegalStateException("❌ Job Reminder drawer did not close after Cancel (locator: " + reminderNameInput + ")");
		}
		logAction("Job Reminder drawer closed without saving");
	}

	// Validation message shown for the field whose label starts with 'fieldLabel' ("" when none)
	public String getValidationMessage(String fieldLabel) {
		String message = getValidationMessages().getOrDefault(fieldLabel, "");
		logAction("Validation message under '" + fieldLabel + "': '" + message + "'");
		return message;
	}

	public int getValidationMessageCount() {
		List<WebElement> errors = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 5,
				ExpectedConditions.presenceOfAllElementsLocatedBy(validationMessages));
		int count = errors == null ? 0 : errors.size();
		logAction("Validation messages displayed in the drawer: " + count);
		return count;
	}

	// Map of field label -> validation message currently shown in the drawer
	public Map<String, String> getValidationMessages() {
		Map<String, String> messages = new LinkedHashMap<>();
		List<WebElement> errors = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 5,
				ExpectedConditions.presenceOfAllElementsLocatedBy(validationMessages));
		if (errors != null) {
			for (WebElement error : errors) {
				// nearest ancestor holding a label = the form field the message belongs to
				WebElement label = error.findElement(By.xpath("./ancestor::*[.//label][1]//label[1]"));
				messages.putIfAbsent(Non_WebDriver_Util.getInnerText(driver, label).split("\\*")[0].trim(),
						Non_WebDriver_Util.getInnerText(driver, error).trim());
			}
		}
		return messages;
	}

	public void clickEditJobReminder(String reminderName) {
		clickRowMenuOption(reminderName, "Edit Job Reminder");
		waitForDrawer("Edit Job Reminder");
		// drawer is pre-filled asynchronously
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> !getFieldValue(reminderNameInput).isEmpty());
	}

	// ==========================================
	// Delete actions
	// ==========================================
	// Opens the delete confirmation; returns its message
	public String clickDeleteJobReminder(String reminderName) {
		clickRowMenuOption(reminderName, "Delete Job Reminder");
		return getDialogText();
	}

	public void confirmDelete() {
		if (!clickDialogButton("Delete")) {
			throw new IllegalStateException("❌ Delete confirmation dialog did not close after clicking 'Delete'");
		}
	}

	// Returns false when the dialog stays open after Cancel
	public boolean cancelDelete() {
		return clickDialogButton("Cancel");
	}

	// ==========================================
	// Dialog & toast actions
	// ==========================================
	public String getDialogText() {
		WebElement dialogElement = Non_WebDriver_Util.findIfVisible(driver, dialog, 15);
		if (dialogElement == null) {
			throw new IllegalStateException("❌ Confirmation dialog did not appear (locator: " + dialog + ")");
		}
		String text = Non_WebDriver_Util.getInnerText(driver, dialogElement).replaceAll("\\s+", " ").trim();
		logAction("Dialog displayed: \"" + text + "\"");
		return text;
	}

	// Clicks a confirmation dialog button and waits for the dialog to close; returns false when it stays open
	public boolean clickDialogButton(String buttonText) {
		By button = By.xpath(String.format(DIALOG_BUTTON, buttonText));
		logAction("Clicking dialog button '" + buttonText + "'");
		WebElement buttonElement = Non_WebDriver_Util.findIfVisible(driver, button, 15);
		if (buttonElement == null) {
			throw new IllegalStateException("❌ Dialog button not found: '" + buttonText + "' (locator: " + button + ")");
		}
		Non_WebDriver_Util.waitForBeClickable(driver, buttonElement, 10);
		buttonElement.click();
		boolean closed = Non_WebDriver_Util.waitForInvisibility(driver, dialog, 10);
		if (closed) {
			logAction("Dialog closed after clicking '" + buttonText + "'");
		} else {
			logger.warn("[ACTION] Dialog is still open 10s after clicking '" + buttonText + "' (locator: " + button + ")");
		}
		return closed;
	}

	public boolean isToastDisplayed(String expectedText) {
		boolean displayed = Non_WebDriver_Util.findIfVisible(driver, By.xpath(String.format(TOAST_MESSAGE, expectedText)), 20) != null;
		if (displayed) {
			logAction("Toast displayed: '" + expectedText + "'");
		} else {
			logger.warn("[ACTION] Expected toast not displayed within 20s: '" + expectedText + "'");
		}
		return displayed;
	}

	// ==========================================
	// Helpers
	// ==========================================
	private By rowByName(String reminderName) {
		return By.xpath(String.format(ROW_BY_NAME, reminderName));
	}

	private int getVisibleReminderCount() {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(reminderRows).size());
		return count == null ? 0 : count;
	}

	private void openRowMenu(String reminderName) {
		logAction("Opening row menu (3-dot) for Job Reminder '" + reminderName + "'");
		WebElement trigger = driver.findElement(rowByName(reminderName)).findElement(By.xpath(ROW_MENU_TRIGGER));
		// success toasts can briefly overlap the row - retry on intercepted click
		Non_WebDriver_Util.clickWithRetry(driver, trigger, 5, 1000);
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, ExpectedConditions.visibilityOfElementLocated(rowMenuItems));
	}

	private void clickRowMenuOption(String reminderName, String option) {
		openRowMenu(reminderName);
		logAction("Clicking row menu option '" + option + "'");
		WebElement menuItem = driver.findElement(By.xpath(String.format(ROW_MENU_ITEM, option)));
		Non_WebDriver_Util.clickWithRetry(driver, menuItem, 5, 1000);
	}

	private void waitForDrawer(String expectedTitle) {
		String title = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> {
			List<WebElement> titles = d.findElements(drawerTitle);
			return !titles.isEmpty() && titles.get(0).getText().trim().equals(expectedTitle) ? expectedTitle : null;
		});
		if (title == null) {
			throw new IllegalStateException("❌ '" + expectedTitle + "' drawer did not open (locator: " + drawerTitle + ")");
		}
		if (Non_WebDriver_Util.findIfVisible(driver, reminderNameInput, 10) == null) {
			throw new IllegalStateException("❌ '" + expectedTitle + "' drawer form did not load (locator: " + reminderNameInput + ")");
		}
		logAction("'" + expectedTitle + "' drawer opened");
	}

	private void selectOption(String selectId, String dropdownName, String optionText) {
		logAction("Selecting '" + optionText + "' from '" + dropdownName + "' dropdown");
		clickButton(By.xpath(String.format(SELECT_TRIGGER, selectId)), dropdownName + " dropdown");
		By option = By.xpath(String.format(DROPDOWN_OPTION, optionText));
		WebElement optionElement = Non_WebDriver_Util.findIfVisible(driver, option, 10);
		if (optionElement == null) {
			throw new IllegalArgumentException("❌ Dropdown option not found: '" + optionText + "' in '" + dropdownName + "' (locator: " + option + ")");
		}
		optionElement.click();
		Non_WebDriver_Util.waitForInvisibility(driver, option, 5);
	}

	private String selectText(String selectId) {
		List<WebElement> trigger = driver.findElements(By.xpath(String.format(SELECT_TRIGGER, selectId)));
		return trigger.isEmpty() ? "" : trigger.get(0).getText().trim();
	}

	// Some buttons on this page are reported as covered by the page container although visible - fall back to JS click
	private void clickButton(By locator, String buttonName) {
		logAction("Clicking '" + buttonName + "'");
		WebElement button = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, ExpectedConditions.presenceOfElementLocated(locator));
		if (button == null) {
			throw new IllegalStateException("❌ '" + buttonName + "' not found (locator: " + locator + ")");
		}
		boolean inViewport = (Boolean) ((JavascriptExecutor) driver).executeScript(
				"var r = arguments[0].getBoundingClientRect(); return r.top >= 0 && r.bottom <= window.innerHeight;", button);
		if (!inViewport) {
			logAction("'" + buttonName + "' is outside the visible window area - clicking with JavaScript");
			Non_WebDriver_Util.jsClick(driver, button);
			return;
		}
		try {
			Non_WebDriver_Util.waitForBeClickable(driver, button, 10);
			button.click();
		} catch (ElementNotInteractableException e) {   // includes ElementClickInterceptedException
			logAction("'" + buttonName + "' click was intercepted (" + e.getClass().getSimpleName() + ") - clicking with JavaScript");
			Non_WebDriver_Util.jsClick(driver, button);
		}
	}

	private void typeInto(By locator, String fieldName, String value) {
		logAction("Entering '" + fieldName + "' = '" + value + "'");
		WebElement field = Non_WebDriver_Util.findIfVisible(driver, locator, 20);
		if (field == null) {
			throw new IllegalStateException("❌ Field '" + fieldName + "' not displayed (locator: " + locator + ")");
		}
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", field);
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

	private String cellText(WebElement row, String cellXpath) {
		return Non_WebDriver_Util.getInnerText(driver, row.findElement(By.xpath(cellXpath))).trim().replaceAll("\\s+", " ");
	}
}
