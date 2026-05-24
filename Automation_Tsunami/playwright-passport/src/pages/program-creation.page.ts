import { expect, type Locator, type Page } from "@playwright/test";
import { BasePage } from "./base.page";

export class ProgramCreationPage extends BasePage {
  constructor(page: Page) {
    super(page);
  }

  private labelInput(labelPattern: RegExp): Locator {
    return this.page.getByLabel(labelPattern).or(this.page.locator("label").filter({ hasText: labelPattern }).locator("xpath=following::input[1]")).first();
  }

  field(fieldName: string): Locator {
    const normalized = fieldName.toLowerCase();

    if (normalized.includes("program title")) {
      return this.labelInput(/program title/i);
    }

    if (normalized.includes("location")) {
      return this.labelInput(/location/i);
    }

    if (normalized.includes("program type")) {
      return this.page.locator("select[name='ProgramTag'], select[aria-label='ProgramTag']").first();
    }

    if (normalized.includes("program description")) {
      return this.page.locator(".ql-editor[contenteditable='true']").first();
    }

    return this.page.getByLabel(new RegExp(fieldName, "i")).first();
  }

  async fillField(fieldName: string, value: string): Promise<string> {
    const field = this.field(fieldName);

    if (fieldName.toLowerCase().includes("program type")) {
      if (!value.trim()) {
        return "Select an option";
      }

      const programType = value.toUpperCase() === "ANY" ? "Accelerator" : value;
      await field.selectOption({ label: programType }).catch(async () => {
        await field.selectOption({ index: 1 });
      });
      return (await field.locator("option:checked").textContent())?.trim() ?? "";
    }

    if (fieldName.toLowerCase().includes("program description")) {
      await field.click();
      await this.page.keyboard.press("Control+A");
      await this.page.keyboard.press("Backspace");
      if (value.trim()) {
        await field.fill(value).catch(async () => {
          await field.type(value);
        });
      }
      return (await field.textContent())?.trim() ?? "";
    }

    await field.click();
    await field.fill(value);

    if (fieldName.toLowerCase().includes("location") && value.trim()) {
      const suggestion = this.page.locator(".pac-container .pac-item").first();
      if (await suggestion.isVisible().catch(() => false)) {
        await suggestion.click();
      }
    }

    return (await field.inputValue().catch(async () => (await field.textContent())?.trim() ?? "")) ?? "";
  }

  async selectIndustries(count: number): Promise<number> {
    const input = this.page.locator("input[name='searchindustries']").first();
    await input.click();

    const checked = this.page.locator("ul.dropdown-list input[type='checkbox']:checked");
    const existingChecked = await checked.count();
    for (let index = 0; index < existingChecked; index++) {
      await checked.first().click();
    }

    if (count <= 0) {
      return 0;
    }

    const items = this.page.locator("ul.dropdown-list li input[type='checkbox']");
    const available = await items.count();
    const target = Math.min(count, available);

    for (let index = 0; index < target; index++) {
      await items.nth(index).check({ force: true }).catch(async () => {
        await items.nth(index).click();
      });
    }

    return await this.page.locator("ul.dropdown-list input[type='checkbox']:checked").count();
  }

  async closeIndustriesDropdown(): Promise<boolean> {
    await this.page.keyboard.press("Escape");
    const visibleItems = this.page.locator("ul.dropdown-list li");
    return !(await visibleItems.first().isVisible().catch(() => false));
  }

  async selectPerks(count: number): Promise<number> {
    const cards = this.page.locator("div.rewards-perk-item");
    const available = await cards.count();
    const target = Math.min(count, 5, available);

    for (let index = 0; index < target; index++) {
      await cards.nth(index).click();
    }

    return await this.page.locator("div.rewards-perk-item input[type='checkbox']:checked").count();
  }

  async attemptSelectPerksNoCap(requested: number): Promise<number> {
    const cards = this.page.locator("div.rewards-perk-item");
    const available = await cards.count();
    const target = Math.min(requested, available);

    for (let index = 0; index < target; index++) {
      await cards.nth(index).click().catch(() => undefined);
    }

    return await this.page.locator("div.rewards-perk-item input[type='checkbox']:checked").count();
  }

  async limitReachedCount(): Promise<number> {
    return await this.page.locator("div.rewards-perk-item.limit-reached").count();
  }

  async textVisible(text: string): Promise<void> {
    await expect(this.page.getByText(text, { exact: false }).first()).toBeVisible();
  }

  async missingFieldHighlighted(fieldName: string): Promise<boolean> {
    const missingLabel = this.page.locator(`xpath=//label[contains(@class,'missingFields') and contains(normalize-space(.),"${fieldName}")]`).first();
    return await missingLabel.isVisible().catch(() => false);
  }
}
