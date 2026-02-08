package ov.pages.passport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import ov.utilities.CommonMethods;
import ov.utilities.LogColor;

public class ProgramCreation_POM extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(ProgramCreation_POM.class);


	public ProgramCreation_POM() {
		PageFactory.initElements(driver, this);
	}


	//	*****************X-path Locators****************************************************************




	//	******************By Locators***************************************************************************



	//	******************Locator Format Template Strings****************************************************************************
	
	//All fields to fill for Program Details 
	private static final String ProgramDetails_fields ="//label[contains(normalize-space(.), '%s')]/following::input[1]";
		

	// FIRST suggestion from Google dropdown (no text matching)
	private static final String locationSuggestion = "(//div[contains(@class,'pac-container')]//div[contains(@class,'pac-item')])[1]";
	


	//	**************** Actions ***********************************************************************************

	public String passFieldValue(String fieldElement_Value,String fieldElement_Name) {

		try {
			logger.info("Formatting the X path of Field: "+fieldElement_Name);
			String formattedXpath = String.format(ProgramDetails_fields, fieldElement_Name);
			logger.info(formattedXpath);


			logger.info("Finding the Field: "+fieldElement_Name);
			WebElement field = driver.findElement(By.xpath(formattedXpath));

			logger.info("Clicking on the Field: "+fieldElement_Name);
			clickAndDraw(field);

			logger.info("Passing value on the Field: "+fieldElement_Value);
			safeSendKeys(field, fieldElement_Value);
			
			// SPECIAL handling for Location autocomplete
			if ("Location".equalsIgnoreCase(fieldElement_Name)) {

			    logger.info("Selecting first location suggestion from dropdown");
			    WebElement suggestion = waitForElement(By.xpath(locationSuggestion));
			    clickAndDraw(suggestion);
			    suggestion.click();
			}

			logger.info("Making sure value is filled up on the Field: "+fieldElement_Name);
			String actualFieldValue = field.getAttribute("value");

			logger.info(LogColor.DarkGreen +"Actual value in the filed :"+actualFieldValue+ LogColor.RESET);
			return actualFieldValue;

		} catch (Exception e) {
			logger.error(LogColor.RED+"Problem in Try Block"+LogColor.RESET);
			logger.error(LogColor.RED+e+LogColor.RESET);
			return "null";
		}
	}

}

