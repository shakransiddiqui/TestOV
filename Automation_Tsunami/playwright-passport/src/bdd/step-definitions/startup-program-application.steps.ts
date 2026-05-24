import { Then, When, Given } from "@cucumber/cucumber";
import { getProperty } from "../configuration-reader";
import { PassportWorld } from "../world";
import { StartupProgramApplicationPage } from "../../pages/startup-program-application.page";

function targetProgramTitle(configKey: string): string {
  return getProperty(configKey).trim();
}

Then("User saves the generated startup signup credentials for later use", async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.saveGeneratedCredentials(
    "Results/generated_startup_accounts.txt",
    getProperty("startupTargetProgramTitle"),
    this.values
  );
});

Then("User searches for the configured startup target program", async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.searchForProgram(targetProgramTitle("startupTargetProgramTitle"));
});

Then("User searches for the startup target program from config key {string}", async function (this: PassportWorld, configKey: string) {
  await this.startupProgramApplicationPage.searchForProgram(targetProgramTitle(configKey));
});

Then("User opens the matching startup target program details from search results", async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.openMatchingProgramDetails(targetProgramTitle("startupTargetProgramTitle"));
});

Then("User opens the matching startup target program details from search results using config key {string}", async function (this: PassportWorld, configKey: string) {
  await this.startupProgramApplicationPage.openMatchingProgramDetails(targetProgramTitle(configKey));
});

Then("User clicks on Apply Now for the startup target program", async function (this: PassportWorld) {
  this.parentPageIndex = this.page.context().pages().length - 1;
  await this.startupProgramApplicationPage.clickApplyNow();
});

Then("User switches to the newly opened startup application tab", async function (this: PassportWorld) {
  this.page = await this.startupProgramApplicationPage.switchToNewApplicationTab();
  this.startupProgramApplicationPage = new StartupProgramApplicationPage(this.page);
});

Then("User should see the configured startup target program title on the application page", async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.verifyProgramTitleOnApplicationPage(targetProgramTitle("startupTargetProgramTitle"));
});

Then("User should see the startup target program title from config key {string} on the application page", async function (this: PassportWorld, configKey: string) {
  await this.startupProgramApplicationPage.verifyProgramTitleOnApplicationPage(targetProgramTitle(configKey));
});

Then("User fills the startup program application form using generated signup data", async function (this: PassportWorld) {
  const email = this.values.get("newEmail") ?? "";
  const emailLocalPart = email.split("@")[0] ?? "";
  await this.startupProgramApplicationPage.fillApplicationForm({
    location: getProperty("location"),
    websiteSuffix: getProperty("startupApplicationWebsite"),
    linkedInSuffix: getProperty("startupApplicationLinkedIn"),
    email,
    emailLocalPart
  });
});

Then("User uploads the startup pitch deck", async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.uploadPitchDeck();
});

Then("User checks Save my responses to use in future applications on Passport for startup application", async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.checkSaveResponsesCheckbox();
});

Then("User clicks on {string} button for startup application", async function (this: PassportWorld, buttonText: string) {
  await this.startupProgramApplicationPage.clickButton(buttonText);
});

Then("User should see the startup application submitted success message", async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.successMessageVisible();
});

Given("User completes initial startup setup successfully", async function (this: PassportWorld) {
  await this.homePage.goto();
});

Then('Click on "See More" button for Startup Program', async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.openMatchingProgramDetails(targetProgramTitle("startupTargetProgramTitle"));
});

When('Click on "Apply Now" button for Startup Program', async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.clickApplyNow();
});

Then("User fills the application form using existing reusable method", async function (this: PassportWorld) {
  const email = this.values.get("newEmail") ?? getProperty("SLvalidEmail");
  await this.startupProgramApplicationPage.fillApplicationForm({
    location: getProperty("location"),
    websiteSuffix: getProperty("startupApplicationWebsite"),
    linkedInSuffix: getProperty("startupApplicationLinkedIn"),
    email,
    emailLocalPart: email.split("@")[0] ?? "startup"
  });
});

Then("Upload pitch deck file", async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.uploadPitchDeck();
});

Then('Check "Save my responses" checkbox', async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.checkSaveResponsesCheckbox();
});

Then('Click "Submit" button', async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.clickButton("Submit");
});

Then('Verify that the success message "Thank you for submitting your application!" is displayed', async function (this: PassportWorld) {
  await this.startupProgramApplicationPage.successMessageVisible();
});
