package PageObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class JobDetailsPage extends Baseclass {

	private WebDriver driver;
	private String waitForReason;
	private String text_TBBAccount_Value;
	private String updated_by;
	private String JobTBDReason;
	private String permit_Needed;
	private String copy_JobTBDReason;
	private String copy_permit_Needed;
	private String selectJobStartDate;
	private String arrivalTimeframe;
	private String StagingLocation;
	private String childJobTags;

	public JobDetailsPage() {
		this.driver = Baseclass.getDriver();
		PageFactory.initElements(driver, this);
	}

	// Page elements

	// Job Title input field
	@FindBy(xpath = "//a[normalize-space()='Status History']")
	private WebElement button_StatusHistory;

	@FindBy(xpath = "//ng-select[@id='job_status']")
	private WebElement update_JobStatus;

	@FindBy(xpath = "//div[@role='listbox']/div/div[@role='option']/span")
	private List<WebElement> option_JobStatus;

	@FindBy(xpath = "//div[@role='listbox']/div/div[@role='option']")
	private WebElement option_JobStatusVisiable;

	// div[@class='ng-star-inserted']//div[contains(@class,'relative container
	// ')]//div[@id='update_job_status']//preceding::p[contains(@class,'text-xl
	// font-medium break-words whitespace-pre-wrap')][1]
	@FindBy(xpath = "//div[@id='update_job_status']//preceding::p[contains(@class,'text-xl')][1]")
	private WebElement current_JobStatus;

	@FindBy(xpath = "//button[text()=' Update Status ']")
	private WebElement button_JobStatusUpdate;

	@FindBy(xpath = "//label[text()='Yes, Ready to Work']/preceding-sibling::input")
	private WebElement button_yes;

	@FindBy(xpath = "//label[text()='Consumer Rights Form has been Acknowledged? (This applies to Illinois Customers Only)']//following::div//label[text()='Yes']//preceding-sibling::input")
	private WebElement button_yes_ConsumerRights;

	@FindBy(xpath = "//label[text()='No, I Cannot Start Working']/preceding-sibling::input")
	private WebElement button_No_ICannot;

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

	@FindBy(xpath = "//label[starts-with(text(),'Job Sold')]//preceding::input[contains(@value,'Job Sold')]")
	private WebElement button_Job_Sold_Future_Appointment;
	
	@FindBy(xpath = "//label[starts-with(text(),'Rollover Needed')]//preceding::input[contains(@value,'Rollover Needed')]")
	private WebElement button_RolloverNeeded;

	@FindBy(xpath = "//label[starts-with(text(),'Estimate Sent')]//preceding::input[contains(@value,'Estimate Sent')]")
	private WebElement button_EstimateSent_EstimateNeeded;

	@FindBy(xpath = "//label[text()='Was Customer Proposal Accepted?']//following::input[1]")
	private WebElement button_Was_Customer_Proposal_Accepted;

	@FindBy(xpath = "//label[text()='Was estimate sent to customer?']//following::input[2]")
	private WebElement button_Was_Estimate_Sent_To_Customer;

	@FindBy(xpath = "//label[text()='Pending Estimate Reasons']//following::label[text()='Unable to Properly Assess Scope Requires a Return Visit']//preceding::input[@value='Unable to Properly Assess Scope Requires a Return Visit']")
	private WebElement button_UnableToProperlyAccess;

	@FindBy(xpath = "//label[text()='Has Appointment been set with Dispatch?']//following::input[2]")
	private WebElement button_HasAppointmentbeensetwithDispatch;

	@FindBy(xpath = "//label[text()='Has Appointment been set with Dispatch?']//following::input[1]")
	private WebElement button_HasAppointmentbeensetwithDispatch_Yes;

	@FindBy(xpath = "//label[text()='Staging Location']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_StagingLocation;

	@FindBy(xpath = "//label[text()='Arrival Timeframe of Future Appointment']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_ArrivalTimeframe;
	
	@FindBy(xpath = "//label[text()='Rollover Appointment Time Frame']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_RolloverTimeframe;

	@FindBy(xpath = "//label[text()='Appointment Time Frame']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_ArrivalTimeframe_estimate;

	@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
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
	
	@FindBy(xpath = "//label[text()='Time Needed to Complete Job?']//following::div[1]")
	private WebElement multiSelect_TimeNeededtoCompleteJob;
	
	@FindBy(xpath = "//label[text()='How Many Guys Needed']//following::div[1]")
	private WebElement multiSelect_GuysNeeded;

	@FindBy(xpath = "//label[text()='If No Appointment has been set, why?']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_IfNoAppointmenthasbeenset;

	@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	private List<WebElement> element_IfNoAppointmenthasbeenset;

	@FindBy(xpath = "//label[text()='If No Appointment has been set, why?']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_appointmentType;

	@FindBy(xpath = "//label[text()='Appointment Type']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_appointmentType_estimate;

	@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	private List<WebElement> element_appointmentType;

	@FindBy(xpath = "//label[text()='Customers Preferred Follow Up Date']//following::div[1]")
	private WebElement select_CustomersPreferredFollowUpDate;

	@FindBy(xpath = "//label[text()='Job Start Date']//following::input[1]")
	private WebElement select_JobStartDate;

	@FindBy(xpath = "//label[text()='Rollover Appointment Date']//following::input[1]")
	private WebElement select_Rollover_Appointment_Date;
	
	@FindBy(xpath = "//label[text()='Appointment Date']//following::input[1]")
	private WebElement select_AppointmentDate_JobStartDate;

	@FindBy(xpath = "//span[contains(@class,'mat-calendar-body-today')]")
	private WebElement select_CurrentDateToFollowUpDate;

	@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	private List<WebElement> element_MultiSelect_FutureJObLength;

	@FindBy(xpath = "//label[text()='How Many Crew Members?']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_HowMany_Crew_Members;

	@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	private List<WebElement> element_HowMany_Crew_Members;

	@FindBy(xpath = "//label[text()='Future Install Job Work Type']//following::label[(text() = 'Stack Work')]//preceding::input[1]")
	private WebElement button_FutureInstallJobWorkType;

	@FindBy(xpath = "//label[text()='Future Job Basic Description']//following::label[(text() = 'Stack Work')]//preceding::input[1]")
	private WebElement button_FutureJobBasicDescription;

	@FindBy(xpath = "//label[text()='Do you want to send the On the Way Text to the Customer?']//following::div[1]")
	private WebElement button_OntheWayTexttotheCustomer;

	@FindBy(xpath = "//label[text()='Basic Description of Work']/following::input[1]")
	private WebElement input_BasicDescriptionofwork;
	
	@FindBy(xpath = "//label[text()='Rollover Job Scope of Work Needed']//following::div[1]//textarea")
	private WebElement input_RolloverDescriptionofwork;

	@FindBy(xpath = "//textarea[contains(@id,'Notes to Account Manager')]")
	private WebElement input_NotestoAccountManager;

	@FindBy(xpath = "//label[text()='Have you Scheduled with Dispatch?']//following::div[1]")
	private WebElement multiSelect_HaveYouScheduled;
	
	@FindBy(xpath = "//label[text()='Do you have a date for rollover job?']//following::div[1]")
	private WebElement multiSelect_HaveYouScheduledForRolloverJobs;

	@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	private List<WebElement> element_HaveYouScheduled;

//	    String dynamicXPath = String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]//preceding::input[1]", JobTDBReason);
//	    WebElement button_JobTBDReason = driver.findElement(By.xpath(dynamicXPath));

	@FindBy(xpath = "//label[text()='Permit Needed?']//following::div[1]")
	private WebElement multiSelect_PremitNeeded;

//	    String xpath = String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permit_Needed);
//	    WebElement element_PremitNeeded = driver.findElement(By.xpath(dynamicXPath));

	@FindBy(xpath = "//label[text()='Plumbing Job Sold Checklist to Be Completed.']//following::label[(text() = 'Yes')]//preceding::input[1]")
	private WebElement button_Plumbing_JobSoldChecklist_to_Be_Completed;

	@FindBy(xpath = "//label[text()='Are All Materials Accounted For?']//following::label[(text() = 'Yes')]//preceding::input[1]")
	private WebElement button_AreAllMaterialsAccountedFor;

	
	@FindBy(xpath = "//label[text()='Do you Need to Travel for Parts?']//following::div[1]")
	private WebElement button_DoyouNeedtoTravelforParts;
	
	@FindBy(xpath = "//span[text()=' Child Jobs Associated']//following-sibling::span")
	private WebElement text_ChildJobs_Associated;

	@FindBy(xpath = "//span[text()=' Child Jobs Associated']//following-sibling::span//following::span[2]")
	private WebElement button_ExtractChildJOb;

	@FindBy(xpath = "//span[text()=' Project']//following::span[1]")
	private WebElement button_ExtractProject;

	@FindBy(xpath = "//span[text()=' Child Jobs Associated']//following::div[@role='region'][1]//a")
	private WebElement button_ChildJObURL;

	@FindBy(xpath = "//span[text()=' Project']//following::div[@role='region'][1]//a")
	private WebElement button_ProjectURL;

	@FindBy(xpath = "//span[text()=' Child Jobs Associated']//following::div[@role='region'][1]//a")
	private List<WebElement> button_MultopleChildJObURL;

	@FindBy(xpath = "//div[@id='center-panel']//following::dt[text()='Job Category']//following::span[1]")
	private WebElement text_JobCategory;

	@FindBy(xpath = "//div[@id='center-panel']//following::dt[text()='Job Tags']//following::badge")
	private List<WebElement> text_JobTags;

	@FindBy(xpath = "//h3[text()='Job Description']//following::dl")
	private WebElement text_JobDescription;

	@FindBy(xpath = "//p[text()='Assessment Completed' or text()='Work Completed']/following::p[2]")
	private WebElement text_WhoUpdateStatus;

	@FindBy(xpath = "//custom-fields//dt[text()='Salesman 1 Name']//following::span[1]")
	private WebElement text_SalesName;

	@FindBy(xpath = "//custom-fields//dt[text()='Staging Location']//following::span[1]")
	private WebElement text_StagingLocation;

	@FindBy(xpath = "//custom-fields//dt[text()='Arrival Timeframe']//following::span[1]")
	private WebElement text_ArrivalTimeframe;

	@FindBy(xpath = "//dt[normalize-space(text())='Scheduled Start Time']//following::span[1]")
	private WebElement text_JobScheduledStartTime;

	@FindBy(xpath = "//dt[normalize-space(text())='Scheduled End Time']//following::span[1]")
	private WebElement text_JobScheduledEndTime;

	@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option[1]")
	private WebElement dropdown_FirstOption;

	@FindBy(xpath = "//label[text()='Reason for Assessment']//following::div[1]")
	private WebElement dropdown_ReasonforAssessment;

	@FindBy(xpath = "//label[text()='Future Install Job Work Type']//following::label[(text() = 'Stack Work')]")
	private WebElement childJob_Tags;

	@FindBy(xpath = "//label[text()='Future Job Basic Description']//following::label[(text() = 'Stack Work')]")
	private WebElement childJob_Tags1;

	@FindBy(xpath = "//a[text()='Jobs']")
	private WebElement button_navigateListingPage_FromJobDetailsPage;

	@FindBy(xpath = "//input[@value='Choose file']")
	private WebElement button_BeforeInstallpicture;

	@FindBy(xpath = "//input[@type='file']")
	private WebElement button_fileupload;

	@FindBy(xpath = "//button[text()='Close']")
	private WebElement button_Closefileupload;

	@FindBy(xpath = "//label[text()='Protective Footwear On?']//following::div[1]//label[text()='Yes']//preceding-sibling::input")
	private WebElement button_ProtectiveFootwaer;

	@FindBy(xpath = "//label[text()='Protective floor Coverings Used?']//following::div[1]//label[text()='Yes']//preceding-sibling::input")
	private WebElement button_ProtectiveFloorUsed;

	@FindBy(xpath = "//label[text()='Scope of work reviewed with client?']//following::div[1]//label[text()='Yes']//preceding-sibling::input")
	private WebElement button_ScopeofWorkreviewed;

	@FindBy(xpath = "//label[text()='Remaining Balance Collected']//following::div[1]//label[text()='Yes']//preceding-sibling::input")
	private WebElement button_Remaining_BalanceCollected;

	@FindBy(xpath = "//label[text()='Person with Authority to Sign is Present on Worksite.']//following::div[1]//label[text()='Yes']//preceding-sibling::input")
	private WebElement button_PersonwithAuthoritytoSign;
	
	@FindBy(xpath = "//label[text()='Balance Still Pending']//following::div[1]//label[text()='No']//preceding-sibling::input")
	private WebElement button_PBalanceStillPending;
	
	@FindBy(xpath = "//label[text()='Additional Materials Needed?']//following::div[1]//label[text()='No']//preceding-sibling::input")
	private WebElement button_AdditionalMaterial;


	// Page actions

	// =============================
	// ✅ STATUS VERIFICATION METHODS
	// =============================

	public void verify_CurrentStatus(String currentStatusName) {
		Non_WebDriver_Util.waitForBeClickable(driver, button_StatusHistory, 3);
		button_StatusHistory.click();
		Non_WebDriver_Util.waitForVisible(driver, current_JobStatus, 3);
		Assert.assertEquals(currentStatusName.toLowerCase(), current_JobStatus.getText().toLowerCase(),
				"❌ Job status mismatch!");
	}

	public void verify_ChildJobAssoicated(String job_Count) {
		Non_WebDriver_Util.waitThread(10);
		Non_WebDriver_Util.refreshPage(driver);
		String text = text_ChildJobs_Associated.getText();
		Integer count = Non_WebDriver_Util.extractNumberFromBrackets(text);
		Assert.assertEquals(count, Integer.parseInt(job_Count), "❌ Count mismatch! Expected 1.");
	}

	public void verify_CustomField() {
		Assert.assertEquals(waitForReason, field_Waiting_Reason.getText(), " \"❌ FAIL: Reason mismatch!\"");
	}

	// =============================
	// ✅ BASIC STATUS UPDATE
	// =============================

	public void updateJobStatus(String statusNameToUpdate) {
		if ((text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Install")
				&& statusNameToUpdate.trim().equalsIgnoreCase("En Route"))
				|| (text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Return Visit")
						&& statusNameToUpdate.trim().equalsIgnoreCase("En Route"))) {
			Non_WebDriver_Util.waitThread(2);
			Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
			Non_WebDriver_Util.scrollIntoViewAndClick(driver, update_JobStatus);
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);
			Non_WebDriver_Util.waitThread(1);
			button_DoyouNeedtoTravelforParts.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "Yes");
			Non_WebDriver_Util.waitThread(1);
			buttonUpdateJobchecklist.click();
			Non_WebDriver_Util.waitThread(2);
			Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
					"Status not updated as expected.");

		} else if ((text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Site Visit")
				&& statusNameToUpdate.trim().equalsIgnoreCase("En Route"))) {
			Non_WebDriver_Util.waitThread(2);
			Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
			Non_WebDriver_Util.scrollIntoViewAndClick(driver, update_JobStatus);
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForBeClickable(driver, button_OntheWayTexttotheCustomer, 5);
			Non_WebDriver_Util.waitForVisible(driver, button_OntheWayTexttotheCustomer, 5);
			button_OntheWayTexttotheCustomer.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForBeClickable(driver, dropdown_FirstOption, 5);
			Non_WebDriver_Util.waitForVisible(driver, dropdown_FirstOption, 5);
			dropdown_FirstOption.click();
			Non_WebDriver_Util.waitThread(1);
			buttonUpdateJobchecklist.click();
			Non_WebDriver_Util.waitThread(2);
			Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
					"Status not updated as expected.");

		} else {
			Non_WebDriver_Util.waitThread(2);
			Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
			Non_WebDriver_Util.scrollIntoViewAndClick(driver, update_JobStatus);
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, button_JobStatusUpdate, 5);
			Non_WebDriver_Util.scrollIntoViewAndClick(driver, button_JobStatusUpdate);
			Non_WebDriver_Util.waitThread(2);
			Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
					"Status not updated as expected.");
		}
	}

	// =============================
	// ✅ STATUS UPDATE + CHECKLIST FLOWS
	// =============================

	public void updateJobStatus_WithChecklist(String statusNameToUpdate) {
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(1);
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
		Non_WebDriver_Util.waitThread(3);
		Non_WebDriver_Util.refreshPage(driver);
		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
				"Status not updated as expected.");
	}

	// Arrived → No Answer flow
	public void updateJobStatus_WithChecklist_Arrived(String statusNameToUpdate) {
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);
		Non_WebDriver_Util.waitForVisible(driver, button_yes, 5);
		Non_WebDriver_Util.waitThread(1);
		button_yes.click();
		if ((text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Install")
				&& statusNameToUpdate.trim().equalsIgnoreCase("Arrived"))) {
			Non_WebDriver_Util.waitForVisible(driver, button_yes_ConsumerRights, 5);
			button_yes_ConsumerRights.click();
		}
		buttonUpdateJobchecklist.click();
		Non_WebDriver_Util.waitThread(2);
		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
				"Status not updated as expected.");
	}

	// Work in progress → flow
	public void updateJobStatus_WithChecklist_WorkInProgress(String statusNameToUpdate) {
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);
		Non_WebDriver_Util.waitThread(1);
		button_BeforeInstallpicture.click();
		Non_WebDriver_Util.waitThread(1);
		button_fileupload
				.sendKeys("C:\\Users\\suriyapathy.b\\OneDrive - Zuper,Inc\\Pictures\\Screenshot 2024-04-03 070447.jpg");
		// Non_WebDriver_Util.uploadUsingRobot(driver, button_fileupload,
		// "C:\\Users\\suriyapathy.b\\OneDrive - Zuper,Inc\\Pictures\\Screenshot
		// 2024-04-03 070447.jpg", 3);
		Non_WebDriver_Util.waitThread(3);
		button_Closefileupload.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForVisible(driver, button_ProtectiveFootwaer, 5);
		button_ProtectiveFootwaer.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForVisible(driver, button_ProtectiveFloorUsed, 5);
		button_ProtectiveFloorUsed.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForVisible(driver, button_ScopeofWorkreviewed, 5);
		button_ScopeofWorkreviewed.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForVisible(driver, button_Remaining_BalanceCollected, 5);
		button_Remaining_BalanceCollected.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForVisible(driver, button_PersonwithAuthoritytoSign, 5);
		button_PersonwithAuthoritytoSign.click();
		Non_WebDriver_Util.waitThread(1);

		buttonUpdateJobchecklist.click();
		Non_WebDriver_Util.waitThread(2);
		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
				"Status not updated as expected.");
	}

	// Starting Assessment flow
	public void updateJobStatus_WithChecklist_StatringAssessment(String statusNameToUpdate) {

		if ((text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Site Visit")
				&& statusNameToUpdate.trim().equalsIgnoreCase("Starting Assessment"))) {
			Non_WebDriver_Util.waitThread(2);
			Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
			Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
			update_JobStatus.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);
			Non_WebDriver_Util.waitThread(1);

			Non_WebDriver_Util.waitForVisible(driver, button_AreYouWearingbooties, 5);
			Non_WebDriver_Util.waitThread(1);
			button_AreYouWearingbooties.click();

			Non_WebDriver_Util.waitForBeClickable(driver, dropdown_ReasonforAssessment, 5);
			Non_WebDriver_Util.waitForVisible(driver, dropdown_ReasonforAssessment, 5);
			dropdown_ReasonforAssessment.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForBeClickable(driver, dropdown_FirstOption, 5);
			Non_WebDriver_Util.waitForVisible(driver, dropdown_FirstOption, 5);
			dropdown_FirstOption.click();
			Non_WebDriver_Util.waitThread(1);
			buttonUpdateJobchecklist.click();
			Non_WebDriver_Util.waitThread(2);
			Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
					"Status not updated as expected.");
		} else {
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
			Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
			update_JobStatus.click();
			Non_WebDriver_Util.waitThread(1);
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
			Non_WebDriver_Util.waitThread(2);
			Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
					"Status not updated as expected.");
		}

	}

	public void updateJobStatus_WithChecklist_AssessmentCompleted_GenericJobSoldFuture(String jobType, // "Plumbing
																										// Install",
																										// "Plumbing
																										// Return
																										// Visit",
																										// "Plumbing
																										// Excavation"
			String statusNameToUpdate, String StagingLocation, String futureJobLength, String basicDescriptionofWork,
			boolean isScheduled, // true = schedule date & timeframe required
			String arrivalTimeframe, // optional if !isScheduled
			String jobTBDReason, // optional if isScheduled
			String permitNeeded) {

		// 1. Update Job Status
		Non_WebDriver_Util.waitThread(2);
		Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);

		// 2. Job Sold → Future Appointment
		Non_WebDriver_Util.waitForBeClickable(driver, button_Job_Sold_Future_Appointment, 5);
		Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Job_Sold_Future_Appointment.click();

		if (!(jobType.trim().equalsIgnoreCase("Plumbing Site Visit"))) {
			Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
			Non_WebDriver_Util.waitThread(1);
			button_Was_Customer_Proposal_Accepted.click();
		}

		// 3. Staging Location
		multiSelect_StagingLocation.click();
		Non_WebDriver_Util.waitThread(1);
		this.StagingLocation = StagingLocation;
		Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, StagingLocation);

		// 4. Job Type Button
		if (jobType.trim().equalsIgnoreCase("Plumbing Install")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
			button_PlumbingInstall.click();
			Non_WebDriver_Util.waitForVisible(driver, button_MaterialOrdering, 5);
			button_MaterialOrdering.click();
		} else if (jobType.trim().equalsIgnoreCase("Plumbing Return Visit")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5); // shared button
			button_PlumbingInstall.click();
			Non_WebDriver_Util.waitForVisible(driver, button_MaterialisTrackStock, 5);
			button_MaterialisTrackStock.click();
		} else if (jobType.trim().equalsIgnoreCase("Plumbing Excavation")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingExcavation, 5);
			button_PlumbingExcavation.click();
		}

		// 5. Job Length & Crew Members
		multiSelect_FutureJObLength.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

		multiSelect_HowMany_Crew_Members.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

		// 6. Work Type Button
		if (text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Site Visit")) {
			Non_WebDriver_Util.waitForVisible(driver, button_FutureJobBasicDescription, 5);
			Non_WebDriver_Util.waitThread(1);
			button_FutureJobBasicDescription.click();
			this.childJobTags = childJob_Tags1.getText();
		} else {
			Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
			Non_WebDriver_Util.waitThread(1);
			button_FutureInstallJobWorkType.click();
			this.childJobTags = childJob_Tags.getText();
		}

		// 7. Description
		Non_WebDriver_Util.waitForVisible(driver, input_BasicDescriptionofwork, 5);
		input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);

		// 8. Scheduling
		multiSelect_HaveYouScheduled.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");

		if (isScheduled) {
			// Scheduled Date
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, select_JobStartDate, 5);
			select_JobStartDate.click();
			Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
			Non_WebDriver_Util.waitThread(1);
			select_CurrentDateToFollowUpDate.click();
			Non_WebDriver_Util.waitThread(1);
			this.selectJobStartDate = select_JobStartDate.getAttribute("value");

			// Arrival Timeframe
			multiSelect_ArrivalTimeframe.click();
			Non_WebDriver_Util.waitThread(1);
			this.arrivalTimeframe = arrivalTimeframe;
			Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		} else {
			// Job TBD Reason
			this.JobTBDReason = jobTBDReason;
			By button_JobTBDReason = By.xpath(String
					.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
			Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
			Non_WebDriver_Util.waitThread(1);
			this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
			driver.findElement(button_JobTBDReason).click();
		}

		// 9. Permit Needed
		multiSelect_PremitNeeded.click();
		Non_WebDriver_Util.waitThread(1);
		this.permit_Needed = permitNeeded;
		By element_PermitNeeded = By.xpath(String.format(
				"//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PermitNeeded), 5);
		this.copy_permit_Needed = driver.findElement(element_PermitNeeded).getText();
		driver.findElement(element_PermitNeeded).click();

		// 10. Submit Checklist
		Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

		buttonUpdateJobchecklist.click();
		this.updated_by = text_WhoUpdateStatus.getText();
		Non_WebDriver_Util.waitThread(1);
		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.split("/")[0].toLowerCase(),
				"Status not updated as expected.");
	}

	
	
	public void updateJobStatus_WithChecklist_WorkCompleted_GenericJobSoldFuture(String jobType, // "Plumbing
			// Install",
			// "Plumbing
			// Return
			// Visit",
			// "Plumbing
			// Excavation"
			String statusNameToUpdate, String StagingLocation, String futureJobLength, String basicDescriptionofWork,
			boolean isScheduled, // true = schedule date & timeframe required
			String arrivalTimeframe, // optional if !isScheduled
			String jobTBDReason, // optional if isScheduled
			String permitNeeded) {

// 1. Update Job Status
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);
		
		
		Non_WebDriver_Util.waitThread(1);
		button_BeforeInstallpicture.click();
		Non_WebDriver_Util.waitThread(1);
		button_fileupload
				.sendKeys("C:\\Users\\suriyapathy.b\\OneDrive - Zuper,Inc\\Pictures\\Screenshot 2024-04-03 070447.jpg");
		Non_WebDriver_Util.waitThread(2);
		button_Closefileupload.click();
		

// 2. Job Sold → Future Appointment
		Non_WebDriver_Util.waitForBeClickable(driver, button_Job_Sold_Future_Appointment, 5);
		Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Job_Sold_Future_Appointment.click();

//		if (!((jobType.trim().equalsIgnoreCase("Plumbing Site Visit")) || (jobType.trim().equalsIgnoreCase("Plumbing Install")) || (jobType.trim().equalsIgnoreCase("Plumbing Return Visit")))) {
//			Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
//			Non_WebDriver_Util.waitThread(1);
//			button_Was_Customer_Proposal_Accepted.click();
//		}
		
// 3. Balance Still Pending
		
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForBeClickable(driver, button_PBalanceStillPending, 5);
		button_PBalanceStillPending.click();
		Non_WebDriver_Util.waitThread(1);

// 3. Staging Location
		multiSelect_StagingLocation.click();
		Non_WebDriver_Util.waitThread(1);
		this.StagingLocation = StagingLocation;
		Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, StagingLocation);

