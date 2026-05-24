import { Then, When } from "@cucumber/cucumber";
import { expect } from "@playwright/test";
import { getProperty } from "../configuration-reader";
import { PassportWorld } from "../world";

async function switchToNewestTab(world: PassportWorld, stateKey: string): Promise<void> {
  const previousCount = Number(world.state.get(stateKey) ?? world.context.pages().length);
  await expect
    .poll(async () => world.context.pages().length, { timeout: 30000 })
    .toBeGreaterThan(previousCount);
  const newestPage = world.context.pages().at(-1);
  if (!newestPage) {
    throw new Error("No new page was opened.");
  }
  world.bindPage(newestPage);
}

When("User clicks on the first application from the evaluation results", async function (this: PassportWorld) {
  const selectedName = await this.applicationEvaluationPage.clickFirstApplicationFromResults();
  this.state.set("selectedApplicationEvaluationName", selectedName);
});

Then("User verifies the selected application evaluation details page is displayed", async function (this: PassportWorld) {
  expect(
    await this.applicationEvaluationPage.applicationDetailsVisible(
      String(this.state.get("selectedApplicationEvaluationName") ?? "")
    )
  ).toBeTruthy();
});

When("User clicks on {string} from the application evaluation details page", async function (this: PassportWorld, buttonLabel: string) {
  await this.applicationEvaluationPage.clickDetailsButton(buttonLabel);
});

Then("User verifies the applications list is displayed again for the same target program", async function (this: PassportWorld) {
  expect(
    await this.applicationManagementPage.pageContainsText(new RegExp(getProperty("programEvaluationTargetProgramName"), "i"))
  ).toBeTruthy();
});

When("User changes the selected application evaluation status to {string}", async function (this: PassportWorld, newStatus: string) {
  await this.applicationEvaluationPage.changeStatusTo(newStatus);
});

Then("User verifies the selected application evaluation status is {string}", async function (this: PassportWorld, expectedStatus: string) {
  expect(await this.applicationEvaluationPage.selectedStatusMatches(expectedStatus)).toBeTruthy();
});

Then("User verifies the application evaluation acceptance confirmation modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.statusConfirmationModalVisible("acceptance")).toBeTruthy();
});

Then("User verifies the application evaluation rejection confirmation modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.statusConfirmationModalVisible("rejection")).toBeTruthy();
});

Then("User verifies {string} is selected on the application evaluation details page", async function (this: PassportWorld, tabName: string) {
  expect(await this.applicationEvaluationPage.ratingTabSelected(tabName)).toBeTruthy();
});

When("User selects a star rating on the application evaluation details page", async function (this: PassportWorld) {
  this.state.set("selectedApplicationEvaluationRatingValue", 4);
  await this.applicationEvaluationPage.selectStarRating(4);
});

When("User enters a rating comment on the application evaluation details page", async function (this: PassportWorld) {
  const comment = `Playwright rating comment ${Date.now()}`;
  this.state.set("selectedApplicationEvaluationRatingComment", comment);
  await this.applicationEvaluationPage.enterRatingComment(comment);
});

Then("User verifies the rating was submitted successfully on the application evaluation details page", async function (this: PassportWorld) {
  expect(
    await this.applicationEvaluationPage.ratingVisible(
      Number(this.state.get("selectedApplicationEvaluationRatingValue") ?? 4),
      String(this.state.get("selectedApplicationEvaluationRatingComment") ?? "")
    )
  ).toBeTruthy();
});

Then("User verifies the rating was not submitted without selecting stars on the application evaluation details page", async function (this: PassportWorld) {
  expect(await this.applicationEvaluationPage.ratingNotSubmittedWithoutStars()).toBeTruthy();
});

Then("User verifies the {string} view is displayed on the application evaluation details page", async function (this: PassportWorld) {
  expect(await this.applicationEvaluationPage.allRatingsViewVisible()).toBeTruthy();
});

Then("User verifies the No Submitted Ratings empty state is displayed on the application evaluation details page", async function (this: PassportWorld) {
  expect(await this.applicationEvaluationPage.noSubmittedRatingsVisible()).toBeTruthy();
});

