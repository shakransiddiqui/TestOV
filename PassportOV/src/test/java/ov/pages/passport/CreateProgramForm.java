package ov.pages.passport;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import io.netty.handler.timeout.TimeoutException;
import junit.framework.Assert;
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

public class CreateProgramForm extends CommonMethods{
	WebDriver driver ;
	WebDriverWait wait;

	public static final Logger logger = LogManager.getLogger(CreateProgramForm.class);
	public CreateProgramForm() {
        driver=Driver.getDriver();
		PageFactory.initElements(Driver.getDriver(), this);
		wait=new WebDriverWait(Driver.getDriver(),Duration.ofSeconds(20));

	}


	 @FindBy(xpath = "//span[@class='form-title-text']")
	    WebElement createProgramHeader;

	 @FindBy(css = "button.create-program-button[aria-label='Create a new program']")
	    private WebElement createNewProgramButton;

	 @FindBy(xpath = "//button[@data-cy='btnCreateNewProgram']")
	    WebElement createPrograms;

	 @FindBy(css = "[name='Program Name']")
	 private WebElement programsTitleField;

	 @FindBy(css = "[id='program-location']")
	 private WebElement programsLocationField;

	 @FindBy(name = "ProgramTag")
	 private WebElement programTypeDropdown;

	 @FindBy(css = "[class='ql-editor ql-blank']")
	 private WebElement programDescription;

	 @FindBy(css = "[name='searchindustries']")
	 private WebElement industrySearchInput;

	// Username Field
			@FindBy(xpath="//*[@id='email']")
			public WebElement Username_field ;

			//Password_field
			@FindBy(xpath="//*[@id='password']")
			public WebElement Password_field ;

			// Full Name Field
			@FindBy(xpath="//*[@id='full-name']")
			public WebElement fullNameField;

			//Job Title Field
			@FindBy(xpath="//*[@id='job-title']")
			public WebElement jobTitleField;

			//Check Box for Terms & Conditions
			@FindBy(xpath = "//label[@for='terms-of-service']")
			public WebElement termsCheckboxLabel;

	 // For LogOut Button
	 @FindBy(xpath = "//span[text()='Log Out']")
		public WebElement logoutbtn;

	 // SignUp Button to go to SignUp Page
	 @FindBy(xpath = "//a[@title='Sign Up']")
		public WebElement signUpBtn;

	 // SignUp Submission Button
	 @FindBy(xpath = "//button[contains(text(),'Sign Up')]")
		public WebElement submitButton;

	 // For Profile Icon on Top Right corner
		@FindBy(xpath = "//span[@class='navbar-action-icon']/img")
		public WebElement profileIcn;

	//By Parts:
			//===== Tc_05 → Tc_06 Linking Locators =====
	By signupBtn_by= By.xpath("//a[@title='Sign Up']");
	By profileIcn_by=By.xpath("//span[@class='navbar-action-icon']/img");


	public void Enter_a_valid_email_address (String email) {
		 Username_field.clear();
		  Username_field.sendKeys(email);
		}

	public void enter_valid_password_meeting_all_requirement (String Password) {
		 Password_field.clear();
		 Password_field.sendKeys(Password);

	}


	public void enterFullName(String fullName) {
	    fullNameField.clear();
	    fullNameField.sendKeys(fullName);
	}

	public void enterJobTitle(String jobTitle) {
	    jobTitleField.clear();
	    jobTitleField.sendKeys(jobTitle);
	}
	public void agreeToTerms() {
		  termsCheckboxLabel.click();
	}
	public void clickSignUpButton() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    try {
	    	  wait.until(ExpectedConditions.elementToBeClickable(signUpBtn));
	    	    signUpBtn.click();
	    } catch (Exception e) {

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("arguments[0].click();", signUpBtn);
	    }
	    }

	 public void goToProgramCreationForm() {
		 createPrograms.click();
	    }

	 public void clickCreateNewProgramButton() {
	        createNewProgramButton.click();
	    }

	 public boolean isProgramCreationFormDisplayed() {
	        try {
	            return createProgramHeader.isDisplayed();
	        } catch (Exception e) {
	            return false;
	        }
		}

