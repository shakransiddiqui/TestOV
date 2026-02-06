package ov.pages.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import ov.utilities.CommonMethods;
import ov.utilities.LogColor;

public class Header_POM extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(Header_POM.class);

	public Header_POM() {
		PageFactory.initElements(driver, this);
	}

	//	*****************X-path Locators****************************************************************




	//	******************By Locators****************************************************************************

	private static final By PROFILE_ICON_by =  By.xpath("//nav//a[@aria-label='Profile' and @title='Profile']");
	private static final By LOGOUT_BTN_by = By.xpath("//nav//a[@href='/logout' and (@title='Log Out' or contains(@aria-label,'Log Out'))]");



	//	******************Locator Format Template Strings****************************************************************************




	//	****************Actions***********************************************************************************

	public boolean isLoggedIn() {
		try {
			// profile icon exists only when logged in
			boolean profile_icon_visibility =  isElementPresent(PROFILE_ICON_by);

			return profile_icon_visibility;
			
		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false;
		}

	}

	public boolean logout() {

		try {
			// If not logged in, do nothing (prevents failures in negative tests)
			if (!isLoggedIn()) {
				logger.info("User not logged in (profile icon not found). Skipping logout.");
				return false;
			}

			boolean visible = isElementPresent(PROFILE_ICON_by);

			if(visible) {
			
				logger.info("Clicking on the Profile Icon");
				WebElement profile = waitForElement(PROFILE_ICON_by);
				clickAndDraw(profile);
				logger.info("Clicked on the Profile Icon");

				logger.info("Clicking on the Log Out Button");
				WebElement logout = waitForElement(LOGOUT_BTN_by);
				clickAndDraw(logout);
				logger.info("Clicked on the Log Out Button");
				
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

