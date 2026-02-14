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



	//	******************Locator Format Template Strings***********************************************

	// Application Title field
	private static final String CREATE_APP_FIELDS =
			"//label[contains(normalize-space(.), '%s')]/following::input[1]";

	// URL proof: after Save & Continue it includes application=<id>
	private static final String BUILDER_URL_MUST_CONTAIN = "application=";

	// Builder page proof elements
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

	// Additional Questions scoped category
	private static final String AQ_CATEGORY =
			String.format(CATEGORY_CONTAINER_BY_HEADER, "Additional Questions");

	// “Current open question form” inside Additional Questions
	private static final String AQ_OPEN_FORM =
			AQ_CATEGORY
			+ "//*[contains(@class,'question-base-container') or contains(@class,'QuestionBaseForm')]"
			+ "[.//input[@placeholder='Write Question Here']][1]";

	//	******************By Locators****************************************************************************

	// Create New Application card (radio value='template')
	private static final By CREATE_NEW_APP_CARD =
			By.xpath("//div[contains(@class,'SelectOption')][.//input[@type='radio' and @value='template']][1]");

	private static final By CREATE_NEW_APP_RADIO =
			By.cssSelector("input[type='radio'][value='template']");

	// Collapse icon/button near Preview Application
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

	private static final By AQ_ADD_NEW_QUESTION_BTN_ENABLED =
			By.xpath(AQ_CATEGORY + "//button[contains(@class,'section-add-question-btn') and not(@disabled)][1]");

	// Choice options
	private static final By AQ_ADD_OPTION_BTN =
			By.xpath(AQ_OPEN_FORM + "//div[contains(@class,'add-option-btn-container')]//button[contains(@class,'add-new-option-btn')][1]");

	private static final By AQ_LAST_OPTION_INPUT =
			By.xpath("(" + AQ_OPEN_FORM + "//div[contains(@class,'choice-options-container')]//input)[last()]");

	private static final By AQ_DISPLAY_AS_RADIO =
			By.xpath(AQ_OPEN_FORM + "//*[normalize-space()='Display As']/following::*[normalize-space()='Radio'][1]");

	private static final By AQ_DISPLAY_AS_CHECKBOX =
			By.xpath(AQ_OPEN_FORM + "//*[normalize-space()='Display As']/following::*[normalize-space()='Checkbox'][1]");

	// File type dropdown that appears only when Answer Type = File
	private static final By AQ_FILE_TYPE_SELECT =
			By.xpath(AQ_OPEN_FORM + "//label[contains(normalize-space(.),'Select File Type')]/following::select[1]");


	//	***************************************************************************************************************
	//	Main Actions Methods to call from Step Definitions:
	//	***************************************************************************************************************

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

			if (!"Create New Application".equalsIgnoreCase(optionText)) {
				logger.warn("Unsupported optionText in automation right now: " + optionText);
				return false;
			}

			WebElement card = waitForElement(CREATE_NEW_APP_CARD);
			clickAndDraw(card);

			WebElement radio = waitForElement(CREATE_NEW_APP_RADIO);
			if (!radio.isSelected()) clickAndDraw(radio);

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
			By text_by = By.xpath(formattedXpath);

			waitForElement(text_by);

			boolean textOk = isElementPresent(text_by);

			String url = driver.getCurrentUrl();
			boolean urlOk = url != null && url.contains(BUILDER_URL_MUST_CONTAIN);

			return urlOk && textOk;

		} catch (Exception e) {
			logger.error(LogColor.RED + "isOnBuilderPage failed: " + e + LogColor.RESET);
			return false;
		}
	}

	//	***************************************************************************************************************
	public boolean clickCollapseIcon() {
		try {
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
			By input = By.xpath(String.format(NEW_QUESTION_INPUT_IN_CATEGORY, "Additional Questions"));
			return isElementPresent(input);

		} catch (Exception e) {
			logger.error(LogColor.RED + "newQuestionFormVisible failed: " + e + LogColor.RESET, e);
			return false;
		}
	}

	//	***************************************************************************************************************
	public int addAdditionalQuestions(List<Map<String, String>> rows) {

		int success = 0;

		try {
			for (int i = 0; i < rows.size(); i++) {

				Map<String, String> row = rows.get(i);

				String type = row.get("Type");
				String question = row.get("Question");
				String instruction = row.get("Instruction");
				String requiredStr = row.getOrDefault("Required", "No");
				boolean required = "Yes".equalsIgnoreCase(requiredStr) || "True".equalsIgnoreCase(requiredStr);

				boolean added = addOneAdditionalQuestion(type, question, instruction, required);

				if (added) {
					success++;

					if (i < rows.size() - 1) {
						boolean opened = openNewAdditionalQuestionForm();
						if (!opened) break;
					}
				} else {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(LogColor.RED + "addAdditionalQuestions failed: " + e + LogColor.RESET, e);
		}

		return success;
	}

	//	***************************************************************************************************************
	//	Helper Methods
	//	***************************************************************************************************************
	private boolean openNewAdditionalQuestionForm() {
		try {
			WebElement addBtn = waitForElement(AQ_ADD_NEW_QUESTION_BTN_ENABLED);
			if (addBtn == null) return false;

			scrollScreen(addBtn);
			clickAndDraw(addBtn);

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
			WebElement qInput = waitForElement(AQ_QUESTION_INPUT);
			if (qInput == null) return false;

			scrollScreen(qInput);
			clickAndDraw(qInput);
			safeSendKeys(qInput, question);

			WebElement editor = waitForElement(AQ_INSTRUCTION_EDITOR);
			if (editor == null) return false;

			clickAndDraw(editor);
			editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			editor.sendKeys(Keys.BACK_SPACE);

			if (instruction != null && !instruction.isBlank()) {
				editor.sendKeys(instruction);
			}

			WebElement selectEl = waitForElement(AQ_ANSWER_TYPE_SELECT);
			if (selectEl == null) return false;

			Select sel = new Select(selectEl);
			sel.selectByVisibleText(type);

			if ("Single Choice".equalsIgnoreCase(type)) {
				boolean ok = addChoiceOptions(Arrays.asList("Option 1", "Option 2"));
				if (!ok) return false;
			}

			if ("Multiple Choice".equalsIgnoreCase(type)) {
				boolean ok = addChoiceOptions(Arrays.asList("Option 1", "Option 2", "Option 3"));
				if (!ok) return false;
			}

			// ✅ File requires selecting the file type before saving
			if ("File".equalsIgnoreCase(type)) {
				// Default to PDF (matches your instruction “PDF only”)
				boolean ok = selectFileType("PDF");
				if (!ok) return false;
			}



			WebElement req = waitForElement(AQ_REQUIRED_CHECKBOX);
			if (req == null) return false;

			if (required && !req.isSelected()) clickAndDraw(req);
			if (!required && req.isSelected()) clickAndDraw(req);

			WebElement saveBtn = waitForElement(AQ_SAVE_BUTTON);
			if (saveBtn == null) return false;

			clickAndDraw(saveBtn);

			WebElement addBtn = waitForElement(AQ_ADD_NEW_QUESTION_BTN_ENABLED);
			return addBtn != null;

		} catch (Exception e) {
			logger.error(LogColor.RED + "addOneAdditionalQuestion failed: " + e + LogColor.RESET, e);
			return false;
		}
	}

	//	***************************************************************************************************************
	private boolean addChoiceOptions(List<String> options) {
		try {
			WebElement addBtn = waitForElement(AQ_ADD_OPTION_BTN);
			if (addBtn == null) return false;

			try {
				if (isElementPresent(AQ_DISPLAY_AS_CHECKBOX)) {
					clickAndDraw(waitForElement(AQ_DISPLAY_AS_CHECKBOX));
				} else if (isElementPresent(AQ_DISPLAY_AS_RADIO)) {
					clickAndDraw(waitForElement(AQ_DISPLAY_AS_RADIO));
				}
			} catch (Exception ignore) {}

			for (String opt : options) {
				WebElement addBtn2 = waitForElement(AQ_ADD_OPTION_BTN);
				if (addBtn2 == null) return false;

				clickAndDraw(addBtn2);

				WebElement optionInput = waitForElement(AQ_LAST_OPTION_INPUT);
				if (optionInput == null) return false;

				clickAndDraw(optionInput);
				safeSendKeys(optionInput, opt);

				optionInput.sendKeys(Keys.ENTER);
				waitForMlsec(200);
			}

			return true;

		} catch (Exception e) {
			logger.error(LogColor.RED + "addChoiceOptions failed: " + e + LogColor.RESET, e);
			return false;
		}
	}

	//	***************************************************************************************************************
	private boolean selectFileType(String expectedType) {
		try {
			logger.info("Selecting File Type: " + expectedType);

			WebElement selectEl = waitForElement(AQ_FILE_TYPE_SELECT);
			if (selectEl == null) return false;

			Select sel = new Select(selectEl);

			// Some options are like "PDF (.pdf)" so we use CONTAINS match
			for (WebElement opt : sel.getOptions()) {
				String text = opt.getText().trim();
				if (text.equalsIgnoreCase(expectedType) || text.toLowerCase().contains(expectedType.toLowerCase())) {
					opt.click();
					logger.info("Selected File Type option: " + text);
					return true;
				}
			}

			logger.warn("No matching File Type option found for: " + expectedType);
			return false;

		} catch (Exception e) {
			logger.error(LogColor.RED + "selectFileType failed: " + e + LogColor.RESET, e);
			return false;
		}
	}
}