// 4. Job Type Button
		if (jobType.trim().equalsIgnoreCase("Plumbing Install")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
			button_PlumbingInstall.click();
			Non_WebDriver_Util.waitForVisible(driver, button_MaterialOrdering, 5);
			button_MaterialOrdering.click();
		} else if (jobType.trim().equalsIgnoreCase("Plumbing Return Visit")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5); // shared button
			button_PlumbingInstall.click();
			Non_WebDriver_Util.waitForVisible(driver, button_MaterialisTrackStock, 5);
			button_MaterialisTrackStock.click();
		} else if (jobType.trim().equalsIgnoreCase("Plumbing Excavation")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingExcavation, 5);
			button_PlumbingExcavation.click();
			Non_WebDriver_Util.waitForVisible(driver, button_MaterialisTrackStock, 5);
			button_MaterialisTrackStock.click();
		}

// 5. Job Length & Crew Members
		multiSelect_FutureJObLength.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

		multiSelect_HowMany_Crew_Members.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

// 6. Work Type Button
		if (text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Site Visit") || text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Install") ) {
			Non_WebDriver_Util.waitForVisible(driver, button_FutureJobBasicDescription, 5);
			Non_WebDriver_Util.waitThread(1);
			button_FutureJobBasicDescription.click();
			this.childJobTags = childJob_Tags1.getText();
		} else {
			Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
			Non_WebDriver_Util.waitThread(1);
			button_FutureInstallJobWorkType.click();
			this.childJobTags = childJob_Tags.getText();
		}

