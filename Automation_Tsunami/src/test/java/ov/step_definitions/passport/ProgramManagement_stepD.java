package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ov.pages.passport.ProgramManagement_POM.DeleteRunResult;
import ov.pages.passport.ProgramManagement_POM.ProgramScanResult;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;

public class ProgramManagement_stepD extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(ProgramManagement_stepD.class);

	private ProgramScanResult unpublishedProgramsScan = new ProgramScanResult();
	private ProgramScanResult targetProgramsScan = new ProgramScanResult();
	private DeleteRunResult deleteRunResult = new DeleteRunResult();

	@Then("User clicks on the Organization Dashboard icon")
	public void user_clicks_on_the_organization_dashboard_icon() {

		boolean clicked = programManagement_pom.clickOrganizationDashboardIcon();

		softAssert.softAssertTrue(
				clicked,
				"Organization Dashboard icon clicked successfully.",
				"Failed to click on the Organization Dashboard icon.");
	}

	@Then("User opens page 1 of the organization programs list")
	public void user_opens_page_1_of_the_organization_programs_list() {

		boolean opened = programManagement_pom.openOrganizationProgramsPageOne();

		softAssert.softAssertTrue(
				opened,
				"Organization programs page 1 opened successfully.",
				"Failed to open organization programs page 1.");
	}

	@Then("User records all programs with unpublished applications across organization pages")
	public void user_records_all_programs_with_unpublished_applications_across_organization_pages() {

		unpublishedProgramsScan = programManagement_pom.scanProgramsWithUnpublishedApplicationsAcrossOrganizationPages();

		softAssert.softAssertTrue(
				!unpublishedProgramsScan.failed && unpublishedProgramsScan.matchedProgramsCount > 0,
				"Recorded unpublished programs across organization pages successfully. Count="
						+ unpublishedProgramsScan.matchedProgramsCount,
				"Failed to record programs with unpublished applications across organization pages.");
	}

	@Then("User verifies Manage Applications is disabled for every recorded unpublished program")
	public void user_verifies_manage_applications_is_disabled_for_every_recorded_unpublished_program() {

		boolean result = !unpublishedProgramsScan.failed
				&& unpublishedProgramsScan.matchedProgramsCount > 0
				&& unpublishedProgramsScan.manageApplicationsDisabledForAllMatches;

		softAssert.softAssertTrue(
				result,
				"Manage Applications is disabled for every recorded unpublished program.",
				"Manage Applications is enabled for one or more recorded unpublished programs.");
	}

	@Then("User records all deletable programs across organization pages")
	public void user_records_all_deletable_programs_across_organization_pages() {

		targetProgramsScan = programManagement_pom.scanTargetProgramsAcrossOrganizationPages();

		softAssert.softAssertTrue(
				!targetProgramsScan.failed && targetProgramsScan.matchedProgramsCount > 0,
				"Recorded deletable programs across organization pages successfully. Count="
						+ targetProgramsScan.matchedProgramsCount,
				"Failed to record deletable programs across organization pages.");
	}

	@When("User deletes the recorded eligible programs from the organization pages")
	public void user_deletes_the_recorded_eligible_programs_from_the_organization_pages() {

		deleteRunResult = programManagement_pom.deleteEligibleProgramsAcrossOrganizationPages();

		boolean result = !deleteRunResult.failed
				&& deleteRunResult.deletedProgramsCount == targetProgramsScan.matchedProgramsCount;

		softAssert.softAssertTrue(
				result,
				"Recorded eligible programs were deleted from the organization pages successfully. Deleted count="
						+ deleteRunResult.deletedProgramsCount,
				"Failed to delete all recorded eligible programs from the organization pages. Expected deletions="
						+ targetProgramsScan.matchedProgramsCount + " Actual deletions="
						+ deleteRunResult.deletedProgramsCount);
	}

	@Then("User verifies the delete confirmation modal was displayed correctly for each deleted program")
	public void user_verifies_the_delete_confirmation_modal_was_displayed_correctly_for_each_deleted_program() {

		boolean result = !deleteRunResult.failed && deleteRunResult.deleteModalVerifiedForAllPrograms;

		softAssert.softAssertTrue(
				result,
				"Delete confirmation modal was displayed correctly for each deleted program.",
				"Delete confirmation modal was not displayed correctly for one or more deleted programs.");
	}

	@Then("User verifies the recorded eligible programs were removed from the organization pages")
	public void user_verifies_the_recorded_eligible_programs_were_removed_from_the_organization_pages() {

		boolean noEligibleProgramsRemain = programManagement_pom.noEligibleProgramsRemainAcrossOrganizationPages();

		softAssert.softAssertTrue(
				noEligibleProgramsRemain,
				"Recorded eligible programs were removed from the organization pages successfully.",
				"One or more eligible programs still remain on the organization pages after deletion.");
	}

	@Then("User verifies the configured target program with applications still remains on the organization pages")
	public void user_verifies_the_configured_target_program_with_applications_still_remains_on_the_organization_pages() {

		String protectedProgramName = ConfigurationReader.getProperty("programManagerTargetProgramName");
		logger.info("[ProgramManagement_stepD] Verifying protected program still remains. protectedProgramName='"
				+ protectedProgramName + "'");

		boolean protectedProgramStillPresent = programManagement_pom
				.isProgramWithApplicationsPresentAnywhere(protectedProgramName);

		softAssert.softAssertTrue(
				protectedProgramStillPresent,
				"Configured target program with applications still remains on the organization pages: "
						+ protectedProgramName,
				"Configured target program with applications was not found on the organization pages: "
						+ protectedProgramName);
	}
}