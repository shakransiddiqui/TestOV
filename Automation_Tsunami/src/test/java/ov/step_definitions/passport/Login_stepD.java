package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.Assert;
import ov.pages.passport.HomePage_Passport;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import ov.utilities.CommonMethods.TestDataGenerator;
import io.cucumber.java.en.*;


public class Login_stepD extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(Login_stepD.class);

	//	***************************************************************************************************************
	@When("User enters {string} in the {string} field")
	public void user_enters_in_the__field(String fieldValue, String fieldName) {

		String expectedFieldValue = ConfigurationReader.getProperty(fieldValue);

		if ("DYNAMIC".equalsIgnoreCase(expectedFieldValue)) {
			expectedFieldValue = TestDataGenerator.generateInvalidEmail();
		}

		logger.info(expectedFieldValue);
		String ActualFieldValue = login_pom.passFieldValue(expectedFieldValue, fieldName);


		softAssert.softAssertEquals(ActualFieldValue, expectedFieldValue, 
				"Actual Field Value: "+ActualFieldValue+ " , and Expected Field Value: "+expectedFieldValue);
	}

	//	***************************************************************************************************************
	@When("User clicks on the {string} button")
	public void user_clicks_on_the_button(String buttonName) {

		logger.info("Click on Button : "+buttonName);
		boolean ButtonIsClickable = login_pom.clickOnLogin(buttonName);
	    
		softAssert.softAssertTrue(ButtonIsClickable, 
				buttonName+" Button is visible and clickable", 
				buttonName+" Button is not clickable");
	}

	//	***************************************************************************************************************
	@Then("User should see a login error or validation message")
	public void user_should_see_a_login_error_or_validation_message() {
		
		logger.info("Validating error/validation message.");
		   boolean visible = login_pom.loginErrorVisible();
		    softAssert.softAssertTrue(visible, "Expected login error/validation message is visible.",
		            "Expected a login error/validation message, but none appeared.");

		    if (visible) {
		        logger.info("Errors shown: " + getElementsTextbyLocator(
		                By.cssSelector(".ulp-input-error-message, .ulp-validator-error")
		        ));
		    }
	}

	//	***************************************************************************************************************
	@Then("User should remain on the Login page with title of {string}")
	public void user_should_remain_on_the_login_page(String pageTitle) {

		logger.info("Getting the expected page title of : "+pageTitle);
		String expectedPageTitle = ConfigurationReader.getProperty(pageTitle);
		logger.info("Expected Title is: "+expectedPageTitle);

		
		boolean TitleMatched = homepage_pom.verify_title_of_Page(expectedPageTitle);

		softAssert.softAssertTrue(TitleMatched, 
				"Remained on Page and "+pageTitle+" Matched successfully", 
				pageTitle+" Did Not Match");
	}
	
//	***************************************************************************************************************
	@Then("User clicks back thrice and navigates to Startup page with title of {string}")
	public void user_navigates_back_to_startUp_page(String pageTitle) {
	    
		logger.info("Getting the expected page title of : "+pageTitle);
		String expectedPageTitle = ConfigurationReader.getProperty(pageTitle);
		logger.info("Expected Title is: "+expectedPageTitle);
		
		boolean TitleMatched = login_pom.clickOnBackThrice(expectedPageTitle);

		softAssert.softAssertTrue(TitleMatched, 
				"Navigated to Page and "+pageTitle+" Matched successfully", 
				pageTitle+" Did Not Match");
	}

	//	***************************************************************************************************************
	@Then("User should be redirected to the page with title of {string}")
	public void user_should_be_redirected_to_the_page_with_title_of(String pageTitle) {
		
		logger.info("Getting the expected page title of : "+pageTitle);
		String expectedPageTitle = ConfigurationReader.getProperty(pageTitle);
		logger.info("Expected Title is: "+expectedPageTitle);

		logger.info("Navigating to the login page");
		boolean TitleMatched = homepage_pom.verify_title_of_Page(expectedPageTitle);

		softAssert.softAssertTrue(TitleMatched, 
				"Navigated to Page and "+pageTitle+" Matched successfully", 
				pageTitle+" Did Not Match");

	}
	
	//*****************************************************************************************************************
	@And("User logs out")
	public void user_logs_out() {
		
		logger.info("Logging out...");
		boolean loggedOut = header_pom.logout();
	    
		softAssert.softAssertTrue(loggedOut, 
				"Log Out Successful", 
				"Log Out Not Successful");
		
	}
	
	
	
}