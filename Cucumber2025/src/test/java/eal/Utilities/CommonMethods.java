package eal.Utilities;
import java.io.File;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.DayOfWeek;

import java.time.Duration;

import java.time.LocalDate;

import java.time.Month;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Calendar;

import java.util.Date;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import java.util.Random;

import java.util.Set;

import java.util.TimeZone;

import java.util.concurrent.TimeUnit;

import java.util.stream.IntStream;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import org.openqa.selenium.Alert;

import org.openqa.selenium.By;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.ExpectedCondition;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Function;

import eal.pages.Add_New_Customer_POM;
import eal.pages.DashBoard_POM;
import eal.pages.HomePage_POM;

import io.cucumber.core.gherkin.Step;
import io.cucumber.java.Scenario;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class CommonMethods extends Driver {
	
	/** ------------------ Global Variables -------------------- **/
	public static WebDriver driver = Driver.getDriver();
	public static final int ELEMENT_WAIT_TIMEOUT_SECONDS = 40;
	public static final int ELEMENT_POLLING_TIME_MILIS = 50;
	public static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;
	public static final int JQUERY_LOAD_TIMEOUT_SECONDS = 30;
	public static final int SESSION_TIMEOUT_MINUTES = 16;
	JavascriptExecutor js = (JavascriptExecutor) driver;
	WebDriverWait wait = null;
	public static final Logger logger = LogManager.getLogger(CommonMethods.class);
	
	
	// ================================
	// 🔹 Class Objects
	// ================================

	public static SftAssert softAssert;
	public static ExcelUtil excelUtil = new ExcelUtil();


	// ================================
	// 🔹 Page Object Models
	// ================================

	public static HomePage_POM hmpage_pom = new HomePage_POM();
	public static DashBoard_POM dashboard_pom = new DashBoard_POM();
	public static Add_New_Customer_POM addNewCustomer_pom = new Add_New_Customer_POM();
	

  	// ================================
	// 🔹 Different Wait Methods
	// ================================

          
		// Wait for Javascript or JQuery to load
	public static void waitForPageAndAjaxToLoad() {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(JQUERY_LOAD_TIMEOUT_SECONDS));
		// Wait for document.readyState to be 'complete'
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
				.equals("complete"));
		// Wait for jQuery.active == 0 if jQuery is present
		try {
			Boolean jQueryDefined = (Boolean) ((JavascriptExecutor) driver)
					.executeScript("return typeof jQuery != 'undefined'");
			if (jQueryDefined) {
				wait.until(webDriver -> (Boolean) ((JavascriptExecutor) webDriver)
						.executeScript("return jQuery.active == 0"));
			}
		} catch (Exception e) {
			// jQuery not present or error occurred — skip AJAX wait
			System.out.println("jQuery not detected or error occurred. Skipping AJAX wait.");
		}

	}

	// newly implemented for faster response/ wait with locator
	public WebElement waitForElement(By locator) {

		WebElement elementLocator = null;
		removeBorder();
		logger.info("Checking visibility of the Element on browser screen");
		// Prioritize ExpectedConditions for efficiency
		Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT_SECONDS));
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			elementLocator = driver.findElement(locator);
		} catch (TimeoutException e) {
			// Handle timeout exception (optional, log or throw as needed)
			logger.warn(
					LogColor.RED + "Element with locator " + locator + " not found within timeout." + LogColor.RESET);
		}
		// Check element visibility and enabled state only if necessary
		if (!elementLocator.isDisplayed() || !elementLocator.isEnabled()) {
			// Consider using a more specific ExpectedCondition or custom implementation
			wait.until(new ExpectedCondition<WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {

					WebElement element = driver.findElement(locator);
					if (element.isDisplayed() && element.isEnabled()) {
						return element;
					} else {
						return null; // Wait for element to become both visible and enabled
					}

				}
			});
		}
		// Draw border and flash (consider optimization techniques)
		drawborder(elementLocator); // Assuming `drawBorder` takes WebElement as parameter
		// flash(elementLocator); // Assuming `flash` takes WebElement as parameter
		logger.info("Wait for the element is completed, Element is visible on the screen");
		return elementLocator;

	}

	public WebElement waitForDisableElement(By locator) {

		removeBorder(); // here we are calling removeBorder method . the method is in this same class (common methods) . this method is removing any border that was created earliar by Us . using java script.
		logger.info("Waiting for the disabled element to be present on the browser screen");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT_SECONDS));
		WebElement elementLocator = null;
		try {
			// Custom ExpectedCondition to wait for a disabled element
			elementLocator = wait.until(driver -> {
				try {
					WebElement element = driver.findElement(locator);
					if (!element.isEnabled()) {
						return element;
					}
				} catch (Exception e) {
					// Element not found or stale, continue waiting
					return null;
				}
				return null;
			});
		} catch (TimeoutException e) {
			logger.warn(LogColor.RED + "Disabled element with locator " + locator + " not found within timeout."
					+ LogColor.RESET);
			return null;
		}
		drawborder(elementLocator);
		// flash(elementLocator); // Uncomment if needed
		logger.info("Wait for the disabled element is completed, Element is present and not editable");
		return elementLocator;

	}

	public void waitForPageToLoadfor(int sec) {

		logger.info("Wait for Web Page to load completely");
		wait = new WebDriverWait(this.driver, Duration.ofSeconds(sec * 1000));
		Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver arg0) {

				boolean isLoaded = false;
				JavascriptExecutor js = (JavascriptExecutor) arg0;
				if (js.executeScript("return document.readyState").toString().equalsIgnoreCase("complete")) {
					isLoaded = true;
					logger.info("Web Page loaded successfully.");
				}
				return isLoaded;

			}
		};
		wait.until(function);

	}

	// Hard wait for specific second :
	public static void waitFor(int sec) {

		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void waitForMlsec(long sec) {

		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<WebElement> waitForTableRows(By locator) {

		logger.info("Waiting for multiple <tr> elements to be visible and enabled on the screen");
		Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT_SECONDS));
		ArrayList<WebElement> rowElements = new ArrayList<>();
		try {
			// Wait until at least one <tr> element is present
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			// Wait until all <tr> elements are visible and enabled
			List<WebElement> elements = wait.until(driver -> {
				List<WebElement> foundElements = driver.findElements(locator);
				boolean allVisibleAndEnabled = foundElements.stream().allMatch(e -> e.isDisplayed() && e.isEnabled());
				return allVisibleAndEnabled ? foundElements : null;
			});
			// Convert to ArrayList
			rowElements = new ArrayList<>(elements);
			// Optional: Visual feedback for each row
			for (WebElement row : rowElements) {
				drawborder(row);
				// flash(row);
			}
			logger.info("All table row elements are now visible and enabled.");
		} catch (TimeoutException e) {
			logger.warn(LogColor.RED + "Table row elements with locator " + locator + " not found within timeout."
					+ LogColor.RESET);
		}
		return rowElements;

	}

	public void waitForNetworkIdle() {

		logger.info("Wait for Web Page to load completely");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		long lastCount = -1;
		long sameCountTimes = 0;
		for (int i = 0; i < 100; i++) {
			long currentCount = (long) js.executeScript(
					"return window.performance.getEntriesByType('resource').filter(r => !r.responseEnd).length;");
			if (currentCount == lastCount) {
				sameCountTimes++;
				if (sameCountTimes >= 3)
					break;
			} else {
				sameCountTimes = 0;
			}
			lastCount = currentCount;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

	}

	public WebElement waitForElement(WebElement locator) {

		logger.info("Checking visibility of the Element on browser screen");
		// Prioritize ExpectedConditions for efficiency
		Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT_SECONDS));
		try {
			wait.until(ExpectedConditions.visibilityOf(locator));
		} catch (TimeoutException e) {
			// Handle timeout exception (optional, log or throw as needed)
			logger.warn("Element with locator " + locator + " not found within timeout.");
			logger.error(LogColor.RED + "Exception occurred: ", e + LogColor.RESET);
		}
		// Check element visibility and enabled state only if necessary
		if (!locator.isDisplayed() || !locator.isEnabled()) {
			// Consider using a more specific ExpectedCondition or custom implementation
			wait.until(new ExpectedCondition<WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {

					if (locator.isDisplayed() && locator.isEnabled()) {
						return locator;
					} else {
						return null; // Wait for element to become both visible and enabled
					}

				}
			});
		}
		// Draw border and flash (consider optimization techniques)
		drawborder(locator); // Assuming `drawBorder` takes WebElement as parameter
