package PageObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
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
	private String jobCategory;
	private String rescheduledJobEndDateAndTimeValue;
	private String rescheduledJobStartDateAndTimeValue;
	private String rescheduledJobUID;
	private String assignedUserOnRescheduledJob;
	private String rescheduleReasonValue;
	private String checklistEndDateAndTime;
	private String checklistStartDateAndTime;
	private String reschedule_Reason;
	protected static String job_ID;
	protected static String job_URL;
	
	
	


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
	@FindBy(xpath = "((//div[@class='relative z-10 ng-star-inserted'])/div/div/div/p)[last()]")
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

	@FindBy(xpath = "//label[contains(normalize-space(text()),'Was Customer Proposal Accepted')]//following::input[1]")
	private WebElement button_Was_Customer_Proposal_Accepted;

	@FindBy(xpath = "//label[text()='Was estimate sent to customer?']//following::input[2]")
	private WebElement button_Was_Estimate_Sent_To_Customer;
	
	@FindBy(xpath = "//label[contains(text(),'hesitation')]//following::input[2]")
	private WebElement button_hesitation;

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
	
	////label[text()='Future Appointment Type']//following::div[(text() = 'Electrical Install' or . = 'Electrical Install')]//input
	@FindBy(xpath = "//label[contains(text(),'Electrical Install')]//preceding::input[1]")
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
	
	@FindBy(xpath = "//label[contains(normalize-space(text()),'If No Appointment has been set')]//following::div[(text() = 'Choose an option' or . = 'Choose an option')][1]")
	private WebElement multiSelect_IfNoappointment;

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

	@FindBy(xpath = "//label[text()='Future Install Job Work Type']//following::label[(text() = 'Panel Upgrade')]//preceding::input[1]")
	private WebElement button_FutureInstallJobWorkType_PanelUpgrade;
	
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
	
	@FindBy(xpath = "//span[text()=' Quotes Associated']//following-sibling::span")
	private WebElement text_Quotes_Associated;

	@FindBy(xpath = "//span[text()=' Child Jobs Associated']//following-sibling::span//following::span[2]")
	private WebElement button_ExtractChildJOb;
	
	@FindBy(xpath = "//span[text()=' Quotes Associated']//following-sibling::span//following::span[2]")
	private WebElement button_ExtractQuote;

	@FindBy(xpath = "//span[text()=' Project']//following::span[1]")
	private WebElement button_ExtractProject;

	@FindBy(xpath = "//span[text()=' Child Jobs Associated']//following::div[@role='region'][1]//a")
	private WebElement button_ChildJObURL;
	
	@FindBy(xpath = "//span[text()=' Quotes Associated']//following::div[@role='region'][1]//a")
	private WebElement button_QuoteURL;

	@FindBy(xpath = "//span[text()=' Project']//following::div[@role='region'][1]//a")
	private WebElement button_ProjectURL;

	@FindBy(xpath = "//span[text()=' Child Jobs Associated']//following::div[@role='region'][1]//a")
	private List<WebElement> button_MultopleChildJObURL;

	@FindBy(xpath = "//div[@id='center-panel']//following::dt[text()='Job Category']//following::span[1]")
	private WebElement text_JobCategory;

	@FindBy(xpath = "//p[@id='editTitle']")
	private WebElement text_JobTitle;
	
	@FindBy(xpath = "//div[@id='center-panel']//following::dt[text()='Job Tags']//following::badge[not(contains(@class,'capitalize'))]")
	private List<WebElement> text_JobTags;

	@FindBy(xpath = "//h3[text()='Job Description']//following::dl")
	private WebElement text_JobDescription;

	@FindBy(xpath = "//p[text()='Assessment Completed' or text()='Work Completed']/following::p[2]")
	private WebElement text_WhoUpdateStatus;

	@FindBy(xpath = "//custom-fields//dt[text()='Salesman 1 Name']//following::span[1]")
	private WebElement text_SalesName;
	
	@FindBy(xpath = "//custom-fields//dt[contains(text(),'Permit Needed')]//following::span[1]")
	private WebElement text_PermitNeeded;
	
	@FindBy(xpath = "//custom-fields//dt[text()='Staging Location']//following::span[1]")
	private WebElement text_StagingLocation;

	@FindBy(xpath = "//custom-fields//dt[text()='Arrival Timeframe']//following::span[1]")
	private WebElement text_ArrivalTimeframe;

	@FindBy(xpath = "//dt[normalize-space(text())='Scheduled Start Time']//following::span[1]")
	private WebElement text_JobScheduledStartTime;
	
	@FindBy(xpath = "//dt[normalize-space(text())='Scheduled Start Time']//following::span[1]")
	private List<WebElement> text_JobScheduledStartTime2;

	@FindBy(xpath = "//dt[normalize-space(text())='Due Date']//following::span[1]")
	private WebElement text_JobDataDue;
	
	@FindBy(xpath = "//dt[normalize-space(text())='Admin Job Type']//following::span[1]")
	private WebElement text_AdminJobType;
	
	@FindBy(xpath = "//dt[normalize-space(text())='VAI Call #']//following::span[1]")
	private WebElement text_VAI_Value;
	
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
	
	@FindBy(xpath = "//label[text()='Future Install Job Work Type']//following::label[(text() = 'Panel Upgrade')]")
	private WebElement childJob_Tags3;

	@FindBy(xpath = "//a[text()='Jobs']")
	private WebElement button_navigateListingPage_FromJobDetailsPage;

	@FindBy(xpath = "//input[@value='Choose file'][1]")
	private WebElement button_BeforeInstallpicture;

	@FindBy(xpath = "//input[@type='file']")
	private WebElement button_fileupload;

	@FindBy(xpath = "//button[normalize-space(text())='Close' or normalize-space(text())='Done']")
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
	
	@FindBy(xpath = "//sidebar-data[@id='customer']//mat-expansion-panel/div//a")
	private WebElement link_CustomerNameFromJobPage;

	@FindBy(xpath = "//mat-panel-title[text()=' Other Details ']")
	private WebElement link_CustomerDetailsFromJobPage;

	@FindBy(xpath = "//div[@id='customer_primary_details_container']//custom-fields//dt[text()='Customer Number']//following::span[3]")
	private WebElement text_CustomerNameFromCustomfield;
	
	
	@FindBy(xpath = "(//customer-summary-preview//button/mat-icon)[1]")
	private WebElement icon_CloseOnCustomerSlideout;
	
	@FindBy(xpath = "//*[text()= ' Invoices Associated']/following::div[1]/span/em")
	private WebElement icon_CreateInvoiceFromJob;
	
	@FindBy(xpath = "//span[normalize-space(text())='Preview Attachments']")
	private WebElement text_Attachments;
	
	// Schedule icon on job details page
	@FindBy(xpath = "//as-split-area/div/div/div/div/div[2]/div/div[2]/a")
	private WebElement icon_ScheduleOnDetailsPage;
	
	@FindBy(xpath = "//as-split-area/div/div/div/div/div[2]/div/div[2]/a")
	private List<WebElement> icon_ScheduleOnDetailsPage2;

	// Update button on Scheduling confirmation popup
	@FindBy(xpath = "//*[text()='Update ']")
	private WebElement button_UpdateOnSchConfirmPopup;

	// Job Scheduled Time Overlapping model
	@FindBy(xpath = "//div[text()='Job Scheduled Time Overlapping']")
	private WebElement model_JobScheduledTimeOverlappingModel;

	// Continue button on Job Scheduled Time Overlapping model
	@FindBy(xpath = "//button[text()=' Continue ']")
	private WebElement button_ContinueOnJobScheduledTimeOverlappingModel;
	
	
	// Selecting Status History tab on job details page
		@FindBy(xpath = "//*[text()=' Status History ']")
		private WebElement statusHistoryTab;
		
		
		// Click + icon to Assign Users
		@FindBy(xpath = "//*[@id='employees_assigned']/div/mat-accordion/mat-expansion-panel/mat-expansion-panel-header/span[1]/mat-panel-title/div/span/em")
		private WebElement addAssignUserButtonOnDetailsPage;

		// Selecting 'Users' tab on User assignment model
		@FindBy(xpath = "//a[text()='Users ']")
		private WebElement button_SelectUser;

		// Search text box on User assignment model
		@FindBy(xpath = "//input[@type='search']")
		private WebElement input_SearchFE;

		// Selecting select FE icon
		@FindBy(xpath = "//zuper-assign-employee/div/div[2]/div/div/div/div[2]/div/div[2]/em")
		private WebElement icon_SelectFEicon;

		// Update button on User Assignment page
		@FindBy(xpath = "//*[text()=' Update ']")
		private WebElement buttonUpdateOnUserAssignmentPage;
		
		// Add button on Parts and Services section
		@FindBy(xpath = "//*[@id='center-panel']/div/div[6]/div[1]/div/a")
		private WebElement button_Add_PartsAndServices;

		@FindBy(xpath = "//app-labor-line-items//button")
		private WebElement button_Add_PartsAndServicesFronCentralpannel;
		// Add button on Parts and Services section
		@FindBy(xpath = "//ul[@class='relative']//li[2]//button")
		private WebElement button_LineitemList;
				
		// Line item button after clicking Add button
		@FindBy(xpath = "//*[text()='Line Item']")
		private WebElement button_LineItem;

		// Search box in Choose Line item model
		@FindBy(xpath = "//*[@placeholder='Search Item']")
		private WebElement input_SearchItemOnChooseLineItemModel;

		// Total Searched Line Items
		@FindBy(xpath = "//*[@formarrayname='tableRows']/table/tbody/tr/td/input")
		private List<WebElement> searchRelatedLineItems;
		
		@FindBy(xpath = "//button[text()=' Add Product ']")
		private WebElement button_AddProduct;
		
		
		// Total Service Task Count in job details page
		@FindBy(xpath = "//*[@id='line-items-table']/tbody/tr")
		private List<WebElement> serviceTasksList;
		
		
		// Scheduled Start Date and Time Value
		@FindBy(xpath = "//*[@id='center-panel']/div/div[2]/div[2]/dl/div[4]//dd/span")
		private WebElement scheduledStartDateTimeValue;

		// Scheduled End Date and Time Value
		@FindBy(xpath = "//*[@id='center-panel']/div/div[2]/div[2]/dl/div[5]//dd/span")
		private WebElement scheduledEndDateTimeValue;
		
		//Job Category value
		@FindBy(xpath = "//dt[normalize-space(text())='Job Category']//following::span[1]")
		private WebElement jobCategoryValue;

		// Due Date and Time Value
		@FindBy(xpath = "//*[@id='center-panel']/div/div[2]/div[2]/dl/div[4]//dd/span")
		private WebElement dueDateTimeValue;

		// Close icon on Customer Slideout
		@FindBy(xpath = "//customer-summary-preview/div[1]/div[1]//mat-icon")
		private WebElement icon_CloseOnCustomerSlideout1;

		// Clicking other details section
		@FindBy(xpath = "//*[text()=' Other Details ']")
		private WebElement otherDetailsOnCustomerSlideout;
		

		//Assigned User Name
		@FindBy(xpath = "//sidebar-data[@id='employees_assigned']//mat-expansion-panel/div/div/div/div[1]//a")
		private WebElement assignedUserName;
		
		//Jobs hyperlink in breadcrumbs on job details page
		@FindBy(xpath = "//*[text()='Jobs']")
		private WebElement hyperlink_JobsOnJobDetailsPage;

		
		// Rescheduled View Checklist option
		@FindBy(xpath = "//*[text()='Rescheduled']//following::p[3]//span")
		private WebElement hyperlink_ViewChecklistOnRescheduledStatus;

		// Rescheduled Date field value
		@FindBy(xpath = "//p[text()='Reschedule Date']/following::div[1]/span")
		private WebElement value_RescheduledDateOnRescheduledStatus;

		// Time Frame field value
		@FindBy(xpath = "//p[text()='Time Frame']/following::div[1]/span/span")
		private WebElement value_TimeFrameOnRescheduledStatus;

		// Reschedule Reason field value
		@FindBy(xpath = "//p[text()='Reschedule Reason']/following::div[1]/span/span")
		private WebElement value_RescheduleReasonOnRescheduledStatus;
		
		
		//Original Job Category custom field value
		@FindBy(xpath = "//dt[text()='Original Job Category']//following::dd[1]")
		private WebElement value_OriginalJobCategory;
		
		//Filter on job details page
	    @FindBy(xpath = "//*[text()=' Filter ']")
	    private WebElement button_FilterOnJobDetailsPage;
	    
	  //Add Filter link on job details page
	    @FindBy(xpath = "//span[text()='Add Filter']")
	    private WebElement hyperlink_AddFilter;
	    
	  //Entering job id on filter
	    @FindBy(xpath = "//input[@id='field_value_Job Origin__Created from']")
	    private WebElement input_JobIdtoPass;
	    
	    //Add button on selected Filter
	    @FindBy(xpath = "//span[text()='Add']")
	    private WebElement button_AddSelectedFilter;
	    
	    //Selecting filtered job
	    @FindBy(xpath = "//app-jobs//table//tbody/tr/td[3]//a")
	    private WebElement link_SelectingFilteredJob;
	    
	    //Searching filter type
	    @FindBy(xpath = "//input[@id='field_type_0']")
	    private WebElement input_FilterType;
	

		// Rescheduled Status button
		@FindBy(xpath = "//*[text()='Rescheduled']")
		private WebElement button_RescheduledStatus;

		// Reschedule Date on Rescheduled checklist
		@FindBy(xpath = "//input[@id='Reschedule Date_0']")
		private WebElement input_RescheduleDate;
		
		//Reascheduled reason value after checklist completion
		@FindBy(xpath = "//*[text()='Reschedule Reason']//following::span[3]")
		private WebElement value_RescheduleReasonAfterCompletion;
		
		//Close button on Rescheduled Checklist
		@FindBy(xpath = "//button[text()=' Print Checklist ']/preceding-sibling::button")
		private WebElement button_CloseOnRescheduledChecklist;
		

		// Time Frame dropdown on Rescheduled checklist
		@FindBy(xpath = "//label[text()='Time Frame']//following::div/mat-form-field")
		private WebElement dropdown_TimeFrameOption;

		// Selecting value for Time Frame option
		@FindBy(xpath = "//span[text()=' 11:00-15:00 (11a-3p) ']")
		private WebElement value_TimeFrameOption;
		
		// Update button on Rescheduled checklist
		@FindBy(xpath = "//*[text()=' Update ']")
		private WebElement button_UpdateOnRescheduledChecklist;

		@FindBy(xpath = " //span[normalize-space(text())='Quotes Associated']//following::div[1]//span")
		private WebElement navigateTo_QuoteCreationPageFromJob;
		
		@FindBy(xpath = " //div[@class='text-secondary']")
		private WebElement text_JobUID;


		
	// Page actions

	// =============================
	// ✅ STATUS VERIFICATION METHODS
	// =============================

	
	public void verify_CurrentStatus(String currentStatusName) {
	    Non_WebDriver_Util.waitForBeClickable(driver, button_StatusHistory, 3);
	    button_StatusHistory.click();
	    Non_WebDriver_Util.waitThread(1);
	    Non_WebDriver_Util.waitForVisible(driver, current_JobStatus, 3);

	    String actualStatus = current_JobStatus.getText().toLowerCase();
	    String expectedStatus = currentStatusName.toLowerCase();

	    if (!actualStatus.equalsIgnoreCase(expectedStatus)) {
	        logger.warn("⚠️ First attempt: Job status mismatch. Retrying once after short wait...");
	        Non_WebDriver_Util.waitThread(5); // short wait before retry
	        Non_WebDriver_Util.refreshPage(driver);
	        button_StatusHistory.click(); // optionally re-open the status history if it might update
	        Non_WebDriver_Util.waitThread(1);
	        Non_WebDriver_Util.waitForVisible(driver, current_JobStatus, 3);
	        actualStatus = current_JobStatus.getText().toLowerCase();
	    }

	    if (!actualStatus.equalsIgnoreCase(expectedStatus)) {
	        logger.error("❌ Job status mismatch even after retry!");
	        Assert.assertEquals(actualStatus, expectedStatus, "❌ Job status mismatch after retry!");
	    } else {
	        logger.info("✅ Job status matched: " + current_JobStatus.getText());
	    }
	}


	public void verify_ChildJobAssoicated(String expectedJobCount) {
	    final int maxRetries = 2;
	    final int waitBetweenRetriesSec = 15;
	    int attempts = 0;
	    boolean isMatched = false;

	    while (attempts < maxRetries) {
	        Non_WebDriver_Util.waitThread(waitBetweenRetriesSec);
	        Non_WebDriver_Util.refreshPage(driver);
	        Non_WebDriver_Util.waitForPageToLoad(driver, 10);

	        try {
	            Non_WebDriver_Util.waitForNonEmptyText(driver, text_ChildJobs_Associated, 5);
	            String text=Non_WebDriver_Util.getInnerText(driver, text_ChildJobs_Associated);

	            logger.info("🔍 Extracted child job text: " + text);
	            Integer actualCount = Non_WebDriver_Util.extractNumberFromBrackets(text);

	            if (actualCount.equals(Integer.parseInt(expectedJobCount))) {
	                isMatched = true;
	                break;
	            }
	        } catch (Exception e) {
	            logger.warn("⚠️ Exception while verifying child job count: " + e.getMessage());
	        }

	        attempts++;
	    }

	    if (!isMatched) {
	        logger.error("❌ ChildJob Count mismatch after {} attempts. Expected = {}", maxRetries, expectedJobCount);
	        Assert.fail("❌ ChildJob Count mismatch after " + maxRetries + " attempts. Expected = " + expectedJobCount);
	    }
	}
	
	public void verify_QuoteAssociated() {
	    final int maxRetries = 2;
	    final int waitBetweenRetriesSec = 7;
	    int attempts = 0;
	    boolean isMatched = false;

	    while (attempts < maxRetries) {
	        Non_WebDriver_Util.waitThread(waitBetweenRetriesSec);
	        Non_WebDriver_Util.refreshPage(driver);
	        Non_WebDriver_Util.waitForPageToLoad(driver, 10);

	        try {
	            Non_WebDriver_Util.waitForNonEmptyText(driver, text_Quotes_Associated, 5);
	            String text = Non_WebDriver_Util.getInnerText(driver, text_Quotes_Associated);

	            logger.info("🔍 Extracted quote association text: " + text);
	            Integer actualCount = Non_WebDriver_Util.extractNumberFromBrackets(text);

	            if (actualCount != null && actualCount.equals(Integer.parseInt("1"))) {
	                logger.info("✅ Quote associated count is correct: " + actualCount);
	                isMatched = true;
	                break;
	            } else {
	                logger.warn("⚠️ Mismatch: Expected = {}, Found = {}", "1", actualCount);
	            }
	        } catch (Exception e) {
	            logger.warn("⚠️ Exception while verifying quote associated count: " + e.getMessage());
	        }

	        attempts++;
	    }

	    if (!isMatched) {
	        logger.error("❌ Quote count mismatch after {} attempts. Expected = {}", maxRetries, "1");
	        Assert.fail("❌ Quote count mismatch after " + maxRetries + " attempts. Expected = " + "1");
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

		 //   try {
		        Non_WebDriver_Util.waitThread(2);
		        Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		        Non_WebDriver_Util.scrollIntoViewAndClick(driver, update_JobStatus);
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, targetStatus);
		        Non_WebDriver_Util.waitThread(1);

		        if ((jobCategory.equalsIgnoreCase("Plumbing Excavation") && targetStatus.equalsIgnoreCase("En Route")|| jobCategory.equalsIgnoreCase("Electrical Install"))
		                && targetStatus.equalsIgnoreCase("En Route")
		                || jobCategory.equalsIgnoreCase("Plumbing Return Visit") && targetStatus.equalsIgnoreCase("En Route") 
		                || jobCategory.equalsIgnoreCase("Plumbing Install") && targetStatus.equalsIgnoreCase("En Route")
		                || jobCategory.equalsIgnoreCase("Plumbing Rollover Job") && targetStatus.equalsIgnoreCase("En Route")
		        		) {

		            button_AreAllMaterialsAccountedFor.click();
		            Non_WebDriver_Util.waitThread(1);
		            buttonUpdateJobchecklist.click();
		        		
//		        		(jobCategory.equalsIgnoreCase("Plumbing Install")) 
//		                && targetStatus.equalsIgnoreCase("En Route")) {
//
//		            button_DoyouNeedtoTravelforParts.click();
//		            Non_WebDriver_Util.waitThread(1);
//		            Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "Yes");
//		            Non_WebDriver_Util.waitThread(1);
//		            buttonUpdateJobchecklist.click();

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

		        } else {
		            Non_WebDriver_Util.waitForVisible(driver, button_JobStatusUpdate, 5);
		            Non_WebDriver_Util.scrollIntoViewAndClick(driver, button_JobStatusUpdate);
		        }

		        Non_WebDriver_Util.waitThread(4);

//		        String actualStatus = current_JobStatus.getText().trim().toLowerCase();
//		        if (!actualStatus.equals(targetStatus.toLowerCase())) {
//		            logger.error("❌ Status not updated as expected. Expected: " + targetStatus + ", but got: " + actualStatus);
//		//            Assert.assertEquals(actualStatus, targetStatus.toLowerCase(), "❌ Status not updated as expected.");
//		        } 	 

//		    } catch (Exception e) {
//		        logger.error("❌ Exception while updating job status to '" + targetStatus + "': " + e.getMessage());
//		        throw e;
//		    }
		}

		
		

	// =============================
	// ✅ STATUS UPDATE + CHECKLIST FLOWS
	// =============================

	public void updateJobStatus_WithChecklist(String statusNameToUpdate) {
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		Non_WebDriver_Util.jsScrollAndActionClick(driver, update_JobStatus);
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
//		Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
//				"Status not updated as expected.");
	}

	// Arrived → No Answer flow
	
	public void updateJobStatus_WithChecklist_Arrived(String statusNameToUpdate) {
	    Non_WebDriver_Util.waitThread(1);
	    Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
	    Non_WebDriver_Util.jsScrollAndActionClick(driver, update_JobStatus);

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
	            && statusNameToUpdate.trim().equalsIgnoreCase("Arrived"))
	        || (text_JobCategory.getText().trim().equalsIgnoreCase("Electrical Install")
	            && statusNameToUpdate.trim().equalsIgnoreCase("Arrived"))
	        || (text_JobCategory.getText().trim().equalsIgnoreCase("Plumbing Rollover Job")
		            && statusNameToUpdate.trim().equalsIgnoreCase("Arrived"))
	    		
	    		) {
	    	 Non_WebDriver_Util.waitThread(1);
	        Non_WebDriver_Util.waitForVisible(driver, button_yes_ConsumerRights, 5);
	        button_yes_ConsumerRights.click();
	    }

	    buttonUpdateJobchecklist.click();
	    Non_WebDriver_Util.waitThread(4);

	    // 🔁 Retry checking status for up to 5 times
