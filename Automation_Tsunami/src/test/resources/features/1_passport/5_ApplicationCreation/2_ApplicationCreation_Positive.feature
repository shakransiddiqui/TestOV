@ApplicationCreation @ApplicationCreation-pos @passport @passport-pos @demo
Feature: Positive tests on Application Creation Feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"
    When User enters "PMvalidEmail" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"
    Then User clicks on "Create a new program" button
    Then User should be redirected to the page with title of "CreateProgram_page_title"
    Then User enters "programTitle" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User clicks on "Save & Create Application" button
    Then User should be redirected to the page with title of "CreateApplication_page_title"
    And User should see "Create Application"

  @Tc_08
  Scenario: Create new application and redirect to builder
    Then User enters "applicationTitle" into the "Application Title" field of Create application
    And User selects the "Create New Application" option on Create Application page
    And User clicks on "Save & Continue" button
    And User should see "Basic Information" in the Application Builder page
    And User clicks the collapse icon on the Application Builder page
    Then Standard Questions should be collapsed
    And User scrolls to "Additional Questions" section
    And User clicks on "Add New Question" in Additional Questions section
    Then User should see the New Question form
    And User fills the Additional Questions form with the following data
      | Type             | Question                                 | Instruction                              | Required |
      | Single-Line Text | What is your company's tagline?          | Short, single line (≤100 chars).         | Yes      |
      | Multi-Line Text  | Describe your product in one paragraph   | 3–5 sentences; problem, solution, users. | No       |
      | Number           | How many paying customers do you have?   | Whole number only (no commas).           | Yes      |
      | Date             | When did you found the company?          | Pick a valid founding date.              | No       |
      | Email            | What is your main contact email address? | Valid format (name@domain).              | Yes      |
      | Single Choice    | What is your primary business model?     | Choose one option.                       | Yes      |
