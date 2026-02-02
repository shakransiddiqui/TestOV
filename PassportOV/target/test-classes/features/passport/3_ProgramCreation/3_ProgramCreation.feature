@Program_Creation
Feature: Program_Creation_Feature

  Background: 
    Given Navigate to the sign-up page
    When Enter a valid email address
    And Enter valid password meeting all requirements.
    And Enter full name
    And Enter job title
    Then Check the terms and conditions checkbox .
    And Click the Sign Up button
    Then Verify that the user is redirected to the role selection page
    Given Verify that "Welcome to Passport" page is displayed
    When the user selects the "Program Manager" role
    And Click the continue button
    Then the Program Manager details should be displayed

  @Tc_15 @Select_Role @passport-pos
  Scenario: 15_Access Welcome Page and Select Program Manager Role
    Given user is on welcome page

  @Tc_16 @Enter_Organization_Info @passport-pos
  Scenario: 16_Enter Program Manager Organization Info
    Given the Program Manager details page is displayed
    When the user enters the organization name
    And the user enters a valid location
    And the user clicks the finish button
    Then the user should be redirected to the "Create a new program" page

  @Tc_17 @Fill_Program_Details @passport-pos
  Scenario: 17_Fill Out New Program Form with 2 industries
    When the user enters the organization name
    And the user enters a valid location
    And the user clicks the finish button
    Then the program creation form should be displayed
    When the user enters a valid program title
    And the user enters valid location
    And the user selects a program type
    And the user enters program description
    And the user selects two valid industries
    And the user clicks the "Save & Continue" button
    Then the user should be redirected to the member perks page

  @Tc_18 @Fill_Program_Details @passport-pos
  Scenario: 18_Fill Out New Program Form with 3 industries
    When the user enters the organization name
    And the user enters a valid location
    And the user clicks the finish button
    Given the user is on the program creation page
    When the user enters a valid program title
    And the user enters valid location
    And the user selects a program type
    And the user enters program description
    And the user selects three valid industries
    And the user clicks the "Save & Continue" button
    Then the user should be redirected to the member perks page

  @Tc_19 @Fill_Program_Details @passport-pos
  Scenario: 19_Fill Out New Program Form with 5 industries
    When the user enters the organization name
    And the user enters a valid location
    And the user clicks the finish button
    Given the user is on the program creation page
    When the user enters a valid program title
    And the user enters valid location
    And the user selects a program type
    And the user enters program description
    And the user selects five valid industries
    And the user clicks the "Save & Continue" button
    Then the user should be redirected to the member perks page

  @Tc_20 @Fill_Program_Details @passport-pos
  Scenario: 20_Fill Out New Program Form with no industries
    When the user enters the organization name
    And the user enters a valid location
    And the user clicks the finish button
    Given the user is on the program creation page
    When the user enters a valid program title
    And the user enters valid location
    And the user selects a program type
    And the user enters program description
    And the user clicks the "Save & Continue" button
    Then the user should be redirected to the member perks page

  @Tc_21 @Select_Perk_Cards @passport-pos
  Scenario: 21_Complete Program Setup with 5 Perk Selection
    When the user enters the organization name
    And the user enters a valid location
    And the user clicks the finish button
    Then the program creation form should be displayed
    When the user enters a valid program title
    And the user enters valid location
    And the user selects a program type
    And the user enters program description
    And the user selects two valid industries
    And the user clicks the "Save & Continue" button
    Then the user should be redirected to the member perks page
    Given the user is on the member perks page
    When the user selects five perk cards
    And the user clicks the "Save & Create Application" button
    Then the user should be redirected to the Create Application screen

  @Tc_22 @Select_Perk_Cards @passport-pos
  Scenario: 22_Complete Program Setup with 1 Perk Selection
    When the user enters the organization name
    And the user enters a valid location
    And the user clicks the finish button
    Then the program creation form should be displayed
    When the user enters a valid program title
    And the user enters valid location
    And the user selects a program type
    And the user enters program description
    And the user selects two valid industries
    And the user clicks the "Save & Continue" button
    Given the user is on the member perks page
    When the user selects one perk cards
    And the user clicks the "Save & Create Application" button
    Then the user should be redirected to the Create Application screen

  @Tc_23 @Select_Perk_Cards @passport-pos
  Scenario: 23_Complete Program Setup with 3 Perk Selection
    When the user enters the organization name
    And the user enters a valid location
    And the user clicks the finish button
    Then the program creation form should be displayed
    When the user enters a valid program title
    And the user enters valid location
    And the user selects a program type
    And the user enters program description
    And the user selects two valid industries
    And the user clicks the "Save & Continue" button
    Given the user is on the member perks page
    When the user selects three perk cards
    And the user clicks the "Save & Create Application" button
    Then the user should be redirected to the Create Application screen