//		flash(locator); // Assuming `flash` takes WebElement as parameter
		logger.info("Wait for the element is completed, Element is visible on the screen");
		return locator;

	}

// browserUtils	
	@SuppressWarnings("deprecation")
	public static WebElement fluentWait(final WebElement webElement, int timeinsec) {

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver())
				// .withTimeout(timeinsec, TimeUnit.SECONDS).pollingEvery(timeinsec,
				// TimeUnit.SECONDS)
				.withTimeout(Duration.ofSeconds(timeinsec)).pollingEvery(Duration.ofSeconds(timeinsec))
				.ignoring(NoSuchElementException.class);
		WebElement element = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				return webElement;

			}
		});
		return element;

	}

	// Wait for the Title of a webpage
	public boolean waitForTitle(String pageTitle) {

		logger.info("Wait for Page title to load");
		wait = new WebDriverWait(driver, Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS));
		return wait.until(ExpectedConditions.titleIs(pageTitle));

	}

	public void waitForClickablility(WebElement locator) {

		WebElement element;
//		try {
		WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT_SECONDS));
		element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		/*
		 * System.out.println("waiting till page is loaded successfully ");
		 * wait.until(ExpectedConditions.and(
		 * ExpectedConditions.elementToBeClickable(locator),
		 * ExpectedConditions.jsReturnsValue("return document.readyState == 'complete';"
		 * ) ));
		 * 
		 * System.out.println("page is loaded successfully ");
		 */
		if (element.isDisplayed() && element.isEnabled()) {
			drawborder(element);
			logger.info("element is visible and Clickable");
		} else {
			logger.info("Element is not clickable");
		}

