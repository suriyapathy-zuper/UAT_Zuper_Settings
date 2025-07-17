package PageObject;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;
import net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.ForAbstractMethod;

public class JobCreationPage extends Baseclass{
	

	  private  WebDriver driver;

	    // Constructor initializes WebElements
//	    public JobCreationPage(WebDriver driver) {
//	        this.driver = driver;
//	        PageFactory.initElements(driver, this);
//	    }
//	
	    public JobCreationPage() {
		    //	System.out.println("Driver received: " + driver);
		    	this.driver = Baseclass.getDriver();
		        PageFactory.initElements(driver, this);
		    }

	    // Page elements
	    
	    //Job Title input field
	    @FindBy(xpath = "//input[@id='title']")
	    private WebElement element_JobTitle; 
	    
	    @FindBy(xpath = "//div[(text() = 'Choose a Job Category' or . = 'Choose a Job Category')]")
	    private WebElement element_JobCategory;
	    
	    //Category list dropdown values
	    @FindBy (xpath = "//div[@id='mat-select-6-panel']//mat-option")
	    private List<WebElement> element_MultiSelectcategory;
	    
	 
	    @FindBy(xpath = "//a[(text() = 'Add Customer' or . = 'Add Customer')]")
	    private WebElement button_AddCustomer;
	    
	    @FindBy(xpath = "//div[@id='existing_data_container']/div/mat-form-field//input")
	    private WebElement text_SearchCustomerName;
	    
	    @FindBy(xpath = "//input[@id='customer_data_select_0']")
	    private WebElement select_Customer;
	    
	    @FindBy(xpath = "//button[(text() = ' Choose Customer ' or . = ' Choose Customer ')]")
	    private WebElement choose_Customer;
	 
	    @FindBy(xpath = "//span[text() ='Assign Users']")
	    private WebElement button_AddFE;
	    
	    @FindBy(xpath = "//a[text()='Users ']")
	    private WebElement button_SelectUser;
	 
	    @FindBy(xpath = "//input[@type='search']")
	    private WebElement input_SearchFE;
	    
	    @FindBy(xpath = "//zuper-assign-employee/div/div[2]/div/div[1]/div/div[2]/div/div[2]")
	    private WebElement select_FE;
	    
	    @FindBy(xpath = "//zuper-assign-employee/div/div[3]/div/div/button[2]")
	    private WebElement save_FE;
	    
	    @FindBy(xpath = "//input[@name='dueDate']")
	    private WebElement set_Duedaue;
	    
	    @FindBy(xpath = "//button[@tabindex='0']//following::td[1]")
	    private WebElement set_Duedaue_CurrentDate;
	     
	    
	    @FindBy(xpath = "//input[@id='jobFormStartDate']")
	    private WebElement set_JobStartDate;
	    
	    @FindBy(xpath = "//button[@tabindex='0']")
	    private WebElement set_CurrentDateToJobStartDate;
	    
	    
	    @FindBy(xpath = "//span[text()='OK']")
	    private WebElement select_StartDateOkButton;
	    
	    @FindBy(xpath = "//span[text()='Create Job']//parent::a")
	    private WebElement button_CreatenewJob;
	    
	    @FindBy(xpath = "//button[text()=' Create ']")
	    private WebElement button_CreatenewJobConfirmation;
	    
	    
	    @FindBy(xpath = "//zuper-assign-employee/div/div[2]/div/div[1]/div/div[2]/div")
	    private List<WebElement> User_multiSelect;
	    
	    @FindBy(xpath = "//app-custom-field-form//label[normalize-space(text())='Permit Needed?']//following::input[1]")
	    private WebElement customfield_Permit_Needed;
	    
	    @FindBy(xpath = " //span[text()=' Customer ']//following::p[1]")
	    private WebElement text_CustomerName;
	   
	    

	    
	    // Page actions
	    
	    /**
	     * ✏️ Set Job Title
	     * @param jobTitle - Title for the new job
	     */
	    public void set_JobTitle(String jobTitle) {
	        element_JobTitle.clear();
	        element_JobTitle.sendKeys(jobTitle);
	    }

	    /**
	     * 📂 Set Job Category
	     * @param jobCategory_Name - Category name to select
	     */
	    public void set_JobCategory(String jobCategory_Name) {
	        try {
	            WebElement dropdown = driver.findElement(By.xpath("//div[(text() = 'Choose a Job Category' or . = 'Choose a Job Category')]"));
	            Non_WebDriver_Util.waitForBeClickable(driver, dropdown, 5);
	            dropdown.click();
	            Non_WebDriver_Util.waitThread(1);

	            List<WebElement> options = driver.findElements(By.xpath("//div[contains(@id,'mat-select')]//mat-option"));
	            Non_WebDriver_Util.visibilityOfAllElements(driver, options, 5);
	            Non_WebDriver_Util.selectMatOptionByText(driver, options, jobCategory_Name);
	       

	            if (jobCategory_Name.trim().equalsIgnoreCase("Plumbing Install")
	                || jobCategory_Name.trim().equalsIgnoreCase("Plumbing Excavation")
	                || jobCategory_Name.trim().equalsIgnoreCase("Electrical Install")) {
	                
	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.waitForBeClickable(driver, customfield_Permit_Needed, 5);
	                customfield_Permit_Needed.click();
	            }

	        } catch (Exception e) {
	            logger.error("❌ Failed to select Job Category: {}", jobCategory_Name, e);
	            throw e;
	        }
	    }

	    

