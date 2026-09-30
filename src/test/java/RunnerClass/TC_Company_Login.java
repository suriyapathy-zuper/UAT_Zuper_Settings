package RunnerClass;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import BaseTest.Baseclass;
import PageObject.DashboardPage;
import TestUtility.TestExecutionListener;
import io.qameta.allure.Allure;


/**
 * Company Login verification.
 * The company login itself is performed ONCE for the whole run by BaseTest.SuiteSession (TestExecutionListener, suite start);
 * this test verifies that the shared authenticated session reached the Dashboard.
 */
@Listeners(TestExecutionListener.class)
public class TC_Company_Login extends Baseclass{


	private DashboardPage dashboardPage;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        dashboardPage = new DashboardPage();      // shared browser session (SuiteSession)
    }

    @Test(groups = { "sanity", "regression" }, description = "Company Login Test")
    public void verify_CompanyAndLoginPage() {
    	logger.info("[STEP] Verifying company login '" + prop.getProperty("company_Name") + "' as '" + prop.getProperty("username") + "'");
    	Allure.step("Verify company login '" + prop.getProperty("company_Name") + "' landed on the Dashboard");
    	Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard is not displayed after company login");
    	logger.info("[PASS] Company login successful - Dashboard displayed: " + Baseclass.getDriver().getCurrentUrl());
    }

}

