package PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import BaseTest.Baseclass;

public class CompanyPage extends Baseclass{
	

	  private WebDriver driver;

	    // Constructor initializes WebElements
	    public CompanyPage(WebDriver driver) {
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	    }

	    // Page elements
	    @FindBy(css = "#company_login_name")
	    private WebElement enterCompanyName;

	    @FindBy(xpath = "(.//*[normalize-space(text()) and normalize-space(.)='Company Name'])[1]/following::button[1]")
	    private WebElement continueButton;


	    @FindBy(css = "#email")
	    private WebElement enter_Email;

	    @FindBy(css = "#password")
	    private WebElement enter_Password;
	    
	    
	    @FindBy(xpath = "(.//*[normalize-space(text()) and normalize-space(.)='Forgot password?'])[1]/following::button[1]")
	    private WebElement click_LoginButton;
	    
	    // Page actions
	    public void enterCompanyName(String name) {
	        enterCompanyName.clear();
	        enterCompanyName.sendKeys(name);
	        continueButton.click();
	    }
	    
	    public void enter_LoginSceanrio(String mail, String password) {
	    	enter_Email.clear();
	    	enter_Email.sendKeys(mail);
	    	enter_Password.clear();
	    	enter_Password.sendKeys(password);
	    	click_LoginButton.click();
	    }
	    

	  
}
