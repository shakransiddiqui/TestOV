package eal.step_definitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eal.Utilities.CommonMethods;
import eal.Utilities.ConfigurationReader;
import io.cucumber.java.en.*;

public class DashBoard_StepD extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(DashBoard_StepD.class);

	//***********************************************************************************************************
	@Given("click on {string} button and open {string} page")
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
	
	@Then("click on {string} button")
	public void click_on_button(String buttonName_fromFeature) {
	
		boolean visibilityAndClickabilityStatus = false;

		logger.info("Verifying Visibility and clickability of button: "+buttonName_fromFeature);

		boolean Ui_LinkButton_Element = dashboard_pom.verify_LinkButtons_inDashBoard(buttonName_fromFeature);
	   
		visibilityAndClickabilityStatus = Ui_LinkButton_Element;
		
		logger.info("Performing Assertion on Visibility and clickability of button: "+buttonName_fromFeature);
		softAssert.softAssertTrue(visibilityAndClickabilityStatus, buttonName_fromFeature+" Button is Visible and Clickable", 
				buttonName_fromFeature+" Button is not Visible and Clickable");
	   
	}
	
}