// public void enterProgramTitle(String title)
//	 {
//		 programsTitleField.sendKeys(title);
//	 }
	 public void enterProgramTitle(String title) {
		    logger.info("Entering Program Title");

		    // wait until the field is actually on the page
		    wait.until(ExpectedConditions.visibilityOf(programsTitleField));

		    ((JavascriptExecutor) driver).executeScript(
		            "arguments[0].scrollIntoView({block:'center'});",
		            programsTitleField
		    );

		    programsTitleField.clear();
		    programsTitleField.sendKeys(title);
		}




	 public void enterProgramLocation(String location) {
		    logger.info("Entering Program Location: " + location);
		    programsLocationField.clear();
		    programsLocationField.sendKeys(location);

		    try {

		        WebElement firstSuggestion = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".pac-item"))
		        );
		        firstSuggestion.click();
		        logger.info("Selected first location suggestion successfully");

		    } catch (Exception e) {
		        logger.warn("Suggestion not found on first try, retrying with: " + location);


		        programsLocationField.clear();
		        programsLocationField.sendKeys(location);

		        WebElement firstSuggestion = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".pac-item"))
		        );
		        firstSuggestion.click();
		        logger.info("Selected first location suggestion on retry");
		    }
		}

	 public void selectProgramType()
	 {
		;

		 WebElement dropdown = driver.findElement(By.xpath("//div[contains(@class,'select-input')]"));
		 dropdown.click();

		 WebElement option = wait.until(ExpectedConditions
		         .elementToBeClickable(By.xpath("//*[contains(text(),'Accelerator')]")));


		 option.click();
	 }

	 public void enterProgramDescription(String description)
	 {
		 programDescription.sendKeys(description);
		 logger.info("Description written");
	 }

	 public void selectIndustries(String searchText, int noOfIndustriesToSelect) {

		    WebElement industryInput = wait.until(
		            ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Search Industries']"))
		    );

		    // Make sure window is scrollable to correct height
		    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");

		    // Scroll input into view but not to very top (avoid navbar overlap)
		    ((JavascriptExecutor) driver).executeScript(
		            "const rect = arguments[0].getBoundingClientRect();" +
		            "window.scrollBy(0, rect.top - 150);",
		            industryInput
		    );

		    // CLICK USING JS – avoids headless click interception
		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", industryInput);

		    // Clear existing text
		    industryInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		    industryInput.sendKeys(Keys.DELETE);

		    // Type search text
		    industryInput.sendKeys(searchText);
		    try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // allow dropdown to load
		    industryInput.sendKeys(Keys.ENTER);

		    // Wait for dropdown options
		    WebElement dropDown = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='dropdown-list']"))
		    );

		    List<WebElement> options = dropDown.findElements(By.xpath(".//li"));
		    int optionsSize = options.size();

		    for (int i = 0; i < noOfIndustriesToSelect && i < optionsSize; i++) {

		        WebElement option = options.get(i);
		        WebElement checkbox = option.findElement(By.xpath(".//input[@type='checkbox']"));

		        // Scroll option into safe clicking range
		        ((JavascriptExecutor) driver).executeScript(
		                "const rect = arguments[0].getBoundingClientRect();" +
		                "window.scrollBy(0, rect.top - 150);",
		                option
		        );

		        // CLICK USING JS for headless reliability
		        if (!checkbox.isSelected()) {
		            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
		        }
		    }

		    // Close dropdown
		    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", industryInput);

		    try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	 public void clickOnSaveAndContinueButton()
	 {

		 WebElement saveAndContinueButton = driver.findElement(By.cssSelector("[class='TBaseButton primary footer-action-btn footer-action-btn-save']"));
         try {
       	 JavascriptExecutor js = (JavascriptExecutor) driver;
    		 js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", saveAndContinueButton);
        	 wait.until(ExpectedConditions.elementToBeClickable(saveAndContinueButton));
        	 saveAndContinueButton.click();
         }catch(Exception e)
         {
        	((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveAndContinueButton);
        	e.printStackTrace();

         }

         System.out.println("clicked on click and save button");
	 }

	 public boolean isUserOnMemebersPerkPage()
	 {
		 WebElement memberPerksHeaders=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='member-rewards-header-text']")));
		 try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 return  memberPerksHeaders.getText().trim().equals("Member Perks");
	 }

	 public boolean isCreateProgramPageDisplayed() {
		    logger.info("Checking if Create Program page header is displayed");
		    wait.until(ExpectedConditions.visibilityOf(createProgramHeader));
		    return createProgramHeader.isDisplayed();
		}

//	 public void enterProgramTitle(String title) {
//
//		    logger.info("Entering Program Title");
//
//		    wait.until(ExpectedConditions.visibilityOf(programsTitleField));
//
//		    ((JavascriptExecutor) driver).executeScript(
//		        "arguments[0].scrollIntoView({block: 'center'});",
//		        programsTitleField
//		    );
//
//		    programsTitleField.clear();
//		    programsTitleField.sendKeys(title);
//		}


}