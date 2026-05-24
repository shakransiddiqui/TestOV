package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.*;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import ov.utilities.GeneratedSignupData;
import ov.utilities.CommonMethods.TestDataGenerator;

public class Signup_stepD extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(Signup_stepD.class);

	private void logStepStart(String stepName, String details) {
		logger.info("[Signup_stepD] START " + stepName + (details == null || details.isBlank() ? "" : " | " + details));
	}

	private void logStepEnd(String stepName, boolean result, String details) {
		logger.info("[Signup_stepD] END " + stepName + " | result=" + result
				+ (details == null || details.isBlank() ? "" : " | " + details));
	}

	private String maskSensitiveValue(String fieldKey, String resolvedValue) {
		if (fieldKey == null || resolvedValue == null) {
			return String.valueOf(resolvedValue);
		}
		if ("newPassword".equalsIgnoreCase(fieldKey) || fieldKey.toLowerCase().contains("password")) {
			return "******";
		}
		return resolvedValue;
	}



	//	***************************************************************************************************************
	@Then("User enters {string} into the {string} field")
	public void user_enters_into_the_field(String fieldValue, String fieldName) {
		logStepStart("user_enters_into_the_field", "fieldKey='" + fieldValue + "' | fieldName='" + fieldName + "'");

		String expectedFieldValue = ConfigurationReader.getProperty(fieldValue);
		logger.info("[Signup_stepD] Resolved config value for key '" + fieldValue + "' => " + expectedFieldValue);

		if ("DYNAMIC_email".equalsIgnoreCase(expectedFieldValue)) {
			expectedFieldValue = TestDataGenerator.generateTestEmail();
			logger.info("[Signup_stepD] Generated dynamic email for '" + fieldValue + "'");
		}

		if ("DYNAMIC_Startup".equalsIgnoreCase(expectedFieldValue)) {
			expectedFieldValue = TestDataGenerator.generateTestStartupCompany();
			logger.info("[Signup_stepD] Generated dynamic startup company for '" + fieldValue + "'");
		}
		
		if ("DYNAMIC_Organization".equalsIgnoreCase(expectedFieldValue)) {
			expectedFieldValue = TestDataGenerator.generateTestProgramCompany();
			logger.info("[Signup_stepD] Generated dynamic organization for '" + fieldValue + "'");
		}

		if ("newEmail".equalsIgnoreCase(fieldValue)) {
			GeneratedSignupData.setEmail(expectedFieldValue);
			logger.info("[Signup_stepD] Saved generated signup email for later reuse.");
		}

		if ("newPassword".equalsIgnoreCase(fieldValue)) {
			GeneratedSignupData.setPassword(expectedFieldValue);
			logger.info("[Signup_stepD] Saved generated signup password for later reuse.");
		}

		if ("fullName".equalsIgnoreCase(fieldValue)) {
			GeneratedSignupData.setFullName(expectedFieldValue);
			logger.info("[Signup_stepD] Saved generated signup full name for later reuse.");
		}

		if ("startupCompanyName".equalsIgnoreCase(fieldValue)) {
			GeneratedSignupData.setStartupCompanyName(expectedFieldValue);
			logger.info("[Signup_stepD] Saved generated startup company name for later reuse.");
		}
		
		logger.info("[Signup_stepD] Entering value into field '" + fieldName + "' => "
				+ maskSensitiveValue(fieldValue, expectedFieldValue));
		String ActualFieldValue = signup_pom.passFieldValue(expectedFieldValue, fieldName);
		logger.info("[Signup_stepD] Field '" + fieldName + "' returned actual value => "
				+ maskSensitiveValue(fieldValue, ActualFieldValue));

		if ("Location".equalsIgnoreCase(fieldName)) {
			boolean matched = ActualFieldValue != null && !ActualFieldValue.equals("null")
					&& ActualFieldValue.contains(expectedFieldValue);
			logStepEnd("user_enters_into_the_field", matched,
					"fieldName='" + fieldName + "' | expectedContains='" + expectedFieldValue + "' | actual='"
							+ ActualFieldValue + "'");
			softAssert.softAssertTrue(
					matched,
					"Location selected: " + ActualFieldValue,
					"Location NOT selected properly. Actual: " + ActualFieldValue + " | Expected to contain: " + expectedFieldValue
					);
		} else {
			boolean matched = expectedFieldValue != null && expectedFieldValue.equals(ActualFieldValue);
			logStepEnd("user_enters_into_the_field", matched,
					"fieldName='" + fieldName + "' | expected='" + maskSensitiveValue(fieldValue, expectedFieldValue)
							+ "' | actual='" + maskSensitiveValue(fieldValue, ActualFieldValue) + "'");
			softAssert.softAssertEquals(ActualFieldValue, expectedFieldValue,
					"Actual Field Value: " + ActualFieldValue + " , and Expected Field Value: " + expectedFieldValue);
		}

	}

	//	***************************************************************************************************************
	@Then("User checks the {string} checkbox")
	public void user_checks_the_checkbox(String checkboxName) {
		logStepStart("user_checks_the_checkbox", "checkboxName='" + checkboxName + "'");

		logger.info("[Signup_stepD] Checking checkbox => " + checkboxName);

		boolean checkedTheBox = signup_pom.clickTermsOfService(checkboxName);
		logStepEnd("user_checks_the_checkbox", checkedTheBox, "checkboxName='" + checkboxName + "'");

		softAssert.softAssertTrue(checkedTheBox, 
				"Checked the box of: "+checkboxName, 
				"Failed to check the box of: "+checkboxName);
	}

	//	***************************************************************************************************************
	@Then("User clicks on {string} button")
	public void user_clicks_on_button(String buttonName) {
		logStepStart("user_clicks_on_button", "buttonName='" + buttonName + "'");

		logger.info("[Signup_stepD] Clicking button => " + buttonName);

		boolean ButtonIsClickable = signup_pom.clickOnButton(buttonName);
		logStepEnd("user_clicks_on_button", ButtonIsClickable, "buttonName='" + buttonName + "'");

		waitForNetworkIdle();
		
		softAssert.softAssertTrue(ButtonIsClickable, 
				buttonName+" Button is visible and clickable", 
				buttonName+" Button is not clickable");
	}

	//	***************************************************************************************************************
	@Then("User should see {string} on top")
	public void user_should_see_on_top(String textElement) {
		logStepStart("user_should_see_on_top", "textElement='" + textElement + "'");

		logger.info("[Signup_stepD] Checking top-of-page visibility => " + textElement);

		boolean pageVisible = signup_pom.visibilityOfRolePage(textElement);
		logStepEnd("user_should_see_on_top", pageVisible, "textElement='" + textElement + "'");

		softAssert.softAssertTrue(pageVisible, 
				"Role Selection is visible", 
				"Role Selection is NOT visible");
	}

	//	***************************************************************************************************************
	@Then("User chooses {string}")
	public void user_chooses(String roleName) {
		logStepStart("user_chooses", "roleName='" + roleName + "'");

		logger.info("[Signup_stepD] Selecting role => " + roleName);

		boolean roleSelected = signup_pom.chooseSignUpRole(roleName);
		logStepEnd("user_chooses", roleSelected, "roleName='" + roleName + "'");

		softAssert.softAssertTrue(roleSelected, 
				"Successfully selected role as : "+roleName, 
				"Failed to select the role: "+roleName);
	}


	//	***************************************************************************************************************
	@Then("User should see a signup error or validation message")
	public void user_should_see_a_signup_error_or_validation_message() {
		logStepStart("user_should_see_a_signup_error_or_validation_message", null);

		logger.info("[Signup_stepD] Validating signup error/validation message.");

		boolean visible = signup_pom.signupErrorVisible();
		logStepEnd("user_should_see_a_signup_error_or_validation_message", visible, null);

		softAssert.softAssertTrue(visible,
				"Signup error/validation message is visible.",
				"Expected a signup error/validation message, but none appeared.");

	}

	//	***************************************************************************************************************
	@Then("User should remain on the Signup page with title of {string}")
	public void user_should_remain_on_the_signup_page_with_title_of(String pageTitle) {
		logStepStart("user_should_remain_on_the_signup_page_with_title_of", "pageTitleKey='" + pageTitle + "'");

		logger.info("[Signup_stepD] Resolving expected page title for key => " + pageTitle);
		String expectedPageTitle = ConfigurationReader.getProperty(pageTitle);
		logger.info("[Signup_stepD] Expected title => " + expectedPageTitle);


		boolean TitleMatched = homepage_pom.verify_title_of_Page(expectedPageTitle);
		logStepEnd("user_should_remain_on_the_signup_page_with_title_of", TitleMatched,
				"pageTitleKey='" + pageTitle + "' | expectedTitle='" + expectedPageTitle + "'");

		softAssert.softAssertTrue(TitleMatched, 
				"Remained on Page and "+pageTitle+" Matched successfully", 
				pageTitle+" Did Not Match");

	}


	//	***************************************************************************************************************
	@Then("User selects {string}")
	public void user_selects(String sso_buttonName) {
		logStepStart("user_selects", "ssoButtonName='" + sso_buttonName + "'");

		logger.info("[Signup_stepD] Clicking SSO button => " + sso_buttonName);

		boolean ButtonIsClickable = signup_pom.clickOnSsoButton(sso_buttonName);
		logStepEnd("user_selects", ButtonIsClickable, "ssoButtonName='" + sso_buttonName + "'");

		softAssert.softAssertTrue(ButtonIsClickable, 
				sso_buttonName+" Button is visible and clickable", 
				sso_buttonName+" Button is not clickable");
	}


//	***************************************************************************************************************
	@Then("User should see {string} on Google page")
	public void user_should_see_on_google_page(String textElement) {
		logStepStart("user_should_see_on_google_page", "textElement='" + textElement + "'");
	   
		logger.info("[Signup_stepD] Checking Google page visibility => " + textElement);

		boolean pageVisible = signup_pom.visibilityOfGoogleSignInPage(textElement);
		logStepEnd("user_should_see_on_google_page", pageVisible, "textElement='" + textElement + "'");

		softAssert.softAssertTrue(pageVisible, 
				"Google Sign in Page is visible", 
				"Google Sign in Page is NOT visible");
	}
	

}