// 7. Description
		Non_WebDriver_Util.waitForVisible(driver, input_BasicDescriptionofwork, 5);
		input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);

// 8. Scheduling
		multiSelect_HaveYouScheduled.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");

		if (isScheduled) {
// Scheduled Date
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, select_JobStartDate, 5);
			select_JobStartDate.click();
			Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
			Non_WebDriver_Util.waitThread(1);
			select_CurrentDateToFollowUpDate.click();
			Non_WebDriver_Util.waitThread(1);
			this.selectJobStartDate = select_JobStartDate.getAttribute("value");

// Arrival Timeframe
			multiSelect_ArrivalTimeframe.click();
			Non_WebDriver_Util.waitThread(1);
			this.arrivalTimeframe = arrivalTimeframe;
			Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		} else {
// Job TBD Reason
			this.JobTBDReason = jobTBDReason;
			By button_JobTBDReason = By.xpath(String
					.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
			Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
			Non_WebDriver_Util.waitThread(1);
			this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
			driver.findElement(button_JobTBDReason).click();
		}

// 9. Permit Needed
		multiSelect_PremitNeeded.click();
		Non_WebDriver_Util.waitThread(1);
		this.permit_Needed = permitNeeded;
		By element_PermitNeeded = By.xpath(String.format(
				"//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PermitNeeded), 5);
		this.copy_permit_Needed = driver.findElement(element_PermitNeeded).getText();
		driver.findElement(element_PermitNeeded).click();

// 10. Submit Checklist
		Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

		buttonUpdateJobchecklist.click();
		this.updated_by = text_WhoUpdateStatus.getText();
		Non_WebDriver_Util.waitThread(1);
		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.split("/")[1].toLowerCase(),
				"Status not updated as expected.");
	}

	public void updateJobStatus_WithChecklist_AssessmentCompleted_EstimateNeeded_Generic(String statusNameToUpdate,
			boolean isScheduled, String ifNoAppointmentHasBeenSet, // Used if isScheduled == false
			String arrivalTimeframe, // Used if isScheduled == true
			String appointmentType, String notesToAccountManagerDescriptionOfWork, String basicDescriptionofWork) {
		// 1. Update Job Status
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);

		// 2. Estimate Needed
		Non_WebDriver_Util.waitForVisible(driver, button_EstimateSent_EstimateNeeded, 5);
		Non_WebDriver_Util.waitThread(1);
		button_EstimateSent_EstimateNeeded.click();

		// 3. Estimate Sent
		Non_WebDriver_Util.waitForVisible(driver, button_Was_Estimate_Sent_To_Customer, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Was_Estimate_Sent_To_Customer.click();

		// 4. Unable to Access
		Non_WebDriver_Util.waitForVisible(driver, button_UnableToProperlyAccess, 5);
		Non_WebDriver_Util.waitThread(1);
		button_UnableToProperlyAccess.click();

		// 5. Has Appointment Been Set with Dispatch
		if (isScheduled) {
			Non_WebDriver_Util.waitForVisible(driver, button_HasAppointmentbeensetwithDispatch_Yes, 5);
			Non_WebDriver_Util.waitThread(1);
			button_HasAppointmentbeensetwithDispatch_Yes.click();
			// Scheduled Date (Today)
			Non_WebDriver_Util.waitForVisible(driver, select_AppointmentDate_JobStartDate, 5);
			select_AppointmentDate_JobStartDate.click();

			Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
			Non_WebDriver_Util.waitThread(1);
			select_CurrentDateToFollowUpDate.click();
			Non_WebDriver_Util.waitThread(1);
			this.selectJobStartDate = select_AppointmentDate_JobStartDate.getAttribute("value");

			// Arrival Timeframe
			multiSelect_ArrivalTimeframe_estimate.click();
			Non_WebDriver_Util.waitThread(1);
			this.arrivalTimeframe = arrivalTimeframe;
			Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);

			// Appointment Type
			multiSelect_appointmentType_estimate.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		} else {
			Non_WebDriver_Util.waitForVisible(driver, button_HasAppointmentbeensetwithDispatch, 5);
			Non_WebDriver_Util.waitThread(1);
			button_HasAppointmentbeensetwithDispatch.click();
			// If No Appointment Has Been Set
			multiSelect_IfNoAppointmenthasbeenset.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, element_IfNoAppointmenthasbeenset,
					ifNoAppointmentHasBeenSet);
			// Appointment Type
			multiSelect_appointmentType.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);

		}

		// 6. Customer’s Preferred Follow-Up Date (Today)
		Non_WebDriver_Util.waitForVisible(driver, select_CustomersPreferredFollowUpDate, 5);
		Non_WebDriver_Util.waitThread(1);
		select_CustomersPreferredFollowUpDate.click();

		Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
		Non_WebDriver_Util.waitThread(1);
		select_CurrentDateToFollowUpDate.click();

		// 7. Notes to Account Manager
		Non_WebDriver_Util.waitThread(1);
		input_NotestoAccountManager.sendKeys(notesToAccountManagerDescriptionOfWork);

		// 7. Description
		if (prop.getProperty("jobCategory").trim().equalsIgnoreCase("Inspection-Plumbing")) {
			Non_WebDriver_Util.waitForVisible(driver, input_BasicDescriptionofwork, 5);
			input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
		}
		// 8. Submit
		buttonUpdateJobchecklist.click();

		// 9. Capture update info
		this.updated_by = text_WhoUpdateStatus.getText();

		Non_WebDriver_Util.waitThread(1);
		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.split("/")[0].toLowerCase(),
				"Status not updated as expected.");
	}
	
	//work Completed
	
	public void updateJobStatus_WithChecklist_WorkCompleted_EstimateNeeded_Generic(String statusNameToUpdate,
			boolean isScheduled, String ifNoAppointmentHasBeenSet, // Used if isScheduled == false
			String arrivalTimeframe, // Used if isScheduled == true
			String appointmentType, String notesToAccountManagerDescriptionOfWork, String basicDescriptionofWork) {
		// 1. Update Job Status
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);

		//Picture Upload	
		Non_WebDriver_Util.waitThread(1);
		button_BeforeInstallpicture.click();
		Non_WebDriver_Util.waitThread(1);
		button_fileupload
				.sendKeys("C:\\Users\\suriyapathy.b\\OneDrive - Zuper,Inc\\Pictures\\Screenshot 2024-04-03 070447.jpg");
		Non_WebDriver_Util.waitThread(2);
		button_Closefileupload.click();
		
		
		// 2. Estimate Needed
		Non_WebDriver_Util.waitForVisible(driver, button_EstimateSent_EstimateNeeded, 5);
		Non_WebDriver_Util.waitThread(1);
		button_EstimateSent_EstimateNeeded.click();
		
		// 3. Balance Still Pending
		
				Non_WebDriver_Util.waitThread(1);
				Non_WebDriver_Util.waitForBeClickable(driver, button_PBalanceStillPending, 5);
				button_PBalanceStillPending.click();
				Non_WebDriver_Util.waitThread(1);

		// 3. Estimate Sent
		Non_WebDriver_Util.waitForVisible(driver, button_Was_Estimate_Sent_To_Customer, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Was_Estimate_Sent_To_Customer.click();

		// 4. Unable to Access
		Non_WebDriver_Util.waitForVisible(driver, button_UnableToProperlyAccess, 5);
		Non_WebDriver_Util.waitThread(1);
		button_UnableToProperlyAccess.click();

		// 5. Has Appointment Been Set with Dispatch
		if (isScheduled) {
			Non_WebDriver_Util.waitForVisible(driver, button_HasAppointmentbeensetwithDispatch_Yes, 5);
			Non_WebDriver_Util.waitThread(1);
			button_HasAppointmentbeensetwithDispatch_Yes.click();
			// Scheduled Date (Today)
			Non_WebDriver_Util.waitForVisible(driver, select_AppointmentDate_JobStartDate, 5);
			select_AppointmentDate_JobStartDate.click();

			Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
			Non_WebDriver_Util.waitThread(1);
			select_CurrentDateToFollowUpDate.click();
			Non_WebDriver_Util.waitThread(1);
			this.selectJobStartDate = select_AppointmentDate_JobStartDate.getAttribute("value");

			// Arrival Timeframe
			multiSelect_ArrivalTimeframe_estimate.click();
			Non_WebDriver_Util.waitThread(1);
			this.arrivalTimeframe = arrivalTimeframe;
			Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);

			// Appointment Type
			multiSelect_appointmentType_estimate.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		} else {
			Non_WebDriver_Util.waitForVisible(driver, button_HasAppointmentbeensetwithDispatch, 5);
			Non_WebDriver_Util.waitThread(1);
			button_HasAppointmentbeensetwithDispatch.click();
			// If No Appointment Has Been Set
//			multiSelect_IfNoAppointmenthasbeenset.click();
//			Non_WebDriver_Util.waitThread(1);
//			Non_WebDriver_Util.selectMatOptionByText(driver, element_IfNoAppointmenthasbeenset,
//					ifNoAppointmentHasBeenSet);
			// Appointment Type
			multiSelect_appointmentType_estimate.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);

		}

