@Homepage
Feature: Homepage_Feature

  @Tc_01 @homepage_to_login @passport-pos
  Scenario: Verify user can navigate to Login page
    Given User clicks on "Sign Up" button
    Then Navigates to the page with title of "Login_page_title"
    Then User navigates back to Homepage

  @Tc_02 @homepage_to_signup @passport-pos
  Scenario: Verify user can navigate to SignUp page
    Given User clicks on "Log In" button
    Then Navigates to the page with title of "Signup_page_title"
    Then User navigates back to Homepage
