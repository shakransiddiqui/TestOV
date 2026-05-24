import { setWorldConstructor, World, type IWorldOptions } from "@cucumber/cucumber";
import { chromium, type Browser, type BrowserContext, type Page } from "@playwright/test";
import { HomePage } from "../pages/home.page";
import { LoginPage } from "../pages/auth/login.page";
import { SignupPage } from "../pages/auth/signup.page";
import { HeaderPage } from "../pages/header.page";
import { ProgramCreationPage } from "../pages/program-creation.page";
import { ApplicationCreationPage } from "../pages/application-creation.page";
import { StartupProgramApplicationPage } from "../pages/startup-program-application.page";
import { ApplicationManagementPage } from "../pages/application-management.page";
import { ApplicationEvaluationPage } from "../pages/application-evaluation.page";
import { ProgramManagementPage } from "../pages/program-management.page";

export class PassportWorld extends World {
  browser!: Browser;
  context!: BrowserContext;
  page!: Page;
  homePage!: HomePage;
  loginPage!: LoginPage;
  signupPage!: SignupPage;
  headerPage!: HeaderPage;
  programCreationPage!: ProgramCreationPage;
  applicationCreationPage!: ApplicationCreationPage;
  startupProgramApplicationPage!: StartupProgramApplicationPage;
  applicationManagementPage!: ApplicationManagementPage;
  applicationEvaluationPage!: ApplicationEvaluationPage;
  programManagementPage!: ProgramManagementPage;
  values = new Map<string, string>();
  state = new Map<string, unknown>();
  parentPageIndex = 0;

  constructor(options: IWorldOptions) {
    super(options);
  }

  async init(): Promise<void> {
    this.browser = await chromium.launch({
      headless: false,
      channel: "chrome"
    });
    this.context = await this.browser.newContext({
      acceptDownloads: true,
      ignoreHTTPSErrors: true,
      baseURL: "https://ov-qa.gsvlabsportal.com/"
    });
    this.page = await this.context.newPage();
    this.bindPage(this.page);
  }

  bindPage(page: Page): void {
    this.page = page;
    this.homePage = new HomePage(this.page);
    this.loginPage = new LoginPage(this.page);
    this.signupPage = new SignupPage(this.page);
    this.headerPage = new HeaderPage(this.page);
    this.programCreationPage = new ProgramCreationPage(this.page);
    this.applicationCreationPage = new ApplicationCreationPage(this.page);
    this.startupProgramApplicationPage = new StartupProgramApplicationPage(this.page);
    this.applicationManagementPage = new ApplicationManagementPage(this.page);
    this.applicationEvaluationPage = new ApplicationEvaluationPage(this.page);
    this.programManagementPage = new ProgramManagementPage(this.page);
  }

  async close(): Promise<void> {
    await this.context?.close();
    await this.browser?.close();
  }
}

setWorldConstructor(PassportWorld);
