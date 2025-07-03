package RunnerClass;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import BaseTest.Baseclass;
import PageObject.CompanyPage;


public class TC_Company_Login extends Baseclass{
	
  
	private CompanyPage companyPage;

    @BeforeClass
    public void setUp() {
        initilizeConfig();           //  BaseClass setup
        companyPage = new CompanyPage(driver);
    }

    @Test
    public void verify_CompanyAndLoginPage() {
    	 companyPage.enterCompanyName(prop.getProperty("company_Name"));
         companyPage.enter_LoginSceanrio(prop.getProperty("username"), prop.getProperty("password"));
        
    }
   
    
//    @AfterClass
//    public void setdown() {
//    	tearDown();
//    }
    
    
}

