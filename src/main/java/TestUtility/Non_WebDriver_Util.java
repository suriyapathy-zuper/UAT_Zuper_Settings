package TestUtility;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Non_WebDriver_Util {
	
		public static void  waitForVisible(WebDriver driver, WebElement element, int timeout) {
	         new WebDriverWait(driver, Duration.ofSeconds(timeout))
	                .until(ExpectedConditions.visibilityOf(element));
		
	}
		public static void  waitForBeClickable(WebDriver driver, WebElement element, int timeout) {
	         new WebDriverWait(driver, Duration.ofSeconds(timeout))
	                .until(ExpectedConditions.elementToBeClickable(element));
		
	}
		
		//
		
		   public static void  waitForAndAcceptAlert(WebDriver driver, int timeout) {
		        Alert alert = new WebDriverWait(driver,Duration.ofSeconds(timeout) )
		                .until(ExpectedConditions.alertIsPresent());
		        alert.accept();
		       
		    }
		   
		   //
		   
		   public static void selectMatOptionByText(WebDriver driver, List<WebElement> elements, String optionText) {
			    for (WebElement option : elements) {
			        String text = option.getText().trim();
			        if (text.equalsIgnoreCase(optionText)) {
			            option.click();
			            break; // stop after selecting the first match
			        }
			    }
			}
		   
		   
		   // 
		   public static void pressEnter(WebDriver driver) {
		        Actions actions = new Actions(driver);
		        actions.sendKeys(Keys.ENTER).perform();
		    }
}