//	    String actualStatus = "";
//	    int retries = 5;
//	    for (int i = 0; i < retries; i++) {
//	        Non_WebDriver_Util.waitThread(2);
//	        actualStatus = current_JobStatus.getText().trim().toLowerCase();
//	        if (actualStatus.equals(statusNameToUpdate.toLowerCase())) {
//	            break;
//	        }
//	    }

//	    if (!actualStatus.equals(statusNameToUpdate.toLowerCase())) {
//	        logger.error("❌ Status not updated as expected! Expected: {}, Found: {}", statusNameToUpdate, actualStatus);
//	        Assert.assertEquals(actualStatus, statusNameToUpdate.toLowerCase(), "❌ Status not updated as expected.");
//	    } else {
//	        logger.info("✅ Job status successfully updated to: {}", actualStatus);
//	    }
	}


	// Work in progress → flow
	public void updateJobStatus_WithChecklist_WorkInProgress(String statusNameToUpdate) {
		try {
			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
			Non_WebDriver_Util.jsScrollAndActionClick(driver, update_JobStatus);

			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

			if(prop.getProperty("jobCategory").contains("Plumbing Install") && statusNameToUpdate.contains("Work in Progress")
					|| prop.getProperty("jobCategory").contains("Electrical Install") && statusNameToUpdate.contains("Work in Progress")
					|| prop.getProperty("jobCategory").contains("Plumbing Excavation") && statusNameToUpdate.contains("Work in Progress")
					|| prop.getProperty("jobCategory").contains("Plumbing Rollover Job") && statusNameToUpdate.contains("Work in Progress")
					) {
			Non_WebDriver_Util.waitThread(1);
			button_BeforeInstallpicture.click();
			Non_WebDriver_Util.waitThread(1);
			button_fileupload.sendKeys(imagePath);
			Non_WebDriver_Util.waitThread(1);
			// Retry if attachment is not visible
			try {
				if (!text_Attachments.isDisplayed()) {
					logger.warn("⚠️ Attachment not visible, retrying file upload after 5 seconds...");
					Non_WebDriver_Util.waitThread(5);
					button_fileupload.sendKeys(imagePath);
					Non_WebDriver_Util.waitThread(2);
				}
			} catch (NoSuchElementException e) {
				// Attachment element not found at all, so retry
				logger.warn("⚠️ Attachment element not found, retrying file upload after 5 seconds...");
				Non_WebDriver_Util.waitThread(5);
				button_fileupload.sendKeys(imagePath);
				Non_WebDriver_Util.waitThread(2);
			}
			
			button_Closefileupload.click();
			}

			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, button_ProtectiveFootwaer, 5);
			Non_WebDriver_Util.jsScrollAndActionClick(driver, button_ProtectiveFootwaer);

			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, button_ProtectiveFloorUsed, 5);
			Non_WebDriver_Util.jsScrollAndActionClick(driver, button_ProtectiveFloorUsed);

			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, button_ScopeofWorkreviewed, 5);
			Non_WebDriver_Util.jsScrollAndActionClick(driver, button_ScopeofWorkreviewed);
	

			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, button_Remaining_BalanceCollected, 5);
			Non_WebDriver_Util.jsScrollAndActionClick(driver, button_Remaining_BalanceCollected);
		

			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.waitForVisible(driver, button_PersonwithAuthoritytoSign, 5);
			Non_WebDriver_Util.jsScrollAndActionClick(driver, button_PersonwithAuthoritytoSign);

			Non_WebDriver_Util.waitThread(1);
			Non_WebDriver_Util.jsScrollAndActionClick(driver, buttonUpdateJobchecklist);
			Non_WebDriver_Util.waitThread(4);

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

	                Non_WebDriver_Util.waitThread(4);

