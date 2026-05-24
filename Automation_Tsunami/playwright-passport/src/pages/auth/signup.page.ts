import { type Locator, type Page } from "@playwright/test";
import { BasePage } from "../base.page";
import { testData } from "../../config/test-data";

export class SignupPage extends BasePage {
  constructor(page: Page) {
    super(page);
  }

  async goto(): Promise<void> {
    await this.open("/signup");
  }

  field(fieldName: string): Locator {
    const normalized = fieldName.toLowerCase();

    if (normalized.includes("email")) {
      return this.page.getByLabel(/email/i).or(this.page.locator("input[type='email']")).first();
    }

    if (normalized.includes("password")) {
      return this.page.getByLabel(/password/i).or(this.page.locator("input[type='password']")).first();
    }

    if (normalized.includes("full name")) {
      return this.page.getByLabel(/full name/i).or(this.page.locator("input[name*='full']")).first();
    }

    if (normalized.includes("company")) {
      return this.page.getByLabel(/company|organization/i).or(this.page.locator("input[name*='company']")).first();
    }

    if (normalized.includes("location")) {
      return this.page.getByLabel(/location/i).or(this.page.locator("input[name*='location']")).first();
    }

    if (normalized.includes("job")) {
      return this.page.getByLabel(/job title/i).or(this.page.locator("input[name*='job']")).first();
    }

    return this.page.getByLabel(new RegExp(fieldName, "i")).first();
  }

  async chooseStartupRole(): Promise<void> {
    await this.page.getByText(/startup/i).first().click();
  }

  async chooseRole(roleName: string): Promise<void> {
    await this.page.getByText(new RegExp(roleName, "i")).first().click();
  }

  async selectSsoOption(optionName: string): Promise<void> {
    await this.page.getByText(new RegExp(optionName, "i")).first().click();
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

  topText(text: string): Locator {
    return this.page.getByText(text, { exact: false }).first();
  }

  errorMessage(): Locator {
    return this.page.locator(".ulp-input-error-message, .ulp-validator-error, [role='alert']").first();
  }

  async expectSignupTitle(): Promise<void> {
    await this.expectTitle(testData.titles.signup);
  }

  async submit(payload: {
    email: string;
    password: string;
    fullName: string;
    jobTitle: string;
    location: string;
    companyName: string;
  }): Promise<void> {
    await this.fill(this.page.locator("input[type='email']").first(), payload.email);
    await this.fill(this.page.locator("input[type='password']").first(), payload.password);
    await this.fill(this.page.locator("input[name*='full']").first(), payload.fullName);
    await this.fill(this.page.locator("input[name*='job']").first(), payload.jobTitle);
    await this.fill(this.page.locator("input[name*='location']").first(), payload.location);
    await this.fill(this.page.locator("input[name*='company']").first(), payload.companyName);
    await this.click(this.page.getByRole("button", { name: /sign up|create account|continue/i }).first());
  }
}
