package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.SoftAssert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertTrue;

import java.util.*;

import io.cucumber.java.en.*;
import junit.framework.Assert;
import ov.pages.passport.*;
import ov.utilities.CommonMethods;
import ov.utilities.Driver;


public class ApplicationCreationSteps extends CommonMethods  {
	public static final Logger logger = LogManager.getLogger(ApplicationCreationSteps.class);
    ProgramManagerOrgInfoPage programManagerOrgInfoPage=new ProgramManagerOrgInfoPage();
    CreateAppliction createApplication =new CreateAppliction();
    PublishApplicationPage publishApplicationPage=new PublishApplicationPage();
    EditApplicationPage editApplication=new EditApplicationPage();

	public String generateUniqueEmail() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String timestamp = LocalDateTime.now().format(formatter);
		return "QA_TESTER+tests_" + timestamp + "@theonevalley.com";
	}


	@Given("User is on Programs page and user have atleast one program on page")
	public void  User_is_on_Programs_page_and_user_have_atleast_one_program_on_page()
	{
		logger.info("User is on Programs page and user have atleast one program on page");

		boolean userHasOneProgram= programManagerOrgInfoPage.isUserHaveAtleastOneProgram();
		System.out.println("---------------------------"+userHasOneProgram);
		Assert.assertTrue(userHasOneProgram);
	}

	@When("User click on Create Application button on very first program on programs page")
	public void User_click_on_Create_Application_button_on_very_first_program_on_programs_page()
	{
		logger.info("User click on Create Application button on very first program on programs page");

		 programManagerOrgInfoPage.clickOnCreateApplicationButtonOnFirstProgram();
	}


	@Given("I navigate to the {string} page")
	public void i_navigate_to_the_page(String string) {
		String expectedUrlPart = "organization";
        waitFor(5);
		Assert.assertTrue("User is redirected to the dashboard after clicking Finish",
            Driver.getDriver().getCurrentUrl().contains(expectedUrlPart));
		 programManagerOrgInfoPage.goToProgramCreationForm();

       logger.info("Clicked the 'Create a new program' button. Verifying the program creation form...");


        boolean formDisplayed = programManagerOrgInfoPage.isProgramCreationFormDisplayed();
        Assert.assertTrue("Program creation form is not displayed", formDisplayed);

       logger.info("Program creation form is displayed successfully.");
	}

   @And("user clicked on cancel button in Create Application screen")
   public void  user_clicked_on_cancel_button_in_Create_Application_screen()
   {
	 logger.info("user clicked on cancel button in Create Application screen");
	 createApplication.clickOnCancelApplicationButton();
   }


	@When("I enter {string} into the {string} field")
	public void i_enter_into_the_field(String string, String string2) {
		logger.info("Application Title is enetered in create new Application page");
        waitFor(10);
		createApplication.enterApplicationTitle(string);

	}

	@When("I select the {string} option")
	public void i_select_the_option(String string) {
	logger.info("Selecting Aplication Type");

	createApplication.selectApplicationType();
	}


	@When("I click the {string} button")
	public void i_click_the_button(String string) {
		logger.info("Clicked on Save And Continue Button");

		createApplication.clickOnSaveButton();
	}

	@Then("the user should be redirected to the {string} builder page")
	public void the_user_should_be_redirected_to_the_builder_page(String string) {

       logger.info("user is on builders page");

		Assert.assertTrue(createApplication.isUserOnApplicationBuilderPage(string));
	}

	@Given("I am on the {string} builder page and the {string} panel is open")
	public void i_am_on_the_builder_page_and_the_panel_is_open(String string, String string2) {
	logger.info("user is on builders page and checking builder pages headers");

		Assert.assertTrue(createApplication.isApplicationBuilderHeaderDisplayed(string));
		Assert.assertTrue(createApplication.isApplicationBuilderAdditionalHeaderDisplayed(string2));
	}

	@And ("user fills the Additional Questions form with the following data")
	public void  user_fills_the_Additional_Questions_form_with_the_following_data(io.cucumber.datatable.DataTable dataTable)
	{
		if(createApplication.isCompanyDescriptionExpanded()) {
			createApplication.clickOnCollapseButton();
		}
		List<Map<String,String>>rows=dataTable.asMaps(String.class,String.class);
	logger.info("User Entered Questions Title as on this filed");

		for(Map<String,String>row:rows) {
			String answerType=row.get("Type");
			String questionTitle=row.get("Question");
			String instruction=row.get("Instruction");
			waitFor(5);
			createApplication.fillAdditionalQuestionFormAndSave(questionTitle,instruction,answerType);
		}

	}

	@Then("I clicked the {string} buttons")
	public void i_click_On_the_button(String string) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		logger.info("User selcting questions type fro"+string);
		createApplication.clickOnSaveAndContinueButton();
		try {
			Thread.sleep(10000);
		}catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	@Then("The user is redirected to publish page")
	public void The_user_is_redirected_to_publish_page() {
	logger.info("The user is redirected to publish page");
	boolean expectedHeader=publishApplicationPage.IsPublishApplicationPageHeaderIsDisplayed();
	Assert.assertTrue(expectedHeader);
}
	@When ("I set the open date to today")
	public void i_set_the_open_date_to_today() {

     logger.info("Seting the date in Open Date calendar !");
     publishApplicationPage.selectOpenDateInCalendar();
	}






	@And("I click on the {string} button")
	public void i_click_on_the_button(String buttonName) {
	    try {
	        if (buttonName.equalsIgnoreCase("Complete")) {
	            publishApplicationPage.clickCompleteButton();
	            logger.info("Clicked on Complete button successfully");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        Assert.fail("Failed to click " + buttonName + " button: " + e.getMessage());
	    }
	}

	@When("the user clicks on the {string} button for the Company Description field")
	public void the_user_clicks_on_button_for_the_Company_Description_field(String buttonName) {
	    try {
	        logger.info("Clicking on " + buttonName + " button for: Company Description");
	        Thread.sleep(1500);
	        createApplication.clickOnCompanyDescriptionToggleButton();
	        Thread.sleep(1500);
	        logger.info("Successfully clicked on " + buttonName + " button for: Company Description");
	    } catch (Exception e) {
	        e.printStackTrace();
	        Assert.fail("Failed to click on " + buttonName + " button for: Company Description  - " + e.getMessage());
	    }
	}

	@And ("the user clicks on the {string} button for the Year of Founding field")
	public void the_user_clicks_on_button_for_the_Year_of_Founding_field(String buttonName) {
		try {
	        logger.info("Clicking on " + buttonName + " button for: Company Description");
	        createApplication.clickOnYearOfFoundingToggleButton();
	        Thread.sleep(1500);
	        logger.info("Successfully clicked on " + buttonName + " button for: Company Description");
	    } catch (Exception e) {
	        e.printStackTrace();
	        Assert.fail("Failed to click on " + buttonName + " button for: Company Description  - " + e.getMessage());
	    }
	}

	@And ("the user clicks on the {string} button for the Funding field")
	public void the_user_clicks_on_button_for_the_Funding_field(String buttonName) {
		try {
	        logger.info("Clicking on " + buttonName + " button for: Company Description");
	        createApplication.clickOnFundingToggleButton();
	        Thread.sleep(1500);
	        logger.info("Successfully clicked on " + buttonName + " button for: Company Description");
	    } catch (Exception e) {
	        e.printStackTrace();
	        Assert.fail("Failed to click on " + buttonName + " button for: Company Description  - " + e.getMessage());
	    }
	}

	@And ("the user clicks on the {string} button for the Number of full-time employees field")
	public void the_user_clicks_on_button_for_the_Number_of_full_time_employees_field(String buttonName) {
		try {
	        logger.info("Clicking on " + buttonName + " button for: Company Description");
	        createApplication.clickOnNumberOfFullTimeEmployeesToggleButton();
	        Thread.sleep(1500);
	        logger.info("Successfully clicked on " + buttonName + " button for: Company Description");
	    } catch (Exception e) {
	        e.printStackTrace();
	        Assert.fail("Failed to click on " + buttonName + " button for: Company Description  - " + e.getMessage());
	    }
	}

	@And ("the user clicks on the {string} button for the Milestones field")
	public void the_user_clicks_on_button_for_the_Milestones_field(String buttonName) {
		try {
	        logger.info("Clicking on " + buttonName + " button for: Company Description");
	        createApplication.clickOnMilestonesToggleButton();
	        Thread.sleep(1500);
	        logger.info("Successfully clicked on " + buttonName + " button for: Company Description");
	    } catch (Exception e) {
	        e.printStackTrace();
	        Assert.fail("Failed to click on " + buttonName + " button for: Company Description  - " + e.getMessage());
	    }
	}

	@And ("the user clicks on the {string} button for the Pitch Deck field")
	public void the_user_clicks_on_button_for_the_Pitch_Deck_field(String buttonName) {
		try {
	        logger.info("Clicking on " + buttonName + " button for: Company Description");
	        createApplication.clickOnPitchDeckToggleButton();
	        Thread.sleep(5000);
	        logger.info("Successfully clicked on " + buttonName + " button for: Company Description");
	    } catch (Exception e) {
	        e.printStackTrace();
	        Assert.fail("Failed to click on " + buttonName + " button for: Company Description  - " + e.getMessage());
	    }
	}
	@When("the user clicks on the {string} button")
	public void the_user_clicks_on_the_button(String buttonName) {
	    try {
	        logger.info("Attempting to click on: " + buttonName + " button");

	        if (buttonName.equalsIgnoreCase("Preview Application")) {
	        	createApplication.clickPreviewApplicationButton();
	            logger.info("Clicked on Preview Application button successfully");
	        } else if (buttonName.equalsIgnoreCase("Save & Continue")) {
	        	createApplication.clickOnSaveButton();
	            logger.info("Clicked on Save & Continue button successfully");
	        }

	    } catch (Exception e) {
	        logger.error("Failed to click on " + buttonName + " button", e);
	        Assert.fail("Unable to click on " + buttonName + " button due to exception: " + e.getMessage());
	    }
	}

	@Then("the user should see that the hidden questions are not visible on the preview page")
	public void the_user_should_see_that_the_hidden_questions_are_not_visible_on_the_preview_page() {
	    try {
	        logger.info("Verifying that hidden questions are not visible on the Edit Application page");

	        boolean hiddenVisible =editApplication.areHiddenQuestionsVisible();

	        SoftAssert softAssert = new SoftAssert();
	       softAssert.assertFalse(hiddenVisible,
	                " One or more hidden questions are still visible on the Edit Application page!");
	        logger.info(" Hidden questions are successfully not visible on the Edit Application page");

	        softAssert.assertAll();

	    } catch (Exception e) {
	        logger.error("Failed to verify hidden question visibility", e);
	        Assert.fail("Error verifying hidden questions on Edit Application page: " + e.getMessage());
	    }
	}

	}