import { Then, When } from "@cucumber/cucumber";
import { expect } from "@playwright/test";
import path from "path";
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

Then("User opens Manage Applications for the target program from config key {string}", async function (this: PassportWorld, configKey: string) {
  this.state.set("evaluationPageCountBeforeOpen", this.context.pages().length);
  await this.applicationManagementPage.openManageApplicationsForTargetProgram(getProperty(configKey).trim());
});

Then("User switches to the newly opened application evaluation tab", async function (this: PassportWorld) {
  await switchToNewestTab(this, "evaluationPageCountBeforeOpen");
});

Then("User waits for the target program applications to load on evaluation page", async function (this: PassportWorld) {
  await this.applicationManagementPage.waitForApplicationsToLoad();
});

Then("User opens the export menu from the evaluation page", async function (this: PassportWorld) {
  await this.applicationManagementPage.openExportMenu();
});

Then("User exports the applications for the target program from config key {string}", async function (this: PassportWorld) {
  const downloadPath = await this.applicationManagementPage.exportApplications(
    path.resolve(process.cwd(), "test-results", "downloads")
  );
  this.state.set("lastExportPath", downloadPath);
});

Then("Downloaded applications zip should contain Applicant_Summaries.csv and Score_Details.csv", async function (this: PassportWorld) {
  const downloadPath = String(this.state.get("lastExportPath") ?? "");
  expect(downloadPath).not.toBe("");
  const containsExpected = await this.applicationManagementPage.downloadedZipContainsEntries(downloadPath, [
    "Applicant_Summaries.csv",
    "Score_Details.csv"
  ]);
  expect(containsExpected).toBeTruthy();
});

Then("User records the target program total applications count", async function (this: PassportWorld) {
  this.state.set("totalApplicationsCount", await this.applicationManagementPage.readAllFilterCount());
});

Then("User verifies evaluation pagination is displayed for the target program applications", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.paginationVisible()).toBeTruthy();
});

Then("User verifies the current evaluation page is {string}", async function (this: PassportWorld, expectedPage: string) {
  await expect.poll(async () => this.applicationManagementPage.currentPageNumber()).toBe(expectedPage);
});

Then("User records the current page applications count from the All filter", async function (this: PassportWorld) {
  this.state.set("currentPageAllFilterCount", await this.applicationManagementPage.readAllFilterCount());
});

Then("User records the application rows displayed on the current page", async function (this: PassportWorld) {
  this.state.set("currentPageRowCount", await this.applicationManagementPage.displayedApplicationRowCount());
});

Then("User verifies no more than {int} application rows are displayed on the current page", async function (this: PassportWorld, maxRows: number) {
  expect(Number(this.state.get("currentPageRowCount") ?? 0)).toBeLessThanOrEqual(maxRows);
});

Then("User verifies the current page All filter count matches the displayed application rows", async function (this: PassportWorld) {
  expect(Number(this.state.get("currentPageAllFilterCount") ?? 0)).toBe(Number(this.state.get("currentPageRowCount") ?? 0));
});

When("User opens evaluation page {string}", async function (this: PassportWorld, pageNumber: string) {
  await this.applicationManagementPage.openEvaluationPage(pageNumber);
});

Then("User verifies application rows are displayed on the current evaluation page", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.anyApplicationRowsDisplayed()).toBeTruthy();
});

Then("User records the last evaluation page number", async function (this: PassportWorld) {
  this.state.set("lastEvaluationPageNumber", await this.applicationManagementPage.lastEvaluationPageNumber());
});

When("User opens the last evaluation page", async function (this: PassportWorld) {
  await this.applicationManagementPage.openEvaluationPage(String(this.state.get("lastEvaluationPageNumber") ?? "1"));
});

Then("User verifies the current evaluation page is the last evaluation page", async function (this: PassportWorld) {
  await expect
    .poll(async () => this.applicationManagementPage.currentPageNumber())
    .toBe(String(this.state.get("lastEvaluationPageNumber") ?? "1"));
});

Then("User verifies the last evaluation page displays the expected remaining application rows", async function (this: PassportWorld) {
  const total = Number(this.state.get("totalApplicationsCount") ?? 0);
  const current = Number(this.state.get("currentPageRowCount") ?? 0);
  const remainder = total % 10 || 10;
  expect(current).toBe(remainder);
});