//		}
		/*
		 * catch (Exception e) {
		 * 
		 * 
		 * WebDriverWait wait = new WebDriverWait(Driver.getDriver(),
		 * Duration.ofSeconds(timeout)); element=
		 * wait.until(ExpectedConditions.elementToBeClickable(locator));
		 * 
		 * logger.info("is element clickable ? : " +element.isEnabled()); }
		 */
	}
	// Wait for an Alert to appear

	public Alert waitForAlert() {

		logger.info("Wait for An alert to appear");
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(ELEMENT_WAIT_TIMEOUT_SECONDS))
				.pollingEvery(Duration.ofMillis(ELEMENT_POLLING_TIME_MILIS)).ignoring(NoAlertPresentException.class);
		Function<WebDriver, Alert> function = new Function<WebDriver, Alert>() {
			public Alert apply(WebDriver arg0) {

				logger.info("Waiting for the alert");
				Alert alert = driver.switchTo().alert();
				return alert;
				}
			};
		return wait.until(function);
	}
	
	public String getAlertText() {
	    try {
	        // Switch to the alert
	        Alert alert = waitForAlert();

	        // Get the text
	        String alertText = alert.getText();

	        // Log it for debugging
	        logger.info("⚠️ Alert text captured: " + alertText);

	        return alertText;
	    } catch (NoAlertPresentException e) {
	        throw new IllegalStateException("No alert is present to capture text.", e);
	    }
	}

	// wait for URL
	public static void waitForUrlContains(String expectedSubstring) {

		try {
			logger.info("waiting for URL to Contain: " + expectedSubstring);
			Duration timeToWaitInSec = Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS);
			WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeToWaitInSec);
			Boolean URlContainsExpect = wait.until(ExpectedConditions.urlContains(expectedSubstring));
			logger.info("URL Contains : " + expectedSubstring);
		} catch (Exception e) {
			logger.info("URL Doesn't Contains : " + expectedSubstring);
		}

	}

	/**
	 * -------------------------------------------------------------------------------------------------------------------------------------------
	 * --------------------------------------------------------Different Element
	 * Display Methods--------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------------------------------------
	 **/
	public boolean isElementPresent(By locator) {

		// Save the current implicit wait
		Duration originalImplicitWait = driver.manage().timeouts().getImplicitWaitTimeout();
		// Set implicit wait to zero
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(200)); // 1 second timeout
		try {
			removeBorder(); // removes border from previous element
//		        wait.until(ExpectedConditions.presenceOfElementLocated(locator)); 
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			drawborder(driver.findElement(locator));
			removeBorder();
			logger.info("isElelementPresent=True");
			return true;
		} catch (TimeoutException e) {
			return false;
		} finally {
			// Restore the original implicit wait
			driver.manage().timeouts().implicitlyWait(originalImplicitWait);
		}

	}

	public boolean isElementDisplayed(WebElement locator) {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
		// .implicitlyWait(0, TimeUnit.SECONDS);
		logger.info("Checking visibility of the Element on browser screen");
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(30))
					.pollingEvery(Duration.ofMillis(ELEMENT_POLLING_TIME_MILIS)).ignoring(Exception.class);
			Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
				public Boolean apply(WebDriver arg0) {

					Boolean isPresent = false;
					if (locator.isDisplayed()) {
						isPresent = true;
						logger.info("Wait for the element is completed, Element is visible on the screen");
					}
					return isPresent;

				}
			};
			boolean e = wait.until(function);
			return e;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean isElementStableAndVisible(WebElement element) {

		// Save the current implicit wait
		Duration originalImplicitWait = driver.manage().timeouts().getImplicitWaitTimeout();
		// Set implicit wait to zero
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(300)); // Slightly longer timeout
		try {
			removeBorder(); // Optional: clear previous highlights
			// Wait until the element is visible and not stale
			wait.until(driver -> {
				try {
					return element.isDisplayed();
				} catch (TimeoutException e) {
					return false;
				}
			});
			drawborder(element); // Optional: highlight the element
			removeBorder();
			logger.info("isElementStableAndVisible=True");
			return true;
		} catch (TimeoutException e) {
			logger.warn("Element not stable or visible within timeout.");
			return false;
		} finally {
			// Restore the original implicit wait
			driver.manage().timeouts().implicitlyWait(originalImplicitWait);
		}

	}

	public void highlightElement(WebElement locator) {

		removeBorder();
		for (int i = 0; i < 3; i++) {
			drawborder(locator);
			removeBorder();
		}

	}

	public boolean isElementPresentbyJS_ShadowRoot(By locator) {

//    	waitFor(3);
		try {
			String script = "return arguments[0].shadowRoot || arguments[0].getRootNode().host || arguments[0].getRootNode()";
			WebElement element = driver.findElement(locator);
			Object shadowRoot = ((JavascriptExecutor) driver).executeScript(script, element);
			return shadowRoot != null;
		} catch (NoSuchElementException e) {
			return false;
		}

	}

	public boolean isElementPresentbyJS(By locator) {

		try {
			waitForElement(locator);
			WebElement element = driver.findElement(locator);
			return true; // Element found
		} catch (NoSuchElementException | TimeoutException e) {
			return false; // Element not found
		}

	}

	/**
	 * -------------------------------------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------- Java Script
	 * Utilities ------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------------------------------------
	 **/
	public static void jsclick(WebDriver driver, WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", element);

	}
	
	

	// import org.apache.logging.log4j.Logger; (If not already present)

	/**
	 * Converts a date string from any common format (M/d/yyyy) to the standard 
	 * HTML input format (yyyy-MM-dd). This ensures date validation passes.
	 *
	 * @param dateValue The date string from the Excel file (e.g., "10/16/1980").
	 * @param sourceFormat The format of the dateValue (e.g., "M/d/yyyy" for flexible Excel dates).
	 * @return The date string in the target format (e.g., "1980-10-16").
	 */
	public static String standardizeDateFormat(String dateValue, String sourceFormat) {
	    
	    // The target format is the standard HTML input type="date" value format
	    final DateTimeFormatter TARGET_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    
	    // Create the formatter based on the format of the data coming from Excel
	    final DateTimeFormatter SOURCE_FORMATTER = DateTimeFormatter.ofPattern(sourceFormat);
	    
	    try {
	        // 1. Parse the date string from the source format
	        LocalDate date = LocalDate.parse(dateValue.trim(), SOURCE_FORMATTER);
	        
	        // 2. Format the LocalDate object into the target format
	        String targetDate = date.format(TARGET_FORMATTER);
	        
	        // Log success (optional, but helpful)
	        logger.debug("Date standardized: " + dateValue + " -> " + targetDate);
	        
	        return targetDate;
	        
	    } catch (Exception e) {
	        // If parsing fails (e.g., date value is garbage, blank, or not in expected format)
	        logger.error("❌ Failed to standardize date format. Value: '" + dateValue + 
	                     "' Source Format: '" + sourceFormat + "'. Error: " + e.getMessage());
	        
	        // Re-throw as a clean RuntimeException to stop the test with clear context
	        throw new RuntimeException("Date standardization failed for value: " + dateValue, e);
	    }
	}

	// Assume driver is accessible in CommonMethods (e.g., public static WebDriver driver;)

	// Inside CommonMethods.java

	/**
	 * The most robust method for date fields, temporarily bypassing stubborn app validation 
	 * and forcing the standardized date value (YYYY-MM-DD) into the element.
	 */
	// Inside CommonMethods.java (or your primary utility class)
	// Imports for logging, date/time, etc., should be present

	/**
	 * 🌟 ONE-STOP SHOP for setting date values. 
	 * Standardizes the date format (e.g., 10/16/1980 -> 1980-10-16), 
	 * sets the value using JavaScript to bypass validation, verifies the input, 
	 * and handles all logging.
	 *
	 * @param element The target Date input WebElement.
	 * @param excelDateValue The raw date string from Excel (e.g., "10/16/1980").
	 * @return true if the field's value attribute matches the expected standardized date, false otherwise.
	 */
	public static boolean safeSetDateValue(WebElement element, String excelDateValue) {
	    
	    // 1. STANDARDIZATION: Convert the expected date to the browser's required format (YYYY-MM-DD)
	    // Assume M/d/yyyy is the most common format from Excel.
	    String expectedStandardizedDate;
	    try {
	        expectedStandardizedDate = standardizeDateFormat(excelDateValue, "M/d/yyyy");
	    } catch (Exception e) {
	        logger.error("❌ Failed to standardize date format for value: " + excelDateValue + ". Aborting input.", e);
	        return false;
	    }

	    // 2. JS EXECUTION SETUP (Includes Validation Bypass)
	    JavascriptExecutor js = (JavascriptExecutor) driver; // Assumes 'driver' is static/accessible

	    String originalOnKeyUp = element.getAttribute("onkeyup");
	    String originalOnBlur = element.getAttribute("onblur");
	    
	    String script = 
	        // Temporarily disable the application's validation handlers
	        "arguments[0].onkeyup = null;" +
	        "arguments[0].onblur = null;" +
	        
	        // Set the value using the CORRECT, STANDARDIZED format
	        "arguments[0].value = arguments[1];" +
	        
	        // Fire the change event to ensure internal framework registration
	        "arguments[0].dispatchEvent(new Event('change'));";
	        
	    // 3. EXECUTE, LOG, and RESTORE HANDLERS
	    try {
	        js.executeScript(script, element, expectedStandardizedDate);
	        
	        // Restore the original handlers
	        js.executeScript("arguments[0].onkeyup = arguments[1];", element, originalOnKeyUp);
	        js.executeScript("arguments[0].onblur = arguments[1];", element, originalOnBlur);

	        // 4. SELF-VERIFICATION
	        String actualValue = element.getAttribute("value").trim();
	        
	        if (expectedStandardizedDate.equals(actualValue)) {
	            logger.info("✅ Date Input Success: Field populated with verified value: " + actualValue);
	            return true;
	        } else {
	            logger.error(String.format("❌ Date Input FAILURE: Value mismatch after set. Expected: [%s] | Actual: [%s]. The application's JS validation may be too aggressive.",
	                                    expectedStandardizedDate, actualValue.isEmpty() ? "<BLANK>" : actualValue));
	            return false;
	        }

	    } catch (Exception e) {
	        logger.error("❌ JS Execution Failed to set date value for element: " + element.toString(), e);
	        // Do not throw a RuntimeException here; return false to allow softAssert to handle it.
	        return false; 
	    }
	}
	

	public static void scrollIntoView(WebDriver driver, WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public static void scrollbottom(WebDriver driver) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");

	}

