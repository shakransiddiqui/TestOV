import fs from "fs";
import path from "path";
import { expect, type Locator, type Page } from "@playwright/test";
import { BasePage } from "./base.page";

export class ApplicationManagementPage extends BasePage {
  private readonly statusNames = ["Needs Review", "On Hold", "Accepted", "Rejected"];

  constructor(page: Page) {
    super(page);
  }

  private evaluationRows(): Locator {
    return this.page.locator("tbody tr").filter({ has: this.page.locator("td") });
  }

  private async visibleRows(): Promise<Locator[]> {
    const rows = this.evaluationRows();
    const count = await rows.count();
    const visible: Locator[] = [];

    for (let index = 0; index < count; index += 1) {
      const row = rows.nth(index);
      if (await row.isVisible().catch(() => false)) {
        visible.push(row);
      }
    }

    return visible;
  }

  private async rowText(row: Locator): Promise<string> {
    return (await row.innerText()).replace(/\s+/g, " ").trim();
  }

  async openManageApplicationsForTargetProgram(targetProgramName: string): Promise<void> {
    const escapedName = targetProgramName.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
    const card = this.page.getByText(new RegExp(escapedName, "i")).first();
    await card.waitFor({ state: "visible" });

    const manageButton = card
      .locator("xpath=ancestor::*[self::div or self::section or self::article][1]")
      .getByRole("button", { name: /manage applications/i })
      .first();

    if (await manageButton.isVisible().catch(() => false)) {
      await manageButton.click();
      return;
    }

    await this.page.getByRole("button", { name: /manage applications/i }).first().click();
  }

  async waitForApplicationsToLoad(): Promise<void> {
    const rowLocator = this.evaluationRows().first();
    await Promise.race([
      rowLocator.waitFor({ state: "visible", timeout: 30000 }),
      this.page.getByText(/no applications|no results/i).waitFor({ state: "visible", timeout: 30000 })
    ]);
  }

  async openExportMenu(): Promise<void> {
    const button = this.page.getByRole("button", { name: /export/i }).first();
    if (await button.isVisible().catch(() => false)) {
      await button.click();
      return;
    }

    await this.page.getByText(/export/i).first().click();
  }

  async exportApplications(downloadDirectory: string): Promise<string> {
    await fs.promises.mkdir(downloadDirectory, { recursive: true });
    const [download] = await Promise.all([
      this.page.waitForEvent("download", { timeout: 60000 }),
      this.page.getByRole("menuitem", { name: /export/i }).first().click().catch(async () => {
        await this.page.getByRole("button", { name: /export/i }).first().click();
      })
    ]);

    const suggestedFilename = download.suggestedFilename();
    const targetFile = path.join(downloadDirectory, suggestedFilename);
    await download.saveAs(targetFile);
    return targetFile;
  }

  async downloadedZipContainsEntries(filePath: string, expectedEntries: string[]): Promise<boolean> {
    const buffer = await fs.promises.readFile(filePath);
    const content = buffer.toString("latin1");
    return expectedEntries.every((entry) => content.includes(entry));
  }

  async readAllFilterCount(): Promise<number> {
    const candidates = [
      this.page.getByRole("tab", { name: /all/i }).first(),
      this.page.getByText(/^all\b/i).first()
    ];

    for (const locator of candidates) {
      if (await locator.isVisible().catch(() => false)) {
        const text = await locator.innerText();
        const match = text.match(/(\d+)/);
        if (match) {
          return Number(match[1]);
        }
      }
    }

    return (await this.visibleRows()).length;
  }

  async paginationVisible(): Promise<boolean> {
    return this.page.getByRole("button", { name: /^[0-9]+$/ }).nth(1).isVisible().catch(() => false);
  }

  async currentPageNumber(): Promise<string> {
    const selected = this.page.locator('[aria-current="page"], [aria-selected="true"]').first();
    if (await selected.isVisible().catch(() => false)) {
      return (await selected.innerText()).trim();
    }

    return "1";
  }

  async openEvaluationPage(pageNumber: string): Promise<void> {
    await this.page.getByRole("button", { name: new RegExp(`^${pageNumber}$`) }).first().click();
    await expect.poll(async () => this.currentPageNumber()).toBe(pageNumber);
  }

