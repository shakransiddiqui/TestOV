import { Given, Then } from "@cucumber/cucumber";
import { expect } from "@playwright/test";
import { getProperty } from "../configuration-reader";
import { PassportWorld } from "../world";

Given("User clicks on {string} button from Homepage", async function (this: PassportWorld, buttonName: string) {
  await this.homePage.goto();
  await this.homePage.clickHomepageButton(buttonName);
});

Then("Navigates to the page with title of {string}", async function (this: PassportWorld, titleKey: string) {
  await expect(this.page).toHaveTitle(getProperty(titleKey));
});

Then("User clicks back twice and navigates to Startup page with title of {string}", async function (this: PassportWorld, titleKey: string) {
  await this.page.goBack();
  await this.page.goBack();
  await expect(this.page).toHaveTitle(getProperty(titleKey));
});
