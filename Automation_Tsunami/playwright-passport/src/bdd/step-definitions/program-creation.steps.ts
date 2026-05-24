import { Then, When } from "@cucumber/cucumber";
import { expect } from "@playwright/test";
import { getProperty, resolvePropertyValue } from "../configuration-reader";
import { PassportWorld } from "../world";

When(
  "User enters {string} into the {string} field of Program Details",
  async function (this: PassportWorld, fieldValueKey: string, fieldName: string) {
    const resolvedValue = resolvePropertyValue(fieldValueKey);
    this.values.set(fieldValueKey, resolvedValue);
    await this.programCreationPage.fillField(fieldName, resolvedValue === "EMPTY" ? "" : resolvedValue);
  }
);

Then("User selects {int} industries", async function (this: PassportWorld, count: number) {
  const selected = await this.programCreationPage.selectIndustries(count);
  expect(selected).toBe(count);
});

Then("User closes the industries dropdown", async function (this: PassportWorld) {
  const closed = await this.programCreationPage.closeIndustriesDropdown();
  expect(closed).toBeTruthy();
});

Then("User should see {string}", async function (this: PassportWorld, text: string) {
  await this.programCreationPage.textVisible(text);
});

Then("User selects {int} perks", async function (this: PassportWorld, count: number) {
  const selected = await this.programCreationPage.selectPerks(count);
  expect(selected).toBe(Math.min(count, 5));
});

Then("{string} should be highlighted as missing on Program Details", async function (this: PassportWorld, missingField: string) {
  const highlighted = await this.programCreationPage.missingFieldHighlighted(missingField);
  expect(highlighted).toBeTruthy();
});

Then("User should remain on the Create Program page with title of {string}", async function (this: PassportWorld, titleKey: string) {
  await expect(this.page).toHaveTitle(getProperty(titleKey));
});

Then("User attempts to select {int} perks and selection is capped at 5", async function (this: PassportWorld, requested: number) {
  const checked = await this.programCreationPage.attemptSelectPerksNoCap(requested);
  expect(checked).toBeLessThanOrEqual(5);

  const limitReachedCount = await this.programCreationPage.limitReachedCount();
  expect(limitReachedCount).toBeGreaterThan(0);
});

Then("User clicks on {string}", async function (this: PassportWorld, buttonName: string) {
  await this.signupPage.clickNamedButton(buttonName.replace(" button", ""));
});
