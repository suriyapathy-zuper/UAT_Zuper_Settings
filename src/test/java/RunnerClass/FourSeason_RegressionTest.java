package RunnerClass;

import java.util.Arrays;

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

public class FourSeason_RegressionTest extends Baseclass {

	private CompanyPage companyPage;
	private DashboardPage dashboardPage;
	private JobListingPage jobListingPage;
	private JobCreationPage jobCreationPage;
	private JobDetailsPage jobDetailsPage;
	protected String currentTestCategory;

	@BeforeClass
	public void setUp() {
		initilizeConfig(); // BaseClass setup
		companyPage = new CompanyPage();
		dashboardPage = new DashboardPage();
		jobListingPage = new JobListingPage();
		jobCreationPage = new JobCreationPage();
		jobDetailsPage = new JobDetailsPage();
		logger.info("Test setup completed.");

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

	@DataProvider(name = "jobFlows")
	public Object[][] jobFlows() {

		Object[][] allData = new Object[][] {
				// jobType, statusToUpdate, stagingLocation, futureJobLength, basicDesc,
				// isScheduled, arrivalTimeframe, jobTBDReason, permitNeeded,
				// ifNoAppointmenthasbeenset,
				// appointmentType,NotetoAccountManagerescriptionofwork

				{ "Plumbing Install", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", false, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Install", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", true, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Return Visit", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", false, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Return Visit", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", true, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Excavation", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", false, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Excavation", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", true, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Service call", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", true, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Service call", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", false, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Rodding", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", true, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule",
						"This job requires a Drain Tech to go back out and assess. (RD)", "test notes" },
				{ "Plumbing Rodding", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", false, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule",
						"This job requires a Drain Tech to go back out and assess. (RD)", "test notes" },
				{ "Plumbing Site Visit", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", true, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This Job requires a Foreman to go back out and assess. (SV)",
						"test notes" },
				{ "Plumbing Site Visit", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", false, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This Job requires a Foreman to go back out and assess. (SV)",
						"test notes" },
				{ "Plumbing Install with Plumbing Rollover Job", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", false, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Install with Plumbing Rollover Job", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", true, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Return Visit with Plumbing Rollover Job", "Assessment Completed/Work Completed",
						"Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", false, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Return Visit with Plumbing Rollover Job", "Assessment Completed/Work Completed",
						"Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", true, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Excavation with Plumbing Rollover Job", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", false, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Excavation with Plumbing Rollover Job", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", true, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Rollover Job", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", true, "1PM - 6PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Plumbing Rollover Job", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", false, "1PM - 6PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Electrical Install", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", true, "8AM - 12PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Electrical Install", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", false, "1PM - 6PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Electrical Return Visit", "Assessment Completed/Work Completed", "Bridgeview",
						"Estimated 3/4 Day Job", "TestDescription", true, "8AM - 12PM Arrival", "Waiting on Permit",
						"Yes", "TBD Based on Client Schedule",
						"This job requires a Plumber to go back out and assess. (P)", "test notes" },
				{ "Electrical Return Visit", "Assessment Completed/Work Completed", "Bridgeview",
						"Estimated 3/4 Day Job", "TestDescription", false, "1PM - 6PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Electrical Service call", "Assessment Completed/Work Completed", "Bridgeview",
						"Estimated 3/4 Day Job", "TestDescription", true, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule",
						"This job requires an Electrician to go back out and assess. (EE)", "test notes" },
				{ "Electrical Service call", "Assessment Completed/Work Completed", "Bridgeview",
						"Estimated 3/4 Day Job", "TestDescription", false, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule",
						"This job requires an Electrician to go back out and assess. (EE)", "test notes" },
				{ "Site Visit Electrical", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", true, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This Job requires a Foreman to go back out and assess. (SV)",
						"test notes" },
				{ "Site Visit Electrical", "Assessment Completed/Work Completed", "Bridgeview", "Estimated 3/4 Day Job",
						"TestDescription", false, "8AM - 12PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This Job requires a Foreman to go back out and assess. (SV)",
						"test notes" },

				{ "Electrical Install with Electrical Rollover", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", false, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Electrical Install with Electrical Rollover", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", true, "8AM - 12PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Electrical Return Visit with Electrical Rollover", "Assessment Completed/Work Completed",
						"Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", false, "12PM - 5PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Electrical Return Visit with Electrical Rollover", "Assessment Completed/Work Completed",
						"Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", true, "8AM - 12PM Arrival", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },

				{ "Electrical Rollover", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", true, "1PM - 6PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" },
				{ "Electrical Rollover", "Assessment Completed/Work Completed", "Bridgeview",
						"2 or More Days Project (Estimated to require multiple days, including inspections)",
						"TestDescription", false, "1PM - 6PM", "Waiting on Permit", "Yes",
						"TBD Based on Client Schedule", "This job requires a Plumber to go back out and assess. (P)",
						"test notes" }, };

		 currentTestCategory = prop.getProperty("jobCategory");

		if (isElectricalCategory(currentTestCategory)) {
			// last 14 rows
			return slice(allData, allData.length - 14, allData.length);
		} else if (isPlumbingInstallCategory(currentTestCategory)) {
			// rows [total-32 .. total-12)
			return slice(allData, allData.length - 34, allData.length - 14);
		} else if (isPlumbingServiceCategory(currentTestCategory)) {
			// rows [total-32 .. total-14)
			return slice(allData, allData.length - 34, allData.length - 33); //16
		}

		// default: all rows
		return allData;
	}

	private boolean isElectricalCategory(String cat) {
		return cat.equalsIgnoreCase("Electrical Install") || cat.equalsIgnoreCase("Electrical Return Visit")
				|| cat.equalsIgnoreCase("Electrical Rollover") || cat.equalsIgnoreCase("Install");
	}

	private boolean isPlumbingInstallCategory(String cat) {
		return cat.equalsIgnoreCase("Plumbing Install") || cat.equalsIgnoreCase("Plumbing Return Visit")
				|| cat.equalsIgnoreCase("Plumbing Rollover Job") || cat.equalsIgnoreCase("Plumbing Excavation");
	}

	private boolean isPlumbingServiceCategory(String cat) {
		return cat.equalsIgnoreCase("Plumbing Service Call") || cat.equalsIgnoreCase("Plumbing Site Visit")
				|| cat.equalsIgnoreCase("Plumbing Blackflow Testing") || cat.equalsIgnoreCase("Inspection-Plumbing");
	}

	private Object[][] slice(Object[][] allData, int start, int end) {
		int total = allData.length;
		start = Math.max(0, Math.min(start, total));
		end = Math.max(0, Math.min(end, total));
		if (start >= end) {
			return new Object[0][];
		}
		return Arrays.copyOfRange(allData, start, end);
	}

	@Test(dataProvider = "jobFlows", dependsOnMethods = "verify_CompanyAndLoginPage")
	public void testJobFlow(String jobType, String statusNameToUpdate, String stagingLocation, String futureJobLength,
			String basicDescriptionofWork, boolean isScheduled, String arrivalTimeframe, String jobTBDReason,
			String permitNeeded, String ifNoAppointmenthasbeenset, String appointmentType,
			String NotetoAccountManagerescriptionofwork) {
		// Common preconditions: login, navigate, create job etc.

		// For example, create job first
		logger.info("=== Starting Test for Job Type: " + jobType + "===");

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

		// work completed for pulmbing related category
		if (currentTestCategory.equalsIgnoreCase("Plumbing Install")
				|| currentTestCategory.equalsIgnoreCase("Plumbing Return Visit")
				|| currentTestCategory.equalsIgnoreCase("Plumbing Excavation")
				|| currentTestCategory.equalsIgnoreCase("Plumbing Rollover Job")) {

			jobDetailsPage.updateJobStatus_WithChecklist_WorkInProgress("Work in Progress");
			logger.info("✅ Work in Progress checklist completed");

			if (jobType.equalsIgnoreCase("Plumbing Install") || jobType.equalsIgnoreCase("Plumbing Return Visit")
					|| jobType.equalsIgnoreCase("Plumbing Excavation")) {

				jobDetailsPage.updateJobStatus_WithChecklist_WorkCompleted_GenericJobSoldFuture(jobType,
						statusNameToUpdate, stagingLocation, futureJobLength, basicDescriptionofWork, isScheduled,
						arrivalTimeframe, jobTBDReason, permitNeeded);

				logger.info("✅ Completed 'Work Completed' with job-sold logic");

				jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
				logger.info("✅ Verified associated child job count");

				jobDetailsPage.navigateToChildJOb();
				logger.info("✅ Navigated to child job");

				String expectedChildJobType = jobType.equalsIgnoreCase("Plumbing Excavation") ? "Plumbing Excavation"
						: jobType.equalsIgnoreCase("Plumbing Install") ? "Plumbing Install"
								: jobType.equalsIgnoreCase("Plumbing Return Visit") ? "Plumbing Return Visit" : "";

				jobDetailsPage.verifyChildJobCategory(expectedChildJobType);
				logger.info("✅ Verified child job category: {}", expectedChildJobType);

				jobDetailsPage.verifyChildJobTags();
				logger.info("✅ Verified child job Tgags");

				jobDetailsPage.verifyChildJobDescription(basicDescriptionofWork);
				logger.info("✅ Verified child job description");

				jobDetailsPage.verifyCustomfield_SalesName();
				logger.info("✅ Verified Sales Name custom field");

				jobDetailsPage.verifyCustomfield_PermitNeeded();
				logger.info("✅ Verified Permit Needed custom field");

				jobDetailsPage.verifyCustomfield_Staging_Location();
				logger.info("✅ Verified Staging Location custom field");

				try {

					if (isScheduled) {
						jobDetailsPage.verifyJobScheduledDate(0);
						jobDetailsPage
								.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
						jobDetailsPage.verifyCustomfield_arrivalTimeframe();
						logger.info("✅ Verified Scheduled details and status for scheduled job");
					} else {
						jobDetailsPage.verifyCustomfield_JObTDBReason(); // Might fail if not set properly
						jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_JobPending"));
						logger.info("✅ Verified Job TBD Reason and pending status for unscheduled job");
					}

				} catch (AssertionError | Exception e) {
					logger.error("❌ Error during post-job creation validation: ", e);
					throw e; // To fail the test cleanly and show the issue in logs
				}

			} else if (jobType.equalsIgnoreCase("Plumbing Service Call") || jobType.equalsIgnoreCase("Plumbing Rodding")
					|| jobType.equalsIgnoreCase("Plumbing Site Visit")) {

				jobDetailsPage.updateJobStatus_WithChecklist_WorkCompleted_EstimateNeeded_Generic(jobType,
						statusNameToUpdate, isScheduled, ifNoAppointmenthasbeenset, arrivalTimeframe, appointmentType,
						basicDescriptionofWork, NotetoAccountManagerescriptionofwork);
				logger.info("✅ Work Completed with Estimate Needed flow executed");

				jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
				logger.info("✅ Verified child job association");

				jobDetailsPage.navigateToChildJOb();
				logger.info("✅ Navigated to child job");

				String expectedChildJobType = jobType.equalsIgnoreCase("Plumbing Service Call")
						? "Plumbing Service Call"
						: jobType.equalsIgnoreCase("Plumbing Rodding") ? "Plumbing Rodding"
								: jobType.equalsIgnoreCase("Plumbing Site Visit") ? "Plumbing Site Visit" : "";

				jobDetailsPage.verifyChildJobCategory(expectedChildJobType);
				logger.info("✅ Verified child job category: {}", expectedChildJobType);

				jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
				logger.info("✅ Verified Dispatch Approval Needed status");

				jobDetailsPage.verifyCustomfield_SalesName();
				logger.info("✅ Verified Sales Name custom field");

				if (isScheduled) {
					jobDetailsPage.verifyJobScheduledDate(0);
					logger.info("✅ Verified scheduled date for job");
					jobDetailsPage.verifyCustomfield_arrivalTimeframe(); // Uncomment if needed
					logger.info("✅ Verified arrival timeframe");
				}

				logger.info("🎉 Job creation and validation flow completed successfully");

			}

			else if (jobType.equalsIgnoreCase("Plumbing Install with Plumbing Rollover Job")
					|| jobType.equalsIgnoreCase("Plumbing Return Visit with Plumbing Rollover Job")
					|| jobType.equalsIgnoreCase("Plumbing Excavation with Plumbing Rollover Job")) {

				jobDetailsPage.updateJobStatus_WithChecklist_WorkCompleted_ProjectWith_RollOverJob(jobType,
						statusNameToUpdate, stagingLocation, futureJobLength, basicDescriptionofWork, isScheduled,
						arrivalTimeframe, jobTBDReason, permitNeeded);
				logger.info("✅ Completed Job Sold Future Appointment flow");

				jobDetailsPage.verify_ChildJobAssoicated("2");
				logger.info("✅ Verified child job association");

				jobDetailsPage.navigateToMultipleJobs(jobType, basicDescriptionofWork, isScheduled, stagingLocation);
				logger.info("🎉 Job creation and validation flow completed successfully");

			} else if (jobType.equalsIgnoreCase("Plumbing Rollover Job")) {

				jobDetailsPage.updateJobStatus_WithChecklist_WorkCompleted_GenericRolloverNeeded(jobType,
						statusNameToUpdate, basicDescriptionofWork, isScheduled, arrivalTimeframe);

				logger.info("✅ Completed 'Work Completed' with job-sold logic");

				jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
				logger.info("✅ Verified associated child job count");

				jobDetailsPage.navigateToChildJOb();
				logger.info("✅ Navigated to child job");

				String expectedChildJobType = jobType.equalsIgnoreCase("Plumbing Rollover Job")
						? "Plumbing Rollover Job"
						: "";

				jobDetailsPage.verifyChildJobCategory(expectedChildJobType);
				logger.info("✅ Verified child job category: {}", expectedChildJobType);

				jobDetailsPage.verifyChildJobDescriptionForRolloverNeeded();
				logger.info("✅ Verified child job description");

				jobDetailsPage.verifyCustomfield_SalesName();
				logger.info("✅ Verified Sales Name custom field");

				try {
					if (isScheduled) {
						jobDetailsPage.verifyJobScheduledDate(0);
						jobDetailsPage
								.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
						jobDetailsPage.verifyCustomfield_arrivalTimeframe();
						logger.info("✅ Verified Scheduled details and status for scheduled job");
					}

				} catch (AssertionError | Exception e) {
					logger.error("❌ Error during post-job creation validation: ", e);
					throw e; // To fail the test cleanly and show the issue in logs
				}

			}

			else {
				logger.warn("🚫 Unsupported job type: " + jobType);
			}

		}

		// work completed for electrical install related category

		else if (currentTestCategory.equalsIgnoreCase("Electrical Install")
				|| currentTestCategory.equalsIgnoreCase("Electrical Return Visit")
				|| currentTestCategory.equalsIgnoreCase("Electrical Rollover")
				|| currentTestCategory.equalsIgnoreCase("Electrical Install")
				|| currentTestCategory.equalsIgnoreCase("Install")) {

			jobDetailsPage.updateJobStatus_WithChecklist_WorkInProgress("Work in Progress");
			logger.info("✅ Work in Progress checklist completed");

			if (jobType.equalsIgnoreCase("Electrical Install") || jobType.equalsIgnoreCase("Electrical Return Visit")) {

				jobDetailsPage.updateJobStatus_WithChecklist_WorkCompleted_GenericJobSoldFuture(jobType,
						statusNameToUpdate, stagingLocation, futureJobLength, basicDescriptionofWork, isScheduled,
						arrivalTimeframe, jobTBDReason, permitNeeded);

				logger.info("✅ Completed 'Work Completed' with job-sold logic");

				jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
				logger.info("✅ Verified associated child job count");

				jobDetailsPage.navigateToChildJOb();
				logger.info("✅ Navigated to child job");

				String expectedChildJobType = jobType.equalsIgnoreCase("Electrical Install") ? "Electrical Install"
						: jobType.equalsIgnoreCase("Electrical Return Visit") ? "Electrical Return Visit" : "";

				jobDetailsPage.verifyChildJobCategory(expectedChildJobType);
				logger.info("✅ Verified child job category: {}", expectedChildJobType);

				jobDetailsPage.verifyChildJobTags();
				logger.info("✅ Verified child job Tgags");

				jobDetailsPage.verifyChildJobDescription(basicDescriptionofWork);
				logger.info("✅ Verified child job description");

				jobDetailsPage.verifyCustomfield_SalesName();
				logger.info("✅ Verified Sales Name custom field");

				jobDetailsPage.verifyCustomfield_PermitNeeded();
				logger.info("✅ Verified Permit Needed custom field");

				jobDetailsPage.verifyCustomfield_Staging_Location();
				logger.info("✅ Verified Staging Location custom field");

				try {

					if (isScheduled) {
						jobDetailsPage.verifyJobScheduledDate(0);
						jobDetailsPage
								.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
						jobDetailsPage.verifyCustomfield_arrivalTimeframe();
						logger.info("✅ Verified Scheduled details and status for scheduled job");
					} else {
						jobDetailsPage.verifyCustomfield_JObTDBReason(); // Might fail if not set properly
						jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_JobPending"));
						logger.info("✅ Verified Job TBD Reason and pending status for unscheduled job");
					}

				} catch (AssertionError | Exception e) {
					logger.error("❌ Error during post-job creation validation: ", e);
					throw e; // To fail the test cleanly and show the issue in logs
				}

			} else if (jobType.equalsIgnoreCase("Electrical Service Call")
					|| jobType.equalsIgnoreCase("Site Visit Electrical")) {

				jobDetailsPage.updateJobStatus_WithChecklist_WorkCompleted_EstimateNeeded_Generic(jobType,
						statusNameToUpdate, isScheduled, ifNoAppointmenthasbeenset, arrivalTimeframe, appointmentType,
						basicDescriptionofWork, NotetoAccountManagerescriptionofwork);
				logger.info("✅ Work Completed with Estimate Needed flow executed");

				jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
				logger.info("✅ Verified child job association");

				jobDetailsPage.navigateToChildJOb();
				logger.info("✅ Navigated to child job");

				String expectedChildJobType = jobType.equalsIgnoreCase("Electrical Service Call")
						? "Electrical Service Call"
						: jobType.equalsIgnoreCase("Site Visit Electrical") ? "Site Visit Electrical" : "";

				jobDetailsPage.verifyChildJobCategory(expectedChildJobType);
				logger.info("✅ Verified child job category: {}", expectedChildJobType);

				jobDetailsPage.verifyChildJobDescription(basicDescriptionofWork);
				logger.info("✅ Verified child job description");

				jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
				logger.info("✅ Verified Dispatch Approval Needed status");

				jobDetailsPage.verifyCustomfield_SalesName();
				logger.info("✅ Verified Sales Name custom field");

				if (isScheduled) {
					jobDetailsPage.verifyJobScheduledDate(0);
					logger.info("✅ Verified scheduled date for job");
					jobDetailsPage.verifyCustomfield_arrivalTimeframe(); // Uncomment if needed
					logger.info("✅ Verified arrival timeframe");
				}

				logger.info("🎉 Job creation and validation flow completed successfully");

			}

			else if (jobType.equalsIgnoreCase("Electrical Install with Electrical Rollover")
					|| jobType.equalsIgnoreCase("Electrical Return Visit with Electrical Rollover")) {

				jobDetailsPage.updateJobStatus_WithChecklist_WorkCompleted_ProjectWith_RollOverJob(jobType,
						statusNameToUpdate, stagingLocation, futureJobLength, basicDescriptionofWork, isScheduled,
						arrivalTimeframe, jobTBDReason, permitNeeded);
				logger.info("✅ Completed Job Sold Future Appointment flow");

				jobDetailsPage.verify_ChildJobAssoicated("2");
				logger.info("✅ Verified child job association");

				jobDetailsPage.navigateToMultipleJobs(jobType, basicDescriptionofWork, isScheduled, stagingLocation);
				logger.info("🎉 Job creation and validation flow completed successfully");

			} else if (jobType.equalsIgnoreCase("Electrical Rollover")) {

				jobDetailsPage.updateJobStatus_WithChecklist_WorkCompleted_GenericRolloverNeeded(jobType,
						statusNameToUpdate, basicDescriptionofWork, isScheduled, arrivalTimeframe);

				logger.info("✅ Completed 'Work Completed' with job-sold logic");

				jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
				logger.info("✅ Verified associated child job count");

				jobDetailsPage.navigateToChildJOb();
				logger.info("✅ Navigated to child job");

				String expectedChildJobType = jobType.equalsIgnoreCase("Electrical Rollover") ? "Electrical Rollover"
						: "";

				jobDetailsPage.verifyChildJobCategory(expectedChildJobType);
				logger.info("✅ Verified child job category: {}", expectedChildJobType);

				jobDetailsPage.verifyChildJobDescriptionForRolloverNeeded();
				logger.info("✅ Verified child job description");

				jobDetailsPage.verifyCustomfield_SalesName();
				logger.info("✅ Verified Sales Name custom field");

				try {
					if (isScheduled) {
						jobDetailsPage.verifyJobScheduledDate(0);
						jobDetailsPage
								.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
						jobDetailsPage.verifyCustomfield_arrivalTimeframe();
						logger.info("✅ Verified Scheduled details and status for scheduled job");
					}

				} catch (AssertionError | Exception e) {
					logger.error("❌ Error during post-job creation validation: ", e);
					throw e; // To fail the test cleanly and show the issue in logs
				}

			}

			else {
				logger.warn("🚫 Unsupported job type: " + jobType);
			}

		}

		// Assessment Completed status

		else {

			jobDetailsPage
					.updateJobStatus_WithChecklist_StatringAssessment(prop.getProperty("StartingAssessmentToUpdate"));
			logger.info("✅ Starting assessment checklist completed");

			if (jobType.equalsIgnoreCase("Plumbing Install") || jobType.equalsIgnoreCase("Plumbing Return Visit")
					|| jobType.equalsIgnoreCase("Plumbing Excavation")) {

				jobDetailsPage.updateJobStatus_WithChecklist_AssessmentCompleted_GenericJobSoldFuture(jobType,
						statusNameToUpdate, stagingLocation, futureJobLength, basicDescriptionofWork, isScheduled,
						arrivalTimeframe, jobTBDReason, permitNeeded);

				logger.info("✅ Completed 'Assessment Completed' with job-sold logic");

				jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
				logger.info("✅ Verified associated child job count");

				jobDetailsPage.navigateToChildJOb();
				logger.info("✅ Navigated to child job");

				String expectedChildJobType = jobType.equalsIgnoreCase("Plumbing Excavation") ? "Plumbing Excavation"
						: jobType.equalsIgnoreCase("Plumbing Install") ? "Plumbing Install"
								: jobType.equalsIgnoreCase("Plumbing Return Visit") ? "Plumbing Return Visit" : "";

				jobDetailsPage.verifyChildJobCategory(expectedChildJobType);
				logger.info("✅ Verified child job category: {}", expectedChildJobType);

				jobDetailsPage.verifyChildJobTags();
				logger.info("✅ Verified child job Tgags");

				jobDetailsPage.verifyChildJobDescription(basicDescriptionofWork);
				logger.info("✅ Verified child job description");

				jobDetailsPage.verifyCustomfield_SalesName();
				logger.info("✅ Verified Sales Name custom field");

				jobDetailsPage.verifyCustomfield_PermitNeeded();
				logger.info("✅ Verified Permit Needed custom field");
				try {
					if (expectedChildJobType.equalsIgnoreCase("Plumbing Install")
							|| expectedChildJobType.equalsIgnoreCase("Plumbing Excavation")) {
						jobDetailsPage.verifyCustomfield_Staging_Location();
						logger.info("✅ Verified Staging Location (Install/Excavation only)");
					}

					if (isScheduled) {
						jobDetailsPage.verifyJobScheduledDate(0);
						jobDetailsPage
								.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
						jobDetailsPage.verifyCustomfield_arrivalTimeframe();
						logger.info("✅ Verified Scheduled details and status for scheduled job");
					} else {
						jobDetailsPage.verifyCustomfield_JObTDBReason(); // Might fail if not set properly
						jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_JobPending"));
						logger.info("✅ Verified Job TBD Reason and pending status for unscheduled job");
					}

				} catch (AssertionError | Exception e) {
					logger.error("❌ Error during post-job creation validation: ", e);
					throw e; // To fail the test cleanly and show the issue in logs
				}

			} else if (jobType.equalsIgnoreCase("Plumbing Service Call") || jobType.equalsIgnoreCase("Plumbing Rodding")
					|| jobType.equalsIgnoreCase("Plumbing Site Visit")) {

				jobDetailsPage.updateJobStatus_WithChecklist_AssessmentCompleted_EstimateNeeded_Generic(
						statusNameToUpdate, isScheduled, ifNoAppointmenthasbeenset, arrivalTimeframe, appointmentType,
						basicDescriptionofWork, NotetoAccountManagerescriptionofwork);
				logger.info("✅ Assessment Completed with Estimate Needed flow executed");

				jobDetailsPage.verify_ChildJobAssoicated(prop.getProperty("job_CountForSignleJOb"));
				logger.info("✅ Verified child job association");

				jobDetailsPage.navigateToChildJOb();
				logger.info("✅ Navigated to child job");

				String expectedChildJobType = jobType.equalsIgnoreCase("Plumbing Service Call")
						? "Plumbing Service Call"
						: jobType.equalsIgnoreCase("Plumbing Rodding") ? "Plumbing Rodding"
								: jobType.equalsIgnoreCase("Plumbing Site Visit") ? "Plumbing Site Visit" : "";

				jobDetailsPage.verifyChildJobCategory(expectedChildJobType);
				logger.info("✅ Verified child job category: {}", expectedChildJobType);

				jobDetailsPage.verify_CurrentStatus(prop.getProperty("currentStatusName_DispatchApprovalNeeded"));
				logger.info("✅ Verified Dispatch Approval Needed status");

				jobDetailsPage.verifyCustomfield_SalesName();
				logger.info("✅ Verified Sales Name custom field");

				if (isScheduled) {
					jobDetailsPage.verifyJobScheduledDate(0);
					logger.info("✅ Verified scheduled date for job");
					jobDetailsPage.verifyCustomfield_arrivalTimeframe(); // Uncomment if needed
					logger.info("✅ Verified arrival timeframe");

				}

				logger.info("🎉 Job creation and validation flow completed successfully");

			} else if (jobType.equalsIgnoreCase("Plumbing Install with Plumbing Rollover Job")
					|| jobType.equalsIgnoreCase("Plumbing Return Visit with Plumbing Rollover Job")
					|| jobType.equalsIgnoreCase("Plumbing Excavation with Plumbing Rollover Job")) {

				jobDetailsPage.updateJobStatus_WithChecklist_AssessmentCompleted_ProjectWith_Plumbing_RollOverJob(
						jobType, statusNameToUpdate, stagingLocation, futureJobLength, basicDescriptionofWork,
						isScheduled, arrivalTimeframe, jobTBDReason, permitNeeded);
				logger.info("✅ Completed Job Sold Future Appointment flow");

				jobDetailsPage.verify_ChildJobAssoicated("2");
				logger.info("✅ Verified child job association");

				jobDetailsPage.navigateToMultipleJobs(jobType, basicDescriptionofWork, isScheduled, stagingLocation);
				logger.info("🎉 Job creation and validation flow completed successfully");

			}

			else {
				logger.warn("🚫 Unsupported job type: " + jobType);
			}

		}
		logger.info("🎉 Job creation and validation flow completed successfully for follwoing child job: {}" + jobType);

	}

//	@AfterClass
//	public void setdown() {
//		logger.info("🔻 Tearing down test execution");
//		tearDown();
//	}

}
