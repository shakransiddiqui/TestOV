@SignUp
Feature: SignUp_Feature

  Background: 
    Given User is on the Passport Sign Up page

  @Tc_05 @validate_signup_without_accepting_terms   @passport-neg
  Scenario Outline: Signup fails when Terms and Conditions are not accepted
    When User enters "<email>" in the email address field
    And User enters "<password>" in the password field in signup page
    And User enters "<fullName>" in the full name field
    And User enters "<jobTitle>" in the job title field
    And User clicks on the Sign Up button
    Then User should see the error message "<errorMessage>"
    And User should remain on the Sign Up page

    Examples: 
      | email                               | password      | fullName | jobTitle    | errorMessage                                     |
      | suzy.ghanem+signup@theonevalley.com | Remyliam@2017 | Suzy G   | QA Engineer | You must accept the Terms of Service to register |

  @Tc_06 @validate_unsuccessful_signup_with_invalid_email_format    @passport-neg
  Scenario Outline: Verify user fails to sign up with invalid email format
    When User enters "<email>" in the email address field
    And User enters "<password>" in the password field
    And User enters "<fullName>" in the full name field
    And User enters "<jobTitle>" in the job title field
    And User accepts the Terms and Conditions
    And  User clicks on the Sign Up button
    And User receives signup error or validation message 
    And User should remain on the Sign Up page

    Examples: 
     | email                               | password      | fullName | jobTitle    |
     | suzy.ghanem+signup@theonevalley     | Remyliam@2017 | Suzy G   | QA Engineer |

@Tc_07 @validate_unsuccessful_signup_with_existing_email      @passport-neg
  Scenario Outline: Verify user fails to sign up with allready registered email address
    When User enters "<email>" in the email address field
    And User enters "<password>" in the password field
    And User enters "<fullName>" in the full name field
    And User enters "<jobTitle>" in the job title field
    And User accepts the Terms and Conditions
    And  User clicks on the Sign Up button
    And User receives signup error or validation message of already registered email
    And User should remain on the Sign Up page

    Examples: 
     | email                                | password      | fullName | jobTitle    |
     | suzy.ghanem+test32QA@theonevalley.com| Remyliam@2017 | Suzy G   | QA Engineer |
     
  @Tc_08 @validate_unsuccessful_signup_with_missing_email_required_field     @passport-neg
  Scenario Outline: Verify user fails to sign up when a email required field is missing
    When User enters "<email>" in the email address field
    And User enters "<password>" in the password field
    And User enters "<fullName>" in the full name field
    And User enters "<jobTitle>" in the job title field
    And User accepts the Terms and Conditions
    And  User clicks on the Sign Up button
    And User receives signup error or validation message for missing fields
    And User should remain on the Sign Up page

    Examples: 
      | email | password      | fullName | jobTitle    |
      |       | Remyliam@2017 | Suzy G   | QA Engineer |
      
   @Tc_09 @validate_unsuccessful_signup_with_missing_any_other_required_fields   @passport-neg
  Scenario Outline: Verify user fails to sign up when a fullname required field is missing
    When User enters "<email>" in the email address field
    And User enters "<password>" in the password field
    And User enters "<fullName>" in the full name field
    And User enters "<jobTitle>" in the job title field
    And User accepts the Terms and Conditions
    And  User clicks on the Sign Up button
    And User receives signup error or validation message for missing any other fields
    And User should remain on the Sign Up page

    Examples: 
      | email                                | password      | fullName | jobTitle    |
      |  suzy.ghanem+signup@theonevalley.com | Remyliam@2017 |          | QA Engineer |
      
       
   @Tc_10 @validate_unsuccessful_signup_with_missing_password_required_fields   @passport-neg
  Scenario Outline: Verify user fails to sign up when a password required field is missing
    When User enters "<email>" in the email address field
    And User enters "<password>" in the password field
    And User enters "<fullName>" in the full name field
    And User enters "<jobTitle>" in the job title field
    And User accepts the Terms and Conditions
    And  User clicks on the Sign Up button
    And User receives signup error or validation message for missing password field
    And User should remain on the Sign Up page

    Examples: 
      | email                                | password      | fullName | jobTitle    |
      |  suzy.ghanem+signup@theonevalley.com |               | Suzy G   | QA Engineer |
      
    @Tc_11 @validate_unsuccessful_signup_with_invalid_password_required_field   @passport-neg
  Scenario Outline: Verify user fails to sign up when a invalid password entered 
    When User enters "<email>" in the email address field
    And User enters "<password>" in the password field
    And User enters "<fullName>" in the full name field
    And User enters "<jobTitle>" in the job title field
    And User accepts the Terms and Conditions
    And  User clicks on the Sign Up button
    And User receives signup error or validation message for invalid password field
    And User should remain on the Sign Up page

    Examples: 
      | email                                | password      | fullName | jobTitle    |
      |  suzy.ghanem+signup@theonevalley.com |  suzy         | Suzy G   | QA Engineer |


  @Tc_12 @Validate_Successful_Account_Creation    @passport-pos
  Scenario: 12_User Successfully Creates An Account
    Given Navigate to the sign-up page .
    When Enter a valid email address .
    And Enter valid password meeting all requirements.
    And Enter full name .
    And Enter job title .
    Then Check the terms and conditions checkbox .
    And Click the Sign Up button .
    Then Verify that the user is redirected to the role selection page

  @Tc_13 @validate_Post_Sign_Up_Role_Selection   @passport-pos
  Scenario: 13_User selects Startup Leader role after account creation
    Given Navigate to the sign-up page .
    When Enter a valid email address .
    And Enter valid password meeting all requirements.
    And Enter full name .
    And Enter job title .
    Then Check the terms and conditions checkbox .
    And Click the Sign Up button .
    Then Verify that the user is redirected to the role selection page
    Given Verify that "Welcome to Passport" page is displayed.
    When Select StartUp Leader .
    And Click the continue button .
    Then Verify that the Startup information is displayed .

  @Tc_14 @validate_Startup_Information_Initial_Setup   @passport-pos
  Scenario: 14_User completes initial startup setup
    Given Navigate to the sign-up page .
    When Enter a valid email address .
    And Enter valid password meeting all requirements.
    And Enter full name .
    And Enter job title .
    Then Check the terms and conditions checkbox .
    And Click the Sign Up button .
    Then Verify that the user is redirected to the role selection page
    Given Verify that "Welcome to Passport" page is displayed.
    When Select StartUp Leader .
    And Click the continue button .
    Then Verify that the Startup information is displayed .
    Given The user is on the Startup Information page
    When Enter a valid Startup name
    And Enter a valid location
    And Click the finish button
    Then Verify user is redirected to search program