//     
	public static void changecolour(String color, WebElement element, WebDriver driver) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].style.backgroundColor='" + color + "'", element);
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//     https://html-color.codes/
	public static void flash(WebElement element) {

		String bgcolor = element.getCssValue("backgroundColor");
		System.out.println(bgcolor);
		for (int i = 0; i < 3; i++) {
//        		changecolour("#0000FF", element, driver); //blue
			changecolour("#f08080", element, driver);
			changecolour(bgcolor, element, driver);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void drawborder(WebElement element) {

		removeBorder();
//        	JavascriptExecutor js = (JavascriptExecutor)driver;	
		js.executeScript("arguments[0].style.border='3px solid red'", element);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removeBorder() {

		// Remove the border by setting the element's style
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Check for existing borders and remove them if present
		String scriptCheckAndRemoveBorders = "var elements = document.querySelectorAll('*');"
				+ "var borderExists = false;" + "for (var i = 0; i < elements.length; i++) {"
				+ "    if (window.getComputedStyle(elements[i]).border !== 'none') {" + "        borderExists = true;"
				+ "        break;" + "    }" + "}" + "if (borderExists) {"
				+ "    for (var i = 0; i < elements.length; i++) {" + "        elements[i].style.border='';" + "    }"
				+ "}";
		js.executeScript(scriptCheckAndRemoveBorders);
		// Wait for 3 seconds (adjust the duration as needed)
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void generateAlert(WebDriver driver, String message) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("alert('" + message + "')");

	}
	
    // Accepts the alert and returns its text
    public static void acceptAlert() {
        try {
            Alert alert = driver.switchTo().alert();

            // Capture the text before accepting
            String alertText = alert.getText();
           logger.info("⚠️ Alert text: " + alertText);

            // Click OK
            alert.accept();
           
        } 		
        
        catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);
			
		}
    }


	public void drawAndFlash(WebElement element) {

		drawborder(element);
		flash(element);

	}

	public void clickAndDraw(WebElement element) {

		removeBorder();
		waitForClickablility(element);
		// hoverAndClick(element);
		hoverOver(element);
		jsclick(driver, element);
//		waitForPageAndAjaxToLoad();
		
	}

	/**
	 * -------------------------------------------------------------------------------------------------------------------------------------------
	 * --------------------------------------------------------Different Reusable
	 * Methods--------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------------------------------------
	 **/
	// Verify Title :
	public boolean verifyPageTitle(String expectedTitle) {
		
		waitForPageAndAjaxToLoad();
	    if (expectedTitle == null || expectedTitle.isEmpty()) {
	        logger.error(LogColor.RED + "Expected title is null or empty!" + LogColor.RESET);
	        return false;
	    }

	    // Get actual title from browser
	    String actualTitle = driver.getTitle();

	    // Print both titles
	    logger.info(LogColor.DarkGreen + "Expected Title: " + expectedTitle + LogColor.RESET);
	    logger.info(LogColor.DarkGreen + "Actual Title From UI: " + actualTitle + LogColor.RESET);

	    // Compare
	    if (actualTitle.equals(expectedTitle.trim())) {
	        logger.info(LogColor.DarkGreen + "✅ Page title matched!" + LogColor.RESET);
	        return true;
	    } else {
	        logger.error(LogColor.RED + "❌ Page title mismatch!" + LogColor.RESET);
	        return false;
	    }
	}
	// Switch to Other Window:

	public void switchToAnotherWindow(String CurrentWin) {

		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> windows = driver.getWindowHandles();
		for (String s : windows) {
			if (!s.equalsIgnoreCase(CurrentWin)) {
				driver.switchTo().window(s);
				break;
			}
		}

	}

	public void hoverOver(WebElement element) {

		try {
			drawborder(element);
			Actions actions = new Actions(driver);
			actions.moveToElement(element).build().perform();
		} catch (NoSuchElementException e) {
			logger.info("Draw Border failed as element not present= " + element);
		}

	}
	
	

	public boolean checkDownloadAndDelete(String expectedFileName) {

		String projectPath = System.getProperty("user.dir") + "\\Downloads";
		boolean isFilePresent = new WebDriverWait(driver, Duration.ofSeconds(60)).until(driver -> {
			File dir = new File(projectPath);
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.getName().contains(expectedFileName)) {
						return true;
					}
				}
			}
			return false;
		});
		// Clean up the file after validation
		if (isFilePresent) {
			File dir = new File(projectPath);
			for (File file : dir.listFiles()) {
				if (file.getName().contains(expectedFileName)) {
					file.delete();
				}
			}
		}
		return isFilePresent;

	}

	public boolean checkDownloadAndDelete(String expectedFileName1, String expectedFileName2,
			String expectedFileName3) {

		String projectPath = System.getProperty("user.dir") + "\\Downloads";
		boolean isFilePresent = new WebDriverWait(driver, Duration.ofSeconds(60)).until(driver -> {
			File dir = new File(projectPath);
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.getName().contains(expectedFileName1) && file.getName().contains(expectedFileName2)
							&& file.getName().contains(expectedFileName3)) {
						return true;
					}
				}
			}
			return false;
		});
		// Clean up the file after validation
		if (isFilePresent) {
			File dir = new File(projectPath);
			for (File file : dir.listFiles()) {
				if (file.getName().contains(expectedFileName1) && file.getName().contains(expectedFileName2)
						&& file.getName().contains(expectedFileName3)) {
					file.delete();
				}
			}
		}
		return isFilePresent;

	}

	public void hoverOver(By elem) {

		try {
			WebElement element = driver.findElement(elem);
			drawborder(element);
			Actions actions = new Actions(driver);
			actions.moveToElement(element).build().perform();
		} catch (NoSuchElementException e) {
			logger.info("Draw Border failed as element not present= " + elem);
		}

	}

	public void hoverAndClick(WebElement element) {

		drawborder(element);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().build().perform();
		waitForMlsec(500);

	}
