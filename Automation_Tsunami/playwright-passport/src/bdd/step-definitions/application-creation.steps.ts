import { Then, When } from "@cucumber/cucumber";
import { expect } from "@playwright/test";
import { getProperty } from "../configuration-reader";
import { PassportWorld } from "../world";

Then(
  "User enters {string} into the {string} field of Create application",
  async function (this: PassportWorld, fieldValueKey: string, fieldName: string) {
    const value = getProperty(fieldValueKey);
    const actual = await this.applicationCreationPage.fillCreateApplicationField(fieldName, value);
    expect(actual).toBe(value);
  }
);

Then("User selects the {string} option on Create Application page", async function (this: PassportWorld, optionName: string) {
  await this.applicationCreationPage.selectOption(optionName);
});

Then("User should see {string} in the Application Builder page", async function (this: PassportWorld, textElement: string) {
  await this.applicationCreationPage.builderTextVisible(textElement);
});

Then("User clicks the collapse icon on the Application Builder page", async function (this: PassportWorld) {
  await this.applicationCreationPage.clickCollapseIcon();
});

Then("Standard Questions should be collapsed", async function (this: PassportWorld) {
  const collapsed = await this.applicationCreationPage.standardQuestionsCollapsed();
  expect(collapsed).toBeTruthy();
});

Then("User scrolls to {string} section", async function (this: PassportWorld, sectionName: string) {
  await this.applicationCreationPage.scrollToSection(sectionName);
});

Then("User clicks on {string} in Additional Questions section", async function (this: PassportWorld, buttonText: string) {
  await this.applicationCreationPage.clickAdditionalQuestionButton(buttonText);
});

Then("User should see the New Question form", async function (this: PassportWorld) {
  const visible = await this.applicationCreationPage.newQuestionFormVisible();
  expect(visible).toBeTruthy();
});

Then("User should see {string} validation message for Additional Question", async function (this: PassportWorld, expectedMsg: string) {
  const actual = await this.applicationCreationPage.additionalQuestionValidationMessage();
  expect(actual).toContain(expectedMsg);
});

Then("Additional Question {string} button should be disabled", async function (this: PassportWorld) {
  const disabled = await this.applicationCreationPage.additionalQuestionSaveDisabled();
  expect(disabled).toBeTruthy();
});

Then("User clicks on {string} button on the Application Builder page", async function (this: PassportWorld, buttonText: string) {
  await this.applicationCreationPage.clickBuilderButton(buttonText);
});

Then("User fills the Additional Questions form with the following data", async function (this: PassportWorld, dataTable) {
  const rows = dataTable.hashes();
  const actual = await this.applicationCreationPage.fillAdditionalQuestionsForm(rows);
  expect(actual).toBe(rows.length);
});

Then("User verifies Rubric section components", async function (this: PassportWorld) {
  const visible = await this.applicationCreationPage.rubricSectionVisible();
  expect(visible).toBeTruthy();
});

When("User clicks on the first Rubric Edit Question icon", async function (this: PassportWorld) {
  await this.applicationCreationPage.clickFirstRubricEditQuestionIcon();
});

Then("User removes the Default question", async function (this: PassportWorld) {
  await this.applicationCreationPage.removeDefaultRubricQuestion();
});

Then("User should see {string} validation message for Rubric Question", async function (this: PassportWorld, expectedMsg: string) {
  const actual = await this.applicationCreationPage.rubricValidationMessage();
  expect(actual).toContain(expectedMsg);
});

Then("Rubric Question {string} button should be disabled", async function (this: PassportWorld, buttonText: string) {
  const disabled = await this.applicationCreationPage.rubricButtonDisabled(buttonText);
  expect(disabled).toBeTruthy();
});

Then("User sets the Publish Open Date to now", async function (this: PassportWorld) {
  await this.applicationCreationPage.setPublishOpenDateToNow();
});

Then("User copies the Application Link on the Publish page", async function (this: PassportWorld) {
  await this.applicationCreationPage.copyApplicationLink();
});

Then("User adds a random invite email and verifies it appears in the list", async function (this: PassportWorld) {
  const email = await this.applicationCreationPage.addRandomInviteEmail();
  this.values.set("lastInvitedEmail", email);
});

Then("User deletes the last added invite email and verifies it is removed", async function (this: PassportWorld) {
  const email = this.values.get("lastInvitedEmail") ?? "";
  await this.applicationCreationPage.deleteInviteEmail(email);
});

Then("User clicks on {string} button on the Publish page", async function (this: PassportWorld, buttonText: string) {
  await this.applicationCreationPage.clickBuilderButton(buttonText);
});

