@StartupProgramApplication @StartupProgramApplication-pos @passport
Feature: Positive tests on Startup Program Application Feature

  @Tc_15 @Verify_Startup_user_can_sign_up_and_apply_to_a_target_program_from_search_results @passport-pos
  Scenario: Verify Startup user can sign up and apply to a target program from search results
    Given User clicks on "Sign Up" button from Homepage
    Then Navigates to the page with title of "Signup_page_title"
    Then User enters "newEmail" into the "Email address" field
    Then User enters "newPassword" into the "Password" field
    Then User enters "fullName" into the "Full Name" field
    Then User clicks on "Sign Up" button
    And User should see "I'm joining Passport because I'm a:" on top
    Then User chooses "Startup Leader"
    Then User enters "startupCompanyName" into the "My company name is" field
    Then User clicks on "Finish" button
    Then User should be redirected to the page with title of "StartUp_page_title"
    Then User saves the generated startup signup credentials for later use
    Then User searches for the configured startup target program
    Then User opens the matching startup target program details from search results
    Then User clicks on Apply Now for the startup target program
    Then User switches to the newly opened startup application tab
    Then User should see the configured startup target program title on the application page
    Then User fills the startup program application form using generated signup data
    Then User uploads the startup pitch deck
    Then User checks Save my responses to use in future applications on Passport for startup application
    Then User clicks on "Submit" button for startup application
    Then User should see the startup application submitted success message
    Then User clicks on "Back to Dashboard" button for startup application
    Then User should be redirected to the page with title of "StartUp_page_title"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"