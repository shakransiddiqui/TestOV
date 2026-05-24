package ov.step_definitions.passport;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;

public class ApplicationEvaluation_stepD extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(ApplicationEvaluation_stepD.class);

	private String selectedApplicationEvaluationName;
	private int selectedApplicationEvaluationRatingValue;
	private String selectedApplicationEvaluationRatingComment;
	private String recordedApplicationEvaluationName;
	private String navigatedApplicationEvaluationName;
	private boolean copyLinkFeedbackObservedOnApplicationEvaluationDetailsPage;
	private Set<String> windowHandlesBeforeOpeningProgramPageFromApplicationEvaluation = new HashSet<>();
	private Set<String> windowHandlesBeforeOpeningEditProgramFromApplicationEvaluation = new HashSet<>();

	@When("User clicks on the first application from the evaluation results")
	public void user_clicks_on_the_first_application_from_the_evaluation_results() {

		selectedApplicationEvaluationName = applicationEvaluation_pom.clickFirstApplicationFromEvaluationResults();

		softAssert.softAssertTrue(
				selectedApplicationEvaluationName != null && !selectedApplicationEvaluationName.isBlank(),
				"Clicked on the first application from the evaluation results successfully: " + selectedApplicationEvaluationName,
				"Failed to click on the first application from the evaluation results.");
	}

	@Then("User verifies the selected application evaluation details page is displayed")
	public void user_verifies_the_selected_application_evaluation_details_page_is_displayed() {

		boolean displayed = applicationEvaluation_pom
				.verifySelectedApplicationEvaluationDetailsPageDisplayed(selectedApplicationEvaluationName);

		softAssert.softAssertTrue(
				displayed,
				"Verified the selected application evaluation details page is displayed successfully.",
				"Failed to verify the selected application evaluation details page is displayed.");
	}

	@When("User clicks on {string} from the application evaluation details page")
	public void user_clicks_on_from_the_application_evaluation_details_page(String buttonLabel) {

		boolean clicked = applicationEvaluation_pom.clickButtonFromApplicationEvaluationDetailsPage(buttonLabel);

		softAssert.softAssertTrue(
				clicked,
				"Clicked on " + buttonLabel + " from the application evaluation details page successfully.",
				"Failed to click on " + buttonLabel + " from the application evaluation details page.");
	}

	@Then("User verifies the applications list is displayed again for the same target program")
	public void user_verifies_the_applications_list_is_displayed_again_for_the_same_target_program() {

		String targetProgramName = ConfigurationReader.getProperty("programEvaluationTargetProgramName");
		boolean displayed = applicationEvaluation_pom.verifyApplicationsListDisplayedAgainForTargetProgram(targetProgramName);

		softAssert.softAssertTrue(
				displayed,
				"Verified the applications list is displayed again for the same target program successfully.",
				"Failed to verify the applications list is displayed again for the same target program.");
	}

	@When("User changes the selected application evaluation status to {string}")
	public void user_changes_the_selected_application_evaluation_status_to(String newStatus) {

		boolean updated = applicationEvaluation_pom.changeSelectedApplicationEvaluationStatusTo(newStatus);

		softAssert.softAssertTrue(
				updated,
				"Changed the selected application evaluation status to \"" + newStatus + "\" successfully.",
				"Failed to change the selected application evaluation status to \"" + newStatus + "\".");
	}

	@Then("User verifies the selected application evaluation status is {string}")
	public void user_verifies_the_selected_application_evaluation_status_is(String expectedStatus) {

		boolean statusMatches = applicationEvaluation_pom.verifySelectedApplicationEvaluationStatusIs(expectedStatus);

		softAssert.softAssertTrue(
				statusMatches,
				"Verified the selected application evaluation status is \"" + expectedStatus + "\" successfully.",
				"Failed to verify the selected application evaluation status is \"" + expectedStatus + "\".");
	}

	@Then("User verifies the application evaluation acceptance confirmation modal is displayed")
	public void user_verifies_the_application_evaluation_acceptance_confirmation_modal_is_displayed() {

		boolean modalDisplayed = applicationManagement_pom.verifyStatusConfirmationModal(
				"Send Email",
				"You have accepted this startup into your program. Would you like to notify them with an email?");

		softAssert.softAssertTrue(
				modalDisplayed,
				"Application evaluation acceptance confirmation modal is displayed correctly.",
				"Application evaluation acceptance confirmation modal is not displayed correctly.");
	}

	@Then("User verifies the application evaluation rejection confirmation modal is displayed")
	public void user_verifies_the_application_evaluation_rejection_confirmation_modal_is_displayed() {

		boolean modalDisplayed = applicationManagement_pom.verifyStatusConfirmationModal(
				"Send Email",
				"You have decided not to move forward with this team. Would you like to notify them with an email?");

		softAssert.softAssertTrue(
				modalDisplayed,
				"Application evaluation rejection confirmation modal is displayed correctly.",
				"Application evaluation rejection confirmation modal is not displayed correctly.");
	}

	@When("User closes the status confirmation modal from the application evaluation details page")
	public void user_closes_the_status_confirmation_modal_from_the_application_evaluation_details_page() {

		boolean closed = applicationManagement_pom.closeStatusConfirmationModal();

		softAssert.softAssertTrue(
				closed,
				"Closed the status confirmation modal from the application evaluation details page successfully.",
				"Failed to close the status confirmation modal from the application evaluation details page.");
	}

	@Then("User verifies {string} is selected on the application evaluation details page")
	public void user_verifies_is_selected_on_the_application_evaluation_details_page(String tabName) {

		boolean selected = applicationEvaluation_pom.verifyApplicationEvaluationRatingTabSelected(tabName);

		softAssert.softAssertTrue(
				selected,
				"Verified \"" + tabName + "\" is selected on the application evaluation details page successfully.",
				"Failed to verify \"" + tabName + "\" is selected on the application evaluation details page.");
	}

	@When("User selects a star rating on the application evaluation details page")
	public void user_selects_a_star_rating_on_the_application_evaluation_details_page() {

		selectedApplicationEvaluationRatingValue = 4;
		boolean selected = applicationEvaluation_pom.selectApplicationEvaluationStarRating(selectedApplicationEvaluationRatingValue);

		softAssert.softAssertTrue(
				selected,
				"Selected a " + selectedApplicationEvaluationRatingValue + "-star rating on the application evaluation details page successfully.",
				"Failed to select a star rating on the application evaluation details page.");
	}

	@When("User enters a rating comment on the application evaluation details page")
	public void user_enters_a_rating_comment_on_the_application_evaluation_details_page() {

		selectedApplicationEvaluationRatingComment = "Automation evaluation rating comment " + System.currentTimeMillis();
		boolean entered = applicationEvaluation_pom.enterApplicationEvaluationRatingComment(selectedApplicationEvaluationRatingComment);

		softAssert.softAssertTrue(
				entered,
				"Entered a rating comment on the application evaluation details page successfully.",
				"Failed to enter a rating comment on the application evaluation details page.");
	}

	@Then("User verifies the rating was submitted successfully on the application evaluation details page")
	public void user_verifies_the_rating_was_submitted_successfully_on_the_application_evaluation_details_page() {

		boolean submitted = applicationEvaluation_pom.verifyApplicationEvaluationRatingWasSubmittedSuccessfully(
				selectedApplicationEvaluationRatingValue,
				selectedApplicationEvaluationRatingComment);

		softAssert.softAssertTrue(
				submitted,
				"Verified the rating was submitted successfully on the application evaluation details page.",
				"Failed to verify the rating was submitted successfully on the application evaluation details page.");
	}

	@Then("User verifies the rating was not submitted without selecting stars on the application evaluation details page")
	public void user_verifies_the_rating_was_not_submitted_without_selecting_stars_on_the_application_evaluation_details_page() {

		boolean notSubmitted = applicationEvaluation_pom.verifyRatingWasNotSubmittedWithoutSelectingStars();

		softAssert.softAssertTrue(
				notSubmitted,
				"Verified the rating was not submitted without selecting stars on the application evaluation details page.",
				"Failed to verify the rating was not submitted without selecting stars on the application evaluation details page.");
	}

	@Then("User verifies the No Submitted Ratings empty state is displayed on the application evaluation details page")
	public void user_verifies_the_no_submitted_ratings_empty_state_is_displayed_on_the_application_evaluation_details_page() {

		boolean displayed = applicationEvaluation_pom.verifyNoSubmittedRatingsEmptyStateDisplayed();

		softAssert.softAssertTrue(
				displayed,
				"Verified the No Submitted Ratings empty state is displayed on the application evaluation details page.",
				"Failed to verify the No Submitted Ratings empty state is displayed on the application evaluation details page.");
	}

	@Then("User verifies the {string} view is displayed on the application evaluation details page")
	public void user_verifies_the_view_is_displayed_on_the_application_evaluation_details_page(String viewName) {

		boolean displayed = false;
		if ("All Ratings".equals(viewName)) {
			displayed = applicationEvaluation_pom.verifyAllRatingsViewDisplayedOnApplicationEvaluationDetailsPage();
		}

		softAssert.softAssertTrue(
				displayed,
				"Verified the \"" + viewName + "\" view is displayed on the application evaluation details page successfully.",
				"Failed to verify the \"" + viewName + "\" view is displayed on the application evaluation details page.");
	}

	@Then("User records the current application name from the application evaluation details page")
	public void user_records_the_current_application_name_from_the_application_evaluation_details_page() {

		recordedApplicationEvaluationName = applicationEvaluation_pom.getCurrentApplicationNameFromApplicationEvaluationDetailsPage();

		softAssert.softAssertTrue(
				recordedApplicationEvaluationName != null && !recordedApplicationEvaluationName.isBlank(),
				"Recorded the current application name from the application evaluation details page successfully: " + recordedApplicationEvaluationName,
				"Failed to record the current application name from the application evaluation details page.");
	}

	@When("User clicks on the next application arrow from the application evaluation details page")
	public void user_clicks_on_the_next_application_arrow_from_the_application_evaluation_details_page() {

		navigatedApplicationEvaluationName = applicationEvaluation_pom.clickApplicationNavigationArrowAndGetDisplayedName(
				"next",
				recordedApplicationEvaluationName);

		softAssert.softAssertTrue(
				navigatedApplicationEvaluationName != null && !navigatedApplicationEvaluationName.isBlank(),
				"Clicked on the next application arrow from the application evaluation details page successfully.",
				"Failed to click on the next application arrow from the application evaluation details page.");
	}

	@Then("User verifies the displayed application changed on the application evaluation details page")
	public void user_verifies_the_displayed_application_changed_on_the_application_evaluation_details_page() {

		boolean changed = navigatedApplicationEvaluationName != null
				&& recordedApplicationEvaluationName != null
				&& !navigatedApplicationEvaluationName.equals(recordedApplicationEvaluationName);

		softAssert.softAssertTrue(
				changed,
				"Verified the displayed application changed on the application evaluation details page successfully.",
				"Failed to verify the displayed application changed on the application evaluation details page.");
	}

	@When("User clicks on the previous application arrow from the application evaluation details page")
	public void user_clicks_on_the_previous_application_arrow_from_the_application_evaluation_details_page() {

		String returnedApplicationName = applicationEvaluation_pom.clickApplicationNavigationArrowAndGetDisplayedName(
				"previous",
				navigatedApplicationEvaluationName);

		softAssert.softAssertTrue(
				returnedApplicationName != null && !returnedApplicationName.isBlank(),
				"Clicked on the previous application arrow from the application evaluation details page successfully.",
				"Failed to click on the previous application arrow from the application evaluation details page.");

		navigatedApplicationEvaluationName = returnedApplicationName;
	}

	@Then("User verifies the previously recorded application is displayed again on the application evaluation details page")
	public void user_verifies_the_previously_recorded_application_is_displayed_again_on_the_application_evaluation_details_page() {

		boolean restored = recordedApplicationEvaluationName != null
				&& recordedApplicationEvaluationName.equals(navigatedApplicationEvaluationName);

		softAssert.softAssertTrue(
				restored,
				"Verified the previously recorded application is displayed again on the application evaluation details page successfully.",
				"Failed to verify the previously recorded application is displayed again on the application evaluation details page.");
	}

	@When("User clicks on the email icon from the application evaluation details page")
	public void user_clicks_on_the_email_icon_from_the_application_evaluation_details_page() {

		boolean opened = applicationEvaluation_pom.clickEmailIconFromApplicationEvaluationDetailsPage();

		softAssert.softAssertTrue(
				opened,
				"Clicked on the email icon from the application evaluation details page successfully.",
				"Failed to click on the email icon from the application evaluation details page.");
	}

	@When("User clicks on the copy link icon from the application evaluation details page")
	public void user_clicks_on_the_copy_link_icon_from_the_application_evaluation_details_page() {

		boolean clicked = applicationEvaluation_pom.clickCopyLinkIconFromApplicationEvaluationDetailsPage();
		copyLinkFeedbackObservedOnApplicationEvaluationDetailsPage = clicked
				&& applicationEvaluation_pom.captureCopyLinkFeedbackOnApplicationEvaluationDetailsPage();

		softAssert.softAssertTrue(
				clicked,
				"Clicked on the copy link icon from the application evaluation details page successfully.",
				"Failed to click on the copy link icon from the application evaluation details page.");
	}

	@Then("User verifies the copy link feedback is displayed on the application evaluation details page")
	public void user_verifies_the_copy_link_feedback_is_displayed_on_the_application_evaluation_details_page() {

		boolean displayed = copyLinkFeedbackObservedOnApplicationEvaluationDetailsPage
				|| applicationEvaluation_pom.verifyCopyLinkFeedbackDisplayedOnApplicationEvaluationDetailsPage();

		softAssert.softAssertTrue(
				displayed,
				"Verified the copy link feedback is displayed on the application evaluation details page successfully.",
				"Failed to verify the copy link feedback is displayed on the application evaluation details page.");
	}

	@When("User refreshes the application evaluation details page")
	public void user_refreshes_the_application_evaluation_details_page() {

		boolean refreshed = applicationEvaluation_pom.refreshApplicationEvaluationDetailsPage();

		softAssert.softAssertTrue(
				refreshed,
				"Refreshed the application evaluation details page successfully.",
				"Failed to refresh the application evaluation details page.");
	}

	@Then("User verifies the submitted rating still persists on the application evaluation details page")
	public void user_verifies_the_submitted_rating_still_persists_on_the_application_evaluation_details_page() {

		boolean persisted = applicationEvaluation_pom.verifySubmittedRatingStillPersistsAfterRefresh(
				selectedApplicationEvaluationRatingValue,
				selectedApplicationEvaluationRatingComment);

		softAssert.softAssertTrue(
				persisted,
				"Verified the submitted rating still persists on the application evaluation details page successfully.",
				"Failed to verify the submitted rating still persists on the application evaluation details page.");
	}

	@When("User clicks on the Go To Program Page icon from the application evaluation details page")
	public void user_clicks_on_the_go_to_program_page_icon_from_the_application_evaluation_details_page() {

		windowHandlesBeforeOpeningProgramPageFromApplicationEvaluation = new HashSet<>(driver.getWindowHandles());
		boolean clicked = applicationManagement_pom.clickEvaluationQuickAction("Go To Program Page");

		softAssert.softAssertTrue(
				clicked,
				"Clicked on the Go To Program Page icon from the application evaluation details page successfully.",
				"Failed to click on the Go To Program Page icon from the application evaluation details page.");
	}

	@Then("User switches to the newly opened Program page tab from the application evaluation details page")
	public void user_switches_to_the_newly_opened_program_page_tab_from_the_application_evaluation_details_page() {

		boolean switched = applicationManagement_pom.switchToNewProgramPageTab(
				windowHandlesBeforeOpeningProgramPageFromApplicationEvaluation);

		softAssert.softAssertTrue(
				switched,
				"Switched to the newly opened Program page tab from the application evaluation details page successfully.",
				"Failed to switch to the newly opened Program page tab from the application evaluation details page.");
	}

	@Then("User verifies the Program page is displayed from the application evaluation details page")
	public void user_verifies_the_program_page_is_displayed_from_the_application_evaluation_details_page() {

		boolean displayed = applicationManagement_pom.verifyProgramPageOpenedFromEvaluationQuickAction();

		softAssert.softAssertTrue(
				displayed,
				"Verified the Program page is displayed from the application evaluation details page successfully.",
				"Failed to verify the Program page is displayed from the application evaluation details page.");
	}

	@When("User opens the three-dot menu from the application evaluation details page")
	public void user_opens_the_three_dot_menu_from_the_application_evaluation_details_page() {

		boolean opened = applicationManagement_pom.openEvaluationHeaderMenu();

		softAssert.softAssertTrue(
				opened,
				"Opened the three-dot menu from the application evaluation details page successfully.",
				"Failed to open the three-dot menu from the application evaluation details page.");
	}

	@Then("User verifies {string} is displayed in the three-dot menu from the application evaluation details page")
	public void user_verifies_is_displayed_in_the_three_dot_menu_from_the_application_evaluation_details_page(String optionName) {

		boolean displayed = applicationManagement_pom.verifyEvaluationHeaderMenuOptionDisplayed(optionName);

		softAssert.softAssertTrue(
				displayed,
				optionName + " is displayed in the three-dot menu from the application evaluation details page successfully.",
				optionName + " is not displayed in the three-dot menu from the application evaluation details page.");
	}

	@When("User clicks on {string} from the three-dot menu on the application evaluation details page")
	public void user_clicks_on_from_the_three_dot_menu_on_the_application_evaluation_details_page(String optionName) {

		windowHandlesBeforeOpeningEditProgramFromApplicationEvaluation = new HashSet<>(driver.getWindowHandles());
		boolean clicked = applicationManagement_pom.clickEvaluationHeaderMenuOption(optionName);

		softAssert.softAssertTrue(
				clicked,
				"Clicked on " + optionName + " from the three-dot menu on the application evaluation details page successfully.",
				"Failed to click on " + optionName + " from the three-dot menu on the application evaluation details page.");
	}

	@Then("User switches to the newly opened Edit Program tab from the application evaluation details page")
	public void user_switches_to_the_newly_opened_edit_program_tab_from_the_application_evaluation_details_page() {

		boolean switched = applicationManagement_pom.switchToNewEditProgramTab(
				windowHandlesBeforeOpeningEditProgramFromApplicationEvaluation);

		softAssert.softAssertTrue(
				switched,
				"Switched to the newly opened Edit Program tab from the application evaluation details page successfully.",
				"Failed to switch to the newly opened Edit Program tab from the application evaluation details page.");
	}

	@Then("User verifies the Edit Program page and form are displayed from the application evaluation details page")
	public void user_verifies_the_edit_program_page_and_form_are_displayed_from_the_application_evaluation_details_page() {

		boolean displayed = applicationManagement_pom.verifyEditProgramPageAndFormDisplayed();

		softAssert.softAssertTrue(
				displayed,
				"Verified the Edit Program page and form are displayed from the application evaluation details page successfully.",
				"Failed to verify the Edit Program page and form are displayed from the application evaluation details page.");
	}
}
message.txt