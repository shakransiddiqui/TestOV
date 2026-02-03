Feature: Startup applies to a program and saves responses

  Background: 
    Given User completes initial startup setup successfully

  @Tc_26 @validate_Startup_Program_Application @passport-pos
  Scenario: 26_Startup completes program application and saves responses
    And Click on "See More" button for Startup Program
    When Click on "Apply Now" button for Startup Program
    And User fills the application form using existing reusable method
    And Upload pitch deck file
    And Check "Save my responses" checkbox
    And Click "Submit" button
    And Verify that the success message "Thank you for submitting your application!" is displayed
