package ov.pages.passport;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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


	//	*****************Objects and Variables****************************************************************

	private final List<String> addedAQQuestions = new ArrayList<>();

	private String lastInvitedEmail;





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

	//Save button:
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


	// AQ required message under the open question form
	private static final By AQ_REQUIRED_MSG_OPEN_FORM =
			By.xpath(AQ_OPEN_FORM + "//span[contains(@class,'input-error-message-container')][1]");

	// Delete icon for the currently open Additional Question (trash button)
	private static final By AQ_DELETE_ICON_BTN =
			By.cssSelector("button.circle-button.warn.hideShadow");

	// popup container (best proof)
	private static final By AQ_DELETE_POPUP =
			By.cssSelector("div.form-build-modal-question-v2");

	// Anchor the delete popup by its title text (stable)
	private static final String DELETE_POPUP_XP =
			"//*[contains(@class,'form-build-modal-question-v2') and .//div[normalize-space()='Delete Question']]";

	private static final By DELETE_POPUP = By.xpath(DELETE_POPUP_XP);

	//Delete AQ PopUp Buttons:
	private static final String AQ_Delete_popUp_buttons = "//button[text()='%s']";



	// Anchor: the question input inside Additional Questions editor (the one currently visible)
	private static final By AQ_QUESTION_INPUT_OPEN =
			By.xpath(AQ_CATEGORY + "//input[@placeholder='Write Question Here'][1]");

	// Save button relative to that question input (go to nearest QuestionFooter then find Save)
	private static final String AQ_SAVE_BTN_RELATIVE_TO_INPUT =
			"./ancestor::main[contains(@class,'FormBuilderQuestionV2') or contains(@class,'FormBuilderQuestion')][1]"
					+ "//button[contains(@class,'question-save-action-btn') and (@aria-label='Save' or normalize-space()='Save')][1]";


	// Builder footer Save & Continue (NOT preview applicant one)
	private static final By BUILDER_SAVE_AND_CONTINUE_BTN =
			By.cssSelector("footer.create-application-footer button[aria-label='Save & Continue']");


	// ===================== PUBLISH (Dates) =====================
	private static final By PUBLISH_OPEN_DATETIME = By.id("date-time-start");
	private static final By PUBLISH_CLOSE_DATETIME = By.id("date-time-end");


	// ===================== PUBLISH (Copy Link) =====================
	private static final By PUBLISH_COPY_APP_LINK_BTN =
			By.cssSelector("button.copy-link-button[aria-label='Copy Application Link']");

	// After click: shows Link Copied (disabled + copy-msg)
	private static final By PUBLISH_LINK_COPIED_STATE =
			By.cssSelector("button.copy-link-button.copy-msg[aria-label='Copy Application Link'][disabled]");


	// ===================== PUBLISH (Invite Applicants) =====================
	private static final By PUBLISH_INVITE_EMAIL_INPUT = By.id("email-add");

	// enabled Add button (after valid email typed)
	private static final By PUBLISH_ADD_EMAIL_BTN =
			By.cssSelector("button.add-email-btn");

	// Invite table container (where emails appear)
	private static final String INVITE_TABLE_XP = "//div[contains(@class,'invite-table-container')]";

	// Find the email row by email text
	private By inviteEmailCellBy(String email) {
		return By.xpath(INVITE_TABLE_XP
				+ "//span[contains(@class,'td-value') and normalize-space()=" + xpathLiteral(email) + "]");
	}

	// Trash icon inside the same row
	// Delete (trash) clickable span inside same row
	private By inviteDeleteIconBy(String email) {
		return By.xpath(INVITE_TABLE_XP
				+ "//span[contains(@class,'td-value') and normalize-space()=" + xpathLiteral(email) + "]"
				+ "/ancestor::tr[1]//span[contains(@class,'table-action-remove')]");
	}


	// ===================== PREVIEW (Applicant view) =====================

	// Preview button on Builder (top right)
	private static final By BUILDER_PREVIEW_APPLICATION_BTN =
			By.xpath("//button/span[text()='Preview Application']");

	// Back to Application button on Preview page (top right)
	private static final By PREVIEW_BACK_TO_APPLICATION_BTN =
			By.xpath("//button/span[text()='Back to Application']");;


			// Preview container root (very important: scopes applicant buttons)
			private static final String PREVIEW_ROOT_XP = "//div[contains(@class,'ApplicantFormOV')]";
			private static final By PREVIEW_ROOT = By.xpath(PREVIEW_ROOT_XP);

			// Tabs (active state proof)
			private static final By PREVIEW_TAB_STANDARD_ACTIVE =
					By.xpath(PREVIEW_ROOT_XP
							+ "//span[contains(@class,'tab-text') and normalize-space()='Standard Questions']"
							+ "/parent::*[contains(concat(' ', normalize-space(@class), ' '), ' active ')]");

			private static final By PREVIEW_TAB_ADDITIONAL_ACTIVE =
					By.xpath(PREVIEW_ROOT_XP
							+ "//span[contains(@class,'tab-text') and normalize-space()='Additional Questions']"
							+ "/parent::*[contains(concat(' ', normalize-space(@class), ' '), ' active ')]");

			// Applicant (Preview) Save & Continue button (inside ApplicantFormOV)
			private static final By PREVIEW_APPLICANT_SAVE_AND_CONTINUE_BTN =
					By.cssSelector("div.ApplicantFormOV button.save-form-btn[aria-label='Save & Continue']");

			// Preview AQ footer buttons (inside ApplicantFormOV)
			private static final By PREVIEW_APPLICANT_BACK_BTN =
					By.cssSelector("div.ApplicantFormOV button.save-form-btn[aria-label='Back']");

			// Submit is DISABLED until required fields are filled, so DO NOT use waitForElement() on it.
			// We locate it by visible text (most reliable) inside ApplicantFormOV.
			private static final By PREVIEW_APPLICANT_SUBMIT_BTN =
					By.xpath(PREVIEW_ROOT_XP + "//button[contains(@class,'save-form-btn') and normalize-space()='Submit']");


			// ===================== PUBLISH (Complete) =====================
			private static final By PUBLISH_COMPLETE_BTN =
					By.xpath("//button[normalize-space()='Complete' or @aria-label='Complete']");


			// ===================== RUBRIC (verified from DOM snippets A-F) =====================

			private static final By RUBRIC_HELP_TEXT =
					By.xpath("//span[contains(@class,'header-sublabel-text') and normalize-space(.)="
							+ "'Customize the criteria your colleagues will use to judge startups.']");

			private static final By RUBRIC_RATING_QUESTION_TEXT =
					By.xpath("//div[contains(@class,'sublabel') and contains(normalize-space(.),'How would you rate this application')][1]");

			// star widget
			private static final By RUBRIC_STAR_BTNS =
					By.cssSelector("div.form-view-star-rating-container span.star-btn.sm");

			private static final By RUBRIC_SELECTED_STARS =
					By.cssSelector("div.form-view-star-rating-container span.selected-star");

			private static final By RUBRIC_UNSELECTED_STARS =
					By.cssSelector("div.form-view-star-rating-container span.unselected-star");

			// comment box
			private static final By RUBRIC_COMMENT_TEXTAREA =
					By.cssSelector("textarea.review-field-input[placeholder='Leaving a rating']");

			// Add New Question button (anchored after the rating widget/comment area)
			private static final By RUBRIC_ADD_NEW_QUESTION_BTN =
					By.xpath("//textarea[@placeholder='Leaving a rating']" +
							"/ancestor::div[contains(@class,'form-view-star-rating-container')][1]" +
							"/following::button[contains(@class,'section-add-question-btn')][1]");



			//	***************************************************************************************************************
			//	Main Actions Methods to call from Step Definitions:
			//	***************************************************************************************************************

			public String passFieldValue(String fieldValue, String fieldName) {
				try {
					logger.info("[passFieldValue] START fieldName='" + fieldName + "' value='" + fieldValue + "'");

					if (fieldValue == null) fieldValue = "";

					String formattedXpath = String.format(CREATE_APP_FIELDS, fieldName);
					logger.info("[passFieldValue] Field XPath=" + formattedXpath);

					WebElement field = waitForElement(By.xpath(formattedXpath));
					if (field == null) {
						logger.warn("[passFieldValue] Field NOT found for: " + fieldName);
						return "null";
					}

					logger.info("[passFieldValue] Clicking field...");
					clickAndDraw(field);

					logger.info("[passFieldValue] Typing value...");
					safeSendKeys(field, fieldValue);

					String actual = field.getAttribute("value");
					logger.info("[passFieldValue] END actualValue='" + actual + "'");
					return actual;

				} catch (Exception e) {
					logger.error("[passFieldValue] EXCEPTION", e);
					return "null";
				}
			}

			//	***************************************************************************************************************//	***************************************************************************************************************
			public boolean selectOption(String optionText) {
				try {
					logger.info("[selectOption] START optionText='" + optionText + "'");

					if (!"Create New Application".equalsIgnoreCase(optionText)) {
						logger.warn("[selectOption] Unsupported optionText: " + optionText);
						return false;
					}

					WebElement card = waitForElement(CREATE_NEW_APP_CARD);
					if (card == null) {
						logger.warn("[selectOption] Create New Application card NOT found.");
						return false;
					}

					logger.info("[selectOption] Clicking option card...");
					clickAndDraw(card);

					WebElement radio = waitForElement(CREATE_NEW_APP_RADIO);
					if (radio == null) {
						logger.warn("[selectOption] Radio NOT found.");
						return false;
					}

					if (!radio.isSelected()) {
						logger.info("[selectOption] Radio not selected. Clicking radio...");
						clickAndDraw(radio);
					}

					boolean selected = radio.isSelected();
					logger.info("[selectOption] END selected=" + selected);
					return selected;

				} catch (Exception e) {
					logger.error("[selectOption] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean isOnPage(String textElement) {
				try {
					logger.info("[isOnPage] START textElement='" + textElement + "'");

					String formattedXpath = String.format(page_TEXT, textElement);
					By textBy = By.xpath(formattedXpath);

					logger.info("[isOnPage] Waiting for page proof text...");
					waitForElement(textBy);

					boolean textOk = isElementPresent(textBy);
					String url = driver.getCurrentUrl();
					boolean urlOk = url != null && url.contains(BUILDER_URL_MUST_CONTAIN);

					logger.info("[isOnPage] url='" + url + "'");
					logger.info("[isOnPage] urlOk=" + urlOk + " textOk=" + textOk);

					boolean result = urlOk && textOk;
					logger.info("[isOnPage] END result=" + result);
					return result;

				} catch (Exception e) {
					logger.error("[isOnPage] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean clickCollapseIcon() {
				try {
					logger.info("[clickCollapseIcon] START");

					WebElement icon = waitForElement(COLLAPSE_BTN);
					if (icon == null) {
						logger.warn("[clickCollapseIcon] Collapse icon NOT found.");
						return false;
					}

					logger.info("[clickCollapseIcon] Clicking collapse icon...");
					clickAndDraw(icon);

					logger.info("[clickCollapseIcon] END success=true");
					return true;

				} catch (Exception e) {
					logger.error("[clickCollapseIcon] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean standardQuestionsCollapsed() {
				try {
					logger.info("[standardQuestionsCollapsed] START");

					By expanded = By.xpath(String.format(EXPANDED_CONTENT_IN_CATEGORY, "Standard Questions"));

					long end = System.currentTimeMillis() + 3000;
					boolean expandedVisible = isElementPresent(expanded);
					logger.info("[standardQuestionsCollapsed] expandedVisible(initial)=" + expandedVisible);

					while (expandedVisible && System.currentTimeMillis() < end) {
						waitForMlsec(200);
						expandedVisible = isElementPresent(expanded);
					}

					boolean collapsed = !expandedVisible;
					logger.info("[standardQuestionsCollapsed] END collapsed=" + collapsed);
					return collapsed;

				} catch (Exception e) {
					logger.error("[standardQuestionsCollapsed] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean scrollToSection(String sectionName) {
				try {
					logger.info("[scrollToSection] START sectionName='" + sectionName + "'");

					By header = By.xpath("//*[normalize-space()='" + sectionName + "']");
					WebElement headerEl = waitForElement(header);
					if (headerEl == null) {
						logger.warn("[scrollToSection] Section header NOT found: " + sectionName);
						return false;
					}

					logger.info("[scrollToSection] Scrolling to section header...");
					scrollScreen(headerEl);

					logger.info("[scrollToSection] END success=true");
					return true;

				} catch (Exception e) {
					logger.error("[scrollToSection] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean clickAddNewQuestion(String buttonText) {
				try {
					logger.info("[clickAddNewQuestion] START buttonText='" + buttonText + "'");

					By btn = By.xpath(String.format(ADD_NEW_QUESTION_BTN_IN_CATEGORY, "Additional Questions"));
					WebElement btnEl = waitForElement(btn);

					if (btnEl == null) {
						logger.warn("[clickAddNewQuestion] Add New Question button NOT found in Additional Questions section.");
						return false;
					}

					logger.info("[clickAddNewQuestion] Scrolling and clicking Add New Question...");
					scrollScreen(btnEl);
					clickAndDraw(btnEl);

					logger.info("[clickAddNewQuestion] END success=true");
					return true;

				} catch (Exception e) {
					logger.error("[clickAddNewQuestion] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean newQuestionFormVisible() {
				try {
					logger.info("[newQuestionFormVisible] START");

					By input = By.xpath(String.format(NEW_QUESTION_INPUT_IN_CATEGORY, "Additional Questions"));
					boolean visible = isElementPresent(input);

					logger.info("[newQuestionFormVisible] END visible=" + visible);
					return visible;

				} catch (Exception e) {
					logger.error("[newQuestionFormVisible] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public int addAdditionalQuestions(List<Map<String, String>> rows) {
				int success = 0;

				try {
					logger.info("[addAdditionalQuestions] START rowsCount=" + (rows == null ? 0 : rows.size()));

					if (rows == null || rows.isEmpty()) {
						logger.warn("[addAdditionalQuestions] DataTable rows are empty.");
						return 0;
					}

					for (int i = 0; i < rows.size(); i++) {
						Map<String, String> row = rows.get(i);

						String type = row.get("Type");
						String question = row.get("Question");
						String instruction = row.get("Instruction");
						String requiredStr = row.getOrDefault("Required", "No");
						boolean required = "Yes".equalsIgnoreCase(requiredStr) || "True".equalsIgnoreCase(requiredStr);

						logger.info("[addAdditionalQuestions] Row " + (i + 1) + "/" + rows.size()
						+ " | type='" + type + "' | required=" + required
						+ " | question='" + question + "'");

						boolean added = addOneAdditionalQuestion(type, question, instruction, required);

						if (added) {
							success++;
							addedAQQuestions.add(question == null ? "" : question.trim());

							logger.info("[addAdditionalQuestions] Added OK. successCount=" + success);

							if (i < rows.size() - 1) {
								logger.info("[addAdditionalQuestions] Opening new Additional Question form...");
								boolean opened = openNewAdditionalQuestionForm();
								if (!opened) {
									logger.warn("[addAdditionalQuestions] Could not open next question form. Breaking.");
									break;
								}
							}
						} else {
							logger.warn("[addAdditionalQuestions] Failed to add row " + (i + 1) + ". Breaking.");
							break;
						}
					}

					logger.info("[addAdditionalQuestions] END successCount=" + success);
					return success;

				} catch (Exception e) {
					logger.error("[addAdditionalQuestions] EXCEPTION", e);
					return success;
				}
			}

			//	***************************************************************************************************************
			public boolean clickBuilderSaveAndContinue() {
				try {
					logger.info("[clickBuilderSaveAndContinue] START");

					WebElement btn = waitForElement(BUILDER_SAVE_AND_CONTINUE_BTN);
					if (btn == null) {
						logger.warn("[clickBuilderSaveAndContinue] Save & Continue button NOT found.");
						return false;
					}

					logger.info("[clickBuilderSaveAndContinue] Clicking Save & Continue...");
					clickAndDraw(btn);
					waitForPageAndAjaxToLoad();

					logger.info("[clickBuilderSaveAndContinue] END success=true");
					return true;

				} catch (Exception e) {
					logger.error("[clickBuilderSaveAndContinue] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean clickPreviewApplicationOnBuilder() {
				try {
					logger.info("[clickPreviewApplicationOnBuilder] START");

					logger.info("[clickPreviewApplicationOnBuilder] Clicking Preview Application button. Locator=" + BUILDER_PREVIEW_APPLICATION_BTN);
					clickAndDrawBy(BUILDER_PREVIEW_APPLICATION_BTN);
					waitForPageAndAjaxToLoad();

					logger.info("[clickPreviewApplicationOnBuilder] Waiting for Back to Application button as proof. Locator=" + PREVIEW_BACK_TO_APPLICATION_BTN);
					WebElement backBtn = waitForElement(PREVIEW_BACK_TO_APPLICATION_BTN);

					boolean ok = backBtn != null;
					logger.info("[clickPreviewApplicationOnBuilder] END success=" + ok);
					return ok;

				} catch (Exception e) {
					logger.error("[clickPreviewApplicationOnBuilder] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean isOnPreviewApplicationPage() {
				try {
					logger.info("[isOnPreviewApplicationPage] START");

					logger.info("[isOnPreviewApplicationPage] Checking Back to Application button...");
					if (waitForElement(PREVIEW_BACK_TO_APPLICATION_BTN) == null) {
						logger.warn("[isOnPreviewApplicationPage] Back to Application NOT found.");
						return false;
					}

					logger.info("[isOnPreviewApplicationPage] Checking preview root ApplicantFormOV...");
					if (waitForElement(PREVIEW_ROOT) == null) {
						logger.warn("[isOnPreviewApplicationPage] Preview root (ApplicantFormOV) NOT found.");
						return false;
					}

					logger.info("[isOnPreviewApplicationPage] END success=true");
					return true;

				} catch (Exception e) {
					logger.error("[isOnPreviewApplicationPage] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean isStandardQuestionsTabActiveOnPreview() {
				try {
					logger.info("[isStandardQuestionsTabActiveOnPreview] START");

					WebElement sqActive = waitForElement(PREVIEW_TAB_STANDARD_ACTIVE);
					boolean ok = sqActive != null;

					logger.info("[isStandardQuestionsTabActiveOnPreview] END active=" + ok);
					return ok;

				} catch (Exception e) {
					logger.error("[isStandardQuestionsTabActiveOnPreview] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean applicantClickSaveAndContinueOnPreview() {
				try {
					logger.info("[applicantClickSaveAndContinueOnPreview] START");

					WebElement btn = waitForElement(PREVIEW_APPLICANT_SAVE_AND_CONTINUE_BTN);
					if (btn == null) {
						logger.warn("[applicantClickSaveAndContinueOnPreview] Save & Continue button NOT found. Locator=" + PREVIEW_APPLICANT_SAVE_AND_CONTINUE_BTN);
						return false;
					}

					logger.info("[applicantClickSaveAndContinueOnPreview] Clicking applicant Save & Continue...");
					clickAndDraw(btn);
					waitForPageAndAjaxToLoad();

					logger.info("[applicantClickSaveAndContinueOnPreview] Waiting for Additional Questions tab to become active...");
					WebElement aqActive = waitForElement(PREVIEW_TAB_ADDITIONAL_ACTIVE);

					boolean ok = aqActive != null;
					logger.info("[applicantClickSaveAndContinueOnPreview] END success=" + ok);
					return ok;

				} catch (Exception e) {
					logger.error("[applicantClickSaveAndContinueOnPreview] EXCEPTION", e);
					return false;
				}
			}

			// ***************************************************************************************************************
			public boolean isAdditionalQuestionsTabActiveOnPreview() {
				try {
					logger.info("[isAdditionalQuestionsTabActiveOnPreview] START");

					WebElement aqActive = waitForElement(PREVIEW_TAB_ADDITIONAL_ACTIVE);
					boolean ok = aqActive != null;

					logger.info("[isAdditionalQuestionsTabActiveOnPreview] END active=" + ok);
					return ok;

				} catch (Exception e) {
					logger.error("[isAdditionalQuestionsTabActiveOnPreview] EXCEPTION", e);
					return false;
				}
			}

			// ***************************************************************************************************************
			public boolean areBackAndSubmitVisibleOnPreviewAQ() {
				try {
					logger.info("[areBackAndSubmitVisibleOnPreviewAQ] START");

					logger.info("[areBackAndSubmitVisibleOnPreviewAQ] Ensuring AQ tab is active...");
					if (waitForElement(PREVIEW_TAB_ADDITIONAL_ACTIVE) == null) {
						logger.warn("[areBackAndSubmitVisibleOnPreviewAQ] AQ tab not active / not found.");
						return false;
					}

					logger.info("[areBackAndSubmitVisibleOnPreviewAQ] Checking Back button presence...");
					boolean backVisible = waitUpToForVisible(PREVIEW_APPLICANT_BACK_BTN, 10);

					logger.info("[areBackAndSubmitVisibleOnPreviewAQ] Checking Submit button presence (may be disabled)...");
					boolean submitVisible = waitUpToForVisible(PREVIEW_APPLICANT_SUBMIT_BTN, 10);

					logger.info("[areBackAndSubmitVisibleOnPreviewAQ] END backVisible=" + backVisible + " submitVisible=" + submitVisible);
					return backVisible && submitVisible;

				} catch (Exception e) {
					logger.error("[areBackAndSubmitVisibleOnPreviewAQ] EXCEPTION", e);
					return false;
				}
			}

			// ***************************************************************************************************************
			public boolean areAllAddedAQVisibleOnPreview() {
				try {
					logger.info("[areAllAddedAQVisibleOnPreview] START - verifying added AQ questions on Preview");

					if (addedAQQuestions == null || addedAQQuestions.isEmpty()) {
						logger.warn("[areAllAddedAQVisibleOnPreview] No stored questions found in addedAQQuestions.");
						return false;
					}

					logger.info("[areAllAddedAQVisibleOnPreview] Stored questions count = " + addedAQQuestions.size());
					logger.info("[areAllAddedAQVisibleOnPreview] Questions = " + addedAQQuestions);

					if (waitForElement(PREVIEW_TAB_ADDITIONAL_ACTIVE) == null) {
						logger.warn("[areAllAddedAQVisibleOnPreview] Preview Additional Questions tab is NOT active/found.");
						return false;
					}
					logger.info("[areAllAddedAQVisibleOnPreview] Preview Additional Questions tab is active.");

					int missing = 0;
					List<String> missingQuestions = new ArrayList<>();

					for (int i = 0; i < addedAQQuestions.size(); i++) {
						String q = addedAQQuestions.get(i);

						if (q == null || q.isBlank()) {
							logger.warn("[areAllAddedAQVisibleOnPreview] Question[" + (i + 1) + "] is blank. Skipping.");
							continue;
						}

						logger.info("[areAllAddedAQVisibleOnPreview] Checking Question[" + (i + 1) + "]: " + q);

						By qBy = By.xpath(PREVIEW_ROOT_XP + "//*[contains(normalize-space(.), " + xpathLiteral(q) + ")]");
						boolean found = waitUpToForVisible(qBy, 10);

						if (found) {
							logger.info("[areAllAddedAQVisibleOnPreview] FOUND: " + q);
						} else {
							missing++;
							missingQuestions.add(q);
							logger.warn("[areAllAddedAQVisibleOnPreview] MISSING: " + q);
						}
					}

					if (missing == 0) {
						logger.info("[areAllAddedAQVisibleOnPreview] PASS - all questions are visible. Count=" + addedAQQuestions.size());
						return true;
					}

					logger.warn("[areAllAddedAQVisibleOnPreview] FAIL - missing count=" + missing + " | Missing questions=" + missingQuestions);
					return false;

				} catch (Exception e) {
					logger.error("[areAllAddedAQVisibleOnPreview] EXCEPTION", e);
					return false;
				}
			}

			// ***************************************************************************************************************
			public boolean clickBackToApplicationOnPreview() {
				try {
					logger.info("[clickBackToApplicationOnPreview] START");

					WebElement btn = waitForElement(PREVIEW_BACK_TO_APPLICATION_BTN);
					if (btn == null) {
						logger.warn("[clickBackToApplicationOnPreview] Back to Application button NOT found. Locator=" + PREVIEW_BACK_TO_APPLICATION_BTN);
						return false;
					}

					logger.info("[clickBackToApplicationOnPreview] Clicking Back to Application...");
					clickAndDraw(btn);
					waitForPageAndAjaxToLoad();

					logger.info("[clickBackToApplicationOnPreview] Verifying builder is visible again by checking Preview Application button...");
					boolean ok = waitForElement(BUILDER_PREVIEW_APPLICATION_BTN) != null;

					logger.info("[clickBackToApplicationOnPreview] END success=" + ok);
					return ok;

				} catch (Exception e) {
					logger.error("[clickBackToApplicationOnPreview] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			/*
			 * To set both Open Date=Now and Close Date=Now+5years this method can be used:
			 */

			//	public boolean setPublishOpenNowAndClosePlusOneMonth() {
			//		try {
			//			LocalDateTime now = LocalDateTime.now()
			//					.plusMinutes(2)       // small buffer to avoid min-time edge
			//					.withSecond(0).withNano(0);
			//
			//			LocalDateTime close = now.plusYears(5);
			//
			//			DateTimeFormatter fmt =
			//					DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			//
			//			waitForElement(PUBLISH_OPEN_DATETIME);
			//
			//			boolean openOk = jsSetDateTimeLocal(PUBLISH_OPEN_DATETIME, now.format(fmt));
			//
			//			waitForElement(PUBLISH_CLOSE_DATETIME);
			//
			//			boolean closeOk = jsSetDateTimeLocal(PUBLISH_CLOSE_DATETIME, close.format(fmt));
			//
			//			return openOk && closeOk;
			//
			//		} catch (Exception e) {
			//			logger.error(LogColor.RED + "setPublishOpenNowAndClosePlusOneMonth failed: " + e + LogColor.RESET, e);
			//			return false;
			//		}
			//	}

			//	***************************************************************************************************************
			public boolean setPublishOpenDateToNow() {
				try {
					logger.info("[setPublishOpenDateToNow] START");

					LocalDateTime open = LocalDateTime.now()
							.plusMinutes(2)
							.withSecond(0).withNano(0);

					DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
					String openVal = open.format(fmt);

					logger.info("[setPublishOpenDateToNow] Setting Open DateTimeLocal to: " + openVal);
					boolean openOk = jsSetDateTimeLocal(PUBLISH_OPEN_DATETIME, openVal);

					logger.info("[setPublishOpenDateToNow] END openOk=" + openOk);
					return openOk;

				} catch (Exception e) {
					logger.error("[setPublishOpenDateToNow] EXCEPTION", e);
					return false;
				}
			}


			//	***************************************************************************************************************
			public boolean copyApplicationLinkAndSeeCopied() {
				try {
					logger.info("[copyApplicationLinkAndSeeCopied] START");

					WebElement btn = waitForElement(PUBLISH_COPY_APP_LINK_BTN);
					if (btn == null) {
						logger.warn("[copyApplicationLinkAndSeeCopied] Copy button not found. Locator=" + PUBLISH_COPY_APP_LINK_BTN);
						return false;
					}

					logger.info("[copyApplicationLinkAndSeeCopied] Clicking Copy Application Link...");
					clickAndDraw(btn);
					waitForPageAndAjaxToLoad();

					logger.info("[copyApplicationLinkAndSeeCopied] Waiting for copied state (copy-msg + disabled)...");
					boolean copied = waitUpToForVisible(PUBLISH_LINK_COPIED_STATE, 5);

					if (!copied) {
						logger.info("[copyApplicationLinkAndSeeCopied] Primary copied-state not found. Trying fallback by text...");
						By copiedByText = By.xpath("//button[contains(@class,'copy-link-button') and normalize-space()='Link Copied']");
						copied = waitUpToForVisible(copiedByText, 5);
					}

					if (copied) {
						logger.info("[copyApplicationLinkAndSeeCopied] PASS - Link Copied displayed.");
						return true;
					}

					logger.warn("[copyApplicationLinkAndSeeCopied] FAIL - Link Copied not displayed.");
					return false;

				} catch (Exception e) {
					logger.error("[copyApplicationLinkAndSeeCopied] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public String addRandomInviteEmailAndVerifyListed() {
				try {
					String email = TestDataGenerator.generateTestEmail();
					logger.info("[addRandomInviteEmailAndVerifyListed] START - email generated: " + email);

					WebElement input = waitForElement(PUBLISH_INVITE_EMAIL_INPUT);
					if (input == null) {
						logger.warn("[addRandomInviteEmailAndVerifyListed] Email input NOT found. Locator=" + PUBLISH_INVITE_EMAIL_INPUT);
						return null;
					}

					logger.info("[addRandomInviteEmailAndVerifyListed] Typing email into input...");
					clickAndDraw(input);
					safeSendKeys(input, email);

					logger.info("[addRandomInviteEmailAndVerifyListed] Triggering validation using TAB...");
					input.sendKeys(Keys.TAB);
					waitForMlsec(200);

					logger.info("[addRandomInviteEmailAndVerifyListed] Clicking Add button. Locator=" + PUBLISH_ADD_EMAIL_BTN);
					clickAndDrawBy(PUBLISH_ADD_EMAIL_BTN);
					waitForPageAndAjaxToLoad();

					By addedEmail = By.xpath(
							INVITE_TABLE_XP
							+ "//span[contains(@class,'td-value') and normalize-space()=" + xpathLiteral(email) + "]"
							);

					logger.info("[addRandomInviteEmailAndVerifyListed] Verifying email appears in list...");
					boolean visible = waitUpToForVisible(addedEmail, 10);

					if (visible) {
						lastInvitedEmail = email;
						logger.info("[addRandomInviteEmailAndVerifyListed] PASS - email listed: " + email);
						return email;
					}

					logger.warn("[addRandomInviteEmailAndVerifyListed] FAIL - email not found in list: " + email);
					return null;

				} catch (Exception e) {
					logger.error("[addRandomInviteEmailAndVerifyListed] EXCEPTION", e);
					return null;
				}
			}

			//	***************************************************************************************************************
			public boolean deleteLastInvitedEmailAndVerifyRemoved() {
				try {
					logger.info("[deleteLastInvitedEmailAndVerifyRemoved] START");

					if (lastInvitedEmail == null || lastInvitedEmail.isBlank()) {
						logger.warn("[deleteLastInvitedEmailAndVerifyRemoved] lastInvitedEmail is blank. Add email first.");
						return false;
					}

					logger.info("[deleteLastInvitedEmailAndVerifyRemoved] Target email to delete: " + lastInvitedEmail);

					By cellBy = inviteEmailCellBy(lastInvitedEmail);

					logger.info("[deleteLastInvitedEmailAndVerifyRemoved] Confirming email exists before delete...");
					if (!waitUpToForVisible(cellBy, 5)) {
						logger.warn("[deleteLastInvitedEmailAndVerifyRemoved] Email not present in list: " + lastInvitedEmail);
						return false;
					}

					By trashBy = inviteDeleteIconBy(lastInvitedEmail);
					logger.info("[deleteLastInvitedEmailAndVerifyRemoved] Delete locator: " + trashBy);

					try {
						logger.info("[deleteLastInvitedEmailAndVerifyRemoved] Hovering over row...");
						hoverOver(cellBy);
					} catch (Exception ignore) {}

					WebElement trash = waitForElement(trashBy);
					if (trash == null) {
						logger.warn("[deleteLastInvitedEmailAndVerifyRemoved] Delete element NOT found.");
						return false;
					}

					logger.info("[deleteLastInvitedEmailAndVerifyRemoved] Clicking delete (JS click)...");
					scrollScreen(trash);
					jsclick(driver, trash);
					waitForPageAndAjaxToLoad();

					logger.info("[deleteLastInvitedEmailAndVerifyRemoved] Verifying email disappears...");
					long end = System.currentTimeMillis() + 10000;
					while (System.currentTimeMillis() < end) {
						if (!isElementPresent(cellBy)) {
							logger.info("[deleteLastInvitedEmailAndVerifyRemoved] PASS - email removed: " + lastInvitedEmail);
							return true;
						}
						waitForMlsec(200);
					}

					logger.warn("[deleteLastInvitedEmailAndVerifyRemoved] FAIL - email still present after timeout: " + lastInvitedEmail);
					return false;

				} catch (Exception e) {
					logger.error("[deleteLastInvitedEmailAndVerifyRemoved] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean clickCompleteOnPublish() {
				try {
					String beforeUrl = driver.getCurrentUrl();
					logger.info("[clickCompleteOnPublish] START beforeUrl=" + beforeUrl);

					WebElement btn = waitForElement(PUBLISH_COMPLETE_BTN);
					if (btn == null) {
						logger.warn("[clickCompleteOnPublish] Complete button NOT found. Locator=" + PUBLISH_COMPLETE_BTN);
						return false;
					}

					logger.info("[clickCompleteOnPublish] Clicking Complete...");
					scrollScreen(btn);
					clickAndDraw(btn);

					waitForPageAndAjaxToLoad();

					String afterUrl = driver.getCurrentUrl();
					logger.info("[clickCompleteOnPublish] afterUrl=" + afterUrl);

					if (afterUrl != null && !afterUrl.toLowerCase().contains("editapplication")) {
						logger.info("[clickCompleteOnPublish] PASS - navigated away from editapplication.");
						return true;
					}

					boolean stillThere = isElementPresent(PUBLISH_COMPLETE_BTN);
					logger.info("[clickCompleteOnPublish] Complete still present? " + stillThere);

					boolean ok = !stillThere;
					logger.info("[clickCompleteOnPublish] END success=" + ok);
					return ok;

				} catch (Exception e) {
					logger.error("[clickCompleteOnPublish] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public String getAQQuestionRequiredMessageText() {
				try {
					logger.info("[getAQQuestionRequiredMessageText] START");

					if (!isElementPresent(AQ_REQUIRED_MSG_OPEN_FORM)) {
						logger.warn("[getAQQuestionRequiredMessageText] Required message not present.");
						return null;
					}

					WebElement msgEl = driver.findElement(AQ_REQUIRED_MSG_OPEN_FORM);
					String msg = msgEl.getText().trim();

					logger.info("[getAQQuestionRequiredMessageText] END msg='" + msg + "'");
					return msg;

				} catch (Exception e) {
					logger.error("[getAQQuestionRequiredMessageText] EXCEPTION", e);
					return null;
				}
			}

			//	***************************************************************************************************************
			public boolean isAQSaveButtonDisabled() {
				try {
					logger.info("[isAQSaveButtonDisabled] START");

					WebElement qInput = waitForElement(AQ_QUESTION_INPUT_OPEN);
					if (qInput == null) {
						logger.warn("[isAQSaveButtonDisabled] Question input not found, cannot scope Save button.");
						return false;
					}

					WebElement saveBtn = qInput.findElement(By.xpath(AQ_SAVE_BTN_RELATIVE_TO_INPUT));

					String disabledAttr = saveBtn.getAttribute("disabled");
					boolean enabledState = saveBtn.isEnabled();
					boolean disabled = (disabledAttr != null) || (!enabledState);

					logger.info("[isAQSaveButtonDisabled] SaveBtnFound=true | isEnabled=" + enabledState
							+ " | disabledAttr=" + disabledAttr + " | RESULT disabled=" + disabled);

					return disabled;

				} catch (Exception e) {
					logger.error("[isAQSaveButtonDisabled] EXCEPTION", e);
					return false;
				}
			} 

			//	***************************************************************************************************************
			public boolean clickDeleteIconForCurrentAdditionalQuestion() {
				try {
					logger.info("[clickDeleteIconForCurrentAdditionalQuestion] START");

					List<WebElement> buttons = driver.findElements(AQ_DELETE_ICON_BTN);
					logger.info("[clickDeleteIconForCurrentAdditionalQuestion] Found delete buttons count=" + buttons.size());

					WebElement target = null;
					for (WebElement b : buttons) {
						if (b.isDisplayed()) { target = b; break; }
					}

					if (target == null) {
						logger.warn("[clickDeleteIconForCurrentAdditionalQuestion] No displayed delete icon found.");
						return false;
					}

					scrollScreen(target);
					jsclick(driver, target);     // safest for overlay/hover containers
					waitForMlsec(300);           // small animation buffer

					boolean popup = isDeleteQuestionPopupVisible();
					logger.info("[clickDeleteIconForCurrentAdditionalQuestion] END popupVisible=" + popup);
					return popup;

				} catch (Exception e) {
					logger.error("[clickDeleteIconForCurrentAdditionalQuestion] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean isDeleteQuestionPopupVisible() {
				try {
					logger.info("[isDeleteQuestionPopupVisible] START");

					boolean visible = waitUpToForVisible(AQ_DELETE_POPUP, 5);

					logger.info("[isDeleteQuestionPopupVisible] END visible=" + visible);
					return visible;

				} catch (Exception e) {
					logger.error("[isDeleteQuestionPopupVisible] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			public boolean clickDeletePopupButton(String btnText) {
				try {
					logger.info("Formatting the X path of Button: "+btnText);
					String formattedXpath = String.format(AQ_Delete_popUp_buttons, btnText);
					logger.info("X path of Button: "+formattedXpath);

					By byLocator = By.xpath(formattedXpath);

					waitForElement(byLocator);

					logger.info("Looking for the Button: "+btnText);
					boolean visible = isElementPresent(byLocator);

					if(visible) {

						logger.info("Clicking on the Button: "+btnText);
						clickAndDrawBy(byLocator);
						logger.info("Clicked on the Button: "+btnText);

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
			// ===================== RUBRIC - BASIC VISIBILITY CHECKS =====================

			public boolean isRubricHelpTextVisible() {
				return waitUpToForVisible(RUBRIC_HELP_TEXT, 10);
			}

			public boolean isRubricRatingQuestionVisible() {
				return waitUpToForVisible(RUBRIC_RATING_QUESTION_TEXT, 10);
			}

			public boolean isRubricCommentBoxVisible() {
				return waitUpToForVisible(RUBRIC_COMMENT_TEXTAREA, 10);
			}

			//***************************************************************************************************************
			// ===================== RUBRIC - STAR RATING (creator verification) =====================

			public int waitForRubricStarCount(int seconds) {
				long end = System.currentTimeMillis() + (seconds * 1000L);
				int count = driver.findElements(RUBRIC_STAR_BTNS).size();

				while (count == 0 && System.currentTimeMillis() < end) {
					waitForMlsec(150);
					count = driver.findElements(RUBRIC_STAR_BTNS).size();
				}
				return count;
			}


			//			***************************************************************************************************************
			/**
			 * Click star #N (1..5) and verify the DOM updates:
			 * - selected-star count becomes N
			 * - total selected+unselected = 5
			 */
			public boolean clickRubricStarAndVerify(int starNumber) {
				try {
					if (starNumber < 1 || starNumber > 5) return false;

					// wait until stars exist
					int count = waitForRubricStarCount(5);
					if (count != 5) {
						logger.warn("[clickRubricStarAndVerify] Expected 5 stars but found: " + count);
						return false;
					}

					List<WebElement> stars = driver.findElements(RUBRIC_STAR_BTNS);
					WebElement target = stars.get(starNumber - 1);

					scrollScreen(target);
					clickAndDraw(target);

					long end = System.currentTimeMillis() + 3000;
					while (System.currentTimeMillis() < end) {
						int selected = driver.findElements(RUBRIC_SELECTED_STARS).size();
						int unselected = driver.findElements(RUBRIC_UNSELECTED_STARS).size();

						if (selected == starNumber && (selected + unselected) == 5) {
							return true;
						}
						waitForMlsec(150);
					}

					return false;

				} catch (Exception e) {
					logger.error("[clickRubricStarAndVerify] EXCEPTION", e);
					return false;
				}
			}

			//***************************************************************************************************************
			public boolean isRubricAddNewQuestionVisible() {
				return waitUpToForVisible(RUBRIC_ADD_NEW_QUESTION_BTN, 10);
			}

			public boolean clickRubricAddNewQuestion() {
				try {
					WebElement btn = waitForElement(RUBRIC_ADD_NEW_QUESTION_BTN);
					if (btn == null) return false;

					scrollScreen(btn);
					clickAndDraw(btn);
					return true;

				} catch (Exception e) {
					logger.error("[clickRubricAddNewQuestion] EXCEPTION", e);
					return false;
				}
			}




			//	***************************************************************************************************************
			//	Helper Methods
			//	***************************************************************************************************************
			private boolean openNewAdditionalQuestionForm() {
				try {
					logger.info("[openNewAdditionalQuestionForm] START");

					WebElement addBtn = waitForElement(AQ_ADD_NEW_QUESTION_BTN_ENABLED);
					if (addBtn == null) {
						logger.warn("[openNewAdditionalQuestionForm] Enabled 'Add New Question' button NOT found.");
						return false;
					}

					logger.info("[openNewAdditionalQuestionForm] Clicking enabled Add New Question...");
					scrollScreen(addBtn);
					clickAndDraw(addBtn);

					WebElement q = waitForElement(AQ_QUESTION_INPUT);
					boolean ok = (q != null);

					logger.info("[openNewAdditionalQuestionForm] END formOpened=" + ok);
					return ok;

				} catch (Exception e) {
					logger.error("[openNewAdditionalQuestionForm] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			private boolean addOneAdditionalQuestion(String type, String question, String instruction, boolean required) {
				try {
					logger.info("[addOneAdditionalQuestion] START type='" + type + "' required=" + required
							+ " question='" + question + "'");

					WebElement qInput = waitForElement(AQ_QUESTION_INPUT);
					if (qInput == null) {
						logger.warn("[addOneAdditionalQuestion] Question input NOT found.");
						return false;
					}

					logger.info("[addOneAdditionalQuestion] Setting question text...");
					scrollScreen(qInput);
					clickAndDraw(qInput);
					safeSendKeys(qInput, question);

					WebElement editor = waitForElement(AQ_INSTRUCTION_EDITOR);
					if (editor == null) {
						logger.warn("[addOneAdditionalQuestion] Instruction editor NOT found.");
						return false;
					}

					logger.info("[addOneAdditionalQuestion] Setting instruction...");
					clickAndDraw(editor);
					editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
					editor.sendKeys(Keys.BACK_SPACE);
					if (instruction != null && !instruction.isBlank()) editor.sendKeys(instruction);

					WebElement selectEl = waitForElement(AQ_ANSWER_TYPE_SELECT);
					if (selectEl == null) {
						logger.warn("[addOneAdditionalQuestion] Answer Type select NOT found.");
						return false;
					}

					logger.info("[addOneAdditionalQuestion] Selecting answer type='" + type + "'");
					new Select(selectEl).selectByVisibleText(type);

					if ("Single Choice".equalsIgnoreCase(type)) {
						logger.info("[addOneAdditionalQuestion] Adding Single Choice options...");
						if (!addChoiceOptions(Arrays.asList("Option 1", "Option 2"))) return false;
					}

					if ("Multiple Choice".equalsIgnoreCase(type)) {
						logger.info("[addOneAdditionalQuestion] Adding Multiple Choice options...");
						if (!addChoiceOptions(Arrays.asList("Option 1", "Option 2", "Option 3"))) return false;
					}

					if ("File".equalsIgnoreCase(type)) {
						logger.info("[addOneAdditionalQuestion] File type detected. Selecting file type PDF...");
						if (!selectFileType("PDF")) return false;
					}

					WebElement req = waitForElement(AQ_REQUIRED_CHECKBOX);
					if (req == null) {
						logger.warn("[addOneAdditionalQuestion] Required checkbox NOT found.");
						return false;
					}

					logger.info("[addOneAdditionalQuestion] Setting required checkbox to=" + required);
					if (required && !req.isSelected()) clickAndDraw(req);
					if (!required && req.isSelected()) clickAndDraw(req);

					WebElement saveBtn = waitForElement(AQ_SAVE_BUTTON);
					if (saveBtn == null) {
						logger.warn("[addOneAdditionalQuestion] Save button NOT found.");
						return false;
					}

					logger.info("[addOneAdditionalQuestion] Clicking Save...");
					clickAndDraw(saveBtn);

					WebElement addBtn = waitForElement(AQ_ADD_NEW_QUESTION_BTN_ENABLED);
					boolean ok = addBtn != null;

					logger.info("[addOneAdditionalQuestion] END saved=" + ok);
					return ok;

				} catch (Exception e) {
					logger.error("[addOneAdditionalQuestion] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			private boolean addChoiceOptions(List<String> options) {
				try {
					logger.info("[addChoiceOptions] START options=" + options);

					WebElement addBtn = waitForElement(AQ_ADD_OPTION_BTN);
					if (addBtn == null) {
						logger.warn("[addChoiceOptions] Add Option button NOT found.");
						return false;
					}

					// Display As is optional
					try {
						if (isElementPresent(AQ_DISPLAY_AS_CHECKBOX)) {
							logger.info("[addChoiceOptions] Display As: clicking Checkbox");
							clickAndDraw(waitForElement(AQ_DISPLAY_AS_CHECKBOX));
						} else if (isElementPresent(AQ_DISPLAY_AS_RADIO)) {
							logger.info("[addChoiceOptions] Display As: clicking Radio");
							clickAndDraw(waitForElement(AQ_DISPLAY_AS_RADIO));
						} else {
							logger.info("[addChoiceOptions] Display As not present (skipping).");
						}
					} catch (Exception ignore) {}

					for (String opt : options) {
						logger.info("[addChoiceOptions] Adding option='" + opt + "'");

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

					logger.info("[addChoiceOptions] END success=true");
					return true;

				} catch (Exception e) {
					logger.error("[addChoiceOptions] EXCEPTION", e);
					return false;
				}
			}

			//	***************************************************************************************************************
			private boolean selectFileType(String expectedType) {
				try {
					logger.info("[selectFileType] START expectedType='" + expectedType + "'");

					WebElement selectEl = waitForElement(AQ_FILE_TYPE_SELECT);
					if (selectEl == null) {
						logger.warn("[selectFileType] File type select NOT found.");
						return false;
					}

					Select sel = new Select(selectEl);

					for (WebElement opt : sel.getOptions()) {
						String text = opt.getText().trim();
						if (text.equalsIgnoreCase(expectedType) || text.toLowerCase().contains(expectedType.toLowerCase())) {
							logger.info("[selectFileType] Selecting option='" + text + "'");
							opt.click();
							logger.info("[selectFileType] END success=true");
							return true;
						}
					}

					logger.warn("[selectFileType] No matching option found for '" + expectedType + "'");
					return false;

				} catch (Exception e) {
					logger.error("[selectFileType] EXCEPTION", e);
					return false;
				}
			}

			// ***************************************************************************************************************
			private boolean waitUpToForVisible(By locator, int seconds) {
				long end = System.currentTimeMillis() + (seconds * 1000L);
				while (System.currentTimeMillis() < end) {
					if (isElementPresent(locator)) return true;   // your 2s visibility wait
					waitForMlsec(200);
				}
				return false;
			}

			// ***************************************************************************************************************
			private static String xpathLiteral(String s) {
				if (s == null) return "''";
				if (s.contains("'")) {
					String[] parts = s.split("'");
					StringBuilder sb = new StringBuilder("concat(");
					for (int i = 0; i < parts.length; i++) {
						sb.append("'").append(parts[i]).append("'");
						if (i < parts.length - 1) sb.append(",\"'\",");
					}
					sb.append(")");
					return sb.toString();
				}
				return "'" + s + "'";
			}

			//	***************************************************************************************************************
			private boolean jsSetDateTimeLocal(By inputBy, String value) {
				try {
					logger.info("[jsSetDateTimeLocal] START locator=" + inputBy + " value=" + value);

					WebElement el = waitForElement(inputBy);
					if (el == null) {
						logger.warn("[jsSetDateTimeLocal] Input NOT found. locator=" + inputBy);
						return false;
					}

					String script =
							"arguments[0].value = arguments[1];" +
									"arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
									"arguments[0].dispatchEvent(new Event('change', {bubbles:true}));";

					((JavascriptExecutor) driver).executeScript(script, el, value);

					String actual = el.getAttribute("value");
					boolean ok = actual != null && actual.startsWith(value);

					logger.info("[jsSetDateTimeLocal] actualValue='" + actual + "' | success=" + ok);
					logger.info("[jsSetDateTimeLocal] END");
					return ok;

				} catch (Exception e) {
					logger.error("[jsSetDateTimeLocal] EXCEPTION locator=" + inputBy + " value=" + value, e);
					return false;
				}
			}

			//	***************************************************************************************************************
			private WebElement getVisibleDeletePopup() {
				List<WebElement> popups = driver.findElements(AQ_DELETE_POPUP);
				for (WebElement p : popups) {
					if (p.isDisplayed()) return p;
				}
				return null;
			}

}