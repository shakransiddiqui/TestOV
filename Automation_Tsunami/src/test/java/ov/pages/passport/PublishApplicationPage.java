package ov.pages.passport;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ov.utilities.CommonMethods;
import ov.utilities.Driver;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;




public class PublishApplicationPage extends CommonMethods{
	/***
	 * instance variable 
	 */
	public WebDriver driver;
	public WebDriverWait wait;
	
	/***
	 * Logger Iniatlization
	 */
    
	public static final Logger logger = LogManager.getLogger(PublishApplicationPage.class);
	/**
	 * constructor of PublishApplicationPage class
	 */
	public PublishApplicationPage() { 
		this.driver=Driver.getDriver();
		PageFactory.initElements(driver, this);
		wait=new WebDriverWait(driver,Duration.ofSeconds(20));
		
	}
	
	/**
	 * Locators variables of page
	 */
   
	@FindBy(css="[id='CreateApplicationPublish']>div>header>h5")
	private WebElement publishPageHeaderText;
	
	@FindBy(css= "[id='date-time-start']")
	private WebElement openDateInput;
	

	
	@FindBy(xpath = "//button[normalize-space()='Complete']")
	private WebElement completeButton;
	
	
	/**
	 * actions methods on PublishApplicationPage 
	 */
	
	
	
	public boolean IsPublishApplicationPageHeaderIsDisplayed()
	{
		logger.info("checking that PublishApplicationPage header is displayed");
		wait.until(ExpectedConditions.visibilityOf(publishPageHeaderText));
		return publishPageHeaderText.isDisplayed();
	}
	
	
	
	public void selectOpenDateInCalendar()
	{
		logger.info("checking that PublishApplicationPage header is displayed");
		wait.until(ExpectedConditions.visibilityOf(openDateInput));
		String dateTimeInputValue=getCurrentAndNextMinute();
		openDateInput.sendKeys(dateTimeInputValue);
		
	}
	

	
	public void clickCompleteButton() {
	    logger.info("Clicking on Complete button");
	    wait.until(ExpectedConditions.elementToBeClickable(completeButton));
	    completeButton.click();
	}
	
	

	
   /**
    * Helper methods for above given page actions.	
    * @return  {@value={currentDateTime+1mint}}
    */
	private String getCurrentAndNextMinute() {
        // Formatter for the required format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // Current Date Time
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.format(formatter);

        // Current Date Time + 1 minute
        LocalDateTime plusOneMinute = now.plusMinutes(1);
        String nextMinuteTime = plusOneMinute.format(formatter);

        return nextMinuteTime;
    }

}