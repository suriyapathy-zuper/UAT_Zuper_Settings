package PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

public class JobListingPage extends Baseclass{
	

	  private WebDriver driver;

	    // Constructor initializes WebElements
	    public JobListingPage() {
	        this.driver = Baseclass.getDriver();
	        PageFactory.initElements(driver, this);
	    }

	    // Page elements
	    @FindBy(xpath = "//span[(text()='New Job')]//parent::a")
	    private WebElement element_Jobcreationbutton;
	    
	 
	    // Page actions
	    public void naviagtetoJobCreationPage() {
	    	Non_WebDriver_Util.waitForVisible(driver, element_Jobcreationbutton, 5);
	    	Non_WebDriver_Util.waitForBeClickable(driver, element_Jobcreationbutton, 5);
	    	element_Jobcreationbutton.click();
	    }

}
