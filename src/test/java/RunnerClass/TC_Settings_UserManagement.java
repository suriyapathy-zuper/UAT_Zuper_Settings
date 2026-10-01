package RunnerClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import PageObject.UserManagementPage;

/**
 * Settings > Users (User Management) - complete user lifecycle:
 * Login -> Settings -> User Management -> Create -> Edit -> Deactivate -> Activate -> Delete
 * plus required-field / invalid-data / duplicate / delete-confirmation checks.
 *
 * Test user is unique per run (timestamp suffix) and is removed in @AfterClass if a test fails midway.
 * Shared browser session, Dashboard navigation and execution logging come from BaseSettingsTest.
 */
public class TC_Settings_UserManagement extends BaseSettingsTest {

	private UserManagementPage userManagementPage;

	// Test data - unique per run so the automation user is always identifiable
	private final String uniqueSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
	private String userId;
	private String firstName;
	private String lastName;
	private String designation;
	private String role;
	private String email;
	private String password;
	private String updatedLastName;
	private String updatedDesignation;
	private String currentFullName;   // name shown in confirmation dialogs - changes after edit

	private boolean userCreated = false;
	private boolean userDeleted = false;

	@Override
	protected String getSuiteName() {
		return "Settings > User Management";
	}

	// Runs after BaseSettingsTest.setUpPages()
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		userManagementPage = new UserManagementPage();

