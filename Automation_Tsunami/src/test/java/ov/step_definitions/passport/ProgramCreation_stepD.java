package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import io.cucumber.java.en.*;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;

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

		// ✅ If expected is EMPTY, just verify it is empty/unselected and stop here
		if (expectedFieldValue == null) expectedFieldValue = "";

		if (expectedFieldValue.isBlank()) {

			boolean ok =
					ActualFieldValue == null
					|| ActualFieldValue.isBlank()
					|| "null".equalsIgnoreCase(ActualFieldValue)
					|| ActualFieldValue.toLowerCase().contains("select"); // helps for Program Type placeholder

			softAssert.softAssertTrue(
					ok,
					"Field left EMPTY as expected: " + fieldName + " | Actual: " + ActualFieldValue,
					"Field was expected EMPTY but has value: " + ActualFieldValue
					);

			return; // IMPORTANT: stop here so it doesn't go into Location/Description/equals checks
		}
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

	//**********************************************************************************
	@Then("User closes the industries dropdown")
	public void user_closes_the_industries_dropdown() {

		logger.info("Closing Industries dropdown...");

		boolean closed = programCreation_pom.closeIndustriesDropdown();

		softAssert.softAssertTrue(
				closed,
				"Industries dropdown is closed.",
				"Industries dropdown is STILL open (it may be blocking Save & Continue)."
				);
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

	//	***************************************************************************************************************
	@Then("{string} should be highlighted as missing on Program Details")
	public void should_be_highlighted_as_missing_on_program_details(String missingField) {

		logger.info("Checking if missing field is highlighted (missingFields class): " + missingField);

		By missingLabel = By.xpath(
				"//label[contains(@class,'missingFields') and contains(normalize-space(.),'" + missingField + "')]"
				);

		boolean markedMissing = isElementPresent(missingLabel);

		softAssert.softAssertTrue(
				markedMissing,
				"Field is highlighted as missing: " + missingField,
				"Field was NOT highlighted as missing: " + missingField
				);
	}


	//	***************************************************************************************************************
	@Then("User should remain on the Create Program page with title of {string}")
	public void user_should_remain_on_the_login_page(String pageTitle) {

		logger.info("Getting the expected page title of : "+pageTitle);
		String expectedPageTitle = ConfigurationReader.getProperty(pageTitle);
		logger.info("Expected Title is: "+expectedPageTitle);


		boolean TitleMatched = homepage_pom.verify_title_of_Page(expectedPageTitle);

		softAssert.softAssertTrue(TitleMatched, 
				"Remained on Page and "+pageTitle+" Matched successfully", 
				pageTitle+" Did Not Match");
	}


}
