import fs from "fs/promises";
import path from "path";
import { expect, type Locator, type Page } from "@playwright/test";
import { BasePage } from "./base.page";

export class StartupProgramApplicationPage extends BasePage {
  constructor(page: Page) {
    super(page);
  }

  async searchForProgram(targetProgramTitle: string): Promise<void> {
    const searchInput = this.page.locator("input[placeholder='Search']").first();
    await searchInput.click();
    await searchInput.fill(targetProgramTitle);
    await searchInput.press("Enter");
  }

  async openMatchingProgramDetails(targetProgramTitle: string): Promise<void> {
    const card = this.page.locator(".ProgramSearchCard").filter({ hasText: targetProgramTitle }).first();
    await expect(card).toBeVisible();
    await card.getByRole("link", { name: /see more/i }).click();
  }

  async clickApplyNow(): Promise<void> {
    const applyNow = this.page.locator("a.cohort-action-btn").first();
    await applyNow.click();
  }

  async switchToNewApplicationTab(): Promise<Page> {
    const pages = this.page.context().pages();
    const latestPage = pages[pages.length - 1];
    await latestPage.waitForLoadState("domcontentloaded");
    return latestPage;
  }

  async verifyProgramTitleOnApplicationPage(targetProgramTitle: string): Promise<void> {
    await expect(this.page.getByText(targetProgramTitle, { exact: false }).first()).toBeVisible();
  }

  fieldByLabel(label: string): Locator {
    const labelRegex = new RegExp(label, "i");
    return this.page.getByLabel(labelRegex)
      .or(this.page.locator("label").filter({ hasText: labelRegex }).locator("xpath=following::input[1]"))
      .first();
  }

  editorByLabel(label: string): Locator {
    const labelRegex = new RegExp(label, "i");
    return this.page.locator("label").filter({ hasText: labelRegex }).locator("xpath=following::div[contains(@class,'ql-editor') and @contenteditable='true'][1]").first();
  }

  async fillApplicationForm(data: { location: string; websiteSuffix: string; linkedInSuffix: string; email: string; emailLocalPart: string; }): Promise<void> {
    await this.fieldByLabel("Job Title").fill(data.emailLocalPart);

    const locationInput = this.fieldByLabel("Startup Location");
    await locationInput.fill(data.location);
    const firstSuggestion = this.page.locator(".pac-container .pac-item").first();
    if (await firstSuggestion.isVisible().catch(() => false)) {
      await firstSuggestion.click();
    }

    await this.selectFirstDropdownOption("Industries");

    await this.appendToInput("Website", data.websiteSuffix);
    await this.appendToInput("LinkedIn", data.linkedInSuffix);

    const descriptionEditor = this.editorByLabel("Company Description");
    await descriptionEditor.click();
    await descriptionEditor.fill(data.email).catch(async () => {
      await descriptionEditor.type(data.email);
    });

    await this.fieldByLabel("Year of Founding").fill("3");
    await this.selectFirstDropdownOption("Funding");
    await this.fieldByLabel("Number of full-time employees").fill("3");

    const milestonesEditor = this.editorByLabel("Milestones");
    await milestonesEditor.click();
    await milestonesEditor.fill(data.email).catch(async () => {
      await milestonesEditor.type(data.email);
    });
  }

  private async appendToInput(label: string, suffix: string): Promise<void> {
    const input = this.fieldByLabel(label);
    const currentValue = await input.inputValue().catch(() => "");
    await input.click();
    await input.press("End");
    await input.type(suffix);
    await expect(input).toHaveValue(`${currentValue}${suffix}`);
  }

  private async selectFirstDropdownOption(label: string): Promise<void> {
    const trigger = this.page.locator("label").filter({ hasText: new RegExp(label, "i") })
      .locator("xpath=following::*[(self::div or self::input) and (contains(@class,'dropdown') or contains(@class,'select') or contains(@class,'searchBox') or @role='combobox')][1]")
      .first();

    await trigger.click();
    const option = this.page.locator("div.multiselect__content-wrapper li.multiselect__element span.multiselect__option, ul.dropdown-list li").first();
    await option.click();
    await this.page.keyboard.press("Escape").catch(() => undefined);
  }

  async uploadPitchDeck(): Promise<void> {
    const fileInput = this.page.locator("input[type='file'][aria-label='Pitch Deck']").first();
    const filePath = path.resolve(__dirname, "..", "..", "..", "src", "test", "resources", "test-data", "pitchdeck.jpg");
    await fileInput.setInputFiles(filePath);
  }

  async checkSaveResponsesCheckbox(): Promise<void> {
    const checkbox = this.page.locator("xpath=//span[normalize-space()='Save my responses to use in future applications on Passport']/preceding::input[@type='checkbox'][1]").first();
    if (!(await checkbox.isChecked().catch(() => false))) {
      await checkbox.check({ force: true }).catch(async () => {
        await checkbox.click();
      });
    }
  }

  async clickButton(buttonText: string): Promise<void> {
    const target = this.page.getByRole("button", { name: new RegExp(buttonText, "i") })
      .or(this.page.getByRole("link", { name: new RegExp(buttonText, "i") }))
      .first();
    await target.click();
  }

  async successMessageVisible(): Promise<void> {
    await expect(this.page.getByText("Thank you for submitting your application!", { exact: false })).toBeVisible();
  }

  async saveGeneratedCredentials(resultPath: string, targetProgramTitle: string, values: Map<string, string>): Promise<void> {
    const email = values.get("newEmail") ?? "";
    const password = values.get("newPassword") ?? "";
    const fullName = values.get("fullName") ?? "";
    const startupCompanyName = values.get("startupCompanyName") ?? "";

    const lines = [
      `targetProgramTitle=${targetProgramTitle}`,
      `email=${email}`,
      `password=${password}`,
      `fullName=${fullName}`,
      `startupCompanyName=${startupCompanyName}`,
      `savedAt=${new Date().toISOString()}`,
      ""
    ].join("\n");

    const absolutePath = path.resolve(__dirname, "..", "..", "..", resultPath);
    await fs.mkdir(path.dirname(absolutePath), { recursive: true });
    await fs.appendFile(absolutePath, lines, "utf8");
  }
}
