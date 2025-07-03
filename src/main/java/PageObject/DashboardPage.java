package PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

public class DashboardPage extends Baseclass{
	

	  private WebDriver driver;

	    // Constructor initializes WebElements
	    public DashboardPage(WebDriver driver) {
	        this.driver = driver;
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
	 
	    
	    // Page actions
	    public void popup_clear() {
	    	Non_WebDriver_Util.waitForVisible(driver, zuper_AllowPopup, 10);
	        zuper_AllowPopup.click();
	      
	        
	        
	    }
	    
	    public void navigatToJobListionPage() {
	    	elemnetGroup_JobAndChat.click();
	    	element_JobcreationPage.click();
	        Non_WebDriver_Util.waitForVisible(driver, alert_Popup, 5);
	        alert_Popup.click();
	        
	    }
	   	    
	   	    

	  
}
