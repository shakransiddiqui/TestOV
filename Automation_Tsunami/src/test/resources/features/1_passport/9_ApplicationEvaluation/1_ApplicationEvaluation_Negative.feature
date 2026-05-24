@ApplicationEvaluation @ApplicationEvaluation-neg @passport
Feature: Negative tests on Application Evaluation feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"
    When User enters "PMvalidEmail" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"
    And User opens Manage Applications for the target program from config key "programEvaluationTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page

  @Tc_013 @Verify_Program_Manager_cannot_submit_rating_without_selecting_stars_on_application_evaluation_details_page   @passed
  Scenario: Verify Program Manager user cannot submit a rating without selecting stars on the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    And User verifies "My Rating" is selected on the application evaluation details page
    When User clicks on "Submit" from the application evaluation details page
    Then User verifies the rating was not submitted without selecting stars on the application evaluation details page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_014 @Verify_Program_Manager_can_dismiss_Accepted_confirmation_modal_from_application_evaluation_details_page   @passed
  Scenario: Verify Program Manager user can dismiss the Accepted confirmation modal from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User changes the selected application evaluation status to "Accepted"
    Then User verifies the application evaluation acceptance confirmation modal is displayed
    When User closes the status confirmation modal from the application evaluation details page
    Then User verifies the selected application evaluation details page is displayed
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_015 @Verify_Program_Manager_can_dismiss_Rejected_confirmation_modal_from_application_evaluation_details_page @passed
  Scenario: Verify Program Manager user can dismiss the Rejected confirmation modal from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User changes the selected application evaluation status to "Rejected"
    Then User verifies the application evaluation rejection confirmation modal is displayed
    When User closes the status confirmation modal from the application evaluation details page
    Then User verifies the selected application evaluation details page is displayed
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_016 @Verify_Program_Manager_can_close_email_composer_without_sending_from_application_evaluation_details_page   @passed
  Scenario: Verify Program Manager user can close the email composer without sending from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User clicks on the email icon from the application evaluation details page
    Then User verifies the standalone row email composer modal is displayed
    When User closes the standalone row email composer modal without sending
    Then User verifies the selected application evaluation details page is displayed
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_017 @Verify_Program_Manager_can_view_No_Submitted_Ratings_empty_state_on_application_evaluation_details_page  @passed
  Scenario: Verify Program Manager user can view the No Submitted Ratings empty state on the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User clicks on "All Ratings" from the application evaluation details page
    Then User verifies the No Submitted Ratings empty state is displayed on the application evaluation details page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"