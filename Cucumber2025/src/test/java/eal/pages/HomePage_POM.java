package eal.pages;


import eal.Utilities.CommonMethods;

import eal.Utilities.LogColor;

import org.apache.logging.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage_POM extends CommonMethods{

	public static final Logger logger = LogManager.getLogger(HomePage_POM.class);

	//PageFactory Constructor:
	public HomePage_POM() {
		PageFactory.initElements(driver, this);
	}

	//@findBy:
	@FindBy(xpath="//td[contains(text(),'UserID')]")
	public WebElement userid_text;

	@FindBy (xpath="//td[contains(text(),'Password')]")
	public WebElement password_text;

	@FindBy (xpath="//td[contains(text(),'UserID')]/following-sibling::td/input")
	public WebElement userid_field;

	@FindBy (xpath="//td[contains(text(),'Password')]/following-sibling::td/input")
	public WebElement password_field;

	@FindBy (xpath="//input[@value='LOGIN']")
	public WebElement login_button;

	@FindBy (xpath="//input[@value='RESET']")
	public WebElement reset_button;

	//By Locator:
	By userid_text_By = By.xpath("//td[contains(text(),'UserID')]");
	By password_text_By = By.xpath("//td[contains(text(),'Password')]");


	//Controllable Xpath Strings:

	String hm_UiTextcontained_Elements= "//td[contains(text(),'%s')]";

	String hm_UiFieldcontained_Elements= "//td[contains(text(),'%s')]/following-sibling::td/input";


	//	**********************************************************************************************************************************************************
	public boolean verify_homepage_title() {

		try {
			logger.info("Getting the Actual Title");
			String actualTitle = driver.getTitle();
			logger.info("Got the Title");

			String expectedTitle = "Guru99 Bank Home Page";

			if(actualTitle.equals(expectedTitle)) {
				return true;

			}
			else {
				return false;
			}

		} catch(Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);
			return false;
		}
	}

	//	**********************************************************************************************************************************************************
	public boolean verify_homepage_UiTextElement_is_visible(String UiTextElement_fromSD) {

		try {

			String formattedStringXpathOf_Ui_Element =String.format(hm_UiTextcontained_Elements, UiTextElement_fromSD);
			logger.info(formattedStringXpathOf_Ui_Element);

			boolean presence=isElementPresent(By.xpath(formattedStringXpathOf_Ui_Element));
			if(presence) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (Exception e) {

			logger.error(LogColor.RED + e + LogColor.RESET);
			return false;
		}
	}

	//	**********************************************************************************************************************************************************
	public String passFieldValue(String UiFieldElement_Value_fromSD,String UiFieldElement_Name_fromSD) {

		try {

			String formatted_hm_UiFieldcontained_Elements = String.format(hm_UiFieldcontained_Elements, UiFieldElement_Name_fromSD);
			logger.info(formatted_hm_UiFieldcontained_Elements);


			logger.info("Finding the Field: "+UiFieldElement_Name_fromSD);
			WebElement login_field = driver.findElement(By.xpath(formatted_hm_UiFieldcontained_Elements));

			logger.info("Clicking on the Field: "+UiFieldElement_Name_fromSD);
			clickAndDraw(login_field);

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

	//	**********************************************************************************************************************************************************
	public String clickingOnLoginButton_with_Invalid_Credentials() {

		try {
			logger.info("Clicking on the Login button");

			clickAndDraw(login_button);

			String actualAlertText = getAlertText();

			return actualAlertText;

		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);

			return null;
		}

	}


	//	**********************************************************************************************************************************************************
	public String clearField(String fieldName) {
		
		try {
			String formatted_hm_UiFieldcontained_Elements = String.format(hm_UiFieldcontained_Elements, fieldName);
			logger.info(formatted_hm_UiFieldcontained_Elements);


			logger.info("Finding the Field: "+fieldName);
			WebElement field = driver.findElement(By.xpath(formatted_hm_UiFieldcontained_Elements));

			field.clear();

			String ActualFieldValue_afterClear = getAttributeValue(field, "value");
			return ActualFieldValue_afterClear;
			
		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);

			return null;

		}
	}
	
	//	**********************************************************************************************************************************************************
	public void clickingOnResetButton() {

		try {
			clickAndDraw(reset_button);
			
		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);

		}
	}

	
//	**********************************************************************************************************************************************************
	public boolean clickingOnLoginButton_with_Valid_Credentials() {

		try {
			logger.info("Clicking on the Login button");

			clickAndDraw(login_button);

			boolean ifTitleMatched = verifyPageTitle("Guru99 Bank Manager HomePage");

			return ifTitleMatched;

		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);

			return false;
		}

	}

}



