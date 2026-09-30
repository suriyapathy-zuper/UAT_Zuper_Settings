package PageObject;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

/**
 * Settings > Jobs > Job Card Templates
 * - Job Card Templates list (live search, edit / delete row actions)
 * - 'New Job Card Template' dialog (template details) -> Proceed -> template designer (details + content editor) -> Save
 * - Edit opens the template designer directly
 * - Delete confirmation dialog and toast messages
 *
 * Every UI action is logged as "[ACTION] ..." through Baseclass.logAction().
 */
public class JobCardTemplatePage extends Baseclass {

	private WebDriver driver;
	private String templateListUrl;

	public JobCardTemplatePage() {
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	// ==========================================
	// Job Card Templates list elements
	// ==========================================
	@FindBy(xpath = "//h3[contains(normalize-space(),'Job Card Templates')]")
	private WebElement jobCardTemplatesHeader;

	@FindBy(xpath = "//button[normalize-space()='New Template']")
	private WebElement newTemplateButton;

	// List search box (the settings side menu has another 'Search' input before it)
	@FindBy(xpath = "//h3[contains(normalize-space(),'Job Card Templates')]/following::input[@placeholder='Search'][1]")
	private WebElement templateSearchInput;

	private final By templateRows = By.xpath("//div[contains(@class,'pt-5') and contains(@class,'border-b')][div[2]/span]");
	private final By noTemplatesMessage = By.xpath("//*[normalize-space(text())='No templates available']");
	// Row matched by its own 'Template Name' cell (2nd column) - never by a container that holds all rows
	private static final String ROW_BY_NAME = "//div[contains(@class,'pt-5') and contains(@class,'border-b')][div[2]/span[normalize-space()='%s']]";
	private static final String ROW_EDIT_BUTTON = ".//i[contains(@class,'ti-pencil')]/parent::button";
	private static final String ROW_DELETE_BUTTON = ".//i[contains(@class,'ti-trash')]/parent::button";

	// ==========================================
	// Template form (same fields in the 'New Job Card Template' dialog and in the designer)
	// ==========================================
	private static final String CREATE_DIALOG = "//mat-dialog-container";
	private static final String DESIGNER = "//zuper-drawer[contains(@class,'template-container')]";
	private static final String TEMPLATE_NAME = "%s//input[@id='template_name']";
	private static final String TEMPLATE_DESCRIPTION = "%s//textarea[@formcontrolname='templateDescription']";
	private static final String SELECT_TRIGGER = "%s//brn-select[@formcontrolname='%s']//button[@role='combobox']";
	private static final String BORDER_INPUT = "%s//input[@id='%s']";
	private static final String DROPDOWN_OPTION = "//hlm-option[normalize-space()='%s']";

	private final By createDialogTitle = By.xpath(CREATE_DIALOG + "//h6");
	private final By validationMessages = By.xpath(CREATE_DIALOG + "//p[contains(@class,'text-red-500') and normalize-space()]");
	private static final String CREATE_DIALOG_BUTTON = CREATE_DIALOG + "//button[normalize-space()='%s']";

	// Template designer (opens after Proceed and on Edit)
	private final By designerNameInput = By.xpath(String.format(TEMPLATE_NAME, DESIGNER));
	private final By designerHeader = By.xpath(DESIGNER + "//div[contains(@class,'zuper-dialog-header')]");
	private static final String DESIGNER_BUTTON = DESIGNER + "//div[contains(@class,'zuper-dialog-header')]//button[normalize-space()='%s']";
	private final By contentEditorFrame = By.cssSelector("iframe.tox-edit-area__iframe");

	// ==========================================
	// Dialogs & toasts
	// ==========================================
	private final By dialog = By.xpath("//mat-dialog-container");
	private static final String DIALOG_BUTTON = "//mat-dialog-container//button[normalize-space()='%s']";
	private static final String TOAST_MESSAGE = "//div[contains(@class,'hot-toast-message')][contains(normalize-space(),'%s')]";

	// ==========================================
	// Job Card Templates list actions
	// ==========================================
	public void waitForJobCardTemplatePageToLoad() {
		logAction("Waiting for Job Card Templates page to load");
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.withMessage("Job Card Templates URL (/settings_new/job/job-card-templates) - current URL: " + driver.getCurrentUrl())
				.until(ExpectedConditions.urlContains("/settings_new/job/job-card-templates"));
		Non_WebDriver_Util.waitForVisible(driver, jobCardTemplatesHeader, 30);
		Non_WebDriver_Util.waitForVisible(driver, newTemplateButton, 30);
		// list rows (or the empty state) are rendered after the API call
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20,
				d -> !d.findElements(templateRows).isEmpty() || !d.findElements(noTemplatesMessage).isEmpty());
		templateListUrl = driver.getCurrentUrl().split("\\?")[0];
		logAction("Job Card Templates page loaded: " + templateListUrl + " (" + getVisibleTemplateCount() + " templates listed)");
	}