  async lastEvaluationPageNumber(): Promise<string> {
    const buttons = this.page.getByRole("button", { name: /^[0-9]+$/ });
    const count = await buttons.count();
    if (count === 0) {
      return "1";
    }

    return (await buttons.nth(count - 1).innerText()).trim();
  }

  async displayedApplicationRowCount(): Promise<number> {
    return (await this.visibleRows()).length;
  }

  async anyApplicationRowsDisplayed(): Promise<boolean> {
    return (await this.displayedApplicationRowCount()) > 0;
  }

  async searchForApplication(name: string): Promise<void> {
    const searchInput = this.page.getByPlaceholder(/search/i).first();
    await searchInput.fill(name);
    await searchInput.press("Enter");
  }

  async searchResultsVisible(): Promise<boolean> {
    return this.page.getByText(/search results/i).first().isVisible().catch(() => false);
  }

  async rowsContainApplicationName(name: string): Promise<boolean> {
    const rows = await this.visibleRows();
    if (rows.length === 0) {
      return false;
    }

    const lowered = name.toLowerCase();
    for (const row of rows) {
      const text = (await this.rowText(row)).toLowerCase();
      if (!text.includes(lowered)) {
        return false;
      }
    }

    return true;
  }

  async openPageOne(): Promise<void> {
    await this.openEvaluationPage("1");
  }

  async everyDisplayedRowMatchesStatus(status: string): Promise<boolean> {
    const rows = await this.visibleRows();
    if (rows.length === 0) {
      return false;
    }

    const lowered = status.toLowerCase();
    for (const row of rows) {
      if (!(await this.rowText(row)).toLowerCase().includes(lowered)) {
        return false;
      }
    }

    return true;
  }

  async namesByStatus(status: string): Promise<string[]> {
    const rows = await this.visibleRows();
    const names: string[] = [];
    const lowered = status.toLowerCase();

    for (const row of rows) {
      const text = await this.rowText(row);
      if (!text.toLowerCase().includes(lowered)) {
        continue;
      }

      const firstCell = row.locator("td").first();
      if (await firstCell.isVisible().catch(() => false)) {
        names.push((await firstCell.innerText()).trim());
      }
    }

    return names;
  }

  async randomApplicationName(status?: string, min = 1, max?: number): Promise<string[]> {
    const pool = status ? await this.namesByStatus(status) : await this.allVisibleApplicationNames();
    if (pool.length === 0) {
      return [];
    }

    const upper = Math.min(max ?? min, pool.length);
    const targetCount = Math.max(min, upper);
    const shuffled = [...pool].sort(() => Math.random() - 0.5);
    return shuffled.slice(0, targetCount);
  }

  async allVisibleApplicationNames(): Promise<string[]> {
    const rows = await this.visibleRows();
    const names: string[] = [];

    for (const row of rows) {
      const firstCell = row.locator("td").first();
      if (await firstCell.isVisible().catch(() => false)) {
        names.push((await firstCell.innerText()).trim());
      }
    }

    return names;
  }

  async changeApplicationStatus(applicationName: string, newStatus: string): Promise<void> {
    const row = this.page.locator("tbody tr", { hasText: applicationName }).first();
    await row.waitFor({ state: "visible" });

    const combobox = row.getByRole("combobox").first();
    if (await combobox.isVisible().catch(() => false)) {
      await combobox.selectOption({ label: newStatus }).catch(async () => {
        await combobox.click();
        await this.page.getByRole("option", { name: new RegExp(newStatus, "i") }).first().click();
      });
      return;
    }

    const statusChip = row.getByText(new RegExp(this.statusNames.join("|"), "i")).first();
    await statusChip.click();
    await this.page.getByRole("option", { name: new RegExp(newStatus, "i") }).first().click().catch(async () => {
      await this.page.getByText(new RegExp(`^${newStatus}$`, "i")).last().click();
    });
  }

