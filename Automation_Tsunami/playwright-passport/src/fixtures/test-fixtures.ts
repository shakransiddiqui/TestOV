import { test as base, expect } from "@playwright/test";
import { HomePage } from "../pages/home.page";
import { LoginPage } from "../pages/auth/login.page";
import { SignupPage } from "../pages/auth/signup.page";

export const test = base.extend<{
  homePage: HomePage;
  loginPage: LoginPage;
  signupPage: SignupPage;
}>({
  homePage: async ({ page }, use) => {
    await use(new HomePage(page));
  },
  loginPage: async ({ page }, use) => {
    await use(new LoginPage(page));
  },
  signupPage: async ({ page }, use) => {
    await use(new SignupPage(page));
  }
});

export { expect };
