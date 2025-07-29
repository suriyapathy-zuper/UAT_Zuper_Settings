package RunnerClass;

import org.testng.annotations.AfterClass;
//import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import BaseTest.Baseclass;
import PageObject.CompanyPage;
import PageObject.DashboardPage;
import PageObject.JobCreationPage;
import PageObject.JobDetailsPage;
import PageObject.JobListingPage;
import TestUtility.Non_WebDriver_Util;

public class RK_JobRescheduled_Scenario extends Baseclass {

	private CompanyPage companyPage;
	private DashboardPage dashboardPage;
	private JobListingPage jobListingPage;
	private JobCreationPage jobCreationPage;
	private JobDetailsPage jobDetailsPage;

	@BeforeClass
	public void setUp() {
		initilizeConfig(); // BaseClass setup
		companyPage = new CompanyPage();
		dashboardPage = new DashboardPage();
		jobListingPage = new JobListingPage();
		jobCreationPage = new JobCreationPage();
		jobDetailsPage = new JobDetailsPage();

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

	@Test(dependsOnMethods = "verify_CompanyAndLoginPage")
	public void new_JobCreation() {
		dashboardPage.navigatToJobListionPage();
		logger.info("✅ Navigated to job listing page");

		jobListingPage.naviagtetoJobCreationPage();
		logger.info("✅ Navigated to job creation page");


		jobCreationPage.set_JobTitle(prop.getProperty("jobTitle"));
		logger.info("✅ Job title set: " + prop.getProperty("jobTitle"));

		jobCreationPage.set_JobCategory(prop.getProperty("jobCategory"));
		logger.info("✅ Job category set: " + prop.getProperty("jobCategory"));

		jobCreationPage.set_FE(prop.getProperty("userName2"));
		logger.info("✅ FE (Field Executive) set: " + prop.getProperty("userName2"));

		jobCreationPage.set_Customer(prop.getProperty("customerName2"));
		logger.info("✅ Customer set: " + prop.getProperty("customerName2"));

		jobCreationPage.set_JobStartdate();
		logger.info("✅ Job start date set");

		jobCreationPage.create_Action();
		logger.info("✅ Job creation action performed");

		jobDetailsPage.gettingRescheduledJobDetails();
		logger.info("🔍 Fetched details of the rescheduled job");

		jobDetailsPage.updatingRescheduledStatusWithCheckList(prop.getProperty("rescheduleReason"));
		logger.info("♻️ Rescheduled job status updated with checklist reason: " + prop.getProperty("rescheduleReason"));

		jobDetailsPage.retrievingRescheduledJobChecklistValues();
		logger.info("✅ Retrieved checklist values after rescheduling");

		jobDetailsPage.checkingRescheduledJobDetails();
		logger.info("🔎 Verified rescheduled job details");

		jobDetailsPage.navigatingToJobDetailsPage();
		logger.info("📄 Navigated to job details page");

		jobDetailsPage.filteringAdminJob();
		logger.info("📂 Filtered admin job from job list");

		jobDetailsPage.checkingAdminJobDetails();
		logger.info("✅ Verified admin job details");
	}
	

    @AfterClass
    public void setdown() {
    	tearDown();
    }

}
