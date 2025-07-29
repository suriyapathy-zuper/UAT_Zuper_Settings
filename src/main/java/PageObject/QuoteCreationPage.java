package PageObject;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

public class QuoteCreationPage extends Baseclass {
		private WebDriver driver;

		public QuoteCreationPage() {
			this.driver = Baseclass.getDriver();
			PageFactory.initElements(driver, this);
		}

		// Page elements

		// Sava as Draft button
		@FindBy(xpath = "//breadcrumb//following::em//following::span[normalize-space(text())='Save as Draft']")
		private WebElement button_SaveAsDraft;

		// Sava as Draft button on confirmation popup
		@FindBy(xpath = "//*[text()='Are you sure you want to save the quote as draft?']//following::div/button[2]")
		private WebElement button_SaveAsDraftOnConfirmationPopup;
		
		//label[contains(normalize-space(text()),'If the Quote is Accepted')]//following::div[1]
		@FindBy(xpath = "//label[contains(normalize-space(text()),'If the Quote is Accepted')]//following::div[1]")
		private WebElement multiSelect_QuoteAccepted;
		
		
		@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
		private List<WebElement> element_MultiSelect_QuoteAccepted;
		
		// Page actions
		public void Quote_SaveAsDraft_CreatedNew() {
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.jsScrollAndActionClick(driver, multiSelect_QuoteAccepted);
			 
		        Non_WebDriver_Util.waitThread(1); 
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_QuoteAccepted, "Repair on Site");
		        logger.info("✅ Selected staging location: " + "Repair on Site");
			
		        Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, button_SaveAsDraft, 5);
			button_SaveAsDraft.click();
			Non_WebDriver_Util.waitForVisible(driver, button_SaveAsDraftOnConfirmationPopup, 5);
			button_SaveAsDraftOnConfirmationPopup.click();
		
		}
		
		

	}

