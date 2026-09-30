package PageObject;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
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
 * Settings > Jobs > Gallery Settings (Albums)
 * - 'Add New Album' inline form (album name + Add Album button, disabled while the name is empty)
 * - Albums list (live search, inline edit with Save / Cancel, delete row action)
 * - Delete confirmation dialog and toast messages
 *
 * Every UI action is logged as "[ACTION] ..." through Baseclass.logAction().
 */
public class GalleryPage extends Baseclass {

	private WebDriver driver;
	private String galleryUrl;

	public GalleryPage() {
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	// ==========================================
	// Gallery Settings page elements
	// ==========================================
	@FindBy(xpath = "//*[normalize-space(text())='Add New Album']")
	private WebElement addNewAlbumHeader;

	@FindBy(xpath = "//input[@placeholder='Enter album name']")
	private WebElement newAlbumNameInput;

	@FindBy(xpath = "//button[normalize-space()='Add Album']")
	private WebElement addAlbumButton;

	@FindBy(xpath = "//input[@placeholder='Search...']")
	private WebElement albumSearchInput;

	private final By albumRows = By.xpath("//div[contains(@class,'pt-5') and contains(@class,'border-b')][div[1]/span]");
	private final By noAlbumsFoundMessage = By.xpath("//*[normalize-space(text())='No albums found']");
	// Row matched by its own 'Album Name' cell (1st column)
	private static final String ROW_BY_NAME = "//div[contains(@class,'pt-5') and contains(@class,'border-b')][div[1]/span[normalize-space()='%s']]";
	private static final String ROW_EDIT_ICON = ".//i[contains(@class,'ti-pencil')]/parent::span";
	private static final String ROW_DELETE_ICON = ".//i[contains(@class,'ti-trash')]/parent::span";

	// Inline edit: the row being edited shows a text box with Save / Cancel
	private static final String EDIT_ROW = "//div[contains(@class,'pt-5') and contains(@class,'border-b')][.//input]";
	private final By inlineEditInput = By.xpath(EDIT_ROW + "//input");
	private static final String INLINE_EDIT_BUTTON = EDIT_ROW + "//button[normalize-space()='%s']";

	// ==========================================
	// Dialogs & toasts
	// ==========================================
	private final By dialog = By.xpath("//mat-dialog-container");
	private static final String DIALOG_BUTTON = "//mat-dialog-container//button[normalize-space()='%s']";
	private static final String TOAST_MESSAGE = "//div[contains(@class,'hot-toast-message')][contains(normalize-space(),'%s')]";

	// ==========================================
	// Page actions
	// ==========================================
	public void waitForGalleryPageToLoad() {
		logAction("Waiting for Gallery Settings page to load");
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.withMessage("Gallery Settings URL (/settings_new/job/gallery) - current URL: " + driver.getCurrentUrl())
				.until(ExpectedConditions.urlContains("/settings_new/job/gallery"));
		Non_WebDriver_Util.waitForVisible(driver, addNewAlbumHeader, 30);
		Non_WebDriver_Util.waitForVisible(driver, newAlbumNameInput, 30);
		// album rows are rendered after the API call; until then the list shows its 'No albums found' placeholder,
		// so wait for real rows before searching (default albums always exist in this account)
		if (Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, d -> !d.findElements(albumRows).isEmpty()) == null) {
			logger.warn("[ACTION] No album rows rendered within 20s - the album list may be empty");
		}
		galleryUrl = driver.getCurrentUrl().split("\\?")[0];
		logAction("Gallery Settings page loaded: " + galleryUrl + " (" + getVisibleAlbumCount() + " albums listed)");
	}

	public boolean isGalleryPageDisplayed() {
		return driver.getCurrentUrl().contains("/settings_new/job/gallery") && addNewAlbumHeader.isDisplayed();
	}

	// Re-open the page through the URL captured when it was first opened from the UI
	public void openGalleryPage() {
		logAction("Opening Gallery Settings page: " + galleryUrl);
		driver.get(galleryUrl);
		waitForGalleryPageToLoad();
	}

	public void refreshGalleryPage() {
		logAction("Reloading Gallery Settings page");
		Non_WebDriver_Util.refreshPage(driver);
		waitForGalleryPageToLoad();
	}

	// ==========================================
	// Create (Add New Album)
	// ==========================================
	public void enterAlbumName(String albumName) {
		typeInto(newAlbumNameInput, "Album Name", albumName);
	}

	public void clearAlbumName() {
		logAction("Clearing 'Album Name'");
		newAlbumNameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
	}

	public String getAlbumNameValue() {
		String value = newAlbumNameInput.getAttribute("value");
		return value == null ? "" : value.trim();
	}

	public boolean isAddAlbumButtonEnabled() {
		boolean enabled = addAlbumButton.isEnabled();
		logAction("'Add Album' button enabled: " + enabled);
		return enabled;
	}

	public void clickAddAlbum() {
		logAction("Clicking 'Add Album' button");
		Non_WebDriver_Util.waitForBeClickable(driver, addAlbumButton, 10);
		addAlbumButton.click();
	}

	public void addAlbum(String albumName) {
		enterAlbumName(albumName);
		clickAddAlbum();
	}

