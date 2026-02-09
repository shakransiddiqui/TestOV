package ov.pages.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import ov.utilities.CommonMethods;
import ov.utilities.LogColor;

public class Signup_POM extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(Signup_POM.class);

	public Signup_POM() {
		PageFactory.initElements(driver, this);
	}



	//	*****************X-path Locators****************************************************************



	//	******************By Locators****************************************************************************



	//	******************Locator Format Template Strings****************************************************************************

	//All fields to fill for SignUp
	private static final String signup_fields ="//label[contains(normalize-space(.), '%s')]/following::input[1]";
	
	//All buttons to click for SignUp and Program Creation
	private static final String buttons ="//button[text()='%s']";
	
	//CheckBox of terms-Of-services
	private static final String checkboxLabelXpath = "//label[@for='%s']";
	private static final String checkboxInputXpath = "//input[@id='%s']";
	
	//All role to choose for SignUp
	private static final String signupRoles = "//label[contains(text(), '%s')]";
	
	//All page Sub Headers for SignUp
	private static final String rolePage_subheader = "//p[contains(text(), '%s')]";
	
	// FIRST suggestion from Google dropdown (no text matching)
	private static final String locationSuggestion = "(//div[contains(@class,'pac-container')]//div[contains(@class,'pac-item')])[1]";
	
	//All error texts for SignUp
	private static final By SIGNUP_ERROR_BY = By.cssSelector(".ulp-input-error-message, .ulp-validator-error, #prompt-alert");
	
	
	
	//	****************Actions***********************************************************************************

	public String passFieldValue(String fieldElement_Value,String fieldElement_Name) {

		try {
			logger.info("Formatting the X path of Field: "+fieldElement_Name);
			String formattedXpath = String.format(signup_fields, fieldElement_Name);
			logger.info(formattedXpath);


			logger.info("Finding the Field: "+fieldElement_Name);
			WebElement signup_field = waitForElement(By.xpath(formattedXpath));

			logger.info("Clicking on the Field: "+fieldElement_Name);
			clickAndDraw(signup_field);

			logger.info("Passing value on the Field: "+fieldElement_Value);
			safeSendKeys(signup_field, fieldElement_Value);
			
			// SPECIAL handling for Location autocomplete
			if ("Location".equalsIgnoreCase(fieldElement_Name)) {

			    logger.info("Selecting first location suggestion from dropdown");
			    WebElement suggestion = waitForElement(By.xpath(locationSuggestion));
			    drawborder(suggestion);
			    suggestion.click();
			}

			logger.info("Making sure value is filled up on the Field: "+fieldElement_Name);
			String actualFieldValue = signup_field.getAttribute("value");

			logger.info(LogColor.DarkGreen +"Actual value in the filed :"+actualFieldValue+ LogColor.RESET);
			return actualFieldValue;

		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return "null";
		}
	}

//	***************************************************************************************************************
	public boolean clickTermsOfService(String checkboxId) {

		try {  
			logger.info("CheckboxId received: " + checkboxId);

			String labelXpath = String.format(checkboxLabelXpath, checkboxId);
			String inputXpath = String.format(checkboxInputXpath, checkboxId);

			By labelBy = By.xpath(labelXpath);
			By inputBy = By.xpath(inputXpath);

			logger.info("Generated Label XPath: " + labelXpath);
			logger.info("Generated Input XPath: " + inputXpath);

			boolean beforeSelected = false;
			if (isElementPresent(labelBy)) {
				beforeSelected = driver.findElement(inputBy).isSelected();
				logger.info("Before click - isSelected(): " + beforeSelected);
			} else {
				logger.warn(LogColor.RED + "Checkbox input NOT found before clicking: " + inputXpath + LogColor.RESET);
			}

			logger.info("Clicking checkbox LABEL (recommended for Auth0 UI) ...");
			clickAndDraw(waitForElement(labelBy));
			logger.info("Clicked checkbox LABEL successfully.");

			boolean afterSelected = driver.findElement(inputBy).isSelected();
			logger.info("After click - isSelected(): " + afterSelected);

			if (afterSelected) {
				logger.info(LogColor.DarkGreen + "✅ Checkbox is now CHECKED: " + checkboxId + LogColor.RESET);
			} else {
				logger.warn(LogColor.RED + "❌ Checkbox is still NOT checked after click: " + checkboxId + LogColor.RESET);
			}

			return afterSelected;
		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false;
		}
	}

//	***************************************************************************************************************
	public boolean clickOnButton(String buttonName) {

		try {
			logger.info("Formatting the X path of Button: "+buttonName);
			String formattedXpath = String.format(buttons, buttonName);
			logger.info("X path of Button: "+formattedXpath);

			waitForNetworkIdle();
			logger.info("Looking for the Button: "+buttonName);
			boolean visible = isElementPresent(By.xpath(formattedXpath));

			if(visible) {
				
				WebElement button = driver.findElement(By.xpath(formattedXpath));

				logger.info("Clicking on the Button: "+buttonName);
				clickAndDraw(button);
				logger.info("Clicked on the Button: "+buttonName);

				return true;
			}
			else {
				return false;
			}		

		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false;
		}
	}

//	***************************************************************************************************************
	public boolean visibilityOfRolePage(String textElement) {

		try {
			
			String formattedXpath = String.format(rolePage_subheader, textElement);
			logger.info(formattedXpath);
			
			By rolePage_subheader_by = By.xpath(formattedXpath);
			
			waitForElement(rolePage_subheader_by);

			boolean headerVisible = isElementPresent(rolePage_subheader_by);

			if(headerVisible) {
				return true;
			}
			else {
				return false;		
			}
		} catch (Exception e) {

			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false;
		}
	}

//	***************************************************************************************************************
	public boolean chooseSignUpRole(String roleName) {

		try {
			logger.info("Role Name received: " + roleName);

			String formattedXpath = String.format(signupRoles, roleName);
			By roleBy = By.xpath(formattedXpath);

			boolean presence = isElementPresent(roleBy);

			if (presence) {
				clickAndDraw(waitForElement(roleBy));

				logger.info("Clicked role label: " + roleName);
				return true;  
			}
			else {
				logger.error("Role label not found: " + roleName);
				return false;
			}

		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return false;
		}
	}

//	***************************************************************************************************************
	public boolean signupErrorVisible() {
	    try {
	        logger.info("Checking if signup error/validation message is visible...");

	        boolean visible = isElementPresent(SIGNUP_ERROR_BY);

	        if (visible) {
	            logger.info(LogColor.DarkGreen + "Signup error/validation message is visible." + LogColor.RESET);

	            // optional: log the actual texts (field errors + banner)
	            logger.info("Errors shown: " + getElementsTextbyLocator(
	                    By.cssSelector(".ulp-input-error-message, .ulp-validator-error, #prompt-alert p")
	            ));
	        } else {
	            logger.warn(LogColor.RED + "No signup error/validation message was detected." + LogColor.RESET);
	        }

	        return visible;

	    } catch (Exception e) {
	        logger.error(LogColor.RED + "Problem in Try Block" + LogColor.RESET);
	        logger.error(LogColor.RED + e + LogColor.RESET);
	        return false;
	    }
	}
	
	
}

