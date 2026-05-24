import { After, Before, Status } from "@cucumber/cucumber";
import { PassportWorld } from "./world";

Before(async function (this: PassportWorld) {
  await this.init();
});

After(async function (this: PassportWorld, scenario) {
  if (scenario.result?.status === Status.FAILED) {
    const screenshot = await this.page.screenshot({ fullPage: true });
    await this.attach(screenshot, "image/png");
  }

  await this.close();
});
