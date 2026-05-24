@ApplicationManagement @ApplicationManagement-pos @passport
Feature: Positive tests on Application Management feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"
    When User enters "PMvalidEmail" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"

  @Tc_16 @Verify_Program_Manager_user_can_export_applications_from_a_target_program @passport-pos
  Scenario: Verify Program Manager user can export applications from a target program
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens the export menu from the evaluation page
    And User exports the applications for the target program from config key "programManagerTargetProgramName"
    Then Downloaded applications zip should contain Applicant_Summaries.csv and Score_Details.csv
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_17 @Verify_pagination_logic_of_applications_in_a_target_program @passport-pos
  Scenario: Verify pagination logic of applications in a target program
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User records the target program total applications count
    And User verifies evaluation pagination is displayed for the target program applications
    And User verifies the current evaluation page is "1"
    And User records the current page applications count from the All filter
    And User records the application rows displayed on the current page
    Then User verifies no more than 10 application rows are displayed on the current page
    And User verifies the current page All filter count matches the displayed application rows
    When User opens evaluation page "2"
    Then User verifies the current evaluation page is "2"
    And User records the current page applications count from the All filter
    And User records the application rows displayed on the current page
    And User verifies application rows are displayed on the current evaluation page
    And User verifies the current page All filter count matches the displayed application rows
    And User records the last evaluation page number
    When User opens the last evaluation page
    Then User verifies the current evaluation page is the last evaluation page
    And User records the current page applications count from the All filter
    And User records the application rows displayed on the current page
    And User verifies the last evaluation page displays the expected remaining application rows
    And User verifies the current page All filter count matches the displayed application rows
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_18 @Verify_Program_Manager_can_search_for_an_application_across_all_pages_in_a_target_program  @stillwaitingforittobedeployed
  Scenario: Verify Program Manager user can search for an application across all pages in a target program
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    When User searches for the configured target application from the evaluation page
    Then Search Results section should appear on the evaluation page
    And Matching application row(s) should be displayed for the configured target application
    And Each matching application row startup name should contain the configured target application name
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_19 @Verify_Program_Manager_can_change_target_Applications_status_to_On_Hold @failed
  Scenario: Verify Program Manager user can change a single application status to On Hold and current page filter counts update
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User locates the configured status target application and records its current page status details
    When User changes the configured status target application status to "On Hold"
    Then User verifies the configured status target application status and current page filter counts reflect the "On Hold" change
    And User restores the configured status target application status to "Needs Review" and verifies the current page filter counts are restored
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_20 @Verify_Program_Manager_can_change_random_individual_Applications_status_to_On_Hold @failed
  Scenario: Verify Program Manager user can change a random number of page 1 application statuses to On Hold and current page filter counts update
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    And User verifies all 10 applications on page 1 are in "Needs Review" status
    When User changes a random 2 to 5 applications on page 1 to "On Hold"
    Then User verifies page 1 application statuses and filter counts reflect the random "On Hold" updates
    And User restores all randomly changed page 1 applications to "Needs Review" and verifies page 1 filter counts are restored
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_21 @Verify_Program_Manager_can_change_individual_Applications_status_to_Accepted @failed
  Scenario: Verify Program Manager user can change page 1 application statuses to Accepted using both No Thanks and Yes Send Email flows
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    And User verifies all 10 applications on page 1 are in "Needs Review" status
    When User selects a random page 1 application in "Needs Review" status for the acceptance flow with "No Thanks"
    And User changes the selected page 1 acceptance application status to "Accepted"
    Then User verifies the acceptance send email confirmation modal is displayed
    And User chooses "No Thanks" from the status confirmation modal
    And User verifies page 1 statuses and filter counts reflect 1 application changed to "Accepted"
    When User selects another random page 1 application in "Needs Review" status for the acceptance flow with "Yes, Send Email"
    And User changes the selected page 1 acceptance application status to "Accepted"
    Then User verifies the acceptance send email confirmation modal is displayed
    And User chooses "Yes, Send Email" from the status confirmation modal
    And User verifies the acceptance email composer modal is displayed
    And User sends the acceptance email and verifies the email sent confirmation
    And User closes the email sent confirmation modal
    And User verifies page 1 statuses and filter counts reflect 2 applications changed to "Accepted"
    And User restores all accepted page 1 applications to "Needs Review" and verifies page 1 filter counts are restored
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_22 @Verify_Program_Manager_can_change_individual_Applications_status_to_Rejected @failed
  Scenario: Verify Program Manager user can change page 1 application statuses to Rejected using both No Thanks and Yes Send Email flows
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    And User verifies all 10 applications on page 1 are in "Needs Review" status
    When User selects a random page 1 application in "Needs Review" status for the rejection flow with "No Thanks"
    And User changes the selected page 1 rejection application status to "Rejected"
    Then User verifies the rejection send email confirmation modal is displayed
    And User chooses "No Thanks" from the status confirmation modal
    And User verifies page 1 rejection statuses and filter counts reflect 1 application changed to "Rejected"
    When User selects another random page 1 application in "Needs Review" status for the rejection flow with "Yes, Send Email"
    And User changes the selected page 1 rejection application status to "Rejected"
    Then User verifies the rejection send email confirmation modal is displayed
    And User chooses "Yes, Send Email" from the status confirmation modal
    And User verifies the rejection email composer modal is displayed
    And User sends the rejection email and verifies the email sent confirmation
    And User closes the email sent confirmation modal
    And User verifies page 1 rejection statuses and filter counts reflect 2 applications changed to "Rejected"
    And User restores all rejected page 1 applications to "Needs Review" and verifies page 1 filter counts are restored
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_23 @Verify_Program_Manager_can_Change_Multiple_Application_status_using_Bulk_Action @passport-pos
  Scenario: Verify Program Manager user can bulk update all page 1 application statuses through On Hold Accepted Rejected and restore them
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    And User verifies all 10 applications on page 1 are in "Needs Review" status
    Then User verifies the Bulk Action dropdown is disabled when no applications are selected
    When User selects all 10 page 1 applications using the Select All checkbox
    Then User verifies the Bulk Action dropdown is enabled
    When User applies the bulk action "Mark as On Hold"
    Then User verifies page 1 application statuses and filter counts reflect all applications changed to "On Hold"
    When User individually reselects all 10 page 1 applications in random order
    And User applies the bulk action "Mark as Accepted"
    Then User verifies the bulk acceptance send email confirmation modal is displayed
    And User chooses "No Thanks" from the status confirmation modal
    And User verifies page 1 application statuses and filter counts reflect all applications changed to "Accepted"
    When User individually reselects all 10 page 1 applications in random order
    And User applies the bulk action "Mark as Rejected"
    Then User verifies the bulk rejection send email confirmation modal is displayed
    And User chooses "No Thanks" from the status confirmation modal
    And User verifies page 1 application statuses and filter counts reflect all applications changed to "Rejected"
    When User selects all 10 page 1 applications using the Select All checkbox
    And User applies the bulk action "Mark as Needs Review"
    Then User verifies page 1 application statuses and filter counts reflect all applications changed to "Needs Review"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_24 @Verify_Program_Manager_can_send_email_from_single_application_row @passport-pos
  Scenario: Verify Program Manager user can send an email from a single page 1 application using the row email action
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    #  And User verifies all 10 applications on page 1 are in "Needs Review" status
    When User selects a random page 1 application in "Needs Review" status for the row email action
    And User opens the email composer from the selected page 1 application row
    Then User verifies the standalone row email composer modal is displayed
    And User sends the standalone row email and verifies the email sent confirmation
    And User closes the email sent confirmation modal
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_25 @Verify_Program_Manager_can_send_bulk_email_from_selected_page_1_applications @passport-pos
  Scenario: Verify Program Manager user can send a bulk email from selected page 1 applications
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    And User verifies all 10 applications on page 1 are in "Needs Review" status
    Then User verifies the Bulk Action dropdown is disabled when no applications are selected
    When User selects all 10 page 1 applications using the Select All checkbox
    Then User verifies the Bulk Action dropdown is enabled
    When User applies the bulk action "Send Email"
    Then User verifies the bulk email composer modal is displayed
    And User sends the bulk email and verifies the email sent confirmation
    And User closes the email sent confirmation modal
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_26 @Verify_Program_Manager_can_send_bulk_email_from_random_selected_page_1_applications @passport-pos
  Scenario: Verify Program Manager user can send a bulk email from a random subset of selected page 1 applications
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    And User verifies all 10 applications on page 1 are in "Needs Review" status
    Then User verifies the Bulk Action dropdown is disabled when no applications are selected
    When User selects a random 2 to 5 page 1 applications for the bulk email action
    And User individually selects the random page 1 applications for the bulk email action
    Then User verifies the Bulk Action dropdown is enabled
    When User applies the bulk action "Send Email"
    Then User verifies the bulk email composer modal is displayed
    And User sends the bulk email and verifies the email sent confirmation
    And User closes the email sent confirmation modal
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_27 @Verify_Program_Manager_can_close_single_application_row_email_without_sending    @passport-pos
  Scenario: Verify Program Manager user can close a single page 1 row email composer without sending
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    When User selects a random page 1 application in "Needs Review" status for the row email action
    And User opens the email composer from the selected page 1 application row
    Then User verifies the standalone row email composer modal is displayed
    When User closes the standalone row email composer modal without sending
    Then User verifies all 10 applications on page 1 are in "Needs Review" status
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_28 @Verify_Program_Manager_can_close_bulk_email_without_sending    @passport-pos
  Scenario: Verify Program Manager user can close a bulk email composer without sending
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    When User selects all 10 page 1 applications using the Select All checkbox
    Then User verifies the Bulk Action dropdown is enabled
    When User applies the bulk action "Send Email"
    Then User verifies the bulk email composer modal is displayed
    When User closes the standalone row email composer modal without sending
    Then User verifies all 10 applications on page 1 are in "Needs Review" status
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_29 @Verify_Program_Manager_can_navigate_back_to_dashboard_from_evaluation_page @passport-pos
  Scenario: Verify Program Manager user can navigate back to dashboard from the evaluation page
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    When User clicks on the "Back to Dashboard" button from evaluation page
    Then User should be redirected to the page with title of "Organization_page_title"
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_30 @Verify_Program_Manager_can_open_invite_flow_from_evaluation_page   @passport-pos
  Scenario: Verify Program Manager user can open the Invite flow from the evaluation page
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    When User clicks on the "Invite" button from evaluation page
    Then User verifies the Invite modal is displayed from evaluation page
    And User adds a random invite email and verifies it appears in the invite list from evaluation page
    And User deletes the added invite email and verifies it is removed from the invite list from evaluation page
    And User closes the Invite modal using Cancel from evaluation page
    When User clicks on the "Invite" button from evaluation page
    Then User verifies the Invite modal is displayed from evaluation page
    And User adds a random invite email and verifies it appears in the invite list from evaluation page
    When User sends the invite from evaluation page and verifies the Invite modal closes
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_31 @Verify_Program_Manager_can_open_edit_program_description_from_evaluation_header_menu   @passport-pos
  Scenario: Verify Program Manager user can open Edit Program Description from the evaluation header menu
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    When User opens the evaluation header menu
    Then User verifies "Edit Program Description" is displayed in the evaluation header menu
    When User clicks on "Edit Program Description" from the evaluation header menu
    Then User switches to the newly opened Edit Program tab
    And User verifies the Edit Program page and form are displayed
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_32 @Verify_Program_Manager_can_open_preview_application_from_evaluation_subsection_menu   @passport-pos
  Scenario: Verify Program Manager user can open Preview Application from the evaluation subsection menu
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    When User opens the evaluation subsection menu
    Then User verifies "Preview Application" is displayed in the evaluation subsection menu
    When User clicks on "Preview Application" from the evaluation subsection menu
    Then User switches to the newly opened Preview Application tab
    And User verifies the Preview Application page, form preview, and Back to Application button are displayed
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_33 @Verify_Program_Manager_can_open_edit_application_from_evaluation_subsection_menu   @passport-pos
  Scenario: Verify Program Manager user can open Edit Application from the evaluation subsection menu
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    When User opens the evaluation subsection menu
    Then User verifies "Edit Application" is displayed in the evaluation subsection menu
    When User clicks on "Edit Application" from the evaluation subsection menu
    Then User switches to the newly opened Edit Application tab
    And User verifies the Edit Application page, form, and Preview Application button are displayed
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_34 @Verify_Program_Manager_can_validate_evaluation_filter_tabs @failed
  Scenario: Verify Program Manager user can validate the evaluation filter tabs on page 1
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    And User verifies all 10 applications on page 1 are in "Needs Review" status
    When User prepares page 1 applications for evaluation filter tab validation
    Then User clicks on the "Needs Review" evaluation filter tab
    And User verifies the "Needs Review" evaluation filter tab displays 7 page 1 applications in "Needs Review" status
    Then User clicks on the "On Hold" evaluation filter tab
    And User verifies the "On Hold" evaluation filter tab displays 1 page 1 applications in "On Hold" status
    Then User clicks on the "Accepted" evaluation filter tab
    And User verifies the "Accepted" evaluation filter tab displays 1 page 1 applications in "Accepted" status
    Then User clicks on the "Rejected" evaluation filter tab
    And User verifies the "Rejected" evaluation filter tab displays 1 page 1 applications in "Rejected" status
    Then User clicks on the "All" evaluation filter tab
    And User verifies the "All" evaluation filter tab displays all 10 page 1 applications
    When User restores the evaluation filter tab validation applications to "Needs Review"
    Then User verifies page 1 filter counts are restored after evaluation filter tab validation
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_35 @Verify_Program_Manager_can_open_go_to_program_page_from_evaluation_quick_action @Passsed
  Scenario: Verify Program Manager user can open Go To Program Page from the evaluation page quick action
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    Then User verifies the "Go To Program Page" quick action is displayed on evaluation page
    When User clicks on the "Go To Program Page" quick action from evaluation page
    Then User switches to the newly opened Program page tab
    Then User verifies the Program page and application card are displayed from the evaluation quick action
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_36 @Verify_Program_Manager_can_enable_and_disable_bulk_action_with_select_all_checkbox @Passsed
  Scenario: Verify Program Manager user can enable and disable Bulk Action using the Select All checkbox
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    Then User verifies the Bulk Action dropdown is disabled when no applications are selected
    When User selects all 10 page 1 applications using the Select All checkbox
    Then User verifies the Bulk Action dropdown is enabled
    When User clears all 10 page 1 applications using the Select All checkbox
    Then User verifies the Bulk Action dropdown is disabled when no applications are selected
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_37 @Verify_Program_Manager_can_open_application_from_startup_name_on_evaluation_page @Passsed
  Scenario: Verify Program Manager user can open an application from the startup name on the evaluation page
    And User opens Manage Applications for the target program from config key "programManagerTargetProgramName"
    Then User switches to the newly opened application evaluation tab
    And User waits for the target program applications to load on evaluation page
    And User opens page 1 of the evaluation results
    When User selects a random page 1 application name from the evaluation table
    And User clicks on the selected page 1 application name from the evaluation table
    And User verifies the selected application details page is displayed
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"