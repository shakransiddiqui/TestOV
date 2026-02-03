@Login @passport
Feature: Login_Feature

  Background: 
    Given User clicks on "Log In" button
    Then Navigates to the page with title of "Login_page_title"

  # Negative login test with various invalid, valid, and empty credentials
  @Tc_02 @passport-neg
  Scenario Outline: Verify login fails with invalid, valid, or empty username and password combinations
    When User enters "<Email>" in the "Email address" field
    And User enters "<Password>" in the "Password" field
    And User clicks on the "Log In" button
    Then User should see a login error or validation message
    And User should remain on the Login page

    Examples: 
      | Email        | Password        |
      | invalidEmail | invalidPassword |
      | validEmail   | invalidPassword |
      | invalidEmail | validPassword   |
      | EMPTY        | invalidPassword |
      | invalidEmail | EMPTY           |
      | EMPTY        | EMPTY           |

  # Positive login test with valid credentials
  @Tc_04 @passport-pos
  Scenario: Verify user logs in successfully with valid credentials
    When User enters "validUsername" in the "Email address" field
    And User enters "validPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the Community Dashboard
