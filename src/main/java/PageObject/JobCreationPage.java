package PageObject;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

public class JobCreationPage extends Baseclass{
	

	  private WebDriver driver;

	    // Constructor initializes WebElements
	    public JobCreationPage(WebDriver driver) {
	        this.driver = driver;
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
	    
	    @FindBy(xpath = "//button[@tabindex='0']")
	    private WebElement set_Duedaue_CurrentDate;
	    
	    @FindBy(xpath = "//input[@id='jobFormStartDate']")
	    private WebElement set_JObStartDate;
	    
	    
	    @FindBy(xpath = "//span[text()='OK']")
	    private WebElement select_StartDateOkButton;
	    
	    @FindBy(xpath = "//*[text()='Create Job']")
	    private WebElement button_CreatenewJob;
	    
	    @FindBy(xpath = "//*[text()=' Create ']")
	    private WebElement button_CreatenewJobConfirmation;
	    
	    
	    // Page actions
	    public void set_JobTitle() {
	    	element_JobTitle.clear();
	    	element_JobTitle.sendKeys(prop.getProperty("jobTitle"));
    
	    }
	    public void set_JobCategory(String jobCategory_Name) {
	    	element_JobCategory.click();
	    	Non_WebDriver_Util.selectMatOptionByText( driver, element_MultiSelectcategory, jobCategory_Name);
    
	    }
	    public void set_Customer(String customer_Name) {
	    	button_AddCustomer.click();
	    	text_SearchCustomerName.clear();
	    	text_SearchCustomerName.sendKeys(customer_Name);
	    	Non_WebDriver_Util.pressEnter(driver);
	    	try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	select_Customer.click();
	    	choose_Customer.click();
    
	    }
	    public void set_FE(String user_Name) {
	    	button_AddFE.click();
	    	Non_WebDriver_Util.waitForBeClickable(driver, button_SelectUser, 5);
	    	button_SelectUser.click();
	    	input_SearchFE.clear();
	    	input_SearchFE.sendKeys(user_Name);
	    	Non_WebDriver_Util.pressEnter(driver);
	    	try {
				Thread.sleep(6000);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	select_FE.click();
	    	save_FE.click();
	    }
	    public void set_JobDuedate() {
	    	set_Duedaue.click();
	        Non_WebDriver_Util.waitForVisible(driver, set_Duedaue_CurrentDate, 2);
	        set_Duedaue_CurrentDate.click();
	    }
	    public void set_JobStartdate() {
	    	Non_WebDriver_Util.waitForVisible(driver, set_JObStartDate, 2);
	        set_JObStartDate.click();
	        select_StartDateOkButton.click();
	        
	    }
	    public void create_Action() {
	    	Non_WebDriver_Util.waitForVisible(driver, button_CreatenewJob, 2);
	        button_CreatenewJob.click(); 
	        button_CreatenewJobConfirmation.click();
	    }
}

