package ov.pages.passport;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ov.utilities.CommonMethods;

public class ProgramManagement_POM extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(ProgramManagement_POM.class);

	private static final By PROGRAM_CARDS = By.cssSelector("div.program-list-container div.ProgramCard");
	private static final By ORGANIZATION_DASHBOARD_ICON = By.cssSelector("nav a[aria-label='Organization Dashboard'][title='Organization Dashboard']");
	private static final By PROGRAM_CARD_TITLE = By.cssSelector("h5.p-card-name");
	private static final By PROGRAM_STATUS_PILL = By.cssSelector("div.selected-cohort-section div.status-pill");
	private static final By MANAGE_APPLICATIONS_BUTTON = By.cssSelector("a.cohort-action-button");
	private static final By PROGRAM_MENU_BUTTON = By.cssSelector("div.program-action-wrapper button.program-action");
	private static final By CARD_DELETE_PROGRAM_MENU_OPTION = By.cssSelector("div.program-action-wrapper div.additional-menu li.warn");
	private static final By ORGANIZATION_PAGINATION = By.cssSelector("nav.org-programs-pagination[aria-label='Pagination']");
	private static final By ORGANIZATION_ACTIVE_PAGE = By.cssSelector("nav.org-programs-pagination div.page-links button.page-link.active");
	private static final By ORGANIZATION_PAGE_LINKS = By.cssSelector("nav.org-programs-pagination div.page-links button.page-link");
	private static final By ORGANIZATION_NEXT_PAGE = By.cssSelector("nav.org-programs-pagination button.nav-arrow[aria-label='Next page']:not([disabled])");
	private static final By DELETE_CONFIRMATION_MODAL = By.cssSelector("div.modal-wrapper");
	private static final By DELETE_CONFIRMATION_MODAL_TITLE = By.cssSelector("div.modal-wrapper span.modal-title");
	private static final By DELETE_CONFIRMATION_MESSAGE_TITLE = By.cssSelector("div.modal-wrapper span.message-text-title");
	private static final By DELETE_CONFIRMATION_CHECKBOX = By.cssSelector("div.modal-wrapper input.confirmation-checkbox");
	private static final By DELETE_CONFIRMATION_CANCEL = By.cssSelector("div.modal-wrapper button.action-cancel");
	private static final By DELETE_CONFIRMATION_SUBMIT = By.cssSelector("div.modal-wrapper button.action-submit");
	private static final Pattern APPLICATION_COUNT_PATTERN = Pattern.compile("\\((\\d+)\\)");
	private static final int MAX_ORGANIZATION_PAGES_TO_SCAN = 25;

	public ProgramManagement_POM() {
		PageFactory.initElements(driver, this);
	}

	public boolean openOrganizationProgramsPageOne() {
		try {
			waitForOrganizationPageToBeReady();
			if ("1".equals(getCurrentOrganizationPageLabel())) {
				logger.info("[openOrganizationProgramsPageOne] Already on organization page 1.");
				return true;
			}

			List<WebElement> pageLinks = driver.findElements(ORGANIZATION_PAGE_LINKS);
			for (WebElement pageLink : pageLinks) {
				if ("1".equals(pageLink.getText().trim())) {
					scrollScreen(pageLink);
					clickAndDraw(pageLink);
					waitForOrganizationPageToBeReady();
					logger.info("[openOrganizationProgramsPageOne] Navigated back to organization page 1.");
					return true;
				}
			}

			logger.warn("[openOrganizationProgramsPageOne] Could not find organization page 1 link.");
			return false;
		} catch (Exception e) {
			logger.error("[openOrganizationProgramsPageOne] Failed while opening organization page 1.", e);
			return false;
		}
	}

	public boolean clickOrganizationDashboardIcon() {
		try {
			WebElement organizationDashboardIcon = waitForElement(ORGANIZATION_DASHBOARD_ICON);
			scrollScreen(organizationDashboardIcon);
			clickAndDraw(organizationDashboardIcon);
			waitForOrganizationPageToBeReady();
			logger.info("[clickOrganizationDashboardIcon] Clicked Organization Dashboard icon successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[clickOrganizationDashboardIcon] Failed while clicking Organization Dashboard icon.", e);
			return false;
		}
	}

	public ProgramScanResult scanProgramsWithUnpublishedApplicationsAcrossOrganizationPages() {
		return scanProgramsAcrossOrganizationPages(ScanMode.UNPUBLISHED_PROGRAMS);
	}

	public ProgramScanResult scanTargetProgramsAcrossOrganizationPages() {
		return scanProgramsAcrossOrganizationPages(ScanMode.TARGET_PROGRAMS);
	}

	public DeleteRunResult deleteEligibleProgramsAcrossOrganizationPages() {
		DeleteRunResult result = new DeleteRunResult();

		try {
			openOrganizationProgramsPageOne();

			for (int pageAttempt = 1; pageAttempt <= MAX_ORGANIZATION_PAGES_TO_SCAN; pageAttempt++) {
				waitForOrganizationPageToBeReady();
				String activePage = getCurrentOrganizationPageLabel();

				DeletionAttempt attempt = deleteFirstEligibleProgramOnCurrentPage(activePage);
				if (attempt.deleted) {
					result.deletedProgramsCount++;
					result.deleteModalVerifiedForAllPrograms = result.deleteModalVerifiedForAllPrograms
							&& attempt.modalVerified;
					pageAttempt = 0;
					continue;
				}

				if (!goToNextOrganizationPage(activePage)) {
					break;
				}
			}

			logger.info("[deleteEligibleProgramsAcrossOrganizationPages] deletedProgramsCount="
					+ result.deletedProgramsCount + " deleteModalVerifiedForAllPrograms="
					+ result.deleteModalVerifiedForAllPrograms);
			return result;
		} catch (Exception e) {
			logger.error("[deleteEligibleProgramsAcrossOrganizationPages] Failed while deleting eligible programs.", e);
			result.failed = true;
			return result;
		}
	}

	public boolean noEligibleProgramsRemainAcrossOrganizationPages() {
		ProgramScanResult remainingPrograms = scanTargetProgramsAcrossOrganizationPages();
		boolean result = !remainingPrograms.failed && remainingPrograms.matchedProgramsCount == 0;
		logger.info("[noEligibleProgramsRemainAcrossOrganizationPages] matchedProgramsCount="
				+ remainingPrograms.matchedProgramsCount + " result=" + result);
		return result;
	}

	public boolean isProgramWithApplicationsPresentAnywhere(String targetProgramName) {
		try {
			openOrganizationProgramsPageOne();

			for (int pageAttempt = 1; pageAttempt <= MAX_ORGANIZATION_PAGES_TO_SCAN; pageAttempt++) {
				waitForOrganizationPageToBeReady();
				String activePage = getCurrentOrganizationPageLabel();
				List<WebElement> cards = driver.findElements(PROGRAM_CARDS);

				for (int i = 0; i < cards.size(); i++) {
					ProgramCardState cardState = readCardState(cards.get(i), activePage, i);
					if (cardState.programTitle.equals(targetProgramName) && cardState.applicationCount > 0) {
						logger.info("[isProgramWithApplicationsPresentAnywhere] Found protected program='"
								+ targetProgramName + "' on activePage='" + activePage + "' applicationCount="
								+ cardState.applicationCount);
						return true;
					}
				}

				if (!goToNextOrganizationPage(activePage)) {
					break;
				}
			}

			logger.warn("[isProgramWithApplicationsPresentAnywhere] Protected program with applications was not found. targetProgramName='"
					+ targetProgramName + "'");
			return false;
		} catch (Exception e) {
			logger.error("[isProgramWithApplicationsPresentAnywhere] Failed while scanning for protected program='"
					+ targetProgramName + "'", e);
			return false;
		}
	}

	private ProgramScanResult scanProgramsAcrossOrganizationPages(ScanMode scanMode) {
		ProgramScanResult result = new ProgramScanResult();

		try {
			openOrganizationProgramsPageOne();

			for (int pageAttempt = 1; pageAttempt <= MAX_ORGANIZATION_PAGES_TO_SCAN; pageAttempt++) {
				waitForOrganizationPageToBeReady();
				String activePage = getCurrentOrganizationPageLabel();
				List<WebElement> cards = driver.findElements(PROGRAM_CARDS);

				logger.info("[scanProgramsAcrossOrganizationPages] scanMode=" + scanMode + " activePage='" + activePage
						+ "' cardsFound=" + cards.size());

				for (int i = 0; i < cards.size(); i++) {
					ProgramCardState cardState = readCardState(cards.get(i), activePage, i);
					boolean isMatch = cardState.isUnpublished;

					if (!isMatch) {
						continue;
					}

					result.matchedProgramsCount++;
					if (cardState.isUnpublished && !cardState.manageApplicationsDisabled) {
						result.manageApplicationsDisabledForAllMatches = false;
					}
				}

				if (!goToNextOrganizationPage(activePage)) {
					break;
				}
			}

			logger.info("[scanProgramsAcrossOrganizationPages] scanMode=" + scanMode + " matchedProgramsCount="
					+ result.matchedProgramsCount + " manageApplicationsDisabledForAllMatches="
					+ result.manageApplicationsDisabledForAllMatches);
			return result;
		} catch (Exception e) {
			logger.error("[scanProgramsAcrossOrganizationPages] Failed for scanMode=" + scanMode, e);
			result.failed = true;
			return result;
		}
	}

	private DeletionAttempt deleteFirstEligibleProgramOnCurrentPage(String activePage) {
		List<WebElement> cards = driver.findElements(PROGRAM_CARDS);
		logger.info("[deleteFirstEligibleProgramOnCurrentPage] activePage='" + activePage + "' cardsFound=" + cards.size());

		for (int i = 0; i < cards.size(); i++) {
			WebElement card = cards.get(i);
			ProgramCardState cardState = readCardState(card, activePage, i);

			if (!cardState.eligibleForDeletion) {
				continue;
			}

			boolean modalOpened = openDeleteProgramModal(card);
			boolean modalVerified = modalOpened && verifyDeleteProgramModal(cardState.programTitle);
			boolean confirmed = modalVerified && confirmDeleteProgram(card);

			logger.info("[deleteFirstEligibleProgramOnCurrentPage] programTitle='" + cardState.programTitle
					+ "' modalOpened=" + modalOpened + " modalVerified=" + modalVerified + " confirmed="
					+ confirmed);

			return new DeletionAttempt(confirmed, modalVerified);
		}

		return new DeletionAttempt(false, true);
	}

	private boolean openDeleteProgramModal(WebElement card) {
		try {
			WebElement menuButton = card.findElement(PROGRAM_MENU_BUTTON);
			scrollScreen(menuButton);
			clickAndDraw(menuButton);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement deleteProgramOption = wait.until(d -> getVisibleDeleteProgramOption(card));
			scrollScreen(deleteProgramOption);
			clickAndDraw(deleteProgramOption);
			wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_CONFIRMATION_MODAL));
			return true;
		} catch (Exception e) {
			logger.error("[openDeleteProgramModal] Failed while opening delete program modal.", e);
			return false;
		}
	}

	private WebElement getVisibleDeleteProgramOption(WebElement card) {
		List<WebElement> deleteProgramOptions = card.findElements(CARD_DELETE_PROGRAM_MENU_OPTION);
		for (WebElement deleteProgramOption : deleteProgramOptions) {
			try {
				if (deleteProgramOption.isDisplayed()) {
					return deleteProgramOption;
				}
			} catch (StaleElementReferenceException e) {
				logger.warn("[getVisibleDeleteProgramOption] Delete Program option went stale while checking visibility.");
			}
		}
		return null;
	}

	private boolean verifyDeleteProgramModal(String expectedProgramTitle) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_CONFIRMATION_MODAL));
			WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_CONFIRMATION_MODAL_TITLE));
			WebElement messageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_CONFIRMATION_MESSAGE_TITLE));
			WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_CONFIRMATION_CHECKBOX));
			WebElement cancelButton = wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_CONFIRMATION_CANCEL));
			WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_CONFIRMATION_SUBMIT));

			String actualTitle = title.getText().trim();
			String actualMessageTitle = messageTitle.getText().trim();
			boolean submitDisabledBeforeVerification = !submitButton.isEnabled()
					|| submitButton.getAttribute("disabled") != null;
			boolean result = modal.isDisplayed()
					&& "Remove Program Confirmation".equals(actualTitle)
					&& actualMessageTitle.contains(expectedProgramTitle)
					&& checkbox.isDisplayed()
					&& cancelButton.isDisplayed()
					&& submitButton.isDisplayed()
					&& submitDisabledBeforeVerification;

			logger.info("[verifyDeleteProgramModal] expectedProgramTitle='" + expectedProgramTitle + "' actualTitle='"
					+ actualTitle + "' actualMessageTitle='" + actualMessageTitle
					+ "' submitDisabledBeforeVerification=" + submitDisabledBeforeVerification + " result=" + result);

			return result;
		} catch (Exception e) {
			logger.error("[verifyDeleteProgramModal] Failed while verifying delete program modal.", e);
			return false;
		}
	}

	private boolean confirmDeleteProgram(WebElement card) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(DELETE_CONFIRMATION_CHECKBOX));
			clickAndDraw(checkbox);

			WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(DELETE_CONFIRMATION_SUBMIT));
			clickAndDraw(submitButton);

			wait.until(ExpectedConditions.invisibilityOfElementLocated(DELETE_CONFIRMATION_MODAL));
			wait.until(ExpectedConditions.stalenessOf(card));
			waitForOrganizationPageToBeReady();
			return true;
		} catch (StaleElementReferenceException e) {
			waitForOrganizationPageToBeReady();
			return true;
		} catch (Exception e) {
			logger.error("[confirmDeleteProgram] Failed while confirming program deletion.", e);
			return false;
		}
	}

	private ProgramCardState readCardState(WebElement card, String activePage, int cardIndex) {
		String programTitle = card.findElement(PROGRAM_CARD_TITLE).getText().trim();
		String statusText = extractCardText(card, PROGRAM_STATUS_PILL);
		String manageApplicationsText = extractCardText(card, MANAGE_APPLICATIONS_BUTTON);
		boolean manageApplicationsDisabled = isManageApplicationsDisabled(card);
		int applicationCount = extractApplicationCount(manageApplicationsText);
		boolean isUnpublished = statusText.toLowerCase().contains("opens tbd");
		boolean isApplicationOpen = statusText.toLowerCase().contains("application open");
		boolean eligibleForDeletion = isUnpublished;

		logger.info("[readCardState] activePage='" + activePage + "' cardIndex=" + cardIndex + " programTitle='"
				+ programTitle + "' statusText='" + statusText + "' manageApplicationsText='"
				+ manageApplicationsText + "' manageApplicationsDisabled=" + manageApplicationsDisabled
				+ " applicationCount=" + applicationCount + " isUnpublished=" + isUnpublished
				+ " isApplicationOpen=" + isApplicationOpen
				+ " eligibleForDeletion=" + eligibleForDeletion);

		return new ProgramCardState(programTitle, statusText, manageApplicationsText, manageApplicationsDisabled,
				applicationCount, isUnpublished, eligibleForDeletion);
	}

	private boolean isManageApplicationsDisabled(WebElement card) {
		try {
			WebElement manageApplicationsButton = card.findElement(MANAGE_APPLICATIONS_BUTTON);
			String classAttribute = manageApplicationsButton.getAttribute("class");
			String disabledAttribute = manageApplicationsButton.getAttribute("disabled");
			boolean result = !manageApplicationsButton.isEnabled()
					|| (classAttribute != null && classAttribute.contains("disabled"))
					|| disabledAttribute != null;

			logger.info("[isManageApplicationsDisabled] classAttribute='" + classAttribute + "' disabledAttribute='"
					+ disabledAttribute + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[isManageApplicationsDisabled] Failed while reading Manage Applications button state.", e);
			return false;
		}
	}

	private int extractApplicationCount(String manageApplicationsText) {
		Matcher matcher = APPLICATION_COUNT_PATTERN.matcher(manageApplicationsText);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}
		return 0;
	}

	private String extractCardText(WebElement card, By locator) {
		try {
			List<WebElement> elements = card.findElements(locator);
			if (elements.isEmpty()) {
				return "";
			}
			return elements.get(0).getText().trim();
		} catch (Exception e) {
			logger.warn("[extractCardText] Failed while extracting card text for locator='" + locator + "'", e);
			return "";
		}
	}

	private boolean goToNextOrganizationPage(String currentPageLabel) {
		try {
			if (!isElementPresent(ORGANIZATION_PAGINATION)) {
				return false;
			}

			List<WebElement> nextButtons = driver.findElements(ORGANIZATION_NEXT_PAGE);
			if (nextButtons.isEmpty()) {
				return false;
			}

			String currentUrl = driver.getCurrentUrl();
			clickAndDrawBy(ORGANIZATION_NEXT_PAGE);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(d -> {
				String newActivePage = getCurrentOrganizationPageLabel();
				String newUrl = d.getCurrentUrl();
				return !newActivePage.equals(currentPageLabel) || !newUrl.equals(currentUrl);
			});

			waitForOrganizationPageToBeReady();
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
		new WebDriverWait(driver, Duration.ofSeconds(20)).until(d -> !d.findElements(PROGRAM_CARDS).isEmpty());
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
			logger.warn("[getCurrentOrganizationPageLabel] Could not read active organization page label.", e);
		}
		return "1";
	}

	private enum ScanMode {
		UNPUBLISHED_PROGRAMS,
		TARGET_PROGRAMS
	}

	private static class ProgramCardState {
		private final String programTitle;
		private final String statusText;
		private final String manageApplicationsText;
		private final boolean manageApplicationsDisabled;
		private final int applicationCount;
		private final boolean isUnpublished;
		private final boolean eligibleForDeletion;

		private ProgramCardState(String programTitle, String statusText, String manageApplicationsText,
				boolean manageApplicationsDisabled, int applicationCount, boolean isUnpublished,
				boolean eligibleForDeletion) {
			this.programTitle = programTitle;
			this.statusText = statusText;
			this.manageApplicationsText = manageApplicationsText;
			this.manageApplicationsDisabled = manageApplicationsDisabled;
			this.applicationCount = applicationCount;
			this.isUnpublished = isUnpublished;
			this.eligibleForDeletion = eligibleForDeletion;
		}
	}

	public static class ProgramScanResult {
		public int matchedProgramsCount;
		public boolean manageApplicationsDisabledForAllMatches = true;
		public boolean failed;
	}

	public static class DeleteRunResult {
		public int deletedProgramsCount;
		public boolean deleteModalVerifiedForAllPrograms = true;
		public boolean failed;
	}

	private static class DeletionAttempt {
		private final boolean deleted;
		private final boolean modalVerified;

		private DeletionAttempt(boolean deleted, boolean modalVerified) {
			this.deleted = deleted;
			this.modalVerified = modalVerified;
		}
	}
}