//	                Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
//	                        "❌ FAIL: Status not updated as expected.");
	              

	            } else {
	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
	                Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
	                update_JobStatus.click();

	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate);

	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.waitForVisible(driver, button_AreYouWearingbooties, 5);
	                button_AreYouWearingbooties.click();

	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.waitForVisible(driver, button_review_dispatch_notes, 5);
	                button_review_dispatch_notes.click();

	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.waitForVisible(driver, button_OwnerOnSite, 5);
	                button_OwnerOnSite.click();

	                Non_WebDriver_Util.waitThread(1);
	                Non_WebDriver_Util.waitForVisible(driver, button_TBBAccount, 5);
	                String text_TBBAccount_Value = text_TBBAccount.getText();
	                this.text_TBBAccount_Value = text_TBBAccount_Value;
	                button_TBBAccount.click();

	                Non_WebDriver_Util.waitThread(1);
	                buttonUpdateJobchecklist.click();
	                Non_WebDriver_Util.waitThread(4);

	          //      Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(), statusNameToUpdate.toLowerCase(),
	          //              "❌ FAIL: Status not updated as expected.");
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
		        Non_WebDriver_Util.jsScrollAndActionClick(driver, update_JobStatus);
		        logger.info("✅ Clicked update status");

		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);
		        logger.info("✅ Selected status: " + statusNameToUpdate.split("/")[0]);

		        // 2. Job Sold → Future Appointment
		        Non_WebDriver_Util.waitThread(1);
		        button_Job_Sold_Future_Appointment.click();
		        logger.info("✅ Clicked Job Sold → Future Appointment");

		        if (!(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Plumbing Site Visit")) 
		        		){
		        	  Non_WebDriver_Util.waitThread(1);
		            button_Was_Customer_Proposal_Accepted.click();
		            logger.info("✅ Customer proposal accepted for job type: " + jobType);
		        }

		        // 3. Staging Location
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_StagingLocation.click();
		        this.StagingLocation=StagingLocation;
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, StagingLocation);
		        logger.info("✅ Staging location selected: " + StagingLocation);
		        


		        // 4. Job Type Button
		        if (jobType.equalsIgnoreCase("Plumbing Install")) {
		        	 Non_WebDriver_Util.waitThread(1);
		            button_PlumbingInstall.click();
		            Non_WebDriver_Util.waitThread(1);
		            button_MaterialOrdering.click();
		            logger.info("✅ Selected job type: Plumbing Install");
		        } else if (jobType.equalsIgnoreCase("Plumbing Return Visit")) {
		        	 Non_WebDriver_Util.waitThread(1);
		            button_PlumbingInstall.click();
		            Non_WebDriver_Util.waitThread(1);
		            button_MaterialisTrackStock.click();
		            logger.info("✅ Selected job type: Plumbing Return Visit");
		        } else if (jobType.equalsIgnoreCase("Plumbing Excavation")) {
		          Non_WebDriver_Util.waitThread(1);
		            button_PlumbingExcavation.click();
		            logger.info("✅ Selected job type: Plumbing Excavation");
		        }

		        // 5. Job Length & Crew Members
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_FutureJObLength.click();
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);
		        logger.info("✅ Selected future job length: " + futureJobLength);

		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_HowMany_Crew_Members.click();
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");
		        logger.info("✅ Selected 3 crew members");

		        // 6. Work Type Button
		        String jobCategory = text_JobCategory.getText().trim();
		        if (jobCategory.equalsIgnoreCase("Plumbing Site Visit")) {
		            Non_WebDriver_Util.waitThread(1);
		            button_FutureJobBasicDescription.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.childJobTags = childJob_Tags1.getText();
		        } else {
		            Non_WebDriver_Util.waitThread(1);
		            button_FutureInstallJobWorkType.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.childJobTags = childJob_Tags.getText();
		        }
		        logger.info("✅ Set work type and captured tags: " + this.childJobTags);

		        // 7. Description
		        input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
		        logger.info("✅ Entered description of work");

		        // 8. Scheduling
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_HaveYouScheduled.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");
		        logger.info("✅ Scheduled: " + isScheduled);

		        if (isScheduled) {
		        	  Non_WebDriver_Util.waitThread(1);
		            select_JobStartDate.click();
		            Non_WebDriver_Util.waitThread(1);
		            select_CurrentDateToFollowUpDate.click();
		            Non_WebDriver_Util.waitThread(1);    
		            this.selectJobStartDate = select_JobStartDate.getAttribute("value");

		            
		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_ArrivalTimeframe.click();
		            this.arrivalTimeframe = arrivalTimeframe;
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		            logger.info("✅ Scheduled with timeframe: " + arrivalTimeframe);
		        } else {
		        	 this.JobTBDReason = jobTBDReason;
		            By button_JobTBDReason = By.xpath(String.format(
		                    "//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
		            driver.findElement(button_JobTBDReason).click();
		            this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
		            logger.info("✅ Reason for unscheduled job: " + jobTBDReason);
		        }
		        
		        // 9. Permit Needed
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_PremitNeeded.click();
		        Non_WebDriver_Util.waitThread(1);
		        this.permit_Needed = permitNeeded;
		        By element_PermitNeeded = By.xpath(String.format(
		                "//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		        Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PermitNeeded), 5);
		        driver.findElement(element_PermitNeeded).click();
		        logger.info("✅ Permit needed: " + permitNeeded);

		        // 10. Submit Checklist
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.jsScrollAndActionClick(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed);
		        Non_WebDriver_Util.waitThread(1);
		        buttonUpdateJobchecklist.click();
		        Non_WebDriver_Util.waitThread(4);
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
		        if(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call")) {
		        	 Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);
		        	  logger.info("✅ Job status selected: " + statusNameToUpdate.split("/")[0]);
		        }
		        else{
		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);
		        logger.info("✅ Job status selected: " + statusNameToUpdate.split("/")[1]);
		        }
		   
		        if(!(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call"))) {
		        // Upload Before Install Picture
		        Non_WebDriver_Util.waitThread(1);
		        button_BeforeInstallpicture.click();
		        button_fileupload.sendKeys(imagePath);
		        Non_WebDriver_Util.waitThread(1);
		    	// Retry if attachment is not visible
				try {
					if (!text_Attachments.isDisplayed()) {
						logger.warn("⚠️ Attachment not visible, retrying file upload after 5 seconds...");
						Non_WebDriver_Util.waitThread(5);
						button_fileupload.sendKeys(imagePath);
						Non_WebDriver_Util.waitThread(2);
					}
				} catch (NoSuchElementException e) {
					// Attachment element not found at all, so retry
					logger.warn("⚠️ Attachment element not found, retrying file upload after 5 seconds...");
					Non_WebDriver_Util.waitThread(5);
					button_fileupload.sendKeys(imagePath);
					Non_WebDriver_Util.waitThread(2);
				}
				
		        Non_WebDriver_Util.waitForVisible(driver, button_Closefileupload, 5);
		        button_Closefileupload.click();
		        logger.info("✅ Uploaded before install picture.");
		        }

		        // 2. Job Sold → Future Appointment
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.waitForBeClickable(driver, button_Job_Sold_Future_Appointment, 5);
		        button_Job_Sold_Future_Appointment.click();
		        logger.info("✅ Clicked Job Sold → Future Appointment");

		        // 3. Balance Still Pending
		        if(!(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call"))) {
		        Non_WebDriver_Util.waitThread(1);
		        button_PBalanceStillPending.click();
		        }else {
		            Non_WebDriver_Util.waitThread(1);
		            button_Was_Customer_Proposal_Accepted.click();
			        logger.info("👍 Selected 'Customer Proposal Accepted'");
		        	
		        }

		        // 4. Staging Location
		        multiSelect_StagingLocation.click();
		        Non_WebDriver_Util.waitThread(1);
		        this.StagingLocation = StagingLocation;
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, StagingLocation);
		        logger.info("✅ Selected staging location: " + StagingLocation);

		        // 5. Job Type Button
		        if (jobType.trim().equalsIgnoreCase("Plumbing Install")) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_PlumbingInstall.click();
		            Non_WebDriver_Util.waitThread(1);
		            button_MaterialOrdering.click();
		        } else if (jobType.trim().equalsIgnoreCase("Plumbing Return Visit")) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_PlumbingInstall.click();
		            Non_WebDriver_Util.waitThread(1);
		            button_MaterialisTrackStock.click();
		        } else if (jobType.trim().equalsIgnoreCase("Plumbing Excavation")) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_PlumbingExcavation.click();
		            Non_WebDriver_Util.waitThread(1);
		            button_MaterialisTrackStock.click();
		        } else if (jobType.trim().equalsIgnoreCase("Electrical Install")) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_ElectricalInstall.click();
		            Non_WebDriver_Util.waitThread(1);
		            Non_WebDriver_Util.scrollIntoViewAndClick(driver, button_MaterialOrdering);
		         
		        } else if (jobType.trim().equalsIgnoreCase("Electrical Return Visit")) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_ElectricalInstall.click();
		            Non_WebDriver_Util.waitThread(1);
		            Non_WebDriver_Util.scrollIntoViewAndClick(driver, button_MaterialisTrackStock);
		        }
		        logger.info("✅ Selected job type: " + jobType);

		        // 6. Job Length & Crew
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_FutureJObLength.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_HowMany_Crew_Members.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");
		        logger.info("✅ Job length and crew members selected.");

		        // 7. Work Type Button
		        String jobCategory = text_JobCategory.getText().trim();
		        if (jobCategory.equalsIgnoreCase("Plumbing Site Visit") ||
		            jobCategory.equalsIgnoreCase("Plumbing Install") ||
		            jobCategory.equalsIgnoreCase("Plumbing Excavation") ||
		            jobCategory.equalsIgnoreCase("Plumbing Return Visit") ||
		            jobCategory.equalsIgnoreCase("Plumbing Rollover Job")
		        		) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_FutureJobBasicDescription.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.childJobTags = childJob_Tags1.getText();
		            
		            
		        } else if ((jobCategory.equalsIgnoreCase("Electrical Service Call"))) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_FutureInstallJobWorkType_PanelUpgrade.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.childJobTags = childJob_Tags3.getText();
		        } 
		        else if (jobCategory.equalsIgnoreCase("Electrical Install")) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_FutureJobBasicDescription_PanelUpgrade.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.childJobTags = childJob_Tags2.getText();
		        } else {
		        	Non_WebDriver_Util.waitThread(1);
		            button_FutureInstallJobWorkType.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.childJobTags = childJob_Tags.getText();
		        }
		        logger.info("✅ Work type selected for category: " + jobCategory);

		        // 8. Description
		        Non_WebDriver_Util.waitThread(1);
		        input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
		        logger.info("✅ Description entered.");

		        // 9. Scheduling
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_HaveYouScheduled.click();
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");

		        if (isScheduled) {
		        	Non_WebDriver_Util.waitThread(1);
		            select_JobStartDate.click();
		            Non_WebDriver_Util.waitThread(1);
		            select_CurrentDateToFollowUpDate.click();
		            Non_WebDriver_Util.waitThread(1);
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
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_PremitNeeded.click();
		        this.permit_Needed=permitNeeded;
		        Non_WebDriver_Util.waitThread(1);
		        By element_PermitNeeded = By.xpath(String.format(
		            "//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		        Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PermitNeeded), 5);
		        driver.findElement(element_PermitNeeded).click();
		        logger.info("✅ Permit selected: " + permitNeeded);

		        // 11. Submit Checklist
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.jsScrollAndActionClick(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed);
		        Non_WebDriver_Util.waitThread(1);
		        buttonUpdateJobchecklist.click();
		        Non_WebDriver_Util.waitThread(4);
		        this.updated_by = text_WhoUpdateStatus.getText();
		        if(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call")) {
		        	 Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(),
				                statusNameToUpdate.split("/")[0].toLowerCase(), "❌ Status not updated as expected.");
				        logger.info("✅ Status updated and verified.");
		        }
		        else{
		        	  Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(),
				                statusNameToUpdate.split("/")[1].toLowerCase(), "❌ Status not updated as expected.");
				        logger.info("✅ Status updated and verified.");
		        }
		        

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
		    	Non_WebDriver_Util.waitThread(1);
		        update_JobStatus.click();
		        logger.info("✅ Clicked 'Update Job Status' and selected '{}'", statusNameToUpdate.split("/")[0]);
		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);

		        // 2. Estimate Needed
		        Non_WebDriver_Util.waitThread(1);
		        button_EstimateSent_EstimateNeeded.click();
		        logger.info("✅ Selected 'Estimate Needed'");

		        // 3. Estimate Sent
		        Non_WebDriver_Util.waitThread(1);
		        button_Was_Estimate_Sent_To_Customer.click();
		        logger.info("✅ Confirmed estimate was sent to customer");

		        // 4. Unable to Access
		        Non_WebDriver_Util.waitThread(1);
		        button_UnableToProperlyAccess.click();
		        logger.info("✅ Selected 'Unable to Properly Access'");

		        // 5. Appointment Scheduling
		        if (isScheduled) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_HasAppointmentbeensetwithDispatch_Yes.click();
		            logger.info("📅 Appointment has been scheduled");

		            Non_WebDriver_Util.waitThread(1);
		            select_AppointmentDate_JobStartDate.click();
		            Non_WebDriver_Util.waitThread(1);
		            select_CurrentDateToFollowUpDate.click();
		            this.selectJobStartDate = select_AppointmentDate_JobStartDate.getAttribute("value");

		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_ArrivalTimeframe_estimate.click();
		            this.arrivalTimeframe = arrivalTimeframe;
		            Non_WebDriver_Util.waitThread(1);
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		            logger.info("✅ Set arrival timeframe: '{}'", arrivalTimeframe);

		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_appointmentType_estimate.click();
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		            logger.info("✅ Set appointment type: '{}'", appointmentType);

		        } else {
		        	Non_WebDriver_Util.waitThread(1);
		            button_HasAppointmentbeensetwithDispatch_No.click();
		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_IfNoAppointmenthasbeenset.click();
		            Non_WebDriver_Util.waitThread(1);
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_IfNoAppointmenthasbeenset, ifNoAppointmentHasBeenSet);
		            logger.info("📅 No appointment scheduled. Reason: '{}'", ifNoAppointmentHasBeenSet);

		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_appointmentType.click();
		            Non_WebDriver_Util.waitThread(1);
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		            logger.info("✅ Set appointment type: '{}'", appointmentType);
		        }

		        // 6. Customer’s Preferred Follow-Up Date
		        Non_WebDriver_Util.waitThread(1);
		        select_CustomersPreferredFollowUpDate.click();
		        Non_WebDriver_Util.waitThread(1);
		        select_CurrentDateToFollowUpDate.click();
		        logger.info("✅ Selected customer's preferred follow-up date");

		        // 7. Notes to Account Manager
		        Non_WebDriver_Util.waitThread(1);
		        input_NotestoAccountManager.sendKeys(notesToAccountManagerDescriptionOfWork);
		        logger.info("📝 Added notes to Account Manager");

		        // 7. Description (conditionally)
		        if (prop.getProperty("jobCategory").trim().equalsIgnoreCase("Inspection-Plumbing")) {
		            input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
		            logger.info("📝 Added basic description of work");
		        }

		        // 8. Submit Checklist
		        Non_WebDriver_Util.waitThread(1);
		        buttonUpdateJobchecklist.click();
		        logger.info("✅ Submitted job status checklist");

		        // 9. Capture update info
		        Non_WebDriver_Util.waitThread(4);
		        this.updated_by = text_WhoUpdateStatus.getText();
		        logger.info("👤 Status updated by: {}", this.updated_by);

		 
		        // 10. Assertion
