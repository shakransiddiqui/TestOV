package ov.pages.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import junit.framework.Assert;
import ov.utilities.CommonMethods;
import ov.utilities.ConfigurationReader;
import ov.utilities.Driver;
import ov.utilities.LogColor;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.NoSuchElementException;





public class HomePage_Passport extends CommonMethods  {
	
	private WebDriver driver;
	private WebDriverWait wait;
	
	public static final Logger logger = LogManager.getLogger(HomePage_Passport.class);

	public HomePage_Passport() {
		this.driver=Driver.getDriver();
		PageFactory.initElements(driver, this);
		wait=new WebDriverWait(driver,Duration.ofSeconds(20));

	}

	//******************************Locators************************************

	// login page elements --------
	//TC_01 ----
	@FindBy(xpath = "//span[@class='navbar-btn-text']")
	public WebElement login;

	@FindBy(xpath = "//*[@id='username']")
	public WebElement username_field;

	@FindBy(xpath = "//*[@id='password']")
	public WebElement password_Field;

	@FindBy(xpath = "//*[@id='SignUp']//div[1]/h3")
	public WebElement dashboard;

	//For Login Button
	@FindBy(xpath = "//button[@name='action']")
	public WebElement loginbtn;

	// For LogOut Button
	@FindBy(xpath = "//span[text()='Log Out']")
	public WebElement logoutbtn;
	
	//For Error Message
	@FindBy(xpath = "//*[@id='error-element-password']")
	public WebElement errorMessage;
	
	
	//For login page logo //*[@id='prompt-logo-center']
	
	@FindBy(xpath = "//*[@id='prompt-logo-center']")
	public WebElement loginPageLogo;



	//****************************************************************************************************

	// String Xpath template 
	String hm_loginField = "//*[@id='%s']";




	//Tc_02----- 
	@FindBy(xpath="//*[@id='email']")
	public WebElement EmailEnter_field ;

	@FindBy(xpath = "//*[@id='error-element-password']")
	public WebElement errormessage;

	//SignUp Scenario Tc_03   ----
	// SignUp Button to go to SignUp Page
	@FindBy(xpath = "//a[@title='Sign Up']")
	public WebElement signUpButton;

	// Username Field
	@FindBy(xpath="//*[@id='username']")
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

	// SignUp Submission Button
	@FindBy(xpath = "//button[contains(text(),'Sign Up')]")
	public WebElement submitButton;

	// For Profile Icon on Top Right corner
	@FindBy(xpath = "(//span[@class='navbar-action-icon'])[3]")
	public WebElement profileIcon;

	//Email-Fields-//*[@id="email-label"]
	@FindBy(xpath = "//*[@id='email']")
	public WebElement email_Field;
	//error meaage for with out checkbox //p[text()='You must accept the Terms of Service to register']
	@FindBy(xpath = "//p[text()='You must accept the Terms of Service to register']")
	public WebElement termsAndConditionsCheckboxErrorMessage;
	
	//wrong email error message //*[@id='error-cs-email-invalid']
	@FindBy(xpath = "//*[@id='error-cs-email-invalid']")
	public WebElement invalidEmailFieldErrorMessage;
	
	//Allready ergistered email error message
	@FindBy(xpath = "//*[@id='error-element-email']")
	public WebElement allreadyRegisteredEmailFieldErrorMessage;
	
	//blank email error message
	@FindBy(xpath = "//*[@id='error-cs-email-required']")
	public WebElement blankEmailFieldErrorMessage;
	
	
	//missing full name field error message
	@FindBy(xpath = "//p[text()='Missing Name']")
	public WebElement missingFullnameFieldErrorMessage;
	
	//missing Password field error message
	@FindBy(xpath = "//*[@id='error-cs-password-required']")
	public WebElement missingPasswordFieldErrorMessage;
	
	//invalid Password field error message
	@FindBy(xpath = "//*[@id='error-element-password']")
	public WebElement invalidPasswordFieldErrorMessage;
	
	
	//By Parts: 
	//===== TC_01 → TC_05 Linking Locators =====	
	By signupButton_by= By.xpath("//a[@title='Sign Up']");
	By profileIcon_by=By.xpath("(//span[@class='navbar-action-icon'])[3]");



