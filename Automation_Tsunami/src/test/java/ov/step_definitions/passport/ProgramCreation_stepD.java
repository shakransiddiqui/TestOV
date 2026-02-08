package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.*;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import ov.utilities.CommonMethods.TestDataGenerator;

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
		} else {
			softAssert.softAssertEquals(ActualFieldValue, expectedFieldValue,
					"Actual Field Value: " + ActualFieldValue + " , and Expected Field Value: " + expectedFieldValue);
		}
	}


	//**********************************************************************************

}