//		// 6. Customer’s Preferred Follow-Up Date (Today)
//		Non_WebDriver_Util.waitForVisible(driver, select_CustomersPreferredFollowUpDate, 5);
//		Non_WebDriver_Util.waitThread(1);
//		select_CustomersPreferredFollowUpDate.click();
//
//		Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
//		Non_WebDriver_Util.waitThread(1);
//		select_CurrentDateToFollowUpDate.click();
//
//		// 7. Notes to Account Manager
//		Non_WebDriver_Util.waitThread(1);
//		input_NotestoAccountManager.sendKeys(notesToAccountManagerDescriptionOfWork);
//
//		// 7. Description
//		if (prop.getProperty("jobCategory").trim().equalsIgnoreCase("Inspection-Plumbing")) {
//			Non_WebDriver_Util.waitForVisible(driver, input_BasicDescriptionofwork, 5);
//			input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
//		}
		
		
		// 8. Submit
		Non_WebDriver_Util.waitThread(1);
		buttonUpdateJobchecklist.click();

		// 9. Capture update info
		this.updated_by = text_WhoUpdateStatus.getText();

		Non_WebDriver_Util.waitThread(1);
		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.split("/")[1].toLowerCase(),
				"Status not updated as expected.");
	}

	

	// Assessment Completed flow for Project With Plumbing Job Types (Install,
	// Return Visit, Excavation)

	public void updateJobStatus_WithChecklist_AssessmentCompleted_ProjectWith_Plumbing_RollOverJob(String jobType,
			String statusNameToUpdate, String stagingLocation, String futureJobLength, String basicDescription,
			boolean isScheduled, String arrivalTimeframe, String jobTBDReason, String permitNeeded) {
		// 1. Update Job Status
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(3);
		Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);

		// 2. Job Sold – Future Appointment
		Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Job_Sold_Future_Appointment.click();

		// 3. Customer Proposal Accepted
		Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Was_Customer_Proposal_Accepted.click();

		// 4. Staging Location
		multiSelect_StagingLocation.click();
		Non_WebDriver_Util.waitThread(1);
		this.StagingLocation = stagingLocation;
		Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, stagingLocation);

		// 5. Job Type & Material Selection
		if (jobType.contains("Plumbing Install")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
			Non_WebDriver_Util.waitThread(1);
			button_PlumbingInstall.click();

			Non_WebDriver_Util.waitForVisible(driver, button_MaterialOrdering, 5);
			Non_WebDriver_Util.waitThread(1);
			button_MaterialOrdering.click();
		} else if (jobType.contains("Plumbing Return Visit")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
			Non_WebDriver_Util.waitThread(1);
			button_PlumbingInstall.click();

			Non_WebDriver_Util.waitForVisible(driver, button_MaterialisTrackStock, 5);
			Non_WebDriver_Util.waitThread(1);
			button_MaterialisTrackStock.click();
		} else if (jobType.contains("Plumbing Excavation")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingExcavation, 5);
			Non_WebDriver_Util.waitThread(1);
			button_PlumbingExcavation.click();
		}

		// 6. Future Job Length & Crew
		multiSelect_FutureJObLength.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

		multiSelect_HowMany_Crew_Members.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

		// 7. Work Type

		Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
		Non_WebDriver_Util.waitThread(1);
		button_FutureInstallJobWorkType.click();
		this.childJobTags = childJob_Tags.getText();

		// 8. Description
		input_BasicDescriptionofwork.sendKeys(basicDescription);

		// 9. Scheduling or TBD
		multiSelect_HaveYouScheduled.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");

		if (isScheduled) {
			// Scheduled Date (today)
			Non_WebDriver_Util.waitForVisible(driver, select_JobStartDate, 5);
			select_JobStartDate.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
			select_CurrentDateToFollowUpDate.click();
			Non_WebDriver_Util.waitThread(1);
			this.selectJobStartDate = select_JobStartDate.getAttribute("value");

			// Arrival Timeframe
			multiSelect_ArrivalTimeframe.click();
			Non_WebDriver_Util.waitThread(1);
			this.arrivalTimeframe = arrivalTimeframe;
			Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		} else {
			// TBD Reason
			this.JobTBDReason = jobTBDReason;
			By button_JobTBDReason = By.xpath(String
					.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
			Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
			Non_WebDriver_Util.waitThread(1);
			this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
			driver.findElement(button_JobTBDReason).click();
		}

		// 10. Permit Needed
		multiSelect_PremitNeeded.click();
		Non_WebDriver_Util.waitThread(1);
		this.permit_Needed = permitNeeded;
		By element_PermitNeeded = By.xpath(String.format(
				"//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PermitNeeded), 5);
		this.copy_permit_Needed = driver.findElement(element_PermitNeeded).getText();
		driver.findElement(element_PermitNeeded).click();

		// 11. Submit Checklist
		Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

		buttonUpdateJobchecklist.click();
		this.updated_by = text_WhoUpdateStatus.getText();

		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.split("/")[0].toLowerCase(),
				"Status not updated as expected.");
	}
	
	// workCompleted
	public void updateJobStatus_WithChecklist_WorkCompleted_ProjectWith_Plumbing_RollOverJob(String jobType,
			String statusNameToUpdate, String stagingLocation, String futureJobLength, String basicDescription,
			boolean isScheduled, String arrivalTimeframe, String jobTBDReason, String permitNeeded) {
		// 1. Update Job Status
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(3);
		Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);
		
		Non_WebDriver_Util.waitThread(1);
		button_BeforeInstallpicture.click();
		Non_WebDriver_Util.waitThread(1);
		button_fileupload
				.sendKeys("C:\\Users\\suriyapathy.b\\OneDrive - Zuper,Inc\\Pictures\\Screenshot 2024-04-03 070447.jpg");
		Non_WebDriver_Util.waitThread(2);
		button_Closefileupload.click();

		// 2. Job Sold – Future Appointment
		Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Job_Sold_Future_Appointment.click();
		
		// 3. Balance Still Pending
		
				Non_WebDriver_Util.waitThread(1);
				Non_WebDriver_Util.waitForBeClickable(driver, button_PBalanceStillPending, 5);
				button_PBalanceStillPending.click();
				Non_WebDriver_Util.waitThread(1);

//		// 3. Customer Proposal Accepted
//		Non_WebDriver_Util.waitForVisible(driver, button_Was_Customer_Proposal_Accepted, 5);
//		Non_WebDriver_Util.waitThread(1);
//		button_Was_Customer_Proposal_Accepted.click();

		// 4. Staging Location
		multiSelect_StagingLocation.click();
		Non_WebDriver_Util.waitThread(1);
		this.StagingLocation = stagingLocation;
		Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, stagingLocation);

		// 5. Job Type & Material Selection
		if (jobType.contains("Plumbing Install")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
			Non_WebDriver_Util.waitThread(1);
			button_PlumbingInstall.click();

			Non_WebDriver_Util.waitForVisible(driver, button_MaterialOrdering, 5);
			Non_WebDriver_Util.waitThread(1);
			button_MaterialOrdering.click();
		} else if (jobType.contains("Plumbing Return Visit")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
			Non_WebDriver_Util.waitThread(1);
			button_PlumbingInstall.click();

			Non_WebDriver_Util.waitForVisible(driver, button_MaterialisTrackStock, 5);
			Non_WebDriver_Util.waitThread(1);
			button_MaterialisTrackStock.click();
		} else if (jobType.contains("Plumbing Excavation")) {
			Non_WebDriver_Util.waitForVisible(driver, button_PlumbingExcavation, 5);
			Non_WebDriver_Util.waitThread(1);
			button_PlumbingExcavation.click();
			Non_WebDriver_Util.waitForVisible(driver, button_MaterialisTrackStock, 5);
			Non_WebDriver_Util.waitThread(1);
			button_MaterialisTrackStock.click();
		}

		// 6. Future Job Length & Crew
		multiSelect_FutureJObLength.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);

		multiSelect_HowMany_Crew_Members.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");

		// 7. Work Type

		if (text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Site Visit") || text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Install") ) {
			Non_WebDriver_Util.waitForVisible(driver, button_FutureJobBasicDescription, 5);
			Non_WebDriver_Util.waitThread(1);
			button_FutureJobBasicDescription.click();
			this.childJobTags = childJob_Tags1.getText();
		} else {
			Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
			Non_WebDriver_Util.waitThread(1);
			button_FutureInstallJobWorkType.click();
			this.childJobTags = childJob_Tags.getText();
		}

		// 8. Description
		input_BasicDescriptionofwork.sendKeys(basicDescription);

		// 9. Scheduling or TBD
		multiSelect_HaveYouScheduled.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");

		if (isScheduled) {
			// Scheduled Date (today)
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, select_JobStartDate, 5);
			select_JobStartDate.click();
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
			select_CurrentDateToFollowUpDate.click();
			Non_WebDriver_Util.waitThread(1);
			this.selectJobStartDate = select_JobStartDate.getAttribute("value");

			// Arrival Timeframe
			multiSelect_ArrivalTimeframe.click();
			Non_WebDriver_Util.waitThread(1);
			this.arrivalTimeframe = arrivalTimeframe;
			Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		} else {
			// TBD Reason
			this.JobTBDReason = jobTBDReason;
			By button_JobTBDReason = By.xpath(String
					.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
			Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
			Non_WebDriver_Util.waitThread(1);
			this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
			driver.findElement(button_JobTBDReason).click();
		}

		// 10. Permit Needed
		multiSelect_PremitNeeded.click();
		Non_WebDriver_Util.waitThread(1);
		this.permit_Needed = permitNeeded;
		By element_PermitNeeded = By.xpath(String.format(
				"//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PermitNeeded), 5);
		this.copy_permit_Needed = driver.findElement(element_PermitNeeded).getText();
		driver.findElement(element_PermitNeeded).click();

		// 11. Submit Checklist
		Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
		Non_WebDriver_Util.waitThread(1);
		button_Plumbing_JobSoldChecklist_to_Be_Completed.click();

		buttonUpdateJobchecklist.click();
		this.updated_by = text_WhoUpdateStatus.getText();

		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.split("/")[1].toLowerCase(),
				"Status not updated as expected.");
	}

	// Rollover Needed
	

	public void updateJobStatus_WithChecklist_WorkCompleted_GenericRolloverNeeded(                 
			String statusNameToUpdate, String basicDescriptionofWork,
			boolean isScheduled, // true = schedule date & timeframe required
			String arrivalTimeframe // optional if !isScheduled
			) {

		// 1. Update Job Status
		Non_WebDriver_Util.waitThread(2);
		Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		update_JobStatus.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);
		
		Non_WebDriver_Util.waitThread(1);
		button_BeforeInstallpicture.click();
		Non_WebDriver_Util.waitThread(1);
		button_fileupload
				.sendKeys("C:\\Users\\suriyapathy.b\\OneDrive - Zuper,Inc\\Pictures\\Screenshot 2024-04-03 070447.jpg");
		Non_WebDriver_Util.waitThread(2);
		button_Closefileupload.click();

		// 2. Rollover Needed
		Non_WebDriver_Util.waitForBeClickable(driver, button_RolloverNeeded, 5);
		Non_WebDriver_Util.waitForVisible(driver, button_RolloverNeeded, 5);
		Non_WebDriver_Util.waitThread(1);
		button_RolloverNeeded.click();

		// 8. Scheduling
		multiSelect_HaveYouScheduledForRolloverJobs.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");

		if (isScheduled) {
			// Scheduled Date
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, select_Rollover_Appointment_Date, 5);
			select_Rollover_Appointment_Date.click();
			Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
			Non_WebDriver_Util.waitThread(1);
			select_CurrentDateToFollowUpDate.click();
			Non_WebDriver_Util.waitThread(1);
			this.selectJobStartDate = select_Rollover_Appointment_Date.getAttribute("value");

			// Arrival Timeframe
			multiSelect_RolloverTimeframe.click();
			Non_WebDriver_Util.waitThread(1);
			this.arrivalTimeframe = arrivalTimeframe;
			Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		} 

		// 8. Description
		input_RolloverDescriptionofwork.sendKeys(basicDescriptionofWork);
		
		// 3. Balance Still Pending
		
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForBeClickable(driver, button_PBalanceStillPending, 5);
		button_PBalanceStillPending.click();
		Non_WebDriver_Util.waitThread(1);

		
		// 3.Additional Materials Needed?
		
				Non_WebDriver_Util.waitThread(1);
				Non_WebDriver_Util.waitForBeClickable(driver, button_AdditionalMaterial, 5);
				button_AdditionalMaterial.click();
				Non_WebDriver_Util.waitThread(1);

		// 5. Time Needed to Complete Job?
				multiSelect_TimeNeededtoCompleteJob.click();
				Non_WebDriver_Util.waitThread(1);
				Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, "1");

		// 5. How Many Guys Needed
				multiSelect_GuysNeeded.click();
				Non_WebDriver_Util.waitThread(1);
				Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");


		buttonUpdateJobchecklist.click();
		this.updated_by = text_WhoUpdateStatus.getText();
		Non_WebDriver_Util.waitThread(1);
		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.split("/")[1].toLowerCase(),
				"Status not updated as expected.");
	}

	

	// =============================
	// ✅ CHILD JOB HANDLING
	// =============================

	public void navigateToChildJOb() {
		try {
			button_ExtractChildJOb.click();
			Non_WebDriver_Util.waitForVisible(driver, button_ChildJObURL, 10);
			button_ChildJObURL.click();
		} catch (Exception e) {
			logger.error("❌ Failed to navigate to child job", e);
			throw e; // or Assert.fail("Navigation failed")
		}
	}

	public void Verify_ProjectAssociation() {
		try {
			button_ExtractProject.click();
			Non_WebDriver_Util.waitForVisible(driver, button_ProjectURL, 10);
			Non_WebDriver_Util.waitThread(1);
			Assert.assertTrue(button_ProjectURL.isDisplayed(), "❌ Failed to Project Association");
		} catch (Exception e) {
			logger.error("❌ Failed to Project Association", e);
			throw e; // or Assert.fail("Navigation failed")
		}
	}

	public void navigateToMultipleJobs(String twoJobCategory, String basicDescriptionofwork, boolean isScheduled,
			String stagingLocation) {

		String[] categoriesArray = twoJobCategory.split("with");
		List<String> expectedCategories = Arrays.stream(categoriesArray).map(String::trim).collect(Collectors.toList());
		// Expand child jobs section initially
		button_ExtractChildJOb.click();
		Non_WebDriver_Util.waitThread(2);

		for (int i = 0; i < expectedCategories.size(); i++) {

			// Re-fetch the list of child job links each time
			List<WebElement> childJobLinks = driver.findElements(
					By.xpath("//span[text()=' Child Jobs Associated']//following::div[@role='region'][1]//a"));

			if (i >= childJobLinks.size()) {
				throw new RuntimeException("❌ Not enough child job links for validation");
			}

			// Click the ith child job link
			childJobLinks.get(i).click();
			Non_WebDriver_Util.waitThread(2);

			verifyChildJobCategory(expectedCategories.get(i).trim());
			logger.info("✅ Verified child job category: {}", expectedCategories.get(i).trim());
			verifyChildJobTags();
			logger.info("✅ Verified child job Tags");
			verifyChildJobProjectTags();
			logger.info("✅ Verified project  Tags");
			verifyChildJobDescription(basicDescriptionofwork);
			logger.info("✅ Verified child job category: {}", basicDescriptionofwork);
			Verify_ProjectAssociation();
			logger.info("✅ Verified Project Assication");
			verifyCustomfield_SalesName();
			logger.info("✅ Verified Sales Name custom field");
			verifyCustomfield_Staging_Location();
			logger.info("✅ Verified Staging Location custom field");

			try {

				if (isScheduled) {
					verifyJobScheduledDate(i);
					verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
					verifyCustomfield_arrivalTimeframe();
					logger.info("✅ Verified Scheduled details and status for scheduled job");
				} else {
					verifyCustomfield_JObTDBReason(); // Might fail if not set properly
					verify_CurrentStatus(prop.getProperty("currentStatusName_JobPending"));
					logger.info("✅ Verified Job TBD Reason and pending status for unscheduled job");
				}

			} catch (AssertionError | Exception e) {
				logger.error("❌ Error during post-job creation validation: ", e);
				throw e; // To fail the test cleanly and show the issue in logs
			}
			logger.info("✅ Associated " + (i + 1) + " JOb varified Successfully,");
			// Navigate back to the parent job page
			driver.navigate().back();
			Non_WebDriver_Util.waitThread(2);

			// Expand the child job panel again
			button_ExtractChildJOb.click();
			Non_WebDriver_Util.waitThread(2);
		}
	}

	public void verifyChildJobCategory(String jobCategory) {
		try {
			Assert.assertEquals(text_JobCategory.getText().trim(), jobCategory.trim(), "❌ Created wrong Job category");
		} catch (AssertionError e) {
			logger.error("❌ Job category verification failed. Expected: {}, Actual: {}", jobCategory,
					text_JobCategory.getText(), e);
			throw e; // Optional: re-throw if you want test to fail
		}
	}

	public void verifyChildJobTags() {
		boolean expectedTagFound = false;
		try {
			Non_WebDriver_Util.visibilityOfAllElements(driver, text_JobTags, 5);

			for (WebElement tag : text_JobTags) {
				String tagText = tag.getText().trim();
				if (tagText.equalsIgnoreCase(childJobTags.trim())) {
					expectedTagFound = true;

				}
			}
			Assert.assertTrue(expectedTagFound, "❌ Expected child job tag not found: " + childJobTags);
		} catch (Exception e) {
			logger.error("❌ Job Tag  mismatch. Expected");
			throw e; // or Assert.fail("Navigation failed")
		}
	}

	public void verifyChildJobProjectTags() {
		boolean projectTagFound = false;
		try {
			Non_WebDriver_Util.visibilityOfAllElements(driver, text_JobTags, 5);

			for (WebElement tag : text_JobTags) {
				String tagText = tag.getText().trim();
				if (tagText.equalsIgnoreCase("project")) {
					projectTagFound = true;
				}
			}
			Assert.assertTrue(projectTagFound, "❌ Expected child job project tag not found: " + "project");
		} catch (Exception e) {
			logger.error("❌ Job Tag  mismatch. Expected");
			throw e; // or Assert.fail("Navigation failed")
		}
	}

	public void verifyChildJobDescription(String basicDescriptionofwork) {
		try {
			Assert.assertEquals(text_JobDescription.getText().trim(), basicDescriptionofwork.trim(),
					"❌ Created wrong Job Description");
		} catch (AssertionError e) {
			logger.error("❌ Job Description mismatch. Expected: '{}', Actual: '{}'", basicDescriptionofwork,
					text_JobDescription.getText(), e);
			throw e;
		}
	}

	public void verifyCustomfield_SalesName() {
		try {
			Assert.assertEquals(text_SalesName.getText().trim(), updated_by.trim(), "❌ Created wrong Job FE");
		} catch (AssertionError e) {
			logger.error("❌ Sales Name mismatch. Expected: '{}', Actual: '{}'", updated_by, text_SalesName.getText(),
					e);
			throw e;
		}
	}

	public void verifyCustomfield_JObTDBReason() {
		try {
			Assert.assertEquals(copy_JobTBDReason.trim(), JobTBDReason.trim(), "❌ Created wrong Job with JobTBDReason");
		} catch (AssertionError e) {
			logger.error("❌ Job TBD Reason mismatch. Expected: '{}', Actual: '{}'", JobTBDReason, copy_JobTBDReason, e);
			throw e;
		}
	}

	public void verifyCustomfield_PermitNeeded() {
		try {
			Assert.assertEquals(copy_permit_Needed.trim(), permit_Needed.trim(),
					"❌ Created wrong Job with PermitNeeded");
		} catch (AssertionError e) {
			logger.error("❌ Permit Needed mismatch. Expected: '{}', Actual: '{}'", permit_Needed, copy_permit_Needed,
					e);
			throw e;
		}
	}

	public void verifyCustomfield_Staging_Location() {
		try {
			Assert.assertEquals(StagingLocation.trim(), text_StagingLocation.getText().trim(),
					"❌ Created wrong Job with StagingLocation");
		} catch (AssertionError e) {
			logger.error("❌ Staging Location mismatch. Expected: '{}', Actual: '{}'", StagingLocation,
					text_StagingLocation.getText(), e);
			throw e;
		}
	}

	public void verifyCustomfield_arrivalTimeframe() {
		try {
			Assert.assertEquals(arrivalTimeframe.trim(), text_ArrivalTimeframe.getText().trim(),
					"❌ Created wrong Job with Arrival Time Frame");
		} catch (AssertionError e) {
			logger.error("❌ Arrival Time Frame mismatch. Expected: '{}', Actual: '{}'", arrivalTimeframe,
					text_ArrivalTimeframe.getText(), e);
			throw e;
		}
	}

	public void navigateListingPage_FromJobDetailsPage() {
		try {
			Non_WebDriver_Util.waitForVisible(driver, button_navigateListingPage_FromJobDetailsPage, 5);
			driver.findElement(By.xpath("//a[text()='Jobs']")).click();
		} catch (Exception e) {
			logger.error("❌ Failed to navigate to JOb Listing page job", e);
			throw e; // or Assert.fail("Navigation failed")
		}
	}
