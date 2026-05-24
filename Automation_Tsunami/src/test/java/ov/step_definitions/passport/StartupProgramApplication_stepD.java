package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.Then;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import ov.utilities.GeneratedSignupData;

public class StartupProgramApplication_stepD extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(StartupProgramApplication_stepD.class);

	private String parentWindowHandle;

	private String getStartupProgramTitleFromConfigKey(String configKey) {
		String targetProgramTitle = ConfigurationReader.getProperty(configKey);
		if (targetProgramTitle == null) {
			logger.warn("[StartupProgramApplication_stepD] No configuration value was found for configKey='" + configKey + "'");
			return null;
		}
		targetProgramTitle = targetProgramTitle.trim();
		logger.info("[StartupProgramApplication_stepD] Resolved configKey='" + configKey
				+ "' to targetProgramTitle='" + targetProgramTitle + "'");
		return targetProgramTitle;
	}

	@Then("User saves the generated startup signup credentials for later use")
	public void user_saves_the_generated_startup_signup_credentials_for_later_use() {

		String targetProgramTitle = ConfigurationReader.getProperty("startupTargetProgramTitle");
		boolean saved = GeneratedSignupData.hasRequiredSignupData()
				&& GeneratedSignupData.appendToFile("Results/generated_startup_accounts.txt", targetProgramTitle);

		softAssert.softAssertTrue(
				saved,
				"Generated startup signup credentials were saved successfully.",
				"Failed to save generated startup signup credentials."
		);
	}

	@Then("User searches for the configured startup target program")
	public void user_searches_for_the_configured_startup_target_program() {

		user_searches_for_the_startup_target_program_from_config_key("startupTargetProgramTitle");
	}

	@Then("User searches for the startup target program from config key {string}")
	public void user_searches_for_the_startup_target_program_from_config_key(String configKey) {

		String targetProgramTitle = getStartupProgramTitleFromConfigKey(configKey);
		boolean searched = startupProgramApplication_pom.searchForProgram(targetProgramTitle);

		softAssert.softAssertTrue(
				searched,
				"Searched for target startup program successfully: " + targetProgramTitle,
				"Failed to search for target startup program: " + targetProgramTitle
		);
	}

	@Then("User opens the matching startup target program details from search results")
	public void user_opens_the_matching_startup_target_program_details_from_search_results() {

		user_opens_the_matching_startup_target_program_details_from_search_results_using_config_key("startupTargetProgramTitle");
	}

	@Then("User opens the matching startup target program details from search results using config key {string}")
	public void user_opens_the_matching_startup_target_program_details_from_search_results_using_config_key(String configKey) {

		String targetProgramTitle = getStartupProgramTitleFromConfigKey(configKey);
		boolean opened = startupProgramApplication_pom.openMatchingProgramDetails(targetProgramTitle);

		softAssert.softAssertTrue(
				opened,
				"Opened matching startup target program successfully: " + targetProgramTitle,
				"Could not find startup target program in paginated search results: " + targetProgramTitle
		);
	}

	@Then("User clicks on Apply Now for the startup target program")
	public void user_clicks_on_apply_now_for_the_startup_target_program() {

		parentWindowHandle = driver.getWindowHandle();
		boolean clicked = startupProgramApplication_pom.clickApplyNow();

		softAssert.softAssertTrue(
				clicked,
				"Clicked Apply Now for the startup target program.",
				"Failed to click Apply Now for the startup target program."
		);
	}

	@Then("User switches to the newly opened startup application tab")
	public void user_switches_to_the_newly_opened_startup_application_tab() {

		boolean switched = startupProgramApplication_pom.switchToNewApplicationTab(parentWindowHandle);

		softAssert.softAssertTrue(
				switched,
				"Switched to the newly opened startup application tab.",
				"Failed to switch to the newly opened startup application tab."
		);
	}

	@Then("User should see the configured startup target program title on the application page")
	public void user_should_see_the_configured_startup_target_program_title_on_the_application_page() {

		user_should_see_the_startup_target_program_title_from_config_key_on_the_application_page("startupTargetProgramTitle");
	}

	@Then("User should see the startup target program title from config key {string} on the application page")
	public void user_should_see_the_startup_target_program_title_from_config_key_on_the_application_page(String configKey) {

		String targetProgramTitle = getStartupProgramTitleFromConfigKey(configKey);
		boolean visible = startupProgramApplication_pom.verifyProgramTitleOnApplicationPage(targetProgramTitle);

		softAssert.softAssertTrue(
				visible,
				"Target startup program title is visible on the application page: " + targetProgramTitle,
				"Target startup program title is not visible on the application page: " + targetProgramTitle
		);
	}

	@Then("User fills the startup program application form using generated signup data")
	public void user_fills_the_startup_program_application_form_using_generated_signup_data() {

		boolean filled = startupProgramApplication_pom.fillApplicationForm(
				ConfigurationReader.getProperty("location"),
				ConfigurationReader.getProperty("startupApplicationWebsite"),
				ConfigurationReader.getProperty("startupApplicationLinkedIn")
		);

		softAssert.softAssertTrue(
				filled,
				"Startup program application form was filled successfully.",
				"Failed to fill the startup program application form."
		);
	}

	@Then("User uploads the startup pitch deck")
	public void user_uploads_the_startup_pitch_deck() {

		boolean uploaded = startupProgramApplication_pom.uploadPitchDeck();

		softAssert.softAssertTrue(
				uploaded,
				"Startup pitch deck uploaded successfully.",
				"Failed to upload the startup pitch deck."
		);
	}

	@Then("User checks Save my responses to use in future applications on Passport for startup application")
	public void user_checks_save_my_responses_to_use_in_future_applications_on_passport_for_startup_application() {

		boolean checked = startupProgramApplication_pom.checkSaveResponsesCheckbox();

		softAssert.softAssertTrue(
				checked,
				"Save my responses checkbox was checked successfully.",
				"Failed to check Save my responses checkbox."
		);
	}

	@Then("User clicks on {string} button for startup application")
	public void user_clicks_on_button_for_startup_application(String buttonText) {

		boolean clicked = startupProgramApplication_pom.clickButton(buttonText);

		softAssert.softAssertTrue(
				clicked,
				"Clicked on startup application button successfully: " + buttonText,
				"Failed to click on startup application button: " + buttonText
		);
	}

	@Then("User should see the startup application submitted success message")
	public void user_should_see_the_startup_application_submitted_success_message() {

		boolean visible = startupProgramApplication_pom.successMessageVisible();

		softAssert.softAssertTrue(
				visible,
				"Startup application success message is visible.",
				"Startup application success message is not visible."
		);
	}
}