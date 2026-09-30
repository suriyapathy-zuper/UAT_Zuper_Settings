package PageObject;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

/**
 * Settings module - reached from the Profile (avatar) menu in the top header.
 */
public class SettingsPage extends Baseclass {

	private WebDriver driver;

	public SettingsPage() {
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	// Page elements
	@FindBy(xpath = "//button[@aria-label='Profile']")
	private WebElement profileMenuButton;

	@FindBy(xpath = "//a[@role='menuitem' and normalize-space()='Settings']")
	private WebElement settingsMenuItem;

	// 'Recommended Settings' is replaced by 'Recently Accessed' once the browser session has opened settings pages,
	// so the Settings home is also recognised by its permanent page subtitle
	@FindBy(xpath = "//*[normalize-space(text())='Recommended Settings' or normalize-space(text())='Your home for settings to manage your workspace and more.']")
	private WebElement recommendedSettingsHeader;

	@FindBy(xpath = "//li[.//h3[normalize-space()='User Management']]")
	private WebElement userManagementCard;

	// Settings side menu: Jobs group -> Job Category Hub / Job Card Templates
	@FindBy(xpath = "//a[contains(@class,'group') and normalize-space()='Jobs']")
	private WebElement jobsMenuGroup;

	private static final String JOBS_MENU_ITEM = "//a[@href='%s']";

	// Page actions
	public void navigateToSettings() {
		logAction("Clicking Profile (avatar) menu in the header");
		Non_WebDriver_Util.waitForBeClickable(driver, profileMenuButton, 20);
		profileMenuButton.click();
		logAction("Clicking 'Settings' in the Profile menu");
		Non_WebDriver_Util.waitForBeClickable(driver, settingsMenuItem, 10);
		settingsMenuItem.click();
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains("/settings_new/home"));
		Non_WebDriver_Util.waitForVisible(driver, recommendedSettingsHeader, 20);
		logAction("Settings home loaded: " + driver.getCurrentUrl());
	}

	public boolean isSettingsHomeDisplayed() {
		return driver.getCurrentUrl().contains("/settings_new/home") && recommendedSettingsHeader.isDisplayed();
	}

	public void openUserManagement() {
		logAction("Clicking 'User Management' card under Recommended Settings");
		Non_WebDriver_Util.waitForBeClickable(driver, userManagementCard, 20);
		userManagementCard.click();
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains("/users-and-teams/users"));
		logAction("User Management opened: " + driver.getCurrentUrl());
	}

	public void openJobCategory() {
		openJobsMenuItem("Job Category Hub", "/settings_new/job/category");
	}

	public void openJobCardTemplates() {
		openJobsMenuItem("Job Card Templates", "/settings_new/job/job-card-templates");
	}

	public void openInspectionForms() {
		openJobsMenuItem("Inspection Forms", "/settings_new/job/inspection-form");
	}

	public void openJobNotifications() {
		openJobsMenuItem("Job Notifications", "/settings_new/job/notifications");
	}

	public void openGallerySettings() {
		openJobsMenuItem("Gallery Settings", "/settings_new/job/gallery");
	}

	// Opens an item under the 'Jobs' group of the Settings side menu (expands the group when collapsed)
	private void openJobsMenuItem(String menuName, String path) {
		By menuItemLocator = By.xpath(String.format(JOBS_MENU_ITEM, path));
		WebElement menuItem = Non_WebDriver_Util.findIfVisible(driver, menuItemLocator, 2);
		if (menuItem == null) {
			logAction("Expanding 'Jobs' group in the Settings side menu");
			Non_WebDriver_Util.waitForBeClickable(driver, jobsMenuGroup, 20);
			jobsMenuGroup.click();
			menuItem = Non_WebDriver_Util.findIfVisible(driver, menuItemLocator, 10);
			if (menuItem == null) {
				throw new IllegalStateException("❌ '" + menuName + "' menu item not displayed under Jobs (locator: " + menuItemLocator + ")");
			}
		}
		logAction("Clicking '" + menuName + "' in the Settings side menu");
		menuItem.click();
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains(path));
		logAction(menuName + " opened: " + driver.getCurrentUrl());
	}
}
