package ov.pages.passport;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ov.utilities.CommonMethods;
import ov.utilities.GeneratedSignupData;
import ov.utilities.LogColor;

public class StartupProgramApplication_POM extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(StartupProgramApplication_POM.class);

	private static final By SEARCH_INPUT = By.xpath("//input[@placeholder='Search']");
	private static final By SEARCH_ICON = By.xpath("//input[@placeholder='Search']/following::*[name()='svg'][1]/ancestor::*[self::button or self::span or self::div][1]");
	private static final By UPDATING_INDICATOR = By.xpath("//*[self::button or self::span or self::div][contains(normalize-space(.), 'Updating')]");
	private static final By WELCOME_TO_PASSPORT_MODAL = By.xpath("//*[contains(normalize-space(.), 'Welcome to Passport')]");

	private static final By PROGRAM_SEARCH_CARDS = By.cssSelector("div.ProgramSearchCard");
	private static final By PAGINATION_NEXT_ARROW = By.xpath(
			"//nav[contains(@class,'search-programs-pagination')]//button[@aria-label='Next page' and not(@disabled)]"
			);
	private static final By ACTIVE_PAGE_BUTTON = By.xpath(
			"//nav[contains(@class,'search-programs-pagination')]//button[contains(@class,'page-link') and contains(@class,'active')]"
			);
	private static final By APPLY_NOW_BUTTON = By.cssSelector("a.cohort-action-btn");
	private static final By FIRST_LOCATION_SUGGESTION = By.xpath("(//div[contains(@class,'pac-container')]//div[contains(@class,'pac-item')])[1]");
	private static final By DROPDOWN_OPTIONS = By.cssSelector("ul.dropdown-list > li");
	private static final By MULTISELECT_OPTIONS = By.cssSelector("div.multiselect__content-wrapper ul.multiselect__content > li.multiselect__element");
	private static final By FILE_UPLOAD_INPUT = By.xpath("//input[@type='file' and @aria-label='Pitch Deck']");
	private static final By SAVE_RESPONSES_CHECKBOX = By.xpath("//span[normalize-space()='Save my responses to use in future applications on Passport']/preceding::input[@type='checkbox'][1]");
	private static final By SUCCESS_MESSAGE = By.xpath("//*[normalize-space()='Thank you for submitting your application!']");
	private static final By BACK_TO_DASHBOARD_LINK = By.cssSelector("a.dashboard-redirect-btn");

	private static final String EXACT_TEXT = "//*[normalize-space()=%s]";
	private static final String INPUT_BY_LABEL = "//label[contains(normalize-space(.), '%s')]/following::input[1]";
	private static final String EDITOR_BY_LABEL = "//label[contains(normalize-space(.), '%s')]/following::div[contains(@class,'ql-editor') and @contenteditable='true'][1]";
	private static final String DROPDOWN_TRIGGER_BY_LABEL =
			"//label[contains(normalize-space(.), '%s')]/following::*[(self::div or self::input) and (contains(@class,'dropdown') or contains(@class,'select') or contains(@class,'searchBox') or @role='combobox')][1]";
	private static final String BUTTON_BY_TEXT = "//*[self::button or self::a][normalize-space()='%s']";

	public StartupProgramApplication_POM() {
		PageFactory.initElements(driver, this);
	}

	public boolean searchForProgram(String targetProgramTitle) {
		try {
			waitForOnboardingUpdateToFinish();

			WebElement searchInput = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT));
			if (searchInput == null) {
				return false;
			}

			clickAndDrawBy(SEARCH_INPUT);
			searchInput = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
			searchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			searchInput.sendKeys(Keys.BACK_SPACE);
			searchInput.sendKeys(targetProgramTitle);

			try {
				clickAndDrawBy(SEARCH_ICON);
			} catch (Exception e) {
				logger.warn("Search icon click failed. Falling back to ENTER.");
				searchInput.sendKeys(Keys.ENTER);
			}

			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			return true;

		} catch (Exception e) {
			logger.error("Failed to search for program title: " + targetProgramTitle, e);
			return false;
		}
	}

	private void waitForOnboardingUpdateToFinish() {
		try {
			logger.info("[waitForOnboardingUpdateToFinish] START");
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

			boolean updatingWasVisible = !driver.findElements(UPDATING_INDICATOR).isEmpty();
			boolean modalWasVisible = !driver.findElements(WELCOME_TO_PASSPORT_MODAL).isEmpty();
			logger.info("[waitForOnboardingUpdateToFinish] updatingWasVisible=" + updatingWasVisible
					+ " modalWasVisible=" + modalWasVisible);

			if (updatingWasVisible) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(UPDATING_INDICATOR));
				logger.info("[waitForOnboardingUpdateToFinish] Updating indicator disappeared.");
			}

			wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[waitForOnboardingUpdateToFinish] END");
		} catch (TimeoutException e) {
			logger.warn("[waitForOnboardingUpdateToFinish] Timed out waiting for onboarding update to finish. Proceeding with latest page state.", e);
		} catch (Exception e) {
			logger.warn("[waitForOnboardingUpdateToFinish] Unexpected issue while waiting for onboarding update to finish. Proceeding with latest page state.", e);
		}
	}

	public boolean openMatchingProgramDetails(String targetProgramTitle) {
		try {
			for (int page = 1; page <= 50; page++) {
				logger.info("Scanning startup program search results page: " + page);

				if (openMatchingCardOnCurrentPage(targetProgramTitle)) {
					return true;
				}

				if (!goToNextResultsPage()) {
					logger.warn("Reached the last results page without finding: " + targetProgramTitle);
					return false;
				}
			}

			return false;

		} catch (Exception e) {
			logger.error("Failed while trying to open matching program details for: " + targetProgramTitle, e);
			return false;
		}
	}

	public boolean clickApplyNow() {
		try {
			WebElement applyNow = waitForElement(APPLY_NOW_BUTTON);
			if (applyNow == null) {
				return false;
			}

			scrollScreen(applyNow);
			clickAndDraw(applyNow);
			return true;

		} catch (Exception e) {
			logger.error("Failed to click Apply Now.", e);
			return false;
		}
	}

	public boolean switchToNewApplicationTab(String originalWindowHandle) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(d -> d.getWindowHandles().size() > 1);

			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(originalWindowHandle)) {
					driver.switchTo().window(handle);
					waitForPageAndAjaxToLoad();
					waitForNetworkIdle();
					return true;
				}
			}

			return false;

		} catch (Exception e) {
			logger.error("Failed to switch to the newly opened application tab.", e);
			return false;
		}
	}

	public boolean verifyProgramTitleOnApplicationPage(String targetProgramTitle) {
		try {
			By titleBy = By.xpath(String.format(EXACT_TEXT, xpathLiteral(targetProgramTitle)));
			waitForElement(titleBy);
			return isElementPresent(titleBy);

		} catch (Exception e) {
			logger.error("Failed to verify application page title: " + targetProgramTitle, e);
			return false;
		}
	}

	public boolean fillApplicationForm(String locationSearchValue, String websiteSuffix, String linkedInSuffix) {
		try {
			String generatedEmail = GeneratedSignupData.getEmail();
			String emailLocalPart = GeneratedSignupData.getEmailLocalPart();

			if (generatedEmail == null || emailLocalPart == null) {
				logger.error("Generated signup email data is missing.");
				return false;
			}

			logger.info("========== Startup application form fill started ==========");
			logger.info("Generated email for reuse: " + generatedEmail);
			logger.info("Email local part for short fields: " + emailLocalPart);

			boolean ok = true;

			ok &= logStepResult("Job Title", enterInputValue("Job Title", emailLocalPart));
			ok &= logStepResult("Startup Location", enterLocation("Startup Location", locationSearchValue));
			ok &= logStepResult("Industries", selectRandomDropdownOption("Industries"));
			ok &= logStepResult("Website", appendToPrefilledUrlField("Website", websiteSuffix));
			ok &= logStepResult("LinkedIn", appendToPrefilledUrlField("LinkedIn", linkedInSuffix));
			ok &= logStepResult("Company Description", enterEditorValue("Company Description", generatedEmail));
			ok &= logStepResult("Year of Founding", enterInputValue("Year of Founding", String.valueOf(randInt(1, 5))));
			ok &= logStepResult("Funding", selectRandomDropdownOption("Funding"));
			ok &= logStepResult("Number of full-time employees", enterInputValue("Number of full-time employees", String.valueOf(randInt(1, 5))));
			ok &= logStepResult("Milestones", enterEditorValue("Milestones", generatedEmail));

			logger.info("========== Startup application form fill finished. overallSuccess=" + ok + " ==========");
			return ok;

		} catch (Exception e) {
			logger.error("Failed to fill startup program application form.", e);
			return false;
		}
	}

	public boolean uploadPitchDeck() {
		try {
			logger.info("[uploadPitchDeck] START");

			List<WebElement> fileInputs = driver.findElements(FILE_UPLOAD_INPUT);
			logger.info("[uploadPitchDeck] Matching file inputs found: " + fileInputs.size());

			if (fileInputs.isEmpty()) {
				logger.warn("[uploadPitchDeck] Pitch Deck file input not found.");
				return false;
			}

			WebElement fileInput = fileInputs.get(0);

			String absolutePath = new File(System.getProperty("user.dir")
					+ File.separator + "src"
					+ File.separator + "test"
					+ File.separator + "resources"
					+ File.separator + "test-data"
					+ File.separator + "pitchdeck.jpg").getAbsolutePath();

			logger.info("[uploadPitchDeck] Absolute file path: " + absolutePath);
			logger.info("[uploadPitchDeck] File input displayed=" + fileInput.isDisplayed() + " enabled=" + fileInput.isEnabled());

			((JavascriptExecutor) driver).executeScript(
					"arguments[0].style.display='block'; arguments[0].style.visibility='visible'; arguments[0].style.opacity=1;",
					fileInput
					);
			fileInput.sendKeys(absolutePath);
			waitForMlsec(500);

			boolean uploaded = driver.getPageSource().contains("pitchdeck.jpg");
			logger.info("[uploadPitchDeck] END uploaded=" + uploaded);
			return uploaded;

		} catch (Exception e) {
			logger.error("[uploadPitchDeck] Failed to upload pitch deck file.", e);
			return false;
		}
	}

	public boolean checkSaveResponsesCheckbox() {
		try {
			WebElement checkbox = waitForElement(SAVE_RESPONSES_CHECKBOX);
			if (checkbox == null) {
				return false;
			}

			if (!checkbox.isSelected()) {
				clickAndDraw(checkbox);
			}

			return checkbox.isSelected();

		} catch (Exception e) {
			logger.error("Failed to check save responses checkbox.", e);
			return false;
		}
	}

	public boolean clickButton(String buttonText) {
		try {
			logger.info("[clickButton] START buttonText='" + buttonText + "'");
			By buttonBy = resolveButtonLocator(buttonText);
			logger.info("[clickButton] Resolved locator for '" + buttonText + "' => " + buttonBy);
			if (!isElementPresent(buttonBy)) {
				logger.warn("[clickButton] No clickable button/link found for text: " + buttonText);
				return false;
			}
			WebElement button = waitForElement(buttonBy);
			if (button == null) {
				logger.warn("[clickButton] Element became unavailable for text: " + buttonText);
				return false;
			}
			scrollScreen(button);
			clickAndDraw(button);
			logger.info("[clickButton] END buttonText='" + buttonText + "' clicked=true");
			return true;

		} catch (Exception e) {
			logger.error("[clickButton] Failed to click button/link: " + buttonText, e);
			return false;
		}
	}

	private By resolveButtonLocator(String buttonText) {
		if ("Back to Dashboard".equalsIgnoreCase(buttonText)) {
			return BACK_TO_DASHBOARD_LINK;
		}
		return By.xpath(String.format(BUTTON_BY_TEXT, buttonText));
	}

	public boolean successMessageVisible() {
		try {
			waitForElement(SUCCESS_MESSAGE);
			return isElementPresent(SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error("Failed to verify success message.", e);
			return false;
		}
	}

	private boolean enterInputValue(String fieldLabel, String value) {
		try {
			logger.info("[enterInputValue] START fieldLabel='" + fieldLabel + "' value='" + value + "'");
			closeOpenDropdownIfPresent();

			By fieldBy = By.xpath(String.format(INPUT_BY_LABEL, fieldLabel));
			WebElement input = waitForElement(fieldBy);
			if (input == null) {
				logger.warn("[enterInputValue] Field not found for label: " + fieldLabel);
				return false;
			}

			clickAndDraw(input);
			input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			input.sendKeys(Keys.BACK_SPACE);
			input.sendKeys(value);

			String actual = input.getAttribute("value");
			boolean matched = value.equals(actual);
			logger.info("[enterInputValue] END fieldLabel='" + fieldLabel + "' actual='" + actual + "' matched=" + matched);
			return matched;

		} catch (Exception e) {
			logger.error("[enterInputValue] Failed for field: " + fieldLabel, e);
			return false;
		}
	}

	private boolean appendToPrefilledUrlField(String fieldLabel, String suffixValue) {
		try {
			logger.info("[appendToPrefilledUrlField] START fieldLabel='" + fieldLabel + "' suffixValue='" + suffixValue + "'");
			closeOpenDropdownIfPresent();

			By fieldBy = By.xpath(String.format(INPUT_BY_LABEL, fieldLabel));
			WebElement input = waitForElement(fieldBy);
			if (input == null) {
				logger.warn("[appendToPrefilledUrlField] Field not found for label: " + fieldLabel);
				return false;
			}

			clickAndDraw(input);
			String currentValue = input.getAttribute("value");
			if (currentValue == null) currentValue = "";

			input.sendKeys(Keys.END);
			input.sendKeys(suffixValue);

			String expected = currentValue + suffixValue;
			String actual = input.getAttribute("value");
			boolean matched = expected.equals(actual);
			logger.info("[appendToPrefilledUrlField] END fieldLabel='" + fieldLabel + "' before='" + currentValue + "' actual='" + actual + "' matched=" + matched);
			return matched;

		} catch (Exception e) {
			logger.error("[appendToPrefilledUrlField] Failed for field: " + fieldLabel, e);
			return false;
		}
	}

	private boolean enterEditorValue(String fieldLabel, String value) {
		try {
			logger.info("[enterEditorValue] START fieldLabel='" + fieldLabel + "' value='" + value + "'");
			closeOpenDropdownIfPresent();

			By editorBy = By.xpath(String.format(EDITOR_BY_LABEL, fieldLabel));
			WebElement editor = waitForElement(editorBy);
			if (editor == null) {
				logger.warn("[enterEditorValue] Editor not found for label: " + fieldLabel);
				return false;
			}

			clickAndDraw(editor);
			editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			editor.sendKeys(Keys.BACK_SPACE);
			editor.sendKeys(value);

			String actual = editor.getText();
			boolean matched = actual != null && actual.contains(value);
			logger.info("[enterEditorValue] END fieldLabel='" + fieldLabel + "' actual='" + actual + "' matched=" + matched);
			return matched;

		} catch (Exception e) {
			logger.error("[enterEditorValue] Failed for field: " + fieldLabel, e);
			return false;
		}
	}

	private boolean enterLocation(String fieldLabel, String searchText) {
		try {
			logger.info("[enterLocation] START fieldLabel='" + fieldLabel + "' searchText='" + searchText + "'");
			closeOpenDropdownIfPresent();

			By fieldBy = By.xpath(String.format(INPUT_BY_LABEL, fieldLabel));
			WebElement locationInput = waitForElement(fieldBy);
			if (locationInput == null) {
				logger.warn("[enterLocation] Location field not found for label: " + fieldLabel);
				return false;
			}

			clickAndDraw(locationInput);
			safeSendKeys(locationInput, searchText);

			WebElement suggestion = waitForElement(FIRST_LOCATION_SUGGESTION);
			if (suggestion == null) {
				return false;
			}

			clickAndDraw(suggestion);
			String actual = locationInput.getAttribute("value");
			boolean matched = actual != null && actual.contains("San");
			logger.info("[enterLocation] END actual='" + actual + "' matched=" + matched);
			return matched;

		} catch (Exception e) {
			logger.error("[enterLocation] Failed.", e);
			return false;
		}
	}

	private boolean selectRandomDropdownOption(String fieldLabel) {
		try {
			logger.info("[selectRandomDropdownOption] START fieldLabel='" + fieldLabel + "'");

			By triggerBy = By.xpath(String.format(DROPDOWN_TRIGGER_BY_LABEL, fieldLabel));
			WebElement trigger = waitForElement(triggerBy);
			if (trigger == null) {
				logger.warn("[selectRandomDropdownOption] Trigger not found for field: " + fieldLabel);
				return false;
			}

			scrollScreen(trigger);
			clickAndDraw(trigger);
			waitForMlsec(300);

			List<WebElement> rawItems = driver.findElements(MULTISELECT_OPTIONS);
			if (rawItems.isEmpty()) {
				rawItems = driver.findElements(DROPDOWN_OPTIONS);
			}
			logger.info("[selectRandomDropdownOption] Raw dropdown items found for '" + fieldLabel + "': " + rawItems.size());
			List<WebElement> candidates = new ArrayList<>();

			for (WebElement item : rawItems) {
				try {
					WebElement optionElement = item;
					List<WebElement> optionSpans = item.findElements(By.cssSelector("span.multiselect__option"));
					if (!optionSpans.isEmpty()) {
						optionElement = optionSpans.get(0);
					}

					String text = optionElement.getText().trim();
					logger.info("[selectRandomDropdownOption] Raw item text='" + text + "' displayed=" + item.isDisplayed());
					if (!item.isDisplayed()) continue;
					if (text.isBlank()) continue;
					if (text.contains("Press enter to remove")) continue;
					if (text.contains("Press enter to select")) continue;
					if (text.equalsIgnoreCase("Please choose an option")) continue;
					candidates.add(optionElement);
				} catch (StaleElementReferenceException ignore) {
					logger.warn("[selectRandomDropdownOption] Stale dropdown item encountered while reading options.");
				}
			}

			if (candidates.isEmpty()) {
				logger.warn("[selectRandomDropdownOption] No selectable dropdown options found for field: " + fieldLabel);
				closeOpenDropdownIfPresent();
				return false;
			}

			WebElement selected = candidates.get(randInt(0, candidates.size() - 1));
			String optionText = selected.getText().trim();
			scrollScreen(selected);
			clickAndDraw(selected);
			waitForMlsec(300);
			closeOpenDropdownIfPresent();

			logger.info(LogColor.DarkGreen + "[selectRandomDropdownOption] Selected random option for " + fieldLabel + ": " + optionText + LogColor.RESET);
			return true;

		} catch (Exception e) {
			logger.error("[selectRandomDropdownOption] Failed for field: " + fieldLabel, e);
			closeOpenDropdownIfPresent();
			return false;
		}
	}

	private boolean goToNextResultsPage() {
		try {
			scrollToPageBottom();

			if (isElementPresent(PAGINATION_NEXT_ARROW)) {
				Integer currentPage = getCurrentPaginationPage();
				clickAndDrawBy(PAGINATION_NEXT_ARROW);
				waitForPageChange(currentPage);
				return true;
			}

			return false;

		} catch (Exception e) {
			logger.error("Failed to go to the next search results page.", e);
			return false;
		}
	}

	private Integer getCurrentPaginationPage() {
		try {
			WebElement activePage = waitForElement(ACTIVE_PAGE_BUTTON);
			if (activePage == null) {
				return null;
			}

			String text = activePage.getText().trim();
			if (text.matches("\\d+")) {
				return Integer.parseInt(text);
			}
			return null;

		} catch (Exception e) {
			logger.warn("Could not determine current pagination page.", e);
			return null;
		}
	}

	private void waitForPageChange(Integer previousPage) {
		try {
			if (previousPage == null) {
				waitForPageAndAjaxToLoad();
				waitForNetworkIdle();
				return;
			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until((WebDriver d) -> {
				Integer currentPage = getCurrentPaginationPage();
				return currentPage == null || !currentPage.equals(previousPage);
			});
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();

		} catch (TimeoutException e) {
			logger.warn("Timed out waiting for pagination page to change.");
		}
	}

	private boolean openMatchingCardOnCurrentPage(String targetProgramTitle) {
		try {
			List<WebElement> cards = driver.findElements(PROGRAM_SEARCH_CARDS);
			logger.info("Program cards visible on current page: " + cards.size());

			for (int i = 0; i < cards.size(); i++) {
				List<WebElement> freshCards = driver.findElements(PROGRAM_SEARCH_CARDS);
				if (i >= freshCards.size()) {
					break;
				}

				WebElement card = freshCards.get(i);
				WebElement title = card.findElement(By.xpath(".//h3"));
				String actualTitle = title.getText().trim();
				logger.info("Checking card title: " + actualTitle);

				if (actualTitle.equals(targetProgramTitle)) {
					WebElement seeMoreLink = card.findElement(By.cssSelector("a.see-more-button"));
					scrollScreen(seeMoreLink);
					clickAndDraw(seeMoreLink);
					waitForPageAndAjaxToLoad();
					waitForNetworkIdle();
					logger.info("Opened matching program card: " + targetProgramTitle);
					return true;
				}
			}

			return false;

		} catch (Exception e) {
			logger.error("Failed while scanning current page for target program card: " + targetProgramTitle, e);
			return false;
		}
	}

	private void scrollToPageBottom() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		waitForMlsec(500);
	}

	private static String xpathLiteral(String text) {
		if (text == null) return "''";
		if (!text.contains("'")) return "'" + text + "'";

		String[] parts = text.split("'");
		StringBuilder builder = new StringBuilder("concat(");
		for (int i = 0; i < parts.length; i++) {
			builder.append("'").append(parts[i]).append("'");
			if (i < parts.length - 1) {
				builder.append(",\"'\",");
			}
		}
		builder.append(")");
		return builder.toString();
	}

	private boolean logStepResult(String stepName, boolean result) {
		logger.info("[fillApplicationForm] Step='" + stepName + "' result=" + result);
		return result;
	}

	private void closeOpenDropdownIfPresent() {
		try {
			driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
			waitForMlsec(150);
			driver.findElement(By.tagName("body")).click();
			waitForMlsec(150);
			logger.info("[closeOpenDropdownIfPresent] Sent ESCAPE and body click to close any open dropdown.");
		} catch (Exception e) {
			logger.info("[closeOpenDropdownIfPresent] No dropdown close action was needed.");
		}
	}
}