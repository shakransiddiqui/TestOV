import { type Locator, type Page } from "@playwright/test";
import { BasePage } from "./base.page";

export class HeaderPage extends BasePage {
  readonly profileMenu: Locator;
  readonly logoutLink: Locator;

  constructor(page: Page) {
    super(page);
    this.profileMenu = page.getByRole("button", { name: /account|profile|menu/i }).first();
    this.logoutLink = page.getByRole("menuitem", { name: /log out|logout|sign out/i }).first();
  }

  async logout(): Promise<void> {
    if (await this.profileMenu.isVisible().catch(() => false)) {
      await this.profileMenu.click();
    }

    const fallbackLogout = this.page.getByText(/log out|logout|sign out/i).first();
    if (await this.logoutLink.isVisible().catch(() => false)) {
      await this.logoutLink.click();
      return;
    }

    await fallbackLogout.click();
  }
}