Then("User records the current application name from the application evaluation details page", async function (this: PassportWorld) {
  this.state.set("recordedApplicationEvaluationName", await this.applicationEvaluationPage.currentApplicationName());
});

When("User clicks on the next application arrow from the application evaluation details page", async function (this: PassportWorld) {
  this.state.set("navigatedApplicationEvaluationName", await this.applicationEvaluationPage.navigateBetweenApplications("next"));
});

Then("User verifies the displayed application changed on the application evaluation details page", async function (this: PassportWorld) {
  expect(String(this.state.get("navigatedApplicationEvaluationName") ?? "")).not.toBe(
    String(this.state.get("recordedApplicationEvaluationName") ?? "")
  );
});

When("User clicks on the previous application arrow from the application evaluation details page", async function (this: PassportWorld) {
  this.state.set("navigatedApplicationEvaluationName", await this.applicationEvaluationPage.navigateBetweenApplications("previous"));
});

Then("User verifies the previously recorded application is displayed again on the application evaluation details page", async function (this: PassportWorld) {
  expect(String(this.state.get("navigatedApplicationEvaluationName") ?? "")).toBe(
    String(this.state.get("recordedApplicationEvaluationName") ?? "")
  );
});

When("User clicks on the email icon from the application evaluation details page", async function (this: PassportWorld) {
  await this.applicationEvaluationPage.clickEmailIcon();
});

When("User clicks on the Go To Program Page icon from the application evaluation details page", async function (this: PassportWorld) {
  this.state.set("programPageFromEvaluationCountBeforeOpen", this.context.pages().length);
  await this.applicationManagementPage.clickQuickAction("Go To Program Page");
});

Then("User switches to the newly opened Program page tab from the application evaluation details page", async function (this: PassportWorld) {
  await switchToNewestTab(this, "programPageFromEvaluationCountBeforeOpen");
});

Then("User verifies the Program page is displayed from the application evaluation details page", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.pageContainsText(/program/i)).toBeTruthy();
});

When("User opens the three-dot menu from the application evaluation details page", async function (this: PassportWorld) {
  await this.applicationManagementPage.evaluationHeaderMenuVisible();
});

Then("User verifies {string} is displayed in the three-dot menu from the application evaluation details page", async function (this: PassportWorld, optionName: string) {
  expect(await this.applicationManagementPage.headerMenuOptionVisible(optionName)).toBeTruthy();
});

When("User clicks on {string} from the three-dot menu on the application evaluation details page", async function (this: PassportWorld, optionName: string) {
  this.state.set("editProgramFromEvaluationCountBeforeOpen", this.context.pages().length);
  await this.applicationManagementPage.clickHeaderMenuOption(optionName);
});

Then("User switches to the newly opened Edit Program tab from the application evaluation details page", async function (this: PassportWorld) {
  await switchToNewestTab(this, "editProgramFromEvaluationCountBeforeOpen");
});

Then("User verifies the Edit Program page and form are displayed from the application evaluation details page", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.pageContainsText(/edit program/i)).toBeTruthy();
});

When("User clicks on the copy link icon from the application evaluation details page", async function (this: PassportWorld) {
  await this.applicationEvaluationPage.clickCopyLinkIcon();
});

Then("User verifies the copy link feedback is displayed on the application evaluation details page", async function (this: PassportWorld) {
  expect(await this.applicationEvaluationPage.copyLinkFeedbackVisible()).toBeTruthy();
});

When("User refreshes the application evaluation details page", async function (this: PassportWorld) {
  await this.applicationEvaluationPage.refreshDetailsPage();
});

When("User closes the status confirmation modal from the application evaluation details page", async function (this: PassportWorld) {
  await this.applicationEvaluationPage.closeStatusConfirmationModal();
});

Then("User verifies the submitted rating still persists on the", async function (this: PassportWorld) {
  expect(
    await this.applicationEvaluationPage.ratingVisible(
      Number(this.state.get("selectedApplicationEvaluationRatingValue") ?? 4),
      String(this.state.get("selectedApplicationEvaluationRatingComment") ?? "")
    )
  ).toBeTruthy();
});