	public boolean isJobCardTemplatePageDisplayed() {
		return driver.getCurrentUrl().contains("/settings_new/job/job-card-templates") && jobCardTemplatesHeader.isDisplayed();
	}

	public boolean isNewTemplateButtonAvailable() {
		return newTemplateButton.isDisplayed() && newTemplateButton.isEnabled();
	}

	// Re-open the list through the URL captured when the page was first opened from the UI
	public void openJobCardTemplateList() {
		logAction("Opening Job Card Templates list: " + templateListUrl);
		driver.get(templateListUrl);
		waitForJobCardTemplatePageToLoad();
	}

	public void refreshJobCardTemplatePage() {
		logAction("Reloading Job Card Templates page");
		Non_WebDriver_Util.refreshPage(driver);
		waitForJobCardTemplatePageToLoad();
	}

	// Search filters the list while typing - wait until only matching rows (or the empty state) are shown.
	// After save/delete the list reloads and can drop the filter, so the text is re-typed when needed.
	public void searchTemplate(String templateName) {
		for (int attempt = 1; attempt <= 3; attempt++) {
			logAction("Searching Job Card Templates for '" + templateName + "'" + (attempt > 1 ? " (attempt " + attempt + ")" : ""));
			Non_WebDriver_Util.waitForBeClickable(driver, templateSearchInput, 20);
			templateSearchInput.click();
			templateSearchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
			templateSearchInput.sendKeys(templateName);
			if (waitForSearchResult(templateName, 7)) {
				int count = getVisibleTemplateCount();
				logAction("Search result for '" + templateName + "': " + (count == 0 ? "No templates available" : count + " row(s)"));
				return;
			}
			logger.warn("[ACTION] List not filtered yet - search box value is '" + templateSearchInput.getAttribute("value")
					+ "', " + getVisibleTemplateCount() + " row(s) shown");
		}
		throw new IllegalStateException("❌ Job Card Templates list was not filtered for search text: " + templateName
				+ " (locator: //h3[contains(normalize-space(),'Job Card Templates')]/following::input[@placeholder='Search'][1])");
	}

