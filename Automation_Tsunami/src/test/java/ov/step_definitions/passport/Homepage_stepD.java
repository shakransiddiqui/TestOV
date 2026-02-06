package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.*;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;

public class Homepage_stepD extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(Homepage_stepD.class);

	
//	***************************************************************************************************************
	@Given("User clicks on {string} button")
	public void user_clicks_on_button(String buttonName) {
		
		logger.info("Click on Button : "+buttonName);
		boolean ButtonIsClickable = homepage_pom.clickOnButton(buttonName);
	    
		softAssert.softAssertTrue(ButtonIsClickable, 
				"Button is visible and clickable", 
				"Button is not clickable");
	}	
	
	//	***************************************************************************************************************
	@Then("Navigates to the page with title of {string}")
	public void navigate_to_the_page(String pageTitle) {

		logger.info("Getting the expected page title of : "+pageTitle);
		String expectedPageTitle = ConfigurationReader.getProperty(pageTitle);
		logger.info("Expected Title is: "+expectedPageTitle);

		logger.info("Navigating to the login page");
		boolean TitleMatched = homepage_pom.verify_title_of_Page(expectedPageTitle);

		softAssert.softAssertTrue(TitleMatched, 
				"Navigated to Page and "+pageTitle+" Matched successfully", 
				pageTitle+" Did Not Match");
	}
	
//	***************************************************************************************************************
	@Then("User clicks back twice and navigates to Startup page with title of {string}")
	public void user_navigates_back_to_startup_page(String pageTitle) {
	    
		logger.info("Getting the expected page title of : "+pageTitle);
		String expectedPageTitle = ConfigurationReader.getProperty(pageTitle);
		logger.info("Expected Title is: "+expectedPageTitle);
		
		logger.info("Navigating back to Homepage");
		boolean TitleMatched = homepage_pom.clickOnBackTwice(expectedPageTitle);

		softAssert.softAssertTrue(TitleMatched, 
				"Navigated to Page and "+pageTitle+" Matched successfully", 
				pageTitle+" Did Not Match");
	}
	
	
	
}
