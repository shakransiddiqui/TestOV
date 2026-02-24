@ProgramCreation @ProgramCreation-neg @passport  @demo
Feature: Negative tests on Program Creation Feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"
    When User enters "PMvalidEmail" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"
    Then User clicks on "Create a new program" button
    Then User should be redirected to the page with title of "CreateProgram_page_title"

  @Tc_009   @passport-neg
  Scenario Outline: Verify Program Details highlights missing required fields when <Case>
    When User enters "<programTitle>" into the "Program Title" field of Program Details
    And User enters "<location>" into the "Location" field of Program Details
    And User enters "<programType>" into the "Program Type" field of Program Details
    And User enters "<programDescription>" into the "Program Description" field of Program Details
    And User clicks on "Save & Continue" button
    Then "<missingField>" should be highlighted as missing on Program Details
    And User should remain on the Create Program page with title of "CreateProgram_page_title"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

    Examples: 
      | Case                         | programTitle | location | programType | programDescription | missingField        |
      | Program Title is empty       | EMPTY        | location | programType | programDescription | Program Title       |
      | Location is empty            | programTitle | EMPTY    | programType | programDescription | Location            |
      | Program Type is empty        | programTitle | location | EMPTY       | programDescription | Program Type        |
      | Program Description is empty | programTitle | location | programType | EMPTY              | Program Description |

  @Tc_010   @passport-neg
  Scenario: Verify that user can not select more than 5 perks
    Then User enters "programTitle" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User selects 1 industries
    And User closes the industries dropdown
    Then User clicks on "Save & Continue" button
    Then User should see "Member Perks"
    Then User attempts to select 6 perks and selection is capped at 5
    Then User clicks on "Cancel button"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"
