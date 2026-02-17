@ApplicationCreation @ApplicationCreation-neg @passport @passport-neg @demo
Feature: Negative tests on Application Creation Feature

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

  @Tc_010
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
    # 1) Untitled question validations (empty)
    Then User should see "This field is required" validation message for Additional Question
    And Additional Question "Save" button should be disabled
    # 2) Delete popup for UNTITLED question -> verify warning -> click NO (do not delete)
    When User clicks on the delete icon for the current Additional Question
    Then User should see the Delete Question popup
    And User clicks on "No, Go Back" in the Delete Question popup
