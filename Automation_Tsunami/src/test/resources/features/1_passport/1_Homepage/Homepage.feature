@Homepage @Homepage-pos @passport @passport-pos  @demo
Feature: Homepage Feature

  @Tc_01 @homepage_to_login @passport-pos
  Scenario: Verify user can navigate to Login page
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"
    Then User clicks back twice and navigates to Startup page with title of "StartUp_page_title"

  @Tc_02 @homepage_to_signup @passport-pos
  Scenario: Verify user can navigate to SignUp page
    Given User clicks on "Sign Up" button from Homepage
    Then Navigates to the page with title of "Signup_page_title"
    Then User clicks back twice and navigates to Startup page with title of "StartUp_page_title"
    