package ov.pages.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import ov.utilities.CommonMethods;
import ov.utilities.LogColor;

public class Homepage_POM extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(Homepage_POM.class);

	public Homepage_POM() {
		PageFactory.initElements(driver, this);
	}

	//	*****************X-path Locators****************************************************************

	@FindBy (xpath="//span[contains(text(), 'Log In')]")
	public WebElement Login_button;



	//	******************By Locators***************************************************************************
	


	//	******************Locator Format Template Strings****************************************************************************

	private static final String feature_buttons = "//span[contains(text(), '%s')]";


	//	****************Actions***********************************************************************************

	public boolean clickOnButton(String buttonName) {

		try {
			logger.info("Formatting the X path of Button: "+buttonName);
			String formattedXpath = String.format(feature_buttons, buttonName);
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
	
//	***************************************************************************************************************
public boolean clickOnBackTwice(String expectedTitle) {
try {
	logger.info("Waiting for page to load fully");
	waitForNetworkIdle();
	
	logger.info("Clicking on the Back Button");
	driver.navigate().back();
	
	logger.info("Waiting for page to load fully");
	waitForNetworkIdle();
	
	logger.info("Clicking on the Back Button");
	driver.navigate().back();
	
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
