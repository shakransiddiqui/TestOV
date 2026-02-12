package ov.pages.passport;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import ov.utilities.CommonMethods;
import ov.utilities.LogColor;

public class ApplicationCreation_POM extends CommonMethods {


	public static final Logger logger = LogManager.getLogger(ApplicationCreation_POM.class);

	public ApplicationCreation_POM() {
		PageFactory.initElements(driver, this);
	}


	//	*****************X-path Locators****************************************************************



	//	******************Locator Format Template Strings****************************************************************************

	//Application Title field
	private static final String CREATE_APP_FIELDS = "//label[contains(normalize-space(.), '%s')]/following::input[1]";

	// This grabs the option "card" by class, and checks the combined text inside it
	private static final String OPTION_CARD = "//div[contains(@class,'SelectOption') and contains(normalize-space(.), '%s')][1]";

	// URL proof: after Save & Continue it includes application=<id>
	private static final String BUILDER_URL_MUST_CONTAIN = "application=";

	//Builder page proof elements
	private static final String page_TEXT = "//*[normalize-space()='%s']";

	// Generic: section/category container by its header text (Standard Questions / Additional Questions)
	private static final String CATEGORY_CONTAINER_BY_HEADER =
			"//*[normalize-space()='%s']/ancestor::div[contains(@class,'FormBuilderCategory')][1]";

	// Expanded content marker (exists when Standard Questions are expanded)
	private static final String EXPANDED_CONTENT_IN_CATEGORY =
			CATEGORY_CONTAINER_BY_HEADER + "//div[contains(@class,'question-preview-container')]";

	// Add New Question button inside Additional Questions category
	private static final String ADD_NEW_QUESTION_BTN_IN_CATEGORY =
			CATEGORY_CONTAINER_BY_HEADER + "//button[contains(@class,'section-add-question-btn')]";

	// New Question form proof (input placeholder) inside Additional Questions category
	private static final String NEW_QUESTION_INPUT_IN_CATEGORY =
			CATEGORY_CONTAINER_BY_HEADER + "//input[@placeholder='Write Question Here']";


	// Additional Questions - Dynamic XPath (scoped to category) =====================
	private static final String AQ_CATEGORY =
			String.format(CATEGORY_CONTAINER_BY_HEADER, "Additional Questions");



	//	******************By Locators****************************************************************************

	// ✅ Create New Application card is the one that has radio value='template'
	private static final By CREATE_NEW_APP_CARD = By.xpath("//div[contains(@class,'SelectOption')][.//input[@type='radio' and @value='template']][1]");

	private static final By CREATE_NEW_APP_RADIO = By.cssSelector("input[type='radio'][value='template']");


	// Collapse icon: locate it relative to the Preview Application button (stable anchor)
	private static final By COLLAPSE_BTN =
			By.xpath("(//button[contains(@class,'preview-btn')])[1]/preceding::div[contains(@class,'collapse-btn')][1]");

	private static final By AQ_QUESTION_INPUT =
			By.xpath(AQ_CATEGORY + "//input[@placeholder='Write Question Here'][1]");

	private static final By AQ_INSTRUCTION_EDITOR =
			By.xpath(AQ_CATEGORY + "//div[contains(@class,'question-rich-text-editor')]//div[contains(@class,'ql-editor') and @contenteditable='true'][1]");

	private static final By AQ_ANSWER_TYPE_SELECT =
			By.xpath(AQ_CATEGORY + "//select[@name='filterByTagTag' or @aria-label='filterByTagTag'][1]");

	private static final By AQ_REQUIRED_CHECKBOX =
			By.xpath(AQ_CATEGORY + "//input[contains(@class,'required-question-input')][1]");

	private static final By AQ_SAVE_BUTTON =
			By.xpath(AQ_CATEGORY + "//button[contains(@class,'question-save-action-btn') and (normalize-space()='Save' or @aria-label='SAVE')][1]");

	// Add New Question button (enabled state) inside Additional Questions category
	private static final By AQ_ADD_NEW_QUESTION_BTN_ENABLED =
			By.xpath(AQ_CATEGORY + "//button[contains(@class,'section-add-question-btn') and not(@disabled)][1]");


	//	****************Actions***********************************************************************************

