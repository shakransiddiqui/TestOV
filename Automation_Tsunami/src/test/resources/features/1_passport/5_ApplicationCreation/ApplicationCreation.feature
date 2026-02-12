@ApplicationCreation
Feature: Application_Creation_Feature

  Background: 
    Given Navigate to the login page.
    And Enter a valid username and password
    When Click the "Login" button.
    Then Verify that the user is redirected to the Community Dashboard.
    Given I navigate to the "Create Application" page

  @Tc_24 @Create_Application @passport-pos
  Scenario: 24_Create New Application and Redirect to Builder
    When the user enters a valid program title
    And the user enters valid location
    And the user selects a program type
    And the user enters program description
    And the user clicks the "Save & Continue" button
    Given the user is on the member perks page
    And the user clicks the "Save & Create Application" button
    Then the user should be redirected to the Create Application screen
    And user clicked on cancel button in Create Application screen
    Given User is on Programs page and user have atleast one program on page
    When User click on Create Application button on very first program on programs page
    When I enter "Fall 2025" into the "Application Title" field
    And I select the "Create New Application" option
    And I click the "Save & Continue" button
    Then the user should be redirected to the "createapplication" builder page
    Given I am on the "Application" builder page and the "Additional Questions" panel is open
    And user fills the Additional Questions form with the following data
      | Type             | Question                                     | Instruction                               |
      | Single-Line Text | What is your company's tagline?              | Short, single line (≤100 chars).          |
      | Multi-Line Text  | Describe your product in one paragraph       | 3–5 sentences; problem, solution, users.  |
      | Number           | How many paying customers do you have today? | Whole number only (no commas).            |
      | Date             | When did you found the company?              | Pick a valid founding date.               |
      | Single Choice    | What is your primary business model?         | Choose one option.                        |
      | Multiple Choice  | Which platforms do you support?              | Select all that apply.                    |
      | File             | Upload your current pitch deck.              | PDF only, max 25 MB.                      |
      | Phone Number     | What is your main contact phone number?      | Include country code if outside US.       |
      | Website/URL      | What is your company website?                | Full URL, e.g., https://example.com       |
      | Address          | What is your company headquarters address?   | Street, City, State, Postal Code, Country |
      | Email            | What is your main contact email address?     | Valid format (name@domain).               |
    And I clicked the "Save & Continue" buttons
    Then The user is redirected to publish page
    When I set the open date to today
    And I click on the "Complete" button

  @Tc_25 @Check_standard_questions_toggle_btn @passport-pos
  Scenario: 25_verify standard questions toggle buttons are working fine in application screen.
    When the user enters a valid program title
    And the user enters valid location
    And the user selects a program type
    And the user enters program description
    And the user clicks the "Save & Continue" button
    Given the user is on the member perks page
    And the user clicks the "Save & Create Application" button
    Then the user should be redirected to the Create Application screen
    And user clicked on cancel button in Create Application screen
    Given User is on Programs page and user have atleast one program on page
    When User click on Create Application button on very first program on programs page
    When I enter "Fall 2025" into the "Application Title" field
    And I select the "Create New Application" option
    And I click the "Save & Continue" button
    Then the user should be redirected to the "createapplication" builder page
    When the user clicks on the "Hide" button for the Company Description field
    And the user clicks on the "Hide" button for the Year of Founding field
    And the user clicks on the "Hide" button for the Funding field
    When the user clicks on the "Preview Application" button
    Then the user should see that the hidden questions are not visible on the preview page
