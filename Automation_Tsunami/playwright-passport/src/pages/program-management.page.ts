import { type Page } from "@playwright/test";
import { BasePage } from "./base.page";

export class ProgramManagementPage extends BasePage {
  constructor(page: Page) {
    super(page);
  }

  async clickOrganizationDashboardIcon(): Promise<void> {
    await this.page.getByRole("button", { name: /organization dashboard/i }).first().click().catch(async () => {
      await this.page.getByText(/organization dashboard/i).first().click();
    });
  }

  async openProgramsPageOne(): Promise<void> {
    await this.page.getByRole("button", { name: /^1$/ }).first().click().catch(async () => {
      await this.page.waitForLoadState("domcontentloaded");
    });
  }

  async hasProgramsTable(): Promise<boolean> {
    return this.page.locator("tbody tr").first().isVisible().catch(() => false);
  }

  async deletableProgramsPresent(): Promise<boolean> {
    return this.page.getByRole("button", { name: /delete/i }).first().isVisible().catch(() => false);
  }

  async clickFirstDeleteButton(): Promise<void> {
    await this.page.getByRole("button", { name: /delete/i }).first().click();
  }

  async deleteConfirmationVisible(): Promise<boolean> {
    return this.page.getByRole("dialog").getByText(/delete/i).first().isVisible().catch(() => false);
  }

  async confirmDelete(): Promise<void> {
    await this.page.getByRole("button", { name: /delete|confirm/i }).last().click();
  }

  async programNameVisible(programName: string): Promise<boolean> {
    return this.page.getByText(new RegExp(programName, "i")).first().isVisible().catch(() => false);
  }
}
