@Signup @Signup-pos @passport
Feature: Positive tests on Sign Up Feature

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

  @Tc_05 @SignUp_as_Startup_Leader   @passport-pos
  Scenario: Verify user successfully creates an account as a Startup Leader.
    Then User chooses "Startup Leader"
    Then User clicks on "Continue" button
    And User should see "Provide a few pieces of information to get started" on top
    Then User enters "startupName" into the "Startup Name" field
    Then User enters "location" into the "Location" field
    Then User clicks on "Finish" button
    Then User should be redirected to the page with title of "StartUp_page_title"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_06 @SignUp_as_Program_Manager   @passport-pos
  Scenario: Verify user successfully creates an account as a Program Manager.
    Then User chooses "Program Manager"
    Then User clicks on "Continue" button
    And User should see "Create an account to start offering your programs" on top
    Then User enters "organizationName" into the "Organization Name" field
    Then User enters "location" into the "Location" field
    Then User clicks on "Finish" button
    Then User should be redirected to the page with title of "Organization_page_title"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"
