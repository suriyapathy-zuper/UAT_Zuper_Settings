package RunnerClass;


import org.testng.annotations.AfterClass;
//import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import BaseTest.Baseclass;
import PageObject.CompanyPage;
import PageObject.DashboardPage;
import PageObject.JobCreationPage;
import PageObject.JobDetailsPage;
import PageObject.JobListingPage;
import TestUtility.Non_WebDriver_Util;



public class RK_JobScheduled_Scenario extends Baseclass{

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
		logger.info("\n🔹 Starting Job Creation Test");

		dashboardPage.popup_clear();
		logger.info("✅ Cleared pop-up if present");
       
    }
    
    @DataProvider(name = "jobCreationVariants")
    public Object[][] jobCreationVariants() {
        return new Object[][] {
            // new_JobCreation: uses due date + schedules after creation
            { false, false, true, false },

            // new_JobCreation1: uses start date + assigns user after creation
            { true, true, false, false },

            // new_JobCreation3: uses start date assigned while creation + refresh page after creation
            { true, false, false, true }
        };
    }

    
    @Test(dataProvider = "jobCreationVariants", dependsOnMethods = "verify_CompanyAndLoginPage")
    public void new_JobCreation_Variant(
            boolean useStartDate,
            boolean assignUserAfterCreation,
            boolean scheduleAfterCreation,
            boolean refreshAfterCreation
    ) {
        
    	dashboardPage.navigatToJobListionPage();
    	logger.info("✅ Navigated to job listing page");

    	jobListingPage.naviagtetoJobCreationPage();
    	logger.info("✅ Navigated to job creation page");

    	jobCreationPage.set_JobTitle(prop.getProperty("jobTitle"));
    	logger.info("✅ Job title set: " + prop.getProperty("jobTitle"));

    	jobCreationPage.set_JobCategory(prop.getProperty("jobCategory2"));
    	logger.info("✅ Job category set: " + prop.getProperty("jobCategory2"));

    	jobCreationPage.set_Customer(prop.getProperty("customerName2"));
    	logger.info("✅ Customer set: " + prop.getProperty("customerName2"));


    	if (useStartDate) {
    	    jobCreationPage.set_JobStartdate();
    	    logger.info("✅ Job start date set");
    	} else {
    	    jobCreationPage.set_JobDueDateToNextMonth();
    	    logger.info("✅ Job due date set to next month");
    	}

    	// Only set FE if scheduling is going to happen
    	if (scheduleAfterCreation) {
    	    jobCreationPage.set_FE(prop.getProperty("userName2"));
    	    logger.info("✅ FE set: " + prop.getProperty("userName2"));
    	}
     	if (refreshAfterCreation) {
     		
     		jobCreationPage.set_FE(prop.getProperty("userName2"));
      	    logger.info("✅ FE set: " + prop.getProperty("userName2"));
//    	    Non_WebDriver_Util.waitThread(15);
//    	    logger.info("🕒 Waited for 15 seconds after job creation");
//    	    Non_WebDriver_Util.refreshPage(getDriver());
//    	    logger.info("🔄 Page refreshed after job creation");
    	}

    	jobCreationPage.create_Action();
    	logger.info("✅ Job creation action triggered");

   
    	if (assignUserAfterCreation) {
    	    jobDetailsPage.assigningUserafterCreation(prop.getProperty("userName2"));
    	    logger.info("✅ Assigned user after job creation: " + prop.getProperty("userName2"));
    	}

    	if (scheduleAfterCreation) {
    	
    	    jobDetailsPage.schedulingAfterJobCreation();
    	    logger.info("📅 Scheduled job after creation");
    	}

//    	jobDetailsPage.checkStatus(prop.getProperty("updatedStatusName"));
//    	logger.info("✅ Verified job status: " + prop.getProperty("updatedStatusName"));
    	
  
    	jobDetailsPage.verify_CurrentStatus(prop.getProperty("updatedStatusName"));
    	logger.info("✅ Verified job status: " + prop.getProperty("updatedStatusName"));

    }
   
    
    @AfterClass
    public void setdown() {
    	tearDown();
    }
    
    
}

