@SSO-Signup @SSO-Signup-pos @passport
Feature: Positive tests on SSO Sign Up Feature

  Background: 
    Given User clicks on "Sign Up" button from Homepage
    Then Navigates to the page with title of "Signup_page_title"

  @Tc_07 @Verify_user_is_successfully_redirected_Google_Page			@passport-pos
  Scenario: Verify user is successfully redirected Google Page
    Then User selects "Continue with Google"
    Then User should be redirected to the page with title of "Google_page_title"

  @Tc_08 @Verify_user_is_successfully_redirected_LinkedIn_Page			@passport-pos
  Scenario: Verify user is successfully redirected LinkedIn Page
    Then User selects "Continue with LinkedIn"
    Then User should be redirected to the page with title of "LinkedIn_page_title"