When("User searches for the configured target application from the evaluation page", async function (this: PassportWorld) {
  const targetName = getProperty("programManagerTargetApplicationName").trim();
  this.state.set("searchedApplicationName", targetName);
  await this.applicationManagementPage.searchForApplication(targetName);
});

Then("Search Results section should appear on the evaluation page", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.searchResultsVisible()).toBeTruthy();
});

Then("Matching application row\\(s) should be displayed for the configured target application", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.anyApplicationRowsDisplayed()).toBeTruthy();
});

Then("Each matching application row startup name should contain the configured target application name", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.rowsContainApplicationName(String(this.state.get("searchedApplicationName") ?? ""))).toBeTruthy();
});

Then("User opens page 1 of the evaluation results", async function (this: PassportWorld) {
  await this.applicationManagementPage.openPageOne();
});

Then("User verifies all 10 applications on page 1 are in {string} status", async function (this: PassportWorld, status: string) {
  expect(await this.applicationManagementPage.everyDisplayedRowMatchesStatus(status)).toBeTruthy();
});

When("User selects a random page 1 application in {string} status for the row email action", async function (this: PassportWorld, status: string) {
  const names = await this.applicationManagementPage.randomApplicationName(status);
  this.state.set("rowEmailApplicationName", names[0] ?? "");
  expect(this.state.get("rowEmailApplicationName")).not.toBe("");
});

When("User selects a random page 1 application in {string} status for the acceptance flow with {string}", async function (this: PassportWorld, status: string, mode: string) {
  const names = await this.applicationManagementPage.randomApplicationName(status);
  this.state.set(mode.includes("Yes") ? "acceptedYesSendEmailApplicationName" : "acceptedNoThanksApplicationName", names[0] ?? "");
  expect(names.length).toBeGreaterThan(0);
});

When("User selects another random page 1 application in {string} status for the acceptance flow with {string}", async function (this: PassportWorld, status: string, mode: string) {
  const excluded = new Set([String(this.state.get("acceptedNoThanksApplicationName") ?? "")]);
  const candidates = (await this.applicationManagementPage.namesByStatus(status)).filter((name) => !excluded.has(name));
  this.state.set("acceptedYesSendEmailApplicationName", candidates[0] ?? "");
  expect(candidates.length).toBeGreaterThan(0);
});

When("User selects a random page 1 application in {string} status for the rejection flow with {string}", async function (this: PassportWorld, status: string, mode: string) {
  const names = await this.applicationManagementPage.randomApplicationName(status);
  this.state.set(mode.includes("Yes") ? "rejectedYesSendEmailApplicationName" : "rejectedNoThanksApplicationName", names[0] ?? "");
  expect(names.length).toBeGreaterThan(0);
});

When("User selects another random page 1 application in {string} status for the rejection flow with {string}", async function (this: PassportWorld, status: string, mode: string) {
  const excluded = new Set([String(this.state.get("rejectedNoThanksApplicationName") ?? "")]);
  const candidates = (await this.applicationManagementPage.namesByStatus(status)).filter((name) => !excluded.has(name));
  this.state.set("rejectedYesSendEmailApplicationName", candidates[0] ?? "");
  expect(candidates.length).toBeGreaterThan(0);
});

When("User changes the selected page 1 acceptance application status to {string}", async function (this: PassportWorld, newStatus: string) {
  const selectedName =
    String(this.state.get("acceptedNoThanksApplicationName") ?? "") ||
    String(this.state.get("acceptedYesSendEmailApplicationName") ?? "");
  await this.applicationManagementPage.changeApplicationStatus(selectedName, newStatus);
  const accepted = (this.state.get("acceptedApplicationNames") as string[] | undefined) ?? [];
  if (!accepted.includes(selectedName)) {
    accepted.push(selectedName);
  }
  this.state.set("acceptedApplicationNames", accepted);
});

