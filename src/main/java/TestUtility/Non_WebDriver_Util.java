package TestUtility;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.netty.handler.timeout.TimeoutException;

public class Non_WebDriver_Util {
	
	
	
	
	// ==========================================
	// ✅ WAIT UTILITIES
	// ==========================================

	// 1. Wait for element to become visible
	public static void waitForVisible(WebDriver driver, WebElement element, int timeout) {
	    new WebDriverWait(driver, Duration.ofSeconds(timeout))
	        .until(ExpectedConditions.visibilityOf(element));
	}

	// 2. Wait for element to become clickable
	public static void waitForBeClickable(WebDriver driver, WebElement element, int timeout) {
	    new WebDriverWait(driver, Duration.ofSeconds(timeout))
	        .until(ExpectedConditions.elementToBeClickable(element));
	}

	// 3. Wait for and accept alert
	public static void waitForAndAcceptAlert(WebDriver driver, int timeout) {
	    Alert alert = new WebDriverWait(driver, Duration.ofSeconds(timeout))
	        .until(ExpectedConditions.alertIsPresent());
	    alert.accept();
	}

	// 4. Hard wait (use with caution)
	public static void waitThread(int seconds) {
	    try {
	        Thread.sleep(seconds * 1000);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}

	// 5. Refresh current page
	public static void refreshPage(WebDriver driver) {
	    driver.get(driver.getCurrentUrl());
	}

	// ==========================================
	// ✅ SELECTION / CLICK HELPERS
	// ==========================================

	// 6. Select option from Angular Material dropdown by visible text
	public static void selectMatOptionByText(WebDriver driver, List<WebElement> elements, String optionText) {
	    boolean found = false;
	    for (WebElement option : elements) {
	        String text = option.getText().trim();
	        if (text.trim().equalsIgnoreCase(optionText)) {
	            Non_WebDriver_Util.waitForBeClickable(driver, option, 5);
	            option.click();
	            found = true;
	            break;
	        }
	    }

	    if (!found) {
	        throw new IllegalArgumentException("Kindly provide a correct option: '" + optionText + "'");
	    }
	}

	// 7. Press Enter key
	public static void pressEnter(WebDriver driver) {
	    Actions actions = new Actions(driver);
	    actions.sendKeys(Keys.ENTER).perform();
	}

	// 8. Click on child element inside parent elements by header text
	public static void clickChildElementByHeaderText(WebDriver driver, List<WebElement> parentElements, String headerText) {
	    boolean found = false;
	    for (WebElement element : parentElements) {
	        String elementName = element.findElement(By.tagName("h3")).getText();
	        if (elementName.equalsIgnoreCase(headerText)) {
	            WebElement icon = element.findElement(By.xpath(".//em[contains(@class,'ti-user-plus')]"));
	            Non_WebDriver_Util.waitForBeClickable(driver, icon, 5);
	            icon.click();
	            found = true;
	            break;
	        }
	    }

	    if (!found) {
	        throw new IllegalArgumentException("Kindly provide a correct user name: '" + headerText + "'");
	    }
	}

	// ==========================================
	// ✅ TEXT / VALUE EXTRACTION
	// ==========================================

	// 9. Extract number from string in brackets (e.g., "(5)" → 5)
	public static int extractNumberFromBrackets(String text) {
	    text = text.trim();
	    if (text.startsWith("(") && text.endsWith(")")) {
	        String numberPart = text.substring(1, text.length() - 1);
	        try {
	            return Integer.parseInt(numberPart);
	        } catch (NumberFormatException e) {
	            throw new IllegalArgumentException("Not a valid number inside brackets: " + text);
	        }
	    } else {
	        throw new IllegalArgumentException("Text not in bracket format: " + text);
	    }
	}
	
	
	public static void jsClick(WebDriver driver, WebElement element) {
	    try {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("arguments[0].click();", element);
	    } catch (Exception e) {
	        System.out.println("❌ JS Click failed: " + e.getMessage());
	    }
	}

	//clickWithRetry
	public static void clickWithRetry(WebDriver driver, WebElement element, int maxRetries, int delayMillis) {
	    int attempts = 0;
	    while (attempts < maxRetries) {
	        try {
	            Non_WebDriver_Util.waitForBeClickable(driver, element, 5);
	            element.click();
	            return; // success
	        } catch (ElementClickInterceptedException e) {
	            // Retry after short delay
	            Non_WebDriver_Util.waitThread(delayMillis / 1000); // convert to seconds if using Thread.sleep()
	        } catch (TimeoutException | NoSuchElementException e) {
	            throw e; // Element must be clicked — rethrow if truly missing or not clickable in time
	        }
	        attempts++;
	    }

	    // Fallback to JS click if normal click failed
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}
	
	
	
	// Returns current date formatted as per the given pattern.
	 public static String[] getStartAndEndDateTime(String date, String timeFrame) {
	        String[] parts = timeFrame.split(" ");
	        String startTime = parts[0]; // "8AM"
	        String endTime = parts[2];   // "12PM"

	        String formattedStart = date + " " + convertToHHMM(startTime);
	        String formattedEnd = date + " " + convertToHHMM(endTime);

	        return new String[]{formattedStart, formattedEnd};
	    }

	    public static String convertToHHMM(String time) {
	        time = time.toLowerCase().replaceAll("\\s", ""); // Normalize to "8AM"
	        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("ha");
	        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("hh:mma");

	        return LocalTime.parse(time, inputFormat).format(outputFormat).toUpperCase();
	    }
	
}
