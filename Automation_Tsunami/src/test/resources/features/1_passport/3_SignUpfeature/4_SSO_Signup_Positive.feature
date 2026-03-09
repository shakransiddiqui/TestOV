@SSO-Signup @SSO-Signup-pos @passport 
Feature: Positive tests on SSO Sign Up Feature

  Background: 
    Given User clicks on "Sign Up" button from Homepage
    Then Navigates to the page with title of "Signup_page_title"

 @demo
  Scenario: Verify user successfully creates an account as a Startup Leader using Google
    Then User selects "Continue with Google"
    Then User should see "Sign in with Google" on Google page
    