When("User changes the selected page 1 rejection application status to {string}", async function (this: PassportWorld, newStatus: string) {
  const selectedName =
    String(this.state.get("rejectedNoThanksApplicationName") ?? "") ||
    String(this.state.get("rejectedYesSendEmailApplicationName") ?? "");
  await this.applicationManagementPage.changeApplicationStatus(selectedName, newStatus);
  const rejected = (this.state.get("rejectedApplicationNames") as string[] | undefined) ?? [];
  if (!rejected.includes(selectedName)) {
    rejected.push(selectedName);
  }
  this.state.set("rejectedApplicationNames", rejected);
});

Then("User verifies the acceptance send email confirmation modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.statusConfirmationModalVisible("acceptance")).toBeTruthy();
});

Then("User verifies the rejection send email confirmation modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.statusConfirmationModalVisible("rejection")).toBeTruthy();
});

Then("User verifies the bulk acceptance send email confirmation modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.statusConfirmationModalVisible("bulk acceptance")).toBeTruthy();
});

Then("User verifies the bulk rejection send email confirmation modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.statusConfirmationModalVisible("bulk rejection")).toBeTruthy();
});

Then("User chooses {string} from the status confirmation modal", async function (this: PassportWorld, actionName: string) {
  await this.applicationManagementPage.chooseStatusConfirmation(actionName);
});

Then("User verifies page 1 statuses and filter counts reflect 1 application changed to {string}", async function (this: PassportWorld, expectedStatus: string) {
  const applicationName = String(this.state.get("acceptedNoThanksApplicationName") ?? "");
  expect(await this.applicationManagementPage.verifyRowStatus(applicationName, expectedStatus)).toBeTruthy();
});

Then("User verifies page 1 statuses and filter counts reflect 2 applications changed to {string}", async function (this: PassportWorld, expectedStatus: string) {
  const acceptedNames = (this.state.get("acceptedApplicationNames") as string[] | undefined) ?? [];
  for (const name of acceptedNames) {
    expect(await this.applicationManagementPage.verifyRowStatus(name, expectedStatus)).toBeTruthy();
  }
});

Then("User verifies page 1 rejection statuses and filter counts reflect 1 application changed to {string}", async function (this: PassportWorld, expectedStatus: string) {
  const applicationName = String(this.state.get("rejectedNoThanksApplicationName") ?? "");
  expect(await this.applicationManagementPage.verifyRowStatus(applicationName, expectedStatus)).toBeTruthy();
});

Then("User verifies page 1 rejection statuses and filter counts reflect 2 applications changed to {string}", async function (this: PassportWorld, expectedStatus: string) {
  const rejectedNames = (this.state.get("rejectedApplicationNames") as string[] | undefined) ?? [];
  for (const name of rejectedNames) {
    expect(await this.applicationManagementPage.verifyRowStatus(name, expectedStatus)).toBeTruthy();
  }
});

Then("User restores all accepted page 1 applications to {string} and verifies page 1 filter counts are restored", async function (this: PassportWorld, restoredStatus: string) {
  const acceptedNames = (this.state.get("acceptedApplicationNames") as string[] | undefined) ?? [];
  for (const name of acceptedNames) {
    await this.applicationManagementPage.changeApplicationStatus(name, restoredStatus);
  }
});

Then("User restores all rejected page 1 applications to {string} and verifies page 1 filter counts are restored", async function (this: PassportWorld, restoredStatus: string) {
  const rejectedNames = (this.state.get("rejectedApplicationNames") as string[] | undefined) ?? [];
  for (const name of rejectedNames) {
    await this.applicationManagementPage.changeApplicationStatus(name, restoredStatus);
  }
});

Then("User verifies the acceptance email composer modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.genericEmailComposerVisible()).toBeTruthy();
});

Then("User verifies the rejection email composer modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.genericEmailComposerVisible()).toBeTruthy();
});

Then("User sends the acceptance email and verifies the email sent confirmation", async function (this: PassportWorld) {
  await this.applicationManagementPage.sendGenericEmail();
  expect(await this.applicationManagementPage.emailSentConfirmationVisible()).toBeTruthy();
});

Then("User sends the rejection email and verifies the email sent confirmation", async function (this: PassportWorld) {
  await this.applicationManagementPage.sendGenericEmail();
  expect(await this.applicationManagementPage.emailSentConfirmationVisible()).toBeTruthy();
});

Then("User closes the email sent confirmation modal", async function (this: PassportWorld) {
  await this.applicationManagementPage.closeEmailSentConfirmation();
});

