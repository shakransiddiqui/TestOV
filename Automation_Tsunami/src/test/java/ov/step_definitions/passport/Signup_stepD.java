package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.*;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import ov.utilities.CommonMethods.TestDataGenerator;

public class Signup_stepD extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(Signup_stepD.class);



	//	***************************************************************************************************************
	@Then("User enters {string} into the {string} field")
	public void user_enters_into_the_field(String fieldValue, String fieldName) {

		String expectedFieldValue = ConfigurationReader.getProperty(fieldValue);

		if ("DYNAMIC".equalsIgnoreCase(expectedFieldValue)) {
			expectedFieldValue = TestDataGenerator.generateTestEmail();
		}

		logger.info(expectedFieldValue);
		String ActualFieldValue = signup_pom.passFieldValue(expectedFieldValue, fieldName);

		if ("Location".equalsIgnoreCase(fieldName)) {
			softAssert.softAssertTrue(
					ActualFieldValue != null && !ActualFieldValue.equals("null") && ActualFieldValue.contains(expectedFieldValue),
					"Location selected: " + ActualFieldValue,
					"Location NOT selected properly. Actual: " + ActualFieldValue + " | Expected to contain: " + expectedFieldValue
					);
		} else {
			softAssert.softAssertEquals(ActualFieldValue, expectedFieldValue,
					"Actual Field Value: " + ActualFieldValue + " , and Expected Field Value: " + expectedFieldValue);
		}

	}

	//	***************************************************************************************************************
	@Then("User checks the {string} checkbox")
	public void user_checks_the_checkbox(String checkboxName) {

		logger.info("Checking the checkbox of: "+checkboxName);

		boolean checkedTheBox = signup_pom.clickTermsOfService(checkboxName);

		softAssert.softAssertTrue(checkedTheBox, 
				"Checked the box of: "+checkboxName, 
				"Failed to check the box of: "+checkboxName);
	}

	//	***************************************************************************************************************
	@Then("User clicks on {string} button")
	public void user_clicks_on_button(String buttonName) {

		logger.info("Click on Button : "+buttonName);

		boolean ButtonIsClickable = signup_pom.clickOnButton(buttonName);

		softAssert.softAssertTrue(ButtonIsClickable, 
				buttonName+" Button is visible and clickable", 
				buttonName+" Button is not clickable");
	}

	//	***************************************************************************************************************
	@Then("User should see {string} on top")
	public void user_should_see_on_top(String textElement) {

		logger.info("Checking visibility of: " + textElement);

		boolean pageVisible = signup_pom.visibilityOfRolePage(textElement);

		softAssert.softAssertTrue(pageVisible, 
				"Role page is visible", 
				"Role page is NOT visible");
	}

	//	***************************************************************************************************************
	@Then("User chooses {string}")
	public void user_chooses(String roleName) {

		logger.info("Selecting role: " + roleName);

		boolean roleSelected = signup_pom.chooseSignUpRole(roleName);

		softAssert.softAssertTrue(roleSelected, 
				"Successfully selected role as : "+roleName, 
				"Failed to select the role: "+roleName);
	}


	//	***************************************************************************************************************
	@Then("User should see a signup error or validation message")
	public void user_should_see_a_signup_error_or_validation_message() {

		logger.info("Validating signup error/validation message.");

		boolean visible = signup_pom.signupErrorVisible();

		softAssert.softAssertTrue(visible,
				"Signup error/validation message is visible.",
				"Expected a signup error/validation message, but none appeared.");

	}

	//	***************************************************************************************************************
	@Then("User should remain on the Signup page with title of {string}")
	public void user_should_remain_on_the_signup_page_with_title_of(String pageTitle) {

		logger.info("Getting the expected page title of : "+pageTitle);
		String expectedPageTitle = ConfigurationReader.getProperty(pageTitle);
		logger.info("Expected Title is: "+expectedPageTitle);


		boolean TitleMatched = homepage_pom.verify_title_of_Page(expectedPageTitle);

		softAssert.softAssertTrue(TitleMatched, 
				"Remained on Page and "+pageTitle+" Matched successfully", 
				pageTitle+" Did Not Match");

	}


	//	***************************************************************************************************************




}
