package ov.pages.passportos;

import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import ov.utilities.Driver;
import ov.utilities.LogColor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.WebDriverWait;



import java.time.Duration;

public class PassportOSLogin_POM extends CommonMethods {

	public static final Logger logger =
			LogManager.getLogger(PassportOSLogin_POM.class);

	public PassportOSLogin_POM() {
		PageFactory.initElements(driver, this);
	}

	
//	***************************************************************************************************************
	
	// ====== Locators ======

	@FindBy(xpath = "//button[text()='Continue']")
	public WebElement continueButton;


	@FindBy(xpath = "//span[text()='COMMUNITY' and contains(@class,'active')]")
	public WebElement communityDashbaordPage_logo;

	
	
	
	// ====== By Locators ======

	By by_continueButton=By.xpath("//button[text()='Continue']");

	By by_communityDashbaordPage_logo = By.xpath("//span[text()='COMMUNITY' and contains(@class,'active')]");


	
	
	// ====== Error Message Locator ======

	By by_loginErrorMessage = By.xpath("//span[contains(@class,'ulp-input-error-message') and not(contains(@class,'screen-reader-only'))]");


	
	
	
	// ====== Login Field X-path Template ======

	private static final String login_fields_xpath_format = "//label/following-sibling::input[@id='%s']";

	private static final String login_button_continue = "//button[text()='%s']";


//	***************************************************************************************************************

	// ====== Actions ======

	//****************************************************************************************************
	public boolean validate_passportOS_login_page_title(String pageTitle_fromSD) {

		try {

			String pageTitle= ConfigurationReader.getProperty(pageTitle_fromSD);

			boolean TitleIsMatched = verifyPageTitle(pageTitle);

			return TitleIsMatched;

		} catch (Exception e) {
			logger.warn(LogColor.RED+e+LogColor.RESET);
			return false;
		}
	}

	//****************************************************************************************************
	public String EnterValueToLoginField(String fieldValue_fromSD, String fieldName_fromSD) {

		try {

			logger.info("Formating the xpath of the field: "+fieldName_fromSD);
			String formattedXpath= String.format(login_fields_xpath_format, fieldName_fromSD);
			logger.info("formatted Xpath: "+formattedXpath);

			logger.info("Finding the field : "+fieldName_fromSD);
			WebElement field = driver.findElement(By.xpath(formattedXpath));

			logger.info("Clicking on the field: "+fieldName_fromSD);
			clickAndDraw(field);

			logger.info("Passing the value : "+fieldValue_fromSD +" to the field: "+fieldName_fromSD);
			safeSendKeys(field, fieldValue_fromSD);

			logger.info("Getting actual value from the field: "+fieldName_fromSD);
			String actualFieldValue = field.getAttribute("value");
			logger.info("Actual Value from the field is: "+actualFieldValue);


			return actualFieldValue;

		} catch (Exception e) {
			logger.warn(LogColor.RED+e+LogColor.RESET);

			return null;
		}

	}

	//****************************************************************************************************
	public boolean clickOnContinueButton(String buttonName) {
		try {
			logger.info("Formating the xpath of the button: "+buttonName);
			String formattedXpath= String.format(login_button_continue, buttonName);
			logger.info(formattedXpath);

			logger.info("Finding the button : "+buttonName);
			WebElement button = driver.findElement(By.xpath(formattedXpath));

			logger.info("Clicking on continue button ");
			clickAndDraw(button);
			logger.info(buttonName+" button is present and clicked on it");

			return true;
		} catch (Exception e) {

			logger.warn(LogColor.RED+e+LogColor.RESET);
			return false ;

		}
	}
	//****************************************************************************************************
	public boolean isCommunityDashboardDisplayed() {

		try {

			////		logger.info("Verifying the Page Title");
			////		boolean pageTitleMatched = verifyPageTitle(expectedPageTitle);
			////
			////		return pageTitleMatched;

			WebElement logo_communityDashboard = waitForElement(by_communityDashbaordPage_logo);

			return logo_communityDashboard.isDisplayed();

		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);
			return false;

		}
	}

	//	**********************************************************************************************************************************************************
	public String getLoginErrorMessage() {
		try {
			WebElement errorTextElement = waitForElement(by_loginErrorMessage);

			String errorText = errorTextElement.getText().trim();
			logger.info("Login error message displayed: " + errorText);
			return errorText;

		} catch (Exception e) {
			logger.warn(LogColor.RED + "No login error message found" + LogColor.RESET);
			return null;
		}
	}

}






