  async verifyRowStatus(applicationName: string, expectedStatus: string): Promise<boolean> {
    const row = this.page.locator("tbody tr", { hasText: applicationName }).first();
    return (await this.rowText(row)).toLowerCase().includes(expectedStatus.toLowerCase());
  }

  async selectAllApplications(): Promise<void> {
    const checkbox = this.page.getByRole("checkbox", { name: /select all/i }).first();
    if (await checkbox.isVisible().catch(() => false)) {
      await checkbox.check();
      return;
    }

    await this.page.locator('input[type="checkbox"]').first().check();
  }

  async clearAllApplications(): Promise<void> {
    const checkbox = this.page.getByRole("checkbox", { name: /select all/i }).first();
    if (await checkbox.isVisible().catch(() => false)) {
      await checkbox.uncheck();
      return;
    }

    await this.page.locator('input[type="checkbox"]').first().uncheck();
  }

  async selectApplicationsIndividually(applicationNames: string[]): Promise<void> {
    for (const name of applicationNames) {
      const row = this.page.locator("tbody tr", { hasText: name }).first();
      const checkbox = row.locator('input[type="checkbox"]').first();
      await checkbox.check();
    }
  }

  async bulkActionEnabled(): Promise<boolean> {
    const bulkAction = this.page.getByRole("button", { name: /bulk action/i }).first();
    if (await bulkAction.isVisible().catch(() => false)) {
      return await bulkAction.isEnabled();
    }

    const select = this.page.getByRole("combobox", { name: /bulk action/i }).first();
    return await select.isEnabled().catch(() => false);
  }

  async applyBulkAction(actionName: string): Promise<void> {
    const bulkButton = this.page.getByRole("button", { name: /bulk action/i }).first();
    if (await bulkButton.isVisible().catch(() => false)) {
      await bulkButton.click();
      await this.page.getByRole("menuitem", { name: new RegExp(actionName, "i") }).first().click().catch(async () => {
        await this.page.getByText(new RegExp(actionName, "i")).last().click();
      });
      return;
    }

    const select = this.page.getByRole("combobox", { name: /bulk action/i }).first();
    await select.selectOption({ label: actionName });
  }

  async statusConfirmationModalVisible(type: "acceptance" | "rejection" | "bulk acceptance" | "bulk rejection"): Promise<boolean> {
    const textByType: Record<string, RegExp> = {
      acceptance: /accepted this startup/i,
      rejection: /decided not to move forward/i,
      "bulk acceptance": /accepted this startup/i,
      "bulk rejection": /decided not to move forward/i
    };

    return this.page.getByText(textByType[type]).first().isVisible().catch(() => false);
  }

