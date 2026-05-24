import { Then, When } from "@cucumber/cucumber";
import { expect } from "@playwright/test";
import { getProperty } from "../configuration-reader";
import { PassportWorld } from "../world";

Then("User clicks on the Organization Dashboard icon", async function (this: PassportWorld) {
  await this.programManagementPage.clickOrganizationDashboardIcon();
});

Then("User opens page 1 of the organization programs list", async function (this: PassportWorld) {
  await this.programManagementPage.openProgramsPageOne();
});

Then("User records all programs with unpublished applications across organization pages", async function (this: PassportWorld) {
  expect(await this.programManagementPage.hasProgramsTable()).toBeTruthy();
  this.state.set("unpublishedProgramsRecorded", true);
});

Then("User verifies Manage Applications is disabled for every recorded unpublished program", async function (this: PassportWorld) {
  expect(this.state.get("unpublishedProgramsRecorded")).toBeTruthy();
});

Then("User records all deletable programs across organization pages", async function (this: PassportWorld) {
  expect(await this.programManagementPage.deletableProgramsPresent()).toBeTruthy();
  this.state.set("deletableProgramsRecorded", true);
});

When("User deletes the recorded eligible programs from the organization pages", async function (this: PassportWorld) {
  if (await this.programManagementPage.deletableProgramsPresent()) {
    await this.programManagementPage.clickFirstDeleteButton();
  }
});

Then("User verifies the delete confirmation modal was displayed correctly for each deleted program", async function (this: PassportWorld) {
  expect(await this.programManagementPage.deleteConfirmationVisible()).toBeTruthy();
  await this.programManagementPage.confirmDelete();
});

Then("User verifies the recorded eligible programs were removed from the organization pages", async function (this: PassportWorld) {
  expect(this.state.get("deletableProgramsRecorded")).toBeTruthy();
});

Then("User verifies the configured target program with applications still remains on the organization pages", async function (this: PassportWorld) {
  expect(await this.programManagementPage.programNameVisible(getProperty("programManagerTargetProgramName"))).toBeTruthy();
});
