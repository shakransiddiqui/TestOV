@ProgramManagement @ProgramManagement-pos @passport
Feature: Positive tests on Program Management feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"
    When User enters "PMvalidEmail" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"

  @ManualTest
  Scenario: Verify Program Manager user can delete target programs
    Then User clicks on the Organization Dashboard icon
    And User opens page 1 of the organization programs list
    And User records all programs with unpublished applications across organization pages
    And User verifies Manage Applications is disabled for every recorded unpublished program
    And User records all deletable programs across organization pages
    When User deletes the recorded eligible programs from the organization pages
    Then User verifies the delete confirmation modal was displayed correctly for each deleted program
    And User verifies the recorded eligible programs were removed from the organization pages
    And User verifies the configured target program with applications still remains on the organization pages
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"