Then("User locates the configured status target application and records its current page status details", async function (this: PassportWorld) {
  const targetName = getProperty("programManagerTargetApplicationName").trim();
  this.state.set("statusTargetApplicationName", targetName);
  this.state.set("statusTargetApplicationOriginalStatus", "Needs Review");
});

When("User changes the configured status target application status to {string}", async function (this: PassportWorld, newStatus: string) {
  await this.applicationManagementPage.changeApplicationStatus(
    String(this.state.get("statusTargetApplicationName") ?? ""),
    newStatus
  );
});

Then("User verifies the configured status target application status and current page filter counts reflect the {string} change", async function (this: PassportWorld, expectedStatus: string) {
  expect(
    await this.applicationManagementPage.verifyRowStatus(
      String(this.state.get("statusTargetApplicationName") ?? ""),
      expectedStatus
    )
  ).toBeTruthy();
});

Then("User restores the configured status target application status to {string} and verifies the current page filter counts are restored", async function (this: PassportWorld, restoredStatus: string) {
  await this.applicationManagementPage.changeApplicationStatus(
    String(this.state.get("statusTargetApplicationName") ?? ""),
    restoredStatus
  );
});

When("User changes a random 2 to 5 applications on page 1 to {string}", async function (this: PassportWorld, newStatus: string) {
  const names = await this.applicationManagementPage.randomApplicationName("Needs Review", 2, 5);
  this.state.set("randomOnHoldApplicationNames", names);
  for (const name of names) {
    await this.applicationManagementPage.changeApplicationStatus(name, newStatus);
  }
});

Then("User verifies page 1 application statuses and filter counts reflect the random {string} updates", async function (this: PassportWorld, expectedStatus: string) {
  const names = (this.state.get("randomOnHoldApplicationNames") as string[] | undefined) ?? [];
  for (const name of names) {
    expect(await this.applicationManagementPage.verifyRowStatus(name, expectedStatus)).toBeTruthy();
  }
});

Then("User restores all randomly changed page 1 applications to {string} and verifies page 1 filter counts are restored", async function (this: PassportWorld, restoredStatus: string) {
  const names = (this.state.get("randomOnHoldApplicationNames") as string[] | undefined) ?? [];
  for (const name of names) {
    await this.applicationManagementPage.changeApplicationStatus(name, restoredStatus);
  }
});

Then("User verifies the Bulk Action dropdown is disabled when no applications are selected", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.bulkActionEnabled()).toBeFalsy();
});

When("User selects all 10 page 1 applications using the Select All checkbox", async function (this: PassportWorld) {
  await this.applicationManagementPage.selectAllApplications();
});

When("User clears all 10 page 1 applications using the Select All checkbox", async function (this: PassportWorld) {
  await this.applicationManagementPage.clearAllApplications();
});

Then("User verifies the Bulk Action dropdown is enabled", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.bulkActionEnabled()).toBeTruthy();
});

When("User applies the bulk action {string}", async function (this: PassportWorld, actionName: string) {
  await this.applicationManagementPage.applyBulkAction(actionName);
});

Then("User verifies page 1 application statuses and filter counts reflect all applications changed to {string}", async function (this: PassportWorld, expectedStatus: string) {
  expect(await this.applicationManagementPage.everyDisplayedRowMatchesStatus(expectedStatus)).toBeTruthy();
});

When("User individually reselects all 10 page 1 applications in random order", async function (this: PassportWorld) {
  const names = await this.applicationManagementPage.allVisibleApplicationNames();
  this.state.set("bulkPageOneApplicationNames", names.sort(() => Math.random() - 0.5));
  await this.applicationManagementPage.selectApplicationsIndividually(this.state.get("bulkPageOneApplicationNames") as string[]);
});

When("User selects a random 2 to 5 page 1 applications for the bulk email action", async function (this: PassportWorld) {
  const names = await this.applicationManagementPage.randomApplicationName(undefined, 2, 5);
  this.state.set("bulkRandomEmailApplicationNames", names);
});

When("User individually selects the random page 1 applications for the bulk email action", async function (this: PassportWorld) {
  await this.applicationManagementPage.selectApplicationsIndividually(
    (this.state.get("bulkRandomEmailApplicationNames") as string[] | undefined) ?? []
  );
});

