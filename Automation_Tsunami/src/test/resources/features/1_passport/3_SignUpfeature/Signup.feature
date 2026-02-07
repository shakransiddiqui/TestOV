@Signup @passport
Feature: Sign Up Feature
  
  Background: 
   Given User clicks on "Sign Up" button from Homepage
    Then Navigates to the page with title of "Signup_page_title"

  @tag1
  Scenario: Verify user successfully creates an account.
  Then User enters "newEmail" into the "Email address" field
  Then User enters "newPassword" into the "Password" field
  Then User enters "fullName" into the "Full Name" field
  Then User enters "jobTitle" into the "Job Title" field
  Then User checks the "terms-of-service" checkbox
  Then User clicks on "Sign Up" button
  And User should see "Tell about your startup" on top
  Then User chooses "Startup Leader"
  Then User clicks on "Continue" button
  And User should see "Provide a few pieces of information to get started" on top
  
 