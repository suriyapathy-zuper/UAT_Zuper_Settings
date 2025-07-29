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

public class RK_InvoiceAttachmentBaseOnServicetask_Scenario extends Baseclass {

	private CompanyPage companyPage;
	private DashboardPage dashboardPage;
	private JobListingPage jobListingPage;
	private JobCreationPage jobCreationPage;
	private JobDetailsPage jobDetailsPage;
	private InvoiceCreationPage invoiceCreationPage;
	private InvoiceDetailsPage invoiceDetailsPage;


	@BeforeClass
	public void setUp() {
		initilizeConfig(); // BaseClass setup
		companyPage = new CompanyPage();
		dashboardPage = new DashboardPage();
		jobListingPage = new JobListingPage();
		jobCreationPage = new JobCreationPage();
		jobDetailsPage = new JobDetailsPage();
		invoiceCreationPage =new InvoiceCreationPage();
	    invoiceDetailsPage=new InvoiceDetailsPage();

	}

	@Test
	public void verify_CompanyAndLoginPage() {
		companyPage.enterCompanyNameDetails(prop.getProperty("company_Name"));
		companyPage.enter_LoginSceanrio(prop.getProperty("username"), prop.getProperty("password"));

	}
	
	@DataProvider(name = "jobCreationData")
	public Object[][] jobCreationData() {
	    return new Object[][] {
	        // Scenario 1: Two attachments
	        {
	            prop.getProperty("customerName2"),
	            prop.getProperty("lineItemName"),
	            prop.getProperty("invoiceAttachmentName1"),
	            prop.getProperty("invoiceAttachmentName2"),
	            true
	        },
	        // Scenario 2: Single attachment
	        {
	            prop.getProperty("customerName"),
	            prop.getProperty("lineItemName"),
	            prop.getProperty("invoiceAttachmentName1"),
	            null,
	            false
	        },
	        // Scenario 3: Single attachment, no line item
	        {
	            prop.getProperty("customerName2"),
	            null,
	            prop.getProperty("invoiceAttachmentName2"),
	            null,
	            false
	        }
	    };
	}

	@Test(dataProvider = "jobCreationData", dependsOnMethods = "verify_CompanyAndLoginPage")
	public void new_JobCreation_WithDataProvider(
	        String customerName,
	        String lineItemName,
	        String attachment1,
	        String attachment2,
	        boolean isDoubleAttachmentCheck) {

	    dashboardPage.popup_clear();
	    dashboardPage.navigatToJobListionPage();
	    jobListingPage.naviagtetoJobCreationPage();

	    jobCreationPage.set_JobTitle(prop.getProperty("jobTitle"));
	    jobCreationPage.set_JobCategory(prop.getProperty("jobCategory2"));
	    jobCreationPage.set_Customer(customerName);
	    jobCreationPage.set_JobDuedate();
	    jobCreationPage.set_JobDueDateToNextMonth();

	    if (lineItemName != null && !lineItemName.isEmpty()) {
	        jobCreationPage.set_LineItem(lineItemName);
	    }

	    jobCreationPage.create_Action();

	    jobDetailsPage.set_InvoiceCreationFromJob();
	    invoiceCreationPage.clickingSaveAsDraftButton();

	    if (isDoubleAttachmentCheck) {
	        invoiceDetailsPage.checkingAttachmentNamesInInvoice(attachment1, attachment2);
	    } else {
	        invoiceDetailsPage.checkingSingleInvoiceAttachmentName(attachment1);
	    }
	}


	@AfterClass
	public void setdown() {
		tearDown();
	}

}
