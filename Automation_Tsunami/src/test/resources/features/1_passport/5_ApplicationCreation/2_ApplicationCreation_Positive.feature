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
      | Type    | Question                                   | Instruction                               | Required |
      | Address | What is your company headquarters address? | Street, City, State, Postal Code, Country | Yes      |
    And User clicks on "Save & Continue" button on the Application Builder page
    And User clicks on "Preview Application" button on the Application Builder page
    Then User should be on the Preview Application page
    And Applicant clicks on "Save & Continue" button on the Preview Application page
    Then Applicant should be on the Preview Additional Questions section
    And Applicant should see "Back" and "Submit" buttons on the Preview Additional Questions section
    And Applicant should see all added Additional Questions on the Preview page
    And User clicks on "Back to Application" button on the Preview Application page
    And User clicks on "Save & Continue" button on the Application Builder page
