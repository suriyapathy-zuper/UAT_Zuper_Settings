package PageObject;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * Settings > Jobs > Inspection Forms
 * - Inspection Forms list (newest first, edit / delete row actions, total count next to the heading)
 * - 'Create New Inspection Form' dialog -> Proceed creates the form and opens the form builder
 * - Form builder: name heading, settings (gear) -> 'Update Inspection Form' dialog, back arrow to the list
 * - Delete confirmation dialog and toast messages
 *
 * Every UI action is logged as "[ACTION] ..." through Baseclass.logAction().
 */
public class InspectionFormPage extends Baseclass {

	private WebDriver driver;
	private String inspectionFormListUrl;

	public InspectionFormPage() {
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	// ==========================================
	// Inspection Forms list elements
	// ==========================================
	@FindBy(xpath = "//h3[normalize-space()='Inspection Forms']")
	private WebElement inspectionFormsHeader;

	@FindBy(xpath = "//button[normalize-space()='New Form']")
	private WebElement newFormButton;

	private final By formRows = By.xpath("//cdk-row");
	private final By inspectionFormsHeaderLocator = By.xpath("//h3[normalize-space()='Inspection Forms']");
	// Row matched by its own 'Form Name' cell (2nd column)
	private static final String ROW_BY_NAME = "//cdk-row[hlm-td[2]//div[normalize-space()='%s']]";
	private static final String ROW_EDIT_ICON = ".//span[@title='Edit Inspection Form']";
	private static final String ROW_DELETE_ICON = ".//span[@title='Delete Inspection Form']";

	// ==========================================
	// Create / Update dialog elements
	// ==========================================
	private final By dialog = By.xpath("//mat-dialog-container");
	private final By dialogTitle = By.xpath("//mat-dialog-container//h6");
	private final By formNameInput = By.xpath("//mat-dialog-container//input[@id='asset_form_name']");
	private final By formDescriptionInput = By.xpath("//mat-dialog-container//textarea[@formcontrolname='asset_form_description']");
	private final By nameValidationMessage = By.xpath("//mat-dialog-container//input[@id='asset_form_name']/ancestor::*[.//p[contains(@class,'text-red-500')]][1]//p[contains(@class,'text-red-500')]");
	private static final String DIALOG_BUTTON = "//mat-dialog-container//button[normalize-space()='%s']";
	private static final String TOAST_MESSAGE = "//div[contains(@class,'hot-toast-message')][contains(normalize-space(),'%s')]";

	// ==========================================
	// Form builder elements
	// ==========================================
	// form name heading next to the back arrow (its title attribute holds the full name)
	private final By builderFormName = By.xpath("//h2[@title][contains(@class,'font-semibold')]");
	private final By builderFieldsPanel = By.xpath("//h2[normalize-space()='Fields']");
	private final By builderSettingsButton = By.xpath("//i[contains(@class,'ti-settings')]/parent::button");
	private final By builderBackButton = By.xpath("//i[contains(@class,'ti-arrow-left')]/parent::button");

	// ==========================================
	// Inspection Forms list actions
	// ==========================================
	public void waitForInspectionFormPageToLoad() {
		logAction("Waiting for Inspection Forms page to load");
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.withMessage("Inspection Forms URL (/settings_new/job/inspection-form) - current URL: " + driver.getCurrentUrl())
				.until(ExpectedConditions.urlContains("/settings_new/job/inspection-form"));
		Non_WebDriver_Util.waitForVisible(driver, inspectionFormsHeader, 30);
		Non_WebDriver_Util.waitForVisible(driver, newFormButton, 30);
		// rows are rendered after the API call (the list may legitimately be empty)
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, d -> !d.findElements(formRows).isEmpty());
		inspectionFormListUrl = driver.getCurrentUrl().split("\\?")[0];
		logAction("Inspection Forms page loaded: " + inspectionFormListUrl + " (" + getVisibleFormCount() + " forms listed, total "
				+ getTotalFormCount() + ")");
	}

	public boolean isInspectionFormPageDisplayed() {
		return driver.getCurrentUrl().contains("/settings_new/job/inspection-form") && inspectionFormsHeader.isDisplayed();
	}

	public boolean isNewFormButtonAvailable() {
		return newFormButton.isDisplayed() && newFormButton.isEnabled();
	}

	// Re-open the list through the URL captured when the page was first opened from the UI
	public void openInspectionFormList() {
		logAction("Opening Inspection Forms list: " + inspectionFormListUrl);
		driver.get(inspectionFormListUrl);
		waitForInspectionFormPageToLoad();
	}

	public void refreshInspectionFormPage() {
		logAction("Reloading Inspection Forms page");
		Non_WebDriver_Util.refreshPage(driver);
		waitForInspectionFormPageToLoad();
	}

