import { expect, type Locator, type Page } from "@playwright/test";
import path from "path";
import { BasePage } from "./base.page";

export class ApplicationCreationPage extends BasePage {
  constructor(page: Page) {
    super(page);
  }

  field(fieldName: string): Locator {
    if (fieldName.toLowerCase().includes("application title")) {
      return this.page.getByLabel(/application title/i).or(this.page.locator("label").filter({ hasText: /application title/i }).locator("xpath=following::input[1]")).first();
    }

    return this.page.getByLabel(new RegExp(fieldName, "i")).first();
  }

  async fillCreateApplicationField(fieldName: string, value: string): Promise<string> {
    const field = this.field(fieldName);
    await field.click();
    await field.fill(value);
    return await field.inputValue();
  }

  async selectOption(optionName: string): Promise<boolean> {
    const option = this.page.getByText(new RegExp(optionName, "i")).first();
    await option.click();
    return true;
  }

  async builderTextVisible(text: string): Promise<void> {
    await expect(this.page.getByText(text, { exact: false }).first()).toBeVisible();
  }

  async clickCollapseIcon(): Promise<void> {
    await this.page.locator(".collapse-btn").first().click();
  }

  async standardQuestionsCollapsed(): Promise<boolean> {
    const questionPreview = this.page.locator(".FormBuilderCategory .question-preview-container").first();
    return !(await questionPreview.isVisible().catch(() => false));
  }

  async scrollToSection(sectionName: string): Promise<void> {
    const section = this.page.getByText(sectionName, { exact: false }).first();
    await section.scrollIntoViewIfNeeded();
  }

  async clickAdditionalQuestionButton(buttonText: string): Promise<void> {
    const category = this.page.locator("xpath=//*[normalize-space()='Additional Questions']/ancestor::div[contains(@class,'FormBuilderCategory')][1]").first();
    await category.getByRole("button", { name: new RegExp(buttonText, "i") }).first().click();
  }

  async newQuestionFormVisible(): Promise<boolean> {
    return await this.page.locator("input[placeholder='Write Question Here']").first().isVisible().catch(() => false);
  }

  async additionalQuestionValidationMessage(): Promise<string> {
    return (await this.page.locator(".input-error-message-container, .ulp-input-error-message").first().textContent())?.trim() ?? "";
  }

  async additionalQuestionSaveDisabled(): Promise<boolean> {
    const button = this.page.getByRole("button", { name: /save/i }).last();
    const disabled = await button.getAttribute("disabled");
    return disabled !== null || !(await button.isEnabled());
  }

  async clickBuilderButton(buttonText: string): Promise<void> {
    await this.page.getByRole("button", { name: new RegExp(buttonText, "i") }).first().click();
  }

  async fillAdditionalQuestionsForm(rows: Array<Record<string, string>>): Promise<number> {
    let added = 0;

    for (const row of rows) {
      const questionInput = this.page.locator("input[placeholder='Write Question Here']").first();
      await questionInput.fill(row.Question ?? "");

      const editor = this.page.locator(".question-rich-text-editor .ql-editor[contenteditable='true']").first();
      await editor.click();
      await editor.fill(row.Instruction ?? "").catch(async () => {
        await editor.type(row.Instruction ?? "");
      });

      const answerType = this.page.locator("select[name='filterByTagTag'], select[aria-label='filterByTagTag']").first();
      if (await answerType.isVisible().catch(() => false)) {
        await answerType.selectOption({ label: row.Type }).catch(async () => {
          const options = await answerType.locator("option").allTextContents();
          const match = options.find(option => option.trim().toLowerCase() === row.Type.toLowerCase());
          if (match) {
            await answerType.selectOption({ label: match });
          }
        });
      }

      if (/single choice|multiple choice/i.test(row.Type ?? "")) {
        for (const optionText of ["Option 1", "Option 2", "Option 3"]) {
          const addOptionButton = this.page.getByRole("button", { name: /add/i }).last();
          if (!(await addOptionButton.isVisible().catch(() => false))) break;
          await addOptionButton.click();
          const optionInputs = this.page.locator(".choice-options-container input");
          const count = await optionInputs.count();
          const input = optionInputs.nth(Math.max(0, count - 1));
          await input.fill(optionText);
          if (/single choice/i.test(row.Type ?? "") && optionText === "Option 2") break;
        }
      }

      if (/file/i.test(row.Type ?? "")) {
        const fileTypeSelect = this.page.locator("label").filter({ hasText: /select file type/i }).locator("xpath=following::select[1]").first();
        if (await fileTypeSelect.isVisible().catch(() => false)) {
          await fileTypeSelect.selectOption({ label: "PDF" }).catch(() => undefined);
        }
      }

      const requiredCheckbox = this.page.locator("input.required-question-input").first();
      if ((row.Required ?? "").toLowerCase() === "yes" && await requiredCheckbox.isVisible().catch(() => false)) {
        if (!(await requiredCheckbox.isChecked().catch(() => false))) {
          await requiredCheckbox.check({ force: true }).catch(async () => {
            await requiredCheckbox.click();
          });
        }
      }

      const saveButton = this.page.getByRole("button", { name: /^save$/i }).last();
      await saveButton.click();
      added += 1;

      const addNewButton = this.page.getByRole("button", { name: /add new question/i }).last();
      if (await addNewButton.isVisible().catch(() => false) && added < rows.length) {
        await addNewButton.click();
      }
    }

    return added;
  }

