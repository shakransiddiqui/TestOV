package ov.step_definitions.passport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;

public class ApplicationManagement_stepD extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(ApplicationManagement_stepD.class);

	private String parentWindowHandle;
	private Set<String> windowHandlesBeforeOpeningEvaluation = new HashSet<>();
	private Set<String> windowHandlesBeforeOpeningEditProgram = new HashSet<>();
	private Set<String> windowHandlesBeforeOpeningPreviewApplication = new HashSet<>();
	private Set<String> windowHandlesBeforeOpeningEditApplication = new HashSet<>();
	private Set<String> windowHandlesBeforeOpeningProgramPage = new HashSet<>();
	private int totalApplicationsCount;
	private int currentPageApplicationsCount;
	private int displayedApplicationRowCount;
	private String lastEvaluationPageNumber;
	private String selectedEvaluationApplicationName;
	private String statusTargetApplicationName;
	private String statusTargetApplicationCurrentPage;
	private String statusTargetApplicationOriginalStatus;
	private Map<String, Integer> baselineStatusFilterCounts = new HashMap<>();
	private List<String> randomOnHoldApplicationNames = new ArrayList<>();
	private String acceptedNoThanksApplicationName;
	private String acceptedYesSendEmailApplicationName;
	private List<String> acceptedApplicationNames = new ArrayList<>();
	private String rejectedNoThanksApplicationName;
	private String rejectedYesSendEmailApplicationName;
	private List<String> rejectedApplicationNames = new ArrayList<>();
	private List<String> bulkPageOneApplicationNames = new ArrayList<>();
	private List<String> bulkRandomEmailApplicationNames = new ArrayList<>();
	private String rowEmailApplicationName;
	private String filterValidationOnHoldApplicationName;
	private String filterValidationAcceptedApplicationName;
	private String filterValidationRejectedApplicationName;
	private String searchedTargetApplicationName;
	private List<String> filterValidationApplicationNames = new ArrayList<>();

	private String getProgramNameFromConfigKey(String configKey) {
		String targetProgramName = ConfigurationReader.getProperty(configKey);
		if (targetProgramName == null) {
			logger.warn("[ApplicationManagement_stepD] No configuration value was found for configKey='" + configKey + "'");
			return null;
		}
		targetProgramName = targetProgramName.trim();
		logger.info("[ApplicationManagement_stepD] Resolved configKey='" + configKey + "' to targetProgramName='" + targetProgramName + "'");
		return targetProgramName;
	}

	@Then("User opens Manage Applications for the configured target program")
	public void user_opens_manage_applications_for_the_configured_target_program() {

		user_opens_manage_applications_for_the_target_program_from_config_key("programManagerTargetProgramName");
	}

	@Then("User opens Manage Applications for the target program from config key {string}")
	public void user_opens_manage_applications_for_the_target_program_from_config_key(String configKey) {

		String targetProgramName = getProgramNameFromConfigKey(configKey);
		logger.info("[ApplicationManagement_stepD] Opening Manage Applications for configKey='" + configKey
				+ "' targetProgramName='" + targetProgramName + "'");
		parentWindowHandle = driver.getWindowHandle();
		windowHandlesBeforeOpeningEvaluation = new HashSet<>(driver.getWindowHandles());
		logger.info("[ApplicationManagement_stepD] windowHandlesBeforeOpeningEvaluation=" + windowHandlesBeforeOpeningEvaluation);

		boolean opened = applicationManagement_pom.openManageApplicationsForTargetProgram(targetProgramName);

		softAssert.softAssertTrue(
				opened,
				"Opened Manage Applications for target program successfully: " + targetProgramName,
				"Failed to open Manage Applications for target program: " + targetProgramName);
	}
//************************************************************************************************************
	@Then("User switches to the newly opened application evaluation tab")
	public void user_switches_to_the_newly_opened_application_evaluation_tab() {

		logger.info("[ApplicationManagement_stepD] Switching to the newly opened application evaluation tab");
		
		boolean switched = applicationManagement_pom.switchToNewEvaluationTab(windowHandlesBeforeOpeningEvaluation);

		softAssert.softAssertTrue(
				switched,
				"Switched to the newly opened application evaluation tab successfully.",
				"Failed to switch to the newly opened application evaluation tab.");
	}
	
