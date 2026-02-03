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



	//	******************By Locators****************************************************************************

	By Login_button_by = By.xpath("//span[contains(text(), 'Log In')]");


	//	******************By Locators****************************************************************************

	private static final String feature_buttons = "//span[contains(text(), '%s')]";


	//	****************Actions***********************************************************************************

	public boolean clickOnButton(String buttonName) {

		try {

			logger.info("Looking for the Login Button");
			boolean visible = isElementPresent(Login_button_by);

			if(visible) {
				logger.info("Clicking on the Login Button");
				clickAndDraw(Login_button);
				logger.info("Clicked on the Login Button");
				
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





	public boolean verify_title_of_Page(String expectedTitle) {

		try {
			logger.info("Looking for the Login Button");
			boolean visible = isElementPresent(Login_button_by);

			if(visible) {
				logger.info("Clicking on the Login Button");
				clickAndDraw(Login_button);
				logger.info("Clicked on the Login Button");


				boolean isTitleMatched = verifyPageTitle(expectedTitle);

				return isTitleMatched;
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
