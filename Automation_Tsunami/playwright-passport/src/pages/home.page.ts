import { expect, type Page } from "@playwright/test";
import { BasePage } from "./base.page";
import { testData } from "../config/test-data";

export class HomePage extends BasePage {
  constructor(page: Page) {
    super(page);
  }

  async goto(): Promise<void> {
    await this.open("/");
  }

  async clickHomepageButton(buttonName: string): Promise<void> {
    const target = this.page
      .getByRole("link", { name: new RegExp(buttonName, "i") })
      .or(this.page.getByRole("button", { name: new RegExp(buttonName, "i") }))
      .or(this.page.getByText(new RegExp(buttonName, "i")))
      .first();

    await target.click();
  }

  async openLogin(): Promise<void> {
    await this.clickHomepageButton("Log In");
    await this.expectTitle(testData.titles.login);
  }

  async openSignup(): Promise<void> {
    await this.clickHomepageButton("Sign Up");
    await this.expectTitle(testData.titles.signup);
  }

  async verifyLoaded(): Promise<void> {
    await expect(this.page).toHaveURL(/ov-qa\.gsvlabsportal\.com/i);
  }
}