  async chooseStatusConfirmation(actionName: string): Promise<void> {
    await this.page.getByRole("button", { name: new RegExp(actionName.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"), "i") }).first().click();
  }

  async genericEmailComposerVisible(): Promise<boolean> {
    return this.page.getByRole("dialog").getByText(/subject|message/i).first().isVisible().catch(() => false);
  }

  async sendGenericEmail(): Promise<void> {
    const subject = this.page.getByLabel(/subject/i).first();
    if (await subject.isVisible().catch(() => false)) {
      await subject.fill(`Playwright subject ${Date.now()}`);
    }

    const body = this.page.getByLabel(/message|body/i).first();
    if (await body.isVisible().catch(() => false)) {
      await body.fill(`Playwright message ${Date.now()}`);
    }

    await this.page.getByRole("button", { name: /send/i }).first().click();
  }

  async emailSentConfirmationVisible(): Promise<boolean> {
    return this.page.getByText(/email sent|sent successfully/i).first().isVisible().catch(() => false);
  }

  async closeEmailSentConfirmation(): Promise<void> {
    await this.page.getByRole("button", { name: /close|done|ok/i }).first().click();
  }

  async openRowEmailComposer(applicationName: string): Promise<void> {
    const row = this.page.locator("tbody tr", { hasText: applicationName }).first();
    await row.getByRole("button", { name: /email/i }).first().click().catch(async () => {
      await row.getByText(/email/i).first().click();
    });
  }

  async closeGenericEmailComposer(): Promise<void> {
    await this.page.getByRole("button", { name: /cancel|close/i }).first().click();
  }

  async quickActionVisible(name: string): Promise<boolean> {
    return this.page.getByRole("button", { name: new RegExp(name, "i") }).first().isVisible().catch(() => false);
  }

  async clickQuickAction(name: string): Promise<void> {
    await this.page.getByRole("button", { name: new RegExp(name, "i") }).first().click();
  }

  async backToDashboard(): Promise<void> {
    await this.page.getByRole("button", { name: /back to dashboard/i }).first().click();
  }

  async inviteModalVisible(): Promise<boolean> {
    return this.page.getByRole("dialog").getByText(/invite/i).first().isVisible().catch(() => false);
  }

  async openInviteModal(): Promise<void> {
    await this.page.getByRole("button", { name: /invite/i }).first().click();
  }

  async addInviteEmail(email: string): Promise<void> {
    const input = this.page.getByLabel(/email/i).first();
    await input.fill(email);
    await this.page.getByRole("button", { name: /add|invite/i }).first().click();
  }

  async inviteEmailListed(email: string): Promise<boolean> {
    return this.page.getByText(email).first().isVisible().catch(() => false);
  }

  async deleteInviteEmail(email: string): Promise<void> {
    const row = this.page.getByText(email).first().locator("xpath=ancestor::*[self::li or self::div or self::tr][1]");
    await row.getByRole("button", { name: /delete|remove/i }).first().click();
  }

  async closeInviteWithCancel(): Promise<void> {
    await this.page.getByRole("button", { name: /cancel/i }).first().click();
  }

  async sendInvite(): Promise<void> {
    await this.page.getByRole("button", { name: /send/i }).first().click();
  }

  async evaluationHeaderMenuVisible(): Promise<void> {
    await this.page.getByRole("button", { name: /more|menu|actions/i }).first().click();
  }

  async headerMenuOptionVisible(optionName: string): Promise<boolean> {
    return this.page.getByRole("menuitem", { name: new RegExp(optionName, "i") }).first().isVisible().catch(() => false);
  }

  async clickHeaderMenuOption(optionName: string): Promise<void> {
    await this.page.getByRole("menuitem", { name: new RegExp(optionName, "i") }).first().click();
  }

  async evaluationSubsectionMenuVisible(): Promise<void> {
    await this.page.getByRole("button", { name: /application actions|subsection|more/i }).nth(1).click().catch(async () => {
      await this.page.getByRole("button", { name: /more|menu/i }).nth(1).click();
    });
  }

  async subsectionMenuOptionVisible(optionName: string): Promise<boolean> {
    return this.headerMenuOptionVisible(optionName);
  }

  async clickSubsectionOption(optionName: string): Promise<void> {
    await this.clickHeaderMenuOption(optionName);
  }

  async pageContainsText(text: RegExp): Promise<boolean> {
    return this.page.getByText(text).first().isVisible().catch(() => false);
  }

  async clickFilterTab(filterName: string): Promise<void> {
    await this.page.getByRole("tab", { name: new RegExp(filterName, "i") }).first().click().catch(async () => {
      await this.page.getByText(new RegExp(`^${filterName}\\b`, "i")).first().click();
    });
  }

  async filterTabDisplaysStatus(filterName: string, expectedStatus: string, count: number): Promise<boolean> {
    await this.clickFilterTab(filterName);
    const rows = await this.visibleRows();
    if (rows.length !== count) {
      return false;
    }

    return this.everyDisplayedRowMatchesStatus(expectedStatus);
  }

  async filterTabDisplaysAll(count: number): Promise<boolean> {
    await this.clickFilterTab("All");
    return (await this.displayedApplicationRowCount()) === count;
  }

  async clickApplicationName(applicationName: string): Promise<void> {
    await this.page.getByRole("link", { name: new RegExp(applicationName, "i") }).first().click().catch(async () => {
      await this.page.getByText(new RegExp(applicationName, "i")).first().click();
    });
  }

  async detailsPageVisible(applicationName: string): Promise<boolean> {
    return this.page.getByText(new RegExp(applicationName, "i")).first().isVisible().catch(() => false);
  }
}
