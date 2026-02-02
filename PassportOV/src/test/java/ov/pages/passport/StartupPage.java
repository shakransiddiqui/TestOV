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
import org.openqa.selenium.NoSuchElementException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StartupPage extends CommonMethods{
	private WebDriver driver;
	private static final Logger logger = LogManager.getLogger(StartupPage.class);

	public StartupPage() {
		this.driver=Driver.getDriver();
		PageFactory.initElements(driver, this);
	}

	// Tc_04  -----------------

	@FindBy(xpath = "//label[@for='name-input' and contains(text(),'Startup Name')]")
	public WebElement startupNameLabel;
	public boolean isStartupPageDisplayed() {
		return startupNameLabel.isDisplayed();
	}

	//Tc_05  -----------------
	@FindBy(xpath = "//h3[text()='Welcome to Passport']")
	public WebElement startupInformationPage;
	public  boolean isOnStartupInformationPage() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(startupInformationPage));
		return startupInformationPage.isDisplayed();
	}

	@FindBy(xpath = "//input[@name='name']")
	public WebElement startupNameInput;
	public void enterStartupName(String name) {
		startupNameInput.sendKeys(name);
	}

	@FindBy(id = "location-field")
	public WebElement locationInput;
	public void enterLocation(String location) {
		locationInput.sendKeys(location);
		WebDriverWait wait = new  WebDriverWait(driver, Duration.ofSeconds(20));


		List<WebElement> suggestions = wait.until(
				ExpectedConditions.visibilityOfAllElementsLocatedBy(
						By.xpath("//div[@class='pac-item']")
						)
				);


		suggestions.get(0).click();

	}



	@FindBy(css = "button.TBaseButton.primary.continue-btn")
	public WebElement finishButton;
	public void clickFinishButton() {
		finishButton.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.urlContains("/searchprogram"));


	}
	@FindBy(css = "a.nav-logo-btn")
	private WebElement navLogoBtn;
	public boolean isSearchPageDisplayed() {
		return navLogoBtn.isDisplayed();
	}

}