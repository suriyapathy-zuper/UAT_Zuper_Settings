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



public class TC_Job_PulmbingServiceCallCreation extends Baseclass{
	
  
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
        companyPage.enterCompanyNameDetails(prop.getProperty("company_Name"));
        companyPage.enter_LoginSceanrio(prop.getProperty("username"), prop.getProperty("password"));
       
    }
    
    
    @Test(dependsOnMethods = "verify_CompanyAndLoginPage" )
    public void new_JobCreation() {
    	dashboardPage.popup_clear();
    	dashboardPage.navigatToJobListionPage();
    	jobListingPage.naviagtetoJobCreationPage();
    	jobCreationPage.set_JobTitle(prop.getProperty("jobTitle"));
    	jobCreationPage.set_JobCategory(prop.getProperty("jobCategory"));
    	jobCreationPage.set_Customer(prop.getProperty("customerName"));
    	jobCreationPage.set_JobDuedate();
    	jobCreationPage.set_JobStartdate();
    	jobCreationPage.create_Action();
    	jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_New_ReadytoAssign"));  
    	jobDetailsPage.updateJobStatus(prop.getProperty("scheduledToUpdate"));
    	jobDetailsPage.updateJobStatus(prop.getProperty("acceptedToUpdate"));
    	jobDetailsPage.updateJobStatus(prop.getProperty("enRouteToUpdate"));
    	jobDetailsPage.updateJobStatus_WithChecklist_Arrived(prop.getProperty("ArrivedToUpdate"));
    	jobDetailsPage.updateJobStatus_WithChecklist_StatringAssessment(prop.getProperty("StartingAssessmentToUpdate"));
    	jobDetailsPage.updateJobStatus_WithChecklist_AssessmentCompleted_PulmbingServicaCallJob(prop.getProperty("AssessmentCompletedToUpdate"),prop.getProperty("ifNoAppointmenthasbeenset"),prop.getProperty("appointmentType_PlumbingServiceCall"),prop.getProperty("NotetoAccountManagerescriptionofwork"));
    	
    	// Verify and navigate to associated child job
    	jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
    	jobDetailsPage.navigateToChildJOb();
    	jobDetailsPage.verifyChildJobCategory(prop.getProperty("childJobCategoryPlumbingServiceCall"));
    	jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
    	//jobDetailsPage.verifyCustomfield_SalesName();
    
    }
//    @AfterClass
//    public void setdown() {
//    	tearDown();
//    }
    
    
}

