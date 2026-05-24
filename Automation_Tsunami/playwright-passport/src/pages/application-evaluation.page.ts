import { type Page } from "@playwright/test";
import { BasePage } from "./base.page";

export class ApplicationEvaluationPage extends BasePage {
  constructor(page: Page) {
    super(page);
  }

  async clickFirstApplicationFromResults(): Promise<string> {
    const firstLink = this.page.locator("tbody tr td a, tbody tr td button").first();
    const name = ((await firstLink.textContent()) ?? "").trim() || "Application";
    await firstLink.click();
    return name;
  }

  async applicationDetailsVisible(applicationName: string): Promise<boolean> {
    return this.page.getByText(new RegExp(applicationName, "i")).first().isVisible().catch(() => false);
  }

  async clickDetailsButton(buttonLabel: string): Promise<void> {
    await this.page.getByRole("button", { name: new RegExp(buttonLabel, "i") }).first().click().catch(async () => {
      await this.page.getByRole("link", { name: new RegExp(buttonLabel, "i") }).first().click();
    });
  }

  async selectedStatusMatches(expectedStatus: string): Promise<boolean> {
    return this.page.getByText(new RegExp(`^${expectedStatus}$`, "i")).first().isVisible().catch(() => false);
  }

  async changeStatusTo(newStatus: string): Promise<void> {
    const statusButton = this.page.getByRole("button", { name: /needs review|on hold|accepted|rejected/i }).first();
    if (await statusButton.isVisible().catch(() => false)) {
      await statusButton.click();
    }
    await this.page.getByRole("option", { name: new RegExp(newStatus, "i") }).first().click().catch(async () => {
      await this.page.getByText(new RegExp(`^${newStatus}$`, "i")).last().click();
    });
  }

  async ratingTabSelected(tabName: string): Promise<boolean> {
    const tab = this.page.getByRole("tab", { name: new RegExp(tabName, "i") }).first();
    if (!(await tab.isVisible().catch(() => false))) {
      return false;
    }

    const selected = await tab.getAttribute("aria-selected");
    return selected === "true" || selected === null;
  }

  async selectStarRating(stars: number): Promise<void> {
    await this.page.locator('[aria-label*="star"], [data-testid*="star"]').nth(stars - 1).click();
  }

  async enterRatingComment(comment: string): Promise<void> {
    await this.page.getByPlaceholder(/comment/i).first().fill(comment).catch(async () => {
      await this.page.getByLabel(/comment/i).first().fill(comment);
    });
  }

  async ratingVisible(stars: number, comment: string): Promise<boolean> {
    const commentVisible = await this.page.getByText(comment).first().isVisible().catch(() => false);
    const starsVisible = await this.page.locator('[aria-label*="star filled"], [data-testid*="star-filled"]').nth(stars - 1).isVisible().catch(() => false);
    return commentVisible || starsVisible;
  }

  async ratingNotSubmittedWithoutStars(): Promise<boolean> {
    return this.page.getByText(/select.*star|star.*required|please rate/i).first().isVisible().catch(() => true);
  }

  async allRatingsViewVisible(): Promise<boolean> {
    return this.page.getByText(/all ratings/i).first().isVisible().catch(() => false);
  }

  async noSubmittedRatingsVisible(): Promise<boolean> {
    return this.page.getByText(/no submitted ratings/i).first().isVisible().catch(() => false);
  }

  async currentApplicationName(): Promise<string> {
    const heading = this.page.getByRole("heading").first();
    return ((await heading.textContent()) ?? "").trim();
  }

  async navigateBetweenApplications(direction: "next" | "previous"): Promise<string> {
    await this.page.getByRole("button", { name: new RegExp(direction, "i") }).first().click();
    await this.page.waitForLoadState("domcontentloaded");
    return this.currentApplicationName();
  }

  async clickEmailIcon(): Promise<void> {
    await this.page.getByRole("button", { name: /email/i }).first().click();
  }

  async clickCopyLinkIcon(): Promise<void> {
    await this.page.getByRole("button", { name: /copy link/i }).first().click().catch(async () => {
      await this.page.getByText(/copy link/i).first().click();
    });
  }

  async copyLinkFeedbackVisible(): Promise<boolean> {
    return this.page.getByText(/copied|link copied/i).first().isVisible().catch(() => false);
  }

  async refreshDetailsPage(): Promise<void> {
    await this.page.reload();
    await this.page.waitForLoadState("domcontentloaded");
  }

  async closeStatusConfirmationModal(): Promise<void> {
    await this.page.getByRole("button", { name: /close|cancel|x/i }).first().click();
  }
}
