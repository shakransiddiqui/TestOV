@PassportOS @CommunityAdminPage
Feature: PassportOS Community Admin Page

  Background: 
    Given User is on the PassportOS login page with title of "Login_Page_Title"
    Then User enters "validEmail" in the "username" field
    Then User enters "validPassword" in the "password" field
    Then User clicks on the "Continue" button
    And User is redirected to Dashboard page with title "Dashboard_Page_Title"

    @Tc_03
      Scenario: Verify Create User Feature
      Then User clicks on "admin" button
      Then User is redirected to Admin Page with "Announcements" text on top
      Then User clicks on "Create User" button
      Then User should see "Add User" Page Header
      Then User passes "testEmail" to the "Enter Email or Select Existing User" field and presses on Enter
      Then User clicks on "Add User(s)" button