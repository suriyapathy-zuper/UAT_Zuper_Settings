package RunnerClass;


import org.testng.annotations.AfterClass;
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


public class RK_JobServiceTaskAdded_Scenario extends Baseclass{

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
            // Scenario 1: Add line item after job creation
            { false, false },

            // Scenario 2: Add line item during job creation + refresh page before check
            { true, true }
        };
    }

    @Test(dataProvider = "jobCreationVariants", dependsOnMethods = "verify_CompanyAndLoginPage")
    public void new_JobCreation_WithVariants(boolean lineItemInCreation, boolean refreshBeforeCheck) {
      
    	dashboardPage.navigatToJobListionPage();
    	logger.info("✅ Navigated to job listing page");

    	jobListingPage.naviagtetoJobCreationPage();
    	logger.info("✅ Navigated to job creation page");

    	jobCreationPage.set_JobTitle(prop.getProperty("jobTitle"));
    	logger.info("✅ Set job title: " + prop.getProperty("jobTitle"));

    	jobCreationPage.set_JobCategory(prop.getProperty("jobCategory2"));
    	logger.info("✅ Set job category: " + prop.getProperty("jobCategory2"));

    	jobCreationPage.set_Customer(prop.getProperty("customerName2"));
    	logger.info("✅ Set customer: " + prop.getProperty("customerName2"));

    	jobCreationPage.set_JobDuedate();
    	logger.info("✅ Set job due date");

    	jobCreationPage.set_JobDueDateToNextMonth();
    	logger.info("✅ Updated due date to next month");

    	if (lineItemInCreation) {
    	    jobCreationPage.set_LineItem(prop.getProperty("lineItemName"));
    	    logger.info("✅ Set line item during job creation: " + prop.getProperty("lineItemName"));
    	}

    	jobCreationPage.create_Action();
    	logger.info("✅ Clicked Create Job");

    	Non_WebDriver_Util.waitThread(2);

    	if (!lineItemInCreation) {
    	    jobDetailsPage.set_LineItem(prop.getProperty("lineItemName"));
    	    logger.info("✅ Set line item after job creation: " + prop.getProperty("lineItemName"));
    	}

    	if (refreshBeforeCheck) {
    	    Non_WebDriver_Util.refreshPage(getDriver());
    	    logger.info("✅ Page refreshed before verification");
    	}

    	jobDetailsPage.checkingServiceTaskName(prop.getProperty("serviceTaskNameforCMP"));
    	logger.info("✅ Verified service task name: " + prop.getProperty("serviceTaskNameforCMP"));

    }
    
    @AfterClass
    public void setdown() {
    	tearDown();
    }
    
    
}

