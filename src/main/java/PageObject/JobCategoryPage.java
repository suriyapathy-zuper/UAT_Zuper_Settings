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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

/**
 * Settings > Jobs > Job Category Hub
 * - Job Categories list (live search, edit / delete row actions)
 * - Create / Edit Job Category dialog
 * - Delete confirmation dialog and toast messages
 *
 * Every UI action is logged as "[ACTION] ..." through Baseclass.logAction().
 */
public class JobCategoryPage extends Baseclass {

	private WebDriver driver;
	private String jobCategoryListUrl;

	public JobCategoryPage() {
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	// ==========================================
	// Job Categories list elements
	// ==========================================
	@FindBy(xpath = "//h3[normalize-space()='Job Categories']")
	private WebElement jobCategoriesHeader;

	@FindBy(xpath = "//button[normalize-space()='New Category']")
	private WebElement newCategoryButton;

	// List search box (the settings side menu has another 'Search' input before it)
	@FindBy(xpath = "//h3[normalize-space()='Job Categories']/following::input[@placeholder='Search'][1]")
	private WebElement categorySearchInput;

	private final By categoryRows = By.xpath("//div[contains(@class,'reorder-list-item')]");
	private final By noJobCategoriesFoundMessage = By.xpath("//*[normalize-space(text())='No Job Categories found']");
	private static final String ROW_BY_NAME = "//div[contains(@class,'reorder-list-item')][.//span[contains(@class,'text-color-copy') and normalize-space()='%s']]";
	private static final String ROW_EDIT_ICON = ".//i[contains(@class,'ti-pencil')]/parent::span";
	private static final String ROW_DELETE_ICON = ".//i[contains(@class,'ti-trash')]/parent::span";

	// ==========================================
	// Create / Edit Job Category dialog elements
	// ==========================================
	private final By dialog = By.xpath("//mat-dialog-container");
	private final By dialogTitle = By.xpath("//mat-dialog-container//h6");
	private final By categoryNameInput = By.id("category_name");
	private final By daysInput = By.id("days");
	private final By hoursInput = By.id("hours");
	private final By minutesInput = By.id("minutes");
	private final By validationMessages = By.xpath("//mat-dialog-container//p[contains(@class,'text-red-500') and normalize-space()]");
	private static final String DIALOG_BUTTON = "//mat-dialog-container//button[normalize-space()='%s']";
	private static final String TOAST_MESSAGE = "//div[contains(@class,'hot-toast-message')][contains(normalize-space(),'%s')]";

	// ==========================================
	// Job Categories list actions
	// ==========================================
	public void waitForJobCategoryPageToLoad() {
		logAction("Waiting for Job Category page to load");
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.withMessage("Job Category URL (/settings_new/job/category) - current URL: " + driver.getCurrentUrl())
				.until(ExpectedConditions.urlContains("/settings_new/job/category"));
		Non_WebDriver_Util.waitForVisible(driver, jobCategoriesHeader, 30);
		Non_WebDriver_Util.waitForVisible(driver, newCategoryButton, 30);
		// list rows (or the empty state) are rendered after the API call
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20,
				d -> !d.findElements(categoryRows).isEmpty() || !d.findElements(noJobCategoriesFoundMessage).isEmpty());
		jobCategoryListUrl = driver.getCurrentUrl().split("\\?")[0];
		logAction("Job Category page loaded: " + jobCategoryListUrl + " (" + getVisibleCategoryCount() + " categories listed)");
	}

	public boolean isJobCategoryPageDisplayed() {
		return driver.getCurrentUrl().contains("/settings_new/job/category") && jobCategoriesHeader.isDisplayed();
	}

	public boolean isNewCategoryButtonAvailable() {
		return newCategoryButton.isDisplayed() && newCategoryButton.isEnabled();
	}

	// Re-open the list through the URL captured when the page was first opened from the UI
	public void openJobCategoryList() {
		logAction("Opening Job Category list: " + jobCategoryListUrl);
		driver.get(jobCategoryListUrl);
		waitForJobCategoryPageToLoad();
	}

	public void refreshJobCategoryPage() {
		logAction("Reloading Job Category page");
		Non_WebDriver_Util.refreshPage(driver);
		waitForJobCategoryPageToLoad();
	}