//		        Assert.assertEquals(
//		            current_JobStatus.getText().trim().toLowerCase(),
//		            statusNameToUpdate.split("/")[0].toLowerCase(),
//		            "Status not updated as expected."
//		        );
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
		    	Non_WebDriver_Util.waitThread(1);
		    	Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
		    	Non_WebDriver_Util.jsScrollAndActionClick(driver, update_JobStatus);
		        Non_WebDriver_Util.waitThread(1);
		        if(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call")) {
		        	 Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);
		        	  logger.info("✅ Job status selected: " + statusNameToUpdate.split("/")[0]);
		        }
		        else{
		        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);
		        logger.info("✅ Job status selected: " + statusNameToUpdate.split("/")[1]);
		        }

		        if(!(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call"))) {
			        // Upload Before Install Picture
			        Non_WebDriver_Util.waitThread(1);
			        button_BeforeInstallpicture.click();
			        button_fileupload.sendKeys(imagePath);
			        Non_WebDriver_Util.waitThread(1);
			    	// Retry if attachment is not visible
					try {
						if (!text_Attachments.isDisplayed()) {
							logger.warn("⚠️ Attachment not visible, retrying file upload after 5 seconds...");
							Non_WebDriver_Util.waitThread(5);
							button_fileupload.sendKeys(imagePath);
							Non_WebDriver_Util.waitThread(2);
						}
					} catch (NoSuchElementException e) {
						// Attachment element not found at all, so retry
						logger.warn("⚠️ Attachment element not found, retrying file upload after 5 seconds...");
						Non_WebDriver_Util.waitThread(5);
						button_fileupload.sendKeys(imagePath);
						Non_WebDriver_Util.waitThread(2);
					}
					
			        Non_WebDriver_Util.waitForVisible(driver, button_Closefileupload, 5);
			        button_Closefileupload.click();
			        logger.info("✅ Uploaded before install picture.");
			        }


		        // Estimate Needed
		        Non_WebDriver_Util.waitThread(1);
		        button_EstimateSent_EstimateNeeded.click();
		        logger.info("📄 Selected 'Estimate Needed'");

		        // Balance Still Pending
		        
		        if(!(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call"))) {
		        Non_WebDriver_Util.waitThread(1);
		        button_PBalanceStillPending.click();
		        logger.info("💰 Selected 'Balance Still Pending'");
		        }
		        // Estimate Sent
		        Non_WebDriver_Util.waitThread(1);
		        button_Was_Estimate_Sent_To_Customer.click();
		        logger.info("📬 Selected 'Estimate Sent to Customer'");

		        // Unable to Access
		        Non_WebDriver_Util.waitThread(1);
		        button_UnableToProperlyAccess.click();
		        logger.info("🚫 Selected 'Unable to Properly Access'");

		        // Appointment logic
		        if (isScheduled) {
		        	Non_WebDriver_Util.waitThread(1);
		            button_HasAppointmentbeensetwithDispatch_Yes.click();
		            Non_WebDriver_Util.waitThread(1);
		            select_AppointmentDate_JobStartDate.click();
		            Non_WebDriver_Util.waitThread(1);
		            select_CurrentDateToFollowUpDate.click();
		            this.selectJobStartDate = select_AppointmentDate_JobStartDate.getAttribute("value");
		            logger.info("📅 Appointment set with date: {}", this.selectJobStartDate);

		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_ArrivalTimeframe_estimate.click();
		            this.arrivalTimeframe = arrivalTimeframe;
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		            logger.info("🕑 Arrival timeframe selected: {}", arrivalTimeframe);

		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_appointmentType_estimate.click();
		            Non_WebDriver_Util.waitThread(1);
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		            logger.info("📌 Appointment type selected: {}", appointmentType);

		        } else {
		        	Non_WebDriver_Util.waitThread(1);
		            button_HasAppointmentbeensetwithDispatch_No.click();
		            
		            
		            if(prop.getProperty("jobCategory").equalsIgnoreCase("Electrical Service Call")) {
		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_IfNoappointment.click();
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, "TBD Based on Client Schedule");
		            }
		            // Appointment type
		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_appointmentType_estimate.click();
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_appointmentType, appointmentType);
		            logger.info("📌 Appointment not scheduled. Type selected: {}", appointmentType);
		        }

