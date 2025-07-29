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


public class InvoiceDetailsPage extends Baseclass {
	private WebDriver driver;

	public InvoiceDetailsPage() {
		// System.out.println("Driver received: " + driver);
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	// Page elements
	@FindBy(xpath = "//attachments-list//file-placeholder")
	private List<WebElement> attachmentCountInInvoice;

	// Page actions

	// Checking Single attachment name
	public void checkingSingleInvoiceAttachmentName(String SingleAttachmentName) {
		Non_WebDriver_Util.waitThread(2);
		Non_WebDriver_Util.refreshPage(driver);
		String attachmentName = driver.findElement(By.xpath("//attachments-list//file-placeholder[1]//a")).getText().trim();
		try {
			if (attachmentName.equalsIgnoreCase(SingleAttachmentName)) {
				Assert.assertEquals(attachmentName, SingleAttachmentName, "Attachment name is not matched");
			} 
		} catch (NoSuchElementException e) {
			System.out.println("Attachments NOT Found");
		}	
	}

	// Checking Multiple attachment Names
	public void checkingAttachmentNamesInInvoice(String attachmentName, String attachmentName2) {
		Non_WebDriver_Util.waitThread(2);
		Non_WebDriver_Util.refreshPage(driver);
		int attachmentCount = attachmentCountInInvoice.size();
		try {
			for (int i = 1; i <= attachmentCount; i++) {
				Non_WebDriver_Util.waitThread(1);
				String attachmentNames = driver.findElement(By.xpath("//attachments-list//file-placeholder[" + i + "]//a"))
						.getText().trim();
				List<String> expectedTasks = Arrays.asList(attachmentName, attachmentName2);
				Assert.assertTrue(expectedTasks.contains(attachmentNames), "Unexpected Attachment name.");
			}
		} catch (NoSuchElementException e) {
			System.out.println("Attachments NOT Found");
		}	
	}
}
