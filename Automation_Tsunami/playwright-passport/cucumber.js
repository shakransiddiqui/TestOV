module.exports = {
  default: {
    requireModule: ["ts-node/register"],
    require: [
      "src/bdd/hooks.ts",
      "src/bdd/step-definitions/**/*.ts"
    ],
    paths: ["features/**/*.feature"],
    format: [
      "progress",
      "html:test-results/cucumber/cucumber-report.html",
      "json:test-results/cucumber/cucumber-report.json"
    ]
  }
};
