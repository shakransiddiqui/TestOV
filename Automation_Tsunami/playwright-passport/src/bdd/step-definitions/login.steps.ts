import { Then, When } from "@cucumber/cucumber";
import { expect } from "@playwright/test";
import { getProperty, resolvePropertyValue } from "../configuration-reader";
import { PassportWorld } from "../world";

When(
  "User enters {string} in the {string} field",
  async function (this: PassportWorld, fieldValueKey: string, fieldName: string) {
    const resolvedValue = resolvePropertyValue(fieldValueKey);
    this.values.set(fieldValueKey, resolvedValue);
    await this.loginPage.fillField(fieldName, resolvedValue);
  }
);

When("User clicks on the {string} button", async function (this: PassportWorld, buttonName: string) {
  await this.loginPage.clickNamedButton(buttonName);
});

Then("User should see a login error or validation message", async function (this: PassportWorld) {
  await expect(this.loginPage.errorMessage()).toBeVisible();
});

Then("User should remain on the Login page with title of {string}", async function (this: PassportWorld, titleKey: string) {
  await expect(this.page).toHaveTitle(getProperty(titleKey));
});

Then("User clicks back thrice and navigates to Startup page with title of {string}", async function (this: PassportWorld, titleKey: string) {
  await this.page.goBack();
  await this.page.goBack();
  await this.page.goBack();
  await expect(this.page).toHaveTitle(getProperty(titleKey));
});

Then("User should be redirected to the page with title of {string}", async function (this: PassportWorld, titleKey: string) {
  await expect(this.page).toHaveTitle(getProperty(titleKey));
});

Then("User logs out", async function (this: PassportWorld) {
  await this.headerPage.logout();
});
