@ApplicationCreation @ApplicationCreation-pos @passport
Feature: Positive tests on Application Creation Feature

  Background: 
    Given User clicks on "Log In" button from Homepage
    Then Navigates to the page with title of "Login_page_title"
    When User enters "PMvalidEmail" in the "Email address" field
    And User enters "PMvalidPassword" in the "Password" field
    And User clicks on the "Log In" button
    Then User should be redirected to the page with title of "Organization_page_title"
    Then User clicks on "Create a new program" button
    Then User should be redirected to the page with title of "CreateProgram_page_title"

  @Tc_10 @Verify_new_application_creation_with_all_types_Standard_and_Additional_Questions @passport-pos
  Scenario: Verify new application creation with all types of Standard and Additional Questions
    Then User enters "programWithStandardAndAdditionalQuestions" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User clicks on "Save & Create Application" button
    Then User should be redirected to the page with title of "CreateApplication_page_title"
    And User should see "Create Application"
    Then User enters "Standard_Additional_applicationTitle" into the "Application Title" field of Create application
    And User selects the "Create New Application" option on Create Application page
    And User clicks on "Save & Continue" button
    And User should see "Basic Information" in the Application Builder page
    And User clicks the collapse icon on the Application Builder page
    Then Standard Questions should be collapsed
    And User scrolls to "Additional Questions" section
    And User clicks on "Add New Question" in Additional Questions section
    Then User should see the New Question form
    And User fills the Additional Questions form with the following data
      | Type             | Question                                     | Instruction                               | Required |
      | Single-Line Text | What is your company's tagline?              | Short, single line (≤100 chars).          | Yes      |
      | Multi-Line Text  | Describe your product in one paragraph.      | 3–5 sentences; problem, solution, users.  | No       |
      | Number           | How many paying customers do you have today? | Whole number only (no commas).            | Yes      |
      | Date             | When did you found the company?              | Pick a valid founding date.               | No       |
      | Email            | What is your main contact email address?     | Valid format (name@domain).               | Yes      |
      | Single Choice    | What is your primary business model?         | Choose one option.                        | Yes      |
      | Multiple Choice  | Which platforms do you support?              | Select all that apply.                    | Yes      |
      | File             | Upload your current pitch deck.              | PDF only, max 25 MB.                      | Yes      |
      | Phone Number     | What is your main contact phone number?      | Include country code if outside US.       | Yes      |
      | Website/URL      | What is your company website?                | Full URL, e.g., https://example.com/      | Yes      |
      | Address          | What is your company headquarters address?   | Street, City, State, Postal Code, Country | Yes      |
    And User clicks on "Save & Continue" button on the Application Builder page
    Then User verifies Rubric section components
    And User clicks on "Save & Continue" button on the Application Builder page
    Then User sets the Publish Open Date to now
    Then User copies the Application Link on the Publish page
    Then User adds a random invite email and verifies it appears in the list
    Then User deletes the last added invite email and verifies it is removed
    Then User clicks on "Complete" button on the Publish page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_11 @Verify_Preview_a_new_application_with_all_types_of_Standard_Questions @passport-pos
  Scenario: Verify Preview a new application with all types of Standard Questions and no Additional Questions
    Then User enters "programWithStandardQuestions" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User clicks on "Save & Create Application" button
    Then User should be redirected to the page with title of "CreateApplication_page_title"
    And User should see "Create Application"
    Then User enters "Standard_applicationTitle" into the "Application Title" field of Create application
    And User selects the "Create New Application" option on Create Application page
    And User clicks on "Save & Continue" button
    And User should see "Basic Information" in the Application Builder page
    And User should see the following Standard Questions on the Application Builder page
      | Question                      |
      | Job Title                     |
      | Startup Location              |
      | Industries                    |
      | Website                       |
      | LinkedIn                      |
      | Company Description           |
      | Year of Founding              |
      | Funding                       |
      | Number of full-time employees |
      | Milestones                    |
      | Pitch Deck                    |
    And User clicks on "Preview Application" button on the Application Builder page
    Then User should be on the Preview Application page
    And User should be on the Standard Questions tab
    And Applicant should see the following Standard Questions on the Preview page
      | Question                      |
      | Job Title                     |
      | Startup Location              |
      | Industries                    |
      | Website                       |
      | LinkedIn                      |
      | Company Description           |
      | Year of Founding              |
      | Funding                       |
      | Number of full-time employees |
      | Milestones                    |
      | Pitch Deck                    |
    And Checks on Save my responses to use in future applications on Passport
    And Clicks on "Back to Application" button on the Preview Application page
    And User clicks on "Save & Continue" button on the Application Builder page
    Then User verifies Rubric section components
    And User clicks on "Save & Continue" button on the Application Builder page
    Then User sets the Publish Open Date to now
    Then User copies the Application Link on the Publish page
    Then User adds a random invite email and verifies it appears in the list
    Then User deletes the last added invite email and verifies it is removed
    Then User clicks on "Complete" button on the Publish page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_12 @Verify_Preview_a_new_application_with_all_types_of_Additional_Questions_and_no_Standard_Questions @passport-pos
  Scenario: Verify Preview a new application with all types of Additional Questions and no Standard Questions
    Then User enters "programWithAdditionalQuestions" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User clicks on "Save & Create Application" button
    Then User should be redirected to the page with title of "CreateApplication_page_title"
    And User should see "Create Application"
    Then User enters "Additional_applicationTitle" into the "Application Title" field of Create application
    And User selects the "Create New Application" option on Create Application page
    And User clicks on "Save & Continue" button
    And User should see "Basic Information" in the Application Builder page
    And User should hide all the following Standard Questions from the Application Builder page
      | Question                      |
      | Job Title                     |
      | Startup Location              |
      | Industries                    |
      | Website                       |
      | LinkedIn                      |
      | Company Description           |
      | Year of Founding              |
      | Funding                       |
      | Number of full-time employees |
      | Milestones                    |
      | Pitch Deck                    |
    And User scrolls to "Additional Questions" section
    And User clicks on "Add New Question" in Additional Questions section
    Then User should see the New Question form
    And User fills the Additional Questions form with the following data
      | Type             | Question                                     | Instruction                               | Required |
      | Single-Line Text | What is your company's tagline?              | Short, single line (≤100 chars).          | Yes      |
      | Multi-Line Text  | Describe your product in one paragraph.      | 3–5 sentences; problem, solution, users.  | No       |
      | Number           | How many paying customers do you have today? | Whole number only (no commas).            | Yes      |
      | Date             | When did you found the company?              | Pick a valid founding date.               | No       |
      | Email            | What is your main contact email address?     | Valid format (name@domain).               | Yes      |
      | Single Choice    | What is your primary business model?         | Choose one option.                        | Yes      |
      | Multiple Choice  | Which platforms do you support?              | Select all that apply.                    | Yes      |
      | File             | Upload your current pitch deck.              | PDF only, max 25 MB.                      | Yes      |
      | Phone Number     | What is your main contact phone number?      | Include country code if outside US.       | Yes      |
      | Website/URL      | What is your company website?                | Full URL, e.g., https://example.com/      | Yes      |
      | Address          | What is your company headquarters address?   | Street, City, State, Postal Code, Country | Yes      |
    And User clicks on "Preview Application" button on the Application Builder page
    Then User should be on the Preview Application page
    And User should be on the Standard Questions tab
    And Checks on Save my responses to use in future applications on Passport
    Then Clicks on "Save & Continue" button on the Preview Application page
    Then User should be on the Additional Questions tab
    And Applicant should see all added Additional Questions on the Preview page
    And Applicant should see "Back" and "Submit" buttons on the Preview Additional Questions section
    And Clicks on "Back to Application" button on the Preview Application page
    And User clicks on "Save & Continue" button on the Application Builder page
    Then User verifies Rubric section components
    And User clicks on "Save & Continue" button on the Application Builder page
    Then User sets the Publish Open Date to now
    Then User copies the Application Link on the Publish page
    Then User adds a random invite email and verifies it appears in the list
    Then User deletes the last added invite email and verifies it is removed
    Then User clicks on "Complete" button on the Publish page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"

  @Tc_13 @Verify_full_lifecycle_of_Additional_Question @passport-pos
  Scenario: Verify full lifecycle of Additional Question (create, copy, edit, and delete)
    Then User enters "programWithStandardQuestions" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User clicks on "Save & Create Application" button
    Then User should be redirected to the page with title of "CreateApplication_page_title"
    And User should see "Create Application"
    Then User enters "Standard_applicationTitle" into the "Application Title" field of Create application
    And User selects the "Create New Application" option on Create Application page
    And User clicks on "Save & Continue" button
    And User should see "Basic Information" in the Application Builder page
    And User scrolls to "Additional Questions" section
    And User clicks on "Add New Question" in Additional Questions section
    Then User should see the New Question form
    Then User enters "An additional Question" into the "Question" field of Additional New Question form
    When User clicks on the "Copy Question" icon for the current Additional Question
    Then User should see the Additional Question "An additional Question" duplicated in the UI
    When User clicks on the first Edit Question icon
    When User clicks on the "Remove Question" icon for the current Additional Question
    Then User should see the Delete Question popup
    And User clicks on "No, Go Back" in the Delete Question popup
    And User should remain in the current New Question form
    When User clicks on the "Remove Question" icon for the current Additional Question
    Then User should see the Delete Question popup
    And User clicks on "Yes, Delete" in the Delete Question popup
    Then User should not see the New Question form displayed
    And User clicks on "Cancel" button on the Application Builder page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"
    

  @Tc_14 @Verify_Application_Creation_with_Rubric_configuration   @passport-pos
  Scenario: Verify Application Creation with Rubric configuration
    Then User enters "programWithStandardQuestions" into the "Program Title" field of Program Details
    Then User enters "location" into the "Location" field of Program Details
    Then User enters "programType" into the "Program Type" field of Program Details
    Then User enters "programDescription" into the "Program Description" field of Program Details
    Then User clicks on "Save & Continue" button
    And User should see "Member Perks"
    Then User clicks on "Save & Create Application" button
    Then User should be redirected to the page with title of "CreateApplication_page_title"
    And User should see "Create Application"
    Then User enters "Standard_applicationTitle" into the "Application Title" field of Create application
    And User selects the "Create New Application" option on Create Application page
    And User clicks on "Save & Continue" button
    And User should see "Basic Information" in the Application Builder page
    And User clicks on "Save & Continue" button on the Application Builder page
    Then User verifies Rubric section components
    When User clicks on the first Rubric Edit Question icon
    Then User clicks on the toggle switch to Allow comments for this criterion
    Then User clicks on Required Question checkbox and Save button
    And User clicks on "Cancel" button on the Application Builder page
    And User logs out
    Then User should be redirected to the page with title of "StartUp_page_title"