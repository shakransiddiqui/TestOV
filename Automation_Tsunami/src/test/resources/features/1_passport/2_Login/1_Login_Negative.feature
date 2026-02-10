@Login @Login-neg @passport @passport-neg    @demo
Feature: Negative tests on Login Feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"

  # Negative login test with various invalid and valid credentials
  @Tc_001 @invalid_login_with_different_invalid_inputs
  Scenario Outline: Verify login fails with invalid and valid email and password combinations
    When User enters "<Email>" in the "Email address" field
    And User enters "<Password>" in the "Password" field
    And User clicks on the "Log In" button
    Then User should see a login error or validation message
    And User should remain on the Login page with title of "Login_page_title"
    Then User clicks back thrice and navigates to Startup page with title of "StartUp_page_title"

    Examples: 
      | Email        | Password        |
      | invalidEmail | invalidPassword |
      | PMvalidEmail | invalidPassword |
      | invalidEmail | PMvalidPassword |

  # Negative login test with various empty credentials
  @Tc_002 @invalid_login_with_different_empty_inputs
  Scenario Outline: Verify login fails with empty email and password combinations
    When User enters "<Email>" in the "Email address" field
    And User enters "<Password>" in the "Password" field
    And User clicks on the "Log In" button
    Then User should see a login error or validation message
    And User should remain on the Login page with title of "Login_page_title"
    Then User clicks back twice and navigates to Startup page with title of "StartUp_page_title"

    Examples: 
      | Email        | Password        |
      | EMPTY        | invalidPassword |
      | invalidEmail | EMPTY           |
      | EMPTY        | EMPTY           |