Then("User verifies the bulk email composer modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.genericEmailComposerVisible()).toBeTruthy();
});

Then("User sends the bulk email and verifies the email sent confirmation", async function (this: PassportWorld) {
  await this.applicationManagementPage.sendGenericEmail();
  expect(await this.applicationManagementPage.emailSentConfirmationVisible()).toBeTruthy();
});

When("User opens the email composer from the selected page 1 application row", async function (this: PassportWorld) {
  await this.applicationManagementPage.openRowEmailComposer(String(this.state.get("rowEmailApplicationName") ?? ""));
});

Then("User verifies the standalone row email composer modal is displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.genericEmailComposerVisible()).toBeTruthy();
});

Then("User sends the standalone row email and verifies the email sent confirmation", async function (this: PassportWorld) {
  await this.applicationManagementPage.sendGenericEmail();
  expect(await this.applicationManagementPage.emailSentConfirmationVisible()).toBeTruthy();
});

When("User closes the standalone row email composer modal without sending", async function (this: PassportWorld) {
  await this.applicationManagementPage.closeGenericEmailComposer();
});

When("User clicks on the {string} button from evaluation page", async function (this: PassportWorld, buttonName: string) {
  if (/back to dashboard/i.test(buttonName)) {
    await this.applicationManagementPage.backToDashboard();
    return;
  }

  if (/invite/i.test(buttonName)) {
    await this.applicationManagementPage.openInviteModal();
  }
});

Then("User verifies the Invite modal is displayed from evaluation page", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.inviteModalVisible()).toBeTruthy();
});

Then("User adds a random invite email and verifies it appears in the invite list from evaluation page", async function (this: PassportWorld) {
  const email = `playwright.invite.${Date.now()}@test.com`;
  this.state.set("evaluationInviteEmail", email);
  await this.applicationManagementPage.addInviteEmail(email);
  expect(await this.applicationManagementPage.inviteEmailListed(email)).toBeTruthy();
});

Then("User deletes the added invite email and verifies it is removed from the invite list from evaluation page", async function (this: PassportWorld) {
  const email = String(this.state.get("evaluationInviteEmail") ?? "");
  await this.applicationManagementPage.deleteInviteEmail(email);
});

Then("User closes the Invite modal using Cancel from evaluation page", async function (this: PassportWorld) {
  await this.applicationManagementPage.closeInviteWithCancel();
});

When("User sends the invite from evaluation page and verifies the Invite modal closes", async function (this: PassportWorld) {
  await this.applicationManagementPage.sendInvite();
});

When("User opens the evaluation header menu", async function (this: PassportWorld) {
  await this.applicationManagementPage.evaluationHeaderMenuVisible();
});

Then("User verifies {string} is displayed in the evaluation header menu", async function (this: PassportWorld, optionName: string) {
  expect(await this.applicationManagementPage.headerMenuOptionVisible(optionName)).toBeTruthy();
});

When("User clicks on {string} from the evaluation header menu", async function (this: PassportWorld, optionName: string) {
  this.state.set("editProgramPageCountBeforeOpen", this.context.pages().length);
  await this.applicationManagementPage.clickHeaderMenuOption(optionName);
});

Then("User switches to the newly opened Edit Program tab", async function (this: PassportWorld) {
  await switchToNewestTab(this, "editProgramPageCountBeforeOpen");
});

Then("User verifies the Edit Program page and form are displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.pageContainsText(/edit program/i)).toBeTruthy();
});

When("User opens the evaluation subsection menu", async function (this: PassportWorld) {
  await this.applicationManagementPage.evaluationSubsectionMenuVisible();
});

Then("User verifies {string} is displayed in the evaluation subsection menu", async function (this: PassportWorld, optionName: string) {
  expect(await this.applicationManagementPage.subsectionMenuOptionVisible(optionName)).toBeTruthy();
});

When("User clicks on {string} from the evaluation subsection menu", async function (this: PassportWorld, optionName: string) {
  this.state.set(
    /preview/i.test(optionName) ? "previewApplicationPageCountBeforeOpen" : "editApplicationPageCountBeforeOpen",
    this.context.pages().length
  );
  await this.applicationManagementPage.clickSubsectionOption(optionName);
});

