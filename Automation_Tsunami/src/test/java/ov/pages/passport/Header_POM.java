package ov.pages.passport;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ov.utilities.CommonMethods;
import ov.utilities.LogColor;

public class Header_POM extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(Header_POM.class);

	public Header_POM() {
		PageFactory.initElements(driver, this);
	}

	//	*****************X-path Locators****************************************************************


	@FindBy (xpath = "//nav//a[@href='/logout' and (@title='Log Out' or contains(@aria-label,'Log Out'))]")
	private  WebElement LOGOUT_BTN;

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
			// If not logged in, skip (or return true if you want "already logged out" to be success)
			if (!isLoggedIn()) {
				logger.info("User not logged in (profile icon not found). Skipping logout.");
				return true;
			}

			logger.info("Clicking on the Profile Icon");
			WebElement profile = waitForElement(PROFILE_ICON_by);
			jsclick(driver, profile);
			logger.info("Clicked on the Profile Icon");

			logger.info("Clicking on the Log Out Button");
			jsclick(driver, LOGOUT_BTN);
			logger.info("Clicked on the Log Out Button");

			//  PROOF OF LOGOUT (pick the most reliable one for your app)
			logger.info("Waiting for logout to complete (profile icon disappears OR login link appears OR URL changes)");
			WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(20));

			boolean loggedOut = localWait.until(driver -> {
				String url = driver.getCurrentUrl();
				boolean profileGone = !isElementPresent(PROFILE_ICON_by);
				boolean urlChanged = url.contains("/searchprogram") || url.contains("/login") || url.contains("/logout");
				return profileGone || urlChanged;
			});

			logger.info("Logout completed = " + loggedOut + " | Current URL: " + driver.getCurrentUrl());
			return loggedOut;


		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in logout()"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false;
		}
	}


}
