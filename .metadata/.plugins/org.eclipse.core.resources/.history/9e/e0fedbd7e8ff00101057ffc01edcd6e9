package ov.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.Scenario;

public class SftAssert extends Assertion {

	private WebDriver driver;
	private ScreenshotUtil screenshotUtil;
//	private Scenario scenario;
	// CHANGED: Removed 'static' to ensure thread safety during parallel execution 
	private SoftAssert softAssert; 
	private List<String> failureMessages;


	private static final Logger logger = LogManager.getLogger(SftAssert.class);
	private int failureCount = 0;

	// Constructor
	public SftAssert(WebDriver driver, ScreenshotUtil screenshotUtil) {
		this.driver = driver;
		this.screenshotUtil = screenshotUtil;
//		this.scenario = scenario;
		this.softAssert = new SoftAssert(); // Initialize per instance
		this.failureMessages = new ArrayList<>();
	}

	public void softAssertFail(String message) {
		String finalMsg = "Forced Failure: " + message;
		handleFail(finalMsg);
		getSoftAssert().fail(message);
	}

	public void softAssertEquals(Object actual, Object expected, String message) {
		// Use Objects.equals for null-safe comparison
		if (!Objects.equals(actual, expected)) {
			String failMsg = message + " [Expected: " + expected + ", Actual: " + actual + "]";
			handleFail(failMsg);
			getSoftAssert().assertEquals(actual, expected, message);
		} else {
			handlePass(message);
			getSoftAssert().assertEquals(actual, expected, message);
		}
	}

	public void softAssertTrue(boolean condition, String passMessage, String failMessage) {
		if (condition) {
			handlePass(passMessage);
			getSoftAssert().assertTrue(true, passMessage);
		} else {
			handleFail(failMessage);
			getSoftAssert().assertTrue(false, failMessage);
		}
	}

	// NEW ADDITION: Useful companion to softAssertTrue
	public void softAssertFalse(boolean condition, String passMessage, String failMessage) {
		if (!condition) {
			handlePass(passMessage);
			getSoftAssert().assertFalse(false, passMessage);
		} else {
			handleFail(failMessage);
			getSoftAssert().assertFalse(true, failMessage);
		}
	}

	// --- Helper Methods to clean up logic ---

	private void handlePass(String message) {
		String logMsg = LogColor.Blue + "Assert Passed :---------------------- " + message + LogColor.RESET;
		logger.info(logMsg);
		if (screenshotUtil != null) {
			// Only capturing info if specifically requested by logic (matches original source [cite: 8])
			screenshotUtil.captureInfo(message);
		}
	}

	private void handleFail(String message) {
		failureCount++;
		failureMessages.add(message); // Track message for AssertAll reporting

		String logMsg = LogColor.RED + "Assert Failed :------------------------ " + message + LogColor.RESET;
		logger.info(logMsg); // [cite: 15]

		if (screenshotUtil != null) {
			screenshotUtil.captureFailure(message); // [cite: 16]
		}

		//*****************UPDATED***********************************************************
		// 🔥 THIS is what tells Cucumber the scenario failed and test does not continue with further steps.
				throw new AssertionError(message);
		//***********************************************************************************
	}

	public int getFailureCount() {
		return failureCount;
	}

	public SoftAssert getSoftAssert() {
		if (this.softAssert == null) {
			this.softAssert = new SoftAssert();
		}
		return this.softAssert;
	}

	public void assertAll(ExtentTest test) {
		// Log all collected failure messages to the Extent Report
		for (String msg : failureMessages) {
			test.fail(msg); 
		}
		// Actually trigger the TestNG failure if any occurred
		if (softAssert != null) {
			softAssert.assertAll();
		}
		// Clean up for next run (though usually a new instance is created)
		failureMessages.clear(); 
		failureCount = 0;
	}

	public void AssertAll() {
		if (softAssert != null) {
			softAssert.assertAll();
		}
		failureMessages.clear();
		failureCount = 0;
	}
}
