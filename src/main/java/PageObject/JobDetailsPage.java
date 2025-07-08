package PageObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import BaseTest.Baseclass;
import TestUtility.Non_WebDriver_Util;

public class JobDetailsPage extends Baseclass{
	

	  private  WebDriver driver;
	  private String waitForReason;
	  private String text_TBBAccount_Value;
	  private String updated_by;
	  private String JobTBDReason;
	  private String permit_Needed;
	  private String copy_JobTBDReason;
	  private String copy_permit_Needed;

	    // Constructor initializes WebElements
//	    public JobDetailsPage(WebDriver driver) {
//	        this.driver = driver;
//	        PageFactory.initElements(driver, this);
//	    }
	    
	    public JobDetailsPage() {
		    //	System.out.println("Driver received: " + driver);
		    	this.driver = Baseclass.getDriver();
		        PageFactory.initElements(driver, this);
		    }

	    // Page elements
	    
	    //Job Title input field
	    @FindBy(xpath = "//a[normalize-space()='Status History']")
	    private WebElement button_StatusHistory; 
	    
	    @FindBy(xpath = "//ng-select[@id='job_status']//div[@class='ng-select-container']")
	    private WebElement update_JobStatus; 
	    
	    @FindBy(xpath = "//div[@role='listbox']/div/div[@role='option']/span")
	    private List<WebElement> option_JobStatus; 

	    @FindBy(xpath = "//div[@class='ng-star-inserted']//div[contains(@class,'relative container ')]//div[@id='update_job_status']//preceding::p[contains(@class,'text-xl font-medium break-words whitespace-pre-wrap')][1]")
	    private WebElement current_JobStatus; 
	  
	    @FindBy(xpath = "//button[text()=' Update Status ']")
	    private WebElement button_JobStatusUpdate; 
	    
	    @FindBy(xpath = "//label[text()='Yes, Ready to Work']/preceding-sibling::input")
	    private WebElement button_yes; 
	    @FindBy(xpath = "//label[text()='No, I Cannot Start Working']/preceding-sibling::input")
	    private WebElement  button_No_ICannot; 
	    
	    @FindBy(xpath = "//label[text()='Waiting On Customer']/preceding-sibling::input")
	    private WebElement checklist_Value3; 
	    
	    @FindBy(xpath = "//label[text()='Waiting On Customer']")
	    private WebElement checklist_Value33; 
	    
	    @FindBy(xpath = "//label[text()='TBB Account']")
	    private WebElement text_TBBAccount; 
	    
	    @FindBy(xpath = "//button[text()=' Update ']")
	    private WebElement buttonUpdateJobchecklist; 
	    
	    
	    @FindBy(xpath = "//h3[text()='Other Details']//following::div//div//dt[text()='Waiting Reason']//following-sibling::dd/span/span/span")
	    private WebElement field_Waiting_Reason; 
	    
	    
	    @FindBy(xpath = "//label[text()='Are you wearing booties?']/following::input[1]")
	    private WebElement button_AreYouWearingbooties; 
	    
	    @FindBy(xpath = "//label[text()='Did you review dispatch notes and service fee instructions?']/following::input[1]")
	    private WebElement button_review_dispatch_notes; 
	    
	    @FindBy(xpath = "//label[text()='Owner / Authorizer on Site?']/following::input[2]")
	    private WebElement button_OwnerOnSite; 
	    
	    @FindBy(xpath = "//label[text()='Owner/Authorizer NOT on site.']//following::label[text()='TBB Account']/preceding-sibling::input")
	    private WebElement button_TBBAccount; 
	    
	    @FindBy(xpath = "//label[text()='Result of Assessment']//following::label[text()='Job Sold Future Appointment']//preceding::input[@value='Job Sold Future Appointment']")
	    private WebElement button_Job_Sold_Future_Appointment; 
	    
	    @FindBy(xpath = "//label[text()='Result of Assessment']//following::label[text()='Estimate Sent / Estimate Needed']//preceding::input[@value='Estimate Sent / Estimate Needed']")
	    private WebElement button_EstimateSent_EstimateNeeded; 
	    
	    @FindBy(xpath = "//label[text()='Was Customer Proposal Accepted?']//following::input[1]")
	    private WebElement button_Was_Customer_Proposal_Accepted; 

	    @FindBy(xpath = "//label[text()='Was estimate sent to customer?']//following::input[2]")
	    private WebElement button_Was_Estimate_Sent_To_Customer; 
	    