	public String passFieldValue(String fieldValue, String fieldName) {
		try {
			if (fieldValue == null) fieldValue = "";

			String formattedXpath = String.format(CREATE_APP_FIELDS, fieldName);
			logger.info("Create Application Field XPath: " + formattedXpath);

			WebElement field = waitForElement(By.xpath(formattedXpath));
			clickAndDraw(field);

			safeSendKeys(field, fieldValue);

			String actual = field.getAttribute("value");
			logger.info(LogColor.DarkGreen + "Actual value in field [" + fieldName + "] = " + actual + LogColor.RESET);
			return actual;

		} catch (Exception e) {
			logger.error(LogColor.RED + "passFieldValue failed: " + e + LogColor.RESET);
			return "null";
		}
	}

	public boolean selectOption(String optionText) {
		try {
			logger.info("Selecting option: " + optionText);

			// For now we support the option you need in the scenario
			if (!"Create New Application".equalsIgnoreCase(optionText)) {
				logger.warn("Unsupported optionText in automation right now: " + optionText);
				return false;
			}

			// 1) Click the card (visible element)
			WebElement card = waitForElement(CREATE_NEW_APP_CARD);
			clickAndDraw(card);

			// 2) Verify/ensure the radio is selected
			WebElement radio = waitForElement(CREATE_NEW_APP_RADIO);

			if (!radio.isSelected()) {
				clickAndDraw(radio);
			}

			boolean selected = radio.isSelected();
			logger.info("Create New Application radio selected = " + selected);

			return selected;

		} catch (Exception e) {
			logger.error(LogColor.RED + "selectOption failed: " + e + LogColor.RESET);
			return false;
		}
	}

	public boolean isOnPage(String textElement) {
		try {

			String formattedXpath = String.format(page_TEXT, textElement);
			logger.info(formattedXpath);

			By text_by = By.xpath(formattedXpath);

			waitForElement(text_by);

			//Page contains Text
			boolean textOk = isElementPresent(text_by);

			//URL contains
			String url = driver.getCurrentUrl();
			logger.info("Current URL: " + url);

			boolean urlOk = url != null && url.contains(BUILDER_URL_MUST_CONTAIN);

			if(urlOk && textOk) {
				return true;
			}
			else {
				return false;
			}

		} catch (Exception e) {
			logger.error(LogColor.RED + "isOnBuilderPage failed: " + e + LogColor.RESET);
			return false;
		}
	}

	//	***************************************************************************************************************
	public boolean clickCollapseIcon() {
		try {
			logger.info("Clicking collapse icon (relative to Preview Application button)...");
			WebElement icon = waitForElement(COLLAPSE_BTN);
			if (icon == null) return false;

			clickAndDraw(icon);
			return true;

		} catch (Exception e) {
			logger.error(LogColor.RED + "clickCollapseIcon failed: " + e + LogColor.RESET, e);
			return false;
		}
	}

	//	***************************************************************************************************************
	public boolean standardQuestionsCollapsed() {
		try {
			logger.info("Checking if Standard Questions are collapsed...");

			// Collapsed definition:
			// Standard Questions category exists, but expanded content containers are NOT visible.
			By expanded = By.xpath(String.format(EXPANDED_CONTENT_IN_CATEGORY, "Standard Questions"));

			long end = System.currentTimeMillis() + 3000;
			boolean expandedVisible = isElementPresent(expanded);

			while (expandedVisible && System.currentTimeMillis() < end) {
				waitForMlsec(200);
				expandedVisible = isElementPresent(expanded);
			}

			return !expandedVisible;
		} catch (Exception e) {
			logger.error(LogColor.RED + "standardQuestionsCollapsed failed: " + e + LogColor.RESET, e);
			return false;
		}
	}

	//	***************************************************************************************************************
	public boolean scrollToSection(String sectionName) {
		try {
			logger.info("Scrolling to section: " + sectionName);

			// We scroll to the section header itself
			By header = By.xpath("//*[normalize-space()='" + sectionName + "']");
			WebElement headerEl = waitForElement(header);
			if (headerEl == null) return false;

			scrollScreen(headerEl);
			return true;

		} catch (Exception e) {
			logger.error(LogColor.RED + "scrollToSection failed: " + e + LogColor.RESET, e);
			return false;
		}
	}

	//	***************************************************************************************************************
	public boolean clickAddNewQuestion(String buttonText) {
		try {
			logger.info("Clicking Add New Question in Additional Questions section. Param=" + buttonText);

			// Scope the button to Additional Questions category container
			By btn = By.xpath(String.format(ADD_NEW_QUESTION_BTN_IN_CATEGORY, "Additional Questions"));

			WebElement btnEl = waitForElement(btn);
			if (btnEl == null) return false;

			scrollScreen(btnEl);
			clickAndDraw(btnEl);

			return true;

		} catch (Exception e) {
			logger.error(LogColor.RED + "clickAddNewQuestion failed: " + e + LogColor.RESET, e);
			return false;
		}
	}

