package PageObject;

import java.util.NoSuchElementException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
	   	    
	   	    

	  
}
