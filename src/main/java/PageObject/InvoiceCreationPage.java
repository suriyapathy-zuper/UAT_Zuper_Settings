package PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

public class InvoiceCreationPage extends Baseclass {
		private WebDriver driver;

		public InvoiceCreationPage() {
			// System.out.println("Driver received: " + driver);
			this.driver = Baseclass.getDriver();
			PageFactory.initElements(driver, this);
		}

		// Page elements

		// Sava as Draft button
		@FindBy(xpath = "//*[text()= 'Save as Draft']")
		private WebElement button_SaveAsDraft;

		// Sava as Draft button on confirmation popup
		@FindBy(xpath = "//*[text()='Are you sure you want to save the invoice as draft?']//following::div/button[2]")
		private WebElement button_SaveAsDraftOnConfirmationPopup;

		// Page actions
		public void clickingSaveAsDraftButton() {
			button_SaveAsDraft.click();
			Non_WebDriver_Util.waitForBeClickable(driver, button_SaveAsDraftOnConfirmationPopup, 5);
			button_SaveAsDraftOnConfirmationPopup.click();
			Non_WebDriver_Util.waitThread(2);;
		}

	}