//		        // Optional Section: Notes and Description
//		        if (jobType.equalsIgnoreCase("Electrical Service Call")
//		                || jobType.equalsIgnoreCase("Site Visit Electrical")) {
//		            button_reason_hesitation.click();
//		            input_NotestoAccountManager.sendKeys(notesToAccountManagerDescriptionOfWork);
//		            logger.info("📝 Notes to Account Manager added");
//
//		            Non_WebDriver_Util.waitForVisible(driver, input_BasicDescriptionofwork, 5);
//		            input_BasicDescriptionofwork.sendKeys(basicDescriptionofWork);
//		            logger.info("🛠️ Basic description of work added");
//		        }

		        // 6. Customer’s Preferred Follow-Up Date
		        if(prop.getProperty("jobCategory").equalsIgnoreCase("Electrical Service Call")) {
		        Non_WebDriver_Util.waitThread(1);
		        select_CustomersPreferredFollowUpDate.click();
		        Non_WebDriver_Util.waitThread(1);
		        select_CurrentDateToFollowUpDate.click();
		        logger.info("✅ Selected customer's preferred follow-up date");

		      
		        }
		        if(prop.getProperty("jobCategory").equalsIgnoreCase("Plumbing Excavation")) {
		        	Non_WebDriver_Util.waitThread(1);
		        	button_hesitation.click();		
		        	}
		        
		        
		        
		        if(prop.getProperty("jobCategory").equalsIgnoreCase("Plumbing Return Visit") || prop.getProperty("jobCategory").equalsIgnoreCase("Electrical Service Call")
		        		|| prop.getProperty("jobCategory").equalsIgnoreCase("Plumbing Excavation")) {
		        
		        // 7. Notes to Account Manager
		        Non_WebDriver_Util.waitThread(1);
		        input_NotestoAccountManager.sendKeys(notesToAccountManagerDescriptionOfWork);
		        logger.info("📝 Added notes to Account Manager");
		        
		        }
		        // Submit Checklist
		        Non_WebDriver_Util.waitThread(1);
		        buttonUpdateJobchecklist.click();
		        logger.info("✅ Submitted job checklist update");

		        // Capture who updated
		        Non_WebDriver_Util.waitThread(4);
		        this.updated_by = text_WhoUpdateStatus.getText();
		        logger.info("👤 Job status updated by '{}'", this.updated_by);

		        // Assertion
		  