	    /**
	     * 👤 Set Customer
	     * @param customer_Name - Name of the customer to associate with the job
	     */
	    public void set_Customer(String customer_Name) {
	        try {
	            // Wait for the "Add Customer" button to be clickable
	            Non_WebDriver_Util.waitForBeClickable(driver, button_AddCustomer, 5);
	            Non_WebDriver_Util.jsScrollAndActionClick(driver, button_AddCustomer);
	            logger.info("✅ Clicked on 'Add Customer' button.");

	            // Search and select customer
	            text_SearchCustomerName.clear();
	            text_SearchCustomerName.sendKeys(customer_Name);
	            Non_WebDriver_Util.pressEnter(driver);
	            Non_WebDriver_Util.waitThread(1);

	            select_Customer.click();
	            choose_Customer.click();
	            Non_WebDriver_Util.waitThread(1);

	            // Assertion: Verify if selected customer is visible in the field after selection
	            String selectedCustomer = text_CustomerName.getText().trim().toLowerCase();
	            Assert.assertEquals(selectedCustomer, customer_Name.toLowerCase(), "❌ FAIL: Customer name mismatch after selection!");

	        } catch (Exception e) {
	            logger.error("❌ Failed to set customer: {}", customer_Name, e);
	            throw e;
	        }
	    }


	    /**
	     * 👷‍♂️ Set Field Executive (FE)
	     * @param user_Name - Name of the FE to assign
	     */
	    public void set_FE(String user_Name) {
	        button_AddFE.click();
	        Non_WebDriver_Util.waitForBeClickable(driver, button_SelectUser, 5);
	        button_SelectUser.click();
	        input_SearchFE.clear();
	        input_SearchFE.sendKeys(user_Name);
	        Non_WebDriver_Util.pressEnter(driver);
	        Non_WebDriver_Util.waitThread(1);
	        Non_WebDriver_Util.clickChildElementByHeaderText(driver, User_multiSelect, user_Name);
	        Non_WebDriver_Util.waitThread(1);
	        save_FE.click();
	    }

	    /**
	     * 📅 Set Job Due Date (selects current date)
	     */
	    public void set_JobDuedate() {
	        set_Duedaue.click();
	        Non_WebDriver_Util.waitForBeClickable(driver, set_Duedaue_CurrentDate, 2);
	        set_Duedaue_CurrentDate.click();
	    }

	    /**
	     * 📆 Set Job Start Date (selects current date)
	     */
	    public void set_JobStartdate() {
	        try {
	            // Click on Job Start Date field
	            Non_WebDriver_Util.waitForBeClickable(driver, set_JobStartDate, 5);
	            Non_WebDriver_Util.jsClick(driver, set_JobStartDate);

	            // Click on current date in calendar
	            Non_WebDriver_Util.waitForBeClickable(driver, set_CurrentDateToJobStartDate, 5);
	            Non_WebDriver_Util.jsClick(driver, set_CurrentDateToJobStartDate);
	      
	            // Click OK to confirm
	            Non_WebDriver_Util.waitForVisible(driver, select_StartDateOkButton, 3);
	            Non_WebDriver_Util.jsClick(driver, select_StartDateOkButton);     

	            // OPTIONAL: Add assertion if selected date appears in a text field
	            // String selectedDate = yourDateField.getAttribute("value");
	            // String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	            // Assert.assertEquals(selectedDate, today, "❌ Start date mismatch.");
	            // logger.info("✅ Assertion passed: Job start date is correctly set to {}", today);

	        } catch (Exception e) {
	            logger.error("❌ Failed to set job start date", e);
	            throw e;
	        }
	    }


	    /**
	     * 🚀 Click Create Action buttons to confirm job creation
	     */
	    public void create_Action() {
	        try {
	            Non_WebDriver_Util.waitThread(1);

	            // Click "Create New Job" button
	            Non_WebDriver_Util.waitForBeClickable(driver, button_CreatenewJob, 5);
	            button_CreatenewJob.click();

	            // Click confirmation button
	            Non_WebDriver_Util.waitForBeClickable(driver, button_CreatenewJobConfirmation, 5);
	            button_CreatenewJobConfirmation.click();

	            // Wait and refresh
	            Non_WebDriver_Util.waitThread(10);
	            Non_WebDriver_Util.refreshPage(driver);

	            // Optional: Add an assertion here if there's a confirmation message or element you can verify.
	            // WebElement successBanner = driver.findElement(By.id("success_message"));
	            // Assert.assertTrue(successBanner.isDisplayed(), "❌ Job creation confirmation not displayed.");
	            // logger.info("✅ Job creation verified via confirmation message.");

	        } catch (Exception e) {
	            logger.error("❌ Failed during job creation flow", e);
	            throw e;
	        }
	    }

}

