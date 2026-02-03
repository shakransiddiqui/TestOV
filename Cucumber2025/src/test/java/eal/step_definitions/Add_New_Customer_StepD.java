package eal.step_definitions;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eal.Utilities.CommonMethods;
import eal.Utilities.ConfigurationReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

public class Add_New_Customer_StepD extends CommonMethods  {

	public static final Logger logger = LogManager.getLogger(Add_New_Customer_StepD.class);

	//***********************************************************************************************************
	@Given("click on {string} button and go to {string} page")
	public void click_on_button(String buttonName_fromFeature, String expectedPage_fromFeature) {

		boolean visibilityAndClickabilityStatus = false;

		logger.info("Verifying Visibility and clickability of button: "+buttonName_fromFeature);

		boolean Ui_LinkButton_Element = dashboard_pom.verify_LinkButtons_inDashBoard(buttonName_fromFeature);

		visibilityAndClickabilityStatus = Ui_LinkButton_Element;

		logger.info("Performing Assertion on Visibility and clickability of button: "+buttonName_fromFeature);
		softAssert.softAssertTrue(visibilityAndClickabilityStatus, buttonName_fromFeature+" Button is Visible and Clickable", 
				buttonName_fromFeature+" Button is not Visible and Clickable");

		String expectedPageTitle = ConfigurationReader.getProperty(expectedPage_fromFeature);

		boolean titleIsCorrect = verifyPageTitle(expectedPageTitle);

		logger.info("Performing Assertion on Page Title after clicking button: "+buttonName_fromFeature);
		softAssert.softAssertTrue(titleIsCorrect, "Title Matched Successfully", 
				"Title didn't Match");
	}

	//	**********************************************************************************************************************************************************
	@When("I fill the form with the following fields from {string} and sheet {string}:")
	public void i_fill_the_form_with_the_following_fields_from_and_sheet(String fileName, String sheetName, DataTable dataTable) {

		logger.info("Opening the Excel File");

		//1. Initialize the Excel Context (open file loads sheets, maps headers)
		excelUtil.openExcel(fileName, sheetName);

		//2. Get the list of fields (Row Keys) from the Cucumber DataTable
		List<String> fields = dataTable.asList();

		for(String fieldKey : fields) {

			String field = fieldKey.trim();

			String value = excelUtil.getCellData(field, "Values").trim();

			if(value.isEmpty()) {
				logger.warn("X Data not found in Excel for the fieldKey: "+field);

				//3. softAssert is inherited from CommonMethods
				softAssert.softAssertFail("X Data not found in Excel for the fieldKey: "+field);

				continue;
			}

			boolean result = false;

			//4. Perform UI Action based on the field Type:
			switch (field) {
			case "Customer Name":
			case "Address":
			case "City":
			case "State":
			case "PIN":
			case "Mobile Number":
			case "E-mail":
				//addNewCustomer_pom is inherited from CommonMethods
				result = addNewCustomer_pom.enterTypableFieldValue(field, value);
				break;
				
			case "Gender":
				result = addNewCustomer_pom.selectGender(field, value);
				break;

			case "Date of Birth":
				result = addNewCustomer_pom.enterDateFieldValue(field, value);
				break;
				
			default:
				logger.warn("Field is not Mapped in step definition switch cases: "+field);
				break;
			}
			softAssert.softAssertTrue(result, field +": is filled with value: "+value, field +": failed to fill UI Element");

		}

	}

	//	**********************************************************************************************************************************************************
	@Then("click on {string} button.")
	public void click_on_submit_button(String buttonName_fromFeature) {
		
		boolean visibilityAndClickabilityStatus = false;

		logger.info("Verifying Visibility and clickability of button: "+buttonName_fromFeature);

		boolean Submit = addNewCustomer_pom.clickOnSubmitButton(buttonName_fromFeature);
	   
		visibilityAndClickabilityStatus = Submit;
		
		logger.info("Performing Assertion on Visibility and clickability of button: "+buttonName_fromFeature);
		softAssert.softAssertTrue(visibilityAndClickabilityStatus, buttonName_fromFeature+" Button is Visible and Clickable", 
				buttonName_fromFeature+" Button is not Visible and Clickable");

	}

}