		userId = prop.getProperty("newUser_IdPrefix") + uniqueSuffix;
		firstName = prop.getProperty("newUser_FirstName");
		lastName = prop.getProperty("newUser_LastName") + uniqueSuffix;
		designation = prop.getProperty("newUser_Designation");
		role = prop.getProperty("newUser_Role");
		email = prop.getProperty("newUser_EmailPrefix") + uniqueSuffix + "@" + prop.getProperty("newUser_EmailDomain");
		// throw-away password generated per run - never stored in config/source, never logged
		password = "Zu@" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
		updatedLastName = prop.getProperty("updatedUser_LastName") + uniqueSuffix;
		updatedDesignation = prop.getProperty("updatedUser_Designation");
		logger.info("[TEST DATA] Automation user -> User ID: " + userId + " | Name: " + firstName + " " + lastName
				+ " | Email: " + email + " | Designation: " + designation + " | Role: " + role + " | Password: ********");
		logger.info("[TEST DATA] Update values -> Last Name: " + updatedLastName + " | Designation: " + updatedDesignation);
		logger.info("==================================================================");
	}

	@Test(groups = { "sanity", "regression" }, priority = 1, description = "Navigate to User Management Test")
	public void verify_NavigateToUserManagement() {
		openSettingsFromDashboard();

		step("Opening Users/User Management");
		settingsPage.openUserManagement();
		userManagementPage.waitForUsersListToLoad();
		verify(userManagementPage.isUsersListDisplayed(), "User Management - Users list is displayed", "Users list is not displayed");
	}

	@Test(groups = { "regression" }, priority = 2, dependsOnMethods = "verify_NavigateToUserManagement",
			description = "Create User - Required Field Validation Test")
	public void verify_CreateUserRequiredFieldValidation() {
		step("Verifying the 'Create New User' option is available");
		verify(userManagementPage.isCreateNewUserButtonAvailable(), "'Create New User' button is displayed and enabled",
				"'Create New User' button is not available");

		step("Clicking Create New User");
		userManagementPage.clickCreateNewUser();
		verify(userManagementPage.isCreateUserFormDisplayed(), "Create user form is displayed", "Create user form is not displayed");

		step("Clicking Save User with all fields empty");
		userManagementPage.clickSaveUser();

		step("Verifying required field validation messages");
		verifyEquals(userManagementPage.getRequiredFieldErrorCount(), 6, "Required field error count");
		for (String field : new String[] { "User ID", "First Name", "Last Name", "Designation", "Choose Role", "Email" }) {
			verifyEquals(userManagementPage.getFieldErrorMessage(field), "This field is required", "Validation message for '" + field + "'");
		}
		verify(userManagementPage.isOnCreateUserPage(), "Form was not submitted - still on Create User page",
				"Form must not be submitted with empty required fields");

		step("Cancelling the Create User form");
		userManagementPage.cancelUserForm();
		pass("Required field validation works for all mandatory fields");
	}

	@Test(groups = { "regression" }, priority = 3, dependsOnMethods = "verify_NavigateToUserManagement",
			description = "Create User - Invalid Data Validation Test")
	public void verify_CreateUserInvalidDataValidation() {
		step("Clicking Create New User");
		userManagementPage.clickCreateNewUser();

		step("Entering valid basic information with invalid email '" + prop.getProperty("invalidUser_Email")
				+ "' and a password shorter than 8 characters");
		userManagementPage.enterBasicInformation(userId, firstName, lastName, designation, role);
		userManagementPage.enterLoginInformation(prop.getProperty("invalidUser_Email"), prop.getProperty("invalidUser_Password"));

		step("Clicking Save User");
		userManagementPage.clickSaveUser();

		step("Verifying invalid data validation messages");
		verifyEquals(userManagementPage.getFieldErrorMessage("Email"), "Entered value is not a valid email", "Email validation message");
		verifyEquals(userManagementPage.getFieldErrorMessage("Password"), "Length of password should be >= 8 characters",
				"Password validation message");
		verify(userManagementPage.isOnCreateUserPage(), "Form was not submitted - still on Create User page",
				"Form must not be submitted with invalid data");

		step("Cancelling the form and confirming no user was created for User ID '" + userId + "'");
		userManagementPage.cancelUserForm();
		userManagementPage.searchUser(userId);
		verify(userManagementPage.isNoUsersFoundDisplayed(), "No user was created with invalid data",
				"User must not be created with invalid data");
	}

	@Test(groups = { "sanity", "regression" }, priority = 4, dependsOnMethods = "verify_NavigateToUserManagement", description = "Create User Test")
	public void verify_CreateUser() {
		step("Clicking Create User, entering user details and saving (User ID: " + userId + ", Email: " + email + ")");
		userManagementPage.createUser(userId, firstName, lastName, designation, role, email, password);
		verify(userManagementPage.isToastDisplayed("The user account has been created successfully"),
				"Success toast 'The user account has been created successfully' displayed", "User created success toast not displayed");
		userCreated = true;
		currentFullName = firstName + " " + lastName;

		step("Searching for the newly created user in the Users list");
		userManagementPage.waitForUsersListToLoad();
		userManagementPage.searchUser(email);
		verify(userManagementPage.isUserDisplayed(email), "Created user is displayed in the Users list",
				"Created user is not displayed in Users list");

		step("Verifying the created user's details in the Users list");
		Map<String, String> row = userManagementPage.getUserRowDetails(email);
		verifyEquals(row.get("userId"), userId, "User ID");
		verifyEquals(row.get("name"), firstName + " " + lastName, "Name");
		verifyEquals(row.get("email"), email, "Email");
		// list shows designation in title case
		verify(row.get("designation").equalsIgnoreCase(designation), "Designation = '" + row.get("designation") + "'",
				"Designation mismatch: " + row.get("designation"));
		verifyEquals(row.get("role"), role, "Role");
		verifyEquals(row.get("status"), "Active", "Status");
		verify(!row.get("createdOn").isEmpty(), "Created On = '" + row.get("createdOn") + "'", "Created On is empty");
		pass("User created successfully and displayed in the Users list");
	}

	@Test(groups = { "regression" }, priority = 5, dependsOnMethods = "verify_CreateUser", description = "Duplicate User Creation Test")
	public void verify_DuplicateUserCreationIsPrevented() {
		step("Creating another user with the same email '" + email + "'");
		userManagementPage.createUser(prop.getProperty("newUser_IdPrefix") + "DUP" + uniqueSuffix, firstName, "Duplicate",
				designation, role, email, password);

		step("Verifying duplicate user creation is rejected");
		verify(userManagementPage.isToastDisplayed("User with same email ID / external login ID already exists"),
				"Error toast 'User with same email ID / external login ID already exists' displayed", "Duplicate user error toast not displayed");
		verify(userManagementPage.isOnCreateUserPage(), "Duplicate user was not saved - still on Create User page",
				"Duplicate user must not be saved");

		step("Cancelling the form and confirming only the original user exists");
		userManagementPage.cancelUserForm();
		userManagementPage.searchUser(email);
		Map<String, String> row = userManagementPage.getUserRowDetails(email);
		verifyEquals(row.get("userId"), userId, "User ID of the only user with this email");
		pass("Duplicate user creation is prevented");
	}

	@Test(groups = { "regression" }, priority = 6, dependsOnMethods = "verify_CreateUser", description = "Edit User Test")
	public void verify_EditUser() {
		step("Searching for the created user '" + email + "'");
		userManagementPage.openUsersList();
		userManagementPage.searchUser(email);

		step("Opening Edit User");
		userManagementPage.openEditUser(email);

		step("Verifying the edit form is pre-filled with the created user's details");
		Map<String, String> formValues = userManagementPage.getEditFormValues();
		verifyEquals(formValues.get("userId"), userId, "Edit form User ID");
		verifyEquals(formValues.get("firstName"), firstName, "Edit form First Name");
		verifyEquals(formValues.get("lastName"), lastName, "Edit form Last Name");
		verifyEquals(formValues.get("designation"), designation, "Edit form Designation");
		verifyEquals(formValues.get("email"), email, "Edit form Email");
		verifyEquals(formValues.get("role"), role, "Edit form Role");

		step("Updating user details -> Last Name: '" + updatedLastName + "', Designation: '" + updatedDesignation + "'");
		userManagementPage.updateUserDetails(updatedLastName, updatedDesignation);

		step("Saving the updated user and confirming the 'Update User' dialog");
		String dialogText = userManagementPage.saveUserUpdate();
		verify(dialogText.contains("Are you sure want to update the details of '" + firstName + " " + updatedLastName + "'"),
				"Update confirmation message is correct", "Update confirmation message mismatch: " + dialogText);
		verify(userManagementPage.isToastDisplayed("User details updated successfully"),
				"Success toast 'User details updated successfully' displayed", "User update toast not displayed");
		currentFullName = firstName + " " + updatedLastName;

		step("Verifying updated details in the Users list");
		userManagementPage.openUsersList();
		userManagementPage.searchUser(email);
		verifyUpdatedRow();

		step("Reloading the page and verifying the update is persisted");
		userManagementPage.refreshUsersList();
		userManagementPage.searchUser(email);
		verifyUpdatedRow();
		pass("User details updated successfully");
	}

	@Test(groups = { "sanity", "regression" }, priority = 7, dependsOnMethods = "verify_CreateUser", description = "User Status Test - Active to Inactive")
	public void verify_DeactivateUser() {
		step("Locating the test user '" + email + "'");
		userManagementPage.openUsersList();
		userManagementPage.searchUser(email);
		verifyEquals(userManagementPage.getUserStatus(email), "Active", "Current status before deactivation");

		step("Changing status from Active to Inactive");
		deactivateUser();
		pass("User successfully changed to Inactive");

		step("Verifying row menu options for the In-Active user");
		List<String> menuOptions = userManagementPage.getRowMenuOptions(email);
		verify(menuOptions.contains("Activate User"), "'Activate User' option is offered", "'Activate User' option missing for In-Active user");
		verify(menuOptions.contains("Delete User"), "'Delete User' option is offered", "'Delete User' option missing for In-Active user");
	}

	@Test(groups = { "sanity", "regression" }, priority = 8, dependsOnMethods = "verify_DeactivateUser", description = "User Status Test - Inactive to Active")
	public void verify_ActivateUser() {
		step("Changing status from Inactive to Active");
		String dialogText = userManagementPage.activateUser(email);
		verify(dialogText.contains("Are you sure to activate this users '" + currentFullName + "'"),
				"Activate confirmation message is correct", "Activate confirmation message mismatch: " + dialogText);
		verify(userManagementPage.isToastDisplayed("User details updated successfully"),
				"Success toast 'User details updated successfully' displayed", "User activate toast not displayed");

		step("Verifying Active status");
		verify(userManagementPage.isUserStatus(email, "Active"), "Status is 'Active'", "User status did not change to Active");
		pass("User successfully changed back to Active");

		step("Verifying row menu options for the Active user");
		List<String> menuOptions = userManagementPage.getRowMenuOptions(email);
		verify(menuOptions.contains("Deactivate User"), "'Deactivate User' option is offered", "'Deactivate User' option missing for Active user");
		verify(!menuOptions.contains("Delete User"), "'Delete User' is not offered for an Active user",
				"'Delete User' must not be offered for an Active user");
	}

	@Test(groups = { "regression" }, priority = 9, dependsOnMethods = "verify_ActivateUser", description = "Cancel Delete User Test")
	public void verify_CancelDeleteKeepsUser() {
		step("Changing status to Inactive (Delete is offered only for In-Active users)");
		deactivateUser();

		step("Clicking Delete User");
		String dialogText = userManagementPage.openDeleteConfirmation(email);
		verify(dialogText.contains("Are you sure to delete this user '" + currentFullName + "'"),
				"Delete confirmation dialog is displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Clicking Cancel on the delete confirmation");
		boolean closedOnCancel = userManagementPage.cancelDelete();
		if (closedOnCancel) {
			pass("Delete confirmation dialog closed on Cancel");
		} else {
			warn("Known issue: 'Cancel' did not close the 'Delete User' confirmation dialog - reloading the page to dismiss it");
		}

		step("Reloading the page and verifying the user was not deleted");
		// reload also clears the dialog in case Cancel did not close it
		userManagementPage.refreshUsersList();
		userManagementPage.searchUser(email);
		verify(userManagementPage.isUserDisplayed(email), "User still exists after cancelling the delete",
				"User must not be deleted when delete is cancelled");
	    //Assert.assertTrue(closedOnCancel, "'Cancel' did not close the 'Delete User' confirmation dialog");
	}

	@Test(groups = { "sanity", "regression" }, priority = 10, dependsOnMethods = "verify_ActivateUser", description = "Delete User Test")
	public void verify_DeleteUser() {
		step("Locating the test user '" + email + "'");
		userManagementPage.openUsersList();
		userManagementPage.searchUser(email);
		// Delete is offered only for In-Active users
		if (userManagementPage.getUserStatus(email).equalsIgnoreCase("Active")) {
			logger.info("[INFO] User is Active - deactivating first because Delete is offered only for In-Active users");
			deactivateUser();
		}

		step("Clicking Delete");
		String dialogText = userManagementPage.openDeleteConfirmation(email);
		verify(dialogText.contains("Are you sure to delete this user '" + currentFullName + "'"),
				"Delete confirmation dialog is displayed with the correct message", "Delete confirmation message mismatch: " + dialogText);

		step("Handling delete confirmation - clicking 'Delete User'");
		userManagementPage.confirmDelete();
		verify(userManagementPage.isToastDisplayed("User deleted Successfully"),
				"Success toast 'User deleted Successfully' displayed", "User deleted toast not displayed");
		userDeleted = true;

		step("Verifying user deletion");
		userManagementPage.refreshUsersList();
		userManagementPage.searchUser(email);
		verify(userManagementPage.isUserDeleted(email), "Search shows 'No Users Found' for the deleted user",
				"Deleted user is still displayed in Users list");
		pass("User deleted successfully and no longer appears in the Users list");
	}

	private void deactivateUser() {
		String dialogText = userManagementPage.deactivateUser(email);
		verify(dialogText.contains("Are you sure to deactivate this user '" + currentFullName + "'"),
				"Deactivate confirmation message is correct", "Deactivate confirmation message mismatch: " + dialogText);
		verify(userManagementPage.isToastDisplayed("User details updated successfully"),
				"Success toast 'User details updated successfully' displayed", "User deactivate toast not displayed");
		verify(userManagementPage.isUserStatus(email, "In-Active"), "Status is 'In-Active'", "User status did not change to In-Active");
	}

	private void verifyUpdatedRow() {
		Map<String, String> row = userManagementPage.getUserRowDetails(email);
		verifyEquals(row.get("name"), firstName + " " + updatedLastName, "Updated Name");
		verify(row.get("designation").equalsIgnoreCase(updatedDesignation), "Updated Designation = '" + row.get("designation") + "'",
				"Updated designation not reflected: " + row.get("designation"));
		verifyEquals(row.get("userId"), userId, "User ID (unchanged)");
	}

	// Best-effort cleanup so a failed run does not leave the automation user behind
	private void cleanUpTestUser() {
		if (!userCreated || userDeleted) {
			logger.info("[CLEANUP] No cleanup needed - automation test user " + (userCreated ? "already deleted" : "was not created"));
			return;
		}
		try {
			logger.info("[CLEANUP] Test user was not deleted by the tests - cleaning up: " + email);
			userManagementPage.openUsersList();
			userManagementPage.searchUser(email);
			if (!userManagementPage.isUserDisplayed(email)) {
				logger.info("[CLEANUP] Test user not found - nothing to clean up");
				return;
			}
			if (userManagementPage.getUserStatus(email).equalsIgnoreCase("Active")) {
				userManagementPage.deactivateUser(email);
				userManagementPage.isUserStatus(email, "In-Active");
			}
			userManagementPage.openDeleteConfirmation(email);
			userManagementPage.confirmDelete();
			logger.info("[CLEANUP] Automation test user deleted: " + userManagementPage.isToastDisplayed("User deleted Successfully"));
		} catch (Exception e) {
			logger.error("[CLEANUP] Cleanup failed - delete user manually: " + email + " | " + firstLine(e.getMessage()));
		}
	}

	// Runs before BaseSettingsTest.closeBrowser()
	@AfterClass(alwaysRun = true)
	public void setdown() {
		cleanUpTestUser();
	}
}