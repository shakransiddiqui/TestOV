package ov.pages.passport;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;  
import io.netty.handler.timeout.TimeoutException;
import ov.utilities.Driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

public class RoleSelectionPage {
    private WebDriver driver;   
    private static final Logger logger = LogManager.getLogger(RoleSelectionPage.class);
    
    
    
    
    @FindBy(xpath = "//h3[contains(text(), 'Welcome to Passport')]")
    public WebElement welcomeToPassportHeader;
    
    @FindBy(xpath = "//input[@type='radio' and @value='startup']")
    public WebElement StartUpLeader;
    
    @FindBy(xpath = "//button[contains(@class, 'continue-btn')]")
    public WebElement continueButton;
    
    public RoleSelectionPage() {
    	this.driver=Driver.getDriver();
    	PageFactory.initElements(driver, this);
    }

    public boolean userIsWelcomeToPassportHeaderDisplayed() {
    	return welcomeToPassportHeader.isDisplayed();
    }
    
    
    public void clickStartupLeader() {
    StartUpLeader.click();
     
    }
    
    public void clickContinueButton() {
    continueButton.click();
    
    }
   
    
    
    public boolean isWelcomeHeaderTextCorrect(String expectedText) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[contains(text(), 'Welcome to Passport')]")));
            wait.until(ExpectedConditions.visibilityOf(welcomeToPassportHeader));

            String actualText = welcomeToPassportHeader.getText();
            logger.info("Expected Header Text: '{}'", expectedText);
            logger.info("Actual Header Text: '{}'", actualText);
            return actualText.equals(expectedText);
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Element not found or not visible: {}", e.getMessage());
            return false;
        }
    }

public String getWelcomeHeaderText() {
    return welcomeToPassportHeader.getText();
}

//Tc_06 --------------------------------

@FindBy(xpath = "//div[contains(@class, 'signup-select-option') and .//text()[contains(., 'Program Manager')]]//input[@type='radio']")
public static WebElement ProgramManager;
public void clickProgramManager() {
    ProgramManager.click();
}

@FindBy(css = "button.continue-btn")
public static WebElement continueBtn;
public void clickContinueBtn() {
    continueBtn.click();
}

@FindBy(xpath = "//h3[text()='Welcome to Passport']")
public static WebElement programManagerHeader;
public boolean isProgramManagerDetailsPageVisible() {
    return programManagerHeader.isDisplayed();
}

  
    
}