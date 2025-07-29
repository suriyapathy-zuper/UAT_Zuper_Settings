package PageObject;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;


public class QuoteDetailsPage extends Baseclass {
	private WebDriver driver;

	public QuoteDetailsPage() {
		
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	
	
	// Page elements
	@FindBy(xpath = "//zuper-stacked-toast-component//following::div[1]//div//a")
	private WebElement link_jobfromQuotePage;
	
	@FindBy(xpath = "//custom-fields//dt[contains(normalize-space(text()),'Originated from Job ID')]//following-sibling::dd//span//span//span")
	private WebElement customfield_OriginatedfromJobID;
	
	@FindBy(xpath = "//custom-fields//dt[contains(normalize-space(text()),'Originated From')]//following-sibling::dd//span//span//a")
	private WebElement customfield_OriginatedfromJobLink;

	// Page actions
	
	
	public void navigateToJobPage() {
	    try {
	        Non_WebDriver_Util.waitForPageToLoad(driver, 10);	        
	        String jobLinkText = link_jobfromQuotePage.getText();
	        String expectedJobUID = Non_WebDriver_Util.extractNumber(jobLinkText);	    
	        Assert.assertEquals(JobDetailsPage.job_ID, expectedJobUID);     
	        link_jobfromQuotePage.click();
	  
	    } catch (Exception e) {
	        logger.error("❌ Exception while navigating to Job page: " + e.getMessage(), e);
	        Assert.fail("Failed to navigate to Job page: " + e.getMessage());
	    }
	}

	


	public void verify_ParentjobIDstoredInQuote() {
	    try {
	        Non_WebDriver_Util.waitForPageToLoad(driver, 10);
	        String jobLinkText = link_jobfromQuotePage.getText();
	        String expectedJobUID = Non_WebDriver_Util.extractNumber(jobLinkText);
	        Assert.assertEquals(JobDetailsPage.job_ID, expectedJobUID);
	        link_jobfromQuotePage.click();

	    } catch (Exception e) {
	        logger.error("❌ Error while verifying Parent Job ID in quote: " + e.getMessage(), e);
	        Assert.fail("Verification failed: Parent Job UID in quote does not match JobDetailsPage.job_ID");
	    }
	}

	
	public  void verify_OriginatedJobUID() {
		
		try {
			Assert.assertEquals(customfield_OriginatedfromJobID.getText().trim(), JobDetailsPage.job_ID.trim(), "❌ Assoicated  wrong orginated Job ");
		} catch (AssertionError e) {
			logger.error("❌ Job category verification failed. Expected: {}, Actual: {}", JobDetailsPage.job_ID.trim(),
					customfield_OriginatedfromJobID.getText().trim(), e);
			throw e; // Optional: re-throw if you want test to fail
		}
	}

	
	public void verify_OriginatedJoblink() {
		try {
			Assert.assertEquals(JobDetailsPage.job_URL, customfield_OriginatedfromJobLink.getAttribute("href"), "❌ Assoicated  wrong orginated Job ");
		} catch (AssertionError e) {
			logger.error("❌ Job category verification failed. Expected: {}, Actual: {}", JobDetailsPage.job_ID.trim(),
					customfield_OriginatedfromJobID.getText().trim(), e);
			throw e; // Optional: re-throw if you want test to fail
		}
	}
	
	public void navigateToJobPagefromQuoteCustomFiledLink() {    
	    try {
	        customfield_OriginatedfromJobLink.click();
	     
	    } catch (Exception e) {
	        logger.error("❌ Exception while navigating to Job page from quote: " + e.getMessage(), e);
	        Assert.fail("Failed to navigate to Job page from quote.");
	    }
	}

}
