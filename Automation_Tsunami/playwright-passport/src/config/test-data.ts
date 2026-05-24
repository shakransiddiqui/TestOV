export const testData = {
  titles: {
    login: "Sign in | Passport",
    signup: "Sign up | Passport"
  },
  credentials: {
    programManager: {
      email: process.env.PM_VALID_EMAIL ?? "suzy.ghanem+test34QA@theonevalley.com",
      password: process.env.PM_VALID_PASSWORD ?? "Remyliam@2017"
    }
  },
  signup: {
    password: "TestPass@#123",
    fullName: "Test User",
    jobTitle: "Tester",
    location: "San",
    startupCompanyPrefix: "PW Startup"
  }
} as const;