	public int getMatchingTemplateCount(String templateName) {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(rowByName(templateName)).size());
		return count == null ? 0 : count;
	}

	public boolean isTemplateDisplayed(String templateName) {
		return Non_WebDriver_Util.findIfVisible(driver, rowByName(templateName), 5) != null;
	}

	public boolean isNoTemplatesAvailableDisplayed() {
		return Non_WebDriver_Util.findIfVisible(driver, noTemplatesMessage, 10) != null;
	}

	public boolean isTemplateDeleted(String templateName) {
		return isNoTemplatesAvailableDisplayed() && getMatchingTemplateCount(templateName) == 0;
	}

	// Reads the list columns: # | Template Name | Template Description | Created On
	public Map<String, String> getTemplateRowDetails(String templateName) {
		WebElement row = driver.findElement(rowByName(templateName));
		Map<String, String> details = new LinkedHashMap<>();
		details.put("name", cellText(row, "./div[2]"));
		details.put("description", cellText(row, "./div[3]"));
		details.put("createdOn", cellText(row, "./div[4]"));
		logAction("Read Job Card Template row: " + details);
		return details;
	}

	// ==========================================
	// Create: 'New Job Card Template' dialog
	// ==========================================
	public void clickNewTemplate() {
		closeLeftoverDialog();
		logAction("Clicking 'New Template' button");
		Non_WebDriver_Util.waitForBeClickable(driver, newTemplateButton, 20);
		newTemplateButton.click();
		String title = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> {
			List<WebElement> titles = d.findElements(createDialogTitle);
			return !titles.isEmpty() && titles.get(0).getText().trim().equals("New Job Card Template") ? "opened" : null;
		});
		if (title == null) {
			throw new IllegalStateException("❌ 'New Job Card Template' dialog did not open (locator: " + createDialogTitle + ")");
		}
		waitForDialogOpenAnimation();
		logAction("'New Job Card Template' dialog opened");
	}

	// Clicks made while the dialog is still animating in are ignored - wait until it is fully open
	private void waitForDialogOpenAnimation() {
		Boolean open = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, d -> {
			List<WebElement> containers = d.findElements(dialog);
			if (containers.isEmpty()) {
				return false;
			}
			String css = containers.get(0).getAttribute("class");
			return css.contains("mdc-dialog--open") && !css.contains("mdc-dialog--opening");
		});
		if (open == null) {
			logger.warn("[ACTION] Dialog open animation did not finish within 10s - continuing");
		}
	}

	// A dialog left open by a previous failed step would block the list - close it with its Cancel button first
	private void closeLeftoverDialog() {
		if (Non_WebDriver_Util.findIfVisible(driver, dialog, 1) != null) {
			logger.warn("[ACTION] A dialog is still open from a previous step - closing it with its 'Cancel' button");
			clickFooterButton(By.xpath(String.format(DIALOG_BUTTON, "Cancel")), "Cancel");
			if (!Non_WebDriver_Util.waitForInvisibility(driver, dialog, 10)) {
				throw new IllegalStateException("❌ Leftover dialog could not be closed (locator: " + dialog + ")");
			}
		}
	}

	public String getCreateDialogTitle() {
		WebElement title = Non_WebDriver_Util.findIfVisible(driver, createDialogTitle, 10);
		return title == null ? "" : title.getText().trim();
	}

	public boolean isCreateDialogOpen() {
		return Non_WebDriver_Util.findIfVisible(driver, By.xpath(String.format(TEMPLATE_NAME, CREATE_DIALOG)), 2) != null;
	}

	public void enterTemplateDetails(String templateName, String jobCategory, String description, String format,
			String orientation, String border) {
		typeInto(By.xpath(String.format(TEMPLATE_NAME, CREATE_DIALOG)), "Template Name", templateName);
		selectOption(CREATE_DIALOG, "associated", "Job Category", jobCategory);
		// Job Category is a multi-select: close the option list before continuing
		new Actions(driver).sendKeys(Keys.ESCAPE).perform();
		Non_WebDriver_Util.waitForInvisibility(driver, By.xpath(String.format(DROPDOWN_OPTION, jobCategory)), 5);
		typeInto(By.xpath(String.format(TEMPLATE_DESCRIPTION, CREATE_DIALOG)), "Template Description", description);
		selectOption(CREATE_DIALOG, "format", "Format", format);
		selectOption(CREATE_DIALOG, "orientation", "Orientation", orientation);
		for (String side : new String[] { "top", "right", "bottom", "left" }) {
			typeInto(By.xpath(String.format(BORDER_INPUT, CREATE_DIALOG, "border_" + side)), "Border " + capitalize(side), border);
		}
	}

	// Proceed validates the dialog and opens the template designer
	public void clickProceed() {
		clickFooterButton(By.xpath(String.format(CREATE_DIALOG_BUTTON, "Proceed")), "Proceed");
	}

	public void cancelCreateDialog() {
		clickFooterButton(By.xpath(String.format(CREATE_DIALOG_BUTTON, "Cancel")), "Cancel");
		if (!Non_WebDriver_Util.waitForInvisibility(driver, dialog, 10)) {
			throw new IllegalStateException("❌ 'New Job Card Template' dialog did not close after Cancel (locator: " + dialog + ")");
		}
		logAction("'New Job Card Template' dialog closed");
	}

	// Validation message shown for the field whose label starts with 'fieldLabel' ("" when none)
	public String getValidationMessage(String fieldLabel) {
		String message = getValidationMessages().getOrDefault(fieldLabel, "");
		logAction("Validation message under '" + fieldLabel + "': '" + message + "'");
		return message;
	}

	// Messages below the visible part of the dialog are clipped (not 'displayed' for Selenium) on small screens,
	// so they are located by presence and read with innerText
	public int getValidationMessageCount() {
		List<WebElement> errors = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 5,
				ExpectedConditions.presenceOfAllElementsLocatedBy(validationMessages));
		int count = errors == null ? 0 : errors.size();
		logAction("Validation messages displayed in the dialog: " + count);
		return count;
	}

	// Map of field label -> validation message currently shown in the 'New Job Card Template' dialog
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

	// ==========================================
	// Template designer (after Proceed / on Edit)
	// ==========================================
	public boolean isTemplateDesignerOpen() {
		return Non_WebDriver_Util.findIfVisible(driver, designerNameInput, 20) != null;
	}

	public void waitForTemplateDesigner() {
		logAction("Waiting for the template designer to open");
		if (Non_WebDriver_Util.findIfVisible(driver, designerNameInput, 20) == null) {
			throw new IllegalStateException("❌ Template designer did not open (locator: " + designerNameInput + ")");
		}
		if (Non_WebDriver_Util.findIfVisible(driver, contentEditorFrame, 20) == null) {
			throw new IllegalStateException("❌ Template content editor did not load (locator: " + contentEditorFrame + ")");
		}
		// form is pre-filled asynchronously on Edit
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, d -> !getFieldValue(designerNameInput).isEmpty());
		logAction("Template designer opened: '" + getDesignerHeaderText() + "'");
	}

	public String getDesignerHeaderText() {
		List<WebElement> header = driver.findElements(designerHeader);
		return header.isEmpty() ? "" : header.get(0).getText().split("\\R")[0].trim();
	}

	// Types the template body into the TinyMCE content editor (iframe)
	public void enterTemplateContent(String content) {
		logAction("Entering template content in the content editor: '" + content + "'");
		WebElement frame = driver.findElement(contentEditorFrame);
		driver.switchTo().frame(frame);
		try {
			WebElement body = driver.findElement(By.tagName("body"));
			body.click();
			body.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
			body.sendKeys(content);
		} finally {
			driver.switchTo().defaultContent();
		}
	}

	public String getTemplateContentText() {
		WebElement frame = driver.findElement(contentEditorFrame);
		driver.switchTo().frame(frame);
		try {
			return driver.findElement(By.tagName("body")).getText().trim();
		} finally {
			driver.switchTo().defaultContent();
		}
	}

	// Values shown in the designer form
	public Map<String, String> getTemplateFormValues() {
		Map<String, String> values = new LinkedHashMap<>();
		values.put("name", getFieldValue(designerNameInput));
		values.put("description", getFieldValue(By.xpath(String.format(TEMPLATE_DESCRIPTION, DESIGNER))));
		values.put("jobCategory", driver.findElement(By.xpath(String.format(SELECT_TRIGGER, DESIGNER, "associated"))).getText().trim());
		values.put("format", driver.findElement(By.xpath(String.format(SELECT_TRIGGER, DESIGNER, "format"))).getText().trim());
		values.put("orientation", driver.findElement(By.xpath(String.format(SELECT_TRIGGER, DESIGNER, "orientation"))).getText().trim());
		values.put("borderTop", getFieldValue(By.xpath(String.format(BORDER_INPUT, DESIGNER, "border_top"))));
		values.put("content", getTemplateContentText());
		logAction("Read template designer values: " + values);
		return values;
	}

	public void updateTemplateDetails(String templateName, String description, String orientation) {
		typeInto(designerNameInput, "Template Name", templateName);
		typeInto(By.xpath(String.format(TEMPLATE_DESCRIPTION, DESIGNER)), "Template Description", description);
		selectOption(DESIGNER, "orientation", "Orientation", orientation);
	}

	// Save in the designer; saving takes a few seconds before the designer closes
	public void clickSaveTemplate() {
		clickFooterButton(By.xpath(String.format(DESIGNER_BUTTON, "Save")), "Save");
	}

	public boolean waitForTemplateDesignerToClose() {
		boolean closed = Non_WebDriver_Util.waitForInvisibility(driver, designerNameInput, 30);
		logAction(closed ? "Template designer closed" : "Template designer is still open 30s after Save");
		return closed;
	}

	// Cancel in the designer closes it without saving
	public void cancelTemplateDesigner() {
		clickFooterButton(By.xpath(String.format(DESIGNER_BUTTON, "Cancel")), "Cancel");
		if (!Non_WebDriver_Util.waitForInvisibility(driver, designerNameInput, 15)) {
			throw new IllegalStateException("❌ Template designer did not close after Cancel (locator: " + designerNameInput + ")");
		}
		logAction("Template designer closed without saving");
	}

	public void clickEditTemplate(String templateName) {
		logAction("Clicking Edit (pencil) button for Job Card Template '" + templateName + "'");
		WebElement editButton = driver.findElement(rowByName(templateName)).findElement(By.xpath(ROW_EDIT_BUTTON));
		// success toasts can briefly overlap the row - retry on intercepted click
		Non_WebDriver_Util.clickWithRetry(driver, editButton, 5, 1000);
		waitForTemplateDesigner();
	}

	// ==========================================
	// Delete actions
	// ==========================================
	// Opens the delete confirmation; returns its message
	public String clickDeleteTemplate(String templateName) {
		logAction("Clicking Delete (trash) button for Job Card Template '" + templateName + "'");
		WebElement deleteButton = driver.findElement(rowByName(templateName)).findElement(By.xpath(ROW_DELETE_BUTTON));
		Non_WebDriver_Util.clickWithRetry(driver, deleteButton, 5, 1000);
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

	public boolean isToastDisplayed(String expectedText, int timeoutSeconds) {
		boolean displayed = Non_WebDriver_Util.findIfVisible(driver, By.xpath(String.format(TOAST_MESSAGE, expectedText)), timeoutSeconds) != null;
		if (displayed) {
			logAction("Toast displayed: '" + expectedText + "'");
		} else {
			logger.warn("[ACTION] Expected toast not displayed within " + timeoutSeconds + "s: '" + expectedText + "'");
		}
		return displayed;
	}

	public boolean isToastDisplayed(String expectedText) {
		return isToastDisplayed(expectedText, 20);
	}

	// ==========================================
	// Helpers
	// ==========================================
	private By rowByName(String templateName) {
		return By.xpath(String.format(ROW_BY_NAME, templateName));
	}

	private boolean waitForSearchResult(String templateName, int timeoutSeconds) {
		Boolean filtered = Non_WebDriver_Util.waitWithoutImplicitWait(driver, timeoutSeconds, d -> {
			if (!d.findElements(noTemplatesMessage).isEmpty()) {
				return true;
			}
			List<WebElement> rows = d.findElements(templateRows);
			if (rows.isEmpty()) {
				return false;
			}
			try {
				for (WebElement row : rows) {
					if (!row.getText().toLowerCase().contains(templateName.toLowerCase())) {
						return false;
					}
				}
			} catch (StaleElementReferenceException e) {
				return false; // list re-rendering
			}
			return true;
		});
		return filtered != null;
	}

	private int getVisibleTemplateCount() {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(templateRows).size());
		return count == null ? 0 : count;
	}

	private void selectOption(String scope, String controlName, String dropdownName, String optionText) {
		logAction("Selecting '" + optionText + "' from '" + dropdownName + "' dropdown");
		WebElement trigger = driver.findElement(By.xpath(String.format(SELECT_TRIGGER, scope, controlName)));
		Non_WebDriver_Util.scrollIntoViewAndClick(driver, trigger);
		By option = By.xpath(String.format(DROPDOWN_OPTION, optionText));
		WebElement optionElement = Non_WebDriver_Util.findIfVisible(driver, option, 10);
		if (optionElement == null) {
			throw new IllegalArgumentException("❌ Dropdown option not found: '" + optionText + "' in '" + dropdownName + "' (locator: " + option + ")");
		}
		optionElement.click();
	}

	// Footer/header buttons can be outside the visible window on small screens - fall back to JS click
	private void clickFooterButton(By button, String buttonText) {
		logAction("Clicking '" + buttonText + "' button");
		// located by presence: a button clipped below the visible dialog area is not 'displayed' for Selenium
		WebElement buttonElement = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, ExpectedConditions.presenceOfElementLocated(button));
		if (buttonElement == null) {
			throw new IllegalStateException("❌ Button '" + buttonText + "' not found (locator: " + button + ")");
		}
		boolean inViewport = (Boolean) ((JavascriptExecutor) driver).executeScript(
				"var r = arguments[0].getBoundingClientRect(); return r.top >= 0 && r.bottom <= window.innerHeight;", buttonElement);
		if (!inViewport) {
			logAction("'" + buttonText + "' button is outside the visible window area - clicking with JavaScript");
			Non_WebDriver_Util.jsClick(driver, buttonElement);
			return;
		}
		try {
			Non_WebDriver_Util.waitForBeClickable(driver, buttonElement, 10);
			buttonElement.click();
		} catch (ElementNotInteractableException e) {   // includes ElementClickInterceptedException
			logAction("'" + buttonText + "' button is not interactable (" + e.getClass().getSimpleName() + ") - clicking with JavaScript");
			Non_WebDriver_Util.jsClick(driver, buttonElement);
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
		return Non_WebDriver_Util.getInnerText(driver, row.findElement(By.xpath(cellXpath))).trim();
	}

	private static String capitalize(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}
}
