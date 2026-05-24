import { test, expect } from "../../src/fixtures/test-fixtures";
import { testData } from "../../src/config/test-data";
import { randomCompany, randomEmail } from "../../src/utils/data-generator";

test("startup user can submit signup", async ({ signupPage, page }) => {
  await signupPage.goto();
  await signupPage.chooseStartupRole();
  await signupPage.submit({
    email: randomEmail(),
    password: testData.signup.password,
    fullName: testData.signup.fullName,
    jobTitle: testData.signup.jobTitle,
    location: testData.signup.location,
    companyName: randomCompany(testData.signup.startupCompanyPrefix)
  });

  await expect(page).not.toHaveURL(/\/signup/i);
});
