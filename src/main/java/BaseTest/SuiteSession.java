package BaseTest;

import java.net.URI;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import PageObject.CompanyPage;
import PageObject.DashboardPage;
import TestUtility.Non_WebDriver_Util;

/**
 * ONE browser + ONE login for the whole TestNG run (suite).
 *
 * Launch Browser -> Open Application -> Company Login -> Verify Dashboard -> (all test classes) -> Logout -> Close Browser
 *
 * - The browser is launched through Baseclass.initilizeConfig() and closed through Baseclass.tearDown().
 * - Every test class starts from - and returns to - the Dashboard (see returnToDashboard()).
 * - A new login is done only when the authenticated session is really lost (the app redirects to /login).
 * Driven by TestUtility.TestExecutionListener (suite start / class start / class end / suite end).
 */
public final class SuiteSession {

	private static final Logger log = LogManager.getLogger(SuiteSession.class);
	private static final By COMPANY_NAME_INPUT = By.cssSelector("#company_login_name");
	private static final By EMAIL_INPUT = By.cssSelector("#email");

	private static Baseclass config;
	private static WebDriver driver;
	private static String dashboardUrl;
	private static int loginCount = 0;

	private SuiteSession() {
	}

	public static synchronized boolean isStarted() {
		return driver != null;
	}

	// Launch browser, open the application, login once and verify the Dashboard
	public static synchronized void start() {
		if (driver != null) {
			Baseclass.setDriver(driver);
			return;
		}
		config = new Baseclass();
		log.info("==================================================================");
		log.info("[SUITE START] UAT-ZuperSettings - shared browser session");
		log.info("[SETUP] Launching browser (" + config.prop.getProperty("browser") + ")");
		log.info("[SETUP] Opening application: " + config.prop.getProperty("baseURL"));
		config.initilizeConfig();           // Baseclass: launch browser + open baseURL
		driver = Baseclass.getDriver();
		URI base = URI.create(config.prop.getProperty("baseURL").trim());
		dashboardUrl = base.getScheme() + "://" + base.getHost() + "/dashboard";
		login();
		log.info("==================================================================");
	}

	// Company login with the existing CompanyPage flow; handles the login page that already remembers the company
	private static void login() {
		loginCount++;
		log.info("[SETUP] Performing company login (company: " + config.prop.getProperty("company_Name") + ", user: "
				+ config.prop.getProperty("username") + ")" + (loginCount > 1 ? " - login #" + loginCount : ""));
		CompanyPage companyPage = new CompanyPage();
		if (Non_WebDriver_Util.findIfVisible(driver, COMPANY_NAME_INPUT, 20) != null) {
			companyPage.enterCompanyNameDetails(config.prop.getProperty("company_Name"));
		} else if (Non_WebDriver_Util.findIfVisible(driver, EMAIL_INPUT, 10) != null) {
			log.info("[SETUP] Company already selected on the login page - continuing with email/password");
		} else {
			throw new IllegalStateException("❌ Login page not displayed - current URL: " + driver.getCurrentUrl());
		}
		companyPage.enter_LoginSceanrio(config.prop.getProperty("username"), config.prop.getProperty("password"));
		// UAT login can take ~30s
		new WebDriverWait(driver, Duration.ofSeconds(90))
				.withMessage("Dashboard after login - current URL: " + driver.getCurrentUrl())
				.until(ExpectedConditions.urlContains("/dashboard"));
		log.info("[SETUP] Login successful");

		DashboardPage dashboardPage = new DashboardPage();
		dashboardPage.waitForDashboardToLoad();
		dashboardPage.dismissTimezonePopup();
		dashboardPage.minimizeZuperConnectDialer();
		if (!dashboardPage.isDashboardDisplayed()) {
			throw new IllegalStateException("❌ Dashboard not displayed after login - current URL: " + driver.getCurrentUrl());
		}
		log.info("[SETUP] Dashboard loaded: " + driver.getCurrentUrl());
	}

	/**
	 * Navigates to the Dashboard and verifies it. Used before and after every test class so every test
	 * starts from - and returns to - the Dashboard. Logs in again only if the session was lost.
	 */
	public static synchronized void returnToDashboard() {
		start();
		Baseclass.setDriver(driver);
		DashboardPage dashboardPage = new DashboardPage();
		boolean navigated = !driver.getCurrentUrl().contains("/dashboard");
		if (navigated) {
			driver.get(dashboardUrl);
			acceptUnexpectedAlert();
		}
		// session expired / lost -> the application redirects to the login page
		Non_WebDriver_Util.waitWithoutImplicitWait(driver, 20,
				d -> d.getCurrentUrl().contains("/dashboard") || d.getCurrentUrl().contains("/login"));
		if (driver.getCurrentUrl().contains("/login")) {
			log.warn("[WARN] Authenticated session lost (redirected to login page) - logging in again");
			login();
			return;
		}
		dashboardPage.waitForDashboardToLoad();
		// the timezone popup / dialer can come up late after login (also while already on the Dashboard),
		// so always do a short check - an open popup blocks every click on the page
		dashboardPage.dismissTimezonePopup(2);
		dashboardPage.minimizeZuperConnectDialer(1);
		if (!dashboardPage.isDashboardDisplayed()) {
			throw new IllegalStateException("❌ Dashboard not displayed - current URL: " + driver.getCurrentUrl());
		}
	}

	// Logout once and close the browser once at the end of the run
	public static synchronized void end() {
		if (driver == null) {
			return;
		}
		log.info("==================================================================");
		log.info("[SUITE END] UAT-ZuperSettings - closing the shared browser session (logins performed: " + loginCount + ")");
		try {
			returnToDashboard();
			log.info("[TEARDOWN] Logging out");
			new DashboardPage().logout();
			log.info("[TEARDOWN] Logged out - login page displayed");
		} catch (Exception e) {
			log.error("[TEARDOWN] Logout failed: " + e.getClass().getSimpleName() + " - " + String.valueOf(e.getMessage()).split("\\R")[0]);
		} finally {
			Baseclass.setDriver(driver);
			config.tearDown();              // Baseclass: quit browser
			driver = null;
			log.info("[TEARDOWN] Browser closed");
			log.info("==================================================================");
		}
	}

	public static WebDriver getDriver() {
		return driver;
	}

	private static void acceptUnexpectedAlert() {
		try {
			driver.switchTo().alert().accept();
			log.warn("[WARN] Browser 'leave page' alert accepted while returning to Dashboard");
		} catch (NoAlertPresentException e) {
			// no alert - normal case
		}
	}
}