	    @FindBy(xpath = "//label[text()='Pending Estimate Reasons']//following::label[text()='Unable to Properly Assess Scope Requires a Return Visit']//preceding::input[@value='Unable to Properly Assess Scope Requires a Return Visit']")
	    private WebElement button_UnableToProperlyAccess; 
	    
	    @FindBy(xpath = "//label[text()='Has Appointment been set with Dispatch?']//following::input[2]")
	    private WebElement button_HasAppointmentbeensetwithDispatch; 
	    
	    @FindBy(xpath = "//label[text()='Staging Location']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	    private WebElement multiSelect_StagingLocation;
	    
	    @FindBy (xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	    private List<WebElement> element_MultiSelect_SatgingLocation;
	    
	    @FindBy(xpath = "//label[text()='Future Appointment Type']//following::div[(text() = 'Plumbing Install' or . = 'Plumbing Install')]//input")
	    private WebElement button_PlumbingInstall;
	    
	    @FindBy(xpath = "//label[text()='Future Appointment Type']//following::div[(text() = 'Plumbing Excavation' or . = 'Plumbing Excavation')]//input")
	    private WebElement button_PlumbingExcavation;
	    
	    @FindBy(xpath = "//label[text()='Future Appointment Material Needs']//following::div[(text() = 'Material Ordering and Staging through Parts Department is Required' or . ='Material Ordering and Staging through Parts Department is Required')]//input")
	    private WebElement button_MaterialOrdering;
	    
	    @FindBy(xpath = "//label[text()='Future Appointment Material Needs']//following::div[(text() = 'Material is Truck Stock' or . ='Material is Truck Stock')]//input")
	    private WebElement button_MaterialisTrackStock;
	    
	    @FindBy(xpath = "//label[text()='Future Job Length']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	    private WebElement multiSelect_FutureJObLength;
	    
	    @FindBy(xpath = "//label[text()='If No Appointment has been set, why?']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	    private WebElement multiSelect_IfNoAppointmenthasbeenset;
	    
	    @FindBy (xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	    private List<WebElement> element_IfNoAppointmenthasbeenset;
	    
	    @FindBy(xpath = "//label[text()='If No Appointment has been set, why?']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	    private WebElement multiSelect_appointmentType;
	    
	    @FindBy (xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	    private List<WebElement> element_appointmentType;
	    
	    @FindBy(xpath = "//label[text()='Customers Preferred Follow Up Date']//following::div[1]")
	    private WebElement select_CustomersPreferredFollowUpDate;
	    
	    @FindBy(xpath = "//span[contains(@class,'mat-calendar-body-today')]")
	    private WebElement select_CurrentDateToFollowUpDate;
	    
	    @FindBy (xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	    private List<WebElement> element_MultiSelect_FutureJObLength;
	    
	    @FindBy(xpath = "//label[text()='How Many Crew Members?']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	    private WebElement multiSelect_HowMany_Crew_Members;
	    
	    @FindBy (xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	    private List<WebElement> element_HowMany_Crew_Members;
	    
	    @FindBy(xpath = "//label[text()='Future Install Job Work Type']//following::label[(text() = 'Stack Work')]//preceding::input[1]")
	    private WebElement button_FutureInstallJobWorkType;
	    
	    
	    @FindBy(xpath = "//label[text()='Basic Description of Work']/following::input[1]")
	    private WebElement input_BasicDescriptionofwork;
	    
	    @FindBy(xpath = "//textarea[contains(@id,'Notes to Account Manager')]")
	    private WebElement input_NotestoAccountManager;
	    
	    @FindBy(xpath = "//label[text()='Have you Scheduled with Dispatch?']//following::div[1]")
	    private WebElement multiSelect_HaveYouScheduled;
	    
	    @FindBy (xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	    private List<WebElement> element_HaveYouScheduled;
	    	
//	    String dynamicXPath = String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]//preceding::input[1]", JobTDBReason);
//	    WebElement button_JobTBDReason = driver.findElement(By.xpath(dynamicXPath));

	  
	    @FindBy(xpath = "//label[text()='Permit Needed?']//following::div[1]")
	    private WebElement multiSelect_PremitNeeded;
	    
	    
//	    String xpath = String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permit_Needed);
//	    WebElement element_PremitNeeded = driver.findElement(By.xpath(dynamicXPath));
	  
	   
	    @FindBy(xpath = "//label[text()='Plumbing Job Sold Checklist to Be Completed.']//following::label[(text() = 'Yes')]//preceding::input[1]")
	    private WebElement button_Plumbing_JobSoldChecklist_to_Be_Completed;
	    
	    @FindBy(xpath = "//span[text()=' Child Jobs Associated']//following-sibling::span")
	    private WebElement text_ChildJobs_Associated;
	    
	    @FindBy(xpath = "//span[text()=' Child Jobs Associated']//following-sibling::span//following::span[2]")
	    private WebElement button_ExtractChildJOb;
	    
	    @FindBy(xpath = "//span[text()=' Child Jobs Associated']//following::div[@role='region'][1]//a")
	    private WebElement button_ChildJObURL;
	    
	    @FindBy(xpath = "//span[text()=' Child Jobs Associated']//following::div[@role='region'][1]//a")
	    private List<WebElement> button_MultopleChildJObURL;
	    
	    @FindBy(xpath = "//div[@id='center-panel']//following::dt[text()='Job Category']//following::span[1]")
	    private WebElement text_JobCategory;
	    
	    @FindBy(xpath = "//h3[text()='Job Description']//following::dl")
	    private WebElement text_JobDescription;
	    
	    @FindBy(xpath = "//p[text()='Assessment Completed']//following::p[2]")
	    private WebElement text_WhoUpdateStatus;
	    
	    @FindBy(xpath = "//custom-fields//dt[text()='Salesman 1 Name']//following::span[1]")
	    private WebElement text_SalesName;
	    
	    // Page actions
	    
	 // =============================
	 // ✅ STATUS VERIFICATION METHODS
	 // =============================

	 public void verify_CurrentStatus(String currentStatusName) { 
		Non_WebDriver_Util.waitForBeClickable(driver, button_StatusHistory, 3);
	 	button_StatusHistory.click(); 
	 	Non_WebDriver_Util.waitForVisible(driver, current_JobStatus, 3);
	 	System.out.println(current_JobStatus.getText());
	 	Assert.assertEquals(currentStatusName, current_JobStatus.getText());
	 }

	 public void verify_ChildJobAssoicated(String job_Count) {	
	 	Non_WebDriver_Util.waitThread(7);
	 	Non_WebDriver_Util.refreshPage(driver);
	 	String text = text_ChildJobs_Associated.getText();
	 	Integer count = Non_WebDriver_Util.extractNumberFromBrackets(text); 
	 	Assert.assertEquals(count, Integer.parseInt(job_Count), "❌ Count mismatch! Expected 1.");
	 }

	 public void verify_CustomField() {	
	 	Assert.assertEquals(waitForReason, field_Waiting_Reason.getText()," \"❌ FAIL: Reason mismatch!\"");
	 }

	 // =============================
	 // ✅ BASIC STATUS UPDATE
	 // =============================

	 public void updateJobStatus(String statusNameToUpdate) {
		Non_WebDriver_Util.clickWithRetry(driver, update_JobStatus, 3, 1000);
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_JobStatusUpdate.click();
	 }

	 // =============================
	 // ✅ STATUS UPDATE + CHECKLIST FLOWS
	 // =============================

	 public void updateJobStatus_WithChecklist(String statusNameToUpdate) {
	 	update_JobStatus.click();
	 	Non_WebDriver_Util.waitThread(2);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);
	 	Non_WebDriver_Util.waitForVisible(driver, button_No_ICannot, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_No_ICannot.click();
	 	Non_WebDriver_Util.waitForVisible(driver, checklist_Value3, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	String waitForReason = checklist_Value33.getText();
	 	this.waitForReason = waitForReason;
	 	checklist_Value3.click();
	 	buttonUpdateJobchecklist.click();
	 	Non_WebDriver_Util.waitThread(5);
	 	Non_WebDriver_Util.refreshPage(driver);
	 }

	 // Arrived → No Answer flow
	 public void updateJobStatus_WithChecklist_Arrived(String statusNameToUpdate) {
	 	update_JobStatus.click();
	 	Non_WebDriver_Util.waitThread(2);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);
	 	Non_WebDriver_Util.waitForVisible(driver, button_yes, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_yes.click();
	 	buttonUpdateJobchecklist.click();
	 }

	 // Starting Assessment flow
	 public void updateJobStatus_WithChecklist_StatringAssessment(String statusNameToUpdate) {
	 	update_JobStatus.click();
	 	Non_WebDriver_Util.waitThread(3);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

	 	Non_WebDriver_Util.waitForVisible(driver, button_AreYouWearingbooties, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_AreYouWearingbooties.click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_review_dispatch_notes, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_review_dispatch_notes.click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_OwnerOnSite, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_OwnerOnSite.click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_TBBAccount, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	String text_TBBAccount_Value = text_TBBAccount.getText();
	 	this.text_TBBAccount_Value = text_TBBAccount_Value;
	 	button_TBBAccount.click();

	 	buttonUpdateJobchecklist.click();
	 }

	 // Assessment Completed flow for PulmbingInstallJob creation
	  public void updateJobStatus_WithChecklist_AssessmentCompleted_PulmbingInstallJob(String statusNameToUpdate,String Bridgeview,String futureJobLength,String basicDescriptionofwork,String JobTBDReason,String permit_Needed) {
	 	update_JobStatus.click();
	 	Non_WebDriver_Util.waitThread(3);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

	 	Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_Job_Sold_Future_Appointment.click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_Was_Customer_Proposal_Accepted.click();

	 	multiSelect_StagingLocation.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, Bridgeview);

	 	Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_PlumbingInstall.click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_MaterialOrdering, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_MaterialOrdering.click();

	 	multiSelect_FutureJObLength.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

	 	multiSelect_HowMany_Crew_Members.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

	 	Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_FutureInstallJobWorkType.click();

	 	input_BasicDescriptionofwork.sendKeys(basicDescriptionofwork);

	 	multiSelect_HaveYouScheduled.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, "No");

	 	
	 	this.JobTBDReason=JobTBDReason;
	 	
	 	
	 	  By button_JobTBDReason = By.xpath(
		            String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", JobTBDReason)
		        );
		    
	 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	this.copy_JobTBDReason=driver.findElement(button_JobTBDReason).getText();
	 	driver.findElement(button_JobTBDReason).click();

	 	multiSelect_PremitNeeded.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	this.permit_Needed=permit_Needed;
	 	 By element_PremitNeeded = By.xpath(
		           String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permit_Needed)
		        );
	 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PremitNeeded), 5);
	 	this.copy_permit_Needed=driver.findElement(element_PremitNeeded).getText();
	 	driver.findElement(element_PremitNeeded).click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

	 	buttonUpdateJobchecklist.click();
	 	String updated_by=text_WhoUpdateStatus.getText();
	 	this.updated_by= updated_by;
	 }
	 
	 
	  // Assessment Completed flow for PulmbingReturnVisitJob creation
	  public void updateJobStatus_WithChecklist_AssessmentCompleted_PulmbingReturnVisitJob(String statusNameToUpdate,String Bridgeview,String futureJobLength,String basicDescriptionofwork,String JobTBDReason,String permit_Needed) {
	 	update_JobStatus.click();
	 	Non_WebDriver_Util.waitThread(2);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

	 	Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_Job_Sold_Future_Appointment.click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_Was_Customer_Proposal_Accepted.click();

	 	multiSelect_StagingLocation.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, Bridgeview);

	 	Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_PlumbingInstall.click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_MaterialisTrackStock, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_MaterialisTrackStock.click();

	 	multiSelect_FutureJObLength.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

	 	multiSelect_HowMany_Crew_Members.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

	 	Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_FutureInstallJobWorkType.click();

	 	input_BasicDescriptionofwork.sendKeys(basicDescriptionofwork);

	 	multiSelect_HaveYouScheduled.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, "No");

	 	
	 	this.JobTBDReason=JobTBDReason;
	 	  By button_JobTBDReason = By.xpath(
		            String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", JobTBDReason)
		        );
		    
	 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	this.copy_JobTBDReason=driver.findElement(button_JobTBDReason).getText();
	 	driver.findElement(button_JobTBDReason).click();

	 	multiSelect_PremitNeeded.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	this.permit_Needed=permit_Needed;
	 	 By element_PremitNeeded = By.xpath(
		           String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permit_Needed)
		        );
	 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PremitNeeded), 5);
	 	this.copy_permit_Needed=driver.findElement(element_PremitNeeded).getText();
	 	driver.findElement(element_PremitNeeded).click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

	 	buttonUpdateJobchecklist.click();
	 	String updated_by=text_WhoUpdateStatus.getText();
	 	this.updated_by= updated_by;
	 }
	 
		 
	  // Assessment Completed flow for PulmbingExcavationJob creation
	  public void updateJobStatus_WithChecklist_AssessmentCompleted_PulmbingExcavationJob(String statusNameToUpdate,String Bridgeview,String futureJobLength,String basicDescriptionofwork,String JobTBDReason,String permit_Needed) {
	 	update_JobStatus.click();
	 	Non_WebDriver_Util.waitThread(2);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

	 	Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_Job_Sold_Future_Appointment.click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_Was_Customer_Proposal_Accepted.click();

	 	multiSelect_StagingLocation.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, Bridgeview);

	 	Non_WebDriver_Util.waitForVisible(driver, button_PlumbingExcavation, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_PlumbingExcavation.click();

	 	multiSelect_FutureJObLength.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

	 	multiSelect_HowMany_Crew_Members.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

	 	Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_FutureInstallJobWorkType.click();

	 	input_BasicDescriptionofwork.sendKeys(basicDescriptionofwork);

	 	multiSelect_HaveYouScheduled.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, "No");

	 	
	 	this.JobTBDReason=JobTBDReason;
	 	  By button_JobTBDReason = By.xpath(
		            String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", JobTBDReason)
		        );
		    
	 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	this.copy_JobTBDReason=driver.findElement(button_JobTBDReason).getText();
	 	driver.findElement(button_JobTBDReason).click();

	 	multiSelect_PremitNeeded.click();
	 	Non_WebDriver_Util.waitThread(1);
	 	this.permit_Needed=permit_Needed;
	 	 By element_PremitNeeded = By.xpath(
		           String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permit_Needed)
		        );
	 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PremitNeeded), 5);
	 	this.copy_permit_Needed=driver.findElement(element_PremitNeeded).getText();
	 	driver.findElement(element_PremitNeeded).click();

	 	Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
	 	Non_WebDriver_Util.waitThread(1);
	 	button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

