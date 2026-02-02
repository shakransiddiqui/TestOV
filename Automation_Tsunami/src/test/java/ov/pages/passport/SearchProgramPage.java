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




public class SearchProgramPage extends CommonMethods{
	WebDriver driver = Driver.getDriver();
	WebDriverWait wait;

	public static final Logger logger = LogManager.getLogger(SearchProgramPage.class);
	public SearchProgramPage   () { 
		PageFactory.initElements(Driver.getDriver(), this);
		wait=new WebDriverWait(Driver.getDriver(),Duration.ofSeconds(10 )); {

}	
						
	}
	//Locators :
	  @FindBy(xpath = "(//a[contains(@class,'see-more-button')])[1]")
	    private WebElement seeMoreButton;
	  
	  
	  
	  
	  
	  
	  
	  //Actions :
	  public void clickOnSeeMoreButton() {
		    wait.until(ExpectedConditions.elementToBeClickable(seeMoreButton));
		    seeMoreButton.click();
		}

}