package ov.Hooks;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import  ov.utilities.CommonMethods;
import  ov.utilities.DBUtil;
import  ov.utilities.Driver;
import  ov.utilities.LogColor;
import  ov.utilities.ScreenshotUtil;
import ov.utilities.SftAssert;
import io.cucumber.java.*;

public class Hooks extends CommonMethods {

	String fileName;
	String scenarioName;
	PrintStream console;
	PrintStream oStream;
	String currentStepName;
	private String scenarioAltId;

	long implicitWaitValue = 15;

	public static final Logger logger = LogManager.getLogger(Hooks.class);

	public static SftAssert softAssert;
	public static ScreenshotUtil screenshotUtil;
	public static WebDriver driver;


	@BeforeAll
	public static void beforeAll() {
		logger.info(LogColor.Indigo + " @Before All Hooks " + LogColor.RESET);

		Driver.BrowserSetup();

		String browser = System.getProperty("browser");
		if ("edge-headless".equalsIgnoreCase(browser)) {
			String tag = System.getProperty("cucumber.filter.tags", "No tag provided");
		}

		logger.info(LogColor.Purple + " Browser Setup " + LogColor.RESET);
	}

	@Before
	public void scenarioName(Scenario scenario) {
		logger.info(LogColor.Indigo + " @Before hook " + LogColor.RESET);

		logger.info(LogColor.DarkGreen + "================================================================" + LogColor.RESET);
		scenarioName = scenario.getName().trim();
		logger.info(LogColor.DarkGreen + "Scenario Title: " + scenario.getName() + LogColor.RESET);
		logger.info(LogColor.DarkGreen + "================================================================" + LogColor.RESET);



		// Ensure driver is initialized
		if (Driver.getDriver() == null) {
			logger.info(LogColor.Purple + "Driver Found Null in Before Hooks , Launching again " + LogColor.RESET);
			Driver.BrowserSetup();
		}

		driver = Driver.getDriver(); // ✅ Assign driver

		logger.info(LogColor.Purple + "Assigning the Driver" + LogColor.RESET);
		
		// Initialize ExtentTest and ScreenshotUtil
		ExtentTest test = ExtentCucumberAdapter.getCurrentStep(); // ✅ Get current ExtentTest
		screenshotUtil = new ScreenshotUtil(driver, test);        // ✅ Create ScreenshotUtil

		// Initialize softAssert and make it globally accessible
//		softAssert = new SftAssert(driver, screenshotUtil);       // ✅ Local instance
//		CommonMethods.softAssert = softAssert;                    // ✅ Global access
		
		//********************************UPDATED*****************************************************
		softAssert = new SftAssert(driver, screenshotUtil);   // ✅ Local instance
		CommonMethods.softAssert = softAssert;							// ✅ Global access
		//********************************************************************************************
	}

	@BeforeStep
	public static void beforeStep() {
		logger.info(LogColor.Indigo + " @Before Step Hooks " + LogColor.RESET);
	}

	@AfterStep
	public void takeScreenshot(Scenario scenario) {
		logger.info(LogColor.Indigo + "  @after step hook " + LogColor.RESET);
		
		//********************************UPDATED**************************************************************************
//		 if (softAssert != null && softAssert.getFailureCount() > 0) {
//		        scenario.log("Scenario marked FAILED due to soft assertion failure");
//		        throw new AssertionError("Soft assertion failure detected");
//		    }
		 //****************************************************************************************************************
		 
		String screenshotName = scenario.getName().replaceAll(" ", "_") + "_" + System.currentTimeMillis();
		captureAndAttachScreenshot(scenario, screenshotName);
	}

	@After
	public void after(Scenario scenario) {
		logger.info(LogColor.Indigo + " @after hook " + LogColor.RESET);
		logger.info(LogColor.DarkGreen + "================================================================" + LogColor.RESET);
		logger.info(LogColor.DarkGreen + scenario.getName() + " :- Status - " + scenario.getStatus() + LogColor.RESET);
		logger.info(LogColor.DarkGreen + "================================================================" + LogColor.RESET);

		// Extract custom status from tags if available
		String status = extractStatusFromTags(scenario.getSourceTagNames());
		if (status == null) {
			status = scenario.isFailed() ? "FAIL" : "PASS"; // fallback
		}



		softAssert.AssertAll(); // Logs all failures with screenshots
	}

	@AfterAll
	public static void tearDown() {
		logger.info(LogColor.Indigo + " @AfterAll Hooks " + LogColor.RESET);

		logger.info("Deleting cookies");
		Driver.getDriver().manage().deleteAllCookies();

		logger.info("Closing the Driver");
		try {
			Driver.closeDriver();
		} catch (Exception e) {
			logger.info("Connection reset handled");
		}

		logger.info("Driver Closed");

		String tempUserDataDir = System.getProperty("edge.temp.profile");
		if (tempUserDataDir != null) {
			try {
				FileUtils.deleteDirectory(new File(tempUserDataDir));
				System.out.println("Deleted temp Edge profile: " + tempUserDataDir);
			} catch (IOException e) {
				System.err.println("Failed to delete temp Edge profile: " + e.getMessage());
			}
		}
	}

	/** ----------Conditional Hooks with Different Tags --------------- **/

	@Before(value = "@sql")
	public void establishDBConnection() {
		try {
			System.out.println("Before hooks for SQL as scenario had @sql Tag");
			DBUtil.establishSQLServerConnectionFromSecret();
		} catch (SQLException e) {
			System.out.println("SQL hook catch block ");
			try {
				DBUtil.establishSQLServerConnectionDirectly();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	@After(value = "@sql")
	public void closeConnection() {
		logger.info("@After hooks for SQL - Closing Database Connection");
		DBUtil.closeConnection();
	}


	//extra methods used here 
	private String extractAltIdFromTags(Collection<String> tags) {
		for (String tag : tags) {
			if (tag.startsWith("@AltID=")) {
				return tag.substring("@AltID=".length());
			}
		}
		return null; // or throw an exception if AltID is mandatory
	}

	private String extractStatusFromTags(Collection<String> tags) {
		for (String tag : tags) {
			if (tag.startsWith("@Status=")) {
				return tag.substring("@Status=".length()).toUpperCase();
			}
		}
		return null; // fallback to default
	}



}