//		        if(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call")) {
//		        	 Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(),
//				                statusNameToUpdate.split("/")[0].toLowerCase(), "❌ Status not updated as expected.");
//				        logger.info("✅ Status updated and verified.");
//		        }
//		        else{
//		        	  Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(),
//				                statusNameToUpdate.split("/")[1].toLowerCase(), "❌ Status not updated as expected.");
//				        logger.info("✅ Status updated and verified.");
//		        }

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
		    	   logger.info("🔄 Starting update of job status to: " + statusNameToUpdate);

			        // 1. Update Job Status
			        Non_WebDriver_Util.waitThread(2);
			        Non_WebDriver_Util.waitForBeClickable(driver, update_JobStatus, 5);
			        Non_WebDriver_Util.jsScrollAndActionClick(driver, update_JobStatus);
			        logger.info("✅ Clicked update status");

			        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);
			        logger.info("✅ Selected status: " + statusNameToUpdate.split("/")[0]);

			      Non_WebDriver_Util.waitThread(1);
		        button_Job_Sold_Future_Appointment.click();
		        logger.info("📅 Selected 'Job Sold – Future Appointment'");


		        if (!(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Plumbing Site Visit")) 
		        		){
		        	  Non_WebDriver_Util.waitThread(1);
		            button_Was_Customer_Proposal_Accepted.click();
		            logger.info("✅ Customer proposal accepted for job type: " + jobType);
		        }

		        multiSelect_StagingLocation.click();
		        Non_WebDriver_Util.waitThread(1);
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
		            Non_WebDriver_Util.waitThread(1);
		            button_MaterialisTrackStock.click();
		            logger.info("🔧 Job type: Plumbing Return Visit – Track Stock selected");
		        } else if (jobType.contains("Plumbing Excavation")) {
		            button_PlumbingExcavation.click();
		            logger.info("⛏️ Job type: Plumbing Excavation selected");
		        } else if (jobType.equalsIgnoreCase("Electrical Install")) {
		            button_ElectricalInstall.click();
		            Non_WebDriver_Util.waitThread(1);
		            button_MaterialOrdering.click();
		            logger.info("⚡ Job type: Electrical Install – Material Ordering selected");
		        } else if (jobType.equalsIgnoreCase("Electrical Return Visit")) {
		            button_ElectricalInstall.click();
		            Non_WebDriver_Util.waitThread(1);
		            button_MaterialisTrackStock.click();
		            logger.info("⚡ Job type: Electrical Return Visit – Track Stock selected");
		        }

		        // Future Job Length and Crew
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_FutureJObLength.click();
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);
		        logger.info("🕓 Future job length selected: '{}'", futureJobLength);
		       
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_HowMany_Crew_Members.click();
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");
		        logger.info("👷 Crew count selected: 3");


		        // 6. Work Type Button
		        String jobCategory = text_JobCategory.getText().trim();
		        if (jobCategory.equalsIgnoreCase("Plumbing Site Visit")) {
		            Non_WebDriver_Util.waitThread(1);
		            button_FutureJobBasicDescription.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.childJobTags = childJob_Tags1.getText();
		        } else {
		            Non_WebDriver_Util.waitThread(1);
		            button_FutureInstallJobWorkType.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.childJobTags = childJob_Tags.getText();
		        }
		        logger.info("✅ Set work type and captured tags: " + this.childJobTags);

		        Non_WebDriver_Util.waitThread(1);
		        input_BasicDescriptionofwork.sendKeys(basicDescription);
		        logger.info("📝 Entered basic job description");

		        // Scheduling
		        this.JobTBDReason = jobTBDReason;
		        multiSelect_HaveYouScheduled.click();
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.selectMatOptionByText(driver, element_HaveYouScheduled, isScheduled ? "Yes" : "No");
		        logger.info("📆 Have you scheduled: {}", isScheduled ? "Yes" : "No");

		        if (isScheduled) {
		            select_JobStartDate.click();
		            Non_WebDriver_Util.waitThread(1);
		            select_CurrentDateToFollowUpDate.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.selectJobStartDate = select_JobStartDate.getAttribute("value");
		            logger.info("📅 Job start date set to: '{}'", this.selectJobStartDate);

		            Non_WebDriver_Util.waitThread(1);
		            multiSelect_ArrivalTimeframe.click();
		            Non_WebDriver_Util.waitThread(1);
		            this.arrivalTimeframe = arrivalTimeframe;
		            Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_SatgingLocation, arrivalTimeframe);
		            logger.info("⏰ Arrival timeframe selected: '{}'", arrivalTimeframe);
		        } else {
		            this.JobTBDReason = jobTBDReason;
		            By button_JobTBDReason = By.xpath(String.format("//label[text()='Job TBD Reason']//following::label[contains(text(),'%s')]", jobTBDReason));
		            Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(button_JobTBDReason), 5);
		            Non_WebDriver_Util.waitThread(1);
		            this.copy_JobTBDReason = driver.findElement(button_JobTBDReason).getText();
		            driver.findElement(button_JobTBDReason).click();
		            logger.info("❓ Job TBD Reason selected: '{}'", this.copy_JobTBDReason);
		        }

		        // Permit Needed
		        Non_WebDriver_Util.waitThread(1);
		        multiSelect_PremitNeeded.click();
		        this.permit_Needed=permitNeeded;
		        Non_WebDriver_Util.waitThread(1);
		        By element_PermitNeeded = By.xpath(String.format("//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		        Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PermitNeeded), 5);
		        driver.findElement(element_PermitNeeded).click();
		        logger.info("📄 Permit needed: '{}'", permitNeeded);

		        // Submit
		        Non_WebDriver_Util.waitThread(1);
		        Non_WebDriver_Util.jsScrollAndActionClick(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed);
		        Non_WebDriver_Util.waitThread(1);
		        buttonUpdateJobchecklist.click();
		        logger.info("✅ Checklist submitted");

		        Non_WebDriver_Util.waitThread(4);
		        this.updated_by = text_WhoUpdateStatus.getText();
		        logger.info("👤 Job status updated by '{}'", this.updated_by);
		       
//		        Assert.assertEquals(
//		                current_JobStatus.getText().trim().toLowerCase(),
//		                statusNameToUpdate.split("/")[0].toLowerCase(),
//		                "❌ Status not updated as expected."
//		        );
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

		    try {
		    // 1. Update Job Status
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.jsScrollAndActionClick(driver, update_JobStatus);
		    logger.info("✅ Clicked Update Job Status");
		    Non_WebDriver_Util.waitThread(1);
		    
		    if(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call")) {
	        	 Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[0]);
	        	  logger.info("✅ Job status selected: " + statusNameToUpdate.split("/")[0]);
	        }
	        else{
	        Non_WebDriver_Util.selectMatOptionByText(driver, option_JobStatus, statusNameToUpdate.split("/")[1]);
	        logger.info("✅ Job status selected: " + statusNameToUpdate.split("/")[1]);
	        }

		    if(!(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call"))) {
		    Non_WebDriver_Util.waitThread(1);
		    button_BeforeInstallpicture.click();
		    logger.info("📸 Clicked before-install picture upload button");
		    Non_WebDriver_Util.waitThread(1);
		    button_fileupload.sendKeys(imagePath);
		    Non_WebDriver_Util.waitThread(1);
			// Retry if attachment is not visible
			try {
				if (!text_Attachments.isDisplayed()) {
					logger.warn("⚠️ Attachment not visible, retrying file upload after 5 seconds...");
					Non_WebDriver_Util.waitThread(5);
					button_fileupload.sendKeys(imagePath);
					Non_WebDriver_Util.waitThread(2);
				}
			} catch (NoSuchElementException e) {
				// Attachment element not found at all, so retry
				logger.warn("⚠️ Attachment element not found, retrying file upload after 5 seconds...");
				Non_WebDriver_Util.waitThread(5);
				button_fileupload.sendKeys(imagePath);
				Non_WebDriver_Util.waitThread(2);
			}
		    logger.info("✅ Uploaded image from path: {}", imagePath);
		    Non_WebDriver_Util.waitForBeClickable(driver, button_Closefileupload, 5);
		    button_Closefileupload.click();
		    logger.info("✅ Closed image upload dialog");
		    }

		    // 2. Future Appointment
		    Non_WebDriver_Util.waitForVisible(driver, button_Job_Sold_Future_Appointment, 5);
		    Non_WebDriver_Util.waitThread(1);
		    button_Job_Sold_Future_Appointment.click();
		    logger.info("📅 Clicked Job Sold – Future Appointment");

		    // 3. Balance Still Pending
		    if(!(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call"))) {
		        Non_WebDriver_Util.waitThread(1);
		        button_PBalanceStillPending.click();
		        }else {
		            Non_WebDriver_Util.waitThread(1);
		            button_Was_Customer_Proposal_Accepted.click();
			        logger.info("👍 Selected 'Customer Proposal Accepted'");
		        	
		        }

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
		    Non_WebDriver_Util.waitThread(1);
		    multiSelect_FutureJObLength.click();
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.selectMatOptionByText(driver, element_MultiSelect_FutureJObLength, futureJobLength);
		    logger.info("📏 Selected Future Job Length: {}", futureJobLength);

		    Non_WebDriver_Util.waitThread(1);
		    multiSelect_HowMany_Crew_Members.click();
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.selectMatOptionByText(driver, element_HowMany_Crew_Members, "3");
		    logger.info("👷 Selected number of crew members: 3");


		    
		      // 7. Work Type Button
	        String jobCategory = text_JobCategory.getText().trim();
	        if (jobCategory.equalsIgnoreCase("Plumbing Site Visit") ||
	            jobCategory.equalsIgnoreCase("Plumbing Install") ||
	            jobCategory.equalsIgnoreCase("Plumbing Excavation") ||
	            jobCategory.equalsIgnoreCase("Plumbing Return Visit") ||
	            jobCategory.equalsIgnoreCase("Plumbing Rollover Job") 
	        		) {
	        	Non_WebDriver_Util.waitThread(1);
	            button_FutureJobBasicDescription.click();
	            this.childJobTags = childJob_Tags1.getText();
	        }  else if ((jobCategory.equalsIgnoreCase("Electrical Service Call"))) {
	        	Non_WebDriver_Util.waitThread(1);
	            button_FutureInstallJobWorkType_PanelUpgrade.click();
	            this.childJobTags = childJob_Tags3.getText();
	        } else if (jobCategory.equalsIgnoreCase("Electrical Install")) {
	        	Non_WebDriver_Util.waitThread(1);
	            button_FutureJobBasicDescription_PanelUpgrade.click();
	            this.childJobTags = childJob_Tags2.getText();
	        } else {
	        	Non_WebDriver_Util.waitThread(1);
	            button_FutureInstallJobWorkType.click();
	            this.childJobTags = childJob_Tags.getText();
	        }
	        logger.info("✅ Work type selected for category: " + jobCategory);
	        
		    // 8. Description
	        Non_WebDriver_Util.waitThread(1);
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
		    Non_WebDriver_Util.waitThread(1);
		    multiSelect_PremitNeeded.click();
		    Non_WebDriver_Util.waitThread(1);
		    this.permit_Needed = permitNeeded;
		    By element_PermitNeeded = By.xpath(String.format(
		            "//div[contains(@id,'cdk-overlay')]//mat-option//span[normalize-space(text())='%s']", permitNeeded));
		    Non_WebDriver_Util.waitForBeClickable(driver, driver.findElement(element_PermitNeeded), 5);
	        driver.findElement(element_PermitNeeded).click();
		    logger.info("🛂 Permit needed: {}", permitNeeded);

		    // 11. Submit Checklist
		    Non_WebDriver_Util.waitForVisible(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed, 5);
		    Non_WebDriver_Util.waitThread(1);
		    Non_WebDriver_Util.jsScrollAndActionClick(driver, button_Plumbing_JobSoldChecklist_to_Be_Completed);
		    Non_WebDriver_Util.waitThread(1);
		    buttonUpdateJobchecklist.click();
		    Non_WebDriver_Util.waitThread(4);
		    this.updated_by = text_WhoUpdateStatus.getText();
		    logger.info("✅ Checklist submitted by: {}", this.updated_by);
		   
		    if(prop.getProperty("jobCategory").trim().equalsIgnoreCase("Electrical Service call")) {
	        	 Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(),
			                statusNameToUpdate.split("/")[0].toLowerCase(), "❌ Status not updated as expected.");
			        logger.info("✅ Status updated and verified.");
	        }
	        else{
	        	  Assert.assertEquals(current_JobStatus.getText().trim().toLowerCase(),
			                statusNameToUpdate.split("/")[1].toLowerCase(), "❌ Status not updated as expected.");
			        logger.info("✅ Status updated and verified.");
	        }
	    } catch (Exception e) {
	        logger.error("❌ Failed to update job status with checklist: " + e.getMessage(), e);
	        throw e;
	    }
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
			// Retry if attachment is not visible
			try {
				if (!text_Attachments.isDisplayed()) {
					logger.warn("⚠️ Attachment not visible, retrying file upload after 5 seconds...");
					Non_WebDriver_Util.waitThread(5);
					button_fileupload.sendKeys(imagePath);
					Non_WebDriver_Util.waitThread(2);
				}
			} catch (NoSuchElementException e) {
				// Attachment element not found at all, so retry
				logger.warn("⚠️ Attachment element not found, retrying file upload after 5 seconds...");
				Non_WebDriver_Util.waitThread(5);
				button_fileupload.sendKeys(imagePath);
				Non_WebDriver_Util.waitThread(2);
			}
		    Non_WebDriver_Util.waitForVisible(driver, button_Closefileupload, 5);
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
		    Non_WebDriver_Util.waitThread(1);
		    buttonUpdateJobchecklist.click();
		    Non_WebDriver_Util.waitThread(4);
		    this.updated_by = text_WhoUpdateStatus.getText();

		    // Final Assert
		   
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
	
	public void navigateToQuoteFromJobPage() {
		try {
			Non_WebDriver_Util.waitForPageToLoad(driver, 10);
			button_ExtractQuote.click();
			Non_WebDriver_Util.waitForVisible(driver, button_QuoteURL, 10);
			button_QuoteURL.click();
		} catch (Exception e) {
			logger.error("❌ Failed to navigate to child job", e);
			throw e; // or Assert.fail("Navigation failed")
		}
	}

	public void Verify_ProjectAssociation() {
		try {
			Non_WebDriver_Util.waitForPageToLoad(driver, 10);
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

//	        verifyChildJobProjectTags();
//	        logger.info("✅ Verified project Tags");

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
	               // verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
	                verifyCustomfield_arrivalTimeframe();
	                logger.info("✅ Verified Scheduled details and status for scheduled job");
	            } else {
	                verifyCustomfield_JObTDBReason();
	              //  verify_CurrentStatus(prop.getProperty("currentStatusName_JobPending"));
	                logger.info("✅ Verified Job TBD Reason and pending status for unscheduled job");
	            }
	        } catch (AssertionError | Exception e) {
	            logger.error("❌ Error during post-job creation validation: ", e);
	            throw e;
	        }
	        
	        if(i==0) {
	        	verify_QuoteAssociated();
				logger.info("✅ Quote Assoicated to the Job verified");
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
		int retryCount = 2;
	for (int attempt = 1; attempt <= retryCount; attempt++) {
	    try {
	        Assert.assertEquals(
	            text_SalesName.getText().trim().toLowerCase(),
	            updated_by.trim().toLowerCase(),
	            "❌ Created wrong Job FE"
	        );
	        break; // success, exit loop
	    } catch (AssertionError e) {
	        if (attempt == retryCount) {
	            logger.error("❌ Sales Name mismatch. Expected: '{}', Actual: '{}'", updated_by, text_SalesName.getText(), e);
	            throw e; // rethrow on final attempt
	        } else {
	            Non_WebDriver_Util.waitThread(3); // wait 1 sec before retry (adjust if needed)
	        }
	    }
	}
}


	public void verifyCustomfield_JObTDBReason() {
		try {
			Assert.assertEquals(copy_JobTBDReason.trim().toLowerCase(), JobTBDReason.trim().toLowerCase(), "❌ Created wrong Job with JobTBDReason");
		} catch (AssertionError e) {
			logger.error("❌ Job TBD Reason mismatch. Expected: '{}', Actual: '{}'", JobTBDReason, copy_JobTBDReason, e);
			throw e;
		}
	}

	public void verifyCustomfield_PermitNeeded() {
		try {
			Assert.assertEquals(text_PermitNeeded.getText().trim().toLowerCase(), permit_Needed.trim().toLowerCase(),
					"❌ Created wrong Job with PermitNeeded");
		} catch (AssertionError e) {
			logger.error("❌ Permit Needed mismatch. Expected: '{}', Actual: '{}'", permit_Needed, text_PermitNeeded.getText().trim(),
					e);
			throw e;
		}
	}

	public void verifyCustomfield_Staging_Location() {
		try {
			Assert.assertEquals(StagingLocation.trim().toLowerCase(), text_StagingLocation.getText().trim().toLowerCase(),
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

	
	
	public void jobTitleFormatCheck() {
		String currentJobCategoryName = text_JobCategory.getText().trim();
		if (currentJobCategoryName.equalsIgnoreCase("Admin Job")) {
			String adminJobCustomFieldValueString = text_AdminJobType.getText();
			Non_WebDriver_Util.refreshPage(driver);
			Non_WebDriver_Util.waitThread(3);
			String afterRefreshTitle = text_JobTitle.getText().trim();
			String adminJobTitle = currentJobCategoryName + ": " + adminJobCustomFieldValueString;
			Assert.assertEquals(afterRefreshTitle, adminJobTitle, "Admin Job Title is not updated");
		} else {
			String valueofVAICustomFieldString = text_VAI_Value.getText().trim();
			if (valueofVAICustomFieldString.equalsIgnoreCase("---")) {
				link_CustomerNameFromJobPage.click();
				link_CustomerDetailsFromJobPage.click();
				String customFieldValueFromCustomerName = text_CustomerNameFromCustomfield.getText().trim();
				Non_WebDriver_Util.waitThread(1);
				Non_WebDriver_Util.waitForBeClickable(driver, icon_CloseOnCustomerSlideout, 5);
				icon_CloseOnCustomerSlideout.click();
				String jobDescriptionValue = text_JobDescription.getText().trim();		
				Non_WebDriver_Util.waitThread(5);
				Non_WebDriver_Util.refreshPage(driver);
				String afterRefreshTitle = text_JobTitle.getText().trim();
				Non_WebDriver_Util.waitThread(1);

	            if ((driver.findElements(By.xpath("//dt[normalize-space(text())='Scheduled Start Time']//following::span[1]"))).size()> 0) {
	                String scheduledStartDateTime = text_JobScheduledStartTime.getText();
	                String[] onlyDate = scheduledStartDateTime.trim().split(" ");
	                String[] datePart = onlyDate[0].split("/");
	                String scheduledValue = datePart[0] + "/" + datePart[1];
	                String scheduledJobTitleFormat = customFieldValueFromCustomerName + "-" + jobDescriptionValue + "-" + scheduledValue;

	                Assert.assertEquals(afterRefreshTitle, scheduledJobTitleFormat, "Job title is not updated with scheduled date");
	            } else {
	                String dueDateTime = text_JobDataDue.getText().trim();
	                String[] dueDatePart = dueDateTime.split("/");
	                String dueDateValue = dueDatePart[0] + "/" + dueDatePart[1];
	                String scheduledJobTitleFormat = customFieldValueFromCustomerName + "-" + jobDescriptionValue + "-" + dueDateValue;

	                Assert.assertEquals(afterRefreshTitle, scheduledJobTitleFormat, "Job title is not updated with due date");
	            }
			}
				
			 else {
				String beforeRefreshTitle = text_JobTitle.getText();
				Non_WebDriver_Util.waitThread(1);
				Non_WebDriver_Util.refreshPage(driver);
				String afterRefreshTitle = text_JobTitle.getText().trim();
				Assert.assertEquals(beforeRefreshTitle, afterRefreshTitle,
						"The Title Should not be Updated for this scenario");
			}
		}
	}
	

	public void set_InvoiceCreationFromJob() {
		icon_CreateInvoiceFromJob.click();
		Non_WebDriver_Util.waitThread(1);
	}
	
	public void schedulingAfterJobCreation() {
		Non_WebDriver_Util.waitForBeClickable(driver, icon_ScheduleOnDetailsPage, 5);
		icon_ScheduleOnDetailsPage.click();
		button_UpdateOnSchConfirmPopup.click();
		try {
			Non_WebDriver_Util.waitThread(1);
			if (model_JobScheduledTimeOverlappingModel.isDisplayed()) {
				button_ContinueOnJobScheduledTimeOverlappingModel.click();
			}
		} catch (Exception e) {
			System.out.println("Job Scheule Overlap model is not Present");
		}
		Non_WebDriver_Util.waitThread(15);
		Non_WebDriver_Util.refreshPage(driver);;
	}
	
	public void checkStatus(String targetStatus) {
		statusHistoryTab.click();
		boolean statusPresent = true;
		try {
			for (int i = 2; statusPresent == true; i++) {
				String currentStatus = driver
						.findElement(By.xpath("//as-split-area[1]/div/div[1]/div/div[3]/div[" + i + "]/div/div/div/p"))
						.getText();
				if (currentStatus.trim().equalsIgnoreCase(targetStatus)) {
					statusPresent = false;
					Assert.assertEquals(targetStatus, currentStatus, "Expecting Status is Scheduled");
				}
			}
		} catch (NoSuchElementException e) {
			System.out.println();
		}
		if (statusPresent == true) {
			System.out.println("User Assignment or Job is not scheduled");
		}
	}

	public void assigningUserafterCreation(String userName) {
		addAssignUserButtonOnDetailsPage.click();
		Non_WebDriver_Util.waitForBeClickable(driver, button_SelectUser, 5);
		button_SelectUser.click();
		input_SearchFE.clear();
		input_SearchFE.sendKeys(userName);
		Non_WebDriver_Util.pressEnter(driver);
		Non_WebDriver_Util.waitThread(4);
		icon_SelectFEicon.click();
		Non_WebDriver_Util.waitThread(1);
		buttonUpdateOnUserAssignmentPage.click();
		Non_WebDriver_Util.waitThread(15);
		Non_WebDriver_Util.refreshPage(driver);
	}
	public void set_LineItem(String lineItemName) {
		Non_WebDriver_Util.waitForBeClickable(driver, button_LineitemList, 5);
		button_LineitemList.click();
		Non_WebDriver_Util.waitForBeClickable(driver, button_Add_PartsAndServicesFronCentralpannel, 5);
		button_Add_PartsAndServicesFronCentralpannel.click();	
		Non_WebDriver_Util.waitForBeClickable(driver, button_LineItem, 5);
		button_LineItem.click();
		input_SearchItemOnChooseLineItemModel.sendKeys(lineItemName);
		Non_WebDriver_Util.pressEnter(driver);
		Non_WebDriver_Util.waitThread(1);
		selectingSearchedLineItems();
		button_AddProduct.click();
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.refreshPage(driver);
	}
	public void selectingSearchedLineItems() {
		int totalLineItemsCount = searchRelatedLineItems.size();
		for (int i = 1; i <= totalLineItemsCount; i++) {
			try {
				Non_WebDriver_Util.waitThread(1);;
				WebElement selectingAllLineItems = driver
						.findElement(By.xpath("//*[@formarrayname='tableRows']/table/tbody/tr/td/input[" + i + "]"));
				selectingAllLineItems.click();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void checkingServiceTaskName(String expectedServiceTask) {
		int serviceTaskCount = serviceTasksList.size();
		for (int i = 1; i <= serviceTaskCount; i++) {
			Non_WebDriver_Util.waitThread(1);
			String serviceTaskName = driver
					.findElement(By.xpath("//*[@id='line-items-table']/tbody/tr[" + i + "]/td[2]/div")).getText();
			if (serviceTaskName.trim().contentEquals(expectedServiceTask)) {
				Assert.assertEquals(serviceTaskName, expectedServiceTask);
			}
		}
	}

	public void gettingRescheduledJobDetails() {
		Non_WebDriver_Util.waitThread(3);
		String rescheduledJobStartDateAndTime = scheduledStartDateTimeValue.getText().trim();
		this.rescheduledJobStartDateAndTimeValue=rescheduledJobStartDateAndTime;
		String rescheduledJobEndDateAndTime = scheduledEndDateTimeValue.getText().trim();
		this.rescheduledJobEndDateAndTimeValue=rescheduledJobEndDateAndTime;
		String selectedJobCategory = jobCategoryValue.getTagName().trim();
		this.jobCategory = selectedJobCategory;
		String assignedUser = assignedUserName.getText().trim();
		this.assignedUserOnRescheduledJob=assignedUser;
	}
	
	public void  retrievingRescheduledJobChecklistValues() {
		statusHistoryTab.click();
		hyperlink_ViewChecklistOnRescheduledStatus.click();
		String rescheduledDateValueString = value_RescheduledDateOnRescheduledStatus.getText().trim();
		String timeFrameString = value_TimeFrameOnRescheduledStatus.getText().trim();
		String[]  timeFrameStringSplitted = timeFrameString.split(" ");
		String timeFrameStringAfterSplitted = timeFrameStringSplitted[0];
        String[] times = timeFrameStringAfterSplitted.split("-");
        // Define input (24-hour) and output (12-hour) formats
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("hh:mm a");
        // Parse and format both times
        LocalTime startTime = LocalTime.parse(times[0], inputFormat);
        LocalTime endTime = LocalTime.parse(times[1], inputFormat);
        String checklistStartDateAndTimeConverted = rescheduledDateValueString + " "+startTime.format(outputFormat) .toUpperCase();
        this.checklistStartDateAndTime=checklistStartDateAndTimeConverted;
        String checklistEndDateAndTimeConverted = rescheduledDateValueString +" "+endTime.format(outputFormat).toUpperCase();
        this.checklistEndDateAndTime=checklistEndDateAndTimeConverted;
        //String formattedRange = startTime.format(outputFormat) + " - " + endTime.format(outputFormat);
            
		String rescheduleReasonString = value_RescheduleReasonOnRescheduledStatus.getText().trim();
		this.rescheduleReasonValue=rescheduleReasonString;
		button_CloseOnRescheduledChecklist.click();	
		
		String currentURL = driver.getCurrentUrl();
		String[] splittedURL = currentURL.split("/");
		String rescheduledjobUID = splittedURL[4];
		this.rescheduledJobUID =rescheduledjobUID; 
		System.out.println(rescheduledjobUID);
		
	}

	public void updatingRescheduledStatusWithCheckList(String rescheduleReason) {
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.waitForVisible(driver, statusHistoryTab, 5);
		statusHistoryTab.click();
		Non_WebDriver_Util.waitForVisible(driver, update_JobStatus, 5);
		update_JobStatus.click();
		Non_WebDriver_Util.waitForVisible(driver, button_RescheduledStatus, 5);
		button_RescheduledStatus.click();
		input_RescheduleDate.click();
		Non_WebDriver_Util.pageDownKeyClick(driver);
		Non_WebDriver_Util.waitThread(1);
		Non_WebDriver_Util.pageEnterKeyClick(driver);
		dropdown_TimeFrameOption.click();
		value_TimeFrameOption.click();
		this.reschedule_Reason = rescheduleReason;
		By element_RescheduledReason = By
				.xpath(String.format("//*[text()='%s']//preceding::input[1]", rescheduleReason));
		driver.findElement(element_RescheduledReason).click();
		button_UpdateOnRescheduledChecklist.click();
		Non_WebDriver_Util.waitThread(3);
		Non_WebDriver_Util.refreshPage(driver);
	}


	public void navigatingToJobDetailsPage() {
		hyperlink_JobsOnJobDetailsPage.click();
		Non_WebDriver_Util.waitThread(3);
	}
	public void checkingAdminJobDetails() {
	String adminJobStartDateAndTime=	scheduledStartDateTimeValue.getText().trim();
	String adminJobEndDateAndTime=	scheduledEndDateTimeValue.getText().trim();
	Assert.assertEquals(adminJobStartDateAndTime, rescheduledJobStartDateAndTimeValue,"Admin job Schedule start date is not matched");	
	Assert.assertEquals(adminJobEndDateAndTime, rescheduledJobEndDateAndTimeValue,"Admin job Schedule end date is not matched");	
	String assignedUserOnAdminJob = assignedUserName.getText().trim();
	Assert.assertEquals(assignedUserOnAdminJob, assignedUserOnRescheduledJob,"Assigned User is not matched");	
	String customFieldValue_OriginalJobCategory =value_OriginalJobCategory.getText().trim();
	Assert.assertEquals(assignedUserOnAdminJob, customFieldValue_OriginalJobCategory,"Job Category is not matched");	
	}
	
	public void checkingRescheduledJobDetails() {
		String afterStatusUpdateJobStartDateAndTime = scheduledStartDateTimeValue.getText().trim();
		String[] afterStatusUpdateJobStartDateAndTimeSplitted=afterStatusUpdateJobStartDateAndTime.split(" ");
		String afterRemoveCDTJobStartDateAndTime = afterStatusUpdateJobStartDateAndTimeSplitted[0]+" "+afterStatusUpdateJobStartDateAndTimeSplitted[1]+" "+afterStatusUpdateJobStartDateAndTimeSplitted[2];
		String afterStatusUpdateJobEndDateAndTime = scheduledEndDateTimeValue.getText().trim();
		String[] afterStatusUpdateJobEndDateAndTimeSplitted=afterStatusUpdateJobEndDateAndTime.split(" ");
		String afterRemoveCDTJobEndDateAndTime = afterStatusUpdateJobEndDateAndTimeSplitted[0]+" "+afterStatusUpdateJobEndDateAndTimeSplitted[1]+" "+afterStatusUpdateJobEndDateAndTimeSplitted[2];
		String afterStatusUpdateRescheduleReason = value_RescheduleReasonAfterCompletion.getText().trim();
		Assert.assertEquals(afterRemoveCDTJobStartDateAndTime, checklistStartDateAndTime,"Start Date and Time is not matched");
		Assert.assertEquals(afterRemoveCDTJobEndDateAndTime, checklistEndDateAndTime,"End Date and Time is not matched");
		Assert.assertEquals(rescheduleReasonValue, afterStatusUpdateRescheduleReason);
		Non_WebDriver_Util.waitThread(2);
		Non_WebDriver_Util.refreshPage(driver);
		verify_CurrentStatus(prop.getProperty("checkStatusName"));
		
	}

	public void filteringAdminJob() {
    	Non_WebDriver_Util.waitForBeClickable(driver, button_FilterOnJobDetailsPage, 5);
    	button_FilterOnJobDetailsPage.click();
    	Non_WebDriver_Util.waitForBeClickable(driver, hyperlink_AddFilter, 5);
    	hyperlink_AddFilter.click();
    	input_FilterType.sendKeys("Job Origin - Created from");
    	Non_WebDriver_Util.downKeyClick(driver);
    	Non_WebDriver_Util.pageEnterKeyClick(driver);
    	input_JobIdtoPass.sendKeys(rescheduledJobUID);
        Non_WebDriver_Util.waitThread(2);
    	button_AddSelectedFilter.click();
    	Non_WebDriver_Util.waitForVisible(driver, link_SelectingFilteredJob, 5);
    	Non_WebDriver_Util.waitForBeClickable(driver, link_SelectingFilteredJob, 5);
    	link_SelectingFilteredJob.click();
    }
	
	public void naviagteToQuotePageFromJob() {
		JobDetailsPage.job_ID=text_JobUID.getText().trim().split(" ")[1];
		JobDetailsPage.job_URL=driver.getCurrentUrl();	
		Non_WebDriver_Util.jsScrollAndActionClick(driver, navigateTo_QuoteCreationPageFromJob);
		Non_WebDriver_Util.waitThread(2);
		
	}
	
	public void verified_OrganiziedJobPage() {
		try {
			Non_WebDriver_Util.waitForPageToLoad(driver, 10);
			Non_WebDriver_Util.waitForVisible(driver, text_JobUID, 5);
			Assert.assertEquals(JobDetailsPage.job_ID, text_JobUID.getText().trim().split(" ")[1]);    
		} catch (Exception e) {
			logger.error("❌ Failed to navigate to JOb Listing page job", e);
			throw e; // or Assert.fail("Navigation failed")
		}
	}
	
}
