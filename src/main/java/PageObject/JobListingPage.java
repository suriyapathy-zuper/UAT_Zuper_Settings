package PageObject;

import org.openqa.selenium.By;
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
	    @FindBy(xpath = "//span[normalize-space(text())='New Job']/parent::a")
	    private WebElement element_Jobcreationbutton;
	    
	    private By new_Page = By.xpath("//span[normalize-space(text())='New Job']//parent::a//em");
	    
	 
	    // Page actions
	    public void naviagtetoJobCreationPage() {
	    	Non_WebDriver_Util.waitThread(2);
	    	//Non_WebDriver_Util.waitpresenceOfElementLocated(driver, new_Page, 10);
	    	driver.findElement(By.xpath("//span[normalize-space(text())='New Job']/parent::a/em")).click();
	    	}
	    

}
