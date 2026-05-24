@ProgramManagement @ProgramManagement-neg @passport
Feature: Negative tests on Program Management feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"
    When User enters "PMvalidEmail" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"

  @Tc_012 @Verify_Manage_Applications_on_programs_with_unpublished_applications_is_disabled      @passport-neg
  Scenario: Verify Manage Applications on programs with unpublished applications is disabled
    Then User clicks on the Organization Dashboard icon
    And User opens page 1 of the organization programs list
    And User records all programs with unpublished applications across organization pages
    And User verifies Manage Applications is disabled for every recorded unpublished program
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"