	// Search filters the list while typing - wait until only matching rows (or the empty state) are shown.
	// Right after a create/update the list reloads and can reset the search box, so the text is re-typed when lost.
	public void searchJobCategory(String categoryName) {
		for (int attempt = 1; attempt <= 3; attempt++) {
			logAction("Searching Job Categories for '" + categoryName + "'" + (attempt > 1 ? " (attempt " + attempt + ")" : ""));
			Non_WebDriver_Util.waitForBeClickable(driver, categorySearchInput, 20);
			categorySearchInput.click();
			categorySearchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
			categorySearchInput.sendKeys(categoryName);
			if (waitForSearchResult(categoryName, 7)) {
				int count = getVisibleCategoryCount();
				logAction("Search result for '" + categoryName + "': " + (count == 0 ? "No Job Categories found" : count + " row(s)"));
				return;
			}
			logger.warn("[ACTION] List not filtered yet - search box value is '" + categorySearchInput.getAttribute("value")
					+ "', " + getVisibleCategoryCount() + " row(s) shown");
		}
		throw new IllegalStateException("❌ Job Category list was not filtered for search text: " + categoryName
				+ " (locator: //h3[normalize-space()='Job Categories']/following::input[@placeholder='Search'][1])");
	}

	private boolean waitForSearchResult(String categoryName, int timeoutSeconds) {
		Boolean filtered = Non_WebDriver_Util.waitWithoutImplicitWait(driver, timeoutSeconds, d -> {
			if (!d.findElements(noJobCategoriesFoundMessage).isEmpty()) {
				return true;
			}
			List<WebElement> rows = d.findElements(categoryRows);
			if (rows.isEmpty()) {
				return false;
			}
			try {
				for (WebElement row : rows) {
					if (!row.getText().toLowerCase().contains(categoryName.toLowerCase())) {
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

	public int getMatchingCategoryCount(String categoryName) {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(rowByName(categoryName)).size());
		return count == null ? 0 : count;
	}

	public boolean isJobCategoryDisplayed(String categoryName) {
		return Non_WebDriver_Util.findIfVisible(driver, rowByName(categoryName), 5) != null;
	}

	public boolean isNoJobCategoriesFoundDisplayed() {
		return Non_WebDriver_Util.findIfVisible(driver, noJobCategoriesFoundMessage, 10) != null;
	}

	public boolean isJobCategoryDeleted(String categoryName) {
		return isNoJobCategoriesFoundDisplayed() && getMatchingCategoryCount(categoryName) == 0;
	}

	// Reads the list columns: Category Name | Estimated Duration | Statuses | Created On
	public Map<String, String> getJobCategoryRowDetails(String categoryName) {
		WebElement row = driver.findElement(rowByName(categoryName));
		Map<String, String> details = new LinkedHashMap<>();
		details.put("name", cellText(row, ".//span[contains(@class,'text-color-copy')]"));
		details.put("estimatedDuration", cellText(row, "./div[3]"));
		details.put("createdOn", cellText(row, "./div[5]"));
		logAction("Read Job Category row: " + details);
		return details;
	}

	// ==========================================
	// Create / Edit dialog actions
	// ==========================================
	public void clickNewCategory() {
		logAction("Clicking 'New Category' button");
		Non_WebDriver_Util.waitForBeClickable(driver, newCategoryButton, 20);
		newCategoryButton.click();
		waitForDialogTitle("Create New Job Category");
	}

	public String getDialogTitle() {
		WebElement title = Non_WebDriver_Util.findIfVisible(driver, dialogTitle, 10);
		return title == null ? "" : title.getText().trim();
	}

	public boolean isCategoryDialogOpen() {
		return Non_WebDriver_Util.findIfVisible(driver, categoryNameInput, 2) != null;
	}

	public void enterJobCategoryDetails(String categoryName, String days, String hours, String minutes) {
		typeInto(categoryNameInput, "Category Name", categoryName);
		typeInto(daysInput, "Estimated Duration - Days", days);
		typeInto(hoursInput, "Estimated Duration - Hours", hours);
		typeInto(minutesInput, "Estimated Duration - Minutes", minutes);
	}

	public void enterCategoryName(String categoryName) {
		typeInto(categoryNameInput, "Category Name", categoryName);
	}

	public String getCategoryNameValue() {
		return getFieldValue(categoryNameInput);
	}

	public String getCategoryNameMaxLength() {
		return driver.findElement(categoryNameInput).getAttribute("maxlength");
	}

	public void clickCreate() {
		clickDialogFooterButton("Create");
	}

	public void clickUpdate() {
		clickDialogFooterButton("Update");
	}

	public void createJobCategory(String categoryName, String days, String hours, String minutes) {
		clickNewCategory();
		enterJobCategoryDetails(categoryName, days, hours, minutes);
		clickCreate();
		waitForDialogToClose("Create New Job Category");
	}

	// Cancel on the Create/Edit dialog closes it without saving
	public void cancelCategoryDialog() {
		String title = getDialogTitle();
		clickDialogFooterButton("Cancel");
		waitForDialogToClose(title);
	}

	// Validation message shown for the field whose label starts with 'fieldLabel' ("" when none)
	public String getValidationMessage(String fieldLabel) {
		String message = getValidationMessages().getOrDefault(fieldLabel, "");
		logAction("Validation message under '" + fieldLabel + "': '" + message + "'");
		return message;
	}

	// Map of field label -> validation message currently shown in the dialog
	public Map<String, String> getValidationMessages() {
		Map<String, String> messages = new LinkedHashMap<>();
		List<WebElement> errors = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 5,
				ExpectedConditions.visibilityOfAllElementsLocatedBy(validationMessages));
		if (errors != null) {
			for (WebElement error : errors) {
				// nearest ancestor holding a label = the form field the message belongs to
				String label = error.findElement(By.xpath("./ancestor::*[.//label][1]//label[1]")).getText();
				messages.put(label.split("\\*")[0].trim(), error.getText().trim());
			}
		}
		return messages;
	}

	public void clickEditJobCategory(String categoryName) {
		logAction("Clicking Edit (pencil) icon for Job Category '" + categoryName + "'");
		WebElement editIcon = driver.findElement(rowByName(categoryName)).findElement(By.xpath(ROW_EDIT_ICON));
		// success toasts can briefly overlap the row - retry on intercepted click
		Non_WebDriver_Util.clickWithRetry(driver, editIcon, 5, 1000);
		waitForDialogTitle("Edit Job Category");
		// form is pre-filled asynchronously
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, d -> !getFieldValue(categoryNameInput).isEmpty());
	}

	public Map<String, String> getCategoryFormValues() {
		Map<String, String> values = new LinkedHashMap<>();
		values.put("name", getFieldValue(categoryNameInput));
		values.put("days", getFieldValue(daysInput));
		values.put("hours", getFieldValue(hoursInput));
		values.put("minutes", getFieldValue(minutesInput));
		logAction("Read Job Category form values: " + values);
		return values;
	}

	public void updateJobCategory(String newCategoryName, String days, String hours, String minutes) {
		enterJobCategoryDetails(newCategoryName, days, hours, minutes);
		saveJobCategoryUpdate();
	}

	public void saveJobCategoryUpdate() {
		clickUpdate();
		waitForDialogToClose("Edit Job Category");
	}

	// Waits up to 10s for the Create/Edit dialog to close
	public boolean isCategoryDialogClosed() {
		return Non_WebDriver_Util.waitForInvisibility(driver, categoryNameInput, 10);
	}

	// ==========================================
	// Delete actions
	// ==========================================
	// Opens the delete confirmation; returns its message
	public String clickDeleteJobCategory(String categoryName) {
		logAction("Clicking Delete (trash) icon for Job Category '" + categoryName + "'");
		WebElement deleteIcon = driver.findElement(rowByName(categoryName)).findElement(By.xpath(ROW_DELETE_ICON));
		Non_WebDriver_Util.clickWithRetry(driver, deleteIcon, 5, 1000);
		return getDialogText();
	}

	public void confirmDelete() {
		clickDialogButton("Delete");
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

	public boolean isDialogDisplayed() {
		return Non_WebDriver_Util.findIfVisible(driver, dialog, 5) != null;
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
	private By rowByName(String categoryName) {
		return By.xpath(String.format(ROW_BY_NAME, categoryName));
	}

	private int getVisibleCategoryCount() {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(categoryRows).size());
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
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, ExpectedConditions.visibilityOfElementLocated(categoryNameInput));
		logAction("'" + expectedTitle + "' dialog opened");
	}

	private void waitForDialogToClose(String dialogName) {
		if (!Non_WebDriver_Util.waitForInvisibility(driver, dialog, 15)) {
			throw new IllegalStateException("❌ '" + dialogName + "' dialog did not close (locator: " + dialog + ")");
		}
		logAction("'" + dialogName + "' dialog closed");
	}

	// Dialog footer can be below the visible area on small screens - fall back to JS click when not interactable
	private void clickDialogFooterButton(String buttonText) {
		By button = By.xpath(String.format(DIALOG_BUTTON, buttonText));
		logAction("Clicking '" + buttonText + "' button in the Job Category dialog");
		WebElement buttonElement = driver.findElement(button);
		boolean inViewport = (Boolean) ((JavascriptExecutor) driver).executeScript(
				"var r = arguments[0].getBoundingClientRect(); return r.top >= 0 && r.bottom <= window.innerHeight;", buttonElement);
		if (!inViewport) {
			logAction("'" + buttonText + "' button is below the visible window area - clicking with JavaScript");
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
		return Non_WebDriver_Util.getInnerText(driver, row.findElement(By.xpath(cellXpath))).trim();
	}
}
