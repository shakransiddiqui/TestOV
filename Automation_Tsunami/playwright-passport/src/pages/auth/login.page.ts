import { type Locator, type Page } from "@playwright/test";
import { BasePage } from "../base.page";
import { testData } from "../../config/test-data";

export class LoginPage extends BasePage {
  constructor(page: Page) {
    super(page);
  }

  async goto(): Promise<void> {
    await this.open("/login");
    await this.expectTitle(testData.titles.login);
  }

  async signInAsProgramManager(): Promise<void> {
    await this.fill(this.page.locator("input[type='email']").first(), testData.credentials.programManager.email);
    await this.fill(this.page.locator("input[type='password']").first(), testData.credentials.programManager.password);
    await this.click(this.page.getByRole("button", { name: /sign in|log in|continue/i }).first());
  }

  field(fieldName: string): Locator {
    const normalized = fieldName.toLowerCase();

    if (normalized.includes("email")) {
      return this.page.getByLabel(/email/i).or(this.page.locator("input[type='email']")).first();
    }

    if (normalized.includes("password")) {
      return this.page.getByLabel(/password/i).or(this.page.locator("input[type='password']")).first();
    }

    return this.page.getByLabel(new RegExp(fieldName, "i")).first();
  }

  async fillField(fieldName: string, value: string): Promise<void> {
    await this.fill(this.field(fieldName), value);
  }

  async clickNamedButton(buttonName: string): Promise<void> {
    await this.page
      .getByRole("button", { name: new RegExp(buttonName, "i") })
      .or(this.page.getByRole("link", { name: new RegExp(buttonName, "i") }))
      .first()
      .click();
  }

  errorMessage(): Locator {
    return this.page.locator(".ulp-input-error-message, .ulp-validator-error, [role='alert']").first();
  }
}