//------------------------------------------ Screenshot Try -----------------------------------------//

	public void captureAndAttachScreenshot(Scenario scenario, String screenshotName) {

		try {
			logger.info("Capturing Screenshot");
			// Take screenshot
			final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			// Attach screenshot to the report
			scenario.attach(screenshot, "image/png", screenshotName);
			logger.info("Screenshot Attached");
		} catch (Exception e) {
			logger.error("Error capturing screenshot: " + e.getMessage());
			// Optionally attach a message to the scenario
			scenario.attach(("Error capturing screenshot: " + e.getMessage()).getBytes(), "text/plain", "error");
		}

	}
	// Get text of an Element

	public String getElementText(WebElement element) {

		return element.getAttribute("textContent").trim();

	}

	// Get attribute value of an Element
	public String getAttributeValue(WebElement element, String attributeName) {

		if ("text".equalsIgnoreCase(attributeName)) {
			return getElementText(element);
		} else {
			return element.getAttribute(attributeName);
		}

	}

	public void scrollToElement(WebElement element) {

		((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + element.getLocation().y + ")");

	}
	// Compare two lists

	public boolean listCompare(List<String> expectedList, List<String> inputList) {

		boolean status = false;
		try {
			if (inputList != null && expectedList != null && (inputList.size() == expectedList.size())) {
				for (String opt : inputList) {
					if (expectedList.contains(opt.trim())) {
						status = true;
					} else {
						status = false;
					}
				}
			}
		} catch (Exception e) {
			status = false;
		}
		return status;

	}
	// Check file is downloaded in the given path

	public boolean isFileDownloaded(String filePath, String fileName) throws InterruptedException {

		boolean isFilePresent = false;
		try {
			File dir = new File(filePath);
			Thread.sleep(15000);
			File[] dir_contents = dir.listFiles();
			for (int i = 0; i < dir_contents.length; i++) {
				if (dir_contents[i].getName().contains(fileName))
					isFilePresent = true;
			}
		} catch (Exception e) {
			isFilePresent = false;
		}
		return isFilePresent;

	}

	public boolean isFileUpload(String filePath, String fileName) throws InterruptedException {

		boolean isFilePresent = false;
		try {
			File dir = new File(filePath);
			Thread.sleep(8000);
			File[] dir_contents = dir.listFiles();
			for (int i = 0; i < dir_contents.length; i++) {
				if (dir_contents[i].getName().contains(fileName))
					isFilePresent = true;
			}
		} catch (Exception e) {
			isFilePresent = false;
		}
		return isFilePresent;

	}

	public static void switchToWindowbyTitile(String targetTitle) {

		String origin = Driver.getDriver().getWindowHandle();
		for (String handle : Driver.getDriver().getWindowHandles()) {
			Driver.getDriver().switchTo().window(handle);
			if (Driver.getDriver().getTitle().equals(targetTitle)) {
				return;
			}
		}
		Driver.getDriver().switchTo().window(origin);

	}

	public String getChildWindowTitle() {

		String title = "";
		String mainWindow = driver.getWindowHandle();
		Set<String> set = driver.getWindowHandles();
		Iterator<String> itr = set.iterator();
		if (itr.hasNext()) {
			while (itr.hasNext()) {
				String childWindow = itr.next();
				if (!mainWindow.equals(childWindow)) {
					driver.switchTo().window(childWindow);
					waitForPageAndAjaxToLoad();
					title = driver.switchTo().window(childWindow).getTitle();
					driver.close();
				}
			}
			driver.switchTo().window(mainWindow);
		} else {
			logger.info("No Child window Opened");
			title = driver.getTitle();
		}
		logger.info("title of window " + title);
		return title;

	}

	public int getOpenWindowsCount() {

		try {
			Thread.sleep(4000);
		} catch (Exception e) {
		}
		// Get all open windows
		Set<String> windowHandles = driver.getWindowHandles();
		// Return the count of open windows
		return windowHandles.size();

	}

	public void selectFromDropdownByIndex(WebElement el, List<WebElement> optionList, int index) throws Exception {

		try {
			el.click();
			// List<WebElement>
			// optionList=driver.findElements(By.xpath("//div[contains(@class,'ui-selectmenu-open')]//a"));
			Thread.sleep(5000);
			for (int i = 0; i < optionList.size(); i++) {
				if (i == index) {
					waitForElement(optionList.get(i)).click();
					break;
				}
			}
		} catch (Exception e) {
			throw new Exception("Failed to click on option at index " + index);
		}

	}

	public static Select selectFromDropDownbyVisibleText(WebElement dropdown, String optionName) {

		dropdown.click();
		waitFor(1);
		Select select = new Select(dropdown);
		// List<WebElement> lis = Driver.getDriver().findElements(By.xpath(dropdown));
		// CommonMethods.waitFor(2);
		select.selectByVisibleText(optionName);
		// dropdown.click();
		return select;

	}

	public static Select selectFromDropDownbyValue(WebElement dropdown, String optionName) {

		dropdown.click();
		waitFor(1);
		Select select = new Select(dropdown);
		// List<WebElement> lis = Driver.getDriver().findElements(By.xpath(dropdown));
		// CommonMethods.waitFor(2);
		// select.selectByVisibleText(optionName);
		select.deselectByValue(optionName.trim());
		// dropdown.click();
		return select;

	}

//	   From Browser Utils
	public static String selectFromropDownRendomOption(WebElement dropdown) {

		Select select = new Select(dropdown);
		List<WebElement> i = select.getOptions();
		int size = i.size();
		int rendomeOption = randInt(0, (size - 1));
		String eachOption = i.get(rendomeOption).getText().trim();
		return eachOption;

	}

	public static int dropDownElementsInTotal(WebElement dropdown) {

		dropdown.click();
		Select dropDown = new Select(dropdown);
		List<WebElement> e = dropDown.getOptions();
		int itemCount = e.size();
		return itemCount;

	}

//	   From Browser Utils
	public static Select selectFromdropDownByStateAbrivation(WebElement dropdown, String optionName) {

		dropdown.click();
		Select dropDown = new Select(dropdown);
		List<WebElement> e = dropDown.getOptions();
		int itemCount = e.size();
		for (int l = 0; l < itemCount; l++) {
			logger.info(e.get(l).getText());
			if (e.get(l).getText().trim().contains(optionName)) {
				dropDown.selectByIndex(l);
				break;
			}
			continue;
		}
		return dropDown;

	}

//	   From Browser Utils
	public static void checkBoxYesNO(WebElement checkBox, String fromExcel) {

		if (fromExcel.equalsIgnoreCase("Yes") && checkBox.isSelected()) {
			logger.info("Check Box already selected for " + checkBox);
		} else if (fromExcel.equalsIgnoreCase("No") && !checkBox.isSelected()) {
			logger.info("Check box is not selected and not will be selected becouse is No for " + checkBox);
		} else {
			checkBox.click();
		}

	}

	public static void checkBox(WebElement webElement, String option) {

		switch (option) {
		case "yes":
			webElement.click();
			break;
		case "Yes":
			webElement.click();
			break;
		case "no":
			logger.info("There is no in excel sheet no Suppress Interest Calculation");
		}

	}

	/**
	 * 
	 * Scroll screen to a particular element
	 * 
	 * 
	 * 
	 * @param element
	 * 
	 */
	public void scrollScreen(WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block: \"center\", inline: \"center\"});", element);

	}

