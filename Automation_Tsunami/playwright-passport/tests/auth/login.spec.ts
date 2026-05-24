import { test, expect } from "../../src/fixtures/test-fixtures";
import { testData } from "../../src/config/test-data";

test("program manager can sign in", async ({ loginPage, page }) => {
  await loginPage.goto();
  await loginPage.signInAsProgramManager();
  await expect(page).not.toHaveTitle(testData.titles.login);
});
