import { Then } from "@cucumber/cucumber";
import { expect } from "@playwright/test";
import { getProperty, resolvePropertyValue } from "../configuration-reader";
import { PassportWorld } from "../world";

Then(
  "User enters {string} into the {string} field",
  async function (this: PassportWorld, fieldValueKey: string, fieldName: string) {
    const resolvedValue = resolvePropertyValue(fieldValueKey);
    this.values.set(fieldValueKey, resolvedValue);
    await this.signupPage.fillField(fieldName, resolvedValue);
  }
);

Then("User clicks on {string} button", async function (this: PassportWorld, buttonName: string) {
  await this.signupPage.clickNamedButton(buttonName);
});

Then("User should see a signup error or validation message", async function (this: PassportWorld) {
  await expect(this.signupPage.errorMessage()).toBeVisible();
});

Then("User should remain on the Signup page with title of {string}", async function (this: PassportWorld, titleKey: string) {
  await expect(this.page).toHaveTitle(getProperty(titleKey));
});

Then("User should see {string} on top", async function (this: PassportWorld, text: string) {
  await expect(this.signupPage.topText(text)).toBeVisible();
});

Then("User chooses {string}", async function (this: PassportWorld, roleName: string) {
  await this.signupPage.chooseRole(roleName);
});

Then("User selects {string}", async function (this: PassportWorld, optionName: string) {
  await this.signupPage.selectSsoOption(optionName);
});
