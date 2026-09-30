package PageObject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
 * Settings > Users & Teams > User Management
 * - Users list (search, row menu, status badge)
 * - Create / Edit user form
 * - Confirmation dialogs and toast messages
 *
 * Every UI action is logged as "[ACTION] ..." through the Baseclass logger.
 */
public class UserManagementPage extends Baseclass {

	private WebDriver driver;
	private String usersListUrl;

	public UserManagementPage() {
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	// ==========================================
	// Users list elements
	// ==========================================
	@FindBy(xpath = "//h3[normalize-space()='Users']")
	private WebElement usersHeader;

	@FindBy(xpath = "//button[normalize-space()='Create New User']")
	private WebElement createNewUserButton;

	// Table search box (the settings side menu has another 'Search' input before it)
	@FindBy(xpath = "//cdk-table/preceding::input[@placeholder='Search'][1]")
	private WebElement userSearchInput;

	private final By userRows = By.xpath("//cdk-row");
	private final By noUsersFoundMessage = By.xpath("//*[normalize-space(text())='No Users Found']");
	private final By rowMenuItems = By.xpath("//button[@role='menuitem']");
	private static final String ROW_BY_EMAIL = "//cdk-row[.//div[normalize-space()='%s']]";
	private static final String ROW_MENU_ITEM = "//button[@role='menuitem' and normalize-space()='%s']";

	// ==========================================
	// Create / Edit user form elements
	// ==========================================
	@FindBy(id = "emp_code")
	private WebElement userIdInput;

	@FindBy(id = "first_name")
	private WebElement firstNameInput;

	@FindBy(id = "last_name")
	private WebElement lastNameInput;

	@FindBy(id = "designation")
	private WebElement designationInput;

	@FindBy(id = "email")
	private WebElement emailInput;

	@FindBy(id = "password")
	private WebElement passwordInput;

	@FindBy(xpath = "//brn-select[@formcontrolname='role_id']//button[@role='combobox']")
	private WebElement roleDropdown;

	@FindBy(xpath = "//brn-select[@formcontrolname='allow_user_set_password']//button[@role='combobox']")
	private WebElement invitePasswordDropdown;

	@FindBy(xpath = "//button[normalize-space()='Save User']")
	private WebElement saveUserButton;

	@FindBy(xpath = "//button[normalize-space()='Save User']/preceding::button[normalize-space()='Cancel'][1]")
	private WebElement formCancelButton;

	private final By passwordField = By.id("password");
	private final By requiredFieldErrors = By.xpath("//p[contains(@class,'text-red-500') and normalize-space()='This field is required']");
	private static final String DROPDOWN_OPTION = "//hlm-option[normalize-space()='%s']";
	private static final String FIELD_ERROR = "//label[starts-with(normalize-space(),'%s')]/..//p[contains(@class,'text-red-500')]";

	// ==========================================
	// Dialogs & toasts
	// ==========================================
	private final By dialog = By.xpath("//mat-dialog-container");
	private final By skipTeamAssignmentButton = By.xpath("//mat-dialog-container//button[normalize-space()='Skip & Add later']");
	private final By leavePageButton = By.xpath("//mat-dialog-container//button[normalize-space()='Leave Page']");
	private static final String DIALOG_BUTTON = "//mat-dialog-container//button[normalize-space()='%s']";
	private static final String TOAST_MESSAGE = "//div[contains(@class,'hot-toast-message')][contains(normalize-space(),'%s')]";

	// ==========================================
	// Users list actions
	// ==========================================
	public void waitForUsersListToLoad() {
		logAction("Waiting for Users list page to load");
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.withMessage("Users list URL (/users-and-teams/users) - current URL: " + driver.getCurrentUrl())
				.until(d -> d.getCurrentUrl().matches(".*/users-and-teams/users/?(\\?.*)?$"));
		Non_WebDriver_Util.waitForVisible(driver, usersHeader, 30);
		Non_WebDriver_Util.waitForVisible(driver, createNewUserButton, 30);
		usersListUrl = driver.getCurrentUrl().split("\\?")[0];
		logAction("Users list loaded: " + usersListUrl);
	}

	public boolean isUsersListDisplayed() {
		return driver.getCurrentUrl().contains("/users-and-teams/users") && usersHeader.isDisplayed();
	}

	public boolean isCreateNewUserButtonAvailable() {
		return createNewUserButton.isDisplayed() && createNewUserButton.isEnabled();
	}

	// Re-open the list through the URL captured when the list was first opened from the UI
	public void openUsersList() {
		logAction("Opening Users list: " + usersListUrl);
		driver.get(usersListUrl);
		waitForUsersListToLoad();
	}

	public void refreshUsersList() {
		logAction("Reloading Users list page");
		Non_WebDriver_Util.refreshPage(driver);
		waitForUsersListToLoad();
	}

	// Search runs on ENTER (matches User ID / name / email) - wait until the grid shows only matching rows (or the empty state)
	public void searchUser(String searchText) {
		logAction("Searching Users list for '" + searchText + "' (typing in table search box + ENTER)");
		Non_WebDriver_Util.waitForBeClickable(driver, userSearchInput, 20);
		userSearchInput.click();
		userSearchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
		userSearchInput.sendKeys(searchText, Keys.ENTER);
		Boolean filtered = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, d -> {
			if (!d.findElements(noUsersFoundMessage).isEmpty()) {
				return true;
			}
			List<WebElement> rows = d.findElements(userRows);
			if (rows.isEmpty()) {
				return false;
			}
			for (WebElement row : rows) {
				if (!row.getText().toLowerCase().contains(searchText.toLowerCase())) {
					return false;
				}
			}
			return true;
		});
		if (filtered == null) {
			throw new IllegalStateException("❌ Users list was not filtered for search text: " + searchText);
		}
		int rowCount = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 1, d -> d.findElements(userRows).size());
		logAction("Search result for '" + searchText + "': " + (rowCount == 0 ? "No Users Found" : rowCount + " row(s)"));
	}

	public boolean isUserDisplayed(String email) {
		return Non_WebDriver_Util.findIfVisible(driver, rowByEmail(email), 5) != null;
	}

	public boolean isNoUsersFoundDisplayed() {
		return Non_WebDriver_Util.findIfVisible(driver, noUsersFoundMessage, 10) != null;
	}

	// Reads the list columns: User ID | Name (+email) | Designation | Role | Status | Created On
	public Map<String, String> getUserRowDetails(String email) {
		WebElement row = driver.findElement(rowByEmail(email));
		Map<String, String> details = new LinkedHashMap<>();
		details.put("userId", cellText(row, ".//hlm-td[1]"));
		details.put("name", cellText(row, ".//hlm-td[2]//div[contains(@class,'pl-3')]/div[1]"));
		details.put("email", cellText(row, ".//hlm-td[2]//div[contains(@class,'pl-3')]/div[2]"));
		details.put("designation", cellText(row, ".//hlm-td[3]"));
		details.put("role", cellText(row, ".//hlm-td[4]"));
		details.put("status", getUserStatus(email));
		details.put("createdOn", cellText(row, ".//hlm-td[6]"));
		logAction("Read Users list row: " + details);
		return details;
	}

	public String getUserStatus(String email) {
		return driver.findElement(rowByEmail(email)).findElement(By.xpath(".//hlm-td[5]//badge")).getAttribute("title").trim();
	}

	public boolean waitForUserStatus(String email, String expectedStatus, int timeoutInSeconds) {
		logAction("Waiting up to " + timeoutInSeconds + "s for status of " + email + " to become '" + expectedStatus + "'");
		boolean matched = Non_WebDriver_Util.waitWithoutImplicitWait(driver, timeoutInSeconds, d -> {
			try {
				return getUserStatus(email).equalsIgnoreCase(expectedStatus);
			} catch (Exception e) {
				return false; // row re-rendering after update
			}
		}) != null;
		logAction("Status badge for " + email + ": '" + safeStatus(email) + "'");
		return matched;
	}

	public List<String> getRowMenuOptions(String email) {
		openRowMenu(email);
		List<String> options = new ArrayList<>();
		for (WebElement item : driver.findElements(rowMenuItems)) {
			options.add(item.getText().trim());
		}
		closeRowMenu();
		logAction("Row menu options for " + email + ": " + options);
		return options;
	}

	public void clickRowMenuOption(String email, String option) {
		openRowMenu(email);
		logAction("Clicking row menu option '" + option + "'");
		WebElement menuItem = driver.findElement(By.xpath(String.format(ROW_MENU_ITEM, option)));
		Non_WebDriver_Util.clickWithRetry(driver, menuItem, 5, 1000);
	}

	// ==========================================
	// Create / Edit form actions
	// ==========================================
	public void clickCreateNewUser() {
		logAction("Clicking 'Create New User' button");
		Non_WebDriver_Util.waitForBeClickable(driver, createNewUserButton, 20);
		createNewUserButton.click();
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains("/users/new"));
		Non_WebDriver_Util.waitForVisible(driver, userIdInput, 30);
		logAction("Create User form opened: " + driver.getCurrentUrl());
	}

	public boolean isCreateUserFormDisplayed() {
		return driver.getCurrentUrl().contains("/users/new") && userIdInput.isDisplayed();
	}

	public void enterBasicInformation(String userId, String firstName, String lastName, String designation, String role) {
		typeInto(userIdInput, "User ID", userId);
		typeInto(firstNameInput, "First Name", firstName);
		typeInto(lastNameInput, "Last Name", lastName);
		typeInto(designationInput, "Designation", designation);
		selectDropdownOption(roleDropdown, "Choose Role", role);
	}

	// Invite = 'No' so no invitation mail is sent; the password field then becomes mandatory
	public void enterLoginInformation(String email, String password) {
		typeInto(emailInput, "Email", email);
		selectDropdownOption(invitePasswordDropdown, "Invite User to set password", "No");
		Non_WebDriver_Util.waitForVisible(driver, driver.findElement(passwordField), 10);
		logAction("Entering 'Password' = ******** (" + (password == null ? 0 : password.length()) + " chars)");
		Non_WebDriver_Util.waitForVisible(driver, passwordInput, 20);
		passwordInput.clear();
		passwordInput.sendKeys(password);
	}

	public void clickSaveUser() {
		logAction("Clicking 'Save User' button");
		Non_WebDriver_Util.scrollIntoViewAndClick(driver, saveUserButton);
	}

	// 'Assign <name> to Team' dialog appears after Save on create
	public void skipTeamAssignment() {
		logAction("Waiting for 'Assign to Team' dialog and clicking 'Skip & Add later'");
		WebElement skipButton = Non_WebDriver_Util.findIfVisible(driver, skipTeamAssignmentButton, 20);
		if (skipButton == null) {
			throw new IllegalStateException("❌ 'Assign to Team' dialog did not appear after saving the user (locator: " + skipTeamAssignmentButton + ")");
		}
		skipButton.click();
		Non_WebDriver_Util.waitForInvisibility(driver, skipTeamAssignmentButton, 10);
	}

	public void createUser(String userId, String firstName, String lastName, String designation, String role,
			String email, String password) {
		clickCreateNewUser();
		enterBasicInformation(userId, firstName, lastName, designation, role);
		enterLoginInformation(email, password);
		clickSaveUser();
		skipTeamAssignment();
	}

	public int getRequiredFieldErrorCount() {
		List<WebElement> errors = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10,
				ExpectedConditions.visibilityOfAllElementsLocatedBy(requiredFieldErrors));
		int count = errors == null ? 0 : errors.size();
		logAction("'This field is required' messages displayed: " + count);
		return count;
	}

	// Validation message shown below the field whose label starts with 'fieldLabel' ("" when none)
	public String getFieldErrorMessage(String fieldLabel) {
		WebElement error = Non_WebDriver_Util.findIfVisible(driver, By.xpath(String.format(FIELD_ERROR, fieldLabel)), 5);
		String message = error == null ? "" : error.getText().trim();
		logAction("Validation message under '" + fieldLabel + "': '" + message + "'");
		return message;
	}

	// Cancel the form; confirm the 'Unsaved Changes' dialog when it shows up (retries Cancel once if the click is ignored)
	public void cancelUserForm() {
		for (int attempt = 1; attempt <= 2; attempt++) {
			logAction("Clicking 'Cancel' on the user form (attempt " + attempt + ")");
			Non_WebDriver_Util.scrollIntoViewAndClick(driver, formCancelButton);
			// either the 'Unsaved Changes' dialog appears or the app navigates straight back to the list
			String outcome = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 15, d -> {
				if (!d.getCurrentUrl().contains("/users/new") && !d.getCurrentUrl().endsWith("/edit")) {
					return "left";
				}
				try {
					return d.findElements(leavePageButton).stream().anyMatch(WebElement::isDisplayed) ? "dialog" : null;
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					return null; // dialog re-rendering
				}
			});
			if ("dialog".equals(outcome)) {
				logAction("'Unsaved Changes' dialog displayed - clicking 'Leave Page'");
				driver.findElement(leavePageButton).click();
			}
			if (outcome != null) {
				waitForUsersListToLoad();
				return;
			}
			logger.warn("[ACTION] Neither 'Unsaved Changes' dialog nor Users list appeared 15s after 'Cancel' - still on " + driver.getCurrentUrl());
		}
		throw new IllegalStateException("❌ 'Cancel' on the user form did not return to the Users list (locator: "
				+ "//button[normalize-space()='Save User']/preceding::button[normalize-space()='Cancel'][1])");
	}

	public boolean isOnCreateUserPage() {
		return driver.getCurrentUrl().contains("/users/new");
	}

	public void openEditUser(String email) {
		clickRowMenuOption(email, "Edit Details");
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlMatches(".*/users/[^/]+/edit$"));
		// form is pre-filled asynchronously
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, d -> !getFieldValue(emailInput).isEmpty());
		logAction("Edit User form opened: " + driver.getCurrentUrl());
	}

	public Map<String, String> getEditFormValues() {
		Map<String, String> values = new LinkedHashMap<>();
		values.put("userId", getFieldValue(userIdInput));
		values.put("firstName", getFieldValue(firstNameInput));
		values.put("lastName", getFieldValue(lastNameInput));
		values.put("designation", getFieldValue(designationInput));
		values.put("email", getFieldValue(emailInput));
		values.put("role", roleDropdown.getText().trim());
		logAction("Read Edit User form values: " + values);
		return values;
	}

	public void updateUserDetails(String lastName, String designation) {
		typeInto(lastNameInput, "Last Name", lastName);
		typeInto(designationInput, "Designation", designation);
	}

	// Save on the edit form opens an 'Update User' confirmation - returns its message
	public String saveUserUpdate() {
		clickSaveUser();
		String dialogText = getDialogText();
		confirmDialog("Update");
		return dialogText;
	}

	// ==========================================
	// User lifecycle actions (row menu + confirmation)
	// ==========================================
	// Active -> In-Active; returns the confirmation message
	public String deactivateUser(String email) {
		logAction("Deactivating user " + email);
		clickRowMenuOption(email, "Deactivate User");
		String dialogText = getDialogText();
		confirmDialog("Deactivate");
		return dialogText;
	}

	// In-Active -> Active; returns the confirmation message
	public String activateUser(String email) {
		logAction("Activating user " + email);
		clickRowMenuOption(email, "Activate User");
		String dialogText = getDialogText();
		confirmDialog("Activate");
		return dialogText;
	}

	// 'Delete User' is offered only for In-Active users; returns the confirmation message
	public String openDeleteConfirmation(String email) {
		logAction("Opening delete confirmation for user " + email);
		clickRowMenuOption(email, "Delete User");
		return getDialogText();
	}

	public void confirmDelete() {
		confirmDialog("Delete User");
	}

	// Returns false when the dialog stays open after Cancel
	public boolean cancelDelete() {
		return clickDialogButton("Cancel");
	}

	public boolean isUserStatus(String email, String expectedStatus) {
		return waitForUserStatus(email, expectedStatus, 20);
	}

	public boolean isUserDeleted(String email) {
		return isNoUsersFoundDisplayed() && !isUserDisplayed(email);
	}

	private void confirmDialog(String buttonText) {
		if (!clickDialogButton(buttonText)) {
			throw new IllegalStateException("❌ Confirmation dialog did not close after clicking '" + buttonText + "'");
		}
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
		logAction("Confirmation dialog displayed: \"" + text + "\"");
		return text;
	}

	public boolean isDialogDisplayed() {
		return Non_WebDriver_Util.findIfVisible(driver, dialog, 5) != null;
	}

	// Clicks a dialog button and waits for the dialog to close; returns false when it stays open
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
	private By rowByEmail(String email) {
		return By.xpath(String.format(ROW_BY_EMAIL, email));
	}

	private void openRowMenu(String email) {
		logAction("Opening row menu (3-dot) for user " + email);
		WebElement menuTrigger = driver.findElement(rowByEmail(email))
				.findElement(By.xpath(".//button[contains(@class,'cdk-menu-trigger')]"));
		// success toasts can briefly overlap the row - retry on intercepted click
		Non_WebDriver_Util.clickWithRetry(driver, menuTrigger, 5, 1000);
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, ExpectedConditions.visibilityOfElementLocated(rowMenuItems));
	}

	private void closeRowMenu() {
		new Actions(driver).sendKeys(Keys.ESCAPE).perform();
		Non_WebDriver_Util.waitForInvisibility(driver, rowMenuItems, 5);
	}

	private void selectDropdownOption(WebElement dropdown, String dropdownName, String optionText) {
		logAction("Selecting '" + optionText + "' from '" + dropdownName + "' dropdown");
		Non_WebDriver_Util.scrollIntoViewAndClick(driver, dropdown);
		By option = By.xpath(String.format(DROPDOWN_OPTION, optionText));
		WebElement optionElement = Non_WebDriver_Util.findIfVisible(driver, option, 10);
		if (optionElement == null) {
			throw new IllegalArgumentException("❌ Dropdown option not found: '" + optionText + "' in '" + dropdownName + "' (locator: " + option + ")");
		}
		optionElement.click();
		Non_WebDriver_Util.waitForInvisibility(driver, option, 5);
	}

	private void typeInto(WebElement field, String fieldName, String value) {
		logAction("Entering '" + fieldName + "' = '" + value + "'");
		Non_WebDriver_Util.waitForVisible(driver, field, 20);
		field.clear();
		field.sendKeys(value);
	}

	private String getFieldValue(WebElement field) {
		String value = field.getAttribute("value");
		return value == null ? "" : value.trim();
	}

	private String safeStatus(String email) {
		try {
			return getUserStatus(email);
		} catch (Exception e) {
			return "not readable";
		}
	}

	private String cellText(WebElement row, String cellXpath) {
		return Non_WebDriver_Util.getInnerText(driver, row.findElement(By.xpath(cellXpath))).trim();
	}
}
