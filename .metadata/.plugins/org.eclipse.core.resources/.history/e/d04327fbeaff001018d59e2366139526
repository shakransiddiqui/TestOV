package ov.pages.passportos;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ov.utilities.CommonMethods;
import ov.utilities.Driver;
import ov.utilities.LogColor;

public class PassportOSAdminPage_POM extends CommonMethods {

	public WebDriver driver;
	public WebDriverWait wait;

	public static final Logger logger =
			LogManager.getLogger(PassportOSAdminPage_POM.class);

	public PassportOSAdminPage_POM() {
		this.driver = Driver.getDriver();
		PageFactory.initElements(driver, this);
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	//	**********************************************************************************************************************************************************

	// ====== X-path Locators ======


	private static final String admin_Page_textElelments = "//*[text()='%s']";


	// ====== Admin_Page Buttons X-path Template ======

	private static final String admin_page_buttons = "//button[text()='%s']";

	private static final String admin_page_CreateUser_field = "//*[text()='%s']";
	
	By by_admin_page_CreateUser_field = By.xpath("//input[@placeholder='Enter Email or Select Existing User']");

	// ====== By Locators ======
	
	

	//	***************************************************************************************************************
	public boolean clickOnButton(String buttonName) {
		try {
			logger.info("Formating the xpath of the button: "+buttonName);
			String formattedXpath= String.format(admin_page_buttons, buttonName);
			logger.info(formattedXpath);

			logger.info("Finding the button : "+buttonName);
			WebElement button = driver.findElement(By.xpath(formattedXpath));

			logger.info("Clicking on "+buttonName+" button ");
			clickAndDraw(button);
			logger.info(buttonName+" button is present and clicked on it");

			return true;
		} catch (Exception e) {

			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false ;
		}
	}

	public boolean verify_AdminPage_UiTextElement_is_visible(String UiTextElement_fromSD) {

		try {

			String formattedStringXpathOf_Ui_Element =String.format(admin_Page_textElelments, UiTextElement_fromSD);
			logger.info(formattedStringXpathOf_Ui_Element);

			boolean presence=isElementPresent(By.xpath(formattedStringXpathOf_Ui_Element));
			if(presence) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (Exception e) {

			logger.error(LogColor.RED + e + LogColor.RESET);
			return false;
		}
	}


	//	public String passFieldValueAndEnter(String fieldValue, String fieldName) {
	//
	//		try {
	//			logger.info("Formatting xpath for field: " + fieldName);
	//			String formattedXpath = String.format(admin_page_CreateUser_field, fieldName);
	//			logger.info("formatted Xpath: "+formattedXpath);
	//
	//			WebElement field = driver.findElement(By.xpath(formattedXpath));
	//
	//			logger.info("Clicking field: " + fieldName);
	//			//	        field.click();
	//			clickAndDraw(field);
	//
	//
	//			// ============================================================
	//			// BREAKINGPOINT
	//			// ============================================================
	//			
	//			logger.info("Sending value: " + fieldValue);
	//			safeSendKeys(field, fieldValue);
	//			
	//			
	//			// Press ENTER via reusable utility
	//			pressKeys(driver, KeyboardKey.ENTER);
	//
	//			String actualValue = field.getAttribute("value");
	//			logger.info("Actual field value: " + actualValue);
	//
	//			return actualValue;
	//
	//		} catch (Exception e) {
	//			logger.error(LogColor.RED+"Failed to pass value and press Enter"+LogColor.RESET);
	//			logger.error(LogColor.RED+e+LogColor.RESET);
	//			return null;
	//		}
	//	}

	public String passFieldValueAndEnter(String fieldValue, String fieldName) {
	    String actualValue = null;
	    try {
	        logger.info("Starting passFieldValueAndEnter for field: " + fieldName);

	        // Target the actual input field by placeholder
	        By locator = By.xpath("//input[@placeholder='" + fieldName + "']");
	        logger.info("Locator built: " + locator.toString());

	        // Wait for element
	        logger.info("Waiting for element visibility: " + fieldName);
	        WebElement field = waitForElement(locator);
	        logger.info("Element located and visible: " + fieldName);

	        // Ensure clickable
	        waitForClickablility(field);
	        logger.info("Element is clickable: " + fieldName);

	        // Send keys safely
	        logger.info("Sending value: " + fieldValue + " into field: " + fieldName);
	        safeSendKeys(field, fieldValue);

	        // Press Enter
	        logger.info("Pressing ENTER key after input");
	        pressKeys(driver, KeyboardKey.ENTER);

	        // Verify value
	        if (isElementStableAndVisible(field)) {
	            actualValue = field.getAttribute("value");
	            logger.info("Actual field value retrieved: " + actualValue);
	        } else {
	            logger.warn("Element became stale or invisible after input: " + fieldName);
	        }
	        
	    } catch (Exception e) {
	        logger.error("Failed to pass value and press Enter for field: " + fieldName);
	        logger.error("Exception: " + e.getMessage(), e);
	    }
	    return actualValue;
	}

}


