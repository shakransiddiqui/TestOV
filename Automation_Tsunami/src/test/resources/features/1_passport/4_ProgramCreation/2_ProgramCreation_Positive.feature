@ProgramCreation @ProgramCreation-pos @passport @passport-pos   @demo
Feature: Positive tests on Program Creation Feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"
    When User enters "PMvalidEmail" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"

  @Tc_07
  Scenario Outline: Fill out a new Create Program form with different number of industries
    Then User clicks on "Create a new program" button
    Then User should be redirected to the page with title of "CreateProgram_page_title"
    Then User enters "programTitle" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User selects <Count> industries
    And User closes the industries dropdown
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User selects 1 perks
    Then User clicks on "Save & Create Application" button
    And User should see "Create Application"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

    Examples: 
      | Count |
      |     0 |
      |     1 |
      |     3 |
      |     2 |
      |     5 |

  @Tc_08
  Scenario Outline: Fill out a new Create Program form with different number of perks
    Then User clicks on "Create a new program" button
    Then User should be redirected to the page with title of "CreateProgram_page_title"
    Then User enters "programTitle" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User selects 1 industries
    And User closes the industries dropdown
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User selects <PerksCount> perks
    Then User clicks on "Save & Create Application" button
    And User should see "Create Application"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

    Examples: 
      | PerksCount |
      |          0 |
      |          1 |
      |          4 |
      |          3 |
      |          5 |
