@ApplicationEvaluation @ApplicationEvaluation-pos @passport
Feature: Positive tests on Application Evaluation feature

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

  @Tc_38 @Verify_Program_Manager_can_open_an_application_evaluation_details_page  @passed
  Scenario: Verify Program Manager user can open an application evaluation details page from the applications list
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_39 @Verify_Program_Manager_can_return_to_application_list_from_application_evaluation_details_page    @passed
  Scenario: Verify Program Manager user can return to the application list from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User clicks on "Application List" from the application evaluation details page
    Then User verifies the applications list is displayed again for the same target program
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_40 @Verify_Program_Manager_can_change_application_status_to_On_Hold_from_application_evaluation_details_page     @passed
  Scenario: Verify Program Manager user can change an application status to On Hold from the application evaluation details page and restore it
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User changes the selected application evaluation status to "On Hold"
    Then User verifies the selected application evaluation status is "On Hold"
    When User changes the selected application evaluation status to "Needs Review"
    Then User verifies the selected application evaluation status is "Needs Review"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_41 @Verify_Program_Manager_can_change_application_status_to_Accepted_from_application_evaluation_details_page    @passed
  Scenario: Verify Program Manager user can change an application status to Accepted from the application evaluation details page and restore it
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User changes the selected application evaluation status to "Accepted"
    Then User verifies the application evaluation acceptance confirmation modal is displayed
    And User chooses "No Thanks" from the status confirmation modal
    And User verifies the selected application evaluation status is "Accepted"
    When User changes the selected application evaluation status to "Needs Review"
    Then User verifies the selected application evaluation status is "Needs Review"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_42 @Verify_Program_Manager_can_change_application_status_to_Rejected_from_application_evaluation_details_page  @passed
  Scenario: Verify Program Manager user can change an application status to Rejected from the application evaluation details page and restore it
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User changes the selected application evaluation status to "Rejected"
    Then User verifies the application evaluation rejection confirmation modal is displayed
    And User chooses "No Thanks" from the status confirmation modal
    And User verifies the selected application evaluation status is "Rejected"
    When User changes the selected application evaluation status to "Needs Review"
    Then User verifies the selected application evaluation status is "Needs Review"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_43 @Verify_Program_Manager_can_submit_rating_from_My_Rating_on_application_evaluation_details_page   @passed
  Scenario: Verify Program Manager user can submit a rating from My Rating on the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    And User verifies "My Rating" is selected on the application evaluation details page
    When User selects a star rating on the application evaluation details page
    And User enters a rating comment on the application evaluation details page
    And User clicks on "Submit" from the application evaluation details page
    Then User verifies the rating was submitted successfully on the application evaluation details page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_44 @Verify_Program_Manager_can_view_All_Ratings_on_application_evaluation_details_page    @passed
  Scenario: Verify Program Manager user can open All Ratings on the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User clicks on "All Ratings" from the application evaluation details page
    Then User verifies the "All Ratings" view is displayed on the application evaluation details page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_45 @Verify_Program_Manager_can_navigate_between_applications_from_application_evaluation_details_page    @passed
  Scenario: Verify Program Manager user can navigate between applications from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    And User records the current application name from the application evaluation details page
    When User clicks on the next application arrow from the application evaluation details page
    Then User verifies the displayed application changed on the application evaluation details page
    When User clicks on the previous application arrow from the application evaluation details page
    Then User verifies the previously recorded application is displayed again on the application evaluation details page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_46 @Verify_Program_Manager_can_open_and_close_email_composer_from_application_evaluation_details_page  @passed
  Scenario: Verify Program Manager user can open and close the email composer from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User clicks on the email icon from the application evaluation details page
    Then User verifies the standalone row email composer modal is displayed
    When User closes the standalone row email composer modal without sending
    Then User verifies the selected application evaluation details page is displayed
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_47 @Verify_Program_Manager_can_open_Go_To_Program_Page_from_application_evaluation_details_page  @passed
  Scenario: Verify Program Manager user can open Go To Program Page from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User clicks on the Go To Program Page icon from the application evaluation details page
    Then User switches to the newly opened Program page tab from the application evaluation details page
    And User verifies the Program page is displayed from the application evaluation details page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_48 @Verify_Program_Manager_can_open_the_three_dot_menu_from_application_evaluation_details_page  @passed
  Scenario: Verify Program Manager user can open the three-dot menu from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User opens the three-dot menu from the application evaluation details page
    Then User verifies "Edit Program Description" is displayed in the three-dot menu from the application evaluation details page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_49 @Verify_Program_Manager_can_open_Edit_Program_Description_from_application_evaluation_details_page  @passed
  Scenario: Verify Program Manager user can open Edit Program Description from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User opens the three-dot menu from the application evaluation details page
    And User clicks on "Edit Program Description" from the three-dot menu on the application evaluation details page
    Then User switches to the newly opened Edit Program tab from the application evaluation details page
    And User verifies the Edit Program page and form are displayed from the application evaluation details page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_50 @Verify_Program_Manager_can_use_the_copy_link_icon_from_application_evaluation_details_page  @passed
  Scenario: Verify Program Manager user can use the copy link icon from the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User clicks on the copy link icon from the application evaluation details page
    Then User verifies the copy link feedback is displayed on the application evaluation details page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_51 @Verify_Program_Manager_can_see_application_status_persist_after_refresh_on_application_evaluation_details_page  @passed
  Scenario: Verify Program Manager user can see the application status persist after refresh on the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    When User changes the selected application evaluation status to "On Hold"
    Then User verifies the selected application evaluation status is "On Hold"
    When User refreshes the application evaluation details page
    Then User verifies the selected application evaluation status is "On Hold"
    When User changes the selected application evaluation status to "Needs Review"
    Then User verifies the selected application evaluation status is "Needs Review"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_52 @Verify_Program_Manager_can_see_submitted_rating_persist_after_refresh_on_application_evaluation_details_page  @Bug
  Scenario: Verify Program Manager user can see the submitted rating persist after refresh on the application evaluation details page
    When User clicks on the first application from the evaluation results
    Then User verifies the selected application evaluation details page is displayed
    And User verifies "My Rating" is selected on the application evaluation details page
    When User selects a star rating on the application evaluation details page
    And User enters a rating comment on the application evaluation details page
    And User clicks on "Submit" from the application evaluation details page
    Then User verifies the rating was submitted successfully on the application evaluation details page
    When User refreshes the application evaluation details page
    Then User verifies the submitted rating still persists on the
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"