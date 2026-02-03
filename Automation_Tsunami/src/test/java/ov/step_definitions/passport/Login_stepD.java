package ov.step_definitions.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Given;
import junit.framework.Assert;
import ov.pages.passport.HomePage_Passport;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import io.cucumber.java.en.*;


public class Login_stepD extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(Login_stepD.class);




	//	***************************************************************************************************************
	@When("User enters {string} in the username field")
	public void user_enters_in_the_username_field(String string) {

	}

	
	//	***************************************************************************************************************
	@When("User enters {string} in the password field")
	public void user_enters_in_the_password_field(String string) {

	}

	//	***************************************************************************************************************
	@When("User clicks on the {string} button")
	public void user_clicks_on_the_button(String string) {

	}

	//	***************************************************************************************************************
	@Then("User should see a login error or validation message")
	public void user_should_see_a_login_error_or_validation_message() {

	}

	//	***************************************************************************************************************
	@Then("User should remain on the Login page")
	public void user_should_remain_on_the_login_page() {

	}

	//	***************************************************************************************************************
	@Then("User should be redirected to the Community Dashboard")
	public void user_should_be_redirected_to_the_community_dashboard() {

	}

}