@ProgramCreation
Feature: Program_Creation_Feature

  Background: 
    Given User clicks on "Sign Up" button from Homepage
    Then Navigates to the page with title of "Signup_page_title"
    Then User enters "newEmail" into the "Email address" field
    Then User enters "newPassword" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User enters "jobTitle" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    And User should see "Tell about your startup" on top
    Then User chooses "Program Manager"
    Then User clicks on "Continue" button
    And User should see "Create an account to start offering your programs" on top
    Then User enters "organizationName" into the "Organization Name" field
    Then User enters "location" into the "Location" field
    Then User clicks on "Finish" button
    Then User should be redirected to the page with title of "Organization_page_title"

  @Tc_07
  Scenario: Fill out a new Create Program form with different number of industries and perks
    Then User clicks on "Create a new program" button
    Then User should be redirected to the page with title of "CreateProgram_page_title"
    Then User enters "programTitle" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User selects 1 industries
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User selects 1 perks
    Then User clicks on "Save & Create Application" button
    And User should see "Create Application"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_08
  Scenario Outline: Fill out a new Create Program form with different number of industries and perks
    Then User clicks on "Create a new program" button
    Then User should be redirected to the page with title of "CreateProgram_page_title"
    Then User enters "programTitle" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User selects <Count> industries
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User selects <PerksCount> perks
    Then User clicks on "Save & Create Application" button
    And User should see "Create Application"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

    Examples: 
      | Count | PerksCount |
      |     0 |          0 |
      |     2 |          2 |
      |     3 |          4 |
      |     5 |          5 |