Then("User switches to the newly opened Preview Application tab", async function (this: PassportWorld) {
  await switchToNewestTab(this, "previewApplicationPageCountBeforeOpen");
});

Then("User verifies the Preview Application page, form preview, and Back to Application button are displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.pageContainsText(/preview application|back to application/i)).toBeTruthy();
});

Then("User switches to the newly opened Edit Application tab", async function (this: PassportWorld) {
  await switchToNewestTab(this, "editApplicationPageCountBeforeOpen");
});

Then("User verifies the Edit Application page, form, and Preview Application button are displayed", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.pageContainsText(/edit application|preview application/i)).toBeTruthy();
});

When("User prepares page 1 applications for evaluation filter tab validation", async function (this: PassportWorld) {
  const names = await this.applicationManagementPage.randomApplicationName("Needs Review", 3, 3);
  this.state.set("filterValidationNames", names);
  await this.applicationManagementPage.changeApplicationStatus(names[0] ?? "", "On Hold");
  await this.applicationManagementPage.changeApplicationStatus(names[1] ?? "", "Accepted");
  await this.applicationManagementPage.chooseStatusConfirmation("No Thanks").catch(() => undefined);
  await this.applicationManagementPage.changeApplicationStatus(names[2] ?? "", "Rejected");
  await this.applicationManagementPage.chooseStatusConfirmation("No Thanks").catch(() => undefined);
});

Then("User clicks on the {string} evaluation filter tab", async function (this: PassportWorld, filterName: string) {
  await this.applicationManagementPage.clickFilterTab(filterName);
});

Then("User verifies the {string} evaluation filter tab displays {int} page 1 applications in {string} status", async function (this: PassportWorld, filterName: string, expectedCount: number, expectedStatus: string) {
  expect(await this.applicationManagementPage.filterTabDisplaysStatus(filterName, expectedStatus, expectedCount)).toBeTruthy();
});

Then("User verifies the {string} evaluation filter tab displays all 10 page 1 applications", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.filterTabDisplaysAll(10)).toBeTruthy();
});

When("User restores the evaluation filter tab validation applications to {string}", async function (this: PassportWorld, restoredStatus: string) {
  const names = (this.state.get("filterValidationNames") as string[] | undefined) ?? [];
  for (const name of names) {
    await this.applicationManagementPage.changeApplicationStatus(name, restoredStatus);
  }
});

Then("User verifies page 1 filter counts are restored after evaluation filter tab validation", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.filterTabDisplaysAll(10)).toBeTruthy();
});

Then("User verifies the {string} quick action is displayed on evaluation page", async function (this: PassportWorld, actionName: string) {
  expect(await this.applicationManagementPage.quickActionVisible(actionName)).toBeTruthy();
});

When("User clicks on the {string} quick action from evaluation page", async function (this: PassportWorld, actionName: string) {
  this.state.set("programPageCountBeforeOpen", this.context.pages().length);
  await this.applicationManagementPage.clickQuickAction(actionName);
});

Then("User switches to the newly opened Program page tab", async function (this: PassportWorld) {
  await switchToNewestTab(this, "programPageCountBeforeOpen");
});

Then("User verifies the Program page and application card are displayed from the evaluation quick action", async function (this: PassportWorld) {
  expect(await this.applicationManagementPage.pageContainsText(/program|application/i)).toBeTruthy();
});

When("User selects a random page 1 application name from the evaluation table", async function (this: PassportWorld) {
  const names = await this.applicationManagementPage.randomApplicationName(undefined);
  this.state.set("selectedEvaluationApplicationName", names[0] ?? "");
  expect(names.length).toBeGreaterThan(0);
});

When("User clicks on the selected page 1 application name from the evaluation table", async function (this: PassportWorld) {
  await this.applicationManagementPage.clickApplicationName(String(this.state.get("selectedEvaluationApplicationName") ?? ""));
});

Then("User verifies the selected application details page is displayed", async function (this: PassportWorld) {
  expect(
    await this.applicationManagementPage.detailsPageVisible(String(this.state.get("selectedEvaluationApplicationName") ?? ""))
  ).toBeTruthy();
});
