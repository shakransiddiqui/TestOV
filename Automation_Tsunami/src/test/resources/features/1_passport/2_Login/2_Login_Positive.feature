@Login @Login-pos @passport @passport-pos @demo
Feature: Positive tests on Login Feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"

  # Positive login test with valid credentials as Program Manager
  @Tc_03 @valid_login_and_logout_program_manager @Logout
  Scenario: Verify user logs in and out successfully with valid credentials as a Program Manager
    When User enters "PMvalidEmail" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  # Positive login test with valid credentials as Startup Leader
  @Tc_04 @valid_login_and_logout_startup_leader @Logout
  Scenario: Verify user logs in and out successfully with valid credentials as a Startup Leader
    When User enters "SLvalidEmail" in the "Email address" field
    And User enters "SLvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "StartUp_page_title"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"
