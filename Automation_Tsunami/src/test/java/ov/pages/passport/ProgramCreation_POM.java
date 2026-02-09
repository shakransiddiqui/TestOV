package ov.pages.passport;

import java.time.Duration;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import ov.utilities.CommonMethods;
import ov.utilities.LogColor;

public class ProgramCreation_POM extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(ProgramCreation_POM.class);


	public ProgramCreation_POM() {
		PageFactory.initElements(driver, this);
	}


	//	*****************X-path Locators****************************************************************




	//	******************By Locators***************************************************************************

	private static final By INDUSTRY_SEARCH_INPUT = By.cssSelector("input[name='searchindustries']");
	private static final By INDUSTRY_ITEMS = By.cssSelector("ul.dropdown-list > li");
	private static final By INDUSTRY_PILLS = By.cssSelector("div.sector-pill-container > *");



	//	******************Locator Format Template Strings****************************************************************************

	//All fields to fill for Program Details 
	private static final String ProgramDetails_fields ="//label[contains(normalize-space(.), '%s')]/following::input[1]";

	//For Location
	private static final String ProgramDetails_Location_field ="//label[contains(normalize-space(.), '%s')]/following::input[3]";
	private static final String locationSuggestion = "(//div[contains(@class,'pac-container')]//div[contains(@class,'pac-item')])[1]";

	//For Program Type
	private static final By PROGRAM_TYPE_SELECTS = By.cssSelector("select[name='ProgramTag'], select[aria-label='ProgramTag']");
	private static final List<String> PROGRAM_TYPES = List.of("Accelerator", "Contest", "Incubator", "Other");
	private static final By PROGRAM_TYPE_DROPDOWN_BOX =By.xpath("//label[contains(normalize-space(.),'Program Type')]/following::div[contains(@class,'select-input')][1]");

	//For Program Description
	private static final String ProgramDetails_ProgramDescription_editor =  "//label[contains(normalize-space(.), '%s')]/following::div[contains(@class,'ql-editor') and @contenteditable='true'][1]";

	//For Perks
	private static final String perksPage_text = "//span[contains(text(), '%s')]";
	private static final By PERK_CARDS =
			By.cssSelector("div.rewards-perk-item");   // the whole card

	private static final By PERK_CHECKED =
			By.cssSelector("div.rewards-perk-item input[type='checkbox']:checked");

	//	**************** Actions ***********************************************************************************

	public String passFieldValue(String fieldElement_Value, String fieldElement_Name) {	
		try {

			if ("Program Type".equalsIgnoreCase(fieldElement_Name)) {
				return selectProgramType(fieldElement_Value);
			}

			String formattedXpath ="null";

			// SPECIAL X path handling for Different Fields and Drop downs
			if("Location".equalsIgnoreCase(fieldElement_Name)) {
				logger.info("Formatting the X path of Field: "+fieldElement_Name);
				formattedXpath = String.format(ProgramDetails_Location_field, fieldElement_Name);
			}
			else if ("Program Description".equalsIgnoreCase(fieldElement_Name)) {
				logger.info("Formatting the X path of Field: " + fieldElement_Name);
				formattedXpath = String.format(ProgramDetails_ProgramDescription_editor, fieldElement_Name);
			}
			else {
				logger.info("Formatting the X path of Field: "+fieldElement_Name);
				formattedXpath = String.format(ProgramDetails_fields, fieldElement_Name);
			}

			logger.info(formattedXpath);

			logger.info("Finding the Field: "+fieldElement_Name);
			WebElement field = waitForElement(By.xpath(formattedXpath));

			// SPECIAL handling for Program Description (Quill editor)
			if ("Program Description".equalsIgnoreCase(fieldElement_Name)) {

				logger.info("Typing into Program Description rich text editor...");

				clickAndDraw(field);

				field.sendKeys(fieldElement_Value);

				String actualFieldValue = field.getText().trim();
				logger.info(LogColor.DarkGreen + "Actual Program Description: " + actualFieldValue + LogColor.RESET);

				return actualFieldValue;
			}

			logger.info("Clicking on the Field: "+fieldElement_Name);
			clickAndDraw(field);

			logger.info("Passing value on the Field: "+fieldElement_Value);
			safeSendKeys(field, fieldElement_Value);

			// SPECIAL handling for Location autocomplete
			if ("Location".equalsIgnoreCase(fieldElement_Name)) {

				logger.info("Selecting first location suggestion from dropdown");
				WebElement suggestion = waitForElement(By.xpath(locationSuggestion));
				drawborder(suggestion);
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


	//	***************************************************************************************************************
	public String selectProgramType(String value) {
		try {
			logger.info("Selecting Program Type. Input = " + value);

			// 1) Click the visible dropdown box to trigger loading/rendering
			WebElement box = waitForElement(PROGRAM_TYPE_DROPDOWN_BOX);
			box.click();

			// 2) Wait for the real <select> to have options (not just placeholder)
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PROGRAM_TYPE_SELECTS));

			// choose best select (some pages render multiple)
			long end = System.currentTimeMillis() + 5000;
			WebElement best = null;
			int bestCount = 0;

			while (System.currentTimeMillis() < end) {
				List<WebElement> selects = driver.findElements(PROGRAM_TYPE_SELECTS);

				for (WebElement s : selects) {
					try {
						int count = new Select(s).getOptions().size();
						if (count > bestCount) {
							bestCount = count;
							best = s;
						}
					} catch (Exception ignore) {}
				}

				if (bestCount > 1 && best != null && best.getAttribute("disabled") == null) {
					break; // options loaded + enabled
				}

				waitForMlsec(200);
			}

			if (best == null) return "null";

			logger.info("Program Type best option count = " + bestCount);
			logger.info("Program Type outerHTML: " + best.getAttribute("outerHTML"));

			if (bestCount <= 1) {
				logger.error(LogColor.RED + "Program Type options not loaded (still placeholder only)." + LogColor.RESET);
				return "null";
			}

			Select select = new Select(best);

			String chosen = value;
			if ("ANY".equalsIgnoreCase(value)) {
				int idx = randInt(0, PROGRAM_TYPES.size() - 1);
				chosen = PROGRAM_TYPES.get(idx);
			}

			logger.info("Program Type chosen = " + chosen);
			select.selectByVisibleText(chosen);

			String selected = select.getFirstSelectedOption().getText().trim();
			logger.info(LogColor.DarkGreen + "Selected Program Type = " + selected + LogColor.RESET);

			return selected;

		} catch (Exception e) {
			logger.error(LogColor.RED + "Problem in Try Block" + LogColor.RESET);
			logger.error(LogColor.RED + e + LogColor.RESET);
			return "null";
		}
	}

	//	***************************************************************************************************************
	public int selectRandomIndustries(int howMany) {
		try {
			logger.info("Selecting industries. Requested count = " + howMany);

			// 1) Open the industries dropdown by clicking the search input
			WebElement input = waitForElement(INDUSTRY_SEARCH_INPUT);
			
			scrollScreen(input);   
			
			nativeClick(input);


			// 2) Wait until industries list items are present
			List<WebElement> items = driver.findElements(INDUSTRY_ITEMS);
			long end = System.currentTimeMillis() + 5000;

			while (items.size() == 0 && System.currentTimeMillis() < end) {
				waitForMlsec(200);
				items = driver.findElements(INDUSTRY_ITEMS);
			}

			if (items.size() == 0) {
				logger.error(LogColor.RED + "Industries dropdown has no items." + LogColor.RESET);
				return 0;
			}

			logger.info("Industries available = " + items.size());

			// 3) If requested 0, do nothing and just return how many are currently selected
			if (howMany <= 0) {
				int pillCount = driver.findElements(INDUSTRY_PILLS).size();
				logger.info("Requested 0 industries. Selected pill count = " + pillCount);
				return pillCount;
			}

			// 4) Make sure we don’t try to pick more than what exists
			int target = Math.min(howMany, items.size());

			// 5) Pick unique random indexes (no duplicates)
			Set<Integer> picks = new java.util.LinkedHashSet<>();
			while (picks.size() < target) {
				picks.add(randInt(0, items.size() - 1));
			}

			// 6) Click each chosen industry row (selects its checkbox)
			for (int idx : picks) {
				WebElement li = items.get(idx);
				String name = li.getText().trim();
				logger.info("Selecting industry: " + name);
				nativeClick(li);
			}

			// 7) Verify by counting the pills (selected industries)
			int selectedCount = driver.findElements(INDUSTRY_PILLS).size();

			long end2 = System.currentTimeMillis() + 3000;
			while (selectedCount != target && System.currentTimeMillis() < end2) {
				waitForMlsec(200);
				selectedCount = driver.findElements(INDUSTRY_PILLS).size();
			}

			logger.info(LogColor.DarkGreen + "Selected industries pill count = " + selectedCount + LogColor.RESET);
			return selectedCount;

		} catch (Exception e) {
			logger.error(LogColor.RED + "Problem in Try Block" + LogColor.RESET);
			logger.error(LogColor.RED + e + LogColor.RESET);
			return 0;
		}
	}


	//	***************************************************************************************************************
	public boolean visibilityOfTextOnPage(String textElement) {

		try {

			String formattedXpath = String.format(perksPage_text, textElement);
			logger.info(formattedXpath);

			By text_by = By.xpath(formattedXpath);

			waitForElement(text_by);

			boolean Visible = isElementPresent(text_by);

			if(Visible) {
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
	public int selectRandomPerks(int requested) {
		try {
			logger.info("Selecting perks. Requested = " + requested);

			int target = Math.min(requested, 5);

			List<WebElement> cards = driver.findElements(PERK_CARDS);
			if (cards.isEmpty()) {
				logger.error(LogColor.RED + "No perk cards found." + LogColor.RESET);
				return 0;
			}

			// 1) Clear existing selections (back to 0)
			int checked = driver.findElements(PERK_CHECKED).size();
			long endClear = System.currentTimeMillis() + 3000;
			while (checked > 0 && System.currentTimeMillis() < endClear) {

				// click all checked ones to uncheck
				List<WebElement> checkedBoxes = driver.findElements(PERK_CHECKED);
				for (WebElement cb : checkedBoxes) {
					nativeClick(cb); // clicking checkbox is safest for uncheck
				}

				waitForMlsec(200);
				checked = driver.findElements(PERK_CHECKED).size();
			}

			// 2) If target = 0, we’re done
			if (target == 0) {
				logger.info("Target=0 perks. Checked perks now = 0");
				return 0;
			}

			// 3) Pick unique random cards
			target = Math.min(target, cards.size());

			Set<Integer> picks = new LinkedHashSet<>();
			while (picks.size() < target) {
				picks.add(randInt(0, cards.size() - 1));
			}

			for (int idx : picks) {
				logger.info("Selecting perk card index: " + idx);
				scrollScreen(cards.get(idx));
				nativeClick(cards.get(idx)); // click the CARD
			}

			// 4) Verify count
			int finalChecked = driver.findElements(PERK_CHECKED).size();
			long end = System.currentTimeMillis() + 3000;
			while (finalChecked != target && System.currentTimeMillis() < end) {
				waitForMlsec(200);
				finalChecked = driver.findElements(PERK_CHECKED).size();
			}

			logger.info(LogColor.DarkGreen + "Checked perks count = " + finalChecked + LogColor.RESET);
			return finalChecked;

		} catch (Exception e) {
			logger.error(LogColor.RED + "Problem in Try Block" + LogColor.RESET);
			logger.error(LogColor.RED + e + LogColor.RESET);
			return 0;
		}
	}

	
	private void nativeClick(WebElement el) {
	    waitForClickablility(el);
	    drawborder(el);
	    el.click();
	}

}

