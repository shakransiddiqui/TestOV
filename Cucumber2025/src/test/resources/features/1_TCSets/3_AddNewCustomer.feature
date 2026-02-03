@addNewCustomer
Feature: Testing Add New Customer Page features after Successful Login

  Background: 
    Given Validate User landed on homepage
    Then Pass "mngr651142" on "UserID" Field
    Then Pass "yzebYbA" on "Password" Field
    And Click on Login button with Valid Credentials

  @TC_0016 @test
  Scenario: Verify new customer creation feature with Invalid data where PIN Is Invalid
    Given click on "New Customer" button and go to "Add_New_Customer" page
    When I fill the form with the following fields from "testcases_for_Automation.xlsx" and sheet "Invalid_Pin_Data":
      | Customer Name |
      | Gender        |
      | Date of Birth |
      | Address       |
      | City          |
      | State         |
      | PIN           |
      | Mobile Number |
      | E-mail        |
    Then click on "Submit" button.
