# Playwright Migration Status

## Current comparison with Selenium project

### Selenium project

- 18 Passport feature files
- 82 scenarios
- Mature Java step definitions and page objects across:
  - Homepage
  - Login
  - Signup
  - Program Creation
  - Application Creation
  - Startup Program Application
  - Application Management
  - Program Management
  - Application Evaluation

### Playwright project

- 18 Passport feature files copied into `features/1_passport`
- Cucumber runner configured
- Shared `Configuration.properties` reader wired
- Browser world and hooks wired
- Playwright page objects wired for:
  - Homepage
  - Login
  - Signup
  - Program Creation
  - Application Creation
  - Startup Program Application
  - Application Management
  - Program Management
  - Application Evaluation
- Cucumber step definitions wired across the copied Passport feature suite

## What is already migrated

- Feature inventory and tag structure
- Cucumber project setup
- Shared property lookup from the Selenium config file
- Homepage entry-point steps
- Login field/button/title steps
- Signup field/button/role/title steps
- Program Creation steps
- Application Creation steps
- Startup Program Application steps
- Application Management steps
- Program Management steps
- Application Evaluation steps

## What is not fully migrated yet

These areas still need runtime validation and refinement against the live product:

- Selector stability across all flows
- Export/download validation parity on real browser runs
- Evaluation bulk action behavior on live data
- Organization dashboard delete behavior safety checks
- SSO signup redirect behavior in a live environment
- Scenario-by-scenario pass verification, trace review, and hardening

## Latest dry-run snapshot

- 82 scenarios discovered
- 82 scenarios have Playwright step bindings
- 0 scenarios contain undefined Playwright steps
- TypeScript compile check passes with `npx.cmd tsc --noEmit`

## What this means

The Playwright project now mirrors the Selenium suite structure and has full Cucumber step coverage for the copied Passport features.
It is much closer to a one-to-one replacement, but it still needs live execution hardening before it should be treated as drop-in parity with the Selenium suite.