Then("User should be on the Preview Application page", async function (this: PassportWorld) {
  const visible = await this.applicationCreationPage.previewPageVisible();
  expect(visible).toBeTruthy();
});

Then("User should be on the Standard Questions tab", async function (this: PassportWorld) {
  const active = await this.applicationCreationPage.standardQuestionsTabActive();
  expect(active).toBeTruthy();
});

Then("User should be on the Additional Questions tab", async function (this: PassportWorld) {
  const active = await this.applicationCreationPage.additionalQuestionsTabActive();
  expect(active).toBeTruthy();
});

Then("Applicant should see the following Standard Questions on the Preview page", async function (this: PassportWorld, dataTable) {
  const questions = dataTable.hashes().map((row: Record<string, string>) => row.Question);
  const visible = await this.applicationCreationPage.verifyQuestionsVisible(questions);
  expect(visible).toBeTruthy();
});

Then("User should see the following Standard Questions on the Application Builder page", async function (this: PassportWorld, dataTable) {
  const questions = dataTable.hashes().map((row: Record<string, string>) => row.Question);
  const visible = await this.applicationCreationPage.verifyQuestionsVisible(questions);
  expect(visible).toBeTruthy();
});

Then("Checks on Save my responses to use in future applications on Passport", async function (this: PassportWorld) {
  await this.applicationCreationPage.checkSaveMyResponses();
});

Then("Applicant should see {string} and {string} buttons on the Preview Additional Questions section", async function (this: PassportWorld) {
  const visible = await this.applicationCreationPage.backAndSubmitButtonsVisible();
  expect(visible).toBeTruthy();
});

Then("Applicant should see all added Additional Questions on the Preview page", async function (this: PassportWorld, dataTable) {
  const rows = dataTable?.hashes?.() ?? [];
  const sourceRows = rows.length ? rows : [];
  const visible = await this.applicationCreationPage.allAddedAdditionalQuestionsVisible(sourceRows);
  expect(visible).toBeTruthy();
});

Then("Clicks on {string} button on the Preview Application page", async function (this: PassportWorld, buttonText: string) {
  await this.applicationCreationPage.clickBuilderButton(buttonText);
});

Then("User should hide all the following Standard Questions from the Application Builder page", async function (this: PassportWorld, dataTable) {
  const questions = dataTable.hashes().map((row: Record<string, string>) => row.Question);
  const hidden = await this.applicationCreationPage.hideStandardQuestions(questions);
  expect(hidden).toBeTruthy();
});

Then("User enters {string} into the {string} field of Additional New Question form", async function (this: PassportWorld, fieldValue: string, fieldName: string) {
  await this.applicationCreationPage.fillAdditionalQuestionField(fieldName, fieldValue);
});

When("User clicks on the {string} icon for the current Additional Question", async function (this: PassportWorld, iconName: string) {
  await this.applicationCreationPage.clickCurrentAdditionalQuestionIcon(iconName);
});

Then("User should see the Additional Question {string} duplicated in the UI", async function (this: PassportWorld, questionText: string) {
  const duplicated = await this.applicationCreationPage.additionalQuestionDuplicated(questionText);
  expect(duplicated).toBeTruthy();
});

When("User clicks on the first Edit Question icon", async function (this: PassportWorld) {
  await this.applicationCreationPage.clickFirstEditQuestionIcon();
});

Then("User should see the Delete Question popup", async function (this: PassportWorld) {
  const visible = await this.applicationCreationPage.deleteQuestionPopupVisible();
  expect(visible).toBeTruthy();
});

Then("User clicks on {string} in the Delete Question popup", async function (this: PassportWorld, buttonText: string) {
  await this.applicationCreationPage.clickDeletePopupButton(buttonText);
});

Then("User should remain in the current New Question form", async function (this: PassportWorld) {
  const visible = await this.applicationCreationPage.currentNewQuestionFormVisible();
  expect(visible).toBeTruthy();
});

Then("User should not see the New Question form displayed", async function (this: PassportWorld) {
  const removed = await this.applicationCreationPage.newQuestionFormRemoved();
  expect(removed).toBeTruthy();
});

Then("User clicks on the toggle switch to Allow comments for this criterion", async function (this: PassportWorld) {
  await this.applicationCreationPage.clickRubricAllowCommentsToggle();
});

Then("User clicks on Required Question checkbox and Save button", async function (this: PassportWorld) {
  await this.applicationCreationPage.clickRubricRequiredQuestionCheckboxAndSave();
});
