package ov.pages.passport;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import groovyjarjarantlr4.v4.runtime.tree.xpath.XPath;
import ov.utilities.CommonMethods;
import ov.utilities.Driver;
import ov.utilities.LogColor;

public class Login_POM extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(Login_POM.class);

	public Login_POM() {
		PageFactory.initElements(driver, this);
	}


	//	*****************X-path Locators****************************************************************




	//	******************By Locators****************************************************************************


	
	
	//	******************Locator Format Template Strings****************************************************************************

	private static final String login_fields ="//label[contains(text(), '%s')]/following-sibling::input";
	private static final String buttons ="//button[contains(text(), '%s')]";

	

	//	****************Actions***********************************************************************************
	public String passFieldValue(String fieldElement_Value,String fieldElement_Name) {

		try {
			logger.info("Formatting the X path of Field: "+fieldElement_Name);
			String formattedXpath = String.format(login_fields, fieldElement_Name);
			logger.info(formattedXpath);


			logger.info("Finding the Field: "+fieldElement_Name);
			WebElement login_field = driver.findElement(By.xpath(formattedXpath));

			logger.info("Clicking on the Field: "+fieldElement_Name);
			clickAndDraw(login_field);

			logger.info("Passing value on the Field: "+fieldElement_Value);
			safeSendKeys(login_field, fieldElement_Value);
			
			logger.info("Making sure value is filled up on the Field: "+fieldElement_Name);
			String actualFieldValue = login_field.getAttribute("value");


			logger.info(LogColor.DarkGreen +"Actual value in the filed :"+actualFieldValue+ LogColor.RESET);
			return actualFieldValue;

		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);

			return "null";
		}
	}
	
//	***************************************************************************************************************
	public boolean clickOnLogin(String buttonName) {

		try {
			logger.info("Formatting the X path of Button: "+buttonName);
			String formattedXpath = String.format(buttons, buttonName);
			logger.info("X path of Button: "+formattedXpath);
			
			waitForNetworkIdle();
			logger.info("Looking for the Button: "+buttonName);
			boolean visible = isElementPresent(By.xpath(formattedXpath));

			if(visible) {
				logger.info("Finding the Button: "+buttonName);
				WebElement button = driver.findElement(By.xpath(formattedXpath));
		
				logger.info("Clicking on the Button: "+buttonName);
				clickAndDraw(button);
				logger.info("Clicked on the Button: "+buttonName);

				return true;
			}
			else {
				return false;
			}		
		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false;
		}
	}
	
//	***************************************************************************************************************
	public boolean loginErrorVisible() {
		try {
			boolean isErrorVisible =  isElementPresent(login_ERROR_by);
			
			return isErrorVisible;
		        
		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false;
		}
	}

	//	***************************************************************************************************************
	public boolean verify_title_of_Page(String expectedTitle) {

		try {
			logger.info("Verifying the Page Title");
			boolean isTitleMatched = verifyPageTitle(expectedTitle);

			if(isTitleMatched) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false;
		}
	}

}
