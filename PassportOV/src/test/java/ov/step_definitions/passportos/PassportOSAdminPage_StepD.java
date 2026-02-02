package ov.step_definitions.passportos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import ov.utilities.CommonMethods.TestDataGenerator;
import io.cucumber.java.en.*;

public class PassportOSAdminPage_StepD extends CommonMethods {

	public static final Logger logger =
			LogManager.getLogger(PassportOSAdminPage_StepD.class);

	
//	***************************************************************************************************************

	@Then("User clicks on {string} button")
	public void user_clicks_on_button(String buttonName) {
		
		passportOSAdmin_pom.clickOnButton(buttonName);

	}
	
//	***************************************************************************************************************

	@Then("User is redirected to Admin Page with {string} text on top")
	public void user_is_redirected_to_admin_page_with_text_on_top(String pageTextElement) {
	   
		passportOSAdmin_pom.verify_AdminPage_UiTextElement_is_visible(pageTextElement);
	}

//	***************************************************************************************************************

	@Then("User should see {string} Page Header")
	public void user_should_see_page(String pageHeaderText) {
		
		passportOSAdmin_pom.verify_AdminPage_UiTextElement_is_visible(pageHeaderText);

	}

//	***************************************************************************************************************

	@Then("User passes {string} to the {string} field and presses on Enter")
	public void user_passes_value_and_enters(String valueKey, String fieldName) {

	    String expectedValue = ConfigurationReader.getProperty(valueKey);

	    if ("DYNAMIC".equalsIgnoreCase(expectedValue)) {
	        expectedValue = TestDataGenerator.generateInvalidEmail();
	    }

	    logger.info("Using value: " + expectedValue);

	    String actualValue =
	        passportOSAdmin_pom.passFieldValueAndEnter(expectedValue, fieldName);

	    softAssert.softAssertEquals(actualValue, expectedValue,
	        "Actual Field Value: " + actualValue + ", Expected Field Value: " + expectedValue );
	}

	
	
	
	
}
