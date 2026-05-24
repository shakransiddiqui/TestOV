package ov.pages.passport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import ov.utilities.CommonMethods;
import ov.utilities.Driver;

public class ApplicationManagement_POM extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(ApplicationManagement_POM.class);

	private static final By PROGRAM_CARDS = By.cssSelector("div.program-list-container div.ProgramCard");
	private static final By PROGRAM_CARD_TITLE = By.cssSelector("h5.p-card-name");
	private static final By MANAGE_APPLICATIONS_LINK = By.cssSelector("a.cohort-action-button");
	private static final By ORGANIZATION_PAGINATION = By.cssSelector("nav.org-programs-pagination[aria-label='Pagination']");
	private static final By ORGANIZATION_ACTIVE_PAGE = By.cssSelector("nav.org-programs-pagination div.page-links button.page-link.active");
	private static final By ORGANIZATION_NEXT_PAGE = By.cssSelector("nav.org-programs-pagination button.nav-arrow[aria-label='Next page']:not([disabled])");
	private static final By SUBJECT_COUNT_TEXT = By.cssSelector("div.subject-count-container span.subject-count-text");
	private static final By APPLICATION_TABLE_CONTAINER = By.cssSelector("div.round-subjects-table-container div.invite-table-content");
	private static final By APPLICATION_TABLE_ROWS = By.cssSelector("div.subjects-table-list tbody tr.row");
	private static final By BACK_TO_DASHBOARD_BUTTON = By.xpath("//span[contains(@class,'back-button')][.//span[normalize-space()='Back to Dashboard']]");
	private static final By INVITE_BUTTON = By.cssSelector("button.action-invite-btn");
	private static final By EVALUATION_PROGRAM_NAME = By.cssSelector("div.eval-header h3.program-name");
	private static final By EVALUATION_GO_TO_PROGRAM_PAGE_BUTTON = By.xpath("(//div[contains(@class,'eval-header')]//div[contains(@class,'eval-main-right')]//button[contains(@class,'circle-button')])[1]");
	private static final By EVALUATION_HEADER_MENU_BUTTON = By.xpath("//div[contains(@class,'eval-header')]//div[contains(@class,'eval-main-right')]//div[contains(@class,'eval-action-wrapper')][last()]//button[contains(@class,'circle-button')]");
	private static final By EVALUATION_HEADER_EDIT_PROGRAM_DESCRIPTION_OPTION = By.xpath("//div[contains(@class,'eval-header')]//div[contains(@class,'additional-menu')]//li[.//span[normalize-space()='Edit Program Description']]");
	private static final By EVALUATION_PAGINATION = By.cssSelector("nav.TPaginationEllipsis[aria-label='Pagination']");
	private static final By EVALUATION_ACTIVE_PAGE = By.cssSelector("nav.TPaginationEllipsis[aria-label='Pagination'] div.page-links button.page-link.active");
	private static final By EVALUATION_PAGE_LINKS = By.cssSelector("nav.TPaginationEllipsis[aria-label='Pagination'] div.page-links button.page-link");
	private static final By ALL_FILTER = By.xpath("//div[contains(@class,'eval-filters')]//div[contains(@class,'eval-filter')][.//span[normalize-space()='All']]");
	private static final By NEEDS_REVIEW_FILTER = By.xpath("//div[contains(@class,'eval-filters')]//div[contains(@class,'eval-filter')][.//span[normalize-space()='Needs Review']]");
	private static final By ON_HOLD_FILTER = By.xpath("//div[contains(@class,'eval-filters')]//div[contains(@class,'eval-filter')][.//span[normalize-space()='On Hold']]");
	private static final By ACCEPTED_FILTER = By.xpath("//div[contains(@class,'eval-filters')]//div[contains(@class,'eval-filter')][.//span[normalize-space()='Accepted']]");
	private static final By REJECTED_FILTER = By.xpath("//div[contains(@class,'eval-filters')]//div[contains(@class,'eval-filter')][.//span[normalize-space()='Rejected']]");
	private static final By EXPORT_MENU_BUTTON = By.cssSelector("div.subsection-top-row-actions div.eval-action-wrapper button.circle-button.secondary");
	private static final By EVALUATION_SEARCH_INPUT = By.cssSelector("div.search-action-container input[placeholder='Search']");
	private static final By EVALUATION_SUBSECTION_PREVIEW_APPLICATION_OPTION = By.xpath("//div[contains(@class,'additional-menu')]//li[.//span[normalize-space()='Preview Application']]");
	private static final By EVALUATION_SUBSECTION_EDIT_APPLICATION_OPTION = By.xpath("//div[contains(@class,'additional-menu')]//li[.//span[normalize-space()='Edit Application']]");
	private static final By EXPORT_MENU_OPTION = By.xpath("//div[contains(@class,'additional-menu')]//li[.//span[normalize-space()='Export']]");
	private static final By STATUS_CONFIRMATION_MODAL = By.cssSelector("div.modal-wrapper");
	private static final By STATUS_CONFIRMATION_MODAL_TITLE = By.cssSelector("div.modal-wrapper span.header-text");
	private static final By STATUS_CONFIRMATION_MODAL_BODY = By.cssSelector("div.modal-wrapper span.body-text");
	private static final By EMAIL_COMPOSER_MODAL = By.cssSelector("div.modal-wrapper div.EmailForm");
	private static final By EMAIL_COMPOSER_MODAL_TITLE = By.cssSelector("div.modal-wrapper span.modal-title");
	private static final By EMAIL_TEMPLATE_SELECT = By.cssSelector("div.modal-wrapper select[name='Template']");
	private static final By EMAIL_SUBJECT_INPUT = By.cssSelector("div.modal-wrapper div.email-subject-container input.text-input");
	private static final By EMAIL_BODY_EDITOR = By.cssSelector("div.modal-wrapper div.ql-editor");
	private static final By EMAIL_SEND_BUTTON = By.cssSelector("div.modal-wrapper button.action-btn-send");
	private static final By EMAIL_SENT_CONFIRMATION = By.cssSelector("div.modal-wrapper div.TEmailSentConfirmation");
	private static final By EMAIL_SENT_CONFIRMATION_TEXT = By.cssSelector("div.modal-wrapper span.confirmation-msg-text");
	private static final By EMAIL_SENT_CONFIRMATION_CLOSE = By.cssSelector("div.modal-wrapper button.EmailForm-email-sent-close-btn");
	private static final By EMAIL_COMPOSER_CLOSE_BUTTON = By.cssSelector("div.modal-wrapper button.modal-close-btn");
	private static final By INVITE_MODAL_TITLE = By.cssSelector("div.modal-wrapper span.modal-title");
	private static final By INVITE_MODAL_EMAIL_INPUT = By.id("email-add");
	private static final By INVITE_MODAL_ADD_BUTTON = By.cssSelector("button.add-email-btn");
	private static final By INVITE_MODAL_TABLE_CONTAINER = By.cssSelector("div.invite-table-container");
	private static final By INVITE_MODAL_CANCEL_BUTTON = By.cssSelector("div.modal-wrapper button.footer-btn.cancel");
	private static final By INVITE_MODAL_SEND_BUTTON = By.cssSelector("div.modal-wrapper button.footer-btn.send");
	private static final By EDIT_PROGRAM_PAGE_TITLE = By.xpath("//span[contains(@class,'form-title-text')][normalize-space()='Edit Program']");
	private static final By EDIT_PROGRAM_FORM_CONTAINER = By.cssSelector("div.CreateProgram div.ProgramFormV2");
	private static final By EDIT_APPLICATION_PAGE_TITLE = By.xpath("//span[contains(@class,'form-title-text')][normalize-space()='Edit Application']");
	private static final By BACK_TO_APPLICATION_BUTTON = By.xpath("//button[contains(@class,'preview-btn')][.//span[normalize-space()='Back to Application']]");
	private static final By PREVIEW_APPLICATION_BUTTON = By.xpath("//button[contains(@class,'preview-btn')][.//span[normalize-space()='Preview Application']]");
	private static final By FORM_CONTENT_CONTAINER = By.cssSelector("div.form-content-container.bg-transparent.formbuilder-view");
	private static final By APPLICANT_FORM_CONTAINER = By.cssSelector("div.ApplicantFormOV");
	private static final By EDIT_APPLICATION_FORM_BUILDER = By.cssSelector("div.FormBuilderV2 main.form-builder");
	private static final By APPLICATION_DETAILS_LIST_LINK = By.xpath("//*[normalize-space()='Application List']");
	private static final By APPLICATION_DETAILS_MY_RATING_TAB = By.xpath("//*[normalize-space()='My Rating']");
	private static final By APPLICATION_DETAILS_SUBMIT_BUTTON = By.xpath("//button[normalize-space()='Submit']");
	private static final By PROGRAM_PAGE_CONTAINER = By.cssSelector("div#ProgramPage");
	private static final By PROGRAM_PAGE_NAME = By.cssSelector("#ProgramPage h2.program-name");
	private static final By PROGRAM_PAGE_DESCRIPTION = By.cssSelector("#ProgramPage div.description");
	private static final By PROGRAM_PAGE_COHORT_CARD = By.cssSelector("#ProgramPage div.ProgramCohortCard");
	private static final By PROGRAM_PAGE_EDIT_APPLICATION_BUTTON = By.cssSelector("#ProgramPage a.cohort-action-btn");
	private static final By BULK_ACTION_SELECT = By.cssSelector("select[name='bulkAction']");
	private static final By HEADER_SELECT_ALL_CHECKBOX = By.cssSelector("input[aria-label='checkbox-all']");
	private static final By STARTUP_NAME_CELLS = By.cssSelector("div.subjects-table-list tbody tr.row td:nth-child(2) span.td-value.link");
	private static final Pattern APPLICATION_COUNT_PATTERN = Pattern.compile("(\\d+)");
	private static final int MAX_ORGANIZATION_PAGES_TO_SCAN = 25;
	private static final int MAX_EVALUATION_PAGES_TO_SCAN = 25;

	private File latestDownloadedZipFile;

	public ApplicationManagement_POM() {
		PageFactory.initElements(driver, this);
	}

	public boolean openManageApplicationsForTargetProgram(String targetProgramName) {
		try {
			logger.info("[openManageApplicationsForTargetProgram] START targetProgramName='" + targetProgramName + "'");

			for (int pageAttempt = 1; pageAttempt <= MAX_ORGANIZATION_PAGES_TO_SCAN; pageAttempt++) {
				waitForOrganizationPageToBeReady();
				String activePage = getCurrentOrganizationPageLabel();
				logger.info("[openManageApplicationsForTargetProgram] Scanning organization page attempt=" + pageAttempt
						+ " activePage='" + activePage + "' currentUrl='" + driver.getCurrentUrl() + "'");

				if (clickManageApplicationsOnCurrentPage(targetProgramName, activePage)) {
					logger.info("[openManageApplicationsForTargetProgram] END result=true");
					return true;
				}

				if (!goToNextOrganizationPage(activePage)) {
					logger.warn("[openManageApplicationsForTargetProgram] Reached the last organization page without finding target program='"
							+ targetProgramName + "'");
					return false;
				}
			}

			logger.warn("[openManageApplicationsForTargetProgram] Stopped after max page scan limit without finding target program='"
					+ targetProgramName + "'");
			return false;
		} catch (Exception e) {
			logger.error("[openManageApplicationsForTargetProgram] Failed for targetProgramName='" + targetProgramName + "'", e);
			return false;
		}
	}

	private boolean clickManageApplicationsOnCurrentPage(String targetProgramName, String activePage) {
		List<WebElement> cards = driver.findElements(PROGRAM_CARDS);
		logger.info("[clickManageApplicationsOnCurrentPage] activePage='" + activePage + "' programCardsFound=" + cards.size());

		for (int i = 0; i < cards.size(); i++) {
			WebElement card = cards.get(i);
			String actualTitle = card.findElement(PROGRAM_CARD_TITLE).getText().trim();
			logger.info("[clickManageApplicationsOnCurrentPage] activePage='" + activePage + "' cardIndex=" + i
					+ " title='" + actualTitle + "'");

			if (actualTitle.equals(targetProgramName)) {
				WebElement manageApplicationsLink = card.findElement(MANAGE_APPLICATIONS_LINK);
				logger.info("[clickManageApplicationsOnCurrentPage] Match found on activePage='" + activePage
						+ "'. Clicking Manage Applications for '" + targetProgramName + "'");
				scrollScreen(manageApplicationsLink);
				clickAndDraw(manageApplicationsLink);
				waitForPageAndAjaxToLoad();
				waitForNetworkIdle();
				return true;
			}
		}

		logger.info("[clickManageApplicationsOnCurrentPage] Target program not found on activePage='" + activePage + "'");
		return false;
	}

	private boolean goToNextOrganizationPage(String currentPageLabel) {
		try {
			if (!isElementPresent(ORGANIZATION_PAGINATION)) {
				logger.info("[goToNextOrganizationPage] Pagination not present. Only one page is available.");
				return false;
			}

			List<WebElement> nextButtons = driver.findElements(ORGANIZATION_NEXT_PAGE);
			if (nextButtons.isEmpty()) {
				logger.info("[goToNextOrganizationPage] Next page button is not available or already disabled.");
				return false;
			}

			String currentUrl = driver.getCurrentUrl();
			logger.info("[goToNextOrganizationPage] Moving from activePage='" + currentPageLabel + "' currentUrl='" + currentUrl + "'");
			clickAndDrawBy(ORGANIZATION_NEXT_PAGE);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(d -> {
				String newActivePage = getCurrentOrganizationPageLabel();
				String newUrl = d.getCurrentUrl();
				boolean pageChanged = !newActivePage.equals(currentPageLabel) || !newUrl.equals(currentUrl);
				logger.info("[goToNextOrganizationPage] polling currentPageLabel='" + currentPageLabel
						+ "' newActivePage='" + newActivePage + "' currentUrl='" + currentUrl
						+ "' newUrl='" + newUrl + "' pageChanged=" + pageChanged);
				return pageChanged;
			});

			waitForOrganizationPageToBeReady();
			logger.info("[goToNextOrganizationPage] Navigation complete. New activePage='"
					+ getCurrentOrganizationPageLabel() + "' currentUrl='" + driver.getCurrentUrl() + "'");
			return true;
		} catch (Exception e) {
			logger.error("[goToNextOrganizationPage] Failed while moving to the next organization page.", e);
			return false;
		}
	}

	private void waitForOrganizationPageToBeReady() {
		waitForElement(PROGRAM_CARDS);
		waitForPageAndAjaxToLoad();
		waitForNetworkIdle();
		new WebDriverWait(driver, Duration.ofSeconds(20))
				.until(d -> !d.findElements(PROGRAM_CARDS).isEmpty());
	}

	private String getCurrentOrganizationPageLabel() {
		try {
			List<WebElement> activePageButtons = driver.findElements(ORGANIZATION_ACTIVE_PAGE);
			if (!activePageButtons.isEmpty()) {
				String activePageText = activePageButtons.get(0).getText().trim();
				if (!activePageText.isEmpty()) {
					return activePageText;
				}
			}
		} catch (Exception e) {
			logger.warn("[getCurrentOrganizationPageLabel] Could not read active page label from pagination.", e);
		}
		return "1";
	}

	public boolean switchToNewEvaluationTab(Set<String> existingWindowHandles) {
		return switchToNewTab(existingWindowHandles, "evaluation");
	}

	public boolean switchToNewEditProgramTab(Set<String> existingWindowHandles) {
		return switchToNewTab(existingWindowHandles, "edit program");
	}

	public boolean switchToNewPreviewApplicationTab(Set<String> existingWindowHandles) {
		return switchToNewTab(existingWindowHandles, "preview application");
	}

	public boolean switchToNewEditApplicationTab(Set<String> existingWindowHandles) {
		return switchToNewTab(existingWindowHandles, "edit application");
	}

	public boolean switchToNewProgramPageTab(Set<String> existingWindowHandles) {
		return switchToNewTab(existingWindowHandles, "program page");
	}

	private boolean switchToNewTab(Set<String> existingWindowHandles, String tabDescription) {
		try {
			logger.info("[switchToNewTab] START tabDescription='" + tabDescription + "' existingWindowHandles=" + existingWindowHandles);
			new WebDriverWait(driver, Duration.ofSeconds(20))
			.until(d -> d.getWindowHandles().size() > existingWindowHandles.size());

			for (String handle : driver.getWindowHandles()) {
				if (!existingWindowHandles.contains(handle)) {
					driver.switchTo().window(handle);
					waitForPageAndAjaxToLoad();
					waitForNetworkIdle();
					logger.info("[switchToNewTab] Switched to " + tabDescription + " tab handle=" + handle + " title='" + driver.getTitle() + "'");
					return true;
				}
			}

			logger.warn("[switchToNewTab] No new " + tabDescription + " tab was found. currentHandles=" + driver.getWindowHandles());
			return false;
		} catch (Exception e) {
			logger.error("[switchToNewTab] Failed to switch to newly opened " + tabDescription + " tab.", e);
			return false;
		}
	}

	public boolean waitForApplicationsToLoad() {
		try {
			logger.info("[waitForApplicationsToLoad] START");
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
			WebElement tableContainer = wait
					.until(ExpectedConditions.visibilityOfElementLocated(APPLICATION_TABLE_CONTAINER));

			Boolean loaded = wait.until(d -> {
				WebElement subjectCountElement = d.findElement(SUBJECT_COUNT_TEXT);
				String subjectCountText = subjectCountElement.getText().trim();
				Matcher matcher = APPLICATION_COUNT_PATTERN.matcher(subjectCountText);
				int applicationCount = matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
				boolean tableDisplayed = tableContainer.isDisplayed();
				int rowCount = d.findElements(APPLICATION_TABLE_ROWS).size();
				int allFilterCount = extractAllFilterCount();
				boolean hasApplicationData = applicationCount > 0 || rowCount > 0 || allFilterCount > 0;
				boolean result = tableDisplayed && hasApplicationData;

				logger.info("[waitForApplicationsToLoad] polling subjectCountText='" + subjectCountText
						+ "' applicationCount=" + applicationCount + " rowCount=" + rowCount
						+ " allFilterCount=" + allFilterCount + " tableDisplayed=" + tableDisplayed
						+ " result=" + result);
				return result;
			});

			logger.info("[waitForApplicationsToLoad] END result=" + loaded);
			return Boolean.TRUE.equals(loaded);
		} catch (Exception e) {
			logger.error("[waitForApplicationsToLoad] Failed while waiting for applications to load.", e);
			return false;
		}
	}

	public boolean searchForApplicationFromEvaluationPage(String targetApplicationName) {
		try {
			logger.info("[searchForApplicationFromEvaluationPage] START targetApplicationName='" + targetApplicationName + "'");
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(EVALUATION_SEARCH_INPUT));
			scrollScreen(searchInput);
			searchInput.clear();
			searchInput.sendKeys(targetApplicationName);
			searchInput.sendKeys(Keys.ENTER);

			wait.until(d -> {
				String currentValue = d.findElement(EVALUATION_SEARCH_INPUT).getAttribute("value");
				return currentValue != null && currentValue.trim().equals(targetApplicationName);
			});

			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[searchForApplicationFromEvaluationPage] END result=true");
			return true;
		} catch (Exception e) {
			logger.error("[searchForApplicationFromEvaluationPage] Failed while searching for application='" + targetApplicationName + "' from evaluation page.", e);
			return false;
		}
	}

	public boolean verifySearchResultsSectionDisplayedOnEvaluationPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(EVALUATION_SEARCH_INPUT));
			WebElement tableContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(APPLICATION_TABLE_CONTAINER));
			List<WebElement> startupNameCells = wait.until(d -> {
				List<WebElement> cells = d.findElements(STARTUP_NAME_CELLS);
				return cells.isEmpty() ? null : cells;
			});

			String currentSearchValue = searchInput.getAttribute("value").trim();
			boolean result = tableContainer.isDisplayed()
					&& !currentSearchValue.isBlank()
					&& !startupNameCells.isEmpty();

			logger.info("[verifySearchResultsSectionDisplayedOnEvaluationPage] currentSearchValue='" + currentSearchValue
					+ "' visibleRows=" + startupNameCells.size() + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifySearchResultsSectionDisplayedOnEvaluationPage] Failed while verifying search results section on evaluation page.", e);
			return false;
		}
	}

	public boolean verifyMatchingApplicationRowsDisplayedForTargetApplication(String targetApplicationName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			List<WebElement> startupNameCells = wait.until(d -> {
				List<WebElement> cells = d.findElements(STARTUP_NAME_CELLS);
				return cells.isEmpty() ? null : cells;
			});

			List<String> startupNames = startupNameCells.stream()
					.map(WebElement::getText)
					.map(String::trim)
					.filter(text -> !text.isBlank())
					.collect(Collectors.toList());

			boolean result = !startupNames.isEmpty()
					&& startupNames.stream().anyMatch(name -> name.contains(targetApplicationName));

			logger.info("[verifyMatchingApplicationRowsDisplayedForTargetApplication] targetApplicationName='" + targetApplicationName
					+ "' startupNames=" + startupNames + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyMatchingApplicationRowsDisplayedForTargetApplication] Failed while verifying matching application rows for targetApplicationName='" + targetApplicationName + "'.", e);
			return false;
		}
	}

	public boolean verifyEachMatchingApplicationRowStartupNameContains(String targetApplicationName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			List<WebElement> startupNameCells = wait.until(d -> {
				List<WebElement> cells = d.findElements(STARTUP_NAME_CELLS);
				return cells.isEmpty() ? null : cells;
			});

			List<String> startupNames = startupNameCells.stream()
					.map(WebElement::getText)
					.map(String::trim)
					.filter(text -> !text.isBlank())
					.collect(Collectors.toList());

			boolean result = !startupNames.isEmpty()
					&& startupNames.stream().allMatch(name -> name.contains(targetApplicationName));

			logger.info("[verifyEachMatchingApplicationRowStartupNameContains] targetApplicationName='" + targetApplicationName
					+ "' startupNames=" + startupNames + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyEachMatchingApplicationRowStartupNameContains] Failed while verifying startup names contain targetApplicationName='" + targetApplicationName + "'.", e);
			return false;
		}
	}

	public boolean verifyEvaluationQuickActionDisplayed(String quickActionName) {
		try {
			By quickActionLocator = getEvaluationQuickActionLocator(quickActionName);
			WebElement quickActionButton = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(quickActionLocator));
			boolean result = quickActionButton.isDisplayed();
			logger.info("[verifyEvaluationQuickActionDisplayed] quickActionName='" + quickActionName + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyEvaluationQuickActionDisplayed] Failed while verifying evaluation quick action='"
					+ quickActionName + "'", e);
			return false;
		}
	}

	public boolean clickEvaluationQuickAction(String quickActionName) {
		try {
			By quickActionLocator = getEvaluationQuickActionLocator(quickActionName);
			WebElement quickActionButton = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(quickActionLocator));
			scrollScreen(quickActionButton);
			clickAndDrawBy(quickActionLocator);
			logger.info("[clickEvaluationQuickAction] quickActionName='" + quickActionName + "' clicked successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[clickEvaluationQuickAction] Failed while clicking evaluation quick action='"
					+ quickActionName + "'", e);
			return false;
		}
	}

	public boolean verifyRedirectedToProgramPageFromEvaluationQuickAction(Set<String> existingWindowHandles,
			String expectedProgramId) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(d -> d.getWindowHandles().size() > existingWindowHandles.size()
					|| d.getCurrentUrl().contains("/program?pid=")
					|| !d.getCurrentUrl().contains("/evaluation"));

			for (String handle : driver.getWindowHandles()) {
				if (!existingWindowHandles.contains(handle)) {
					driver.switchTo().window(handle);
					break;
				}
			}

			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();

			String currentUrl = driver.getCurrentUrl();
			boolean result = currentUrl.contains("/program?pid=")
					&& !currentUrl.contains("/evaluation")
					&& (expectedProgramId == null || currentUrl.contains("/program?pid=" + expectedProgramId));
			logger.info("[verifyRedirectedToProgramPageFromEvaluationQuickAction] expectedProgramId='" + expectedProgramId
					+ "' currentUrl='" + currentUrl + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyRedirectedToProgramPageFromEvaluationQuickAction] Failed while verifying redirect to program page.", e);
			return false;
		}
	}

	public boolean verifyProgramPageOpenedFromEvaluationQuickAction() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement programPageContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(PROGRAM_PAGE_CONTAINER));
			WebElement programPageName = wait.until(ExpectedConditions.visibilityOfElementLocated(PROGRAM_PAGE_NAME));
			WebElement programPageDescription = wait.until(ExpectedConditions.visibilityOfElementLocated(PROGRAM_PAGE_DESCRIPTION));
			WebElement cohortCard = wait.until(ExpectedConditions.visibilityOfElementLocated(PROGRAM_PAGE_COHORT_CARD));
			WebElement editApplicationButton = wait.until(ExpectedConditions.visibilityOfElementLocated(PROGRAM_PAGE_EDIT_APPLICATION_BUTTON));

			String actualProgramName = programPageName.getText().trim();
			String currentUrl = driver.getCurrentUrl();
			boolean result = programPageContainer.isDisplayed()
					&& !actualProgramName.isBlank()
					&& programPageDescription.isDisplayed()
					&& cohortCard.isDisplayed()
					&& editApplicationButton.isDisplayed()
					&& currentUrl.contains("/program?pid=");

			logger.info("[verifyProgramPageOpenedFromEvaluationQuickAction] actualProgramName='" + actualProgramName
					+ "' currentUrl='" + currentUrl + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyProgramPageOpenedFromEvaluationQuickAction] Failed while verifying the Program page opened from the evaluation quick action.", e);
			return false;
		}
	}

	public boolean clickBackToDashboardFromEvaluationPage() {
		try {
			logger.info("[clickBackToDashboardFromEvaluationPage] START");
			WebElement backToDashboardButton = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(BACK_TO_DASHBOARD_BUTTON));
			scrollScreen(backToDashboardButton);
			clickAndDraw(backToDashboardButton);

			new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.urlContains("/organization"));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[clickBackToDashboardFromEvaluationPage] END result=true currentUrl='" + driver.getCurrentUrl() + "'");
			return true;
		} catch (Exception e) {
			logger.error("[clickBackToDashboardFromEvaluationPage] Failed while clicking Back to Dashboard from evaluation page.", e);
			return false;
		}
	}

	public boolean clickInviteFromEvaluationPage() {
		try {
			logger.info("[clickInviteFromEvaluationPage] START");
			WebElement inviteButton = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(INVITE_BUTTON));
			scrollScreen(inviteButton);
			clickAndDraw(inviteButton);

			new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(INVITE_MODAL_EMAIL_INPUT));
			logger.info("[clickInviteFromEvaluationPage] END result=true");
			return true;
		} catch (Exception e) {
			logger.error("[clickInviteFromEvaluationPage] Failed while clicking Invite from evaluation page.", e);
			return false;
		}
	}

	public boolean openEvaluationHeaderMenu() {
		try {
			logger.info("[openEvaluationHeaderMenu] START");
			WebElement headerMenuButton = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(EVALUATION_HEADER_MENU_BUTTON));
			scrollScreen(headerMenuButton);
			clickAndDrawBy(EVALUATION_HEADER_MENU_BUTTON);

			new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(EVALUATION_HEADER_EDIT_PROGRAM_DESCRIPTION_OPTION));
			logger.info("[openEvaluationHeaderMenu] END result=true");
			return true;
		} catch (Exception e) {
			logger.error("[openEvaluationHeaderMenu] Failed while opening the evaluation header menu.", e);
			return false;
		}
	}

	public boolean verifyEvaluationHeaderMenuOptionDisplayed(String optionName) {
		try {
			By optionLocator = getEvaluationHeaderMenuOptionLocator(optionName);
			WebElement optionElement = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
			boolean result = optionElement.isDisplayed();
			logger.info("[verifyEvaluationHeaderMenuOptionDisplayed] optionName='" + optionName + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyEvaluationHeaderMenuOptionDisplayed] Failed while verifying evaluation header menu option='"
					+ optionName + "'", e);
			return false;
		}
	}

	public boolean clickEvaluationHeaderMenuOption(String optionName) {
		try {
			By optionLocator = getEvaluationHeaderMenuOptionLocator(optionName);
			WebElement optionElement = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(optionLocator));
			scrollScreen(optionElement);
			clickAndDrawBy(optionLocator);
			logger.info("[clickEvaluationHeaderMenuOption] Clicked optionName='" + optionName + "' successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[clickEvaluationHeaderMenuOption] Failed while clicking evaluation header menu option='"
					+ optionName + "'", e);
			return false;
		}
	}

	public boolean verifyEditProgramPageAndFormDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(EDIT_PROGRAM_PAGE_TITLE));
			WebElement formContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(EDIT_PROGRAM_FORM_CONTAINER));
			boolean result = "Edit Program".equals(pageTitle.getText().trim()) && formContainer.isDisplayed();
			logger.info("[verifyEditProgramPageAndFormDisplayed] pageTitle='" + pageTitle.getText().trim()
					+ "' formDisplayed=" + formContainer.isDisplayed() + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyEditProgramPageAndFormDisplayed] Failed while verifying Edit Program page and form.", e);
			return false;
		}
	}

	public boolean openEvaluationSubsectionMenu() {
		try {
			logger.info("[openEvaluationSubsectionMenu] START");
			WebElement subsectionMenuButton = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(EXPORT_MENU_BUTTON));
			scrollScreen(subsectionMenuButton);
			clickAndDrawBy(EXPORT_MENU_BUTTON);

			new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(EVALUATION_SUBSECTION_PREVIEW_APPLICATION_OPTION));
			logger.info("[openEvaluationSubsectionMenu] END result=true");
			return true;
		} catch (Exception e) {
			logger.error("[openEvaluationSubsectionMenu] Failed while opening the evaluation subsection menu.", e);
			return false;
		}
	}

	public boolean verifyEvaluationSubsectionMenuOptionDisplayed(String optionName) {
		try {
			By optionLocator = getEvaluationSubsectionMenuOptionLocator(optionName);
			WebElement optionElement = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
			boolean result = optionElement.isDisplayed();
			logger.info("[verifyEvaluationSubsectionMenuOptionDisplayed] optionName='" + optionName + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyEvaluationSubsectionMenuOptionDisplayed] Failed while verifying evaluation subsection menu option='"
					+ optionName + "'", e);
			return false;
		}
	}

	public boolean clickEvaluationSubsectionMenuOption(String optionName) {
		try {
			boolean clicked = clickEvaluationSubsectionMenuOptionSafely(optionName);
			logger.info("[clickEvaluationSubsectionMenuOption] optionName='" + optionName + "' clicked=" + clicked);
			return clicked;
		} catch (Exception e) {
			logger.error("[clickEvaluationSubsectionMenuOption] Failed while clicking evaluation subsection menu option='"
					+ optionName + "'", e);
			return false;
		}
	}

	public boolean verifyPreviewApplicationPageAndFormDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(EDIT_APPLICATION_PAGE_TITLE));
			WebElement formContent = wait.until(ExpectedConditions.visibilityOfElementLocated(FORM_CONTENT_CONTAINER));
			WebElement applicantForm = wait.until(ExpectedConditions.visibilityOfElementLocated(APPLICANT_FORM_CONTAINER));
			WebElement backToApplicationButton = wait.until(ExpectedConditions.visibilityOfElementLocated(BACK_TO_APPLICATION_BUTTON));
			boolean result = "Edit Application".equals(pageTitle.getText().trim())
					&& formContent.isDisplayed()
					&& applicantForm.isDisplayed()
					&& backToApplicationButton.isDisplayed();
			logger.info("[verifyPreviewApplicationPageAndFormDisplayed] pageTitle='" + pageTitle.getText().trim()
					+ "' formContentDisplayed=" + formContent.isDisplayed()
					+ " applicantFormDisplayed=" + applicantForm.isDisplayed()
					+ " backButtonDisplayed=" + backToApplicationButton.isDisplayed()
					+ " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyPreviewApplicationPageAndFormDisplayed] Failed while verifying Preview Application page.", e);
			return false;
		}
	}

	public boolean verifyEditApplicationPageAndFormDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(EDIT_APPLICATION_PAGE_TITLE));
			WebElement formBuilder = wait.until(ExpectedConditions.visibilityOfElementLocated(EDIT_APPLICATION_FORM_BUILDER));
			WebElement previewButton = wait.until(ExpectedConditions.visibilityOfElementLocated(PREVIEW_APPLICATION_BUTTON));
			boolean result = "Edit Application".equals(pageTitle.getText().trim())
					&& formBuilder.isDisplayed()
					&& previewButton.isDisplayed();
			logger.info("[verifyEditApplicationPageAndFormDisplayed] pageTitle='" + pageTitle.getText().trim()
					+ "' formBuilderDisplayed=" + formBuilder.isDisplayed()
					+ " previewButtonDisplayed=" + previewButton.isDisplayed()
					+ " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyEditApplicationPageAndFormDisplayed] Failed while verifying Edit Application page.", e);
			return false;
		}
	}

	private By getEvaluationHeaderMenuOptionLocator(String optionName) {
		if ("Edit Program Description".equals(optionName)) {
			return EVALUATION_HEADER_EDIT_PROGRAM_DESCRIPTION_OPTION;
		}
		throw new IllegalArgumentException("Unsupported evaluation header menu option: " + optionName);
	}

	private By getEvaluationQuickActionLocator(String quickActionName) {
		if ("Go To Program Page".equals(quickActionName)) {
			return EVALUATION_GO_TO_PROGRAM_PAGE_BUTTON;
		}
		throw new IllegalArgumentException("Unsupported evaluation quick action: " + quickActionName);
	}

	private By getEvaluationSubsectionMenuOptionLocator(String optionName) {
		if ("Preview Application".equals(optionName)) {
			return EVALUATION_SUBSECTION_PREVIEW_APPLICATION_OPTION;
		}
		if ("Edit Application".equals(optionName)) {
			return EVALUATION_SUBSECTION_EDIT_APPLICATION_OPTION;
		}
		throw new IllegalArgumentException("Unsupported evaluation subsection menu option: " + optionName);
	}

	private boolean clickEvaluationSubsectionMenuOptionSafely(String optionName) {
		By optionLocator = getEvaluationSubsectionMenuOptionLocator(optionName);

		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				logger.info("[clickEvaluationSubsectionMenuOptionSafely] attempt=" + attempt + " optionName='" + optionName + "'");

				if (!ensureEvaluationSubsectionMenuOptionVisible(optionName)) {
					logger.warn("[clickEvaluationSubsectionMenuOptionSafely] Option is not visible on attempt=" + attempt
							+ " optionName='" + optionName + "'");
					continue;
				}

				WebElement optionElement = new WebDriverWait(driver, Duration.ofSeconds(5))
						.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
				scrollScreen(optionElement);

				try {
					optionElement.click();
					logger.info("[clickEvaluationSubsectionMenuOptionSafely] Native click succeeded on attempt=" + attempt
							+ " optionName='" + optionName + "'");
				} catch (Exception nativeClickException) {
					logger.warn("[clickEvaluationSubsectionMenuOptionSafely] Native click failed on attempt=" + attempt
							+ " optionName='" + optionName + "' message=" + nativeClickException.getMessage());
					optionElement = driver.findElement(optionLocator);
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionElement);
					logger.info("[clickEvaluationSubsectionMenuOptionSafely] JS click succeeded on attempt=" + attempt
							+ " optionName='" + optionName + "'");
				}

				return true;
			} catch (StaleElementReferenceException staleElementReferenceException) {
				logger.warn("[clickEvaluationSubsectionMenuOptionSafely] Stale element on attempt=" + attempt
						+ " optionName='" + optionName + "'. Retrying...");
			} catch (Exception e) {
				logger.warn("[clickEvaluationSubsectionMenuOptionSafely] Attempt=" + attempt + " failed for optionName='"
						+ optionName + "'", e);
			}
		}

		logger.error("[clickEvaluationSubsectionMenuOptionSafely] Failed after all retry attempts for optionName='"
				+ optionName + "'");
		return false;
	}

	private boolean ensureEvaluationSubsectionMenuOptionVisible(String optionName) {
		try {
			By optionLocator = getEvaluationSubsectionMenuOptionLocator(optionName);

			if (isElementPresent(optionLocator)) {
				logger.info("[ensureEvaluationSubsectionMenuOptionVisible] Option is already visible optionName='" + optionName + "'");
				return true;
			}

			WebElement menuButton = waitForElement(EXPORT_MENU_BUTTON);
			if (menuButton == null) {
				logger.warn("[ensureEvaluationSubsectionMenuOptionVisible] Subsection menu button not found for optionName='"
						+ optionName + "'");
				return false;
			}

			logger.info("[ensureEvaluationSubsectionMenuOptionVisible] Opening subsection menu for optionName='" + optionName + "'");
			scrollScreen(menuButton);
			clickAndDrawBy(EXPORT_MENU_BUTTON);

			WebElement optionElement = waitForElement(optionLocator);
			boolean visible = optionElement != null && optionElement.isDisplayed();
			logger.info("[ensureEvaluationSubsectionMenuOptionVisible] optionName='" + optionName + "' visible=" + visible);
			return visible;
		} catch (Exception e) {
			logger.error("[ensureEvaluationSubsectionMenuOptionVisible] Failed while ensuring subsection menu option visibility for optionName='"
					+ optionName + "'", e);
			return false;
		}
	}

	public boolean verifyInviteModalFromEvaluationPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement modalTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(INVITE_MODAL_TITLE));
			WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(INVITE_MODAL_EMAIL_INPUT));
			WebElement addButton = wait.until(ExpectedConditions.visibilityOfElementLocated(INVITE_MODAL_ADD_BUTTON));
			boolean tableVisible = isElementPresent(INVITE_MODAL_TABLE_CONTAINER);
			boolean cancelVisible = isElementPresent(INVITE_MODAL_CANCEL_BUTTON);
			boolean sendVisible = isElementPresent(INVITE_MODAL_SEND_BUTTON);
			boolean result = "Invite Applicants".equals(modalTitle.getText().trim())
					&& emailInput.isDisplayed()
					&& addButton.isDisplayed()
					&& tableVisible
					&& cancelVisible
					&& sendVisible;

			logger.info("[verifyInviteModalFromEvaluationPage] modalTitle='" + modalTitle.getText().trim()
					+ "' tableVisible=" + tableVisible + " cancelVisible=" + cancelVisible
					+ " sendVisible=" + sendVisible + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyInviteModalFromEvaluationPage] Failed while verifying Invite modal from evaluation page.", e);
			return false;
		}
	}

	public boolean closeInviteModalUsingCancelFromEvaluationPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(INVITE_MODAL_CANCEL_BUTTON));
			scrollScreen(cancelButton);
			clickAndDraw(cancelButton);

			wait.until(ExpectedConditions.invisibilityOfElementLocated(INVITE_MODAL_EMAIL_INPUT));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[closeInviteModalUsingCancelFromEvaluationPage] Closed Invite modal using Cancel successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[closeInviteModalUsingCancelFromEvaluationPage] Failed while closing Invite modal using Cancel from evaluation page.", e);
			return false;
		}
	}

	public boolean sendInviteFromEvaluationPageAndVerifyModalCloses() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(INVITE_MODAL_SEND_BUTTON));
			scrollScreen(sendButton);
			clickAndDraw(sendButton);

			wait.until(ExpectedConditions.invisibilityOfElementLocated(INVITE_MODAL_EMAIL_INPUT));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[sendInviteFromEvaluationPageAndVerifyModalCloses] Invite sent and modal closed successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[sendInviteFromEvaluationPageAndVerifyModalCloses] Failed while sending invite from evaluation page.", e);
			return false;
		}
	}

	public boolean openExportMenu() {
		try {
			logger.info("[openExportMenu] START");
			boolean menuVisible = ensureExportMenuOptionVisible();
			logger.info("[openExportMenu] END result=" + menuVisible);
			return menuVisible;
		} catch (Exception e) {
			logger.error("[openExportMenu] Failed to open export menu.", e);
			return false;
		}
	}

	public boolean clickExportAndWaitForDownload(String targetProgramName) {
		try {
			logger.info("[clickExportAndWaitForDownload] START targetProgramName='" + targetProgramName + "'");
			clearOldZipDownloads();

			if (!ensureExportMenuOptionVisible()) {
				logger.warn("[clickExportAndWaitForDownload] Export option not found.");
				return false;
			}

			logger.info("[clickExportAndWaitForDownload] Clicking export option using export-specific stale-safe flow.");
			if (!clickExportMenuOption()) {
				logger.warn("[clickExportAndWaitForDownload] Failed to click Export option.");
				return false;
			}
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();

			latestDownloadedZipFile = waitForDownloadedZip(targetProgramName);
			boolean downloaded = latestDownloadedZipFile != null && latestDownloadedZipFile.exists();
			logger.info("[clickExportAndWaitForDownload] END result=" + downloaded + " file="
					+ (latestDownloadedZipFile == null ? "null" : latestDownloadedZipFile.getAbsolutePath()));
			return downloaded;
		} catch (Exception e) {
			logger.error("[clickExportAndWaitForDownload] Failed to export applications.", e);
			return false;
		}
	}

	private boolean clickExportMenuOption() {
		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				logger.info("[clickExportMenuOption] attempt=" + attempt);
				if (!ensureExportMenuOptionVisible()) {
					logger.warn("[clickExportMenuOption] Export option is not visible on attempt=" + attempt);
					continue;
				}

				WebElement exportOption = new WebDriverWait(driver, Duration.ofSeconds(5))
						.until(ExpectedConditions.visibilityOfElementLocated(EXPORT_MENU_OPTION));
				scrollScreen(exportOption);

				try {
					exportOption.click();
					logger.info("[clickExportMenuOption] Native click succeeded on attempt=" + attempt);
				} catch (Exception nativeClickException) {
					logger.warn("[clickExportMenuOption] Native click failed on attempt=" + attempt + ": "
							+ nativeClickException.getMessage());
					exportOption = driver.findElement(EXPORT_MENU_OPTION);
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", exportOption);
					logger.info("[clickExportMenuOption] JS click succeeded on attempt=" + attempt);
				}

				new WebDriverWait(driver, Duration.ofSeconds(5))
						.until(ExpectedConditions.invisibilityOfElementLocated(EXPORT_MENU_OPTION));
				logger.info("[clickExportMenuOption] Export option disappeared after click on attempt=" + attempt);
				return true;
			} catch (StaleElementReferenceException staleElementReferenceException) {
				logger.warn("[clickExportMenuOption] Stale element on attempt=" + attempt + ". Retrying...");
			} catch (Exception e) {
				logger.warn("[clickExportMenuOption] Attempt=" + attempt + " failed.", e);
			}
		}

		logger.error("[clickExportMenuOption] Failed after all retry attempts.");
		return false;
	}

	private boolean ensureExportMenuOptionVisible() {
		try {
			if (isElementPresent(EXPORT_MENU_OPTION)) {
				logger.info("[ensureExportMenuOptionVisible] Export option is already visible.");
				return true;
			}

			WebElement menuButton = waitForElement(EXPORT_MENU_BUTTON);
			if (menuButton == null) {
				logger.warn("[ensureExportMenuOptionVisible] Export menu button not found.");
				return false;
			}

			logger.info("[ensureExportMenuOptionVisible] Opening export menu.");
			scrollScreen(menuButton);
			clickAndDrawBy(EXPORT_MENU_BUTTON);

			WebElement exportOption = waitForElement(EXPORT_MENU_OPTION);
			boolean visible = exportOption != null && exportOption.isDisplayed();
			logger.info("[ensureExportMenuOptionVisible] END result=" + visible);
			return visible;
		} catch (Exception e) {
			logger.error("[ensureExportMenuOptionVisible] Failed while ensuring export option visibility.", e);
			return false;
		}
	}

	public boolean downloadedZipContainsExpectedFiles() {
		try {
			logger.info("[downloadedZipContainsExpectedFiles] START latestDownloadedZipFile="
					+ (latestDownloadedZipFile == null ? "null" : latestDownloadedZipFile.getAbsolutePath()));
			if (latestDownloadedZipFile == null || !latestDownloadedZipFile.exists()) {
				logger.warn("[downloadedZipContainsExpectedFiles] No downloaded zip is available to inspect.");
				return false;
			}

			List<String> expectedFiles = Arrays.asList("Applicant_Summaries.csv", "Score_Details.csv");
			try (ZipFile zipFile = new ZipFile(latestDownloadedZipFile)) {
				for (String expectedFile : expectedFiles) {
					boolean present = zipFile.stream()
							.map(ZipEntry::getName)
							.anyMatch(entryName -> entryName.endsWith(expectedFile));
					logger.info("[downloadedZipContainsExpectedFiles] expectedFile='" + expectedFile + "' present=" + present);
					if (!present) {
						return false;
					}
				}
			}

			logger.info("[downloadedZipContainsExpectedFiles] END result=true");
			return true;
		} catch (Exception e) {
			logger.error("[downloadedZipContainsExpectedFiles] Failed while checking zip contents.", e);
			return false;
		}
	}

	public int getTotalApplicationsCount() {
		return extractFirstNumberFromElement(SUBJECT_COUNT_TEXT, "getTotalApplicationsCount");
	}

	public int getCurrentPageApplicationsCountFromAllFilter() {
		return extractAllFilterCount();
	}

	public int getDisplayedApplicationRowCount() {
		try {
			waitForEvaluationPageDataToStabilize();
			int rowCount = driver.findElements(APPLICATION_TABLE_ROWS).size();
			logger.info("[getDisplayedApplicationRowCount] rowCount=" + rowCount);
			return rowCount;
		} catch (Exception e) {
			logger.error("[getDisplayedApplicationRowCount] Failed while counting displayed application rows.", e);
			return -1;
		}
	}

	public boolean verifyCurrentPageHasNoMoreThanTenRows() {
		int rowCount = getDisplayedApplicationRowCount();
		boolean result = rowCount >= 0 && rowCount <= 10;
		logger.info("[verifyCurrentPageHasNoMoreThanTenRows] rowCount=" + rowCount + " result=" + result);
		return result;
	}

	public boolean isEvaluationPaginationVisible() {
		try {
			waitForEvaluationPageDataToStabilize();
			boolean visible = isElementPresent(EVALUATION_PAGINATION);
			logger.info("[isEvaluationPaginationVisible] visible=" + visible);
			return visible;
		} catch (Exception e) {
			logger.error("[isEvaluationPaginationVisible] Failed while checking evaluation pagination visibility.", e);
			return false;
		}
	}

	public boolean verifyEvaluationPaginationBehavior(int totalApplications) {
		try {
			logger.info("[verifyEvaluationPaginationBehavior] START totalApplications=" + totalApplications);
			waitForEvaluationPageDataToStabilize();

			if (totalApplications <= 10) {
				boolean paginationVisible = isElementPresent(EVALUATION_PAGINATION);
				int rowCount = getDisplayedApplicationRowCount();
				int allFilterCount = getCurrentPageApplicationsCountFromAllFilter();
				boolean result = !paginationVisible && rowCount == totalApplications && allFilterCount == totalApplications;
				logger.info("[verifyEvaluationPaginationBehavior] <=10 branch paginationVisible=" + paginationVisible
						+ " rowCount=" + rowCount + " allFilterCount=" + allFilterCount + " result=" + result);
				return result;
			}

			if (!isElementPresent(EVALUATION_PAGINATION)) {
				logger.warn("[verifyEvaluationPaginationBehavior] Pagination is not visible even though totalApplications > 10");
				return false;
			}

			String activePage = getCurrentEvaluationPageLabel();
			if (!"1".equals(activePage)) {
				logger.warn("[verifyEvaluationPaginationBehavior] Expected to begin on page 1 but found page '" + activePage + "'");
				navigateToEvaluationPage("1");
			}

			int firstPageRowCount = getDisplayedApplicationRowCount();
			if (firstPageRowCount != 10) {
				logger.warn("[verifyEvaluationPaginationBehavior] Page 1 row count is not 10. actual=" + firstPageRowCount);
				return false;
			}

			if (!navigateToEvaluationPage("2")) {
				logger.warn("[verifyEvaluationPaginationBehavior] Failed to open page 2.");
				return false;
			}

			int secondPageRowCount = getDisplayedApplicationRowCount();
			if (secondPageRowCount <= 0) {
				logger.warn("[verifyEvaluationPaginationBehavior] Page 2 does not contain a first row.");
				return false;
			}

			List<String> pageNumbers = getEvaluationPageNumbers();
			String lastPage = pageNumbers.get(pageNumbers.size() - 1);
			if (!navigateToEvaluationPage(lastPage)) {
				logger.warn("[verifyEvaluationPaginationBehavior] Failed to open last page='" + lastPage + "'");
				return false;
			}

			int lastPageRowCount = getDisplayedApplicationRowCount();
			int allFilterCountOnLastPage = getCurrentPageApplicationsCountFromAllFilter();
			int expectedLastPageCount = totalApplications % 10 == 0 ? 10 : totalApplications % 10;
			boolean result = lastPageRowCount == allFilterCountOnLastPage && lastPageRowCount == expectedLastPageCount;
			logger.info("[verifyEvaluationPaginationBehavior] >10 branch lastPage='" + lastPage + "' lastPageRowCount="
					+ lastPageRowCount + " allFilterCountOnLastPage=" + allFilterCountOnLastPage
					+ " expectedLastPageCount=" + expectedLastPageCount + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyEvaluationPaginationBehavior] Failed while verifying evaluation pagination behavior.", e);
			return false;
		}
	}

	public boolean navigateToPageContainingApplication(String applicationName) {
		try {
			logger.info("[navigateToPageContainingApplication] START applicationName='" + applicationName + "'");
			List<String> pageNumbers = getEvaluationPageNumbers();
			if (pageNumbers.isEmpty()) {
				pageNumbers = Arrays.asList("1");
			}

			int pagesToScan = Math.min(pageNumbers.size(), MAX_EVALUATION_PAGES_TO_SCAN);
			for (int index = 0; index < pagesToScan; index++) {
				String targetPage = pageNumbers.get(index);
				if (!navigateToEvaluationPage(targetPage)) {
					logger.warn("[navigateToPageContainingApplication] Could not navigate to page='" + targetPage + "'");
					continue;
				}

				if (isApplicationPresentOnCurrentPage(applicationName)) {
					logger.info("[navigateToPageContainingApplication] Found application='" + applicationName
							+ "' on page='" + targetPage + "'");
					return true;
				}
			}

			logger.warn("[navigateToPageContainingApplication] Application='" + applicationName + "' was not found.");
			return false;
		} catch (Exception e) {
			logger.error("[navigateToPageContainingApplication] Failed while locating application='" + applicationName + "'", e);
			return false;
		}
	}

	public String getCurrentEvaluationPage() {
		return getCurrentEvaluationPageLabel();
	}

	public boolean openEvaluationPage(String targetPage) {
		return navigateToEvaluationPage(targetPage);
	}

	public String getLastEvaluationPageNumber() {
		try {
			waitForEvaluationPageDataToStabilize();
			List<String> pageNumbers = getEvaluationPageNumbers();
			String lastPageNumber = pageNumbers.isEmpty() ? "1" : pageNumbers.get(pageNumbers.size() - 1);
			logger.info("[getLastEvaluationPageNumber] lastPageNumber='" + lastPageNumber + "'");
			return lastPageNumber;
		} catch (Exception e) {
			logger.error("[getLastEvaluationPageNumber] Failed while getting the last evaluation page number.", e);
			return null;
		}
	}

	public int getExpectedLastEvaluationPageRowCount(int totalApplications) {
		int expectedLastPageCount = totalApplications <= 10
				? totalApplications
				: (totalApplications % 10 == 0 ? 10 : totalApplications % 10);
		logger.info("[getExpectedLastEvaluationPageRowCount] totalApplications=" + totalApplications
				+ " expectedLastPageCount=" + expectedLastPageCount);
		return expectedLastPageCount;
	}

	public String getApplicationStatusOnCurrentPage(String applicationName) {
		try {
			WebElement statusDropdown = getStatusDropdownOnCurrentPage(applicationName);
			String status = new Select(statusDropdown).getFirstSelectedOption().getText().trim();
			logger.info("[getApplicationStatusOnCurrentPage] applicationName='" + applicationName + "' status='" + status + "'");
			return status;
		} catch (Exception e) {
			logger.error("[getApplicationStatusOnCurrentPage] Failed while reading status for application='"
					+ applicationName + "'", e);
			return "";
		}
	}

	public Map<String, Integer> getCurrentPageStatusFilterCounts() {
		Map<String, Integer> counts = new HashMap<>();
		counts.put("All", extractFilterCount(ALL_FILTER, "All"));
		counts.put("Needs Review", extractFilterCount(NEEDS_REVIEW_FILTER, "Needs Review"));
		counts.put("On Hold", extractFilterCount(ON_HOLD_FILTER, "On Hold"));
		counts.put("Accepted", extractFilterCount(ACCEPTED_FILTER, "Accepted"));
		counts.put("Rejected", extractFilterCount(REJECTED_FILTER, "Rejected"));
		logger.info("[getCurrentPageStatusFilterCounts] counts=" + counts);
		return counts;
	}

	public Map<String, String> getCurrentPageApplicationStatuses() {
		Map<String, String> applicationStatuses = new HashMap<>();
		try {
			waitForEvaluationPageDataToStabilize();
			List<WebElement> rows = driver.findElements(APPLICATION_TABLE_ROWS);
			for (WebElement row : rows) {
				String applicationName = row.findElement(By.cssSelector("td:nth-child(2) span.td-value")).getText().trim();
				String status = new Select(row.findElement(By.cssSelector("td:nth-child(5) select[name='status']")))
						.getFirstSelectedOption()
						.getText()
						.trim();
				applicationStatuses.put(applicationName, status);
			}
			logger.info("[getCurrentPageApplicationStatuses] applicationStatuses=" + applicationStatuses);
		} catch (Exception e) {
			logger.error("[getCurrentPageApplicationStatuses] Failed while reading current page application statuses.", e);
		}
		return applicationStatuses;
	}

	public List<String> selectRandomApplicationNamesFromCurrentPage(int minSelections, int maxSelections) {
		try {
			Map<String, String> applicationStatuses = getCurrentPageApplicationStatuses();
			List<String> applicationNames = new java.util.ArrayList<>(applicationStatuses.keySet());
			Collections.shuffle(applicationNames);

			int selectionCount = randInt(minSelections, Math.min(maxSelections, applicationNames.size()));
			List<String> selected = new java.util.ArrayList<>(applicationNames.subList(0, selectionCount));
			logger.info("[selectRandomApplicationNamesFromCurrentPage] selectionCount=" + selectionCount
					+ " selected=" + selected);
			return selected;
		} catch (Exception e) {
			logger.error("[selectRandomApplicationNamesFromCurrentPage] Failed while selecting random applications.", e);
			return Collections.emptyList();
		}
	}

	public Map<String, Integer> buildExpectedCountsAfterSingleStatusChange(
			Map<String, Integer> baselineCounts,
			String fromStatus,
			String toStatus) {

		Map<String, Integer> expectedCounts = new HashMap<>(baselineCounts);
		if (!"All".equals(fromStatus)) {
			expectedCounts.put(fromStatus, expectedCounts.getOrDefault(fromStatus, 0) - 1);
		}
		if (!"All".equals(toStatus)) {
			expectedCounts.put(toStatus, expectedCounts.getOrDefault(toStatus, 0) + 1);
		}
		logger.info("[buildExpectedCountsAfterSingleStatusChange] baselineCounts=" + baselineCounts
				+ " fromStatus='" + fromStatus + "' toStatus='" + toStatus + "' expectedCounts=" + expectedCounts);
		return expectedCounts;
	}

	public boolean changeApplicationStatusOnCurrentPage(String applicationName, String newStatus) {
		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				WebElement statusDropdown = getStatusDropdownOnCurrentPage(applicationName);
				scrollScreen(statusDropdown);
				Select select = new Select(statusDropdown);
				String currentStatus = select.getFirstSelectedOption().getText().trim();
				logger.info("[changeApplicationStatusOnCurrentPage] attempt=" + attempt + " applicationName='"
						+ applicationName + "' currentStatus='" + currentStatus + "' newStatus='" + newStatus + "'");

				if (newStatus.equals(currentStatus)) {
					return true;
				}

				select.selectByVisibleText(newStatus);
				waitForPageAndAjaxToLoad();
				waitForNetworkIdle();
				return true;
			} catch (StaleElementReferenceException staleElementReferenceException) {
				logger.warn("[changeApplicationStatusOnCurrentPage] Stale element while updating status for application='"
						+ applicationName + "' on attempt=" + attempt);
			} catch (Exception e) {
				logger.error("[changeApplicationStatusOnCurrentPage] Failed on attempt=" + attempt + " for application='"
						+ applicationName + "'", e);
			}
		}
		return false;
	}

	public boolean changeApplicationStatusesOnCurrentPage(List<String> applicationNames, String newStatus) {
		try {
			for (String applicationName : applicationNames) {
				if (!changeApplicationStatusOnCurrentPage(applicationName, newStatus)) {
					logger.warn("[changeApplicationStatusesOnCurrentPage] Failed to update application='"
							+ applicationName + "' to newStatus='" + newStatus + "'");
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("[changeApplicationStatusesOnCurrentPage] Failed while changing multiple application statuses.", e);
			return false;
		}
	}

	public boolean isBulkActionDropdownEnabled() {
		try {
			WebElement bulkActionSelect = waitForElement(BULK_ACTION_SELECT);
			boolean enabled = bulkActionSelect != null && bulkActionSelect.isEnabled();
			logger.info("[isBulkActionDropdownEnabled] enabled=" + enabled);
			return enabled;
		} catch (Exception e) {
			logger.error("[isBulkActionDropdownEnabled] Failed while checking Bulk Action dropdown state.", e);
			return false;
		}
	}

	public boolean selectAllCurrentPageApplicationsUsingHeaderCheckbox() {
		try {
			waitForEvaluationPageDataToStabilize();
			WebElement headerCheckbox = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(HEADER_SELECT_ALL_CHECKBOX));
			if (!headerCheckbox.isSelected()) {
				scrollScreen(headerCheckbox);
				headerCheckbox.click();
			}
			boolean allSelected = new WebDriverWait(driver, Duration.ofSeconds(20)).until(d ->
					d.findElements(By.cssSelector("tbody tr.row td:first-child input[aria-label='checkbox']")).stream()
							.allMatch(WebElement::isSelected));
			logger.info("[selectAllCurrentPageApplicationsUsingHeaderCheckbox] allSelected=" + allSelected);
			return allSelected;
		} catch (Exception e) {
			logger.error("[selectAllCurrentPageApplicationsUsingHeaderCheckbox] Failed while selecting all current page applications.", e);
			return false;
		}
	}

	public boolean clearAllCurrentPageApplicationsUsingHeaderCheckbox() {
		try {
			waitForEvaluationPageDataToStabilize();
			WebElement headerCheckbox = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(HEADER_SELECT_ALL_CHECKBOX));
			if (headerCheckbox.isSelected()) {
				scrollScreen(headerCheckbox);
				headerCheckbox.click();
			}
			boolean allCleared = new WebDriverWait(driver, Duration.ofSeconds(20)).until(d ->
					d.findElements(By.cssSelector("tbody tr.row td:first-child input[aria-label='checkbox']")).stream()
							.noneMatch(WebElement::isSelected));
			logger.info("[clearAllCurrentPageApplicationsUsingHeaderCheckbox] allCleared=" + allCleared);
			return allCleared;
		} catch (Exception e) {
			logger.error("[clearAllCurrentPageApplicationsUsingHeaderCheckbox] Failed while clearing all current page applications.", e);
			return false;
		}
	}

	public String selectRandomApplicationNameFromCurrentPage() {
		try {
			Map<String, String> statuses = getCurrentPageApplicationStatuses();
			List<String> applicationNames = new java.util.ArrayList<>(statuses.keySet());
			if (applicationNames.isEmpty()) {
				logger.warn("[selectRandomApplicationNameFromCurrentPage] No application names are available on the current page.");
				return null;
			}
			String applicationName = applicationNames.get(randInt(0, applicationNames.size() - 1));
			logger.info("[selectRandomApplicationNameFromCurrentPage] applicationName='" + applicationName + "'");
			return applicationName;
		} catch (Exception e) {
			logger.error("[selectRandomApplicationNameFromCurrentPage] Failed while selecting a random application name from the current page.", e);
			return null;
		}
	}

	public boolean openApplicationFromStartupNameOnCurrentPage(String applicationName) {
		try {
			waitForEvaluationPageDataToStabilize();
			String previousUrl = driver.getCurrentUrl();
			WebElement row = findApplicationRowOnCurrentPage(applicationName);
			WebElement startupNameLink = row.findElement(By.cssSelector("td:nth-child(2) span.td-value"));
			scrollScreen(startupNameLink);
			clickAndDraw(startupNameLink);
			new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(d -> !d.getCurrentUrl().equals(previousUrl)
							|| !d.findElements(APPLICATION_DETAILS_LIST_LINK).isEmpty()
							|| !d.findElements(APPLICATION_DETAILS_MY_RATING_TAB).isEmpty());
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[openApplicationFromStartupNameOnCurrentPage] Opened application from startup name='"
					+ applicationName + "'");
			return true;
		} catch (Exception e) {
			logger.error("[openApplicationFromStartupNameOnCurrentPage] Failed while opening application from startup name='"
					+ applicationName + "'", e);
			return false;
		}
	}

	public boolean verifySelectedApplicationDetailsPageDisplayed(String applicationName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			String currentUrl = driver.getCurrentUrl();
			WebElement applicationListLink = wait.until(ExpectedConditions.visibilityOfElementLocated(APPLICATION_DETAILS_LIST_LINK));
			WebElement myRatingTab = wait.until(ExpectedConditions.visibilityOfElementLocated(APPLICATION_DETAILS_MY_RATING_TAB));
			WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(APPLICATION_DETAILS_SUBMIT_BUTTON));
			By selectedApplicationNameLocator = By.xpath("//*[normalize-space()='" + applicationName + "']");
			WebElement selectedApplicationName = wait.until(ExpectedConditions.visibilityOfElementLocated(selectedApplicationNameLocator));
			boolean result = applicationListLink.isDisplayed()
					&& myRatingTab.isDisplayed()
					&& submitButton.isDisplayed()
					&& selectedApplicationName.isDisplayed()
					&& !applicationName.isBlank();
			logger.info("[verifySelectedApplicationDetailsPageDisplayed] applicationName='" + applicationName
					+ "' currentUrl='" + currentUrl
					+ "' applicationListDisplayed=" + applicationListLink.isDisplayed()
					+ " myRatingDisplayed=" + myRatingTab.isDisplayed()
					+ " submitButtonDisplayed=" + submitButton.isDisplayed()
					+ " selectedApplicationNameDisplayed=" + selectedApplicationName.isDisplayed()
					+ " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifySelectedApplicationDetailsPageDisplayed] Failed while verifying the selected application details page.", e);
			return false;
		}
	}

	public boolean selectApplicationsIndividuallyInRandomOrder(List<String> applicationNames) {
		try {
			List<String> randomizedApplicationNames = new java.util.ArrayList<>(applicationNames);
			Collections.shuffle(randomizedApplicationNames);

			for (String applicationName : randomizedApplicationNames) {
				WebElement row = findApplicationRowOnCurrentPage(applicationName);
				WebElement checkbox = row.findElement(By.cssSelector("td:first-child input[aria-label='checkbox']"));
				if (!checkbox.isSelected()) {
					scrollScreen(checkbox);
					checkbox.click();
				}
			}

			boolean allSelected = randomizedApplicationNames.stream()
					.allMatch(applicationName -> findApplicationRowOnCurrentPage(applicationName)
							.findElement(By.cssSelector("td:first-child input[aria-label='checkbox']")).isSelected());
			logger.info("[selectApplicationsIndividuallyInRandomOrder] randomizedApplicationNames="
					+ randomizedApplicationNames + " allSelected=" + allSelected);
			return allSelected;
		} catch (Exception e) {
			logger.error("[selectApplicationsIndividuallyInRandomOrder] Failed while selecting applications individually in random order.", e);
			return false;
		}
	}

	public boolean applyBulkAction(String actionLabel) {
		try {
			WebElement bulkActionSelect = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(BULK_ACTION_SELECT));
			if (!bulkActionSelect.isEnabled()) {
				logger.warn("[applyBulkAction] Bulk Action dropdown is disabled for actionLabel='" + actionLabel + "'");
				return false;
			}

			scrollScreen(bulkActionSelect);
			new Select(bulkActionSelect).selectByVisibleText(actionLabel);
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[applyBulkAction] Applied actionLabel='" + actionLabel + "' successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[applyBulkAction] Failed while applying actionLabel='" + actionLabel + "'", e);
			return false;
		}
	}

	public boolean waitForApplicationStatusAndFilterCounts(
			String applicationName,
			String expectedPageLabel,
			String expectedStatus,
			Map<String, Integer> expectedCounts) {
		try {
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();

			boolean stabilized = new WebDriverWait(driver, Duration.ofSeconds(90)).until(d -> {
				String activePageOne = getCurrentEvaluationPageLabel();
				String statusOne = getApplicationStatusOnCurrentPage(applicationName);
				Map<String, Integer> countsOne = getCurrentPageStatusFilterCounts();

				try {
					Thread.sleep(2000);
				} catch (InterruptedException interruptedException) {
					Thread.currentThread().interrupt();
				}

				String activePageTwo = getCurrentEvaluationPageLabel();
				String statusTwo = getApplicationStatusOnCurrentPage(applicationName);
				Map<String, Integer> countsTwo = getCurrentPageStatusFilterCounts();

				boolean countsValid = expectedCounts.equals(countsOne) && expectedCounts.equals(countsTwo);
				boolean rowValid = expectedStatus.equals(statusOne) && expectedStatus.equals(statusTwo);
				boolean pageStable = expectedPageLabel.equals(activePageOne) && expectedPageLabel.equals(activePageTwo);
				boolean sumsMatch = countsTwo.getOrDefault("Needs Review", 0)
						+ countsTwo.getOrDefault("On Hold", 0)
						+ countsTwo.getOrDefault("Accepted", 0)
						+ countsTwo.getOrDefault("Rejected", 0)
						== countsTwo.getOrDefault("All", -1);
				boolean result = pageStable && rowValid && countsValid && sumsMatch;

				logger.info("[waitForApplicationStatusAndFilterCounts] applicationName='" + applicationName
						+ "' expectedPageLabel='" + expectedPageLabel + "' activePageOne='" + activePageOne
						+ "' activePageTwo='" + activePageTwo + "' expectedStatus='" + expectedStatus
						+ "' statusOne='" + statusOne + "' statusTwo='" + statusTwo + "' expectedCounts="
						+ expectedCounts + " countsOne=" + countsOne + " countsTwo=" + countsTwo
						+ " sumsMatch=" + sumsMatch + " result=" + result);
				return result;
			});

			logger.info("[waitForApplicationStatusAndFilterCounts] END result=" + stabilized);
			return stabilized;
		} catch (Exception e) {
			logger.error("[waitForApplicationStatusAndFilterCounts] Failed while waiting for application='"
					+ applicationName + "' status/filter stabilization.", e);
			return false;
		}
	}

	public boolean waitForCurrentPageStatusDistribution(
			String expectedPageLabel,
			Map<String, Integer> expectedCounts,
			List<String> targetApplicationNames,
			String targetStatus,
			String nonTargetStatus) {
		try {
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();

			boolean stabilized = new WebDriverWait(driver, Duration.ofSeconds(90)).until(d -> {
				String activePageOne = getCurrentEvaluationPageLabel();
				Map<String, String> statusesOne = getCurrentPageApplicationStatuses();
				Map<String, Integer> countsOne = getCurrentPageStatusFilterCounts();

				try {
					Thread.sleep(2000);
				} catch (InterruptedException interruptedException) {
					Thread.currentThread().interrupt();
				}

				String activePageTwo = getCurrentEvaluationPageLabel();
				Map<String, String> statusesTwo = getCurrentPageApplicationStatuses();
				Map<String, Integer> countsTwo = getCurrentPageStatusFilterCounts();

				boolean targetsValid = targetApplicationNames.stream()
						.allMatch(name -> targetStatus.equals(statusesOne.get(name)) && targetStatus.equals(statusesTwo.get(name)));
				boolean nonTargetsValid = statusesOne.entrySet().stream()
						.filter(entry -> !targetApplicationNames.contains(entry.getKey()))
						.allMatch(entry -> nonTargetStatus.equals(entry.getValue()))
						&& statusesTwo.entrySet().stream()
								.filter(entry -> !targetApplicationNames.contains(entry.getKey()))
								.allMatch(entry -> nonTargetStatus.equals(entry.getValue()));
				boolean countsValid = expectedCounts.equals(countsOne) && expectedCounts.equals(countsTwo);
				boolean pageStable = expectedPageLabel.equals(activePageOne) && expectedPageLabel.equals(activePageTwo);
				boolean rowCountStable = statusesOne.size() == 10 && statusesTwo.size() == 10;
				boolean sumsMatch = countsTwo.getOrDefault("Needs Review", 0)
						+ countsTwo.getOrDefault("On Hold", 0)
						+ countsTwo.getOrDefault("Accepted", 0)
						+ countsTwo.getOrDefault("Rejected", 0)
						== countsTwo.getOrDefault("All", -1);
				boolean result = pageStable && rowCountStable && targetsValid && nonTargetsValid && countsValid && sumsMatch;

				logger.info("[waitForCurrentPageStatusDistribution] expectedPageLabel='" + expectedPageLabel
						+ "' activePageOne='" + activePageOne + "' activePageTwo='" + activePageTwo
						+ "' targetApplicationNames=" + targetApplicationNames + " targetStatus='" + targetStatus
						+ "' nonTargetStatus='" + nonTargetStatus + "' statusesOne=" + statusesOne
						+ " statusesTwo=" + statusesTwo + " expectedCounts=" + expectedCounts
						+ " countsOne=" + countsOne + " countsTwo=" + countsTwo + " rowCountStable="
						+ rowCountStable + " sumsMatch=" + sumsMatch + " result=" + result);
				return result;
			});

			logger.info("[waitForCurrentPageStatusDistribution] END result=" + stabilized);
			return stabilized;
		} catch (Exception e) {
			logger.error("[waitForCurrentPageStatusDistribution] Failed while waiting for current page status distribution.", e);
			return false;
		}
	}

	public boolean verifyStatusConfirmationModal(String expectedTitle, String expectedBodyText) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
			wait.until(ExpectedConditions.visibilityOfElementLocated(STATUS_CONFIRMATION_MODAL));

			boolean result = wait.until(d -> {
				String actualTitle = d.findElement(STATUS_CONFIRMATION_MODAL_TITLE).getText().trim();
				String actualBodyText = d.findElement(STATUS_CONFIRMATION_MODAL_BODY).getText().trim();
				boolean noThanksVisible = isElementPresent(By.xpath(
						"//div[contains(@class,'modal-wrapper')]//button[normalize-space()='No Thanks']"));
				boolean yesSendEmailVisible = isElementPresent(By.xpath(
						"//div[contains(@class,'modal-wrapper')]//button[normalize-space()='Yes, Send Email']"));
				boolean modalReady = !actualTitle.isEmpty() && !actualBodyText.isEmpty() && noThanksVisible && yesSendEmailVisible;
				boolean textMatches = expectedTitle.equals(actualTitle)
						&& normalizeModalText(expectedBodyText).equals(normalizeModalText(actualBodyText));

				logger.info("[verifyStatusConfirmationModal] expectedTitle='" + expectedTitle + "' actualTitle='"
						+ actualTitle + "' expectedBodyText='" + expectedBodyText + "' actualBodyText='"
						+ actualBodyText + "' noThanksVisible=" + noThanksVisible + " yesSendEmailVisible="
						+ yesSendEmailVisible + " modalReady=" + modalReady + " textMatches=" + textMatches);
				return modalReady && textMatches;
			});

			logger.info("[verifyStatusConfirmationModal] END result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyStatusConfirmationModal] Failed while verifying the status confirmation modal.", e);
			return false;
		}
	}

	private String normalizeModalText(String text) {
		return text == null ? "" : text.trim().replaceAll("\\s+", " ");
	}

	public boolean chooseStatusConfirmationModalAction(String buttonLabel) {
		try {
			By buttonLocator = By.xpath("//div[contains(@class,'modal-wrapper')]//button[normalize-space()='" + buttonLabel + "']");
			WebElement button = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(buttonLocator));
			scrollScreen(button);
			button.click();
			if ("Yes, Send Email".equals(buttonLabel)) {
				new WebDriverWait(driver, Duration.ofSeconds(20))
						.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_COMPOSER_MODAL));
			} else {
				new WebDriverWait(driver, Duration.ofSeconds(20))
						.until(ExpectedConditions.invisibilityOfElementLocated(STATUS_CONFIRMATION_MODAL));
			}
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[chooseStatusConfirmationModalAction] Clicked buttonLabel='" + buttonLabel + "' successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[chooseStatusConfirmationModalAction] Failed while choosing buttonLabel='" + buttonLabel + "'", e);
			return false;
		}
	}

	public boolean closeStatusConfirmationModal() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOfElementLocated(STATUS_CONFIRMATION_MODAL));
			WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(EMAIL_COMPOSER_CLOSE_BUTTON));
			scrollScreen(closeButton);
			closeButton.click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(STATUS_CONFIRMATION_MODAL));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[closeStatusConfirmationModal] Closed the status confirmation modal successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[closeStatusConfirmationModal] Failed while closing the status confirmation modal.", e);
			return false;
		}
	}

	public boolean verifyAcceptanceEmailComposerModal() {
		return verifyEmailComposerModal("Acceptance Email");
	}

	public boolean verifyRejectionEmailComposerModal() {
		return verifyEmailComposerModal("Rejection Email");
	}

	public boolean verifyGenericEmailComposerModal() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_COMPOSER_MODAL));

			String actualTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_COMPOSER_MODAL_TITLE))
					.getText()
					.trim();
			boolean recipientVisible = !driver.findElements(By.cssSelector("div.modal-wrapper span.recipient-name")).isEmpty();
			boolean ccVisible = !driver.findElements(By.cssSelector("div.modal-wrapper input.cc-input")).isEmpty();
			WebElement templateSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_TEMPLATE_SELECT));
			String selectedTemplate = new Select(templateSelect).getFirstSelectedOption().getText().trim();
			WebElement subjectInput = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SUBJECT_INPUT));
			String subject = subjectInput.getAttribute("value") == null ? "" : subjectInput.getAttribute("value").trim();
			WebElement bodyEditor = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_BODY_EDITOR));
			String bodyText = bodyEditor.getText() == null ? "" : bodyEditor.getText().trim();
			boolean sendVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SEND_BUTTON)).isDisplayed();

			boolean result = "Send Email".equals(actualTitle)
					&& recipientVisible
					&& ccVisible
					&& "Select Option".equals(selectedTemplate)
					&& subject.isEmpty()
					&& bodyText.isEmpty()
					&& sendVisible;

			logger.info("[verifyGenericEmailComposerModal] actualTitle='" + actualTitle + "' recipientVisible="
					+ recipientVisible + " ccVisible=" + ccVisible + " selectedTemplate='" + selectedTemplate
					+ "' subjectEmpty=" + subject.isEmpty() + " bodyEmpty=" + bodyText.isEmpty()
					+ " sendVisible=" + sendVisible + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyGenericEmailComposerModal] Failed while verifying the generic email composer modal.", e);
			return false;
		}
	}

	public boolean verifyBulkEmailComposerModal() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_COMPOSER_MODAL));

			String actualTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_COMPOSER_MODAL_TITLE))
					.getText()
					.trim();
			List<WebElement> recipientElements = driver.findElements(By.cssSelector("div.modal-wrapper span.recipient-name"));
			String toRecipientsText = recipientElements.isEmpty() ? "" : recipientElements.get(0).getText().trim();
			boolean multipleRecipientsVisible = toRecipientsText.contains(",");
			boolean ccVisible = !driver.findElements(By.cssSelector("div.modal-wrapper input.cc-input")).isEmpty();
			WebElement templateSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_TEMPLATE_SELECT));
			String selectedTemplate = new Select(templateSelect).getFirstSelectedOption().getText().trim();
			WebElement subjectInput = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SUBJECT_INPUT));
			String subject = subjectInput.getAttribute("value") == null ? "" : subjectInput.getAttribute("value").trim();
			WebElement bodyEditor = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_BODY_EDITOR));
			String bodyText = bodyEditor.getText() == null ? "" : bodyEditor.getText().trim();
			boolean sendVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SEND_BUTTON)).isDisplayed();

			boolean result = "Send Email".equals(actualTitle)
					&& multipleRecipientsVisible
					&& ccVisible
					&& "Select Option".equals(selectedTemplate)
					&& subject.isEmpty()
					&& bodyText.isEmpty()
					&& sendVisible;

			logger.info("[verifyBulkEmailComposerModal] actualTitle='" + actualTitle + "' multipleRecipientsVisible="
					+ multipleRecipientsVisible + " ccVisible=" + ccVisible + " selectedTemplate='" + selectedTemplate
					+ "' subjectEmpty=" + subject.isEmpty() + " bodyEmpty=" + bodyText.isEmpty()
					+ " sendVisible=" + sendVisible + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyBulkEmailComposerModal] Failed while verifying the bulk email composer modal.", e);
			return false;
		}
	}

	private boolean verifyEmailComposerModal(String expectedTemplate) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_COMPOSER_MODAL));

			String actualTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_COMPOSER_MODAL_TITLE))
					.getText()
					.trim();
			String selectedTemplate = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_TEMPLATE_SELECT)))
					.getFirstSelectedOption()
					.getText()
					.trim();
			String subject = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SUBJECT_INPUT))
					.getAttribute("value")
					.trim();
			String bodyText = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_BODY_EDITOR))
					.getText()
					.trim();
			boolean sendVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SEND_BUTTON)).isDisplayed();

			boolean result = "Send Email".equals(actualTitle)
					&& expectedTemplate.equals(selectedTemplate)
					&& !subject.isEmpty()
					&& !bodyText.isEmpty()
					&& sendVisible;

			logger.info("[verifyEmailComposerModal] expectedTemplate='" + expectedTemplate + "' actualTitle='" + actualTitle + "' selectedTemplate='"
					+ selectedTemplate + "' subjectEmpty=" + subject.isEmpty() + " bodyEmpty=" + bodyText.isEmpty()
					+ " sendVisible=" + sendVisible + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyEmailComposerModal] Failed while verifying the email composer modal.", e);
			return false;
		}
	}

	public boolean sendAcceptanceEmailAndVerifyConfirmation() {
		return sendEmailAndVerifyConfirmation("acceptance");
	}

	public boolean sendRejectionEmailAndVerifyConfirmation() {
		return sendEmailAndVerifyConfirmation("rejection");
	}

	public boolean sendGenericEmailAndVerifyConfirmation() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement subjectInput = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SUBJECT_INPUT));
			String subject = subjectInput.getAttribute("value") == null ? "" : subjectInput.getAttribute("value").trim();
			if (subject.isEmpty()) {
				subjectInput.clear();
				subjectInput.sendKeys("Application follow-up");
			}

			WebElement bodyEditor = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_BODY_EDITOR));
			String bodyText = bodyEditor.getText() == null ? "" : bodyEditor.getText().trim();
			if (bodyText.isEmpty()) {
				scrollScreen(bodyEditor);
				bodyEditor.click();
				bodyEditor.sendKeys("Hello, this is a test email sent from the application row email action.");
			}

			return sendEmailAndVerifyConfirmation("generic row");
		} catch (Exception e) {
			logger.error("[sendGenericEmailAndVerifyConfirmation] Failed while preparing and sending the generic row email.", e);
			return false;
		}
	}

	private boolean sendEmailAndVerifyConfirmation(String flowType) {
		try {
			WebElement sendButton = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(EMAIL_SEND_BUTTON));
			scrollScreen(sendButton);
			sendButton.click();

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SENT_CONFIRMATION));
			String confirmationText = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SENT_CONFIRMATION_TEXT))
					.getText()
					.trim();
			boolean closeVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_SENT_CONFIRMATION_CLOSE))
					.isDisplayed();
			boolean result = "Email Sent!".equals(confirmationText) && closeVisible;

			logger.info("[sendEmailAndVerifyConfirmation] flowType='" + flowType + "' confirmationText='" + confirmationText
					+ "' closeVisible=" + closeVisible + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[sendEmailAndVerifyConfirmation] Failed while sending the " + flowType + " email.", e);
			return false;
		}
	}

	public boolean closeEmailSentConfirmationModal() {
		try {
			WebElement closeButton = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(EMAIL_SENT_CONFIRMATION_CLOSE));
			scrollScreen(closeButton);
			closeButton.click();
			new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.invisibilityOfElementLocated(STATUS_CONFIRMATION_MODAL));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[closeEmailSentConfirmationModal] Closed the email sent confirmation modal successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[closeEmailSentConfirmationModal] Failed while closing the email sent confirmation modal.", e);
			return false;
		}
	}

	public boolean closeGenericEmailComposerModal() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_COMPOSER_MODAL));

			WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(EMAIL_COMPOSER_CLOSE_BUTTON));
			scrollScreen(closeButton);
			closeButton.click();

			wait.until(ExpectedConditions.invisibilityOfElementLocated(EMAIL_COMPOSER_MODAL));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[closeGenericEmailComposerModal] Closed the email composer modal successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[closeGenericEmailComposerModal] Failed while closing the email composer modal.", e);
			return false;
		}
	}

	public boolean openRowEmailComposerOnCurrentPage(String applicationName) {
		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				waitForEvaluationPageDataToStabilize();
				WebElement row = findApplicationRowOnCurrentPage(applicationName);
				WebElement emailAction = row.findElement(By.cssSelector("td.td-actions span.table-action-email"));
				scrollScreen(emailAction);
				emailAction.click();

				new WebDriverWait(driver, Duration.ofSeconds(20))
						.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_COMPOSER_MODAL));
				waitForPageAndAjaxToLoad();
				waitForNetworkIdle();
				logger.info("[openRowEmailComposerOnCurrentPage] Opened row email composer successfully for applicationName='"
						+ applicationName + "' on attempt=" + attempt);
				return true;
			} catch (StaleElementReferenceException staleElementReferenceException) {
				logger.warn("[openRowEmailComposerOnCurrentPage] Stale element while opening row email composer for applicationName='"
						+ applicationName + "' on attempt=" + attempt);
			} catch (Exception e) {
				logger.error("[openRowEmailComposerOnCurrentPage] Failed on attempt=" + attempt + " for applicationName='"
						+ applicationName + "'", e);
			}
		}
		return false;
	}

	private void clearOldZipDownloads() {
		try {
			int deletedCount = 0;
			for (File downloadsFolder : getDownloadFolders()) {
				downloadsFolder.mkdirs();
				File[] existingFiles = downloadsFolder.listFiles((dir, name) -> {
					String lowerName = name.toLowerCase(Locale.ROOT);
					return lowerName.endsWith(".zip") || lowerName.endsWith(".tmp") || lowerName.equals("datafiles");
				});
				if (existingFiles != null) {
					for (File file : existingFiles) {
						deletePathRecursively(file.toPath());
						deletedCount++;
					}
				}
				logger.info("[clearOldZipDownloads] Cleared old export artifacts from '" + downloadsFolder.getAbsolutePath() + "'");
			}
			logger.info("[clearOldZipDownloads] Total deleted export artifact count=" + deletedCount);
		} catch (Exception e) {
			logger.error("[clearOldZipDownloads] Failed to clear old export artifacts before export.", e);
		}
	}

	private File waitForDownloadedZip(String targetProgramName) {
		try {
			List<File> downloadFolders = getDownloadFolders();
			Map<String, Long> lastObservedTmpSizes = new HashMap<>();
			Map<String, Integer> stableTmpPollCounts = new HashMap<>();

			for (File folder : downloadFolders) {
				folder.mkdirs();
				logger.info("[waitForDownloadedZip] Watching folder='" + folder.getAbsolutePath()
				+ "' for zip containing targetProgramName='" + targetProgramName + "'");
			}

			return new WebDriverWait(driver, Duration.ofSeconds(60)).until(d -> {
				File newestZip = null;
				for (File folder : downloadFolders) {
					logFolderContents(folder);
					File[] zipFiles = folder.listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".zip"));
					if (zipFiles != null && zipFiles.length > 0) {
						File candidate = Arrays.stream(zipFiles)
								.filter(File::isFile)
								.filter(file -> fileNameContainsTargetProgram(file.getName(), targetProgramName))
								.max(Comparator.comparingLong(File::lastModified))
								.orElse(null);

						if (candidate != null
								&& (newestZip == null || candidate.lastModified() > newestZip.lastModified())) {
							newestZip = candidate;
						}
					}
				}

				if (newestZip != null) {
					try {
						long size = Files.size(newestZip.toPath());
						logger.info("[waitForDownloadedZip] Candidate zip='" + newestZip.getName() + "' size=" + size);
						if (size > 0) {
							File movedZip = moveZipToPreferredFolderIfNeeded(newestZip);
							return isReadableZip(movedZip) ? movedZip : null;
						}
					} catch (Exception sizeException) {
						logger.warn("[waitForDownloadedZip] Could not read zip size yet for '" + newestZip.getName() + "'");
					}
				}

				File stableTmpZip = findStableTmpAsZip(downloadFolders, targetProgramName, lastObservedTmpSizes,
						stableTmpPollCounts);
				if (stableTmpZip != null) {
					return stableTmpZip;
				}

				File archivedDataFilesZip = createZipFromDownloadedDataFiles(downloadFolders, targetProgramName);
				if (archivedDataFilesZip != null) {
					return archivedDataFilesZip;
				}

				return null;
			});
		} catch (Exception e) {
			logger.error("[waitForDownloadedZip] Timed out waiting for downloaded zip for targetProgramName='"
					+ targetProgramName + "'", e);
			return null;
		}
	}

	private File findStableTmpAsZip(List<File> downloadFolders, String targetProgramName, Map<String, Long> lastObservedTmpSizes,
			Map<String, Integer> stableTmpPollCounts) {
		try {
			for (File folder : downloadFolders) {
				File[] tmpFiles = folder.listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".tmp"));
				if (tmpFiles == null || tmpFiles.length == 0) {
					continue;
				}

				for (File tmpFile : tmpFiles) {
					if (!tmpFile.isFile()) {
						continue;
					}

					long currentSize = Files.size(tmpFile.toPath());
					if (currentSize <= 0) {
						continue;
					}

					String key = tmpFile.getAbsolutePath();
					Long previousSize = lastObservedTmpSizes.get(key);
					int stablePollCount = previousSize != null && previousSize == currentSize
							? stableTmpPollCounts.getOrDefault(key, 0) + 1
									: 0;

					lastObservedTmpSizes.put(key, currentSize);
					stableTmpPollCounts.put(key, stablePollCount);

					logger.info("[findStableTmpAsZip] tmpFile='" + key + "' size=" + currentSize
							+ " stablePollCount=" + stablePollCount);

					if (stablePollCount < 3) {
						continue;
					}

					File copiedZip = copyTmpFileToZip(tmpFile, targetProgramName);
					if (copiedZip != null) {
						return copiedZip;
					}
				}
			}
		} catch (Exception e) {
			logger.error("[findStableTmpAsZip] Failed while checking for stable temp downloads.", e);
		}
		return null;
	}

	private File copyTmpFileToZip(File tmpFile, String targetProgramName) {
		try {
			File preferredFolder = getDownloadsFolder();
			preferredFolder.mkdirs();

			File copiedZip = new File(preferredFolder, buildZipFileName(targetProgramName));
			Files.copy(tmpFile.toPath(), copiedZip.toPath(), StandardCopyOption.REPLACE_EXISTING);
			logger.info("[copyTmpFileToZip] Copied tmp file '" + tmpFile.getAbsolutePath() + "' to zip '"
					+ copiedZip.getAbsolutePath() + "'");

			if (isReadableZip(copiedZip)) {
				mirrorZipToCurrentReportDownloadsFolder(copiedZip);
				return copiedZip;
			}

			FileDeleteStrategy.FORCE.delete(copiedZip);
			logger.warn("[copyTmpFileToZip] Copied tmp file was not a readable zip yet: " + copiedZip.getAbsolutePath());
			return null;
		} catch (Exception e) {
			logger.error("[copyTmpFileToZip] Failed to copy tmp file into zip form.", e);
			return null;
		}
	}

	private File createZipFromDownloadedDataFiles(List<File> downloadFolders, String targetProgramName) {
		try {
			for (File folder : downloadFolders) {
				File dataFilesFolder = new File(folder, "DataFiles");
				if (!dataFilesFolder.exists() || !dataFilesFolder.isDirectory()) {
					continue;
				}

				File[] requiredCsvFiles = findExpectedCsvFiles(dataFilesFolder);
				boolean hasAllExpectedCsvs = requiredCsvFiles[0] != null && requiredCsvFiles[1] != null;
				logger.info("[createZipFromDownloadedDataFiles] dataFilesFolder='" + dataFilesFolder.getAbsolutePath()
				+ "' hasAllExpectedCsvs=" + hasAllExpectedCsvs);

				if (!hasAllExpectedCsvs) {
					continue;
				}

				File preferredFolder = getDownloadsFolder();
				preferredFolder.mkdirs();
				File archivedZip = new File(preferredFolder, buildZipFileName(targetProgramName));
				zipDirectoryContents(dataFilesFolder.toPath(), archivedZip.toPath());
				mirrorZipToCurrentReportDownloadsFolder(archivedZip);
				logger.info("[createZipFromDownloadedDataFiles] Created zip from DataFiles folder='"
						+ archivedZip.getAbsolutePath() + "'");
				return archivedZip;
			}
		} catch (Exception e) {
			logger.error("[createZipFromDownloadedDataFiles] Failed while creating zip from DataFiles folder.", e);
		}
		return null;
	}

	private File[] findExpectedCsvFiles(File rootFolder) {
		File applicantSummaries = null;
		File scoreDetails = null;
		File[] files = rootFolder.listFiles();
		if (files == null) {
			return new File[] { null, null };
		}

		for (File file : files) {
			if (file.isDirectory()) {
				File[] nestedResult = findExpectedCsvFiles(file);
				if (applicantSummaries == null) {
					applicantSummaries = nestedResult[0];
				}
				if (scoreDetails == null) {
					scoreDetails = nestedResult[1];
				}
			} else if (file.getName().endsWith("Applicant_Summaries.csv")) {
				applicantSummaries = file;
			} else if (file.getName().endsWith("Score_Details.csv")) {
				scoreDetails = file;
			}
		}

		return new File[] { applicantSummaries, scoreDetails };
	}

	private void zipDirectoryContents(Path sourceFolder, Path targetZip) throws IOException {
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(targetZip))) {
			Files.walk(sourceFolder)
			.filter(Files::isRegularFile)
			.forEach(path -> {
				try {
					ZipEntry zipEntry = new ZipEntry(sourceFolder.relativize(path).toString().replace("\\", "/"));
					zipOutputStream.putNextEntry(zipEntry);
					Files.copy(path, zipOutputStream);
					zipOutputStream.closeEntry();
				} catch (IOException ioException) {
					throw new RuntimeException(ioException);
				}
			});
		} catch (RuntimeException runtimeException) {
			if (runtimeException.getCause() instanceof IOException) {
				throw (IOException) runtimeException.getCause();
			}
			throw runtimeException;
		}
	}

	private boolean isReadableZip(File zipFile) {
		try (ZipFile ignored = new ZipFile(zipFile)) {
			logger.info("[isReadableZip] Readable zip confirmed: " + zipFile.getAbsolutePath());
			return true;
		} catch (Exception e) {
			logger.info("[isReadableZip] File is not a readable zip yet: " + zipFile.getAbsolutePath());
			return false;
		}
	}

	private String buildZipFileName(String targetProgramName) {
		String sanitizedName = targetProgramName == null ? "export"
				: targetProgramName.replaceAll("[\\\\/:*?\"<>|]", "").trim().replaceAll("\\s+", "_");
		return sanitizedName + "_" + System.currentTimeMillis() + ".zip";
	}

	private void deletePathRecursively(Path path) throws IOException {
		if (!Files.exists(path)) {
			return;
		}

		if (Files.isDirectory(path)) {
			File[] files = path.toFile().listFiles();
			if (files != null) {
				for (File child : files) {
					deletePathRecursively(child.toPath());
				}
			}
		}

		FileDeleteStrategy.FORCE.delete(path.toFile());
	}

	private File moveZipToPreferredFolderIfNeeded(File zipFile) {
		try {
			File preferredFolder = getDownloadsFolder();
			preferredFolder.mkdirs();

			if (zipFile.getParentFile().getAbsolutePath().equals(preferredFolder.getAbsolutePath())) {
				logger.info("[moveZipToPreferredFolderIfNeeded] Zip already in preferred folder: " + zipFile.getAbsolutePath());
				mirrorZipToCurrentReportDownloadsFolder(zipFile);
				return zipFile;
			}

			File movedFile = new File(preferredFolder, zipFile.getName());
			Files.move(zipFile.toPath(), movedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			mirrorZipToCurrentReportDownloadsFolder(movedFile);
			logger.info("[moveZipToPreferredFolderIfNeeded] Moved zip from '" + zipFile.getAbsolutePath()
			+ "' to '" + movedFile.getAbsolutePath() + "'");
			return movedFile;
		} catch (Exception e) {
			logger.error("[moveZipToPreferredFolderIfNeeded] Failed to move zip into preferred folder. Returning original file.",
					e);
			return zipFile;
		}
	}

	private boolean fileNameContainsTargetProgram(String fileName, String targetProgramName) {
		String normalizedFileName = normalizeForComparison(fileName);
		String normalizedTarget = normalizeForComparison(targetProgramName);
		boolean matches = normalizedFileName.contains(normalizedTarget);
		logger.info("[fileNameContainsTargetProgram] fileName='" + fileName + "' normalizedFileName='"
				+ normalizedFileName + "' normalizedTarget='" + normalizedTarget + "' matches=" + matches);
		return matches;
	}

	private String normalizeForComparison(String value) {
		return value == null ? "" : value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
	}

	private void logFolderContents(File folder) {
		try {
			File[] files = folder.listFiles();
			String contents = files == null || files.length == 0
					? "[]"
							: Arrays.stream(files)
							.sorted(Comparator.comparingLong(File::lastModified).reversed())
							.map(file -> file.getName() + "(" + file.length() + ")")
							.limit(10)
							.reduce((left, right) -> left + ", " + right)
							.orElse("[]");
			logger.info("[logFolderContents] folder='" + folder.getAbsolutePath() + "' contents=" + contents);
		} catch (Exception e) {
			logger.warn("[logFolderContents] Could not list folder contents for '" + folder.getAbsolutePath() + "'");
		}
	}

	private List<File> getDownloadFolders() {
		File preferredFolder = getDownloadsFolder();
		File fallbackFolder = new File(System.getProperty("user.home"), "Downloads");
		if (preferredFolder.getAbsolutePath().equalsIgnoreCase(fallbackFolder.getAbsolutePath())) {
			return Arrays.asList(preferredFolder);
		}
		return Arrays.asList(preferredFolder, fallbackFolder);
	}

	private File getDownloadsFolder() {
		return new File(Driver.downloadFolderPath);
	}

	private void mirrorZipToCurrentReportDownloadsFolder(File zipFile) {
		try {
			if (zipFile == null || !zipFile.exists() || !zipFile.isFile()) {
				return;
			}

			File reportDownloadsFolder = getCurrentReportDownloadsFolder();
			if (reportDownloadsFolder == null) {
				logger.warn("[mirrorZipToCurrentReportDownloadsFolder] Current report folder was not found. Skipping mirror for '"
						+ zipFile.getAbsolutePath() + "'");
				return;
			}

			reportDownloadsFolder.mkdirs();
			File mirroredZip = new File(reportDownloadsFolder, zipFile.getName());
			Files.copy(zipFile.toPath(), mirroredZip.toPath(), StandardCopyOption.REPLACE_EXISTING);
			logger.info("[mirrorZipToCurrentReportDownloadsFolder] Mirrored zip to '" + mirroredZip.getAbsolutePath() + "'");
		} catch (Exception e) {
			logger.error("[mirrorZipToCurrentReportDownloadsFolder] Failed while mirroring zip into the current report Downloads folder.",
					e);
		}
	}

	private File getCurrentReportDownloadsFolder() {
		try {
			File resultsRoot = new File(System.getProperty("user.dir"), "Results");
			if (!resultsRoot.exists() || !resultsRoot.isDirectory()) {
				return null;
			}

			File[] reportFolders = resultsRoot.listFiles(file -> file.isDirectory()
					&& file.getName().startsWith("test-report"));
			if (reportFolders == null || reportFolders.length == 0) {
				return null;
			}

			File latestReportFolder = Arrays.stream(reportFolders)
					.max(Comparator.comparingLong(File::lastModified))
					.orElse(null);
			return latestReportFolder == null ? null : new File(latestReportFolder, "Downloads");
		} catch (Exception e) {
			logger.error("[getCurrentReportDownloadsFolder] Failed while resolving the current report Downloads folder.", e);
			return null;
		}
	}

	private int extractFirstNumberFromElement(By locator, String sourceName) {
		try {
			WebElement element = waitForElement(locator);
			if (element == null) {
				logger.warn("[" + sourceName + "] Element not found for locator=" + locator);
				return -1;
			}

			String text = element.getText().trim();
			Matcher matcher = APPLICATION_COUNT_PATTERN.matcher(text);
			int value = matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
			logger.info("[" + sourceName + "] text='" + text + "' value=" + value);
			return value;
		} catch (Exception e) {
			logger.error("[" + sourceName + "] Failed while extracting numeric value.", e);
			return -1;
		}
	}

	private int extractAllFilterCount() {
		try {
			WebElement allFilter = waitForElement(ALL_FILTER);
			if (allFilter == null) {
				logger.warn("[extractAllFilterCount] All filter was not found.");
				return -1;
			}

			List<WebElement> spans = allFilter.findElements(By.tagName("span"));
			for (int i = spans.size() - 1; i >= 0; i--) {
				String spanText = spans.get(i).getText().trim();
				if (spanText.matches("\\d+")) {
					int value = Integer.parseInt(spanText);
					logger.info("[extractAllFilterCount] value=" + value);
					return value;
				}
			}
			logger.warn("[extractAllFilterCount] Could not find numeric value in All filter.");
			return -1;
		} catch (Exception e) {
			logger.error("[extractAllFilterCount] Failed while reading All filter count.", e);
			return -1;
		}
	}

	private int extractFilterCount(By filterLocator, String filterName) {
		try {
			WebElement filterElement = waitForElement(filterLocator);
			if (filterElement == null) {
				logger.warn("[extractFilterCount] Filter not found for filterName='" + filterName + "'");
				return -1;
			}

			List<WebElement> spans = filterElement.findElements(By.tagName("span"));
			for (int i = spans.size() - 1; i >= 0; i--) {
				String spanText = spans.get(i).getText().trim();
				if (spanText.matches("\\d+")) {
					int value = Integer.parseInt(spanText);
					logger.info("[extractFilterCount] filterName='" + filterName + "' value=" + value);
					return value;
				}
			}

			logger.warn("[extractFilterCount] Could not find numeric value for filterName='" + filterName + "'");
			return -1;
		} catch (Exception e) {
			logger.error("[extractFilterCount] Failed while reading filterName='" + filterName + "'", e);
			return -1;
		}
	}

	private By getEvaluationFilterLocator(String filterName) {
		switch (filterName) {
		case "All":
			return ALL_FILTER;
		case "Needs Review":
			return NEEDS_REVIEW_FILTER;
		case "On Hold":
			return ON_HOLD_FILTER;
		case "Accepted":
			return ACCEPTED_FILTER;
		case "Rejected":
			return REJECTED_FILTER;
		default:
			throw new IllegalArgumentException("Unsupported evaluation filter: " + filterName);
		}
	}

	public boolean clickEvaluationFilterTab(String filterName) {
		try {
			By filterLocator = getEvaluationFilterLocator(filterName);
			WebElement filterTab = new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.elementToBeClickable(filterLocator));
			scrollScreen(filterTab);
			clickAndDraw(filterTab);
			waitForEvaluationPageDataToStabilize();
			logger.info("[clickEvaluationFilterTab] Clicked filterName='" + filterName + "' successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[clickEvaluationFilterTab] Failed while clicking filterName='" + filterName + "'", e);
			return false;
		}
	}

	public boolean verifyDisplayedRowsMatchStatus(String expectedStatus, int expectedRowCount) {
		try {
			waitForEvaluationPageDataToStabilize();
			Map<String, String> applicationStatuses = getCurrentPageApplicationStatuses();
			boolean rowCountMatches = applicationStatuses.size() == expectedRowCount;
			boolean allStatusesMatch = applicationStatuses.values().stream().allMatch(expectedStatus::equals);
			boolean result = rowCountMatches && allStatusesMatch;
			logger.info("[verifyDisplayedRowsMatchStatus] expectedStatus='" + expectedStatus + "' expectedRowCount="
					+ expectedRowCount + " applicationStatuses=" + applicationStatuses + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyDisplayedRowsMatchStatus] Failed while verifying displayed rows for expectedStatus='"
					+ expectedStatus + "'", e);
			return false;
		}
	}

	public boolean verifyDisplayedRowCount(int expectedRowCount) {
		try {
			waitForEvaluationPageDataToStabilize();
			int actualRowCount = getDisplayedApplicationRowCount();
			boolean result = actualRowCount == expectedRowCount;
			logger.info("[verifyDisplayedRowCount] expectedRowCount=" + expectedRowCount + " actualRowCount="
					+ actualRowCount + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyDisplayedRowCount] Failed while verifying the displayed row count.", e);
			return false;
		}
	}

	public boolean waitForCurrentPageFilterCounts(String expectedPageLabel, Map<String, Integer> expectedCounts) {
		try {
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();

			boolean stabilized = new WebDriverWait(driver, Duration.ofSeconds(90)).until(d -> {
				String activePageOne = getCurrentEvaluationPageLabel();
				Map<String, Integer> countsOne = getCurrentPageStatusFilterCounts();

				try {
					Thread.sleep(2000);
				} catch (InterruptedException interruptedException) {
					Thread.currentThread().interrupt();
				}

				String activePageTwo = getCurrentEvaluationPageLabel();
				Map<String, Integer> countsTwo = getCurrentPageStatusFilterCounts();

				boolean countsValid = expectedCounts.equals(countsOne) && expectedCounts.equals(countsTwo);
				boolean pageStable = expectedPageLabel.equals(activePageOne) && expectedPageLabel.equals(activePageTwo);
				boolean sumsMatch = countsTwo.getOrDefault("Needs Review", 0)
						+ countsTwo.getOrDefault("On Hold", 0)
						+ countsTwo.getOrDefault("Accepted", 0)
						+ countsTwo.getOrDefault("Rejected", 0)
						== countsTwo.getOrDefault("All", -1);
				boolean result = pageStable && countsValid && sumsMatch;

				logger.info("[waitForCurrentPageFilterCounts] expectedPageLabel='" + expectedPageLabel
						+ "' activePageOne='" + activePageOne + "' activePageTwo='" + activePageTwo
						+ "' expectedCounts=" + expectedCounts + " countsOne=" + countsOne + " countsTwo=" + countsTwo
						+ " sumsMatch=" + sumsMatch + " result=" + result);
				return result;
			});

			logger.info("[waitForCurrentPageFilterCounts] END result=" + stabilized);
			return stabilized;
		} catch (Exception e) {
			logger.error("[waitForCurrentPageFilterCounts] Failed while waiting for the current page filter counts.", e);
			return false;
		}
	}

	private boolean navigateToEvaluationPage(String targetPage) {
		try {
			String currentPage = getCurrentEvaluationPageLabel();
			if (targetPage.equals(currentPage)) {
				logger.info("[navigateToEvaluationPage] Already on target page='" + targetPage + "'");
				waitForEvaluationPageDataToStabilize(targetPage);
				return true;
			}

			String previousSignature = buildEvaluationRowSignature();
			int previousRowCount = driver.findElements(APPLICATION_TABLE_ROWS).size();
			int previousAllFilterCount = extractAllFilterCount();
			logger.info("[navigateToEvaluationPage] Moving from page='" + currentPage + "' to page='" + targetPage + "'");
			clickEvaluationPageLink(targetPage);

			waitForEvaluationPageTransition(targetPage, previousSignature, previousRowCount, previousAllFilterCount);
			waitForEvaluationPageDataToStabilize(targetPage);
			logger.info("[navigateToEvaluationPage] Navigation complete. currentPage='" + getCurrentEvaluationPageLabel() + "'");
			return true;
		} catch (Exception e) {
			logger.error("[navigateToEvaluationPage] Failed while navigating to evaluation page='" + targetPage + "'", e);
			return false;
		}
	}

	private void clickEvaluationPageLink(String targetPage) {
		List<WebElement> pageLinks = driver.findElements(EVALUATION_PAGE_LINKS);
		for (WebElement pageLink : pageLinks) {
			String text = pageLink.getText().trim();
			if (targetPage.equals(text)) {
				scrollScreen(pageLink);
				try {
					pageLink.click();
					logger.info("[clickEvaluationPageLink] Native click succeeded for page='" + targetPage + "'");
				} catch (Exception nativeClickException) {
					logger.warn("[clickEvaluationPageLink] Native click failed for page='" + targetPage + "': "
							+ nativeClickException.getMessage());
					WebElement freshPageLink = driver.findElements(EVALUATION_PAGE_LINKS).stream()
							.filter(link -> targetPage.equals(link.getText().trim()))
							.findFirst()
							.orElseThrow(() -> new IllegalStateException("Page link not found after refresh for target page " + targetPage));
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", freshPageLink);
					logger.info("[clickEvaluationPageLink] JS click succeeded for page='" + targetPage + "'");
				}
				return;
			}
		}
		throw new IllegalStateException("Evaluation pagination page link not found for target page " + targetPage);
	}

	private void waitForEvaluationPageTransition(String targetPage, String previousSignature, int previousRowCount,
			int previousAllFilterCount) {
		waitForPageAndAjaxToLoad();
		waitForNetworkIdle();

		new WebDriverWait(driver, Duration.ofSeconds(45)).until(d -> {
			String activePage = getCurrentEvaluationPageLabel();
			int currentRowCount = d.findElements(APPLICATION_TABLE_ROWS).size();
			int currentAllFilterCount = extractAllFilterCount();
			String currentSignature = buildEvaluationRowSignature();

			boolean pageActivated = targetPage.equals(activePage);
			boolean contentChanged = !currentSignature.equals(previousSignature)
					|| currentRowCount != previousRowCount
					|| currentAllFilterCount != previousAllFilterCount;
			boolean transitioned = pageActivated && contentChanged;

			logger.info("[waitForEvaluationPageTransition] targetPage='" + targetPage + "' activePage='" + activePage
					+ "' previousRowCount=" + previousRowCount + " currentRowCount=" + currentRowCount
					+ " previousAllFilterCount=" + previousAllFilterCount
					+ " currentAllFilterCount=" + currentAllFilterCount
					+ " previousSignature='" + previousSignature + "' currentSignature='" + currentSignature
					+ "' pageActivated=" + pageActivated + " contentChanged=" + contentChanged
					+ " transitioned=" + transitioned);
			return transitioned;
		});
	}

	private void waitForEvaluationPageDataToStabilize() {
		waitForEvaluationPageDataToStabilize(getCurrentEvaluationPageLabel());
	}

	private void waitForEvaluationPageDataToStabilize(String expectedPageLabel) {
		waitForPageAndAjaxToLoad();
		waitForNetworkIdle();
		new WebDriverWait(driver, Duration.ofSeconds(45)).until(d -> {
			String activePageOne = getCurrentEvaluationPageLabel();
			int rowCountOne = d.findElements(APPLICATION_TABLE_ROWS).size();
			int allFilterCountOne = extractAllFilterCount();
			String signatureOne = buildEvaluationRowSignature();

			try {
				Thread.sleep(1500);
			} catch (InterruptedException interruptedException) {
				Thread.currentThread().interrupt();
			}

			String activePageTwo = getCurrentEvaluationPageLabel();
			int rowCountTwo = d.findElements(APPLICATION_TABLE_ROWS).size();
			int allFilterCountTwo = extractAllFilterCount();
			String signatureTwo = buildEvaluationRowSignature();
			boolean stabilized = rowCountOne > 0
					&& expectedPageLabel.equals(activePageOne)
					&& expectedPageLabel.equals(activePageTwo)
					&& rowCountOne == rowCountTwo
					&& allFilterCountOne == allFilterCountTwo
					&& signatureOne.equals(signatureTwo)
					&& rowCountTwo == allFilterCountTwo;

			logger.info("[waitForEvaluationPageDataToStabilize] expectedPageLabel='" + expectedPageLabel
					+ "' activePageOne='" + activePageOne + "' activePageTwo='" + activePageTwo
					+ "' rowCountOne=" + rowCountOne + " rowCountTwo=" + rowCountTwo
					+ " allFilterCountOne=" + allFilterCountOne + " allFilterCountTwo=" + allFilterCountTwo
					+ " signatureOne='" + signatureOne + "' signatureTwo='" + signatureTwo + "' stabilized=" + stabilized);
			return stabilized;
		});
	}

	private String buildEvaluationRowSignature() {
		List<WebElement> rows = driver.findElements(APPLICATION_TABLE_ROWS);
		return rows.stream()
				.map(row -> row.getText().trim().replaceAll("\\s+", " "))
				.collect(Collectors.joining("|"));
	}

	private boolean isApplicationPresentOnCurrentPage(String applicationName) {
		try {
			findApplicationRowOnCurrentPage(applicationName);
			return true;
		} catch (NoSuchElementException noSuchElementException) {
			return false;
		}
	}

	private WebElement findApplicationRowOnCurrentPage(String applicationName) {
		String xpath = "//tbody//tr[contains(@class,'row')][.//td[2]//span[contains(@class,'td-value') and normalize-space()=\""
				+ applicationName + "\"]]";
		return driver.findElement(By.xpath(xpath));
	}

	private WebElement getStatusDropdownOnCurrentPage(String applicationName) {
		WebElement row = findApplicationRowOnCurrentPage(applicationName);
		return row.findElement(By.cssSelector("td:nth-child(5) select[name='status']"));
	}

	private String getCurrentEvaluationPageLabel() {
		try {
			List<WebElement> activePageButtons = driver.findElements(EVALUATION_ACTIVE_PAGE);
			if (!activePageButtons.isEmpty()) {
				String activePageText = activePageButtons.get(0).getText().trim();
				if (!activePageText.isEmpty()) {
					return activePageText;
				}
			}
		} catch (Exception e) {
			logger.warn("[getCurrentEvaluationPageLabel] Could not read active evaluation page label.", e);
		}
		return "1";
	}

	private List<String> getEvaluationPageNumbers() {
		List<String> pageNumbers = driver.findElements(EVALUATION_PAGE_LINKS).stream()
				.map(element -> element.getText().trim())
				.filter(text -> !text.isEmpty())
				.collect(Collectors.toList());
		logger.info("[getEvaluationPageNumbers] pageNumbers=" + pageNumbers);
		return pageNumbers;
	}
}