//		public void verifyJobScheduledDate() {
//		    try {
//		        String[] expectedTimes = Non_WebDriver_Util.getStartAndEndDateTime(selectJobStartDate, arrivalTimeframe);
//
//		        String actualStart = text_JobScheduledStartTime.getText();
//		        String actualEnd = text_JobScheduledEndTime.getText();
//
//		        Assert.assertEquals(
//		            actualStart.replaceAll("\\s+", "").toUpperCase(),
//		            expectedTimes[0].replaceAll("\\s+", "").toUpperCase(),
//		            "❌ Start Time doesn't match"
//		        );
//		        Assert.assertEquals(
//		            actualEnd.replaceAll("\\s+", "").toUpperCase(),
//		            expectedTimes[1].replaceAll("\\s+", "").toUpperCase(),
//		            "❌ End Time doesn't match"
//		        );
//
//		        logger.info("✅ Verified Scheduled Start Time: {}", expectedTimes[0]);
//		        logger.info("✅ Verified Scheduled End Time: {}", expectedTimes[1]);
//
//		    } catch (AssertionError e) {
//		        logger.error("❌ Scheduled date mismatch. Expected Start: '{}', End: '{}', Actual Start: '{}', End: '{}'",
//		                Non_WebDriver_Util.getStartAndEndDateTime(selectJobStartDate, arrivalTimeframe)[0],
//		                Non_WebDriver_Util.getStartAndEndDateTime(selectJobStartDate, arrivalTimeframe)[1],
//		                text_JobScheduledStartTime.getText(),
//		                text_JobScheduledEndTime.getText(),
//		                e);
//		        throw e;
//		    }
	// }

	public void verifyJobScheduledDate(int jobOffsetDays) {
		try {
			// Get shifted date: original + offset days
			LocalDate baseDate = LocalDate.parse(selectJobStartDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			LocalDate actualDate = baseDate.plusDays(jobOffsetDays);
			String adjustedDateStr = actualDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

			// Calculate expected start/end based on adjusted date
			String[] expectedTimes = Non_WebDriver_Util.getStartAndEndDateTime(adjustedDateStr, arrivalTimeframe);

			String actualStart = text_JobScheduledStartTime.getText().replaceAll("\\s+", "").toUpperCase();
			String actualEnd = text_JobScheduledEndTime.getText().replaceAll("\\s+", "").toUpperCase();

			Assert.assertEquals(actualStart, expectedTimes[0].replaceAll("\\s+", "").toUpperCase(),
					"❌ Start Time doesn't match");
			Assert.assertEquals(actualEnd, expectedTimes[1].replaceAll("\\s+", "").toUpperCase(),
					"❌ End Time doesn't match");

		} catch (AssertionError e) {
			logger.error("❌ Scheduled date mismatch. Expected Start: '{}', End: '{}', Actual Start: '{}', End: '{}'",
					Non_WebDriver_Util.getStartAndEndDateTime(selectJobStartDate, arrivalTimeframe)[0],
					Non_WebDriver_Util.getStartAndEndDateTime(selectJobStartDate, arrivalTimeframe)[1],
					text_JobScheduledStartTime.getText(), text_JobScheduledEndTime.getText(), e);
			throw e;
		}
	}

}
