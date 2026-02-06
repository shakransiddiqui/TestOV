@Login @passport
Feature: Login_Feature

  Background: 
    Given User clicks on "Log In" button
    Then Navigates to the page with title of "Login_page_title"

  # Negative login test with various invalid, valid, and empty credentials
  @Tc_03 @passport-neg
  Scenario Outline: Verify login fails with invalid, valid, or empty username and password combinations
    When User enters "<Email>" in the "Email address" field
    And User enters "<Password>" in the "Password" field
    And User clicks on the "Log In" button
    Then User should see a login error or validation message
    And User should remain on the Login page with title of "Login_page_title"

    Examples: 
      | Email        | Password        |
      | invalidEmail | invalidPassword |
      | validEmail   | invalidPassword |
      | invalidEmail | validPassword   |
      | EMPTY        | invalidPassword |
      | invalidEmail | EMPTY           |
      | EMPTY        | EMPTY           |

  # Positive login test with valid credentials as Program Manager
  @Tc_04 @valid_login_and_logout_program_manager @Logout @passport-pos
  Scenario: Verify user logs in and out successfully with valid credentials as a Program Manager
    When User enters "PMvalidUsername" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"
    And User logs out
    Then User should be redirected to the page with title of "Startup_page_title"

  # Positive login test with valid credentials as Startup Leader
  @Tc_05 @valid_login_and_logout_startup_leader @Logout @passport-pos
  Scenario: Verify user logs in and out successfully with valid credentials as a Startup Leader
    When User enters "SLvalidUsername" in the "Email address" field
    And User enters "SLvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Startup_page_title"
    And User logs out
    Then User should be redirected to the page with title of "Startup_page_title"
