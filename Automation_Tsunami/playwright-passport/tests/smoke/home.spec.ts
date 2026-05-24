import { test, expect } from "../../src/fixtures/test-fixtures";
import { testData } from "../../src/config/test-data";

test.describe("Homepage", () => {
  test("homepage loads and routes to login", async ({ homePage, page }) => {
    await homePage.goto();
    await homePage.verifyLoaded();
    await homePage.openLogin();
    await expect(page).toHaveTitle(testData.titles.login);
  });

  test("homepage routes to signup", async ({ homePage, page }) => {
    await homePage.goto();
    await homePage.openSignup();
    await expect(page).toHaveTitle(testData.titles.signup);
  });
});