	// ==========================================
	// List / search
	// ==========================================
	// Search filters the list while typing - wait until only matching rows (or the empty state) are shown.
	// After add/update/delete the list reloads and can drop the filter, so the text is re-typed when needed.
	public void searchAlbum(String albumName) {
		for (int attempt = 1; attempt <= 3; attempt++) {
			logAction("Searching albums for '" + albumName + "'" + (attempt > 1 ? " (attempt " + attempt + ")" : ""));
			Non_WebDriver_Util.waitForBeClickable(driver, albumSearchInput, 20);
			albumSearchInput.click();
			albumSearchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
			albumSearchInput.sendKeys(albumName);
			if (waitForSearchResult(albumName, 7)) {
				int count = getVisibleAlbumCount();
				logAction("Search result for '" + albumName + "': " + (count == 0 ? "No albums found" : count + " row(s)"));
				return;
			}
			logger.warn("[ACTION] List not filtered yet - search box value is '" + albumSearchInput.getAttribute("value")
					+ "', " + getVisibleAlbumCount() + " row(s) shown");
		}
		throw new IllegalStateException("❌ Albums list was not filtered for search text: " + albumName
				+ " (locator: //input[@placeholder='Search...'])");
	}

	public boolean isAlbumDisplayed(String albumName) {
		return Non_WebDriver_Util.findIfVisible(driver, rowByName(albumName), 5) != null;
	}

	public int getMatchingAlbumCount(String albumName) {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(rowByName(albumName)).size());
		return count == null ? 0 : count;
	}

	public boolean isNoAlbumsFoundDisplayed() {
		return Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, d -> isEmptyStateVisible(d)) != null;
	}

	public boolean isAlbumDeleted(String albumName) {
		return isNoAlbumsFoundDisplayed() && getMatchingAlbumCount(albumName) == 0;
	}

	// Reads the list columns: Album Name | Created On
	public Map<String, String> getAlbumRowDetails(String albumName) {
		WebElement row = driver.findElement(rowByName(albumName));
		Map<String, String> details = new LinkedHashMap<>();
		details.put("name", cellText(row, "./div[1]"));
		details.put("createdOn", cellText(row, "./div[2]"));
		logAction("Read album row: " + details);
		return details;
	}

	// ==========================================
	// Edit (inline)
	// ==========================================
	public void clickEditAlbum(String albumName) {
		logAction("Clicking Edit (pencil) icon for album '" + albumName + "'");
		WebElement editIcon = driver.findElement(rowByName(albumName)).findElement(By.xpath(ROW_EDIT_ICON));
		// success toasts can briefly overlap the row - retry on intercepted click
		Non_WebDriver_Util.clickWithRetry(driver, editIcon, 5, 1000);
		if (Non_WebDriver_Util.findIfVisible(driver, inlineEditInput, 10) == null) {
			throw new IllegalStateException("❌ Inline edit box did not open for album '" + albumName + "' (locator: " + inlineEditInput + ")");
		}
		logAction("Inline edit opened for album '" + albumName + "'");
	}

	public String getInlineEditValue() {
		List<WebElement> input = driver.findElements(inlineEditInput);
		String value = input.isEmpty() ? "" : input.get(0).getAttribute("value");
		logAction("Inline edit box value: '" + value + "'");
		return value == null ? "" : value.trim();
	}

	public void updateAlbumName(String newAlbumName) {
		WebElement input = driver.findElement(inlineEditInput);
		typeInto(input, "Album Name (inline edit)", newAlbumName);
	}

	public void clickSaveInlineEdit() {
		clickInlineEditButton("Save");
	}

	public void cancelInlineEdit() {
		clickInlineEditButton("Cancel");
	}

	public boolean isInlineEditClosed() {
		return Non_WebDriver_Util.waitForInvisibility(driver, inlineEditInput, 10);
	}

	// ==========================================
	// Delete actions
	// ==========================================
	// Opens the delete confirmation; returns its message
	public String clickDeleteAlbum(String albumName) {
		logAction("Clicking Delete (trash) icon for album '" + albumName + "'");
		WebElement deleteIcon = driver.findElement(rowByName(albumName)).findElement(By.xpath(ROW_DELETE_ICON));
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
	private By rowByName(String albumName) {
		return By.xpath(String.format(ROW_BY_NAME, albumName));
	}

	// 'No albums found' can be clipped by the small list container on low-height windows (not 'displayed' for Selenium),
	// so it is checked by presence - only after the list has loaded (see waitForGalleryPageToLoad)
	private boolean isEmptyStateVisible(WebDriver d) {
		return !d.findElements(noAlbumsFoundMessage).isEmpty();
	}

	private int getVisibleAlbumCount() {
		Integer count = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(albumRows).size());
		return count == null ? 0 : count;
	}

	private boolean waitForSearchResult(String albumName, int timeoutSeconds) {
		Boolean filtered = Non_WebDriver_Util.waitWithoutImplicitWait(driver, timeoutSeconds, d -> {
			if (isEmptyStateVisible(d)) {
				return true;
			}
			List<WebElement> rows = d.findElements(albumRows);
			if (rows.isEmpty()) {
				return false;
			}
			try {
				for (WebElement row : rows) {
					if (!row.getText().toLowerCase().contains(albumName.toLowerCase())) {
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

	private void clickInlineEditButton(String buttonText) {
		By button = By.xpath(String.format(INLINE_EDIT_BUTTON, buttonText));
		logAction("Clicking '" + buttonText + "' in the inline album edit");
		WebElement buttonElement = Non_WebDriver_Util.findIfVisible(driver, button, 10);
		if (buttonElement == null) {
			throw new IllegalStateException("❌ Inline edit button '" + buttonText + "' not displayed (locator: " + button + ")");
		}
		Non_WebDriver_Util.clickWithRetry(driver, buttonElement, 5, 1000);
	}

	private void typeInto(WebElement field, String fieldName, String value) {
		logAction("Entering '" + fieldName + "' = '" + value + "'");
		Non_WebDriver_Util.waitForVisible(driver, field, 20);
		field.click();
		field.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
		field.sendKeys(value);
	}

	private String cellText(WebElement row, String cellXpath) {
		return Non_WebDriver_Util.getInnerText(driver, row.findElement(By.xpath(cellXpath))).trim().replaceAll("\\s+", " ");
	}
}