	//***********************************Actions ***********************************************
    public void enterEmailonSignUpPage(String email) {
    	 wait.until(ExpectedConditions.visibilityOf(email_Field));
    	 email_Field.sendKeys(email);
    }
    
    
    public String getErrorMessageOfTermsAndCondtionsCheckbox() {
   	 wait.until(ExpectedConditions.visibilityOf(termsAndConditionsCheckboxErrorMessage));
    	 return termsAndConditionsCheckboxErrorMessage.getText();
    }
    
    public String getErrorMessageOfInvalidEmailField() {
      	 wait.until(ExpectedConditions.visibilityOf(invalidEmailFieldErrorMessage));
       	 return invalidEmailFieldErrorMessage.getText();
       }
    
    public String getErrorMessageOfBlankEmailField() {
     	 wait.until(ExpectedConditions.visibilityOf(blankEmailFieldErrorMessage));
      	 return blankEmailFieldErrorMessage.getText().trim();
      }
    
    public String getErrorMessageOfBlankPasswordField() {
    	 wait.until(ExpectedConditions.visibilityOf(missingPasswordFieldErrorMessage));
     	 return missingPasswordFieldErrorMessage.getText().trim();
     }
    
    public String getErrorMessageOfInvalidPasswordField() {
   	 wait.until(ExpectedConditions.visibilityOf(invalidPasswordFieldErrorMessage));
    	 return invalidPasswordFieldErrorMessage.getText().trim();
    	 }
    public String getLoginErrorMessage() {

        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return errorMessage.getText().trim();

        } catch (Exception e) {

            try {
                WebElement genericError = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Wrong email or password') or contains(text(),'Wrong') or contains(text(),'Invalid') or contains(text(),'required')]")
                    )
                );
                return genericError.getText().trim();

            } catch (Exception ex) {
                return "";
            }
        }
    }

    
  
   
    public String getErrorMessageOfBlankFullNameField() {
    	 wait.until(ExpectedConditions.visibilityOf(missingFullnameFieldErrorMessage));
     	 return missingFullnameFieldErrorMessage.getText().trim();
     }
    
    public String getErrorMessageOfAllReadyRegisteredEmail() {
     	 wait.until(ExpectedConditions.visibilityOf(allreadyRegisteredEmailFieldErrorMessage));
      	 return allreadyRegisteredEmailFieldErrorMessage.getText().trim();
      }
    
    public boolean isSignPagePageLogoDisplayed() {
		waitFor(10);
		return loginPageLogo.isDisplayed();
	}
	
	public String passFieldValue(String UiFieldElement_Value_fromSD,String UiFieldElement_Name_fromSD) {

		try {

			String formatted_hm_UiFieldcontained_Elements = String.format(hm_loginField, UiFieldElement_Name_fromSD);
			logger.info(formatted_hm_UiFieldcontained_Elements);


			logger.info("Finding the Field: "+UiFieldElement_Name_fromSD);
			WebElement login_field = driver.findElement(By.xpath(formatted_hm_UiFieldcontained_Elements));

			logger.info("Clicking on the Field: "+UiFieldElement_Name_fromSD);
			login_field.click();

			logger.info("Passing value on the Field: "+UiFieldElement_Value_fromSD);
			safeSendKeys(login_field, UiFieldElement_Value_fromSD);

			logger.info("Making sure value is filled up on the Field: "+UiFieldElement_Name_fromSD);
			String actualFieldValue = login_field.getAttribute("value");


			logger.info(LogColor.DarkGreen +"Actual value in the filed :"+actualFieldValue+ LogColor.RESET);
			return actualFieldValue;

		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);

			return "null";
		}
	}
	
	public boolean isLoginPageLogoDisplayed() {
		waitFor(10);
		return loginPageLogo.isDisplayed();
	}
	
	
	public String getErrorMessage() {
		waitFor(10);
		String error=errorMessage.getText();
		return error;
	}
	
	public void clickOnLoginBtn() {
		waitFor(10);
		loginbtn.click();
	}

	//****************************************************************************************************


	public void enter_username_and_password(String username,String Password) {

		//01-------
		username_field.sendKeys(username);
		password_Field.sendKeys(Password);

	}
	//****************************************************************************************************

	public void click_on_login_button() {
		logger.info("clicking on login button from homepage");
		waitFor(10);
		try {
			login.click();
			//CommonMethods.softAssert.assertTrue(driver.getTitle().contains("Sign in"), downloadFolderPath);
		} catch (Exception e) {
			logger.info("problem in try block");

			e.printStackTrace();
			//logger.info(e);
		}
	}

	//****************************************************************************************************


	public void click_on_signup_button() {
		
		try {
			logger.info("clicking on signup button from homepage");
			waitFor(10);
			signUpButton.click();
			//CommonMethods.softAssert.assertTrue(driver.getTitle().contains("Sign in"), downloadFolderPath);
		} catch (Exception e) {
			logger.info("problem in try block");

			e.printStackTrace();
			//logger.info(e);
		}


	}

	//****************************************************************************************************


	// Method to LogOut and then SignUp back in:
	public void verifySignupFunctionality() {
		try {

			if (isElementPresent(signupButton_by)) {
				logger.info("User is logged out — clicking Sign Up.");
				click(signUpButton);
			}

			else if (isElementPresent(profileIcon_by)) {
				logger.info("Profile icon visible — refreshing to logout and then click on Sign Up");
				Driver.getDriver().navigate().refresh();

				waitForClickablility(profileIcon);

				click(profileIcon);

				waitForClickablility(logoutbtn);
				click(logoutbtn);
				logger.info("Clicked ProfileIcon, the Logout after refresh.");

				click(signUpButton);

			}

			else {
				logger.error("Neither Sign Up nor ProfileIcon visible — cannot continue.");
				Assert.fail("No valid element for Sign Up found.");
			}
		} catch (Exception e) {
			logger.error("verifySignupFunctionality() failed: ", e);
			Assert.fail("verifySignupFunctionality() failed: " + e.getMessage());
		}
	}

	//****************************************************************************************************

	private void click(WebElement signUpButton2) {
		// TODO Auto-generated method stub
		
	}


	public void verify_community_dashboard(){ 

		try {
			// return dashboard.getText();
			softAssert.assertEquals(dashboard.getText(), "Welcome to Passport", "- User is on UserHomePage");


		} catch (Exception e) {
			// TODO: handle exception
		}


	}
	
	public void clickOnSignUpSubmitButton() {
		waitFor(10);
		submitButton.click();
	}

	//****************************************************************************************************


	
	public void verify_clickOnSubmitButton() {
		click(submitButton);
	}

	public void Verif_yUser_Is_On_Homepage() {
		try {
			String actualTitle=driver.getTitle();
			String expextedTitle="OV Tsunami Project";
			softAssert.assertEquals(actualTitle, actualTitle,"Verify User Is On Homepge");

		}
		catch (Exception e) {
			logger.info(e);
		}


	}

	//****************************************************************************************************

	public void user_login() {

		//clickAndDraw(loginbtn);


		jsclick(driver, loginbtn);

		waitFor(3);


	}

	//****************************************************************************************************

	// SignUp Scenario Tc_03 ------------

	public void navigate_to_sigup_page() {

		PageFactory.initElements(Driver.getDriver(), this);
	}
	
	public void Enter_a_valid_email_address_on_Signup(String email) {
		EmailEnter_field.clear();
		EmailEnter_field.sendKeys(email);
	}


	public void Enter_a_valid_email_address (String email) {
		Username_field.clear();
		Username_field.sendKeys(email);
	}

	public void enter_valid_password_meeting_all_requirement (String Password) {
		Password_field.clear();
		Password_field.sendKeys(Password);

	}

	//****************************************************************************************************


	public void enterFullName(String fullName) {
		fullNameField.clear();
		fullNameField.sendKeys(fullName);
	}

	//****************************************************************************************************

	public void enterJobTitle(String jobTitle) {
		jobTitleField.clear();
		jobTitleField.sendKeys(jobTitle);
	}
	//****************************************************************************************************
	public void agreeToTerms() {
		termsCheckboxLabel.click();
	}

	//****************************************************************************************************
	public void clickSignUpButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.elementToBeClickable(signUpButton));
			signUpButton.click();
		} catch (Exception e) {

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", signUpButton);
		}
	}

	//  @Tc_04 @validate_Post_Sign_Up_Role_Selection ---------------

	@FindBy(xpath = "//h3[text()='Welcome to Passport']")
	public WebElement welcomeToPassportHeader;

	public boolean isWelcomeHeaderDisplayed() {
		try {
			return welcomeToPassportHeader.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

}