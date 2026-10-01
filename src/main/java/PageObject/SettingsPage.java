package PageObject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
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

	// Settings side menu: Search field and (filtered) menu entries - group link holds <span title='group'>
	private static final By SIDE_MENU_SEARCH = By.xpath("//ul[@role='list']//input[@placeholder='Search']");
	private static final String SIDE_MENU_GROUP = "//ul[@role='list']//a[contains(@class,'group')][.//span[@title='%s']]";
	private static final String SIDE_MENU_GROUP_ITEM = SIDE_MENU_GROUP + "/following-sibling::ul//a[.//span[@title='%s']]";

	// Settings sub page header (Custom Fields builder etc.): back arrow + page heading
	private static final By PAGE_BACK_BUTTON = By.xpath("//i[contains(@class,'ti-arrow-left')]/parent::button");
	private static final By PAGE_HEADING = By.xpath("//i[contains(@class,'ti-arrow-left')]/following::h2[1]");

	// General Settings rows: <label><span>Label</span></label> <button role='switch' aria-checked> <span>Yes/No</span>
	private static final String SETTING_SWITCH = "//div[label[.//span[starts-with(normalize-space(),'%s')]]]/button[@role='switch']";
	private static final By SAVE_BUTTON = By.xpath("//button[normalize-space()='Save']");

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

	// ==========================================
	// Side menu search
	// ==========================================
	public boolean isSideMenuSearchDisplayed() {
		return Non_WebDriver_Util.findIfVisible(driver, SIDE_MENU_SEARCH, 10) != null;
	}

	// Clears the side menu Search field and types 'text'; returns the value now shown in the field
	public String searchSideMenu(String text) {
		WebElement search = Non_WebDriver_Util.findIfVisible(driver, SIDE_MENU_SEARCH, 10);
		if (search == null) {
			throw new IllegalStateException("❌ Settings side menu Search field not displayed (locator: " + SIDE_MENU_SEARCH + ")");
		}
		logAction("Entering '" + text + "' in the Settings side menu Search field");
		search.click();
		search.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
		search.sendKeys(text);
		return search.getAttribute("value");
	}

	public void clearSideMenuSearch() {
		WebElement search = Non_WebDriver_Util.findIfVisible(driver, SIDE_MENU_SEARCH, 10);
		if (search != null && !search.getAttribute("value").isEmpty()) {
			logAction("Clearing the Settings side menu Search field");
			search.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
		}
	}

	// Waits until 'item' under side menu group 'group' is listed, visible AND clickable; null when it does not show up.
	// The menu re-renders while it filters, so a result element replaced during the check is simply re-checked.
	public WebElement waitForSideMenuResult(String group, String item, int timeoutSeconds) {
		By result = By.xpath(String.format(SIDE_MENU_GROUP_ITEM, group, item));
		logAction("Waiting up to " + timeoutSeconds + "s for side menu result '" + group + " > " + item + "'");
		return Non_WebDriver_Util.waitWithoutImplicitWait(driver, timeoutSeconds, d -> {
			try {
				return ExpectedConditions.elementToBeClickable(result).apply(d);
			} catch (StaleElementReferenceException e) {
				return null;
			}
		});
	}

	// Visible side menu entries (groups and items) - e.g. the current search results.
	// Read in one script call: the menu re-renders while filtering, element-by-element reads could go stale.
	@SuppressWarnings("unchecked")
	public List<String> getSideMenuEntries() {
		Object entries = ((JavascriptExecutor) driver).executeScript(
				"return Array.from(document.querySelectorAll(\"ul[role='list'] li.settings-scroll a\"))"
						+ ".filter(a => a.offsetParent !== null && a.innerText.trim()).map(a => a.innerText.trim());");
		return entries instanceof List ? new ArrayList<>((List<String>) entries) : new ArrayList<>();
	}

	// Clicks side menu result 'group > item' once it is clickable (re-located if the menu re-rendered) and waits for its page URL
	public void openSideMenuResult(String group, String item) {
		String path = null;
		for (int attempt = 1; path == null; attempt++) {
			WebElement result = waitForSideMenuResult(group, item, 10);
			if (result == null) {
				throw new IllegalStateException("❌ Side menu result '" + group + " > " + item + "' not clickable");
			}
			try {
				String href = result.getAttribute("href").replaceFirst("^https?://[^/]+", "");
				logAction("Clicking side menu result '" + group + " > " + item + "'");
				result.click();
				path = href;
			} catch (StaleElementReferenceException e) {
				if (attempt == 3) {
					throw e;
				}
			}
		}
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains(path));
		Non_WebDriver_Util.waitForPageToLoad(driver, 30);
		logAction(item + " opened: " + driver.getCurrentUrl());
	}

	// The side menu item of the current page is highlighted (routerLinkActive -> text-gray-900)
	public boolean isSideMenuItemActive(String group, String item) {
		WebElement link = Non_WebDriver_Util.findIfVisible(driver, By.xpath(String.format(SIDE_MENU_GROUP_ITEM, group, item)), 10);
		return link != null && link.getAttribute("class").contains("text-gray-900");
	}

	public void openProjectGeneralSettings() {
		openSideMenuItem("Projects", "General Settings", "/settings_new/project/configuration");
		Non_WebDriver_Util.waitForPageToLoad(driver, 30);
	}

	// ==========================================
	// Settings sub page header (back arrow + heading)
	// ==========================================
	public String getPageHeading() {
		WebElement heading = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20,
				d -> d.findElements(PAGE_HEADING).stream().filter(h -> h.isDisplayed() && !h.getText().trim().isEmpty()).findFirst().orElse(null));
		return heading == null ? "" : heading.getText().trim();
	}

	public void clickPageBack() {
		WebElement back = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, ExpectedConditions.elementToBeClickable(PAGE_BACK_BUTTON));
		if (back == null) {
			throw new IllegalStateException("❌ Back button not displayed (locator: " + PAGE_BACK_BUTTON + ")");
		}
		logAction("Clicking the Back (arrow) button");
		back.click();
	}

	// ==========================================
	// General Settings switches (Yes/No, Enabled/Disabled) + Save
	// ==========================================
	public boolean isSettingSwitchDisplayed(String label) {
		return Non_WebDriver_Util.findIfVisible(driver, By.xpath(String.format(SETTING_SWITCH, label)), 20) != null;
	}

	public boolean isSettingSwitchOn(String label) {
		return "true".equals(settingSwitch(label).getAttribute("aria-checked"));
	}

	// State text next to the switch, e.g. Yes / No, Enabled / Disabled
	public String getSettingSwitchText(String label) {
		return settingSwitch(label).findElement(By.xpath("following-sibling::span[1]")).getText().trim();
	}

	public void clickSettingSwitch(String label) {
		WebElement toggle = settingSwitch(label);
		boolean before = "true".equals(toggle.getAttribute("aria-checked"));
		logAction("Clicking '" + label + "' switch (currently " + (before ? "on" : "off") + ")");
		Non_WebDriver_Util.waitForBeClickable(driver, toggle, 10);
		toggle.click();
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, d -> before != "true".equals(settingSwitch(label).getAttribute("aria-checked")));
	}

	public boolean isSaveEnabled() {
		WebElement save = Non_WebDriver_Util.findIfVisible(driver, SAVE_BUTTON, 5);
		return save != null && save.isEnabled();
	}

	// Clicks Save and waits until the button is disabled again (changes stored)
	public boolean saveSettings() {
		WebElement save = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 10, ExpectedConditions.elementToBeClickable(SAVE_BUTTON));
		if (save == null) {
			throw new IllegalStateException("❌ Save button not enabled (locator: " + SAVE_BUTTON + ")");
		}
		logAction("Clicking Save");
		save.click();
		boolean saved = Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20, d -> !d.findElement(SAVE_BUTTON).isEnabled()) != null;
		logAction(saved ? "Settings saved (Save button disabled again)" : "Save button still enabled 20s after saving");
		return saved;
	}

	// Reloads the current settings page and waits until 'label' is displayed again
	public void reloadSettingsPage(String label) {
		logAction("Reloading " + driver.getCurrentUrl());
		driver.navigate().refresh();
		Non_WebDriver_Util.waitForPageToLoad(driver, 30);
		if (!isSettingSwitchDisplayed(label)) {
			throw new IllegalStateException("❌ '" + label + "' not displayed after reloading " + driver.getCurrentUrl());
		}
	}

	private WebElement settingSwitch(String label) {
		By locator = By.xpath(String.format(SETTING_SWITCH, label));
		WebElement toggle = Non_WebDriver_Util.findIfVisible(driver, locator, 20);
		if (toggle == null) {
			throw new IllegalStateException("❌ '" + label + "' switch not displayed (locator: " + locator + ")");
		}
		return toggle;
	}

	// Opens 'item' under side menu group 'group' (expands the group when collapsed)
	private void openSideMenuItem(String group, String item, String path) {
		By itemLocator = By.xpath(String.format(SIDE_MENU_GROUP_ITEM, group, item));
		WebElement menuItem = Non_WebDriver_Util.findIfVisible(driver, itemLocator, 2);
		if (menuItem == null) {
			logAction("Expanding '" + group + "' group in the Settings side menu");
			WebElement groupLink = Non_WebDriver_Util.findIfVisible(driver, By.xpath(String.format(SIDE_MENU_GROUP, group)), 20);
			if (groupLink == null) {
				throw new IllegalStateException("❌ '" + group + "' group not displayed in the Settings side menu");
			}
			groupLink.click();
			menuItem = Non_WebDriver_Util.findIfVisible(driver, itemLocator, 10);
			if (menuItem == null) {
				throw new IllegalStateException("❌ '" + item + "' menu item not displayed under " + group + " (locator: " + itemLocator + ")");
			}
		}
		logAction("Clicking '" + group + " > " + item + "' in the Settings side menu");
		menuItem.click();
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains(path));
		logAction(group + " > " + item + " opened: " + driver.getCurrentUrl());
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