  async rubricSectionVisible(): Promise<boolean> {
    return await this.page.getByText(/customize the criteria your colleagues will use/i).first().isVisible().catch(() => false);
  }

  async clickFirstRubricEditQuestionIcon(): Promise<void> {
    await this.page.locator("button.edit-question-btn").last().click();
  }

  async removeDefaultRubricQuestion(): Promise<void> {
    const removeButton = this.page.getByRole("button", { name: /remove question/i }).first();
    if (await removeButton.isVisible().catch(() => false)) {
      await removeButton.click();
    }
  }

  async rubricValidationMessage(): Promise<string> {
    return (await this.page.locator(".input-error-message-container").first().textContent())?.trim() ?? "";
  }

  async rubricButtonDisabled(buttonText: string): Promise<boolean> {
    const button = this.page.getByRole("button", { name: new RegExp(buttonText, "i") }).first();
    const disabled = await button.getAttribute("disabled");
    return disabled !== null || !(await button.isEnabled());
  }

  async setPublishOpenDateToNow(): Promise<void> {
    const input = this.page.locator("#date-time-start").first();
    const now = new Date();
    const pad = (value: number) => value.toString().padStart(2, "0");
    const formatted = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}T${pad(now.getHours())}:${pad(now.getMinutes())}`;
    await input.fill(formatted);
  }

  async copyApplicationLink(): Promise<void> {
    await this.page.getByRole("button", { name: /copy application link/i }).click();
  }

  async addRandomInviteEmail(): Promise<string> {
    const email = `invite.${Date.now()}@test.com`;
    await this.page.locator("#email-add").fill(email);
    await this.page.getByRole("button", { name: /add/i }).click();
    await expect(this.page.getByText(email, { exact: false })).toBeVisible();
    return email;
  }

  async deleteInviteEmail(email: string): Promise<void> {
    const row = this.page.locator("tr").filter({ hasText: email }).first();
    await row.locator(".table-action-remove").click();
    await expect(this.page.getByText(email, { exact: false })).not.toBeVisible();
  }

  async previewPageVisible(): Promise<boolean> {
    return await this.page.locator("div.ApplicantFormOV").first().isVisible().catch(() => false);
  }

  async standardQuestionsTabActive(): Promise<boolean> {
    return await this.page.locator(".active .tab-text").filter({ hasText: /standard questions/i }).first().isVisible().catch(() => false);
  }

  async additionalQuestionsTabActive(): Promise<boolean> {
    return await this.page.locator(".active .tab-text").filter({ hasText: /additional questions/i }).first().isVisible().catch(() => false);
  }

  async verifyQuestionsVisible(questions: string[]): Promise<boolean> {
    for (const question of questions) {
      if (!(await this.page.getByText(question, { exact: false }).first().isVisible().catch(() => false))) {
        return false;
      }
    }
    return true;
  }

  async checkSaveMyResponses(): Promise<void> {
    const checkbox = this.page.locator("xpath=//span[contains(normalize-space(),'Save my responses to use in future applications on Passport')]/preceding::input[@type='checkbox'][1]").first();
    if (!(await checkbox.isChecked().catch(() => false))) {
      await checkbox.check({ force: true }).catch(async () => {
        await checkbox.click();
      });
    }
  }

  async backAndSubmitButtonsVisible(): Promise<boolean> {
    const back = this.page.getByRole("button", { name: /^back$/i }).first();
    const submit = this.page.getByRole("button", { name: /^submit$/i }).first();
    return await back.isVisible().catch(() => false) && await submit.isVisible().catch(() => false);
  }

  async allAddedAdditionalQuestionsVisible(rows: Array<Record<string, string>>): Promise<boolean> {
    return this.verifyQuestionsVisible(rows.map(row => row.Question));
  }

  async uploadDefaultPitchDeckIfVisible(): Promise<void> {
    const fileInput = this.page.locator("input[type='file']").first();
    if (await fileInput.isVisible().catch(() => false)) {
      const filePath = path.resolve(__dirname, "..", "..", "..", "src", "test", "resources", "test-data", "pitchdeck.jpg");
      await fileInput.setInputFiles(filePath);
    }
  }

  async hideStandardQuestions(questions: string[]): Promise<boolean> {
    for (const question of questions) {
      const block = this.page.locator(".list-group-item.standard").filter({ hasText: question }).first();
      if (!(await block.isVisible().catch(() => false))) {
        return false;
      }

      const toggle = block.locator(".is-hidden-option-container, svg[data-icon='eye'], svg[data-icon='eye-slash']").first();
      if (await toggle.isVisible().catch(() => false)) {
        await toggle.click();
      }
    }
    return true;
  }

  async fillAdditionalQuestionField(fieldName: string, value: string): Promise<void> {
    if (/question/i.test(fieldName)) {
      await this.page.locator("input[placeholder='Write Question Here']").first().fill(value);
      return;
    }

    const field = this.page.getByLabel(new RegExp(fieldName, "i")).first();
    if (await field.isVisible().catch(() => false)) {
      await field.fill(value);
    }
  }

  async clickCurrentAdditionalQuestionIcon(iconName: string): Promise<void> {
    const iconKey = /copy/i.test(iconName) ? "copy" : "trash-can";
    await this.page.locator(`button.circle-button svg[data-icon='${iconKey}']`).first().locator("xpath=ancestor::button[1]").click();
  }

  async additionalQuestionDuplicated(questionText: string): Promise<boolean> {
    const labels = this.page.locator(".list-group-item .label").filter({ hasText: questionText });
    return (await labels.count()) >= 2;
  }

  async clickFirstEditQuestionIcon(): Promise<void> {
    await this.page.locator("button.edit-question-btn").first().click();
  }

  async deleteQuestionPopupVisible(): Promise<boolean> {
    return await this.page.locator(".form-build-modal-question-v2").first().isVisible().catch(() => false);
  }

  async clickDeletePopupButton(buttonText: string): Promise<void> {
    await this.page.getByRole("button", { name: new RegExp(buttonText, "i") }).click();
  }

  async currentNewQuestionFormVisible(): Promise<boolean> {
    return await this.page.locator("input[placeholder='Write Question Here']").first().isVisible().catch(() => false);
  }

  async newQuestionFormRemoved(): Promise<boolean> {
    return !(await this.currentNewQuestionFormVisible());
  }

  async clickRubricAllowCommentsToggle(): Promise<void> {
    await this.page.locator(".rubric-comments-field-container .TToggleSwitch, .rubric-comments-field-container .t-toggle-switch").first().click();
  }

  async clickRubricRequiredQuestionCheckboxAndSave(): Promise<void> {
    const checkbox = this.page.locator("input.required-question-input").first();
    if (!(await checkbox.isChecked().catch(() => false))) {
      await checkbox.check({ force: true }).catch(async () => {
        await checkbox.click();
      });
    }
    await this.page.getByRole("button", { name: /^save$/i }).first().click();
  }
}
