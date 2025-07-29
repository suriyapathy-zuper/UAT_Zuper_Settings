package TestUtility;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	  private static Actions actions;

	// ==========================================
	// ✅ WAIT UTILITIES
	// ==========================================

	// 1. Wait for element to become visible
	public static void waitForVisible(WebDriver driver, WebElement element, int timeout) {
		new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.visibilityOf(element));
	}
	
	  public static void waitForPageToLoad(WebDriver driver, int timeoutInSeconds) {
	        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
	            .until(webDriver -> ((JavascriptExecutor) webDriver)
	                .executeScript("return document.readyState").equals("complete"));
	    }
	  
	  public static void waitForNonEmptyText(WebDriver driver, WebElement element, int timeoutInSeconds) {
	        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
	            .pollingEvery(Duration.ofMillis(1000))
	            .withMessage("❌ Element text did not become non-empty within " + timeoutInSeconds + " seconds")
	            .until(d -> {
	                try {
	                    String text = element.getText();
	                    System.out.println("🔍 Waiting for non-empty text. Current: '" + text + "'");
	                    return text != null && !text.trim().isEmpty();
	                } catch (Exception e) {
	                    System.out.println("⚠️ Exception while checking element text: " + e.getMessage());
	                    return false;
	                }
	            });
	  }
	  
	  public static String getInnerText(WebDriver driver, WebElement element) {
	        try {
	            return (String) ((JavascriptExecutor) driver)
	                    .executeScript("return arguments[0].innerText;", element);
	        } catch (Exception e) {
	            System.out.println("⚠️ Error fetching innerText: " + e.getMessage());
	            return "";
	        }
	    }
	  
	public static void waitForpresenceOfNestedElementLocatedBy(WebDriver driver, WebElement element, int timeout) {
		new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, null));
	}

	public static void waitpresenceOfElementLocated(WebDriver driver, By element, int timeout) {
		new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.presenceOfElementLocated(element));
	}

	public static void visibilityOfAllElements(WebDriver driver, List<WebElement> elements, int timeout) {
		new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	// 2. Wait for element to become clickable
	public static void waitForBeClickable(WebDriver driver, WebElement element, int timeout) {
		new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.elementToBeClickable(element));
	}

	// 3. Wait for and accept alert
	public static void waitForAndAcceptAlert(WebDriver driver, int timeout) {
		Alert alert = new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.alertIsPresent());
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
	    int retryCount = 3;

	    for (int attempt = 1; attempt <= retryCount; attempt++) {
	        boolean found = false;

	        for (WebElement option : elements) {
	            try {
	                String text = option.getText().trim();
	                if (text.contains(optionText) || text.equalsIgnoreCase(optionText)) {
	                    Non_WebDriver_Util.waitForVisible(driver, option, 5);
	                    Non_WebDriver_Util.jsScrollAndActionClick(driver, option);
	                    found = true;
	                    break;
	                }
	            } catch (Exception e) {
	                // Log and continue to retry
	                System.out.println("Attempt " + attempt + ": Element interaction failed. Retrying...");
	            }
	        }

	        if (found) {
	            return; // successfully found and clicked
	        } else if (attempt < retryCount) {
	            try {
	                Thread.sleep(1000); // wait 1 second before retry
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt(); // restore interrupted status
	                throw new RuntimeException("Thread interrupted during retry wait", e);
	            }
	        }
	    }

	    // After all retries fail
	    throw new IllegalArgumentException("❌ Failed after retries. Kindly provide a correct option: '" + optionText + "'");
	}


	// 7. Press Enter key
	public static void pressEnter(WebDriver driver) {
		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.ENTER).perform();
	}

	// 8. Click on child element inside parent elements by header text
	public static void clickChildElementByHeaderText(WebDriver driver, List<WebElement> parentElements,
			String headerText) {
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

	// clickWithRetry
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
		String endTime = parts[2]; // "12PM"

		String formattedStart = date + " " + convertToHHMM(startTime);
		String formattedEnd = date + " " + convertToHHMM(endTime);

		return new String[] { formattedStart, formattedEnd };
	}

	public static String convertToHHMM(String time) {
		time = time.toLowerCase().replaceAll("\\s", ""); // Normalize to "8AM"
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("ha");
		DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("hh:mma");

		return LocalTime.parse(time, inputFormat).format(outputFormat).toUpperCase();
	}

	// Needed

	public void selectMultiCheckboxOptions(WebElement assessmentBlock, List<String> optionsToSelect) {
		for (String option : optionsToSelect) {
			try {
				WebElement label = assessmentBlock
						.findElement(By.xpath(".//label[normalize-space(text())='" + option + "']"));
				WebElement checkbox = label.findElement(By.xpath("./preceding-sibling::input[1]"));

				if (!checkbox.isSelected()) {
					label.click();
				}
			} catch (NoSuchElementException e) {
				System.out.println("Option not found: " + option);
			}
		}
	}

	public static void scrollIntoViewAndClick(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	public static void jsScrollAndActionClick(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
		actions = new Actions(driver);
		actions.moveToElement(element).pause(Duration.ofMillis(300)).click().build().perform();
	}

	public static void jsScrollAndSendKeys(WebDriver driver, WebElement element, String textToSend) {
	    try {
	        // Scroll into view using JavaScript
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

	        // Focus on the element using Actions
	        actions = new Actions(driver);
	        actions.moveToElement(element).pause(Duration.ofMillis(300)).click().sendKeys(textToSend).build().perform();

	      
	    } catch (Exception e) {
	        throw e;
	    }
	}

	
	/** Utility: wait until any Angular/CDK overlay backdrop disappears */
	public static void waitForOverlayToDisappear(WebDriver driver) {
		By overlay = By.cssSelector(".cdk-overlay-backdrop");
		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
	}

	public static void uploadUsingRobot(WebDriver driver, WebElement uploadButton, String filePath, int timeoutSec) {
		try {
// Click the button that opens the native dialog
			uploadButton.click();

// Optional: add a short explicit wait if the button triggers animations
			new WebDriverWait(driver, Duration.ofSeconds(timeoutSec)).until(d -> {
// crude way: wait for window focus change – otherwise just sleep
               Non_WebDriver_Util.waitThread(1);
				return true;
			});

// Put the file path into the OS clipboard
			StringSelection selection = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

// Use Robot to paste (Ctrl+V) then press Enter
			Robot robot = new Robot();
			robot.setAutoDelay(500);

			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			
			

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (Exception e) {
			throw new RuntimeException("File upload failed for: " + filePath, e);
		}
	}

	
	    // To press page down key
     	public static void pageDownKeyClick(WebDriver driver) {
		actions = new Actions(driver);
		actions.keyDown(Keys.PAGE_DOWN).build().perform();
		actions.keyUp(Keys.PAGE_DOWN).build().perform();
     	}
		
		// To press Up key
		public static void upKeyClick(WebDriver driver) {
			actions = new Actions(driver);
			actions.keyDown(Keys.UP).build().perform();
			actions.keyUp(Keys.UP).build().perform();
		}

		// To press Up key
			public static void downKeyClick(WebDriver driver) {
				actions = new Actions(driver);
				actions.keyDown(Keys.DOWN).build().perform();
				actions.keyUp(Keys.DOWN).build().perform();
			}
			
     	
     	
		// To press ENTER key
		public static void pageEnterKeyClick(WebDriver driver) {
			actions = new Actions(driver);
			actions.keyDown(Keys.ENTER).build().perform();
			actions.keyUp(Keys.ENTER).build().perform();
		
	}
		
		public static void navigate_Back(WebDriver driver) {
			driver.navigate().back();
		}
		
		public static String extractNumber(String input) {
		    Pattern pattern = Pattern.compile("#(\\d+)");
		    Matcher matcher = pattern.matcher(input);

		    if (matcher.find()) {
		        return matcher.group(1); // Returns "6063"
		    }
		    return null;
		}

		
		
}
