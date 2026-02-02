@Login @Login_passport @Passport
Feature: Passport Login

  Background: 
    Given Navigate to the login page.

  @Tc_01 @passport-neg
  Scenario Outline: Login fails with invalid or empty email and password combinations
    When User enters "<username>" in the username field
    And User enters "<password>" in the password field
    And User clicks on the Login button
    Then User should see a login error or validation message
    And User should remain on the Login page

    Examples: 
      | username       | password |
      | invalid@email2 | wrong123 |

  @Tc_02 @validate_successful_login_with_valid_credentials @passport-pos
  Scenario: 02_User logs in successfully with valid credentials.
    And Enter a valid username and password
    When Click the "Login" button.
    Then Verify that the user is redirected to the Community Dashboard.


  @Tc_03 @validate_unsuccessful_login_with_invalid_email_and_valid_password @passport-neg
  Scenario Outline: Login fails with invalid or empty email and password combinations
    When User enters "<username>" in the username field
    And User enters "<password>" in the password field
    And User clicks on the Login button
    Then User should see a login error or validation message
    And User should remain on the Login page

    Examples: 
      | username       | password      |
      | invalid@email3 | Remyliam@2017 |

  @Tc_04 @validate_unsuccessful_login_with_valid_email_and_invalid_password @passport-neg
  Scenario Outline: Login fails with invalid or empty email and password combinations
    When User enters "<username>" in the username field
    And User enters "<password>" in the password field
    And User clicks on the Login button
    Then User should see a login error or validation message
    And User should remain on the Login page

    Examples: 
      | username                              | password |
      | suzy.ghanem+test32QA@theonevalley.com | wrong123 |
