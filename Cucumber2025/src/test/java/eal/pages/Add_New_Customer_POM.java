package eal.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import eal.Utilities.CommonMethods;
import eal.Utilities.LogColor;

public class Add_New_Customer_POM extends CommonMethods  {

	public static final Logger logger = LogManager.getLogger(DashBoard_POM.class);

	//PageFactory Constructor:
	public Add_New_Customer_POM() {
		PageFactory.initElements(driver, this);
	}

	//*************************************************
	//Dynamic Locator Templates:
	//*************************************************

	private static final String FIELD_INPUT_XPATH = "//td[text()='%s']/following-sibling::td/input | //td[text()='%s']/following-sibling::td/textarea";
	private static final String GENDER_RADIO_BUTTON ="//td[text()='Gender']/following-sibling::td/input[@value='%s']";

	private static final String AddNewCustomer_Buttons = "//td/input[@value='%s']";
	
	//*************************************************
	//Action Methods:
	//*************************************************

	//	**********************************************************************************************************************************************************
	public boolean enterTypableFieldValue(String fieldName, String value) {

		try {

			String formattedXpath = String.format(FIELD_INPUT_XPATH, fieldName, fieldName);
			logger.info(LogColor.DarkGreen+formattedXpath+LogColor.RESET);

			WebElement input = driver.findElement(By.xpath(formattedXpath));

			safeSendKeys(input, value);

			String ActualFieldValue = input.getAttribute("value");
			boolean success = ActualFieldValue.equals(value);

			if(success) {
				logger.info("Entered value for field: "+ fieldName + " -> " + value);
			}
			else {
				logger.warn("Value Mismatch for field: "+ fieldName + ". Expected: " + value + "but found: "+ ActualFieldValue);
			}

			return success;
		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);
			return false;
		}
	}

	//	**********************************************************************************************************************************************************
	public boolean enterDateFieldValue(String fieldName, String value) {

		try {
			String formattedXpath = String.format(FIELD_INPUT_XPATH, fieldName, fieldName);
			logger.info(LogColor.DarkGreen+formattedXpath+LogColor.RESET);
			WebElement input = driver.findElement(By.xpath(formattedXpath));

			boolean isDateInserted = safeSetDateValue(input, value);

			return isDateInserted;

		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);
			return false;
		}
	}

	//	**********************************************************************************************************************************************************
	public boolean selectGender(String fieldName, String value) {

		try {
			WebElement input = null;

			if(value.equalsIgnoreCase("male")) {
				input =driver.findElement(By.xpath(String.format(GENDER_RADIO_BUTTON, "m")));
			}
			else if(value.equalsIgnoreCase("female")) {
				input =driver.findElement(By.xpath(String.format(GENDER_RADIO_BUTTON, "f")));
			}

			if(input==null) {
				logger.error(LogColor.RED + "Invalid gender option passed: "+fieldName + LogColor.RESET);
				return false;
			}

			safeSelectRadioButton(input);

			boolean isRadioButtonSelected = input.isSelected();

			if(isRadioButtonSelected) {
				logger.info("Radio Button selected successfully for: "+ fieldName + " -> " + value);
			}
			else {
				logger.warn("Radio Button NOT selected for: "+ fieldName + " -> " + value);
			}

			return isRadioButtonSelected;
		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);
			return false;
		}
	}

	//	**********************************************************************************************************************************************************
	public boolean clickOnSubmitButton(String buttonName_fromSD) {

		try {

			String formattedStringXpath =String.format(AddNewCustomer_Buttons, buttonName_fromSD);
			logger.info(formattedStringXpath);

			boolean presence=isElementPresent(By.xpath(formattedStringXpath));
			if(presence) {

				WebElement Button = driver.findElement(By.xpath(formattedStringXpath));
				logger.info("Clicking on the Element LinkButton: "+Button);

				clickAndDraw(Button);

				return true;
			}
			else {
				return false;
			}

		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);	
			return false;
		}
	}
}
