package RunnerClass;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import BaseTest.Baseclass;
import PageObject.CompanyPage;
import PageObject.DashboardPage;
import PageObject.InvoiceCreationPage;
import PageObject.InvoiceDetailsPage;
import PageObject.JobCreationPage;
import PageObject.JobDetailsPage;
import PageObject.JobListingPage;
import PageObject.QuoteCreationPage;

public class RK_QuoteCarriedToChildJob_Scenario extends Baseclass {

	private CompanyPage companyPage;
	private DashboardPage dashboardPage;
	private JobListingPage jobListingPage;
	private JobCreationPage jobCreationPage;
	private JobDetailsPage jobDetailsPage;
	private QuoteCreationPage quoteCreationPage;
	


	@BeforeClass
	public void setUp() {
		initilizeConfig(); // BaseClass setup
		companyPage = new CompanyPage();
		dashboardPage = new DashboardPage();
		jobListingPage = new JobListingPage();
		jobCreationPage = new JobCreationPage();
		jobDetailsPage = new JobDetailsPage();
		quoteCreationPage =new QuoteCreationPage();
	 

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
	public void new_JobCreation_WithDataProvider() {


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
		
		jobDetailsPage.naviagteToQuotePageFromJob();
		logger.info("✅ Navigated to Quote Creation page");
		
		quoteCreationPage.Quote_SaveAsDraft_CreatedNew();
		logger.info("✅ Quote Created Successfully");


	}


	@AfterClass
	public void setdown() {
		tearDown();
	}

}
