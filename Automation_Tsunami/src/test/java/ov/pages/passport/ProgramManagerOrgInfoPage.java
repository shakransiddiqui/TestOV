package ov.pages.passport;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

public class ProgramManagerOrgInfoPage {
	private WebDriver driver;   
	private static final Logger logger = LogManager.getLogger(ProgramManagerOrgInfoPage.class);
	private WebDriverWait wait;

	public  ProgramManagerOrgInfoPage () {
		driver=Driver.getDriver();	
		PageFactory.initElements(driver, this);
		wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		
	}
	//Tc_07 -------------------
	
	
	@FindBy(css="[class='p-card-name']")
	private List<WebElement> programCards;
	
	@FindBy(xpath="//a[contains(@class,'create-button')]")
	private List<WebElement> craeteApplicationButtons;
	
	@FindBy(css="[class='nav-container next']>button")
	private List<WebElement> programsPaginationNextButton;
	
	
	
	public boolean isUserHaveAtleastOneProgram()
	{
		wait.until(ExpectedConditions.visibilityOfAllElements(programCards));
		return programCards.size()>0 ? true : false;
	}
	
	
	public void clickOnCreateApplicationButtonOnFirstProgram() {

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    // Step 1: Check first page
	    if (!craeteApplicationButtons.isEmpty()) {
	        craeteApplicationButtons.get(craeteApplicationButtons.size() - 1).click();
	        return;
	    }

	    // Step 2: Loop all pages safely
	    while (!programsPaginationNextButton.isEmpty()) {

	        WebElement nextBtn = programsPaginationNextButton.get(0);

	        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));

	        js.executeScript("arguments[0].scrollIntoView(true);", nextBtn);
	        nextBtn.click();
	        
	        wait.until(driver -> js.executeScript("return document.readyState").equals("complete"));
	        try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // stabilizes DOM

	        // --- SAFE REFRESH (NO EXCEPTIONS) ---
	        List<WebElement> refreshedButtons =
	                driver.findElements(By.xpath("//a[contains(@class,'create-button')]"));

	        if (!refreshedButtons.isEmpty()) {
	            refreshedButtons.get(refreshedButtons.size() - 1).click();
	            return;
	        }
	    }

	    // Step 3: If loop finished and still nothing found
	    throw new RuntimeException("Create Application button not found on any page.");
	}
	
	
	
	public void enterOrganizationName(String orgName) {
	    WebElement orgNameField = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='name']")));

	    orgNameField.clear();
	    orgNameField.sendKeys(orgName);
	    try {
	        Thread.sleep(3000); 
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	        
	    }
	}


	public String getOrganizationName() {
	    WebElement orgNameField = driver.findElement(By.name("name"));
	    return orgNameField.getAttribute("value");
	}


	public void enterLocation(String location) {
	    WebElement locationField = driver.findElement(By.id("location-field"));
	    locationField.clear();
	    locationField.sendKeys(location);
	    WebDriverWait wait = new  WebDriverWait(driver, Duration.ofSeconds(20));


		List<WebElement> suggestions = wait.until(
				ExpectedConditions.visibilityOfAllElementsLocatedBy(
						By.xpath("//div[@class='pac-item']")   
						)
				);


		suggestions.get(0).click();


	    try {
	        Thread.sleep(1500);  
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	public void clickFinishButton() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    WebElement finishBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='Finish']")));
	    finishBtn.click();
	    try {
	        Thread.sleep(15000);  
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	    
	}
	@FindBy(css = "button.create-program-button")
	public WebElement createProgramButton;

	public String getPageHeading() {
	    return createProgramButton.getText();
	


	}
	public void goToProgramCreationForm() {
	    driver.findElement(By.xpath("//button[@aria-label='Create a new program']")).click();
	}

	public boolean isProgramCreationFormDisplayed() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    return wait.until(ExpectedConditions.visibilityOfElementLocated(
	        By.xpath("//*[@class='form-title-text']")
	    )).isDisplayed();
	}
	
	}