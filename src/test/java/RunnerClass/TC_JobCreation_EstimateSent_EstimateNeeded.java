package RunnerClass;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import BaseTest.Baseclass;
import PageObject.CompanyPage;
import PageObject.DashboardPage;
import PageObject.JobCreationPage;
import PageObject.JobDetailsPage;
import PageObject.JobListingPage;



public class TC_JobCreation_EstimateSent_EstimateNeeded extends Baseclass{
	
  
	private CompanyPage companyPage;
	private DashboardPage dashboardPage;
	private JobListingPage jobListingPage;
	private JobCreationPage jobCreationPage;
	private JobDetailsPage jobDetailsPage;
	
    @BeforeClass
    public void setUp() {
        initilizeConfig();           //  BaseClass setup
        companyPage = new CompanyPage();
        dashboardPage= new DashboardPage();
        jobListingPage= new JobListingPage();
        jobCreationPage= new JobCreationPage();
        jobDetailsPage= new JobDetailsPage();
   
        }
    @Test
    public void verify_CompanyAndLoginPage() {
        logger.info("🔹 Starting Company and Login Page Verification");

        companyPage.enterCompanyNameDetails(prop.getProperty("company_Name"));
        logger.info("✅ Company name entered: {}", prop.getProperty("company_Name"));

        companyPage.enter_LoginSceanrio(prop.getProperty("username"), prop.getProperty("password"));
        logger.info("✅ Login attempted with username: {}", prop.getProperty("username"));

        logger.info("🎯 Company and login verification completed");
    }

    @Test(dependsOnMethods = "verify_CompanyAndLoginPage")
    public void new_JobCreation() {
        logger.info("\n🔹 Starting Job Creation Test");

        dashboardPage.popup_clear();
        logger.info("✅ Cleared pop-up if present");

        dashboardPage.navigatToJobListionPage();
        logger.info("✅ Navigated to job listing page");

        jobListingPage.naviagtetoJobCreationPage();
        logger.info("✅ Navigated to job creation page");

        jobCreationPage.set_JobTitle(prop.getProperty("jobTitle"));
        logger.info("✅ Set job title: {}", prop.getProperty("jobTitle"));

        jobCreationPage.set_JobCategory(prop.getProperty("jobCategory"));
        logger.info("✅ Set job category: {}", prop.getProperty("jobCategory"));

        jobCreationPage.set_Customer(prop.getProperty("customerName"));
        logger.info("✅ Set customer: {}", prop.getProperty("customerName"));

        jobCreationPage.set_JobDuedate();
        jobCreationPage.set_JobStartdate();
        logger.info("✅ Set job due date and start date");

        jobCreationPage.create_Action();
        logger.info("✅ Job created successfully");

        jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_New_ReadytoAssign"));
        logger.info("✅ Verified status: New - Ready to Assign");

        jobDetailsPage.updateJobStatus(prop.getProperty("scheduledToUpdate"));
        logger.info("➡️ Status updated to: Scheduled");

        jobDetailsPage.updateJobStatus(prop.getProperty("acceptedToUpdate"));
        logger.info("➡️ Status updated to: Accepted");

        jobDetailsPage.updateJobStatus(prop.getProperty("enRouteToUpdate"));
        logger.info("➡️ Status updated to: En Route");

        jobDetailsPage.updateJobStatus_WithChecklist_Arrived(prop.getProperty("ArrivedToUpdate"));
        logger.info("✅ Arrived checklist completed");

        jobDetailsPage.updateJobStatus_WithChecklist_StatringAssessment(prop.getProperty("StartingAssessmentToUpdate"));
        logger.info("✅ Starting assessment checklist completed");

        jobDetailsPage.updateJobStatus_WithChecklist_AssessmentCompleted_EstimateNeeded_Generic(
            prop.getProperty("AssessmentCompletedToUpdate"),
            Boolean.parseBoolean(prop.getProperty("isScheduled")),
            prop.getProperty("ifNoAppointmenthasbeenset"),
            prop.getProperty("arrivalTimeframe"),
            prop.getProperty("appointmentType"),
            prop.getProperty("NotetoAccountManagerescriptionofwork"),
            prop.getProperty("basicDescriptionofwork")
        );
        logger.info("✅ Assessment Completed with Estimate Needed flow executed");

        jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
        logger.info("✅ Verified child job association");

        jobDetailsPage.navigateToChildJOb();
        logger.info("✅ Navigated to child job");

        jobDetailsPage.verifyChildJobCategory(prop.getProperty("childjobcategory"));
        logger.info("✅ Verified child job category: {}", prop.getProperty("childjobcategory"));

        jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
        logger.info("✅ Verified Dispatch Approval Needed status");

        jobDetailsPage.verifyCustomfield_SalesName();
        logger.info("✅ Verified Sales Name custom field");

        if (Boolean.parseBoolean(prop.getProperty("isScheduled"))) {
            jobDetailsPage.verifyJobScheduledDate(0);
            logger.info("✅ Verified scheduled date for job");
            // jobDetailsPage.verifyCustomfield_arrivalTimeframe(); // Uncomment if needed
            // logger.info("✅ Verified arrival timeframe");
        }

        logger.info("🎉 Job creation and validation flow completed successfully");
    }

    @AfterClass
    public void setdown() {
        tearDown();
        logger.info("🧹 Tear down complete");
    }

    
    
}

