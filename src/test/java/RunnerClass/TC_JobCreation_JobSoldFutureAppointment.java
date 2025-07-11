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



public class TC_JobCreation_JobSoldFutureAppointment extends Baseclass{
	
  
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
        logger.info("✅ Entered company name: {}", prop.getProperty("company_Name"));

        companyPage.enter_LoginSceanrio(prop.getProperty("username"), prop.getProperty("password"));
        logger.info("✅ Entered login credentials and submitted");

        logger.info("🎉 Login flow verification completed successfully");
    }

    @Test(dependsOnMethods = "verify_CompanyAndLoginPage")
    public void new_JobCreation() {
        logger.info("🔹 Starting Job Creation Test");

        dashboardPage.popup_clear();
        logger.info("✅ Cleared pop-up if present");

        dashboardPage.navigatToJobListionPage();
        logger.info("✅ Navigated to Job Listing Page");

        jobListingPage.naviagtetoJobCreationPage();
        logger.info("✅ Navigated to Job Creation Page");

        jobCreationPage.set_JobTitle(prop.getProperty("jobTitle"));
        
        
        jobCreationPage.set_JobCategory(prop.getProperty("jobCategory"));
        jobCreationPage.set_Customer(prop.getProperty("customerName"));
        jobCreationPage.set_JobDuedate();
        jobCreationPage.set_JobStartdate();
        logger.info("✅ Set Job details: Title={}, Category={}, Customer={}",
                prop.getProperty("jobTitle"),
                prop.getProperty("jobCategory"),
                prop.getProperty("customerName"));

        jobCreationPage.create_Action();
        logger.info("✅ Job Created Successfully");

        jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_New_ReadytoAssign"));
        logger.info("✅ Verified Status: New - Ready to Assign");

        jobDetailsPage.updateJobStatus(prop.getProperty("scheduledToUpdate"));
        logger.info("✅ Updated Status to: Scheduled");

        jobDetailsPage.updateJobStatus(prop.getProperty("acceptedToUpdate"));
        logger.info("✅ Updated Status to: Accepted");

        jobDetailsPage.updateJobStatus(prop.getProperty("enRouteToUpdate"));
        logger.info("✅ Updated Status to: En Route");

        jobDetailsPage.updateJobStatus_WithChecklist_Arrived(prop.getProperty("ArrivedToUpdate"));
        logger.info("✅ Completed 'Arrived' checklist");

        jobDetailsPage.updateJobStatus_WithChecklist_StatringAssessment(prop.getProperty("StartingAssessmentToUpdate"));
        logger.info("✅ Completed 'Starting Assessment' checklist");

        jobDetailsPage.updateJobStatus_WithChecklist_AssessmentCompleted_GenericJobSoldFuture(
            prop.getProperty("childjobtype"),
            prop.getProperty("AssessmentCompletedToUpdate"),
            prop.getProperty("StagingLocation"),
            prop.getProperty("futureJobLength"),
            prop.getProperty("basicDescriptionofwork"),
            Boolean.parseBoolean(prop.getProperty("isScheduled")),
            prop.getProperty("arrivalTimeframe"),
            prop.getProperty("JobTBDReason"),
            prop.getProperty("permit_Needed")
        );
        logger.info("✅ Completed 'Assessment Completed' with job-sold logic");

        jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
        logger.info("✅ Verified associated child job count");

        jobDetailsPage.navigateToChildJOb();
        logger.info("✅ Navigated to child job");

        jobDetailsPage.verifyChildJobCategory(prop.getProperty("childjobtype"));
        logger.info("✅ Verified child job category: {}", prop.getProperty("childjobtype"));

        jobDetailsPage.verifyChildJobDescription(prop.getProperty("basicDescriptionofwork"));
        logger.info("✅ Verified child job description");

        jobDetailsPage.verifyCustomfield_SalesName();
        logger.info("✅ Verified Sales Name custom field");

        jobDetailsPage.verifyCustomfield_PermitNeeded();
        logger.info("✅ Verified Permit Needed custom field");

        if (prop.getProperty("childjobtype").equalsIgnoreCase("Plumbing Install") ||
            prop.getProperty("childjobtype").equalsIgnoreCase("Plumbing Excavation")) {
            jobDetailsPage.verifyCustomfield_Staging_Location();
            logger.info("✅ Verified Staging Location (Install/Excavation only)");
        }

        if (Boolean.parseBoolean(prop.getProperty("isScheduled"))) {
            jobDetailsPage.verifyJobScheduledDate();
            jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
            jobDetailsPage.verifyCustomfield_arrivalTimeframe();
            logger.info("✅ Verified Scheduled details and status for scheduled job");
        } else {
            jobDetailsPage.verifyCustomfield_JObTDBReason();
            jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_JobPending"));
            logger.info("✅ Verified Job TBD Reason and pending status for unscheduled job");
        }

        logger.info("🎉 Job Creation Test Completed Successfully");
    }

//    @AfterClass
//    public void setdown() {
//        logger.info("🔻 Tearing down test execution");
//        tearDown();
//    }
    
    
}

