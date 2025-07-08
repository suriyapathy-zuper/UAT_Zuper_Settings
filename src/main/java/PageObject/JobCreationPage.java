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
	        element_JobCategory.click();
	        Non_WebDriver_Util.waitThread(3);
	        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelectcategory, jobCategory_Name);
	    }

	    /**
	     * 👤 Set Customer
	     * @param customer_Name - Name of the customer to associate with the job
	     */
	    public void set_Customer(String customer_Name) {
	        button_AddCustomer.click();
	        text_SearchCustomerName.clear();
	        text_SearchCustomerName.sendKeys(customer_Name);
	        Non_WebDriver_Util.pressEnter(driver);
	        Non_WebDriver_Util.waitThread(2);

	        select_Customer.click();
	        choose_Customer.click();
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
	        Non_WebDriver_Util.waitThread(2);
	        Non_WebDriver_Util.clickChildElementByHeaderText(driver, User_multiSelect, user_Name);
	        Non_WebDriver_Util.waitThread(2);
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
	        Non_WebDriver_Util.waitForBeClickable(driver, set_JobStartDate, 5);
	        Non_WebDriver_Util.jsClick(driver, set_JobStartDate);
	        Non_WebDriver_Util.waitForBeClickable(driver, set_CurrentDateToJobStartDate, 5);
	        Non_WebDriver_Util.jsClick(driver, set_CurrentDateToJobStartDate);
	        Non_WebDriver_Util.waitForVisible(driver, select_StartDateOkButton, 3);
	        Non_WebDriver_Util.jsClick(driver, select_StartDateOkButton);
	    }

	    /**
	     * 🚀 Click Create Action buttons to confirm job creation
	     */
	    public void create_Action() {
	    	Non_WebDriver_Util.waitThread(1);
	        Non_WebDriver_Util.waitForBeClickable(driver, button_CreatenewJob, 5);
	        button_CreatenewJob.click(); 
	        Non_WebDriver_Util.waitForBeClickable(driver, button_CreatenewJobConfirmation, 5);
	        button_CreatenewJobConfirmation.click();
	        Non_WebDriver_Util.waitThread(10);
		 	Non_WebDriver_Util.refreshPage(driver);
	    }
}