	// Total number of forms shown next to the 'Inspection Forms' heading (-1 when it cannot be read)
	public int getTotalFormCount() {
		List<WebElement> header = driver.findElements(inspectionFormsHeaderLocator);
		if (header.isEmpty()) {
			return -1;
		}
		String headerText = Non_WebDriver_Util.getInnerText(driver, header.get(0).findElement(By.xpath("./ancestor::div[1]")));
		Matcher matcher = Pattern.compile("Inspection Forms\\s*(\\d+)").matcher(headerText);
		return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
	}

	// The list is sorted by Created On (newest first), so the automation form is on the first page
	public boolean isInspectionFormDisplayed(String formName) {
		return Non_WebDriver_Util.findIfVisible(driver, rowByName(formName), 5) != null;
	}

	public int getMatchingFormCount(String formName) {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(rowByName(formName)).size());
		return count == null ? 0 : count;
	}

	// Waits until the row is gone (list refreshes after a delete)
	public boolean isInspectionFormRemoved(String formName) {
		return Non_WebDriver_Util.waitForInvisibility(driver, rowByName(formName), 15);
	}

	// Reads the list columns: # | Form Name | Description | Created By | Created On
	public Map<String, String> getInspectionFormRowDetails(String formName) {
		WebElement row = driver.findElement(rowByName(formName));
		Map<String, String> details = new LinkedHashMap<>();
		details.put("name", cellText(row, "./hlm-td[2]"));
		details.put("description", cellText(row, "./hlm-td[3]"));
		details.put("createdBy", cellText(row, "./hlm-td[4]").replaceAll("^\\S\\s+", ""));   // drop the avatar initial
		details.put("createdOn", cellText(row, "./hlm-td[5]"));
		logAction("Read Inspection Form row: " + details);
		return details;
	}

	// ==========================================
	// Create dialog
	// ==========================================
	public void clickNewForm() {
		logAction("Clicking 'New Form' button");
		Non_WebDriver_Util.waitForBeClickable(driver, newFormButton, 20);
		newFormButton.click();
		waitForDialogTitle("Create New Inspection Form");
	}

	public String getDialogTitle() {
		WebElement title = Non_WebDriver_Util.findIfVisible(driver, dialogTitle, 10);
		return title == null ? "" : title.getText().trim();
	}

	public boolean isFormDialogOpen() {
		return Non_WebDriver_Util.findIfVisible(driver, formNameInput, 2) != null;
	}

	public void enterInspectionFormDetails(String formName, String description) {
		typeInto(formNameInput, "Inspection Form Name", formName);
		typeInto(formDescriptionInput, "Description", description);
	}

	public void enterInspectionFormName(String formName) {
		typeInto(formNameInput, "Inspection Form Name", formName);
	}

	// Clears the name field (keyboard) so the form control becomes touched + empty
	public void clearInspectionFormName() {
		logAction("Clearing 'Inspection Form Name'");
		WebElement field = driver.findElement(formNameInput);
		field.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
		driver.findElement(formDescriptionInput).click();   // blur the name field
	}

	public boolean isDialogButtonEnabled(String buttonText) {
		WebElement button = driver.findElement(By.xpath(String.format(DIALOG_BUTTON, buttonText)));
		boolean enabled = button.isEnabled();
		logAction("'" + buttonText + "' button enabled: " + enabled);
		return enabled;
	}

	public String getNameValidationMessage() {
		WebElement message = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 5, ExpectedConditions.presenceOfElementLocated(nameValidationMessage));
		String text = message == null ? "" : Non_WebDriver_Util.getInnerText(driver, message).trim();
		logAction("Validation message under 'Inspection Form Name': '" + text + "'");
		return text;
	}

	// Proceed creates the form; on success the app navigates to the form builder
	public void clickProceed() {
		clickDialogFooterButton("Proceed");
	}

	public void cancelFormDialog() {
		clickDialogFooterButton("Cancel");
		if (!Non_WebDriver_Util.waitForInvisibility(driver, dialog, 10)) {
			throw new IllegalStateException("❌ Inspection Form dialog did not close after Cancel (locator: " + dialog + ")");
		}
		logAction("Inspection Form dialog closed");
	}

	public boolean isFormDialogClosed() {
		return Non_WebDriver_Util.waitForInvisibility(driver, dialog, 10);
	}

	// ==========================================
	// Form builder
	// ==========================================
	public void waitForFormBuilder() {
		logAction("Waiting for the Inspection Form builder to open");
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.withMessage("Form builder URL (/settings_new/inspection_form/<id>/edit) - current URL: " + driver.getCurrentUrl())
				.until(ExpectedConditions.urlMatches(".*/settings_new/inspection_form/[^/]+/edit$"));
		if (Non_WebDriver_Util.findIfVisible(driver, builderFieldsPanel, 20) == null) {
			throw new IllegalStateException("❌ Form builder 'Fields' panel did not load (locator: " + builderFieldsPanel + ")");
		}
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, d -> !getBuilderFormName().isEmpty());
		logAction("Form builder opened for '" + getBuilderFormName() + "': " + driver.getCurrentUrl());
	}

	public boolean isFormBuilderDisplayed() {
		return driver.getCurrentUrl().matches(".*/settings_new/inspection_form/[^/]+/edit$")
				&& Non_WebDriver_Util.findIfVisible(driver, builderFieldsPanel, 5) != null;
	}

	public String getBuilderFormName() {
		List<WebElement> name = driver.findElements(builderFormName);
		return name.isEmpty() ? "" : name.get(0).getText().trim();
	}

	// Waits until the builder heading shows the expected form name (after an update)
	public boolean waitForBuilderFormName(String expectedName) {
		return Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> getBuilderFormName().equals(expectedName)) != null;
	}

	// Gear icon next to the form name opens the 'Update Inspection Form' dialog
	public void openFormSettings() {
		logAction("Clicking settings (gear) icon next to the form name");
		WebElement gear = Non_WebDriver_Util.findIfVisible(driver, builderSettingsButton, 15);
		if (gear == null) {
			throw new IllegalStateException("❌ Form settings (gear) button not displayed (locator: " + builderSettingsButton + ")");
		}
		gear.click();
		waitForDialogTitle("Update Inspection Form");
		// form is pre-filled asynchronously
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> !getFieldValue(formNameInput).isEmpty());
	}

	public Map<String, String> getFormDialogValues() {
		Map<String, String> values = new LinkedHashMap<>();
		values.put("name", getFieldValue(formNameInput));
		values.put("description", getFieldValue(formDescriptionInput));
		logAction("Read Inspection Form dialog values: " + values);
		return values;
	}

	public void clickUpdate() {
		clickDialogFooterButton("Update");
	}

	// Back arrow returns from the builder to the Inspection Forms list
	public void backToInspectionFormList() {
		logAction("Clicking back arrow to return to the Inspection Forms list");
		WebElement back = Non_WebDriver_Util.findIfVisible(driver, builderBackButton, 15);
		if (back == null) {
			throw new IllegalStateException("❌ Form builder back button not displayed (locator: " + builderBackButton + ")");
		}
		back.click();
		waitForInspectionFormPageToLoad();
	}

	public void clickEditInspectionForm(String formName) {
		logAction("Clicking Edit (pencil) icon for Inspection Form '" + formName + "'");
		WebElement editIcon = driver.findElement(rowByName(formName)).findElement(By.xpath(ROW_EDIT_ICON));
		// success toasts can briefly overlap the row - retry on intercepted click
		Non_WebDriver_Util.clickWithRetry(driver, editIcon, 5, 1000);
		waitForFormBuilder();
	}

	// ==========================================
	// Delete actions
	// ==========================================
	// Opens the delete confirmation; returns its message
	public String clickDeleteInspectionForm(String formName) {
		logAction("Clicking Delete (trash) icon for Inspection Form '" + formName + "'");
		WebElement deleteIcon = driver.findElement(rowByName(formName)).findElement(By.xpath(ROW_DELETE_ICON));
		Non_WebDriver_Util.clickWithRetry(driver, deleteIcon, 5, 1000);
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
	private By rowByName(String formName) {
		return By.xpath(String.format(ROW_BY_NAME, formName));
	}

	private int getVisibleFormCount() {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(formRows).size());
		return count == null ? 0 : count;
	}

	private void waitForDialogTitle(String expectedTitle) {
		String title = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> {
			List<WebElement> titles = d.findElements(dialogTitle);
			return !titles.isEmpty() && titles.get(0).getText().trim().equals(expectedTitle) ? expectedTitle : null;
		});
		if (title == null) {
			throw new IllegalStateException("❌ '" + expectedTitle + "' dialog did not open (locator: " + dialogTitle + ")");
		}
		// clicks made while the dialog is still animating in are ignored - wait until it is fully open
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, d -> {
			List<WebElement> containers = d.findElements(dialog);
			String css = containers.isEmpty() ? "" : containers.get(0).getAttribute("class");
			return css.contains("mdc-dialog--open") && !css.contains("mdc-dialog--opening");
		});
		logAction("'" + expectedTitle + "' dialog opened");
	}

	// Dialog footer can be outside the visible window on small screens - fall back to JS click
	private void clickDialogFooterButton(String buttonText) {
		By button = By.xpath(String.format(DIALOG_BUTTON, buttonText));
		logAction("Clicking '" + buttonText + "' button in the Inspection Form dialog");
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
