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
	private String scopeOfWork;
	private String additionalMaterial;
	private String timeOfComplete;
	private String teamMember;
	


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

	@FindBy(xpath = "//label[contains(text(),'Consumer Rights Form has been Acknowledged')]//following::div[3]//label[text()='Yes']//preceding-sibling::input")
	private WebElement button_yes_ConsumerRights;
	

	@FindBy(xpath = "//label[starts-with(text(),'Terms and Conditions (Disclaimers)')]//following::div[3]//label[text()='Yes']//preceding-sibling::input")
	private WebElement button_Terms_Condition;

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

	
	@FindBy(xpath = "//label[text()='Reason/s for hesitation']//following::label[text()='Price']//preceding::input[@value='Price']")
	private WebElement button_reason_hesitation;
	
	@FindBy(xpath = "//label[contains(text(),'Has Appointment been set')]//following::input[2]")
	private WebElement button_HasAppointmentbeensetwithDispatch_No;

	@FindBy(xpath = "//label[contains(text(),'Has Appointment been set')]//following::input[1]")
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
	
	@FindBy(xpath = "//label[text()='Future Appointment Type']//following::div[(text() = 'Electrical Install' or . = 'Electrical Install')]//input")
	private WebElement button_ElectricalInstall;

	@FindBy(xpath = "//label[text()='Future Appointment Material Needs']//following::div[(text() = 'Material Ordering and Staging through Parts Department is Required' or . ='Material Ordering and Staging through Parts Department is Required')]//input")
	private WebElement button_MaterialOrdering;

	@FindBy(xpath = "//label[text()='Future Appointment Material Needs']//following::div[(text() = 'Material is Truck Stock' or . ='Material is Truck Stock')]//input")
	private WebElement button_MaterialisTrackStock;

	@FindBy(xpath = "//label[text()='Future Job Length']//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_FutureJObLength;
	
	@FindBy(xpath = "//label[text()='Time Needed to Complete Job?']//following::div[1]")
	private WebElement multiSelect_TimeNeededtoCompleteJob;
	
	@FindBy(xpath = "//label[text()='Time Needed to Complete Job?']//following::div[1]//following-sibling::span[1]//span")
	private WebElement value_TimeNeededtoCompleteJob;
	
	@FindBy(xpath = "//label[text()='How Many Guys Needed']//following::div[1]")
	private WebElement multiSelect_GuysNeeded;
	
	@FindBy(xpath = "//label[text()='How Many Guys Needed']//following::div[1]//following-sibling::span[1]//span")
	private WebElement value_GuysNeeded;

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

	@FindBy(xpath = "//label[contains(text(),'How Many Crew')]//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_HowMany_Crew_Members;

	@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	private List<WebElement> element_HowMany_Crew_Members;

	@FindBy(xpath = "//label[text()='Future Install Job Work Type']//following::label[(text() = 'Stack Work')]//preceding::input[1]")
	private WebElement button_FutureInstallJobWorkType;

	@FindBy(xpath = "//label[text()='Future Job Basic Description']//following::label[(text() = 'Stack Work')]//preceding::input[1]")
	private WebElement button_FutureJobBasicDescription;
	
	@FindBy(xpath = "//label[text()='Future Job Basic Description']//following::label[(text() = 'Panel Upgrade')]//preceding::input[1]")
	private WebElement button_FutureJobBasicDescription_PanelUpgrade;

	@FindBy(xpath = "//label[text()='Do you want to send the On the Way Text to the Customer?']//following::div[1]")
	private WebElement button_OntheWayTexttotheCustomer;

	@FindBy(xpath = "//label[text()='Basic Description of Work']/following::input[1]")
	private WebElement input_BasicDescriptionofwork;
	
	@FindBy(xpath = "//label[text()='Rollover Job Scope of Work Needed']//following::div[1]//textarea")
	private WebElement input_RolloverDescriptionofwork;

	@FindBy(xpath = "//textarea[contains(@id,'Notes to Account Manager')]")
	private WebElement input_NotestoAccountManager;

	@FindBy(xpath = "//label[contains(text(),'with Dispatch?')]//following::div[1]")
	private WebElement multiSelect_HaveYouScheduled;
	
	@FindBy(xpath = "//label[contains(text(),'date for rollover job')]//following::div[1]")
	private WebElement multiSelect_HaveYouScheduledForRolloverJobs;

	@FindBy(xpath = "//div[contains(@id,'cdk-overlay')]//mat-option")
	private List<WebElement> element_HaveYouScheduled;

//	    String dynamicXPath = String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]//preceding::input[1]", JobTDBReason);
//	    WebElement button_JobTBDReason = driver.findElement(By.xpath(dynamicXPath));

	@FindBy(xpath = "//label[contains(text(),'Permit Needed')]//following::div[1]")
	private WebElement multiSelect_PremitNeeded;

//	    String xpath = String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permit_Needed);
//	    WebElement element_PremitNeeded = driver.findElement(By.xpath(dynamicXPath));

	@FindBy(xpath = "//label[contains(text(),'Job Sold Checklist to Be Completed')]\r\n"
			+ "    /following::label[normalize-space(text())='Yes' or normalize-space(text())='YES']\r\n"
			+ "    /preceding::input[1]")
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
	
	@FindBy(xpath = "//label[text()='Future Job Basic Description']//following::label[(text() = 'Panel Upgrade')]")
	private WebElement childJob_Tags2;

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
	
	@FindBy(xpath = "//label[text()='Additional Materials Needed?']//following::div[1]//label[text()='No']")
	private WebElement value_AdditionalMaterial;


	// Page actions

	// =============================
	// ✅ STATUS VERIFICATION METHODS
	// =============================

	public void verify_CurrentStatus(String currentStatusName) {
		Non_WebDriver_Util.waitForBeClickable(driver, button_StatusHistory, 3);
		button_StatusHistory.click();
		Non_WebDriver_Util.waitForVisible(driver, current_JobStatus, 3);
		if(!(current_JobStatus.getText().toLowerCase().equalsIgnoreCase(currentStatusName))) {
			logger.error("❌ Job status mismatch!");
		    Assert.assertEquals(currentStatusName.toLowerCase(), current_JobStatus.getText().toLowerCase(),
				"❌ Job status mismatch!");
		}
	}

	public void verify_ChildJobAssoicated(String expectedJobCount) {
	    final int maxRetries = 4;
	    final int waitBetweenRetriesSec = 5;

	    int attempts = 0;
	    boolean isMatched = false;

	    while (attempts < maxRetries) {
	        Non_WebDriver_Util.waitThread(waitBetweenRetriesSec);
	        Non_WebDriver_Util.refreshPage(driver);

	        String text = text_ChildJobs_Associated.getText();
	        Integer actualCount = Non_WebDriver_Util.extractNumberFromBrackets(text);

	        if (actualCount.equals(Integer.parseInt(expectedJobCount))) {
	            isMatched = true;
	            break;
	        }

	        attempts++;
	    }

	    if (!isMatched) {
	    	logger.error("❌ ChildJob Count mismatch!");
	        Assert.fail("❌ ChildJob Count mismatch after " + maxRetries + " attempts. Expected = " + expectedJobCount);

	    }
	}


		public void verify_CustomField() {
		    String actualReason = field_Waiting_Reason.getText().trim();
		    String expectedReason = waitForReason.trim();
		    if (!expectedReason.equals(actualReason)) {
		        logger.error("❌ FAIL: Reason mismatch! Expected: \"" + expectedReason + "\", but found: \"" + actualReason + "\"");
			    Assert.assertEquals(actualReason, expectedReason, "❌ FAIL: Reason mismatch!");
		    } 
	}

	// =============================
	// ✅ BASIC STATUS UPDATE
	// =============================

		public void updateJobStatus(String statusNameToUpdate) {
		    String jobCategory = text_JobCategory.getText().trim();
		    String targetStatus = statusNameToUpdate.trim();

		    try {
		        Non_WebDriver_Util.waitThread(2);
		        Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		        Non_WebDriver_Util.scrollIntoViewAndClick(driver, update_JobStatus);
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, targetStatus);
		        Non_WebDriver_Util.waitThread(1);

		        if ((jobCategory.equalsIgnoreCase("Plumbing Install") || jobCategory.equalsIgnoreCase("Plumbing Return Visit")) 
		                && targetStatus.equalsIgnoreCase("En Route")) {

		            button_DoyouNeedtoTravelforParts.click();
		            Non_WebDriver_Util.waitThread(1);
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "Yes");
		            Non_WebDriver_Util.waitThread(1);
		            buttonUpdateJobchecklist.click();

		        } else if (jobCategory.equalsIgnoreCase("Plumbing Site Visit") && targetStatus.equalsIgnoreCase("En Route")) {

		            Non_WebDriver_Util.waitForBeClickable(driver, button_OntheWayTexttotheCustomer, 5);
		            Non_WebDriver_Util.waitForVisible(driver, button_OntheWayTexttotheCustomer, 5);
		            button_OntheWayTexttotheCustomer.click();
		            Non_WebDriver_Util.waitThread(1);
		            Non_WebDriver_Util.waitForBeClickable(driver, dropdown_FirstOption, 5);
		            Non_WebDriver_Util.waitForVisible(driver, dropdown_FirstOption, 5);
		            dropdown_FirstOption.click();
		            Non_WebDriver_Util.waitThread(1);
		            buttonUpdateJobchecklist.click();

		        } else if ((jobCategory.equalsIgnoreCase("Plumbing Excavation") || jobCategory.equalsIgnoreCase("Electrical Install"))
		                && targetStatus.equalsIgnoreCase("En Route")) {

		            button_AreAllMaterialsAccountedFor.click();
		            Non_WebDriver_Util.waitThread(1);
		            buttonUpdateJobchecklist.click();

		        } else {
		            Non_WebDriver_Util.waitForVisible(driver, button_JobStatusUpdate, 5);
		            Non_WebDriver_Util.scrollIntoViewAndClick(driver, button_JobStatusUpdate);
		        }

		        Non_WebDriver_Util.waitThread(2);

		        String actualStatus = current_JobStatus.getText().trim().toLowerCase();
		        if (!actualStatus.equals(targetStatus.toLowerCase())) {
		            logger.error("❌ Status not updated as expected. Expected: " + targetStatus + ", but got: " + actualStatus);
		            Assert.assertEquals(actualStatus, targetStatus.toLowerCase(), "❌ Status not updated as expected.");
		        } 	 

		    } catch (Exception e) {
		        logger.error("❌ Exception while updating job status to '" + targetStatus + "': " + e.getMessage());
		        throw e;
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

		if ((text_JobCategory.getText().trim().equalsIgnoreCase("Electrical Install")
				&& statusNameToUpdate.trim().equalsIgnoreCase("Arrived"))) {
			Non_WebDriver_Util.waitForVisible(driver, button_Terms_Condition, 5);
			button_Terms_Condition.click();
		}
		if ((text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Install")
				&& statusNameToUpdate.trim().equalsIgnoreCase("Arrived")) ||
			(text_JobCategory.getText().trim().equalsIgnoreCase("Electrical Install")
				&& statusNameToUpdate.trim().equalsIgnoreCase("Arrived"))) {
			Non_WebDriver_Util.waitForVisible(driver, button_yes_ConsumerRights, 5);
			button_yes_ConsumerRights.click();
		}

		buttonUpdateJobchecklist.click();
		Non_WebDriver_Util.waitThread(2);

		String actualStatus = current_JobStatus.getText().trim().toLowerCase();
		if (!actualStatus.equals(statusNameToUpdate.toLowerCase())) {
			logger.error("❌ Status not updated as expected! Expected: {}, Found: {}", statusNameToUpdate, actualStatus);
			Assert.assertEquals(actualStatus, statusNameToUpdate.toLowerCase(), "❌ Status not updated as expected.");
		}

	}


	// Work in progress → flow
	public void updateJobStatus_WithChecklist_WorkInProgress(String statusNameToUpdate) {
		try {
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
			Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
			update_JobStatus.click();

			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

			Non_WebDriver_Util.waitThread(1);
			button_BeforeInstallpicture.click();
			Non_WebDriver_Util.waitThread(1);
			button_fileupload.sendKeys(imagePath);
			Non_WebDriver_Util.waitThread(1);
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

			String actualStatus = current_JobStatus.getText().trim().toLowerCase();
			String expectedStatus = statusNameToUpdate.toLowerCase();
			
			Assert.assertEquals(actualStatus, expectedStatus, "Status not updated as expected.");

		} catch (AssertionError e) {
			logger.error("❌ Assertion Failed: " + e.getMessage());
			throw e; // rethrow to mark test as failed
		} catch (Exception e) {
			logger.error("❌ Unexpected Exception: ", e);
			throw e; // rethrow for handling in the test framework
		}
	}


	// Starting Assessment flow
	 public void updateJobStatus_WithChecklist_StatringAssessment(String statusNameToUpdate) {

	        try {
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
	                button_AreYouWearingbooties.click();

	                Non_WebDriver_Util.waitForBeClickable(driver, dropdown_ReasonforAssessment, 5);
	                dropdown_ReasonforAssessment.click();

	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.waitForVisible(driver, dropdown_FirstOption, 5);
	                dropdown_FirstOption.click();

	                Non_WebDriver_Util.waitThread(1);
	                buttonUpdateJobchecklist.click();

	                Non_WebDriver_Util.waitThread(2);

	                Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
	                        "❌ FAIL: Status not updated as expected.");
	              

	            } else {
	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
	                Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
	                update_JobStatus.click();

	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

	                Non_WebDriver_Util.waitForVisible(driver, button_AreYouWearingbooties, 5);
	                button_AreYouWearingbooties.click();

	                Non_WebDriver_Util.waitForVisible(driver, button_review_dispatch_notes, 5);
	                button_review_dispatch_notes.click();

	                Non_WebDriver_Util.waitForVisible(driver, button_OwnerOnSite, 5);
	                button_OwnerOnSite.click();

	                Non_WebDriver_Util.waitForVisible(driver, button_TBBAccount, 5);
	                String text_TBBAccount_Value = text_TBBAccount.getText();
	                this.text_TBBAccount_Value = text_TBBAccount_Value;
	                button_TBBAccount.click();

	                buttonUpdateJobchecklist.click();
	                Non_WebDriver_Util.waitThread(2);

	                Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
	                        "❌ FAIL: Status not updated as expected.");
	            }
	        } catch (AssertionError e) {
	            logger.error("❌ Assertion failed while updating status to '{}': {}", statusNameToUpdate, e.getMessage());
	            throw e; // Re-throw to mark test as failed
	        } catch (Exception e) {
	            logger.error("❌ Unexpected exception during job status update: {}", e.getMessage(), e);
	            throw new RuntimeException("Job status update failed", e);
	        }
	    }
	 
	 
	 
	 //AssessmentCompleted_GenericJobSoldFuture
	 
	 public void updateJobStatus_WithChecklist_AssessmentCompleted_GenericJobSoldFuture(
		        String jobType,
		        String statusNameToUpdate,
		        String StagingLocation,
		        String futureJobLength,
		        String basicDescriptionofWork,
		        boolean isScheduled,
		        String arrivalTimeframe,
		        String jobTBDReason,
		        String permitNeeded) {

		    try {
		        logger.info("🔄 Starting update of job status to: " + statusNameToUpdate);

		        // 1. Update Job Status
		        Non_WebDriver_Util.waitThread(2);
		        Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		        update_JobStatus.click();
		        logger.info("✅ Clicked update status");

		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);
		        logger.info("✅ Selected status: " + statusNameToUpdate);

		        // 2. Job Sold → Future Appointment
		        button_Job_Sold_Future_Appointment.click();
		        logger.info("✅ Clicked Job Sold → Future Appointment");

		        if (!jobType.trim().equalsIgnoreCase("Plumbing Site Visit")) {
		            button_Was_Customer_Proposal_Accepted.click();
		            logger.info("✅ Customer proposal accepted for job type: " + jobType);
		        }

		        // 3. Staging Location
		        multiSelect_StagingLocation.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, StagingLocation);
		        logger.info("✅ Staging location selected: " + StagingLocation);

		        // 4. Job Type Button
		        if (jobType.equalsIgnoreCase("Plumbing Install")) {
		            button_PlumbingInstall.click();
		            button_MaterialOrdering.click();
		            logger.info("✅ Selected job type: Plumbing Install");
		        } else if (jobType.equalsIgnoreCase("Plumbing Return Visit")) {
		            button_PlumbingInstall.click();
		            button_MaterialisTrackStock.click();
		            logger.info("✅ Selected job type: Plumbing Return Visit");
		        } else if (jobType.equalsIgnoreCase("Plumbing Excavation")) {
		            button_PlumbingExcavation.click();
		            logger.info("✅ Selected job type: Plumbing Excavation");
		        }

		        // 5. Job Length & Crew Members
		        multiSelect_FutureJObLength.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);
		        logger.info("✅ Selected future job length: " + futureJobLength);

		        multiSelect_HowMany_Crew_Members.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");
		        logger.info("✅ Selected 3 crew members");

		        // 6. Work Type Button
		        if (text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Site Visit")) {
		            button_FutureJobBasicDescription.click();
		            this.childJobTags = childJob_Tags1.getText();
		        } else {
		            button_FutureInstallJobWorkType.click();
		            this.childJobTags = childJob_Tags.getText();
		        }
		        logger.info("✅ Set work type and captured tags: " + this.childJobTags);

		        // 7. Description
		        input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
		        logger.info("✅ Entered description of work");

		        // 8. Scheduling
		        multiSelect_HaveYouScheduled.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");
		        logger.info("✅ Scheduled: " + isScheduled);

		        if (isScheduled) {
		            select_JobStartDate.click();
		            select_CurrentDateToFollowUpDate.click();
		            this.selectJobStartDate = select_JobStartDate.getAttribute("value");

		            multiSelect_ArrivalTimeframe.click();
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		            logger.info("✅ Scheduled with timeframe: " + arrivalTimeframe);
		        } else {
		            By button_JobTBDReason = By.xpath(String.format(
		                    "//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
		            driver.findElement(button_JobTBDReason).click();
		            this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
		            logger.info("✅ Reason for unscheduled job: " + jobTBDReason);
		        }

		        // 9. Permit Needed
		        multiSelect_PremitNeeded.click();
		        By element_PermitNeeded = By.xpath(String.format(
		                "//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		        driver.findElement(element_PermitNeeded).click();
		        this.copy_permit_Needed = driver.findElement(element_PermitNeeded).getText();
		        logger.info("✅ Permit needed: " + permitNeeded);

		        // 10. Submit Checklist
		        button_Plumbing_JobSoldChecklist_to_Be_Completed.click();
		        buttonUpdateJobchecklist.click();
		        this.updated_by = text_WhoUpdateStatus.getText();
		        logger.info("✅ Checklist submitted. Updated by: " + updated_by);

		        // Final Assertion
		        Assert.assertEquals(
		                current_JobStatus.getText().trim().toLowerCase(),
		                statusNameToUpdate.split("/")[0].toLowerCase(),
		                "❌ Status not updated as expected.");
		        logger.info("✅ Job status updated and verified successfully.");

		    } catch (Exception e) {
		        logger.error("❌ Failed to update job status to: " + statusNameToUpdate, e);
		        throw e;
		    }
		}

	
// WorkCompleted_GenericJobSoldFuture
	 
	 
	 public void updateJobStatus_WithChecklist_WorkCompleted_GenericJobSoldFuture(String jobType,
		        String statusNameToUpdate, String StagingLocation, String futureJobLength, String basicDescriptionofWork,
		        boolean isScheduled, String arrivalTimeframe, String jobTBDReason, String permitNeeded) {

		    try {
		        logger.info("▶ Updating job status to: " + statusNameToUpdate);

		        // 1. Update Job Status
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		        update_JobStatus.click();
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);

		        logger.info("✅ Job status selected: " + statusNameToUpdate.split("/")[1]);

		        // Upload Before Install Picture
		        Non_WebDriver_Util.waitThread(1);
		        button_BeforeInstallpicture.click();
		        button_fileupload.sendKeys(imagePath);
		        Non_WebDriver_Util.waitThread(1);
		        button_Closefileupload.click();
		        logger.info("✅ Uploaded before install picture.");

		        // 2. Job Sold → Future Appointment
		        Non_WebDriver_Util.waitForBeClickable(driver, button_Job_Sold_Future_Appointment, 5);
		        button_Job_Sold_Future_Appointment.click();
		        logger.info("✅ Clicked Job Sold → Future Appointment");

		        // 3. Balance Still Pending
		        Non_WebDriver_Util.waitThread(1);
		        button_PBalanceStillPending.click();

		        // 4. Staging Location
		        multiSelect_StagingLocation.click();
		        this.StagingLocation = StagingLocation;
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, StagingLocation);
		        logger.info("✅ Selected staging location: " + StagingLocation);

		        // 5. Job Type Button
		        if (jobType.trim().equalsIgnoreCase("Plumbing Install")) {
		            button_PlumbingInstall.click();
		            button_MaterialOrdering.click();
		        } else if (jobType.trim().equalsIgnoreCase("Plumbing Return Visit")) {
		            button_PlumbingInstall.click();
		            button_MaterialisTrackStock.click();
		        } else if (jobType.trim().equalsIgnoreCase("Plumbing Excavation")) {
		            button_PlumbingExcavation.click();
		            button_MaterialisTrackStock.click();
		        } else if (jobType.trim().equalsIgnoreCase("Electrical Install")) {
		            button_ElectricalInstall.click();
		            button_MaterialOrdering.click();
		        } else if (jobType.trim().equalsIgnoreCase("Electrical Return Visit")) {
		            button_ElectricalInstall.click();
		            button_MaterialisTrackStock.click();
		        }
		        logger.info("✅ Selected job type: " + jobType);

		        // 6. Job Length & Crew
		        multiSelect_FutureJObLength.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);
		        multiSelect_HowMany_Crew_Members.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");
		        logger.info("✅ Job length and crew members selected.");

		        // 7. Work Type Button
		        String jobCategory = text_JobCategory.getText().trim();
		        if (jobCategory.equalsIgnoreCase("Plumbing Site Visit") ||
		            jobCategory.equalsIgnoreCase("Plumbing Install") ||
		            jobCategory.equalsIgnoreCase("Plumbing Excavation")) {
		            button_FutureJobBasicDescription.click();
		            this.childJobTags = childJob_Tags1.getText();
		        } else if (jobCategory.equalsIgnoreCase("Electrical Install")) {
		            button_FutureJobBasicDescription_PanelUpgrade.click();
		            this.childJobTags = childJob_Tags2.getText();
		        } else {
		            button_FutureInstallJobWorkType.click();
		            this.childJobTags = childJob_Tags.getText();
		        }
		        logger.info("✅ Work type selected for category: " + jobCategory);

		        // 8. Description
		        input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
		        logger.info("✅ Description entered.");

		        // 9. Scheduling
		        multiSelect_HaveYouScheduled.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");

		        if (isScheduled) {
		            select_JobStartDate.click();
		            select_CurrentDateToFollowUpDate.click();
		            this.selectJobStartDate = select_JobStartDate.getAttribute("value");
		            multiSelect_ArrivalTimeframe.click();
		            this.arrivalTimeframe = arrivalTimeframe;
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		            logger.info("✅ Scheduled date and timeframe set.");
		        } else {
		            this.JobTBDReason = jobTBDReason;
		            By button_JobTBDReason = By.xpath(String.format(
		                "//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
		            driver.findElement(button_JobTBDReason).click();
		            this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
		            logger.info("✅ Job TBD reason selected: " + jobTBDReason);
		        }

		        // 10. Permit Needed
		        multiSelect_PremitNeeded.click();
		        By element_PermitNeeded = By.xpath(String.format(
		            "//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		        driver.findElement(element_PermitNeeded).click();
		        this.copy_permit_Needed = driver.findElement(element_PermitNeeded).getText();
		        logger.info("✅ Permit selected: " + permitNeeded);

		        // 11. Submit Checklist
		        button_Plumbing_JobSoldChecklist_to_Be_Completed.click();
		        buttonUpdateJobchecklist.click();
		        this.updated_by = text_WhoUpdateStatus.getText();

		        Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(),
		                statusNameToUpdate.split("/")[1].toLowerCase(), "❌ Status not updated as expected.");
		        logger.info("✅ Status updated and verified.");

		    } catch (Exception e) {
		        logger.error("❌ Failed to update job status with checklist: " + e.getMessage(), e);
		        throw e;
		    }
		}


	 // AssessmentCompleted_EstimateNeeded_Generic
	
	 public void updateJobStatus_WithChecklist_AssessmentCompleted_EstimateNeeded_Generic(
		        String statusNameToUpdate,
		        boolean isScheduled,
		        String ifNoAppointmentHasBeenSet, // Used if isScheduled == false
		        String arrivalTimeframe, // Used if isScheduled == true
		        String appointmentType,
		        String notesToAccountManagerDescriptionOfWork,
		        String basicDescriptionofWork) {

		    logger.info("🔄 Starting job status update to '{}'", statusNameToUpdate);

		    try {
		        // 1. Update Job Status
		        update_JobStatus.click();
		        logger.info("✅ Clicked 'Update Job Status' and selected '{}'", statusNameToUpdate.split("/")[0]);
		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);

		        // 2. Estimate Needed
		        button_EstimateSent_EstimateNeeded.click();
		        logger.info("✅ Selected 'Estimate Needed'");

		        // 3. Estimate Sent
		        button_Was_Estimate_Sent_To_Customer.click();
		        logger.info("✅ Confirmed estimate was sent to customer");

		        // 4. Unable to Access
		        button_UnableToProperlyAccess.click();
		        logger.info("✅ Selected 'Unable to Properly Access'");

		        // 5. Appointment Scheduling
		        if (isScheduled) {
		            button_HasAppointmentbeensetwithDispatch_Yes.click();
		            logger.info("📅 Appointment has been scheduled");

		            select_AppointmentDate_JobStartDate.click();
		            select_CurrentDateToFollowUpDate.click();
		            this.selectJobStartDate = select_AppointmentDate_JobStartDate.getAttribute("value");

		            multiSelect_ArrivalTimeframe_estimate.click();
		            this.arrivalTimeframe = arrivalTimeframe;
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		            logger.info("✅ Set arrival timeframe: '{}'", arrivalTimeframe);

		            multiSelect_appointmentType_estimate.click();
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		            logger.info("✅ Set appointment type: '{}'", appointmentType);

		        } else {
		            button_HasAppointmentbeensetwithDispatch_No.click();
		            multiSelect_IfNoAppointmenthasbeenset.click();
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_IfNoAppointmenthasbeenset, ifNoAppointmentHasBeenSet);
		            logger.info("📅 No appointment scheduled. Reason: '{}'", ifNoAppointmentHasBeenSet);

		            multiSelect_appointmentType.click();
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		            logger.info("✅ Set appointment type: '{}'", appointmentType);
		        }

		        // 6. Customer’s Preferred Follow-Up Date
		        select_CustomersPreferredFollowUpDate.click();
		        select_CurrentDateToFollowUpDate.click();
		        logger.info("✅ Selected customer's preferred follow-up date");

		        // 7. Notes to Account Manager
		        input_NotestoAccountManager.sendKeys(notesToAccountManagerDescriptionOfWork);
		        logger.info("📝 Added notes to Account Manager");

		        // 7. Description (conditionally)
		        if (prop.getProperty("jobCategory").trim().equalsIgnoreCase("Inspection-Plumbing")) {
		            input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
		            logger.info("📝 Added basic description of work");
		        }

		        // 8. Submit Checklist
		        buttonUpdateJobchecklist.click();
		        logger.info("✅ Submitted job status checklist");

		        // 9. Capture update info
		        this.updated_by = text_WhoUpdateStatus.getText();
		        logger.info("👤 Status updated by: {}", this.updated_by);

		        // 10. Assertion
		        Assert.assertEquals(
		            current_JobStatus.getText().trim().toLowerCase(),
		            statusNameToUpdate.split("/")[0].toLowerCase(),
		            "Status not updated as expected."
		        );
		        logger.info("✅ Job status updated successfully to '{}'", statusNameToUpdate);

		    } catch (Exception e) {
		        logger.error("❌ Failed to update job status to '{}'", statusNameToUpdate, e);
		        throw e;
		    }
		}

	 
	//work Completed
	
	 public void updateJobStatus_WithChecklist_WorkCompleted_EstimateNeeded_Generic(
		        String jobType, String statusNameToUpdate,
		        boolean isScheduled, String ifNoAppointmentHasBeenSet,
		        String arrivalTimeframe, String appointmentType,
		        String basicDescriptionofWork, String notesToAccountManagerDescriptionOfWork) {

		    logger.info("🔄 Starting job status update to '{}'", statusNameToUpdate);

		    try {
		        update_JobStatus.click();
		        logger.info("✅ Clicked 'Update Job Status' and selected '{}'", statusNameToUpdate.split("/")[1]);
		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);

		        // Picture Upload
		        button_BeforeInstallpicture.click();
		        button_fileupload.sendKeys(imagePath);
		        button_Closefileupload.click();
		        logger.info("🖼️ Uploaded job completion picture");

		        // Estimate Needed
		        button_EstimateSent_EstimateNeeded.click();
		        logger.info("📄 Selected 'Estimate Needed'");

		        // Balance Still Pending
		        button_PBalanceStillPending.click();
		        logger.info("💰 Selected 'Balance Still Pending'");

		        // Estimate Sent
		        button_Was_Estimate_Sent_To_Customer.click();
		        logger.info("📬 Selected 'Estimate Sent to Customer'");

		        // Unable to Access
		        button_UnableToProperlyAccess.click();
		        logger.info("🚫 Selected 'Unable to Properly Access'");

		        // Appointment logic
		        if (isScheduled) {
		            button_HasAppointmentbeensetwithDispatch_Yes.click();
		            select_AppointmentDate_JobStartDate.click();
		            select_CurrentDateToFollowUpDate.click();
		            this.selectJobStartDate = select_AppointmentDate_JobStartDate.getAttribute("value");
		            logger.info("📅 Appointment set with date: {}", this.selectJobStartDate);

		            multiSelect_ArrivalTimeframe_estimate.click();
		            this.arrivalTimeframe = arrivalTimeframe;
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		            logger.info("🕑 Arrival timeframe selected: {}", arrivalTimeframe);

		            multiSelect_appointmentType_estimate.click();
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		            logger.info("📌 Appointment type selected: {}", appointmentType);

		        } else {
		            button_HasAppointmentbeensetwithDispatch_No.click();
		            // Appointment type
		            multiSelect_appointmentType_estimate.click();
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		            logger.info("📌 Appointment not scheduled. Type selected: {}", appointmentType);
		        }

		        // Optional Section: Notes and Description
		        if (jobType.equalsIgnoreCase("Electrical Service Call")
		                || jobType.equalsIgnoreCase("Site Visit Electrical")) {
		            button_reason_hesitation.click();
		            input_NotestoAccountManager.sendKeys(notesToAccountManagerDescriptionOfWork);
		            logger.info("📝 Notes to Account Manager added");

		            Non_WebDriver_Util.waitForVisible(driver, input_BasicDescriptionofwork, 5);
		            input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
		            logger.info("🛠️ Basic description of work added");
		        }

		        // Submit Checklist
		        buttonUpdateJobchecklist.click();
		        logger.info("✅ Submitted job checklist update");

		        // Capture who updated
		        this.updated_by = text_WhoUpdateStatus.getText();
		        logger.info("👤 Job status updated by '{}'", this.updated_by);

		        // Assertion
		        Assert.assertEquals(
		                current_JobStatus.getText().trim().toLowerCase(),
		                statusNameToUpdate.split("/")[1].toLowerCase(),
		                "Status not updated as expected."
		        );
		        logger.info("🎯 Job status successfully updated to '{}'", statusNameToUpdate.split("/")[1]);

		    } catch (Exception e) {
		        logger.error("❌ Failed to update job status to '{}'", statusNameToUpdate, e);
		        throw e;
		    }
		}

	

	// Assessment Completed flow for Project With Plumbing Job Types (Install,
	// Return Visit, Excavation)

	 public void updateJobStatus_WithChecklist_AssessmentCompleted_ProjectWith_Plumbing_RollOverJob(
		        String jobType,
		        String statusNameToUpdate,
		        String stagingLocation,
		        String futureJobLength,
		        String basicDescription,
		        boolean isScheduled,
		        String arrivalTimeframe,
		        String jobTBDReason,
		        String permitNeeded) {

		    logger.info("🔄 Updating job status to '{}'", statusNameToUpdate);

		    try {
		        update_JobStatus.click();
		        logger.info("✅ Clicked on 'Update Job Status'");

		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);
		        logger.info("📝 Selected job status: '{}'", statusNameToUpdate.split("/")[0]);

		        button_Job_Sold_Future_Appointment.click();
		        logger.info("📅 Selected 'Job Sold – Future Appointment'");

		        button_Was_Customer_Proposal_Accepted.click();
		        logger.info("👍 Selected 'Customer Proposal Accepted'");

		        multiSelect_StagingLocation.click();
		        this.StagingLocation = stagingLocation;
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, stagingLocation);
		        logger.info("🏗️ Staging location selected: '{}'", stagingLocation);

		        // Job Type Specific Material Selection
		        if (jobType.contains("Plumbing Install")) {
		            button_PlumbingInstall.click();
		            button_MaterialOrdering.click();
		            logger.info("🔧 Job type: Plumbing Install – Material Ordering selected");
		        } else if (jobType.contains("Plumbing Return Visit")) {
		            button_PlumbingInstall.click();
		            button_MaterialisTrackStock.click();
		            logger.info("🔧 Job type: Plumbing Return Visit – Track Stock selected");
		        } else if (jobType.contains("Plumbing Excavation")) {
		            button_PlumbingExcavation.click();
		            logger.info("⛏️ Job type: Plumbing Excavation selected");
		        } else if (jobType.equalsIgnoreCase("Electrical Install")) {
		            button_ElectricalInstall.click();
		            button_MaterialOrdering.click();
		            logger.info("⚡ Job type: Electrical Install – Material Ordering selected");
		        } else if (jobType.equalsIgnoreCase("Electrical Return Visit")) {
		            button_ElectricalInstall.click();
		            button_MaterialisTrackStock.click();
		            logger.info("⚡ Job type: Electrical Return Visit – Track Stock selected");
		        }

		        // Future Job Length and Crew
		        multiSelect_FutureJObLength.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);
		        logger.info("🕓 Future job length selected: '{}'", futureJobLength);

		        multiSelect_HowMany_Crew_Members.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");
		        logger.info("👷 Crew count selected: 3");

		        button_FutureInstallJobWorkType.click();
		        this.childJobTags = childJob_Tags.getText();
		        logger.info("🏷️ Work type selected. Tags: '{}'", this.childJobTags);

		        input_BasicDescriptionofwork.sendKeys(basicDescription);
		        logger.info("📝 Entered basic job description");

		        // Scheduling
		        multiSelect_HaveYouScheduled.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");
		        logger.info("📆 Have you scheduled: {}", isScheduled ? "Yes" : "No");

		        if (isScheduled) {
		            select_JobStartDate.click();
		            select_CurrentDateToFollowUpDate.click();
		            this.selectJobStartDate = select_JobStartDate.getAttribute("value");
		            logger.info("📅 Job start date set to: '{}'", this.selectJobStartDate);

		            multiSelect_ArrivalTimeframe.click();
		            this.arrivalTimeframe = arrivalTimeframe;
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		            logger.info("⏰ Arrival timeframe selected: '{}'", arrivalTimeframe);
		        } else {
		            this.JobTBDReason = jobTBDReason;
		            By button_JobTBDReason = By.xpath(String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
		            Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
		            this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
		            driver.findElement(button_JobTBDReason).click();
		            logger.info("❓ Job TBD Reason selected: '{}'", this.copy_JobTBDReason);
		        }

		        // Permit Needed
		        multiSelect_PremitNeeded.click();
		        this.permit_Needed = permitNeeded;
		        By element_PermitNeeded = By.xpath(String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		        Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PermitNeeded), 5);
		        this.copy_permit_Needed = driver.findElement(element_PermitNeeded).getText();
		        driver.findElement(element_PermitNeeded).click();
		        logger.info("📄 Permit needed: '{}'", this.copy_permit_Needed);

		        // Submit
		        button_Plumbing_JobSoldChecklist_to_Be_Completed.click();
		        buttonUpdateJobchecklist.click();
		        logger.info("✅ Checklist submitted");

		        this.updated_by = text_WhoUpdateStatus.getText();
		        logger.info("👤 Job status updated by '{}'", this.updated_by);

		        Assert.assertEquals(
		                current_JobStatus.getText().trim().toLowerCase(),
		                statusNameToUpdate.split("/")[0].toLowerCase(),
		                "❌ Status not updated as expected."
		        );
		        logger.info("🎯 Status update verified successfully");

		    } catch (Exception e) {
		        logger.error("❌ Error updating job status to '{}': {}", statusNameToUpdate, e.getMessage(), e);
		        throw e;
		    }
		}

	
	// workCompleted
	 public void updateJobStatus_WithChecklist_WorkCompleted_ProjectWith_RollOverJob(String jobType,
		        String statusNameToUpdate, String stagingLocation, String futureJobLength, String basicDescription,
		        boolean isScheduled, String arrivalTimeframe, String jobTBDReason, String permitNeeded) {

		    logger.info("🔄 Updating job status to Work Completed with rollover job...");

		    // 1. Update Job Status
		    update_JobStatus.click();
		    logger.info("✅ Clicked Update Job Status");
		    Non_WebDriver_Util.waitThread(3);
		    Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);
		    logger.info("✅ Selected job status: {}", statusNameToUpdate.split("/")[1]);

		    Non_WebDriver_Util.waitThread(1);
		    button_BeforeInstallpicture.click();
		    logger.info("📸 Clicked before-install picture upload button");
		    Non_WebDriver_Util.waitThread(1);
		    button_fileupload.sendKeys(imagePath);
		    logger.info("✅ Uploaded image from path: {}", imagePath);
		    Non_WebDriver_Util.waitThread(1);
		    button_Closefileupload.click();
		    logger.info("✅ Closed image upload dialog");

		    // 2. Future Appointment
		    Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
		    Non_WebDriver_Util.waitThread(1);
		    button_Job_Sold_Future_Appointment.click();
		    logger.info("📅 Clicked Job Sold – Future Appointment");

		    // 3. Balance Still Pending
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.waitForBeClickable(driver, button_PBalanceStillPending, 5);
		    button_PBalanceStillPending.click();
		    logger.info("💰 Selected Balance Still Pending");

		    // 4. Staging Location
		    multiSelect_StagingLocation.click();
		    Non_WebDriver_Util.waitThread(1);
		    this.StagingLocation = stagingLocation;
		    Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, stagingLocation);
		    logger.info("📦 Selected staging location: {}", stagingLocation);

		    // 5. Job Type & Material
		    if (jobType.contains("Plumbing Install")) {
		        Non_WebDriver_Util.waitForVisible(driver, button_PlumbingInstall, 5);
		        Non_WebDriver_Util.waitThread(1);
		        button_PlumbingInstall.click();
		        logger.info("🛠️ Selected Plumbing Install");

		        Non_WebDriver_Util.waitForVisible(driver, button_MaterialOrdering, 5);
		        Non_WebDriver_Util.waitThread(1);
		        button_MaterialOrdering.click();
		        logger.info("📦 Selected Material Ordering");
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
		    } else if (jobType.contains("Electrical Install")) {
		        Non_WebDriver_Util.waitForVisible(driver, button_ElectricalInstall, 5);
		        button_ElectricalInstall.click();
		        Non_WebDriver_Util.waitForVisible(driver, button_MaterialOrdering, 5);
		        button_MaterialOrdering.click();
		    } else if (jobType.contains("Electrical Return Visit")) {
		        Non_WebDriver_Util.waitForVisible(driver, button_ElectricalInstall, 5);
		        button_ElectricalInstall.click();
		        Non_WebDriver_Util.waitForVisible(driver, button_MaterialisTrackStock, 5);
		        button_MaterialisTrackStock.click();
		    }

		    // 6. Future Job Length & Crew
		    multiSelect_FutureJObLength.click();
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);
		    logger.info("📏 Selected Future Job Length: {}", futureJobLength);

		    multiSelect_HowMany_Crew_Members.click();
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");
		    logger.info("👷 Selected number of crew members: 3");

		    // 7. Work Type
		    if (text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Site Visit")
		            || text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Install")
		            || text_JobCategory.getText().trim().equalsIgnoreCase("Electrical Install")) {
		        Non_WebDriver_Util.waitForVisible(driver, button_FutureJobBasicDescription_PanelUpgrade, 5);
		        Non_WebDriver_Util.waitThread(1);
		        button_FutureJobBasicDescription_PanelUpgrade.click();
		        this.childJobTags = childJob_Tags2.getText();
		        logger.info("🧾 Selected Work Type from Panel Upgrade");
		    } else {
		        Non_WebDriver_Util.waitForVisible(driver, button_FutureInstallJobWorkType, 5);
		        Non_WebDriver_Util.waitThread(1);
		        button_FutureInstallJobWorkType.click();
		        this.childJobTags = childJob_Tags.getText();
		        logger.info("🧾 Selected Work Type (default)");
		    }

		    // 8. Description
		    input_BasicDescriptionofwork.sendKeys(basicDescription);
		    logger.info("📝 Entered job description");

		    // 9. Scheduling
		    multiSelect_HaveYouScheduled.click();
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");
		    logger.info("📅 Scheduling set: {}", isScheduled ? "Yes" : "No");

		    if (isScheduled) {
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.waitForVisible(driver, select_JobStartDate, 5);
		        select_JobStartDate.click();
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
		        select_CurrentDateToFollowUpDate.click();
		        Non_WebDriver_Util.waitThread(1);
		        this.selectJobStartDate = select_JobStartDate.getAttribute("value");

		        multiSelect_ArrivalTimeframe.click();
		        Non_WebDriver_Util.waitThread(1);
		        this.arrivalTimeframe = arrivalTimeframe;
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		        logger.info("⏱️ Scheduled with arrival timeframe: {}", arrivalTimeframe);
		    } else {
		        this.JobTBDReason = jobTBDReason;
		        By button_JobTBDReason = By.xpath(String
		                .format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
		        Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
		        Non_WebDriver_Util.waitThread(1);
		        this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
		        driver.findElement(button_JobTBDReason).click();
		        logger.info("📋 Selected TBD Reason: {}", jobTBDReason);
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
		    logger.info("🛂 Permit needed: {}", permitNeeded);

		    // 11. Submit Checklist
		    Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
		    Non_WebDriver_Util.waitThread(1);
		    button_Plumbing_JobSoldChecklist_to_Be_Completed.click();
		    buttonUpdateJobchecklist.click();
		    this.updated_by = text_WhoUpdateStatus.getText();
		    logger.info("✅ Checklist submitted by: {}", this.updated_by);

		    Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.split("/")[1].toLowerCase(),
		            "❌ Status not updated as expected!");
		    logger.info("🎯 Job status successfully updated to: {}", statusNameToUpdate.split("/")[1]);
		}


	// Rollover Needed
	
	 public void updateJobStatus_WithChecklist_WorkCompleted_GenericRolloverNeeded(
		        String jobType, String statusNameToUpdate, String basicDescriptionofWork,
		        boolean isScheduled, // true = schedule date & timeframe required
		        String arrivalTimeframe) {

		    logger.info("⏳ Starting updateJobStatus_WithChecklist_WorkCompleted_GenericRolloverNeeded...");

		    // 1. Update Job Status
		    logger.info("➡️ Updating job status to: " + statusNameToUpdate);
		    Non_WebDriver_Util.waitThread(2);
		    Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		    Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		    update_JobStatus.click();
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);

		    // Upload picture
		    logger.info("📷 Uploading image...");
		    Non_WebDriver_Util.waitThread(1);
		    button_BeforeInstallpicture.click();
		    Non_WebDriver_Util.waitThread(1);
		    button_fileupload.sendKeys(imagePath);
		    Non_WebDriver_Util.waitThread(1);
		    button_Closefileupload.click();

		    // 2. Rollover Needed
		    logger.info("🔁 Marking 'Rollover Needed'...");
		    Non_WebDriver_Util.waitForBeClickable(driver, button_RolloverNeeded, 5);
		    Non_WebDriver_Util.waitForVisible(driver, button_RolloverNeeded, 5);
		    Non_WebDriver_Util.waitThread(1);
		    button_RolloverNeeded.click();

		    // 3. Scheduling
		    logger.info("📅 Scheduling the rollover job...");
		    multiSelect_HaveYouScheduledForRolloverJobs.click();
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");

		    if (isScheduled) {
		        logger.info("📆 Selecting scheduled date...");
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.waitForVisible(driver, select_Rollover_Appointment_Date, 5);
		        select_Rollover_Appointment_Date.click();
		        Non_WebDriver_Util.waitForVisible(driver, select_CurrentDateToFollowUpDate, 5);
		        Non_WebDriver_Util.waitThread(1);
		        select_CurrentDateToFollowUpDate.click();
		        Non_WebDriver_Util.waitThread(1);
		        this.selectJobStartDate = select_Rollover_Appointment_Date.getAttribute("value");

		        logger.info("🕒 Selecting arrival timeframe: " + arrivalTimeframe);
		        multiSelect_RolloverTimeframe.click();
		        Non_WebDriver_Util.waitThread(1);
		        this.arrivalTimeframe = arrivalTimeframe;
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		    } else {
		        logger.info("⛔ Scheduling skipped (not scheduled).");
		        this.arrivalTimeframe = "";
		    }

		    // 4. Description
		    logger.info("📝 Entering description of work...");
		    input_RolloverDescriptionofwork.sendKeys(basicDescriptionofWork);
		    this.scopeOfWork = input_RolloverDescriptionofwork.getAttribute("value");

		    // 5. Balance Still Pending
		    logger.info("💰 Clicking 'Balance Still Pending'...");
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.waitForBeClickable(driver, button_PBalanceStillPending, 5);
		    button_PBalanceStillPending.click();
		    Non_WebDriver_Util.waitThread(1);

		    // 6. Additional Materials Needed
		    logger.info("📦 Checking if additional materials are needed...");
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.waitForBeClickable(driver, button_AdditionalMaterial, 5);
		    button_AdditionalMaterial.click();
		    Non_WebDriver_Util.waitThread(1);
		    this.additionalMaterial = value_AdditionalMaterial.getText();

		    // 7. Time Needed to Complete Job
		    logger.info("⏱️ Selecting time needed to complete job...");
		    multiSelect_TimeNeededtoCompleteJob.click();
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, "1");
		    Non_WebDriver_Util.waitThread(1);
		    this.timeOfComplete = value_TimeNeededtoCompleteJob.getText();

		    // 8. How Many Guys Needed
		    logger.info("👷 Selecting number of crew members...");
		    multiSelect_GuysNeeded.click();
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");
		    Non_WebDriver_Util.waitThread(1);
		    this.teamMember = value_GuysNeeded.getText();

		    // 9. Description for Electrical Rollover
		    if (jobType.equalsIgnoreCase("Electrical Rollover")) {
		        logger.info("⚡ Additional description entry for Electrical Rollover...");
		        Non_WebDriver_Util.waitThread(1);
		        input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
		    }

		    // 10. Submit
		    logger.info("✅ Submitting checklist...");
		    buttonUpdateJobchecklist.click();
		    this.updated_by = text_WhoUpdateStatus.getText();

		    // Final Assert
		    Non_WebDriver_Util.waitThread(1);
		    logger.info("🔎 Verifying updated status...");
		    Assert.assertEquals(
		            current_JobStatus.getText().trim().toLowerCase(),
		            statusNameToUpdate.split("/")[1].toLowerCase(),
		            "Status not updated as expected.");

		    logger.info("🎉 updateJobStatus_WithChecklist_WorkCompleted_GenericRolloverNeeded completed successfully.");
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

	// navigateToMultipleJobs to verify
	
	public void navigateToMultipleJobs(String twoJobCategory, String basicDescriptionofwork, boolean isScheduled,
	        String stagingLocation) {

	    String[] categoriesArray = twoJobCategory.split("with");
	    List<String> expectedCategories = Arrays.stream(categoriesArray).map(String::trim).collect(Collectors.toList());

	    button_ExtractChildJOb.click();
	    Non_WebDriver_Util.waitThread(2);

	    for (int i = 0; i < expectedCategories.size(); i++) {

	        List<WebElement> childJobLinks = driver.findElements(
	                By.xpath("//span[text()=' Child Jobs Associated']//following::div[@role='region'][1]//a"));

	        if (i >= childJobLinks.size()) {
	            throw new RuntimeException("❌ Not enough child job links for validation");
	        }

	        childJobLinks.get(i).click();
	        Non_WebDriver_Util.waitThread(2);

	        verifyChildJobCategory(expectedCategories.get(i).trim());
	        logger.info("✅ Verified child job category: {}", expectedCategories.get(i).trim());

	        verifyChildJobTags();
	        logger.info("✅ Verified child job Tags");

	        verifyChildJobProjectTags();
	        logger.info("✅ Verified project Tags");

	        verifyChildJobDescription(basicDescriptionofwork);
	        logger.info("✅ Verified child job description: {}", basicDescriptionofwork);

	        Verify_ProjectAssociation();
	        logger.info("✅ Verified Project Association");

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
	                verifyCustomfield_JObTDBReason();
	                verify_CurrentStatus(prop.getProperty("currentStatusName_JobPending"));
	                logger.info("✅ Verified Job TBD Reason and pending status for unscheduled job");
	            }
	        } catch (AssertionError | Exception e) {
	            logger.error("❌ Error during post-job creation validation: ", e);
	            throw e;
	        }

	        logger.info("✅ Associated job {} verified successfully.", i + 1);

	        driver.navigate().back();
	        Non_WebDriver_Util.waitThread(2);
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
	
	private  String getTotalDescription() {
	    return "Scope of work: " + scopeOfWork + "\n" +
	           "Rollover Time Frame: " + arrivalTimeframe + "\n" +
	           "If additional materials are needed: " + additionalMaterial + "\n" +
	           "Time to complete: " + timeOfComplete + "\n" +
	           "Number of team members: " + teamMember;
	}
	
	public void verifyChildJobDescriptionForRolloverNeeded() {
	    String actualDescription = text_JobDescription.getText();
	    String expectedDescription = getTotalDescription();

	    // Keep only alphanumeric characters
	    String sanitizedActual = actualDescription.replaceAll("[^a-zA-Z0-9]", "");
	    String sanitizedExpected = expectedDescription.replaceAll("[^a-zA-Z0-9]", "");

	    try {
	        Assert.assertEquals(sanitizedActual, sanitizedExpected, "❌ Created wrong Job Description");
	    } catch (AssertionError e) {
	        logger.error("❌ Job Description mismatch (alphanumeric only).\nExpected: {}\nActual: {}", sanitizedExpected, sanitizedActual, e);
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
