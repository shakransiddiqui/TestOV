package ov.pages.passport;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import ov.utilities.CommonMethods;
import ov.utilities.Driver;

public class Login_POM extends CommonMethods {
	
	public WebDriver driver;
	public WebDriverWait wait;
	
	public static final Logger logger = LogManager.getLogger(Login_POM.class);
	
	public Login_POM() {
		this.driver=Driver.getDriver();
		PageFactory.initElements(driver, this);
		wait=new WebDriverWait(driver,Duration.ofSeconds(20));

	}
	
	

}