	//	***************************************************************************************************************
	public boolean newQuestionFormVisible() {
		try {
			logger.info("Checking New Question form visibility (scoped to Additional Questions)...");

			By input = By.xpath(String.format(NEW_QUESTION_INPUT_IN_CATEGORY, "Additional Questions"));

			return isElementPresent(input);

		} catch (Exception e) {
			logger.error(LogColor.RED + "newQuestionFormVisible failed: " + e + LogColor.RESET, e);
			return false;
		}
	}



	//	***************************************************************************************************************

	//	***************************************************************************************************************
	public int addAdditionalQuestions(List<Map<String, String>> rows) {

		int success = 0;

		try {
			logger.info("Adding Additional Questions. Rows=" + rows.size());

			for (int i = 0; i < rows.size(); i++) {

				Map<String, String> row = rows.get(i);

				String type = row.get("Type");
				String question = row.get("Question");
				String instruction = row.get("Instruction");
				String requiredStr = row.getOrDefault("Required", "No");
				boolean required = "Yes".equalsIgnoreCase(requiredStr) || "True".equalsIgnoreCase(requiredStr);

				logger.info("Row#" + (i + 1) + " Type=" + type + " Question=" + question);

				boolean added = addOneAdditionalQuestion(type, question, instruction, required);

				if (added) {
					success++;

					// If more questions remain, click Add New Question to open a fresh form
					if (i < rows.size() - 1) {
						boolean opened = openNewAdditionalQuestionForm();
						if (!opened) {
							logger.warn("Could not open new question form for next row.");
							break;
						}
					}
				} else {
					logger.warn("Failed to add question row#" + (i + 1) + ": " + question);
					break;
				}
			}

		} catch (Exception e) {
			logger.error(LogColor.RED + "addAdditionalQuestions failed: " + e + LogColor.RESET, e);
		}

		logger.info("Additional Questions added successfully = " + success + "/" + rows.size());
		return success;
	}

	//	***************************************************************************************************************
	private boolean openNewAdditionalQuestionForm() {
		try {
			WebElement addBtn = waitForElement(AQ_ADD_NEW_QUESTION_BTN_ENABLED);
			if (addBtn == null) return false;

			scrollScreen(addBtn);
			clickAndDraw(addBtn);

			// form is open when question input appears
			WebElement q = waitForElement(AQ_QUESTION_INPUT);
			return q != null;

		} catch (Exception e) {
			logger.error(LogColor.RED + "openNewAdditionalQuestionForm failed: " + e + LogColor.RESET, e);
			return false;
		}
	}

	//	***************************************************************************************************************
	private boolean addOneAdditionalQuestion(String type, String question, String instruction, boolean required) {
		try {
			// 1) Question
			WebElement qInput = waitForElement(AQ_QUESTION_INPUT);
			if (qInput == null) return false;

			scrollScreen(qInput);
			clickAndDraw(qInput);
			safeSendKeys(qInput, question);

			// 2) Instructions (Quill editor)
			WebElement editor = waitForElement(AQ_INSTRUCTION_EDITOR);
			if (editor == null) return false;

			clickAndDraw(editor);

			// clear then type (Quill)
			editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			editor.sendKeys(Keys.BACK_SPACE);

			if (instruction != null && !instruction.isBlank()) {
				editor.sendKeys(instruction);
			}

			// 3) Answer Type dropdown
			WebElement selectEl = waitForElement(AQ_ANSWER_TYPE_SELECT);
			if (selectEl == null) return false;

			Select sel = new Select(selectEl);
			sel.selectByVisibleText(type);

			// 4) Required checkbox
			WebElement req = waitForElement(AQ_REQUIRED_CHECKBOX);
			if (req == null) return false;

			if (required && !req.isSelected()) clickAndDraw(req);
			if (!required && req.isSelected()) clickAndDraw(req);

			// 5) Save question
			WebElement saveBtn = waitForElement(AQ_SAVE_BUTTON);
			if (saveBtn == null) return false;

			clickAndDraw(saveBtn);

			// After save, Add New Question button becomes enabled again (good “save completed” signal)
			WebElement addBtn = waitForElement(AQ_ADD_NEW_QUESTION_BTN_ENABLED);
			return addBtn != null;

		} catch (Exception e) {
			logger.error(LogColor.RED + "addOneAdditionalQuestion failed: " + e + LogColor.RESET, e);
			return false;
		}
	}

}