	 	buttonUpdateJobchecklist.click();
	 	String updated_by=text_WhoUpdateStatus.getText();
	 	this.updated_by= updated_by;
	 }
	 
	// Assessment Completed flow for PulmbingSiteVisitJob creation
		  public void updateJobStatus_WithChecklist_AssessmentCompleted_PulmbingSiteVisitJob(String statusNameToUpdate,String ifNoAppointmenthasbeenset,String appointmentType_PlumbingSiteVisit,String NotetoAccountManagerescriptionofwork) {
		 	update_JobStatus.click();
		 	Non_WebDriver_Util.waitThread(2);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

		 	Non_WebDriver_Util.waitForVisible(driver, button_EstimateSent_EstimateNeeded, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_EstimateSent_EstimateNeeded.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_Was_Estimate_Sent_To_Customer, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_Was_Estimate_Sent_To_Customer.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_UnableToProperlyAccess, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_UnableToProperlyAccess.click();
		 	
		 	Non_WebDriver_Util.waitForVisible(driver, button_HasAppointmentbeensetwithDispatch, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_HasAppointmentbeensetwithDispatch.click();
		 	
		 	multiSelect_IfNoAppointmenthasbeenset.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_IfNoAppointmenthasbeenset, ifNoAppointmenthasbeenset);

		 	multiSelect_appointmentType.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType_PlumbingSiteVisit);
		 	
		 	Non_WebDriver_Util.waitForVisible(driver, select_CustomersPreferredFollowUpDate, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	select_CustomersPreferredFollowUpDate.click();
		 	
		 	Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	select_CurrentDateToFollowUpDate.click();

		 	Non_WebDriver_Util.waitThread(1);
		 	input_NotestoAccountManager.sendKeys(NotetoAccountManagerescriptionofwork);


		 	buttonUpdateJobchecklist.click();
		 	String updated_by=text_WhoUpdateStatus.getText();
		 	this.updated_by= updated_by;
		 }
		 
		 
		// Assessment Completed flow for PulmbingRodding creation
		  public void updateJobStatus_WithChecklist_AssessmentCompleted_PulmbingRoddingJob(String statusNameToUpdate,String ifNoAppointmenthasbeenset,String appointmentType_PlumbingRodding,String NotetoAccountManagerescriptionofwork) {
		 	update_JobStatus.click();
		 	Non_WebDriver_Util.waitThread(2);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

		 	Non_WebDriver_Util.waitForVisible(driver, button_EstimateSent_EstimateNeeded, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_EstimateSent_EstimateNeeded.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_Was_Estimate_Sent_To_Customer, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_Was_Estimate_Sent_To_Customer.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_UnableToProperlyAccess, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_UnableToProperlyAccess.click();
		 	
		 	Non_WebDriver_Util.waitForVisible(driver, button_HasAppointmentbeensetwithDispatch, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_HasAppointmentbeensetwithDispatch.click();
		 	
		 	multiSelect_IfNoAppointmenthasbeenset.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_IfNoAppointmenthasbeenset, ifNoAppointmenthasbeenset);

		 	multiSelect_appointmentType.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType_PlumbingRodding);
		 	
		 	Non_WebDriver_Util.waitForVisible(driver, select_CustomersPreferredFollowUpDate, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	select_CustomersPreferredFollowUpDate.click();
		 	
		 	Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	select_CurrentDateToFollowUpDate.click();

		 	Non_WebDriver_Util.waitThread(1);
		 	input_NotestoAccountManager.sendKeys(NotetoAccountManagerescriptionofwork);


		 	buttonUpdateJobchecklist.click();
		 	String updated_by=text_WhoUpdateStatus.getText();
		 	this.updated_by= updated_by;
		 }
		 
	  
		// Assessment Completed flow for PulmbingServiceCall creation
		  public void updateJobStatus_WithChecklist_AssessmentCompleted_PulmbingServicaCallJob(String statusNameToUpdate,String ifNoAppointmenthasbeenset,String appointmentType_PlumbingRodding,String NotetoAccountManagerescriptionofwork) {
		 	update_JobStatus.click();
		 	Non_WebDriver_Util.waitThread(2);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

		 	Non_WebDriver_Util.waitForVisible(driver, button_EstimateSent_EstimateNeeded, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_EstimateSent_EstimateNeeded.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_Was_Estimate_Sent_To_Customer, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_Was_Estimate_Sent_To_Customer.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_UnableToProperlyAccess, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_UnableToProperlyAccess.click();
		 	
		 	Non_WebDriver_Util.waitForVisible(driver, button_HasAppointmentbeensetwithDispatch, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_HasAppointmentbeensetwithDispatch.click();
		 	
		 	multiSelect_IfNoAppointmenthasbeenset.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_IfNoAppointmenthasbeenset, ifNoAppointmenthasbeenset);

		 	multiSelect_appointmentType.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType_PlumbingRodding);
		 	
		 	Non_WebDriver_Util.waitForVisible(driver, select_CustomersPreferredFollowUpDate, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	select_CustomersPreferredFollowUpDate.click();
		 	
		 	Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	select_CurrentDateToFollowUpDate.click();

		 	Non_WebDriver_Util.waitThread(1);
		 	input_NotestoAccountManager.sendKeys(NotetoAccountManagerescriptionofwork);


		 	buttonUpdateJobchecklist.click();
		 	String updated_by=text_WhoUpdateStatus.getText();
		 	this.updated_by= updated_by;
		 }
		  
	// Assessment Completed flow for Project With Pulmbing Install,Plumbing Roll Over Job creation
		  public void updateJobStatus_WithChecklist_AssessmentCompleted_ProjectWith_PlumbingInstall_RollOverJob(String statusNameToUpdate,String Bridgeview,String futureJobLength,String basicDescriptionofwork,String JobTBDReason,String permit_Needed) {
		 	update_JobStatus.click();
		 	Non_WebDriver_Util.waitThread(3);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

		 	Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_Job_Sold_Future_Appointment.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_Was_Customer_Proposal_Accepted.click();

		 	multiSelect_StagingLocation.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, Bridgeview);

		 	Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_PlumbingInstall.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_MaterialOrdering, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_MaterialOrdering.click();

		 	multiSelect_FutureJObLength.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

		 	multiSelect_HowMany_Crew_Members.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

		 	Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_FutureInstallJobWorkType.click();

		 	input_BasicDescriptionofwork.sendKeys(basicDescriptionofwork);

		 	multiSelect_HaveYouScheduled.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, "No");

		 	
		 	this.JobTBDReason=JobTBDReason;
		 	  By button_JobTBDReason = By.xpath(
			            String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", JobTBDReason)
			        );
			    
		 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	this.copy_JobTBDReason=driver.findElement(button_JobTBDReason).getText();
		 	driver.findElement(button_JobTBDReason).click();

		 	multiSelect_PremitNeeded.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	this.permit_Needed=permit_Needed;
		 	 By element_PremitNeeded = By.xpath(
			           String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permit_Needed)
			        );
		 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PremitNeeded), 5);
		 	this.copy_permit_Needed=driver.findElement(element_PremitNeeded).getText();
		 	driver.findElement(element_PremitNeeded).click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

		 	buttonUpdateJobchecklist.click();
		 	String updated_by=text_WhoUpdateStatus.getText();
		 	this.updated_by= updated_by;
		 }
		 
		  
		// Assessment Completed flow for Project With Pulmbing Return Visit,Plumbing Roll Over Job creation
		  public void updateJobStatus_WithChecklist_AssessmentCompleted_ProjectWith_PlumbingReturnVisit_RollOverJob(String statusNameToUpdate,String Bridgeview,String futureJobLength,String basicDescriptionofwork,String JobTBDReason,String permit_Needed) {
		 	update_JobStatus.click();
		 	Non_WebDriver_Util.waitThread(3);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

		 	Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_Job_Sold_Future_Appointment.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_Was_Customer_Proposal_Accepted.click();

		 	multiSelect_StagingLocation.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, Bridgeview);

		 	Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_PlumbingInstall.click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_MaterialisTrackStock, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_MaterialisTrackStock.click();

		 	multiSelect_FutureJObLength.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

		 	multiSelect_HowMany_Crew_Members.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

		 	Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_FutureInstallJobWorkType.click();

		 	input_BasicDescriptionofwork.sendKeys(basicDescriptionofwork);

		 	multiSelect_HaveYouScheduled.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, "No");

		 	
		 	this.JobTBDReason=JobTBDReason;
		 	  By button_JobTBDReason = By.xpath(
			            String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", JobTBDReason)
			        );
			    
		 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	this.copy_JobTBDReason=driver.findElement(button_JobTBDReason).getText();
		 	driver.findElement(button_JobTBDReason).click();

		 	multiSelect_PremitNeeded.click();
		 	Non_WebDriver_Util.waitThread(1);
		 	this.permit_Needed=permit_Needed;
		 	 By element_PremitNeeded = By.xpath(
			           String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permit_Needed)
			        );
		 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PremitNeeded), 5);
		 	this.copy_permit_Needed=driver.findElement(element_PremitNeeded).getText();
		 	driver.findElement(element_PremitNeeded).click();

		 	Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
		 	Non_WebDriver_Util.waitThread(1);
		 	button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

		 	buttonUpdateJobchecklist.click(); 	
		 	String updated_by=text_WhoUpdateStatus.getText();
		 	this.updated_by= updated_by;
		 }
	  
		  
			// Assessment Completed flow for Project With Pulmbing Excavation ,Plumbing Roll Over Job creation
			  public void updateJobStatus_WithChecklist_AssessmentCompleted_ProjectWith_PlumbingExcavation_RollOverJob(String statusNameToUpdate,String Bridgeview,String futureJobLength,String basicDescriptionofwork,String JobTBDReason,String permit_Needed) {
			 	update_JobStatus.click();
			 	Non_WebDriver_Util.waitThread(3);
			 	Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

			 	Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
			 	Non_WebDriver_Util.waitThread(1);
			 	button_Job_Sold_Future_Appointment.click();

			 	Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
			 	Non_WebDriver_Util.waitThread(1);
			 	button_Was_Customer_Proposal_Accepted.click();

			 	multiSelect_StagingLocation.click();
			 	Non_WebDriver_Util.waitThread(1);
			 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, Bridgeview);

			 	Non_WebDriver_Util.waitForVisible(driver, button_PlumbingExcavation, 5);
			 	Non_WebDriver_Util.waitThread(1);
			    button_PlumbingExcavation.click();

			 	multiSelect_FutureJObLength.click();
			 	Non_WebDriver_Util.waitThread(1);
			 	Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

			 	multiSelect_HowMany_Crew_Members.click();
			 	Non_WebDriver_Util.waitThread(1);
			 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

			 	Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
			 	Non_WebDriver_Util.waitThread(1);
			 	button_FutureInstallJobWorkType.click();

			 	input_BasicDescriptionofwork.sendKeys(basicDescriptionofwork);

			 	multiSelect_HaveYouScheduled.click();
			 	Non_WebDriver_Util.waitThread(1);
			 	Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, "No");

			 	
			 	this.JobTBDReason=JobTBDReason;
			 	  By button_JobTBDReason = By.xpath(
				            String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", JobTBDReason)
				        );
				    
			 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
			 	Non_WebDriver_Util.waitThread(1);
			 	this.copy_JobTBDReason=driver.findElement(button_JobTBDReason).getText();
			 	driver.findElement(button_JobTBDReason).click();

			 	multiSelect_PremitNeeded.click();
			 	Non_WebDriver_Util.waitThread(1);
			 	this.permit_Needed=permit_Needed;
			 	 By element_PremitNeeded = By.xpath(
				           String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permit_Needed)
				        );
			 	Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PremitNeeded), 5);
			 	this.copy_permit_Needed=driver.findElement(element_PremitNeeded).getText();
			 	driver.findElement(element_PremitNeeded).click();

			 	Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
			 	Non_WebDriver_Util.waitThread(1);
			 	button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

			 	buttonUpdateJobchecklist.click();
			 	String updated_by=text_WhoUpdateStatus.getText();
			 	this.updated_by= updated_by;
			 }
			  
			  
	 // =============================
	 // ✅ CHILD JOB HANDLING
	 // =============================

	 public void navigateToChildJOb() {	
	 	button_ExtractChildJOb.click();
	 	Non_WebDriver_Util.waitForVisible(driver, button_ChildJObURL, 5);
	 	button_ChildJObURL.click();
	 }
	 


	 public void navigateToMultipleJobs(String twoJobCategory, String basicDescriptionofwork ) {
		 
		 String[] categoriesArray =twoJobCategory.split(",");
		 List<String> expectedCategories = Arrays.stream(categoriesArray)
		                                         .map(String::trim)
		                                         .collect(Collectors.toList());
		    // Expand child jobs section initially
		    button_ExtractChildJOb.click();
		    Non_WebDriver_Util.waitThread(2);

		    for (int i = 0; i < expectedCategories.size(); i++) {

		        // Re-fetch the list of child job links each time
		        List<WebElement> childJobLinks = driver.findElements(
		                By.xpath("//span[text()=' Child Jobs Associated']//following::div[@role='region'][1]//a")
		        );

		        if (i >= childJobLinks.size()) {
		            throw new RuntimeException("❌ Not enough child job links for validation");
		        }

		        // Click the ith child job link
		        childJobLinks.get(i).click();
		        Non_WebDriver_Util.waitThread(2);

		        // Assert only the category is different, rest is same
		        Assert.assertEquals(text_JobCategory.getText(), expectedCategories.get(i), "❌ Wrong job category at index " + i);
		        Assert.assertEquals(text_JobDescription.getText(), basicDescriptionofwork, "❌ Wrong Job Description");
		        Assert.assertEquals(text_SalesName.getText(), updated_by, "❌ Wrong Sales Name");
		        Assert.assertEquals(copy_JobTBDReason, JobTBDReason, "❌ Wrong JobTBDReason");
		        Assert.assertEquals(copy_permit_Needed, permit_Needed, "❌ Wrong Permit Needed");

		        // Navigate back to the parent job page
		        driver.navigate().back();
		        Non_WebDriver_Util.waitThread(2);

		        // Expand the child job panel again
		        button_ExtractChildJOb.click();
		        Non_WebDriver_Util.waitThread(2);
		    }
		}
	 
	 
	 public void verifyChildJobCategory(String jobCategory) {	 
		 Assert.assertEquals(text_JobCategory.getText(),jobCategory ,"Created wrong Job category");
	 }
	 public void verifyChildJobDescription(String basicDescriptionofwork) {	 
		 Assert.assertEquals(text_JobDescription.getText(),basicDescriptionofwork ,"Created wrong Job Description");
	 }
	 public void verifyCustomfield_SalesName() {	 
		 Assert.assertEquals(text_SalesName.getText(),updated_by ,"Created wrong Job Description");
	 }
	 
	 public void verifyCustomfield_JObTDBReason() {
		    Assert.assertEquals(copy_JobTBDReason, JobTBDReason, "❌ Created wrong Job with JobTBDReason");
		}

	public void verifyCustomfield_PermitNeeded() {
		
		    Assert.assertEquals(copy_permit_Needed, permit_Needed, "❌ Created wrong Job with permitNeeded");
		}
	
	
}

