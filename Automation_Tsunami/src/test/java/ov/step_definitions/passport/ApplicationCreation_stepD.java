package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;

public class ApplicationCreation_stepD extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(ApplicationCreation_stepD.class);


	//	***************************************************************************************************************
	@Then("User enters {string} into the {string} field of Create application")
	public void user_enters_into_the_field_of_create_application(String fieldValueKey, String fieldName) {

		String expectedFieldValue = ConfigurationReader.getProperty(fieldValueKey);

		logger.info("Resolved key [" + fieldValueKey + "] to value: " + expectedFieldValue);

		String actualFieldValue = applicationCreation_pom.passFieldValue(expectedFieldValue, fieldName);

		softAssert.softAssertEquals(
				actualFieldValue,
				expectedFieldValue,
				"Actual Field Value: " + actualFieldValue + " , Expected Field Value: " + expectedFieldValue
				);
	}

	//	***************************************************************************************************************
	@Then("User selects the {string} option on Create Application page")
	public void user_selects_the_option_on_create_application_page(String optionName) {

		logger.info("Selecting option on Create Application page: " + optionName);

		boolean selected = applicationCreation_pom.selectOption(optionName);

		softAssert.softAssertTrue(
				selected,
				"Option selected successfully: " + optionName,
				"Failed to select option: " + optionName
				);
	}
	//	***************************************************************************************************************
	@Then("User should see {string} in the Application Builder page")
	public void user_should_see_on_top(String textElement) {

		logger.info("Verifying redirection to Application Builder page...");

		boolean onPage = applicationCreation_pom.isOnPage(textElement);

		softAssert.softAssertTrue(
				onPage,
				"User is redirected to Application Builder page.",
				"User is NOT on Application Builder page."
				);
	}

	//	***************************************************************************************************************
	@Then("User clicks the collapse icon on the Application Builder page")
	public void user_clicks_the_collapse_icon_on_the_application_builder_page() {

		logger.info("Clicking collapse icon on Application Builder page...");

		boolean clicked = applicationCreation_pom.clickCollapseIcon();

		softAssert.softAssertTrue(
				clicked,
				"Collapse icon clicked successfully.",
				"Failed to click collapse icon."
				);
	}

	//	***************************************************************************************************************
	@Then("Standard Questions should be collapsed")
	public void standard_questions_should_be_collapsed() {

		logger.info("Verifying Standard Questions are collapsed...");

		boolean collapsed = applicationCreation_pom.standardQuestionsCollapsed();

		softAssert.softAssertTrue(
				collapsed,
				"Standard Questions are collapsed.",
				"Standard Questions are NOT collapsed."
				);
	}

	//	***************************************************************************************************************
	@Then("User scrolls to {string} section")
	public void user_scrolls_to_section(String sectionName) {

		logger.info("Scrolling to section: " + sectionName);

		boolean scrolled = applicationCreation_pom.scrollToSection(sectionName);

		softAssert.softAssertTrue(
				scrolled,
				"Scrolled to section: " + sectionName,
				"Failed to scroll to section: " + sectionName
				);
	}

	//	***************************************************************************************************************
	@Then("User clicks on {string} in Additional Questions section")
	public void user_clicks_on_in_additional_questions_section(String buttonText) {

		logger.info("Clicking on [" + buttonText + "] in Additional Questions section...");

		boolean clicked = applicationCreation_pom.clickAddNewQuestion(buttonText);

		softAssert.softAssertTrue(
				clicked,
				"Clicked on: " + buttonText,
				"Failed to click on: " + buttonText
				);
	}

	//	***************************************************************************************************************
	@Then("User should see the New Question form")
	public void user_should_see_the_new_question_form() {

		logger.info("Verifying New Question form is visible...");

		boolean visible = applicationCreation_pom.newQuestionFormVisible();

		softAssert.softAssertTrue(
				visible,
				"New Question form is visible.",
				"New Question form is NOT visible."
				);
	}


	//	***************************************************************************************************************
	@Then("User fills the Additional Questions form with the following data")
	public void user_fills_additional_questions_form(DataTable dataTable) {

		int expected = dataTable.asMaps(String.class, String.class).size();
		int actual = applicationCreation_pom.addAdditionalQuestions(dataTable.asMaps(String.class, String.class));

		softAssert.softAssertTrue(
				actual == expected,
				"Added all additional questions. Count=" + actual,
				"Failed to add all additional questions. Expected=" + expected + " | Actual=" + actual
				);
	}

	//	***************************************************************************************************************
	@Then("User clicks on {string} button on the Application Builder page")
	public void user_clicks_on_button_on_the_application_builder_page(String buttonText) {

		logger.info("Clicking on Builder page button: " + buttonText);

		boolean clicked = false;

		if ("Preview Application".equalsIgnoreCase(buttonText)) {
			clicked = applicationCreation_pom.clickPreviewApplicationOnBuilder();

		} else if ("Save & Continue".equalsIgnoreCase(buttonText)) {
			clicked = applicationCreation_pom.clickBuilderSaveAndContinue();

		} else {
			logger.warn("Unsupported builder button in this step: " + buttonText);
			clicked = false;
		}

		softAssert.softAssertTrue(
				clicked,
				"Clicked on Builder button: " + buttonText,
				"Failed to click on Builder button: " + buttonText
				);
	}

	//	***************************************************************************************************************
	@Then("User should be on the Preview Application page")
	public void user_should_be_on_the_preview_application_page() {

		logger.info("Verifying Preview Application page is displayed...");

		boolean onPreview = applicationCreation_pom.isOnPreviewApplicationPage();
		boolean standardActive = applicationCreation_pom.isStandardQuestionsTabActiveOnPreview();

		softAssert.softAssertTrue(
				onPreview,
				"Preview Application page loaded successfully.",
				"Preview Application page NOT loaded."
				);

		softAssert.softAssertTrue(
				standardActive,
				"Standard Questions tab is active on Preview (expected first screen).",
				"Standard Questions tab is NOT active on Preview."
				);
	}


	// ***************************************************************************************************************
	@Then("Applicant clicks on {string} button on the Preview Application page")
	public void applicant_clicks_on_button_on_the_preview_application_page(String buttonText) {

		logger.info("Applicant clicking on Preview page button: " + buttonText);

		boolean clicked = false;

		if ("Save & Continue".equalsIgnoreCase(buttonText)) {
			clicked = applicationCreation_pom.applicantClickSaveAndContinueOnPreview();
		} else {
			logger.warn("Unsupported preview applicant button in this step: " + buttonText);
			clicked = false;
		}

		softAssert.softAssertTrue(
				clicked,
				"Applicant clicked on Preview button: " + buttonText,
				"Applicant FAILED to click on Preview button: " + buttonText
				);
	}

	// ***************************************************************************************************************
	@Then("Applicant should be on the Preview Additional Questions section")
	public void applicant_should_be_on_the_preview_additional_questions_section() {

		logger.info("Verifying applicant is on Preview Additional Questions section...");

		boolean onAQ = applicationCreation_pom.isAdditionalQuestionsTabActiveOnPreview();

		softAssert.softAssertTrue(
				onAQ,
				"Applicant is on Preview Additional Questions section (AQ tab is active).",
				"Applicant is NOT on Preview Additional Questions section (AQ tab not active)."
				);
	}

	// ***************************************************************************************************************
	@Then("Applicant should see {string} and {string} buttons on the Preview Additional Questions section")
	public void applicant_should_see_back_and_submit_buttons(String backText, String submitText) {

		logger.info("Verifying applicant sees buttons on Preview AQ: " + backText + " and " + submitText);

		boolean visible = applicationCreation_pom.areBackAndSubmitVisibleOnPreviewAQ();

		softAssert.softAssertTrue(
				visible,
				"Back and Submit buttons are visible on Preview Additional Questions section.",
				"Back and/or Submit buttons are NOT visible on Preview Additional Questions section."
				);
	}

	// ***************************************************************************************************************
	@Then("Applicant should see all added Additional Questions on the Preview page")
	public void applicant_should_see_all_added_additional_questions_on_preview() {

		logger.info("Verifying all added Additional Questions are visible on Preview...");

		boolean ok = applicationCreation_pom.areAllAddedAQVisibleOnPreview();

		softAssert.softAssertTrue(
				ok,
				"All added Additional Questions are visible on Preview.",
				"Some added Additional Questions are missing on Preview."
				);
	}

	// ***************************************************************************************************************
	@Then("User clicks on {string} button on the Preview Application page")
	public void user_clicks_on_button_on_the_preview_application_page(String buttonText) {

		logger.info("User clicking on Preview page button: " + buttonText);

		boolean clicked = false;

		if ("Back to Application".equalsIgnoreCase(buttonText)) {
			clicked = applicationCreation_pom.clickBackToApplicationOnPreview();
		} else {
			logger.warn("Unsupported preview page button in this step: " + buttonText);
			clicked = false;
		}

		softAssert.softAssertTrue(
				clicked,
				"Clicked on Preview page button: " + buttonText,
				"Failed to click on Preview page button: " + buttonText
				);
	}

	//	***************************************************************************************************************
	//	@Then("User sets the Publish Open Date to now and Close Date to one month from now")
	//	public void user_sets_publish_open_date_now_and_close_date_one_month() {
	//
	//		logger.info("Setting Publish Open Date=now and Close Date=now+1 month...");
	//		
	//		boolean ok = applicationCreation_pom.setPublishOpenDateToNow();
	//
	//		softAssert.softAssertTrue(
	//				ok,
	//				"Publish Open & Close dates were set successfully.",
	//				"Failed to set Publish Open and/or Close date."
	//				);
	//	}

	@Then("User sets the Publish Open Date to now")
	public void user_sets_the_publish_open_date_to_now() {

		logger.info("Setting Publish Open Date=now ...");

		boolean ok = applicationCreation_pom.setPublishOpenDateToNow();

		softAssert.softAssertTrue(
				ok,
				"Publish Open date was set successfully.",
				"Failed to set Publish Open date."
				);
	}


	//	***************************************************************************************************************
	@Then("User copies the Application Link on the Publish page")
	public void user_copies_the_application_link_on_the_publish_page() {

		logger.info("Copying Application Link and verifying 'Link Copied'...");

		boolean ok = applicationCreation_pom.copyApplicationLinkAndSeeCopied();

		softAssert.softAssertTrue(
				ok,
				"Application Link copied successfully (Link Copied displayed).",
				"Failed to copy Application Link (Link Copied not displayed)."
				);
	}

	//	***************************************************************************************************************
	@Then("User adds a random invite email and verifies it appears in the list")
	public void user_adds_random_invite_email_and_verifies_it_appears() {

		logger.info("Adding a random invite email and verifying it appears in the list...");

		String email = applicationCreation_pom.addRandomInviteEmailAndVerifyListed();

		softAssert.softAssertTrue(
				email != null,
				"Invite email added and listed successfully: " + email,
				"Failed to add invite email or email not listed."
				);
	}

	//	***************************************************************************************************************
	@Then("User deletes the last added invite email and verifies it is removed")
	public void user_deletes_last_added_invite_email_and_verifies_removed() {

		logger.info("Deleting last added invite email and verifying it is removed...");

		boolean ok = applicationCreation_pom.deleteLastInvitedEmailAndVerifyRemoved();

		softAssert.softAssertTrue(
				ok,
				"Invite email deleted and removed from list.",
				"Failed to delete invite email or it still appears in the list."
				);
	}

	//	***************************************************************************************************************
	@Then("User clicks on {string} button on the Publish page")
	public void user_clicks_on_button_on_the_publish_page(String buttonText) {

		logger.info("Clicking on Publish page button: " + buttonText);

		boolean clicked = false;

		if ("Complete".equalsIgnoreCase(buttonText)) {
			clicked = applicationCreation_pom.clickCompleteOnPublish();
		} else {
			logger.warn("Unsupported Publish button in this step: " + buttonText);
			clicked = false;
		}

		softAssert.softAssertTrue(
				clicked,
				"Clicked on Publish button: " + buttonText,
				"Failed to click on Publish button: " + buttonText
				);
	}

	//***************************************************************************************************************
	@Then("User verifies Rubric section components")
	public void user_verifies_rubric_section_components() {

		// 2) Help text visible
		softAssert.softAssertTrue(
				applicationCreation_pom.isRubricHelpTextVisible(),
				"Rubric help text is visible.",
				"Rubric help text is NOT visible."
				);

		// 3) Rating question visible
		softAssert.softAssertTrue(
				applicationCreation_pom.isRubricRatingQuestionVisible(),
				"Rubric rating question is visible.",
				"Rubric rating question is NOT visible."
				);

		// 4) Star widget exists and has 5 stars
		int starCount = applicationCreation_pom.waitForRubricStarCount(5);
		softAssert.softAssertEquals(
				starCount, 5,
				"Rubric star count should be 5 but was: " + starCount
				);

		// 5) Click star #3 and verify 3 selected stars
		softAssert.softAssertTrue(
				applicationCreation_pom.clickRubricStarAndVerify(3),
				"Rubric star selection works (clicked 3 => 3 selected).",
				"Rubric star selection FAILED (clicked 3 did not result in 3 selected)."
				);

		// 6) Comment box visible (read-only for creator, so just presence check)
		softAssert.softAssertTrue(
				applicationCreation_pom.isRubricCommentBoxVisible(),
				"Rubric comment box is visible.",
				"Rubric comment box is NOT visible."
				);

		// 7) Add New Question visible
		softAssert.softAssertTrue(
				applicationCreation_pom.isRubricAddNewQuestionVisible(),
				"Rubric Add New Question button is visible.",
				"Rubric Add New Question button is NOT visible."
				);
	}

	//	*****************************Steps for Negative tests************************************************************

	@Then("User should see {string} validation message for Additional Question")
	public void user_should_see_validation_message_for_additional_question(String expectedMsg) {

		logger.info("Verifying AQ validation message: " + expectedMsg);

		String actualMsg = applicationCreation_pom.getAQQuestionRequiredMessageText();

		softAssert.softAssertTrue(
				actualMsg != null && actualMsg.trim().equals(expectedMsg.trim()),
				"Validation message matched. Actual='" + actualMsg + "'",
				"Validation message mismatch. Expected='" + expectedMsg + "' | Actual='" + actualMsg + "'"
				);
	}


	//	***************************************************************************************************************
	@Then("Additional Question {string} button should be disabled")
	public void additional_question_button_should_be_disabled(String buttonText) {

		logger.info("Verifying Additional Question button is disabled: " + buttonText);

		boolean disabled = applicationCreation_pom.isAQSaveButtonDisabled();

		softAssert.softAssertTrue(
				disabled,
				"AQ '" + buttonText + "' button is disabled (expected).",
				"AQ '" + buttonText + "' button is NOT disabled (unexpected)."
				);
	}

	//	***************************************************************************************************************
	@When("User clicks on the delete icon for the current Additional Question")
	public void user_clicks_on_delete_icon_for_current_additional_question() {

		logger.info("Clicking delete icon for the current open Additional Question...");

		boolean ok = applicationCreation_pom.clickDeleteIconForCurrentAdditionalQuestion();

		softAssert.softAssertTrue(
				ok,
				"Delete icon clicked and popup opened.",
				"Failed to click delete icon OR popup did not open."
				);
	}


	//	***************************************************************************************************************
	@Then("User should see the Delete Question popup")
	public void user_should_see_the_delete_question_popup() {

		logger.info("Verifying Delete Question popup is visible...");

		boolean visible = applicationCreation_pom.isDeleteQuestionPopupVisible();

		softAssert.softAssertTrue(
				visible,
				"Delete Question popup is visible.",
				"Delete Question popup is NOT visible."
				);
	}


	//	***************************************************************************************************************
	@Then("User clicks on {string} in the Delete Question popup")
	public void user_clicks_on_in_the_delete_question_popup(String btnText) {

		logger.info("Clicking '" + btnText + "' in Delete Question popup...");

		boolean ok = applicationCreation_pom.clickDeletePopupButton(btnText);

		softAssert.softAssertTrue(
				ok,
				"Clicked '" + btnText + "' in Delete Question popup.",
				"Failed to click '" + btnText + "' in Delete Question popup."
				);
	}




}
