package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.Given;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;

public class Homepage_stepD extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(Homepage_stepD.class);

	
	//	***************************************************************************************************************
	@Given("Navigates to the page with title of {string}")
	public void navigate_to_the_login_page(String pageTitle) {

		logger.info("Getting the expected page title of : "+pageTitle);
		String expectedPageTitle = ConfigurationReader.getProperty(pageTitle);
		logger.info("Expected Title is: "+expectedPageTitle);

		logger.info("Navigating to the login page");
		boolean TitleMatched = homepage_pom.verify_title_of_Page(expectedPageTitle);

		softAssert.softAssertTrue(TitleMatched, 
				"Navigated to Login Page and Title Matched successfully", 
				"Title of Login Page Did Not Match");
	}
}
