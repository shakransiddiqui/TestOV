package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.*;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import ov.utilities.CommonMethods.*;

public class ProgramCreation_stepD extends CommonMethods{


	public static final Logger logger = LogManager.getLogger(ProgramCreation_stepD.class);



	//	***************************************************************************************************************
	@When("User enters {string} into the {string} field of Program Details")
	public void user_enters_in_the__field(String fieldValue, String fieldName) {

		String expectedFieldValue = ConfigurationReader.getProperty(fieldValue);

		if ("DYNAMIC".equalsIgnoreCase(expectedFieldValue)) {
			expectedFieldValue = TestDataGenerator.generateTestEmail();
		}

		logger.info(expectedFieldValue);

		String ActualFieldValue = programCreation_pom.passFieldValue(expectedFieldValue, fieldName);

		if ("Location".equalsIgnoreCase(fieldName)) {

			softAssert.softAssertTrue(
					ActualFieldValue != null && !ActualFieldValue.equals("null") && ActualFieldValue.contains(expectedFieldValue),
					"Location selected: " + ActualFieldValue,
					"Location NOT selected properly. Actual: " + ActualFieldValue + " | Expected to contain: " + expectedFieldValue
					);

		} else if ("Program Description".equalsIgnoreCase(fieldName)) {

			softAssert.softAssertTrue(
					ActualFieldValue != null && !ActualFieldValue.equals("null") && ActualFieldValue.contains(expectedFieldValue),
					"Program Description entered: " + ActualFieldValue,
					"Program Description NOT entered properly. Actual: " + ActualFieldValue
					);
		} else if ("Program Type".equalsIgnoreCase(fieldName) && "ANY".equalsIgnoreCase(expectedFieldValue)) {

			boolean ok = ActualFieldValue.equals("Accelerator")
					|| ActualFieldValue.equals("Contest")
					|| ActualFieldValue.equals("Incubator")
					|| ActualFieldValue.equals("Other");

			softAssert.softAssertTrue(ok,
					"Program Type selected: " + ActualFieldValue,
					"Program Type NOT selected correctly. Actual: " + ActualFieldValue);
		} else {

			softAssert.softAssertEquals(ActualFieldValue, expectedFieldValue,
					"Actual Field Value: " + ActualFieldValue + " , and Expected Field Value: " + expectedFieldValue);
		}
	}


	//**********************************************************************************
	@Then("User selects {int} industries")
	public void user_selects_industries(int count) {

		int selected = programCreation_pom.selectRandomIndustries(count);

		softAssert.softAssertTrue(selected == count,
				"Selected industries count matched: " + selected,
				"Selected industries count mismatch. Expected: " + count + " | Actual: " + selected);
	}


	//	***************************************************************************************************************
	@Then("User should see {string}")
	public void user_should_see_on_top(String textElement) {

		logger.info("Checking visibility of: " + textElement);

		boolean pageVisible = programCreation_pom.visibilityOfTextOnPage(textElement);

		softAssert.softAssertTrue(pageVisible, 
				"Perks page is visible", 
				"Perks page is NOT visible");
	}

	//	***************************************************************************************************************
	@Then("User selects {int} perks")
	public void user_selects_perks(int count) {

		int expected = Math.min(count, 5); // max allowed
		int actual = programCreation_pom.selectRandomPerks(count);

		softAssert.softAssertTrue(actual == expected,
				"Selected perks count matched: " + actual,
				"Selected perks count mismatch. Expected: " + expected + " | Actual: " + actual);
	}

}
