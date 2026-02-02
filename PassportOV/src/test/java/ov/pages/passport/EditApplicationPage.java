package ov.pages.passport;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import io.netty.handler.timeout.TimeoutException;
import ov.utilities.CommonMethods;
import ov.utilities.Driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class EditApplicationPage extends CommonMethods{
	public WebDriver driver;
	public WebDriverWait wait;

	public static final Logger logger = LogManager.getLogger(EditApplicationPage.class);
	public  EditApplicationPage  () {
		this.driver=Driver.getDriver();
		PageFactory.initElements(driver, this);
		wait=new WebDriverWait(driver,Duration.ofSeconds(10 ));

	}
	@FindBy(xpath = "//span[@class='form-title-text' and text()='Edit Application']")
	public WebElement editApplicationHeader;





	public boolean areHiddenQuestionsVisible() {
	    try {

	        WebElement hiddenField = driver.findElement(By.xpath("//*[text()='Company Description']"));
	        return hiddenField.isDisplayed(); // if found and visible -> fail condition
	    } catch (NoSuchElementException e) {

	        return false;
	    }
	}

	}