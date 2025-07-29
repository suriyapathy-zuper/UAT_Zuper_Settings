package PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

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

	    	 final int maxRetries = 3;
	    	    int attempts = 0;
	    	    boolean isClicked = false;

	    	    while (attempts < maxRetries) {
	    	        try {
	    	            Non_WebDriver_Util.waitThread(2);
	    	            Non_WebDriver_Util.waitForPageToLoad(driver, 5);

	    	            By newJobButton = By.xpath("//span[normalize-space(text())='New Job']/parent::a/em");
	    	            WebElement button = driver.findElement(newJobButton);

	    	            if (button.isDisplayed() && button.isEnabled()) {
	    	                button.click();
	    	                isClicked = true;
	    	                break;
	    	            }
	    	        } catch (Exception e) {
	    	            logger.warn("⚠️ Attempt {} to click 'New Job' failed: {}", (attempts + 1), e.getMessage());
	    	        }
	    	        attempts++;
	    	    }

	    	    if (!isClicked) {
	    	        logger.error("❌ Failed to navigate to Job Creation page after {} attempts.", maxRetries);
	    	        Assert.fail("❌ Could not navigate to Job Creation page after retrying.");
	    	    }
}
}
