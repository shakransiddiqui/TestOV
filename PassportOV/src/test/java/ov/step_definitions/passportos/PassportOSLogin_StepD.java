package ov.step_definitions.passportos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;

import io.cucumber.java.en.*;


public class PassportOSLogin_StepD extends CommonMethods {

	public static final Logger logger =
			LogManager.getLogger(PassportOSLogin_StepD.class);



	//*****************************************************************************************************************
	@Given("User is on the PassportOS login page with title of {string}")
	public void user_is_on_the_passport_os_login_page_with_title_of(String pageTitle_fromFeature) {

		boolean pageTitleMatched = passportOSLoginPage_pom.validate_passportOS_login_page_title(pageTitle_fromFeature);

		//		softAssert.softAssertTrue(pageTitleMatched, "Title of Login Page Matched successfully", 
		//				"Title of Login Page Did Not Match");

	}

	//*****************************************************************************************************************
	/*
	 * Feature that call this Method: 1, 2
	 */
	@Then("User enters {string} in the {string} field")
	public void user_enters_valid_passport_os_email(String fieldValue_fromfeature, String fieldName_fromFeature) {

		String expectedFieldValue = ConfigurationReader.getProperty(fieldValue_fromfeature);
		
		if ("DYNAMIC".equalsIgnoreCase(expectedFieldValue)) {
		    expectedFieldValue = TestDataGenerator.generateInvalidEmail();
		}
		
		logger.info(expectedFieldValue);
		String ActualFieldValue = passportOSLoginPage_pom.EnterValueToLoginField(expectedFieldValue, fieldName_fromFeature);


		softAssert.softAssertEquals(ActualFieldValue, expectedFieldValue, 
				"Actual Field Value: "+ActualFieldValue+ " , and Expected Field Value: "+expectedFieldValue);

	}

	//*****************************************************************************************************************
	@Then("User clicks on the {string} button")
	public void user_clicks_on_the_continue_button(String buttonName) {

		boolean landedOnDashboardPage =passportOSLoginPage_pom.clickOnContinueButton(buttonName);

		softAssert.softAssertTrue(landedOnDashboardPage, "Clicked on Continue Button and Login Successful","Clicked on Continue Button but Login Failed");
	}

	//	**********************************************************************************************************************************************************
	@Then("User is redirected to Dashboard page with title {string}")
	public void user_is_redirected_to_dashboard_page_with_title(String pageTitle) {

		boolean landedOnDashboardPage =passportOSLoginPage_pom.isCommunityDashboardDisplayed();

		softAssert.softAssertTrue(landedOnDashboardPage, "User is redirected to Dashboard page","User is NOT redirected to Dashboard page");
	}

	//	**********************************************************************************************************************************************************
	@Then("User should see login error message {string}")
	public void user_should_see_login_error_message(String expectedErrorMessage) {

		String actualErrorMessage = passportOSLoginPage_pom.getLoginErrorMessage();

		softAssert.softAssertTrue(
				actualErrorMessage != null && actualErrorMessage.contains(expectedErrorMessage),
				"Correct login error message is displayed: " + actualErrorMessage,
				"Expected error message not displayed. Expected: "
						+ expectedErrorMessage + ", Actual: " + actualErrorMessage
				);
	}



}





