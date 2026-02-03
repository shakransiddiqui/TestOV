@dashboard
Feature: Testing User Dashboard features after Successful Login

Background: 
    Given Validate User landed on homepage
    Then Pass "mngr651142" on "UserID" Field
    Then Pass "yzebYbA" on "Password" Field
    And Click on Login button with Valid Credentials
    
    @TC_0014 @test @afterLogin
    Scenario: Verify Manager and logout button is visible in the left side
    Given click on "Manager" button and open "Welcome_To_Manager's_Page_of_Guru99_Bank" page
    Then click on "Log out" button
    
