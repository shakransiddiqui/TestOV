@Signup-neg @passport
Feature: Negative tests on Sign Up Feature

Background:
  Given User clicks on "Sign Up" button from Homepage
  Then Navigates to the page with title of "Signup_page_title"
   
   @Tc_003  @passport-neg
   Scenario Outline: Sign up should show validation error for missing required fields
    Then User enters "EMPTY" into the "Email address" field
    Then User enters "newPassword" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User enters "jobTitle" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back twice and navigates to Startup page with title of "StartUp_page_title"

  @Tc_004  @passport-neg
   Scenario Outline: Sign up should show validation error for missing required fields
    Then User enters "newEmail" into the "Email address" field
    Then User enters "EMPTY" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User enters "jobTitle" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back twice and navigates to Startup page with title of "StartUp_page_title"
    
     @Tc_005  @passport-neg
   Scenario Outline: Sign up should show validation error for missing required fields
    Then User enters "newEmail" into the "Email address" field
    Then User enters "newPassword" into the "Password" field
    Then User enters "EMPTY" into the "Full Name" field
    Then User enters "jobTitle" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back thrice and navigates to Startup page with title of "StartUp_page_title"
    
     @Tc_006  @passport-neg
   Scenario Outline: Sign up should show validation error for missing required fields
    Then User enters "newEmail" into the "Email address" field
    Then User enters "newPassword" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User enters "EMPTY" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back thrice and navigates to Startup page with title of "StartUp_page_title"