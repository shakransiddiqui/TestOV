@Signup @Signup-neg @passport @passport-neg  @demo
Feature: Negative tests on Sign Up Feature

  Background: 
    Given User clicks on "Sign Up" button from Homepage
    Then Navigates to the page with title of "Signup_page_title"

  @Tc_003 @signup_with_empty_email
  Scenario: Sign up should show validation error when Email address is empty
    Then User enters "EMPTY" into the "Email address" field
    Then User enters "newPassword" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User enters "jobTitle" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back twice and navigates to Startup page with title of "StartUp_page_title"

  @Tc_004 @signup_with_empty_password
  Scenario: Sign up should show validation error when Password is empty
    Then User enters "newEmail" into the "Email address" field
    Then User enters "EMPTY" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User enters "jobTitle" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back twice and navigates to Startup page with title of "StartUp_page_title"

  @Tc_005 @signup_with_empty_full_name
  Scenario: Sign up should show validation error when Full Name is empty
    Then User enters "newEmail" into the "Email address" field
    Then User enters "newPassword" into the "Password" field
    Then User enters "EMPTY" into the "Full Name" field
    Then User enters "jobTitle" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back thrice and navigates to Startup page with title of "StartUp_page_title"

  @Tc_006 @signup_with_empty_job_title
  Scenario: Sign up should show validation error when Job Title is empty
    Then User enters "newEmail" into the "Email address" field
    Then User enters "newPassword" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User enters "EMPTY" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back thrice and navigates to Startup page with title of "StartUp_page_title"

  @Tc_007 @signup_with_invalid_email_format
  Scenario: Sign up should show validation error for invalid Email address format
    Then User enters "invalidEmailFormat" into the "Email address" field
    Then User enters "newPassword" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User enters "jobTitle" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back twice and navigates to Startup page with title of "StartUp_page_title"

  @Tc_008 @signup_with_weak_password
  Scenario: Sign up should show validation error for weak Password
    Then User enters "newEmail" into the "Email address" field
    Then User enters "weakPassword" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User enters "jobTitle" into the "Job Title" field
    Then User checks the "terms-of-service" checkbox
    Then User clicks on "Sign Up" button
    Then User should see a signup error or validation message
    Then User should remain on the Signup page with title of "Signup_page_title"
    Then User clicks back thrice and navigates to Startup page with title of "StartUp_page_title"