//***************************************************************************************************************

	@Then("User waits for the target program applications to load on evaluation page")
	public void user_waits_for_the_target_program_applications_to_load_on_evaluation_page() {

		boolean loaded = applicationManagement_pom.waitForApplicationsToLoad();

		softAssert.softAssertTrue(
				loaded,
				"Target program applications loaded successfully on the evaluation page.",
				"Target program applications did not load correctly on the evaluation page.");
	}

	@When("User searches for the configured target application from the evaluation page")
	public void user_searches_for_the_configured_target_application_from_the_evaluation_page() {

		searchedTargetApplicationName = ConfigurationReader.getProperty("programManagerTargetApplicationName");
		if (searchedTargetApplicationName != null) {
			searchedTargetApplicationName = searchedTargetApplicationName.trim();
		}

		boolean searched = applicationManagement_pom.searchForApplicationFromEvaluationPage(searchedTargetApplicationName);

		softAssert.softAssertTrue(
				searched,
				"Searched for the configured target application from the evaluation page successfully: " + searchedTargetApplicationName,
				"Failed to search for the configured target application from the evaluation page: " + searchedTargetApplicationName);
	}

	@Then("Search Results section should appear on the evaluation page")
	public void search_results_section_should_appear_on_the_evaluation_page() {

		boolean displayed = applicationManagement_pom.verifySearchResultsSectionDisplayedOnEvaluationPage();

		softAssert.softAssertTrue(
				displayed,
				"Search Results section appeared on the evaluation page successfully.",
				"Search Results section did not appear on the evaluation page.");
	}

	@Then("Matching application row(s) should be displayed for the configured target application")
	public void matching_application_rows_should_be_displayed_for_the_configured_target_application() {

		boolean displayed = applicationManagement_pom.verifyMatchingApplicationRowsDisplayedForTargetApplication(searchedTargetApplicationName);

		softAssert.softAssertTrue(
				displayed,
				"Matching application row(s) were displayed for the configured target application successfully: " + searchedTargetApplicationName,
				"Matching application row(s) were not displayed for the configured target application: " + searchedTargetApplicationName);
	}

	@Then("Each matching application row startup name should contain the configured target application name")
	public void each_matching_application_row_startup_name_should_contain_the_configured_target_application_name() {

		boolean matched = applicationManagement_pom.verifyEachMatchingApplicationRowStartupNameContains(searchedTargetApplicationName);

		softAssert.softAssertTrue(
				matched,
				"Each matching application row startup name contained the configured target application name successfully: " + searchedTargetApplicationName,
				"One or more matching application row startup names did not contain the configured target application name: " + searchedTargetApplicationName);
	}

	@Then("User verifies the {string} quick action is displayed on evaluation page")
	public void user_verifies_the_quick_action_is_displayed_on_evaluation_page(String quickActionName) {

		boolean displayed = applicationManagement_pom.verifyEvaluationQuickActionDisplayed(quickActionName);

		softAssert.softAssertTrue(
				displayed,
				quickActionName + " quick action is displayed on the evaluation page successfully.",
				quickActionName + " quick action is not displayed on the evaluation page.");
	}

	@When("User clicks on the {string} quick action from evaluation page")
	public void user_clicks_on_the_quick_action_from_evaluation_page(String quickActionName) {

		windowHandlesBeforeOpeningProgramPage = new HashSet<>(driver.getWindowHandles());
		boolean clicked = applicationManagement_pom.clickEvaluationQuickAction(quickActionName);

		softAssert.softAssertTrue(
				clicked,
				"Clicked on the " + quickActionName + " quick action from the evaluation page successfully.",
				"Failed to click on the " + quickActionName + " quick action from the evaluation page.");
	}

	@Then("User switches to the newly opened Program page tab")
	public void user_switches_to_the_newly_opened_program_page_tab() {

		boolean switched = applicationManagement_pom.switchToNewProgramPageTab(windowHandlesBeforeOpeningProgramPage);

		softAssert.softAssertTrue(
				switched,
				"Switched to the newly opened Program page tab successfully.",
				"Failed to switch to the newly opened Program page tab.");
	}

	@Then("User should be redirected to the target program page from the evaluation quick action")
	public void user_should_be_redirected_to_the_target_program_page_from_the_evaluation_quick_action() {

		boolean redirected = applicationManagement_pom.verifyRedirectedToProgramPageFromEvaluationQuickAction(
				windowHandlesBeforeOpeningProgramPage, null);

		softAssert.softAssertTrue(
				redirected,
				"Redirected to the target program page from the evaluation quick action successfully.",
				"Failed to redirect to the target program page from the evaluation quick action.");
	}

	@Then("User verifies the Program page and application card are displayed from the evaluation quick action")
	public void user_verifies_the_program_page_and_application_card_are_displayed_from_the_evaluation_quick_action() {

		boolean displayed = applicationManagement_pom.verifyProgramPageOpenedFromEvaluationQuickAction();

		softAssert.softAssertTrue(
				displayed,
				"Verified the Program page and application card are displayed from the evaluation quick action successfully.",
				"Failed to verify the Program page and application card are displayed from the evaluation quick action.");
	}

	@When("User opens the evaluation header menu")
	public void user_opens_the_evaluation_header_menu() {

		boolean opened = applicationManagement_pom.openEvaluationHeaderMenu();

		softAssert.softAssertTrue(
				opened,
				"Opened the evaluation header menu successfully.",
				"Failed to open the evaluation header menu.");
	}

	@Then("User verifies {string} is displayed in the evaluation header menu")
	public void user_verifies_is_displayed_in_the_evaluation_header_menu(String optionName) {

		boolean displayed = applicationManagement_pom.verifyEvaluationHeaderMenuOptionDisplayed(optionName);

		softAssert.softAssertTrue(
				displayed,
				optionName + " is displayed in the evaluation header menu successfully.",
				optionName + " is not displayed in the evaluation header menu.");
	}

	@When("User clicks on {string} from the evaluation header menu")
	public void user_clicks_on_from_the_evaluation_header_menu(String optionName) {

		windowHandlesBeforeOpeningEditProgram = new HashSet<>(driver.getWindowHandles());
		boolean clicked = applicationManagement_pom.clickEvaluationHeaderMenuOption(optionName);

		softAssert.softAssertTrue(
				clicked,
				"Clicked on " + optionName + " from the evaluation header menu successfully.",
				"Failed to click on " + optionName + " from the evaluation header menu.");
	}

	@Then("User switches to the newly opened Edit Program tab")
	public void user_switches_to_the_newly_opened_edit_program_tab() {

		boolean switched = applicationManagement_pom.switchToNewEditProgramTab(windowHandlesBeforeOpeningEditProgram);

		softAssert.softAssertTrue(
				switched,
				"Switched to the newly opened Edit Program tab successfully.",
				"Failed to switch to the newly opened Edit Program tab.");
	}

	@Then("User verifies the Edit Program page and form are displayed")
	public void user_verifies_the_edit_program_page_and_form_are_displayed() {

		boolean displayed = applicationManagement_pom.verifyEditProgramPageAndFormDisplayed();

		softAssert.softAssertTrue(
				displayed,
				"Verified the Edit Program page and form are displayed successfully.",
				"Failed to verify the Edit Program page and form are displayed.");
	}

	@When("User opens the evaluation subsection menu")
	public void user_opens_the_evaluation_subsection_menu() {

		boolean opened = applicationManagement_pom.openEvaluationSubsectionMenu();

		softAssert.softAssertTrue(
				opened,
				"Opened the evaluation subsection menu successfully.",
				"Failed to open the evaluation subsection menu.");
	}

	@Then("User verifies {string} is displayed in the evaluation subsection menu")
	public void user_verifies_is_displayed_in_the_evaluation_subsection_menu(String optionName) {

		boolean displayed = applicationManagement_pom.verifyEvaluationSubsectionMenuOptionDisplayed(optionName);

		softAssert.softAssertTrue(
				displayed,
				optionName + " is displayed in the evaluation subsection menu successfully.",
				optionName + " is not displayed in the evaluation subsection menu.");
	}

	@When("User clicks on {string} from the evaluation subsection menu")
	public void user_clicks_on_from_the_evaluation_subsection_menu(String optionName) {

		if ("Preview Application".equals(optionName)) {
			windowHandlesBeforeOpeningPreviewApplication = new HashSet<>(driver.getWindowHandles());
		} else if ("Edit Application".equals(optionName)) {
			windowHandlesBeforeOpeningEditApplication = new HashSet<>(driver.getWindowHandles());
		}

		boolean clicked = applicationManagement_pom.clickEvaluationSubsectionMenuOption(optionName);

		softAssert.softAssertTrue(
				clicked,
				"Clicked on " + optionName + " from the evaluation subsection menu successfully.",
				"Failed to click on " + optionName + " from the evaluation subsection menu.");
	}

	@Then("User switches to the newly opened Preview Application tab")
	public void user_switches_to_the_newly_opened_preview_application_tab() {

		boolean switched = applicationManagement_pom.switchToNewPreviewApplicationTab(windowHandlesBeforeOpeningPreviewApplication);

		softAssert.softAssertTrue(
				switched,
				"Switched to the newly opened Preview Application tab successfully.",
				"Failed to switch to the newly opened Preview Application tab.");
	}

	@Then("User verifies the Preview Application page, form preview, and Back to Application button are displayed")
	public void user_verifies_the_preview_application_page_form_preview_and_back_to_application_button_are_displayed() {

		boolean displayed = applicationManagement_pom.verifyPreviewApplicationPageAndFormDisplayed();

		softAssert.softAssertTrue(
				displayed,
				"Verified the Preview Application page, form preview, and Back to Application button are displayed successfully.",
				"Failed to verify the Preview Application page, form preview, and Back to Application button are displayed.");
	}

	@Then("User switches to the newly opened Edit Application tab")
	public void user_switches_to_the_newly_opened_edit_application_tab() {

		boolean switched = applicationManagement_pom.switchToNewEditApplicationTab(windowHandlesBeforeOpeningEditApplication);

		softAssert.softAssertTrue(
				switched,
				"Switched to the newly opened Edit Application tab successfully.",
				"Failed to switch to the newly opened Edit Application tab.");
	}

	@Then("User verifies the Edit Application page, form, and Preview Application button are displayed")
	public void user_verifies_the_edit_application_page_form_and_preview_application_button_are_displayed() {

		boolean displayed = applicationManagement_pom.verifyEditApplicationPageAndFormDisplayed();

		softAssert.softAssertTrue(
				displayed,
				"Verified the Edit Application page, form, and Preview Application button are displayed successfully.",
				"Failed to verify the Edit Application page, form, and Preview Application button are displayed.");
	}

	@When("User clicks on the {string} button from evaluation page")
	public void user_clicks_on_the_button_from_evaluation_page(String buttonName) {

		boolean clicked = false;

		if ("Back to Dashboard".equals(buttonName)) {
			clicked = applicationManagement_pom.clickBackToDashboardFromEvaluationPage();
		} else if ("Invite".equals(buttonName)) {
			clicked = applicationManagement_pom.clickInviteFromEvaluationPage();
		}

		softAssert.softAssertTrue(
				clicked,
				"Clicked on the " + buttonName + " button from evaluation page successfully.",
				"Failed to click on the " + buttonName + " button from evaluation page.");
	}

	@Then("User verifies the Invite modal is displayed from evaluation page")
	public void user_verifies_the_invite_modal_is_displayed_from_evaluation_page() {

		boolean displayed = applicationManagement_pom.verifyInviteModalFromEvaluationPage();

		softAssert.softAssertTrue(
				displayed,
				"Invite modal is displayed correctly from evaluation page.",
				"Invite modal is not displayed correctly from evaluation page.");
	}

	@Then("User adds a random invite email and verifies it appears in the invite list from evaluation page")
	public void user_adds_a_random_invite_email_and_verifies_it_appears_in_the_invite_list_from_evaluation_page() {

		String email = applicationCreation_pom.addRandomInviteEmailAndVerifyListed();

		softAssert.softAssertTrue(
				email != null,
				"Added a random invite email and verified it appears in the invite list from evaluation page successfully: "
						+ email,
				"Failed to add a random invite email or verify it appears in the invite list from evaluation page.");
	}

	@Then("User deletes the added invite email and verifies it is removed from the invite list from evaluation page")
	public void user_deletes_the_added_invite_email_and_verifies_it_is_removed_from_the_invite_list_from_evaluation_page() {

		boolean deleted = applicationCreation_pom.deleteLastInvitedEmailAndVerifyRemoved();

		softAssert.softAssertTrue(
				deleted,
				"Deleted the added invite email and verified it is removed from the invite list from evaluation page successfully.",
				"Failed to delete the added invite email or verify it is removed from the invite list from evaluation page.");
	}

	@Then("User closes the Invite modal using Cancel from evaluation page")
	public void user_closes_the_invite_modal_using_cancel_from_evaluation_page() {

		boolean closed = applicationManagement_pom.closeInviteModalUsingCancelFromEvaluationPage();

		softAssert.softAssertTrue(
				closed,
				"Invite modal was closed successfully using Cancel from evaluation page.",
				"Failed to close the Invite modal using Cancel from evaluation page.");
	}

	@When("User sends the invite from evaluation page and verifies the Invite modal closes")
	public void user_sends_the_invite_from_evaluation_page_and_verifies_the_invite_modal_closes() {

		boolean sent = applicationManagement_pom.sendInviteFromEvaluationPageAndVerifyModalCloses();

		softAssert.softAssertTrue(
				sent,
				"Invite was sent successfully from evaluation page and the Invite modal closed.",
				"Failed to send the invite from evaluation page or the Invite modal did not close.");
	}
	
	//*****************************************************************************************************************

	@Then("User opens the export menu from the evaluation page")
	public void user_opens_the_export_menu_from_the_evaluation_page() {

		boolean opened = applicationManagement_pom.openExportMenu();

		softAssert.softAssertTrue(
				opened,
				"Export menu opened successfully on the evaluation page.",
				"Failed to open export menu on the evaluation page.");
	}
	
	//**********************************************************************************************************************

	@Then("User exports the applications for the configured target program")
	public void user_exports_the_applications_for_the_configured_target_program() {

		user_exports_the_applications_for_the_target_program_from_config_key("programManagerTargetProgramName");
	}

	@Then("User exports the applications for the target program from config key {string}")
	public void user_exports_the_applications_for_the_target_program_from_config_key(String configKey) {

		String targetProgramName = getProgramNameFromConfigKey(configKey);
		boolean exported = applicationManagement_pom.clickExportAndWaitForDownload(targetProgramName);

		softAssert.softAssertTrue(
				exported,
				"Applications were exported successfully for target program: " + targetProgramName,
				"Failed to export applications for target program: " + targetProgramName);
	}
	


	@Then("Downloaded applications zip should contain Applicant_Summaries.csv and Score_Details.csv")
	public void downloaded_applications_zip_should_contain_applicant_summaries_csv_and_score_details_csv() {

		boolean validZip = applicationManagement_pom.downloadedZipContainsExpectedFiles();

		softAssert.softAssertTrue(
				validZip,
				"Downloaded applications zip contains Applicant_Summaries.csv and Score_Details.csv.",
				"Downloaded applications zip does not contain the expected csv files.");
	}

	@Then("User records the target program total applications count")
	public void user_records_the_target_program_total_applications_count() {

		totalApplicationsCount = applicationManagement_pom.getTotalApplicationsCount();

		softAssert.softAssertTrue(
				totalApplicationsCount >= 0,
				"Captured the target program total applications count successfully: " + totalApplicationsCount,
				"Failed to capture the target program total applications count.");
	}

	@Then("User records the current page applications count from the All filter")
	public void user_records_the_current_page_applications_count_from_the_all_filter() {

		currentPageApplicationsCount = applicationManagement_pom.getCurrentPageApplicationsCountFromAllFilter();

		softAssert.softAssertTrue(
				currentPageApplicationsCount >= 0,
				"Captured the current page applications count from the All filter successfully: " + currentPageApplicationsCount,
				"Failed to capture the current page applications count from the All filter.");
	}

	@Then("User records the application rows displayed on the current page")
	public void user_records_the_application_rows_displayed_on_the_current_page() {

		displayedApplicationRowCount = applicationManagement_pom.getDisplayedApplicationRowCount();

		softAssert.softAssertTrue(
				displayedApplicationRowCount >= 0,
				"Captured the application rows displayed on the current page successfully: " + displayedApplicationRowCount,
				"Failed to capture the application rows displayed on the current page.");
	}

	@Then("User verifies no more than 10 application rows are displayed on the current page")
	public void user_verifies_no_more_than_10_application_rows_are_displayed_on_the_current_page() {

		boolean validCurrentPageRowCount = applicationManagement_pom.verifyCurrentPageHasNoMoreThanTenRows();

		softAssert.softAssertTrue(
				validCurrentPageRowCount,
				"Verified successfully that no more than 10 application rows are displayed on the current page.",
				"More than 10 application rows are displayed on the current page.");
	}

	@Then("User verifies evaluation pagination is displayed for the target program applications")
	public void user_verifies_evaluation_pagination_is_displayed_for_the_target_program_applications() {

		boolean paginationVisible = applicationManagement_pom.isEvaluationPaginationVisible();

		softAssert.softAssertTrue(
				paginationVisible,
				"Verified the evaluation pagination is displayed for the target program applications successfully.",
				"Evaluation pagination is not displayed for the target program applications.");
	}

	@When("User opens evaluation page {string}")
	public void user_opens_evaluation_page(String targetPage) {

		boolean opened = applicationManagement_pom.openEvaluationPage(targetPage);

		softAssert.softAssertTrue(
				opened,
				"Opened evaluation page " + targetPage + " successfully.",
				"Failed to open evaluation page " + targetPage + ".");
	}

	@Then("User verifies the current evaluation page is {string}")
	public void user_verifies_the_current_evaluation_page_is(String expectedPage) {

		String actualPage = applicationManagement_pom.getCurrentEvaluationPage();
		boolean pageMatches = expectedPage.equals(actualPage);

		softAssert.softAssertTrue(
				pageMatches,
				"Verified the current evaluation page successfully. Expected=" + expectedPage + ", Actual=" + actualPage,
				"Current evaluation page is incorrect. Expected=" + expectedPage + ", Actual=" + actualPage);
	}

	@Then("User verifies application rows are displayed on the current evaluation page")
	public void user_verifies_application_rows_are_displayed_on_the_current_evaluation_page() {

		boolean rowsDisplayed = displayedApplicationRowCount > 0;

		softAssert.softAssertTrue(
				rowsDisplayed,
				"Verified application rows are displayed on the current evaluation page successfully. Displayed rows="
						+ displayedApplicationRowCount,
				"No application rows are displayed on the current evaluation page.");
	}

	@Then("User verifies the current page All filter count matches the displayed application rows")
	public void user_verifies_the_current_page_all_filter_count_matches_the_displayed_application_rows() {

		boolean countsMatch = currentPageApplicationsCount == displayedApplicationRowCount;

		softAssert.softAssertTrue(
				countsMatch,
				"Verified the current page All filter count matches the displayed application rows successfully. All filter count="
						+ currentPageApplicationsCount + ", Displayed rows=" + displayedApplicationRowCount,
				"Current page All filter count does not match the displayed application rows. All filter count="
						+ currentPageApplicationsCount + ", Displayed rows=" + displayedApplicationRowCount);
	}

	@Then("User records the last evaluation page number")
	public void user_records_the_last_evaluation_page_number() {

		lastEvaluationPageNumber = applicationManagement_pom.getLastEvaluationPageNumber();

		softAssert.softAssertTrue(
				lastEvaluationPageNumber != null && !lastEvaluationPageNumber.isBlank(),
				"Captured the last evaluation page number successfully: " + lastEvaluationPageNumber,
				"Failed to capture the last evaluation page number.");
	}

	@When("User opens the last evaluation page")
	public void user_opens_the_last_evaluation_page() {

		boolean opened = applicationManagement_pom.openEvaluationPage(lastEvaluationPageNumber);

		softAssert.softAssertTrue(
				opened,
				"Opened the last evaluation page successfully: " + lastEvaluationPageNumber,
				"Failed to open the last evaluation page: " + lastEvaluationPageNumber);
	}

	@Then("User verifies the current evaluation page is the last evaluation page")
	public void user_verifies_the_current_evaluation_page_is_the_last_evaluation_page() {

		String actualPage = applicationManagement_pom.getCurrentEvaluationPage();
		boolean pageMatches = lastEvaluationPageNumber != null && lastEvaluationPageNumber.equals(actualPage);

		softAssert.softAssertTrue(
				pageMatches,
				"Verified the current evaluation page is the last evaluation page successfully. Expected="
						+ lastEvaluationPageNumber + ", Actual=" + actualPage,
				"Current evaluation page is not the last evaluation page. Expected="
						+ lastEvaluationPageNumber + ", Actual=" + actualPage);
	}

	@Then("User verifies the last evaluation page displays the expected remaining application rows")
	public void user_verifies_the_last_evaluation_page_displays_the_expected_remaining_application_rows() {

		int expectedLastPageRowCount = applicationManagement_pom.getExpectedLastEvaluationPageRowCount(totalApplicationsCount);
		boolean validLastPageRowCount = displayedApplicationRowCount == expectedLastPageRowCount;

		softAssert.softAssertTrue(
				validLastPageRowCount,
				"Verified the last evaluation page displays the expected remaining application rows successfully. Expected="
						+ expectedLastPageRowCount + ", Actual=" + displayedApplicationRowCount,
				"Last evaluation page row count is incorrect. Expected="
						+ expectedLastPageRowCount + ", Actual=" + displayedApplicationRowCount);
	}

	@Then("User verifies pagination behavior for the target program applications")
	public void user_verifies_pagination_behavior_for_the_target_program_applications() {

		boolean validPaginationBehavior = applicationManagement_pom.verifyEvaluationPaginationBehavior(totalApplicationsCount);

		softAssert.softAssertTrue(
				validPaginationBehavior,
				"Verified pagination behavior successfully for the target program applications.",
				"Pagination behavior is incorrect for the target program applications.");
	}

	@Then("User locates the configured status target application and records its current page status details")
	public void user_locates_the_configured_status_target_application_and_records_its_current_page_status_details() {

		statusTargetApplicationName = ConfigurationReader.getProperty("programManagerStatusTargetApplicationName");
		logger.info("[ApplicationManagement_stepD] Recording baseline status details for target application='"
				+ statusTargetApplicationName + "'");

		boolean located = applicationManagement_pom.navigateToPageContainingApplication(statusTargetApplicationName);
		softAssert.softAssertTrue(
				located,
				"Located the configured status target application successfully: " + statusTargetApplicationName,
				"Failed to locate the configured status target application: " + statusTargetApplicationName);

		statusTargetApplicationCurrentPage = applicationManagement_pom.getCurrentEvaluationPage();
		statusTargetApplicationOriginalStatus = applicationManagement_pom
				.getApplicationStatusOnCurrentPage(statusTargetApplicationName);
		baselineStatusFilterCounts = applicationManagement_pom.getCurrentPageStatusFilterCounts();

		if (!"Needs Review".equals(statusTargetApplicationOriginalStatus)) {
			Map<String, Integer> expectedNormalizedCounts = applicationManagement_pom.buildExpectedCountsAfterSingleStatusChange(
					baselineStatusFilterCounts,
					statusTargetApplicationOriginalStatus,
					"Needs Review");

			boolean normalized = applicationManagement_pom.changeApplicationStatusOnCurrentPage(
					statusTargetApplicationName,
					"Needs Review");
			softAssert.softAssertTrue(
					normalized,
					"Normalized the configured status target application back to Needs Review before starting the scenario.",
					"Failed to normalize the configured status target application back to Needs Review before starting the scenario.");

			boolean normalizedStateVerified = applicationManagement_pom.waitForApplicationStatusAndFilterCounts(
					statusTargetApplicationName,
					statusTargetApplicationCurrentPage,
					"Needs Review",
					expectedNormalizedCounts);
			softAssert.softAssertTrue(
					normalizedStateVerified,
					"Verified the configured status target application and current page filter counts were normalized to Needs Review.",
					"Failed to verify the configured status target application and current page filter counts were normalized to Needs Review.");

			statusTargetApplicationOriginalStatus = "Needs Review";
			baselineStatusFilterCounts = applicationManagement_pom.getCurrentPageStatusFilterCounts();
		}

		softAssert.softAssertTrue(
				!baselineStatusFilterCounts.isEmpty(),
				"Captured the current page status filter counts successfully for the target application page.",
				"Failed to capture the current page status filter counts for the target application page.");
	}

	@Then("User changes the configured status target application status to {string}")
	public void user_changes_the_configured_status_target_application_status_to(String newStatus) {

		boolean updated = applicationManagement_pom.changeApplicationStatusOnCurrentPage(
				statusTargetApplicationName,
				newStatus);

		softAssert.softAssertTrue(
				updated,
				"Changed the configured status target application to " + newStatus + " successfully.",
				"Failed to change the configured status target application to " + newStatus + ".");
	}

	@Then("User verifies the configured status target application status and current page filter counts reflect the {string} change")
	public void user_verifies_the_configured_status_target_application_status_and_current_page_filter_counts_reflect_the_change(
			String expectedStatus) {

		Map<String, Integer> expectedCounts = applicationManagement_pom.buildExpectedCountsAfterSingleStatusChange(
				baselineStatusFilterCounts,
				statusTargetApplicationOriginalStatus,
				expectedStatus);

		boolean updated = applicationManagement_pom.waitForApplicationStatusAndFilterCounts(
				statusTargetApplicationName,
				statusTargetApplicationCurrentPage,
				expectedStatus,
				expectedCounts);

		softAssert.softAssertTrue(
				updated,
				"Configured status target application row status and current page filter counts reflect the "
						+ expectedStatus + " change.",
				"Configured status target application row status and current page filter counts do not reflect the "
						+ expectedStatus + " change.");
	}

	@Then("User restores the configured status target application status to {string} and verifies the current page filter counts are restored")
	public void user_restores_the_configured_status_target_application_status_to_and_verifies_the_current_page_filter_counts_are_restored(
			String restoredStatus) {

		boolean restored = applicationManagement_pom.changeApplicationStatusOnCurrentPage(
				statusTargetApplicationName,
				restoredStatus);

		softAssert.softAssertTrue(
				restored,
				"Restored the configured status target application status to " + restoredStatus + " successfully.",
				"Failed to restore the configured status target application status to " + restoredStatus + ".");

		boolean restoredStateVerified = applicationManagement_pom.waitForApplicationStatusAndFilterCounts(
				statusTargetApplicationName,
				statusTargetApplicationCurrentPage,
				restoredStatus,
				baselineStatusFilterCounts);

		softAssert.softAssertTrue(
				restoredStateVerified,
				"Restored status target application row status and current page filter counts successfully.",
				"Failed to restore status target application row status and current page filter counts.");
	}

	@Then("User opens page 1 of the evaluation results")
	public void user_opens_page_1_of_the_evaluation_results() {

		acceptedNoThanksApplicationName = null;
		acceptedYesSendEmailApplicationName = null;
		acceptedApplicationNames.clear();
		rejectedNoThanksApplicationName = null;
		rejectedYesSendEmailApplicationName = null;
		rejectedApplicationNames.clear();
		bulkPageOneApplicationNames.clear();
		bulkRandomEmailApplicationNames.clear();
		rowEmailApplicationName = null;

		boolean opened = applicationManagement_pom.openEvaluationPage("1");

		softAssert.softAssertTrue(
				opened,
				"Opened page 1 of the evaluation results successfully.",
				"Failed to open page 1 of the evaluation results.");
	}

	@Then("User verifies all 10 applications on page 1 are in {string} status")
	public void user_verifies_all_10_applications_on_page_1_are_in_status(String expectedStatus) {

		Map<String, String> statuses = applicationManagement_pom.getCurrentPageApplicationStatuses();

		softAssert.softAssertTrue(
				statuses.size() == 10,
				"Captured all 10 application rows from page 1 successfully.",
				"Page 1 does not contain exactly 10 application rows. Actual row count: " + statuses.size());

		boolean allMatch = statuses.values().stream().allMatch(expectedStatus::equals);
		if (allMatch) {
			softAssert.softAssertTrue(
					true,
					"All 10 applications on page 1 are already in " + expectedStatus + " status.",
					"All 10 applications on page 1 are already in " + expectedStatus + " status.");
			return;
		}

		List<String> applicationsToNormalize = statuses.entrySet().stream()
				.filter(entry -> !expectedStatus.equals(entry.getValue()))
				.map(Map.Entry::getKey)
				.toList();

		boolean normalized = applicationManagement_pom.changeApplicationStatusesOnCurrentPage(
				applicationsToNormalize,
				expectedStatus);

		softAssert.softAssertTrue(
				normalized,
				"Normalized page 1 application statuses to " + expectedStatus + " for: " + applicationsToNormalize,
				"Failed to normalize page 1 application statuses to " + expectedStatus + " for: "
						+ applicationsToNormalize);

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", 10);
		expectedCounts.put("On Hold", 0);
		expectedCounts.put("Accepted", 0);
		expectedCounts.put("Rejected", 0);

		boolean normalizedStateVerified = applicationManagement_pom.waitForCurrentPageStatusDistribution(
				"1",
				expectedCounts,
				new ArrayList<>(statuses.keySet()),
				expectedStatus,
				expectedStatus);

		softAssert.softAssertTrue(
				normalizedStateVerified,
				"Page 1 statuses and filter counts were normalized successfully to " + expectedStatus + ".",
				"Page 1 statuses and filter counts were not normalized successfully to " + expectedStatus + ".");
	}

	@Then("User changes a random 2 to 5 applications on page 1 to {string}")
	public void user_changes_a_random_2_to_5_applications_on_page_1_to(String newStatus) {

		randomOnHoldApplicationNames = applicationManagement_pom.selectRandomApplicationNamesFromCurrentPage(2, 5);

		softAssert.softAssertTrue(
				!randomOnHoldApplicationNames.isEmpty(),
				"Selected random page 1 applications successfully for status update: " + randomOnHoldApplicationNames,
				"Failed to select random page 1 applications for status update.");

		boolean updated = applicationManagement_pom.changeApplicationStatusesOnCurrentPage(
				randomOnHoldApplicationNames,
				newStatus);

		softAssert.softAssertTrue(
				updated,
				"Changed the randomly selected page 1 applications to " + newStatus + " successfully.",
				"Failed to change the randomly selected page 1 applications to " + newStatus + ".");
	}

	@Then("User verifies page 1 application statuses and filter counts reflect the random {string} updates")
	public void user_verifies_page_1_application_statuses_and_filter_counts_reflect_the_random_updates(
			String expectedStatus) {

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", 10 - randomOnHoldApplicationNames.size());
		expectedCounts.put("On Hold", randomOnHoldApplicationNames.size());
		expectedCounts.put("Accepted", 0);
		expectedCounts.put("Rejected", 0);

		boolean updated = applicationManagement_pom.waitForCurrentPageStatusDistribution(
				"1",
				expectedCounts,
				randomOnHoldApplicationNames,
				expectedStatus,
				"Needs Review");

		softAssert.softAssertTrue(
				updated,
				"Page 1 application statuses and filter counts reflect the random " + expectedStatus + " updates.",
				"Page 1 application statuses and filter counts do not reflect the random " + expectedStatus + " updates.");
	}

	@Then("User restores all randomly changed page 1 applications to {string} and verifies page 1 filter counts are restored")
	public void user_restores_all_randomly_changed_page_1_applications_to_and_verifies_page_1_filter_counts_are_restored(
			String restoredStatus) {

		boolean restored = applicationManagement_pom.changeApplicationStatusesOnCurrentPage(
				randomOnHoldApplicationNames,
				restoredStatus);

		softAssert.softAssertTrue(
				restored,
				"Restored all randomly changed page 1 applications to " + restoredStatus + " successfully.",
				"Failed to restore all randomly changed page 1 applications to " + restoredStatus + ".");

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", 10);
		expectedCounts.put("On Hold", 0);
		expectedCounts.put("Accepted", 0);
		expectedCounts.put("Rejected", 0);

		boolean restoredStateVerified = applicationManagement_pom.waitForCurrentPageStatusDistribution(
				"1",
				expectedCounts,
				randomOnHoldApplicationNames,
				restoredStatus,
				restoredStatus);

		softAssert.softAssertTrue(
				restoredStateVerified,
				"Restored all randomly changed page 1 application statuses and filter counts successfully.",
				"Failed to restore all randomly changed page 1 application statuses and filter counts.");
	}

	@Then("User verifies the acceptance send email confirmation modal is displayed")
	public void user_verifies_the_acceptance_send_email_confirmation_modal_is_displayed() {

		boolean modalDisplayed = applicationManagement_pom.verifyStatusConfirmationModal(
				"Send Email",
				"You have accepted this startup into your program. Would you like to notify them with an email?");

		softAssert.softAssertTrue(
				modalDisplayed,
				"Acceptance send email confirmation modal is displayed correctly.",
				"Acceptance send email confirmation modal is not displayed correctly.");
	}

	@Then("User chooses {string} from the status confirmation modal")
	public void user_chooses_from_the_status_confirmation_modal(String buttonLabel) {

		boolean chosen = applicationManagement_pom.chooseStatusConfirmationModalAction(buttonLabel);

		softAssert.softAssertTrue(
				chosen,
				"Chose \"" + buttonLabel + "\" from the status confirmation modal successfully.",
				"Failed to choose \"" + buttonLabel + "\" from the status confirmation modal.");
	}

	@Then("User selects a random page 1 application in {string} status for the acceptance flow with {string}")
	public void user_selects_a_random_page_1_application_in_status_for_the_acceptance_flow_with(
			String requiredStatus,
			String flowType) {

		acceptedNoThanksApplicationName = selectRandomApplicationByStatus(
				requiredStatus,
				acceptedApplicationNames,
				acceptedApplicationNames);
		softAssert.softAssertTrue(
				acceptedNoThanksApplicationName != null,
				"Selected a random page 1 application for the acceptance flow with " + flowType + ": "
						+ acceptedNoThanksApplicationName,
				"Failed to select a random page 1 application in " + requiredStatus + " status for the acceptance flow with "
						+ flowType + ".");
	}

	@Then("User changes the selected page 1 acceptance application status to {string}")
	public void user_changes_the_selected_page_1_acceptance_application_status_to(String newStatus) {

		String applicationNameToUpdate = acceptedYesSendEmailApplicationName != null
				? acceptedYesSendEmailApplicationName
				: acceptedNoThanksApplicationName;

		boolean updated = applicationManagement_pom.changeApplicationStatusOnCurrentPage(
				applicationNameToUpdate,
				newStatus);

		softAssert.softAssertTrue(
				updated,
				"Changed the selected page 1 acceptance application to " + newStatus + " successfully.",
				"Failed to change the selected page 1 acceptance application to " + newStatus + ".");
	}

	@Then("^User verifies page 1 statuses and filter counts reflect (\\d+) applications? changed to \"([^\"]*)\"$")
	public void user_verifies_page_1_statuses_and_filter_counts_reflect_application_changed_to(
			Integer acceptedCount,
			String expectedStatus) {

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", 10 - acceptedCount);
		expectedCounts.put("On Hold", 0);
		expectedCounts.put("Accepted", acceptedCount);
		expectedCounts.put("Rejected", 0);

		boolean updated = applicationManagement_pom.waitForCurrentPageStatusDistribution(
				"1",
				expectedCounts,
				new ArrayList<>(acceptedApplicationNames),
				expectedStatus,
				"Needs Review");

		softAssert.softAssertTrue(
				updated,
				"Page 1 statuses and filter counts reflect " + acceptedCount + " application changed to "
						+ expectedStatus + ".",
				"Page 1 statuses and filter counts do not reflect " + acceptedCount + " application changed to "
						+ expectedStatus + ".");
	}

	@Then("User selects another random page 1 application in {string} status for the acceptance flow with {string}")
	public void user_selects_another_random_page_1_application_in_status_for_the_acceptance_flow_with(
			String requiredStatus,
			String flowType) {

		acceptedYesSendEmailApplicationName = selectRandomApplicationByStatus(
				requiredStatus,
				acceptedApplicationNames,
				acceptedApplicationNames);
		softAssert.softAssertTrue(
				acceptedYesSendEmailApplicationName != null,
				"Selected another random page 1 application for the acceptance flow with " + flowType + ": "
						+ acceptedYesSendEmailApplicationName,
				"Failed to select another random page 1 application in " + requiredStatus
						+ " status for the acceptance flow with " + flowType + ".");
	}

	@Then("User verifies the acceptance email composer modal is displayed")
	public void user_verifies_the_acceptance_email_composer_modal_is_displayed() {

		boolean displayed = applicationManagement_pom.verifyAcceptanceEmailComposerModal();

		softAssert.softAssertTrue(
				displayed,
				"Acceptance email composer modal is displayed correctly.",
				"Acceptance email composer modal is not displayed correctly.");
	}

	@Then("User sends the acceptance email and verifies the email sent confirmation")
	public void user_sends_the_acceptance_email_and_verifies_the_email_sent_confirmation() {

		boolean sent = applicationManagement_pom.sendAcceptanceEmailAndVerifyConfirmation();

		softAssert.softAssertTrue(
				sent,
				"Acceptance email was sent and the email sent confirmation was displayed successfully.",
				"Acceptance email was not sent successfully or the email sent confirmation was not displayed.");
	}

	@Then("User closes the email sent confirmation modal")
	public void user_closes_the_email_sent_confirmation_modal() {

		boolean closed = applicationManagement_pom.closeEmailSentConfirmationModal();

		softAssert.softAssertTrue(
				closed,
				"Email sent confirmation modal was closed successfully.",
				"Failed to close the email sent confirmation modal.");
	}

	@Then("User restores all accepted page 1 applications to {string} and verifies page 1 filter counts are restored")
	public void user_restores_all_accepted_page_1_applications_to_and_verifies_page_1_filter_counts_are_restored(
			String restoredStatus) {

		boolean restored = applicationManagement_pom.changeApplicationStatusesOnCurrentPage(
				acceptedApplicationNames,
				restoredStatus);

		softAssert.softAssertTrue(
				restored,
				"Restored all accepted page 1 applications to " + restoredStatus + " successfully.",
				"Failed to restore all accepted page 1 applications to " + restoredStatus + ".");

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", 10);
		expectedCounts.put("On Hold", 0);
		expectedCounts.put("Accepted", 0);
		expectedCounts.put("Rejected", 0);

		boolean restoredStateVerified = applicationManagement_pom.waitForCurrentPageStatusDistribution(
				"1",
				expectedCounts,
				new ArrayList<>(acceptedApplicationNames),
				restoredStatus,
				restoredStatus);

		softAssert.softAssertTrue(
				restoredStateVerified,
				"Restored all accepted page 1 applications and page 1 filter counts successfully.",
				"Failed to restore all accepted page 1 applications and page 1 filter counts.");
	}

	@Then("User selects a random page 1 application in {string} status for the rejection flow with {string}")
	public void user_selects_a_random_page_1_application_in_status_for_the_rejection_flow_with(
			String requiredStatus,
			String flowType) {

		rejectedNoThanksApplicationName = selectRandomApplicationByStatus(
				requiredStatus,
				rejectedApplicationNames,
				rejectedApplicationNames);
		softAssert.softAssertTrue(
				rejectedNoThanksApplicationName != null,
				"Selected a random page 1 application for the rejection flow with " + flowType + ": "
						+ rejectedNoThanksApplicationName,
				"Failed to select a random page 1 application in " + requiredStatus + " status for the rejection flow with "
						+ flowType + ".");
	}

	@Then("User changes the selected page 1 rejection application status to {string}")
	public void user_changes_the_selected_page_1_rejection_application_status_to(String newStatus) {

		String applicationNameToUpdate = rejectedYesSendEmailApplicationName != null
				? rejectedYesSendEmailApplicationName
				: rejectedNoThanksApplicationName;

		boolean updated = applicationManagement_pom.changeApplicationStatusOnCurrentPage(
				applicationNameToUpdate,
				newStatus);

		softAssert.softAssertTrue(
				updated,
				"Changed the selected page 1 rejection application to " + newStatus + " successfully.",
				"Failed to change the selected page 1 rejection application to " + newStatus + ".");
	}

	@Then("User verifies the rejection send email confirmation modal is displayed")
	public void user_verifies_the_rejection_send_email_confirmation_modal_is_displayed() {

		boolean modalDisplayed = applicationManagement_pom.verifyStatusConfirmationModal(
				"Send Email",
				"You have decided not to move forward with this team. Would you like to notify them with an email?");

		softAssert.softAssertTrue(
				modalDisplayed,
				"Rejection send email confirmation modal is displayed correctly.",
				"Rejection send email confirmation modal is not displayed correctly.");
	}

	@Then("User verifies the bulk acceptance send email confirmation modal is displayed")
	public void user_verifies_the_bulk_acceptance_send_email_confirmation_modal_is_displayed() {

		boolean modalDisplayed = applicationManagement_pom.verifyStatusConfirmationModal(
				"Send Email",
				"You have accepted multiple startups into your program. Would you like to notify them with an email?");

		softAssert.softAssertTrue(
				modalDisplayed,
				"Bulk acceptance send email confirmation modal is displayed correctly.",
				"Bulk acceptance send email confirmation modal is not displayed correctly.");
	}

	@Then("User verifies the bulk rejection send email confirmation modal is displayed")
	public void user_verifies_the_bulk_rejection_send_email_confirmation_modal_is_displayed() {

		boolean modalDisplayed = applicationManagement_pom.verifyStatusConfirmationModal(
				"Send Email",
				"You've decided not to move forward with the selected teams. Would you like to notify them with an email?");

		softAssert.softAssertTrue(
				modalDisplayed,
				"Bulk rejection send email confirmation modal is displayed correctly.",
				"Bulk rejection send email confirmation modal is not displayed correctly.");
	}

	@Then("User verifies the Bulk Action dropdown is disabled when no applications are selected")
	public void user_verifies_the_bulk_action_dropdown_is_disabled_when_no_applications_are_selected() {

		boolean disabled = applicationManagement_pom.isBulkActionDropdownEnabled() == false;

		softAssert.softAssertTrue(
				disabled,
				"Bulk Action dropdown is disabled when no applications are selected.",
				"Bulk Action dropdown is not disabled when no applications are selected.");
	}

	@Then("User verifies the Bulk Action dropdown is enabled")
	public void user_verifies_the_bulk_action_dropdown_is_enabled() {

		boolean enabled = applicationManagement_pom.isBulkActionDropdownEnabled();

		softAssert.softAssertTrue(
				enabled,
				"Bulk Action dropdown is enabled after selecting applications.",
				"Bulk Action dropdown is not enabled after selecting applications.");
	}

	@Then("User selects all 10 page 1 applications using the Select All checkbox")
	public void user_selects_all_10_page_1_applications_using_the_select_all_checkbox() {

		bulkPageOneApplicationNames = new ArrayList<>(applicationManagement_pom.getCurrentPageApplicationStatuses().keySet());
		boolean selected = applicationManagement_pom.selectAllCurrentPageApplicationsUsingHeaderCheckbox();

		softAssert.softAssertTrue(
				selected,
				"Selected all 10 page 1 applications using the Select All checkbox successfully.",
				"Failed to select all 10 page 1 applications using the Select All checkbox.");
	}

	@When("User clears all 10 page 1 applications using the Select All checkbox")
	public void user_clears_all_10_page_1_applications_using_the_select_all_checkbox() {

		boolean cleared = applicationManagement_pom.clearAllCurrentPageApplicationsUsingHeaderCheckbox();

		softAssert.softAssertTrue(
				cleared,
				"Cleared all 10 page 1 applications using the Select All checkbox successfully.",
				"Failed to clear all 10 page 1 applications using the Select All checkbox.");
	}

	@When("User selects a random page 1 application name from the evaluation table")
	public void user_selects_a_random_page_1_application_name_from_the_evaluation_table() {

		selectedEvaluationApplicationName = applicationManagement_pom.selectRandomApplicationNameFromCurrentPage();

		softAssert.softAssertTrue(
				selectedEvaluationApplicationName != null && !selectedEvaluationApplicationName.isBlank(),
				"Selected a random page 1 application name from the evaluation table successfully: "
						+ selectedEvaluationApplicationName,
				"Failed to select a random page 1 application name from the evaluation table.");
	}

	@When("User clicks on the selected page 1 application name from the evaluation table")
	public void user_clicks_on_the_selected_page_1_application_name_from_the_evaluation_table() {

		boolean clicked = applicationManagement_pom.openApplicationFromStartupNameOnCurrentPage(
				selectedEvaluationApplicationName);

		softAssert.softAssertTrue(
				clicked,
				"Clicked on the selected page 1 application name from the evaluation table successfully.",
				"Failed to click on the selected page 1 application name from the evaluation table.");
	}

	@Then("User verifies the selected application details page is displayed")
	public void user_verifies_the_selected_application_details_page_is_displayed() {

		boolean displayed = applicationManagement_pom.verifySelectedApplicationDetailsPageDisplayed(
				selectedEvaluationApplicationName);

		softAssert.softAssertTrue(
				displayed,
				"Verified the selected application details page is displayed successfully.",
				"Failed to verify the selected application details page is displayed.");
	}

	@Then("User applies the bulk action {string}")
	public void user_applies_the_bulk_action(String actionLabel) {

		boolean applied = applicationManagement_pom.applyBulkAction(actionLabel);

		softAssert.softAssertTrue(
				applied,
				"Applied the bulk action " + actionLabel + " successfully.",
				"Failed to apply the bulk action " + actionLabel + ".");
	}

	@Then("User verifies page 1 application statuses and filter counts reflect all applications changed to {string}")
	public void user_verifies_page_1_application_statuses_and_filter_counts_reflect_all_applications_changed_to(
			String expectedStatus) {

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", "Needs Review".equals(expectedStatus) ? 10 : 0);
		expectedCounts.put("On Hold", "On Hold".equals(expectedStatus) ? 10 : 0);
		expectedCounts.put("Accepted", "Accepted".equals(expectedStatus) ? 10 : 0);
		expectedCounts.put("Rejected", "Rejected".equals(expectedStatus) ? 10 : 0);

		boolean updated = applicationManagement_pom.waitForCurrentPageStatusDistribution(
				"1",
				expectedCounts,
				new ArrayList<>(bulkPageOneApplicationNames),
				expectedStatus,
				expectedStatus);

		softAssert.softAssertTrue(
				updated,
				"Page 1 application statuses and filter counts reflect all applications changed to " + expectedStatus + ".",
				"Page 1 application statuses and filter counts do not reflect all applications changed to " + expectedStatus + ".");
	}

	@Then("User individually reselects all 10 page 1 applications in random order")
	public void user_individually_reselects_all_10_page_1_applications_in_random_order() {

		boolean selected = applicationManagement_pom.selectApplicationsIndividuallyInRandomOrder(bulkPageOneApplicationNames);

		softAssert.softAssertTrue(
				selected,
				"Individually reselected all 10 page 1 applications in random order successfully.",
				"Failed to individually reselect all 10 page 1 applications in random order.");
	}

	@Then("^User verifies page 1 rejection statuses and filter counts reflect (\\d+) applications? changed to \"([^\"]*)\"$")
	public void user_verifies_page_1_rejection_statuses_and_filter_counts_reflect_application_changed_to(
			Integer rejectedCount,
			String expectedStatus) {

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", 10 - rejectedCount);
		expectedCounts.put("On Hold", 0);
		expectedCounts.put("Accepted", 0);
		expectedCounts.put("Rejected", rejectedCount);

		boolean updated = applicationManagement_pom.waitForCurrentPageStatusDistribution(
				"1",
				expectedCounts,
				new ArrayList<>(rejectedApplicationNames),
				expectedStatus,
				"Needs Review");

		softAssert.softAssertTrue(
				updated,
				"Page 1 rejection statuses and filter counts reflect " + rejectedCount + " application changed to "
						+ expectedStatus + ".",
				"Page 1 rejection statuses and filter counts do not reflect " + rejectedCount + " application changed to "
						+ expectedStatus + ".");
	}

	@Then("User selects another random page 1 application in {string} status for the rejection flow with {string}")
	public void user_selects_another_random_page_1_application_in_status_for_the_rejection_flow_with(
			String requiredStatus,
			String flowType) {

		rejectedYesSendEmailApplicationName = selectRandomApplicationByStatus(
				requiredStatus,
				rejectedApplicationNames,
				rejectedApplicationNames);
		softAssert.softAssertTrue(
				rejectedYesSendEmailApplicationName != null,
				"Selected another random page 1 application for the rejection flow with " + flowType + ": "
						+ rejectedYesSendEmailApplicationName,
				"Failed to select another random page 1 application in " + requiredStatus
						+ " status for the rejection flow with " + flowType + ".");
	}

	@Then("User verifies the rejection email composer modal is displayed")
	public void user_verifies_the_rejection_email_composer_modal_is_displayed() {

		boolean displayed = applicationManagement_pom.verifyRejectionEmailComposerModal();

		softAssert.softAssertTrue(
				displayed,
				"Rejection email composer modal is displayed correctly.",
				"Rejection email composer modal is not displayed correctly.");
	}

	@Then("User sends the rejection email and verifies the email sent confirmation")
	public void user_sends_the_rejection_email_and_verifies_the_email_sent_confirmation() {

		boolean sent = applicationManagement_pom.sendRejectionEmailAndVerifyConfirmation();

		softAssert.softAssertTrue(
				sent,
				"Rejection email was sent and the email sent confirmation was displayed successfully.",
				"Rejection email was not sent successfully or the email sent confirmation was not displayed.");
	}

	@Then("User restores all rejected page 1 applications to {string} and verifies page 1 filter counts are restored")
	public void user_restores_all_rejected_page_1_applications_to_and_verifies_page_1_filter_counts_are_restored(
			String restoredStatus) {

		boolean restored = applicationManagement_pom.changeApplicationStatusesOnCurrentPage(
				rejectedApplicationNames,
				restoredStatus);

		softAssert.softAssertTrue(
				restored,
				"Restored all rejected page 1 applications to " + restoredStatus + " successfully.",
				"Failed to restore all rejected page 1 applications to " + restoredStatus + ".");

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", 10);
		expectedCounts.put("On Hold", 0);
		expectedCounts.put("Accepted", 0);
		expectedCounts.put("Rejected", 0);

		boolean restoredStateVerified = applicationManagement_pom.waitForCurrentPageStatusDistribution(
				"1",
				expectedCounts,
				new ArrayList<>(rejectedApplicationNames),
				restoredStatus,
				restoredStatus);

		softAssert.softAssertTrue(
				restoredStateVerified,
				"Restored all rejected page 1 applications and page 1 filter counts successfully.",
				"Failed to restore all rejected page 1 applications and page 1 filter counts.");
	}

	@Then("User selects a random page 1 application in {string} status for the row email action")
	public void user_selects_a_random_page_1_application_in_status_for_the_row_email_action(String requiredStatus) {

		Map<String, String> statuses = applicationManagement_pom.getCurrentPageApplicationStatuses();
		List<String> candidates = new ArrayList<>();
		for (Map.Entry<String, String> entry : statuses.entrySet()) {
			if (requiredStatus.equals(entry.getValue())) {
				candidates.add(entry.getKey());
			}
		}

		if (candidates.isEmpty()) {
			rowEmailApplicationName = null;
		} else {
			rowEmailApplicationName = candidates.get(randInt(0, candidates.size() - 1));
			logger.info("[ApplicationManagement_stepD] Selected page 1 row email application='"
					+ rowEmailApplicationName + "' requiredStatus='" + requiredStatus + "'");
		}

		softAssert.softAssertTrue(
				rowEmailApplicationName != null,
				"Selected a random page 1 application for the row email action successfully: " + rowEmailApplicationName,
				"Failed to select a random page 1 application in " + requiredStatus + " status for the row email action.");
	}

	@Then("User opens the email composer from the selected page 1 application row")
	public void user_opens_the_email_composer_from_the_selected_page_1_application_row() {

		boolean opened = applicationManagement_pom.openRowEmailComposerOnCurrentPage(rowEmailApplicationName);

		softAssert.softAssertTrue(
				opened,
				"Opened the email composer from the selected page 1 application row successfully.",
				"Failed to open the email composer from the selected page 1 application row.");
	}

	@Then("User verifies the standalone row email composer modal is displayed")
	public void user_verifies_the_standalone_row_email_composer_modal_is_displayed() {

		boolean displayed = applicationManagement_pom.verifyGenericEmailComposerModal();

		softAssert.softAssertTrue(
				displayed,
				"Standalone row email composer modal is displayed correctly.",
				"Standalone row email composer modal is not displayed correctly.");
	}

	@Then("User sends the standalone row email and verifies the email sent confirmation")
	public void user_sends_the_standalone_row_email_and_verifies_the_email_sent_confirmation() {

		boolean sent = applicationManagement_pom.sendGenericEmailAndVerifyConfirmation();

		softAssert.softAssertTrue(
				sent,
				"Standalone row email was sent and the email sent confirmation was displayed successfully.",
				"Standalone row email was not sent successfully or the email sent confirmation was not displayed.");
	}

	@Then("User closes the standalone row email composer modal without sending")
	public void user_closes_the_standalone_row_email_composer_modal_without_sending() {

		boolean closed = applicationManagement_pom.closeGenericEmailComposerModal();

		softAssert.softAssertTrue(
				closed,
				"Standalone row email composer modal was closed successfully without sending.",
				"Failed to close the standalone row email composer modal without sending.");
	}

	@Then("User verifies the bulk email composer modal is displayed")
	public void user_verifies_the_bulk_email_composer_modal_is_displayed() {

		boolean displayed = applicationManagement_pom.verifyBulkEmailComposerModal();

		softAssert.softAssertTrue(
				displayed,
				"Bulk email composer modal is displayed correctly.",
				"Bulk email composer modal is not displayed correctly.");
	}

	@Then("User sends the bulk email and verifies the email sent confirmation")
	public void user_sends_the_bulk_email_and_verifies_the_email_sent_confirmation() {

		boolean sent = applicationManagement_pom.sendGenericEmailAndVerifyConfirmation();

		softAssert.softAssertTrue(
				sent,
				"Bulk email was sent and the email sent confirmation was displayed successfully.",
				"Bulk email was not sent successfully or the email sent confirmation was not displayed.");
	}

	@Then("User selects a random 2 to 5 page 1 applications for the bulk email action")
	public void user_selects_a_random_2_to_5_page_1_applications_for_the_bulk_email_action() {

		bulkRandomEmailApplicationNames = applicationManagement_pom.selectRandomApplicationNamesFromCurrentPage(2, 5);

		softAssert.softAssertTrue(
				!bulkRandomEmailApplicationNames.isEmpty(),
				"Selected a random 2 to 5 page 1 applications for the bulk email action successfully: "
						+ bulkRandomEmailApplicationNames,
				"Failed to select a random 2 to 5 page 1 applications for the bulk email action.");
	}

	@Then("User individually selects the random page 1 applications for the bulk email action")
	public void user_individually_selects_the_random_page_1_applications_for_the_bulk_email_action() {

		boolean selected = applicationManagement_pom.selectApplicationsIndividuallyInRandomOrder(
				bulkRandomEmailApplicationNames);

		softAssert.softAssertTrue(
				selected,
				"Individually selected the random page 1 applications for the bulk email action successfully.",
				"Failed to individually select the random page 1 applications for the bulk email action.");
	}

	@When("User prepares page 1 applications for evaluation filter tab validation")
	public void user_prepares_page_1_applications_for_evaluation_filter_tab_validation() {

		filterValidationOnHoldApplicationName = null;
		filterValidationAcceptedApplicationName = null;
		filterValidationRejectedApplicationName = null;
		filterValidationApplicationNames.clear();

		filterValidationOnHoldApplicationName = selectRandomApplicationByStatus(
				"Needs Review",
				filterValidationApplicationNames,
				filterValidationApplicationNames);
		softAssert.softAssertTrue(
				filterValidationOnHoldApplicationName != null,
				"Selected a page 1 application for the On Hold filter validation flow successfully: "
						+ filterValidationOnHoldApplicationName,
				"Failed to select a page 1 application for the On Hold filter validation flow.");

		boolean updatedToOnHold = applicationManagement_pom.changeApplicationStatusOnCurrentPage(
				filterValidationOnHoldApplicationName,
				"On Hold");
		softAssert.softAssertTrue(
				updatedToOnHold,
				"Changed the selected page 1 application to On Hold for filter validation successfully.",
				"Failed to change the selected page 1 application to On Hold for filter validation.");

		filterValidationAcceptedApplicationName = selectRandomApplicationByStatus(
				"Needs Review",
				filterValidationApplicationNames,
				filterValidationApplicationNames);
		softAssert.softAssertTrue(
				filterValidationAcceptedApplicationName != null,
				"Selected a page 1 application for the Accepted filter validation flow successfully: "
						+ filterValidationAcceptedApplicationName,
				"Failed to select a page 1 application for the Accepted filter validation flow.");

		boolean updatedToAccepted = applicationManagement_pom.changeApplicationStatusOnCurrentPage(
				filterValidationAcceptedApplicationName,
				"Accepted");
		softAssert.softAssertTrue(
				updatedToAccepted,
				"Changed the selected page 1 application to Accepted for filter validation successfully.",
				"Failed to change the selected page 1 application to Accepted for filter validation.");

		boolean acceptedModalDisplayed = applicationManagement_pom.verifyStatusConfirmationModal(
				"Send Email",
				"You have accepted this startup into your program. Would you like to notify them with an email?");
		softAssert.softAssertTrue(
				acceptedModalDisplayed,
				"Accepted status confirmation modal is displayed correctly during filter validation setup.",
				"Accepted status confirmation modal is not displayed correctly during filter validation setup.");

		boolean acceptedNoThanks = applicationManagement_pom.chooseStatusConfirmationModalAction("No Thanks");
		softAssert.softAssertTrue(
				acceptedNoThanks,
				"Chose No Thanks for the Accepted filter validation flow successfully.",
				"Failed to choose No Thanks for the Accepted filter validation flow.");

		filterValidationRejectedApplicationName = selectRandomApplicationByStatus(
				"Needs Review",
				filterValidationApplicationNames,
				filterValidationApplicationNames);
		softAssert.softAssertTrue(
				filterValidationRejectedApplicationName != null,
				"Selected a page 1 application for the Rejected filter validation flow successfully: "
						+ filterValidationRejectedApplicationName,
				"Failed to select a page 1 application for the Rejected filter validation flow.");

		boolean updatedToRejected = applicationManagement_pom.changeApplicationStatusOnCurrentPage(
				filterValidationRejectedApplicationName,
				"Rejected");
		softAssert.softAssertTrue(
				updatedToRejected,
				"Changed the selected page 1 application to Rejected for filter validation successfully.",
				"Failed to change the selected page 1 application to Rejected for filter validation.");

		boolean rejectedModalDisplayed = applicationManagement_pom.verifyStatusConfirmationModal(
				"Send Email",
				"You have decided not to move forward with this team. Would you like to notify them with an email?");
		softAssert.softAssertTrue(
				rejectedModalDisplayed,
				"Rejected status confirmation modal is displayed correctly during filter validation setup.",
				"Rejected status confirmation modal is not displayed correctly during filter validation setup.");

		boolean rejectedNoThanks = applicationManagement_pom.chooseStatusConfirmationModalAction("No Thanks");
		softAssert.softAssertTrue(
				rejectedNoThanks,
				"Chose No Thanks for the Rejected filter validation flow successfully.",
				"Failed to choose No Thanks for the Rejected filter validation flow.");

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", 7);
		expectedCounts.put("On Hold", 1);
		expectedCounts.put("Accepted", 1);
		expectedCounts.put("Rejected", 1);

		boolean countsUpdated = applicationManagement_pom.waitForCurrentPageFilterCounts("1", expectedCounts);
		softAssert.softAssertTrue(
				countsUpdated,
				"Prepared the page 1 status mix for filter validation and verified the filter counts successfully.",
				"Failed to verify the page 1 filter counts after preparing the filter validation status mix.");
	}

	@Then("User verifies the evaluation filter tabs display the correct page 1 applications")
	public void user_verifies_the_evaluation_filter_tabs_display_the_correct_page_1_applications() {

		boolean needsReviewClicked = applicationManagement_pom.clickEvaluationFilterTab("Needs Review");
		softAssert.softAssertTrue(
				needsReviewClicked,
				"Clicked the Needs Review filter tab successfully.",
				"Failed to click the Needs Review filter tab.");

		boolean needsReviewVerified = applicationManagement_pom.verifyDisplayedRowsMatchStatus("Needs Review", 7);
		softAssert.softAssertTrue(
				needsReviewVerified,
				"Verified the Needs Review filter tab displays the correct page 1 applications successfully.",
				"The Needs Review filter tab does not display the correct page 1 applications.");

		boolean onHoldClicked = applicationManagement_pom.clickEvaluationFilterTab("On Hold");
		softAssert.softAssertTrue(
				onHoldClicked,
				"Clicked the On Hold filter tab successfully.",
				"Failed to click the On Hold filter tab.");

		boolean onHoldVerified = applicationManagement_pom.verifyDisplayedRowsMatchStatus("On Hold", 1);
		softAssert.softAssertTrue(
				onHoldVerified,
				"Verified the On Hold filter tab displays the correct page 1 application successfully.",
				"The On Hold filter tab does not display the correct page 1 application.");

		boolean acceptedClicked = applicationManagement_pom.clickEvaluationFilterTab("Accepted");
		softAssert.softAssertTrue(
				acceptedClicked,
				"Clicked the Accepted filter tab successfully.",
				"Failed to click the Accepted filter tab.");

		boolean acceptedVerified = applicationManagement_pom.verifyDisplayedRowsMatchStatus("Accepted", 1);
		softAssert.softAssertTrue(
				acceptedVerified,
				"Verified the Accepted filter tab displays the correct page 1 application successfully.",
				"The Accepted filter tab does not display the correct page 1 application.");

		boolean rejectedClicked = applicationManagement_pom.clickEvaluationFilterTab("Rejected");
		softAssert.softAssertTrue(
				rejectedClicked,
				"Clicked the Rejected filter tab successfully.",
				"Failed to click the Rejected filter tab.");

		boolean rejectedVerified = applicationManagement_pom.verifyDisplayedRowsMatchStatus("Rejected", 1);
		softAssert.softAssertTrue(
				rejectedVerified,
				"Verified the Rejected filter tab displays the correct page 1 application successfully.",
				"The Rejected filter tab does not display the correct page 1 application.");

		boolean allClicked = applicationManagement_pom.clickEvaluationFilterTab("All");
		softAssert.softAssertTrue(
				allClicked,
				"Clicked the All filter tab successfully.",
				"Failed to click the All filter tab.");

		boolean allVerified = applicationManagement_pom.verifyDisplayedRowCount(10);
		softAssert.softAssertTrue(
				allVerified,
				"Verified the All filter tab displays all 10 page 1 applications successfully.",
				"The All filter tab does not display all 10 page 1 applications.");
	}

	@Then("User clicks on the {string} evaluation filter tab")
	public void user_clicks_on_the_evaluation_filter_tab(String filterName) {

		boolean clicked = applicationManagement_pom.clickEvaluationFilterTab(filterName);

		softAssert.softAssertTrue(
				clicked,
				"Clicked on the " + filterName + " evaluation filter tab successfully.",
				"Failed to click on the " + filterName + " evaluation filter tab.");
	}

	@Then("User verifies the {string} evaluation filter tab displays {int} page 1 applications in {string} status")
	public void user_verifies_the_evaluation_filter_tab_displays_page_1_applications_in_status(
			String filterName,
			Integer expectedRowCount,
			String expectedStatus) {

		boolean verified = applicationManagement_pom.verifyDisplayedRowsMatchStatus(expectedStatus, expectedRowCount);

		softAssert.softAssertTrue(
				verified,
				"Verified the " + filterName + " evaluation filter tab displays " + expectedRowCount
						+ " page 1 applications in " + expectedStatus + " status successfully.",
				"Failed to verify the " + filterName + " evaluation filter tab displays " + expectedRowCount
						+ " page 1 applications in " + expectedStatus + " status.");
	}

	@Then("User verifies the {string} evaluation filter tab displays all 10 page 1 applications")
	public void user_verifies_the_evaluation_filter_tab_displays_all_10_page_1_applications(String filterName) {

		boolean verified = applicationManagement_pom.verifyDisplayedRowCount(10);

		softAssert.softAssertTrue(
				verified,
				"Verified the " + filterName + " evaluation filter tab displays all 10 page 1 applications successfully.",
				"Failed to verify the " + filterName + " evaluation filter tab displays all 10 page 1 applications.");
	}

	@When("User restores the evaluation filter tab validation applications to {string}")
	public void user_restores_the_evaluation_filter_tab_validation_applications_to(
			String restoredStatus) {

		boolean allClicked = applicationManagement_pom.clickEvaluationFilterTab("All");
		softAssert.softAssertTrue(
				allClicked,
				"Returned to the All filter tab before restoring the filter validation applications successfully.",
				"Failed to return to the All filter tab before restoring the filter validation applications.");

		boolean restored = applicationManagement_pom.changeApplicationStatusesOnCurrentPage(
				new ArrayList<>(filterValidationApplicationNames),
				restoredStatus);
		softAssert.softAssertTrue(
				restored,
				"Restored the filter validation applications to " + restoredStatus + " successfully.",
				"Failed to restore the filter validation applications to " + restoredStatus + ".");
	}

	@Then("User verifies page 1 filter counts are restored after evaluation filter tab validation")
	public void user_verifies_page_1_filter_counts_are_restored_after_evaluation_filter_tab_validation() {

		Map<String, Integer> expectedCounts = new HashMap<>();
		expectedCounts.put("All", 10);
		expectedCounts.put("Needs Review", 10);
		expectedCounts.put("On Hold", 0);
		expectedCounts.put("Accepted", 0);
		expectedCounts.put("Rejected", 0);

		boolean restoredCounts = applicationManagement_pom.waitForCurrentPageFilterCounts("1", expectedCounts);
		softAssert.softAssertTrue(
				restoredCounts,
				"Restored the page 1 filter counts successfully after the filter validation flow.",
				"Failed to restore the page 1 filter counts after the filter validation flow.");
	}

	private String selectRandomApplicationByStatus(
			String requiredStatus,
			List<String> trackedApplicationNames,
			List<String> excludedApplicationNames) {
		Map<String, String> statuses = applicationManagement_pom.getCurrentPageApplicationStatuses();
		List<String> candidates = new ArrayList<>();
		for (Map.Entry<String, String> entry : statuses.entrySet()) {
			if (requiredStatus.equals(entry.getValue()) && !excludedApplicationNames.contains(entry.getKey())) {
				candidates.add(entry.getKey());
			}
		}

		if (candidates.isEmpty()) {
			return null;
		}

		String selectedApplicationName = candidates.get(randInt(0, candidates.size() - 1));
		trackedApplicationNames.add(selectedApplicationName);
		logger.info("[ApplicationManagement_stepD] Selected random application='" + selectedApplicationName
				+ "' requiredStatus='" + requiredStatus + "' excludedApplicationNames=" + excludedApplicationNames);
		return selectedApplicationName;
	}
}
