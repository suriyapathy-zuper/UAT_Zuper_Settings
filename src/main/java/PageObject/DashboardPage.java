package PageObject;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;
import io.netty.handler.timeout.TimeoutException;

public class DashboardPage extends Baseclass{
	

	  private WebDriver driver;

	    // Constructor initializes WebElements
//	    public DashboardPage(WebDriver driver) {
//	        this.driver = driver;
//	        PageFactory.initElements(driver, this);
//	    }
	    
	    public DashboardPage() {
		    //	System.out.println("Driver received: " + driver);
		    	this.driver = Baseclass.getDriver();
		        PageFactory.initElements(driver, this);
		    }

	    // Page elements
	    @FindBy(css = "#pushActionRefuse")
	    private WebElement zuper_AllowPopup;
	    
	    @FindBy(xpath = "//zuper-vertical-navigation-aside-item[@id='job_group']")
	    private WebElement elemnetGroup_JobAndChat;
	    
	    @FindBy(xpath = "//a[contains(@href, '/jobs')]")
	    private WebElement element_JobcreationPage;

	    
	    @FindBy(xpath = " //span[contains(text(),' Get started ')]")
	    private WebElement alert_Popup;
	    
	    @FindBy(xpath = "//button[@type='button' and text()=' Cancel ']")
	    private WebElement dataUpdate_Popup;

	    // "Your timezone has changed" dialog shown after login when system timezone differs from account timezone
	    private final By timezonePopup_CancelButton = By.xpath(
	            "//mat-dialog-container[.//h1[contains(normalize-space(),'Your timezone has changed')]]//button[normalize-space()='Cancel']");

	    // Zuper Connect dialer panel that opens on login and covers the right half of the page
	    private final By zuperConnect_MinimizeIcon = By.xpath(
	            "//span[normalize-space()='Zuper Connect']/following-sibling::span[contains(@class,'zuper-connect-action-icon')]/em[contains(@class,'ti-minus')]");

	    // Dashboard item of the workspace side navigation / header profile menu (logout)
	    private final By dashboardNavigationLink = By.xpath("//a[@href='/dashboard']");
	    private final By profileMenuButton = By.xpath("//button[@aria-label='Profile']");
	    private final By logoutMenuItem = By.xpath("//button[@role='menuitem' and normalize-space()='Log Out']");
	    private final By logoutConfirmButton = By.xpath("//mat-dialog-container//button[normalize-space()='Log Out' or normalize-space()='Logout' or normalize-space()='Yes']");

	    
	    // Page actions
	    public void popup_clear() {
	        
	        try {
	        	Non_WebDriver_Util.waitForVisible(driver, zuper_AllowPopup, 10);
	            Non_WebDriver_Util.waitForBeClickable(driver, zuper_AllowPopup, 10);
	            zuper_AllowPopup.click();
	            
	        } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.NoSuchElementException ignored) {
	            // popup did not appear — no problem, continue
	        }
	      
	        
	        
	    }
	    
	    public void navigatToJobListionPage() {
	    	  try {
	    		    Non_WebDriver_Util.refreshPage(driver);
	    	        elemnetGroup_JobAndChat.click();
	    	        Non_WebDriver_Util.waitThread(1);
	    	        element_JobcreationPage.click();
	    	        Non_WebDriver_Util.waitThread(2);
	    	        
	    	    } catch (Exception e) {
	    	        logger.error("❌ Failed to navigate to Job Listing Page", e);
	    	        throw e; // Optional: rethrow if you want test to fail
	    	    }

	    }

	    public void waitForDashboardToLoad() {
	    	logAction("Waiting for Dashboard to load");
	    	new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.urlContains("/dashboard"));
	    	Non_WebDriver_Util.waitForPageToLoad(driver, 30);
	    	logAction("Dashboard loaded: " + driver.getCurrentUrl());
	    }

	    // Dashboard = /dashboard URL with the 'Dashboard' item of the workspace side navigation displayed
	    public boolean isDashboardDisplayed() {
	    	return driver.getCurrentUrl().contains("/dashboard")
	    			&& Non_WebDriver_Util.findIfVisible(driver, dashboardNavigationLink, 15) != null;
	    }

	    // Profile (avatar) menu -> Log Out; confirms a logout prompt when shown and waits for the login page
	    public void logout() {
	    	logAction("Clicking Profile (avatar) menu to log out");
	    	WebElement profile = Non_WebDriver_Util.findIfVisible(driver, profileMenuButton, 20);
	    	if (profile == null) {
	    		throw new IllegalStateException("❌ Profile menu button not displayed (locator: " + profileMenuButton + ")");
	    	}
	    	profile.click();
	    	WebElement logoutItem = Non_WebDriver_Util.findIfVisible(driver, logoutMenuItem, 10);
	    	if (logoutItem == null) {
	    		throw new IllegalStateException("❌ 'Log Out' menu item not displayed (locator: " + logoutMenuItem + ")");
	    	}
	    	logAction("Clicking 'Log Out'");
	    	logoutItem.click();
	    	WebElement confirm = Non_WebDriver_Util.findIfVisible(driver, logoutConfirmButton, 3);
	    	if (confirm != null) {
	    		logAction("Confirming the logout prompt");
	    		confirm.click();
	    	}
	    	new WebDriverWait(driver, Duration.ofSeconds(30))
	    			.withMessage("Login page after logout - current URL: " + driver.getCurrentUrl())
	    			.until(ExpectedConditions.urlContains("/login"));
	    	logAction("Logged out: " + driver.getCurrentUrl());
	    }

	    // Timezone popup is optional - dismiss it only if it shows up
	    public void dismissTimezonePopup() {
	    	dismissTimezonePopup(10);
	    }

	    // Timezone popup is optional - wait up to 'waitSeconds' for it and dismiss it only if it shows up
	    public void dismissTimezonePopup(int waitSeconds) {
	    	WebElement cancelButton = Non_WebDriver_Util.findIfVisible(driver, timezonePopup_CancelButton, waitSeconds);
	    	if (cancelButton != null) {
	    		cancelButton.click();
	    		Non_WebDriver_Util.waitForInvisibility(driver, timezonePopup_CancelButton, 10);
	    		logAction("'Your timezone has changed' popup dismissed (Cancel)");
	    	} else {
	    		logAction("Timezone popup not shown - nothing to dismiss");
	    	}
	    }

	    // Dialer panel is optional and opens a few seconds after login - wait up to 10s for it
	    public void minimizeZuperConnectDialer() {
	    	minimizeZuperConnectDialer(10);
	    }

	    // Dialer panel is optional - minimize it only if it is open (normal click is not interactable on this icon)
	    public void minimizeZuperConnectDialer(int waitSeconds) {
	    	WebElement minimizeIcon = Non_WebDriver_Util.findIfVisible(driver, zuperConnect_MinimizeIcon, waitSeconds);
	    	if (minimizeIcon != null) {
	    		Non_WebDriver_Util.jsClick(driver, minimizeIcon);
	    		Non_WebDriver_Util.waitForInvisibility(driver, zuperConnect_MinimizeIcon, 10);
	    		logAction("Zuper Connect dialer minimized");
	    	} else {
	    		logAction("Zuper Connect dialer not open - nothing to minimize");
	    	}
	    }
	   	    
	   	    

	  
}
