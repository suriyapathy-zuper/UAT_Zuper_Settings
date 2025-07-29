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

public class RK_JobTitleScenario extends Baseclass {

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

	@DataProvider(name = "jobCreationVariants")
	public Object[][] jobCreationVariants() {
		return new Object[][] {
			// original new_JobCreation (AdminJobType field set)
	  	{ prop.getProperty("jobCategory3"), false, false, prop.getProperty("AdminJobTypeCustomFieldValue"), null },

			// original new_JobCreation3 (set FE, no custom fields)
			{ prop.getProperty("jobCategory2"), true, false, null, null },

			// original new_JobCreation2 (set FE + VAI field)
			{ prop.getProperty("jobCategory2"), true, false, null, prop.getProperty("customFieldVAIvalue") },

			// original new_JobCreation4 (set FE + start date)
			{ prop.getProperty("jobCategory2"), true, true, null, null }
		};
	}
	
	@Test(dataProvider = "jobCreationVariants", dependsOnMethods = "verify_CompanyAndLoginPage")
	public void new_JobCreation_Variant(
	        String jobCategory,
	        boolean setFE,
	        boolean useStartDate,
	        String adminJobTypeValue,
	        String vaiCustomFieldValue
	) {
	
		dashboardPage.navigatToJobListionPage();
		logger.info("✅ Navigated to job listing page");

		jobListingPage.naviagtetoJobCreationPage();
		logger.info("✅ Navigated to job creation page");

		jobCreationPage.set_JobTitle(prop.getProperty("jobTitle"));
		logger.info("✅ Job title set: " + prop.getProperty("jobTitle"));

		jobCreationPage.set_Customer(prop.getProperty("customerName2"));
		logger.info("✅ Customer set: " + prop.getProperty("customerName2"));

		jobCreationPage.set_JobCategory(jobCategory);
		logger.info("✅ Job category set: " + jobCategory);

		if (setFE) {
			jobCreationPage.set_FE(prop.getProperty("userName2"));
			logger.info("✅ FE set: " + prop.getProperty("userName2"));
		}

		if (useStartDate) {
			jobCreationPage.set_JobStartdate();
			logger.info("✅ Job start date set");
		} else {
			jobCreationPage.set_JobDueDateToNextMonth();
			logger.info("✅ Job due date set to next month");
		}

		if (adminJobTypeValue != null) {
			jobCreationPage.set_updatingAdminJobTypeCustomField(adminJobTypeValue);
			logger.info("✅ Admin Job Type custom field set: " + adminJobTypeValue);
		}

		if (vaiCustomFieldValue != null) {
			jobCreationPage.set_VAICustomField(vaiCustomFieldValue);
			logger.info("✅ VAI custom field set: " + vaiCustomFieldValue);
		}

		jobCreationPage.create_Action();
		logger.info("✅ Job creation action triggered");

		jobDetailsPage.jobTitleFormatCheck();
		logger.info("✅ Job title format verified");

	}


	@AfterClass
	public void setdown() {
		tearDown();
	}

}