// **********************  Random Number Methods *******************************
	public static int randInt(int min, int max) {

		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;

	}

	public String getRandomNumber() {

		Random r = new Random();
		int num = Math.abs(r.nextInt()) / 10000;
		;
		return Integer.toString(num);

	}

	public static int getDecimalRandomNumber() {

		// create instance of Random class
		Random rand = new Random();
		// Generate and return Random number with decimal
		return rand.nextInt();

	}

	public static void rendomFromDD(String webelement) {

		List<WebElement> options = Driver.getDriver().findElements(By.xpath(webelement));
		Random rand = new Random();
		int list = rand.nextInt(options.size());
		options.get(list).click();

	}

	public static int rendomNumberWithin(int min, int max) {

		// define the range
		int range = max - min + 1;
		int rand = 0;
		// generate random numbers within 1 to 10 i=min-1,i<max,i++
		for (int i = min; i <= max; i++) {
			rand = (int) (Math.random() * range) + min;
			// Output is different everytime this code is executed
			// logger.info("FROM INSIDE FOREACH "+rand);
		}
		return rand;

	}
//********************** End Of - Random Number Methods *******************************

//	   From Browser Utils: 
	public static ArrayList<String> removeDuplicates(ArrayList<String> manufacturerCodeList2) {

		Set<String> set = new LinkedHashSet<>();
		set.addAll(manufacturerCodeList2);
		manufacturerCodeList2.clear();
		manufacturerCodeList2.addAll(set);
		// logger.info("manufacturerCodeList2");
		return manufacturerCodeList2;

	}

	public static int numberOfTheRowsDynamicTable(String partOfTheXpath) {

		List<WebElement> rows = Driver.getDriver().findElements(By.xpath(partOfTheXpath));
		int rowNumber = rows.size();
		return rowNumber;

	}

	public String GetFuture_EST_Date(int days) {

		SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		sd.setTimeZone(TimeZone.getTimeZone("EST"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return sd.format(cal.getTime());

	}
//Shams Addition	 

	public String yesterdaysDate() { // Get the current date

		Date currentDate = new Date();
		// Subtract 2 days from the current date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, -2);
		Date modifiedDate = calendar.getTime();
		// Define the desired date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		// Convert the modified date to the desired format
		String formattedDate = dateFormat.format(modifiedDate);
		// Print the formatted date
		logger.info("Current date minus 2 days in mm/dd/yyyy format: " + formattedDate);
		return formattedDate;

	}

	public void clearDownloadFolder() throws IOException {

		String projectPath = System.getProperty("user.dir");
		String downloadFoderPath = "runtime" + File.separator + "downloads";
		File downloadFolder = new File(projectPath + File.separator + downloadFoderPath);
		if (downloadFolder.exists()) {
			if (downloadFolder.listFiles().length > 0) {
				for (File file : downloadFolder.listFiles()) {
					FileDeleteStrategy.FORCE.delete(file);
				}
			}
		} else {
			downloadFolder.mkdir();
		}

	}

	public int getFileCountInDownloadFolder() {

		String projectPath = System.getProperty("user.dir");
		String downloadFoderPath = "runtime" + File.separator + "downloads";
		File downloadFolder = new File(projectPath + File.separator + downloadFoderPath);
		File[] dir_contents = downloadFolder.listFiles();
		return dir_contents.length;

	}

	/**
	 * 
	 * Get weekend dates of a given month & year
	 * 
	 * 
	 * 
	 * @param month - month of a Year
	 * 
	 * @param year  - Year
	 * 
	 * @return
	 * 
	 */
	public String leftPadStringWithLeadingZeroes(Integer n, String str) {

		// n -> Size of the string to be generated
		// str -> String to be padded with Zeros
		String format = "%0" + n + "d";
		String str1 = String.valueOf(String.format(format, Integer.parseInt(str)));
		return str1; // return String

	}

	public String getCurrentDate(String dateFormate) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormate);
		Date d = new Date();
		return dateFormat.format(d).toString();

	}

	public String getDateInFormat(String input, String inputDF, String outputDF) {

		String finalInput = input;
		if (inputDF.length() - input.length() > 0) {
			finalInput = leftPadStringWithLeadingZeroes(inputDF.length(), input);
		}
		DateFormat fmt1 = new SimpleDateFormat(inputDF);
		Date date = null;
		try {
			date = fmt1.parse(finalInput);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DateFormat fmt2 = new SimpleDateFormat(outputDF);
		String reqFmtDate = fmt2.format(date);
		return reqFmtDate;

	}

//	   From Browser Utils
	public static String date(String dateFromExcel) {

		if (dateFromExcel.substring(0, 1).equals("0")) {
			String date = months(dateFromExcel.substring(3, 6)) + "/" + dateFromExcel.substring(1, 2) + "/"
					+ dateFromExcel.substring(7);
			// When reading from excel in format 01-Mar-2019 but application 3/1/2019
			return date;
		} else {
			String date = months(dateFromExcel.substring(3, 6)) + "/" + dateFromExcel.substring(0, 2) + "/"
					+ dateFromExcel.substring(7);
			return date;
		}

	}

	public String getCurrentWindowHandle() {

		return driver.getWindowHandle();

	}

	public String getChildWindowUrl() {

		String mainWindow = driver.getWindowHandle();
		String url1 = "";
		Set<String> set = driver.getWindowHandles();
		Iterator<String> itr = set.iterator();
		if (itr.hasNext()) {
			while (itr.hasNext()) {
				String childWindow = itr.next();
				if (!mainWindow.equals(childWindow)) {
					driver.switchTo().window(childWindow);
					waitForPageAndAjaxToLoad();
					url1 = driver.getCurrentUrl();
					driver.close();
				}
			}
			driver.switchTo().window(mainWindow);
		}
		return url1;

	}

	public String getWeekeendDates(int month, int year) {

		int y = year;
		Month m = Month.of(month);
		List<Integer> weekendDate = new ArrayList<Integer>();
		IntStream.rangeClosed(1, YearMonth.of(y, m).lengthOfMonth()).mapToObj((day) -> LocalDate.of(y, m, day))
				.filter(date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
				.forEach(date -> weekendDate.add(date.getDayOfMonth()));
		String date = month + "/" + weekendDate.get(2) + "/" + year;
		return getDateInFormat(date, "M/d/yyyy", "MM/dd/yyyy");

	}
//        ********************** from  browser Utils *******************

	public static String getElementText_P(WebElement element) {

		// WebElement element=Driver.getDriver().findElement(locator);
		String text = "";
		if (!element.getText().isEmpty()) {
			text = element.getText();
			logger.info("text on element is " + text);
		}
		return text;

	}

	public static List<String> getElementsTextByPassingElementList(List<WebElement> list) {

		List<String> elemTexts = new ArrayList<>();
		for (WebElement el : list) {
			if (!el.getText().isEmpty()) {
				elemTexts.add(el.getText());
			}
		}
		return elemTexts;

	}

	public static List<String> getElementsTextbyLocator(By locator) {

		List<WebElement> elems = Driver.getDriver().findElements(locator);
		List<String> elemTexts = new ArrayList<>();
		for (WebElement el : elems) {
			if (!el.getText().isEmpty()) {
				elemTexts.add(el.getText());
			}
		}
		return elemTexts;

	}

	public void menuIsVisible(WebElement menu, By locator, int retryCount) {

		int currentRetryCount = 0;
		clickAndDraw(menu);
		try {
			WebElement element = driver.findElement(locator);
			waitFor(1);
			drawborder(element);
			logger.info("----------------- Menu items are visible -------------------");
			waitFor(1);
			clickAndDraw(menu);
		} catch (NoSuchElementException e) {
			if (currentRetryCount < retryCount) {
				currentRetryCount = currentRetryCount + 1;
				logger.info("----------------- Menu items not visible refreshing for time= " + currentRetryCount
						+ " ------------");
				driver.navigate().refresh();
				menuIsVisible(menu, locator, retryCount);
			} else {
				logger.info("Menu items are not visible after 3 retries.");
				// Handle the failure or throw an exception if needed.
			}
		}

	}

	public void waitTillLoadingScreenVanishes() {

		logger.info("Vanishing Loading Screen Method");
		Duration timeout = Duration.ofSeconds(180); // explicit wait 15 sec max limit
//		String loadingImageSelector = ".cs-loader";  // the black screen 
		By loadingImage = By.cssSelector(".cs-loader");
//		waitForNetworkIdle();
		waitFor(1);
		try {
			// Check if the loading image element exists
			if (isElementPresent(loadingImage)) {
				logger.info("Vanishing loading screen method - Loading spinning image is present");
				wait = new WebDriverWait(driver, timeout);
				WebElement loadingImageElement = waitForElement(loadingImage);
				// Wait for either the loading image to vanish or the page to refresh
				wait.until(ExpectedConditions.or(ExpectedConditions.invisibilityOf(loadingImageElement),
						ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(loadingImageElement))));
				logger.info("######## Loading screen is vanished ##############");
			} else {
				logger.info("######## Loading image element not found now. good to go ##############");
			}
		} catch (Exception e) {
			logger.info("######## Loading screen is not vanished, Refreshing the page ##############");
//			driver.navigate().refresh();
			logger.error(LogColor.RED + "Exception occurred: ", e + LogColor.RESET);
			waitTillLoadingScreenVanishes(); // Recursive call to retry after page refresh
		}

	}

	public static void printandlogAllAttributes(WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String script = "var items = {}; " + "for (var i = 0; i < arguments[0].attributes.length; ++i) { "
				+ "    items[arguments[0].attributes[i].name] = arguments[0].attributes[i].value; " + "} "
				+ "return items;";
		@SuppressWarnings("unchecked")
		Map<String, String> attributes = (Map<String, String>) js.executeScript(script, element);
		logger.info("Logging attributes for element: " + element.toString());
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			logger.info("Attribute: " + entry.getKey() + " = " + entry.getValue());
		}

	}

	public static boolean isPageLoaded(int timeOutInSec) {

		boolean isPageLoaded = false;
		Duration timeOutInSeconds = Duration.ofSeconds(timeOutInSec);
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {

				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");

			}
		};
		try {
			if (!expectation.equals(true)) {
				logger.info("************************ Page is refreshing *********************************");
				Driver.getDriver().navigate().refresh();
				WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeOutInSeconds);
				wait.until(expectation);
				if (expectation.equals(true)) {
					logger.info("Page is loaded Successfully");
				}
				isPageLoaded = true;
			} else if (!expectation.equals(false)) {
				logger.info("Page is Loaded ");
				isPageLoaded = true;
			}
			return isPageLoaded;
		} catch (Throwable error) {
			// logger.info(
			// "Timeout waiting for Page Load Request to complete after " + timeOutInSeconds
			// + " seconds");
			return isPageLoaded;
		}

	}

	public static String phoneNumberThreePart(String phoneNumber) {

		String phone = null;
		logger.info("fax number lenght " + phoneNumber.length());
		if (phoneNumber.length() != 0) {
			phone = phoneNumber.substring(0, 3) + Keys.TAB + phoneNumber.substring(4, 7) + Keys.TAB
					+ phoneNumber.substring(8);
		} else {
			logger.info("There is no Number.");
		}
		return phone;

	}

	public static void safeSendKeys(WebElement element, String text) {
//	    WebElement element = driver.findElement(locator);

	    // Step 1: Check if element is enabled
	    if (!element.isEnabled() || !element.isDisplayed()) {
	        throw new IllegalStateException("Element located by " + element.toString() + " is disabled and cannot accept input.");
	    }

	    // Step 2: Clear existing text
	    element.clear();

	    // Step 3: Send the text
	    element.sendKeys(text);

	    // Step 4: Verify the text was entered correctly
	    String actualValue = element.getAttribute("value");
	    if (!text.equals(actualValue)) {
	        throw new AssertionError("Text mismatch! Expected: " + text + " but found: " + actualValue);
	    }

	    // Optional: Log success
	    logger.info("✅ Text '" + text + "' successfully entered into element: " + element.toString());
	}
	
	/**
	 * Safely select a radio button element.
	 * - Verifies element is enabled and displayed
	 * - Clicks the radio button
	 * - Confirms it is selected
	 * - Logs success or throws an error if selection fails
	 */
	public static void safeSelectRadioButton(WebElement element) {
	    // Step 1: Check if element is enabled and displayed
	    if (!element.isEnabled() || !element.isDisplayed()) {
	        throw new IllegalStateException("Radio button located by " + element.toString() + " is disabled or not visible.");
	    }

	    // Step 2: Click the radio button
	    element.click();

	    // Step 3: Verify the radio button is selected
	    if (!element.isSelected()) {
	        throw new AssertionError("Radio button selection failed for element: " + element.toString());
	    }

	    // Optional: Log success
	    logger.info("✅ Radio button successfully selected: " + element.toString());
	}

	public static String months(String optionName) {

		String month = "";
		switch (optionName) {
		case "Jan":
			month = "1";
			break;
		case "Feb":
			month = "2";
			break;
		case "Mar":
			month = "3";
			break;
		case "Apr":
			month = "4";
			break;
		case "May":
			month = "5";
			break;
		case "Jun":
			month = "6";
			break;
		case "Jul":
			month = "7";
			break;
		case "Aug":
			month = "8";
			break;
		case "Sep":
			month = "9";
			break;
		case "Oct":
			month = "10";
			break;
		case "Nov":
			month = "11";
			break;
		case "Dec":
			month = "12";
			break;
		}
		return month;

	}
}