package ov.pages.passport;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import ov.utilities.CommonMethods;

public class ApplicationEvaluation_POM extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(ApplicationEvaluation_POM.class);

	private static final By EVALUATION_TABLE_ROWS = By.cssSelector("div.subjects-table-list tbody tr.row");
	private static final By APPLICATION_TABLE_CONTAINER = By.cssSelector("div.round-subjects-table-container div.invite-table-content");
	private static final By FIRST_APPLICATION_NAME = By.cssSelector("td:nth-child(2) span.td-value.link");
	private static final By ENTRY_VIEWER_CONTAINER = By.cssSelector("div.entry-viewer-container");
	private static final By ENTRY_VIEWER_COMPANY_NAME = By.cssSelector("div.entry-navigator-company-container span.company-name-text");
	private static final By PREVIOUS_APPLICATION_ARROW = By.cssSelector("button.entry-navigator-btn svg[data-icon='angle-left']");
	private static final By NEXT_APPLICATION_ARROW = By.cssSelector("button.entry-navigator-btn svg[data-icon='angle-right']");
	private static final By DETAILS_PAGE_EMAIL_BUTTON = By.cssSelector("button.circle-button.secondary.email-btn");
	private static final By COPY_LINK_BUTTON = By.xpath("//button[contains(@class,'circle-button')][.//*[name()='svg' and @data-icon='link']]");
	private static final By APPLICATION_LIST_BUTTON = By.xpath("//*[normalize-space()='Application List']");
	private static final By ENTRY_STATUS_DROPDOWN = By.cssSelector("div.entry-navigator-action-container select[name='select-drop-down']");
	private static final By MY_RATING_ACTIVE_TAB = By.cssSelector("div.entry-rating-tab.entry-rating-tab-active span");
	private static final By ALL_RATINGS_TAB = By.xpath("//div[contains(@class,'entry-rating-tab')]//span[normalize-space()='All Ratings']");
	private static final By ALL_RATINGS_ACTIVE_TAB = By.xpath("//div[contains(@class,'entry-rating-tab') and contains(@class,'entry-rating-tab-active')]//span[normalize-space()='All Ratings']");
	private static final By ENTRY_SUBMIT_BUTTON = By.xpath("//div[contains(@class,'rubric-action-container')]//button[normalize-space()='Submit']");
	private static final By STARTUP_COMPANY_NAME = By.cssSelector("div.company-name-container span.company-name");
	private static final By EVALUATION_HEADER_TITLE = By.cssSelector("div.eval-header-title span.eval-title-text");
	private static final By ENTRY_RATING_STAR_BUTTONS = By.cssSelector("div.entry-rating-container span.star-btn.sm");
	private static final By ENTRY_SELECTED_STARS = By.cssSelector("div.entry-rating-container span.selected-star");
	private static final By ENTRY_UNSELECTED_STARS = By.cssSelector("div.entry-rating-container span.unselected-star");
	private static final By ENTRY_RATING_COMMENT_TEXTAREA = By.cssSelector("div.entry-rating-container textarea.review-field-input");
	private static final By SUBMITTED_RATINGS_CONTAINER = By.cssSelector("div.SubmittedRatings");
	private static final By NO_SUBMITTED_RATINGS_MESSAGE = By.xpath("//*[normalize-space()='No Submitted Ratings']");
	private static final By COPY_LINK_BUTTON_DISABLED = By.xpath("//button[contains(@class,'circle-button')][@disabled and .//*[name()='svg' and @data-icon='link']]");
	private static final By COPY_LINK_DOM_FEEDBACK = By.xpath("//div[contains(@class,'hover-text')][contains(normalize-space(),'Copied') or contains(normalize-space(),'Link Copied')]");

	public String clickFirstApplicationFromEvaluationResults() {
		try {
			applicationManagement_pom.waitForApplicationsToLoad();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(d -> !d.findElements(EVALUATION_TABLE_ROWS).isEmpty());

			WebElement firstApplicationName = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_APPLICATION_NAME));
			String selectedApplicationName = firstApplicationName.getText().trim();
			scrollScreen(firstApplicationName);
			clickAndDraw(firstApplicationName);

			wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_VIEWER_CONTAINER));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();

			logger.info("[clickFirstApplicationFromEvaluationResults] selectedApplicationName='" + selectedApplicationName + "'");
			return selectedApplicationName;
		} catch (Exception e) {
			logger.error("[clickFirstApplicationFromEvaluationResults] Failed while clicking the first application from evaluation results.", e);
			return null;
		}
	}

	public boolean verifySelectedApplicationEvaluationDetailsPageDisplayed(String expectedApplicationName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement entryViewerContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_VIEWER_CONTAINER));
			WebElement navigatorCompanyName = wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_VIEWER_COMPANY_NAME));
			WebElement applicationListButton = wait.until(ExpectedConditions.visibilityOfElementLocated(APPLICATION_LIST_BUTTON));
			WebElement entryStatusDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_STATUS_DROPDOWN));
			WebElement myRatingActiveTab = wait.until(ExpectedConditions.visibilityOfElementLocated(MY_RATING_ACTIVE_TAB));
			WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_SUBMIT_BUTTON));
			WebElement startupCompanyName = wait.until(ExpectedConditions.visibilityOfElementLocated(STARTUP_COMPANY_NAME));

			String navigatorApplicationName = navigatorCompanyName.getText().trim();
			String startupApplicationName = startupCompanyName.getText().trim();
			String currentStatus = new Select(entryStatusDropdown).getFirstSelectedOption().getText().trim();
			String activeRatingTab = myRatingActiveTab.getText().trim();

			boolean result = entryViewerContainer.isDisplayed()
					&& applicationListButton.isDisplayed()
					&& entryStatusDropdown.isDisplayed()
					&& submitButton.isDisplayed()
					&& "My Rating".equals(activeRatingTab)
					&& !currentStatus.isBlank()
					&& expectedApplicationName != null
					&& expectedApplicationName.equals(navigatorApplicationName)
					&& expectedApplicationName.equals(startupApplicationName);

			logger.info("[verifySelectedApplicationEvaluationDetailsPageDisplayed] expectedApplicationName='" + expectedApplicationName
					+ "' navigatorApplicationName='" + navigatorApplicationName
					+ "' startupApplicationName='" + startupApplicationName
					+ "' currentStatus='" + currentStatus
					+ "' activeRatingTab='" + activeRatingTab
					+ "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifySelectedApplicationEvaluationDetailsPageDisplayed] Failed while verifying the selected application evaluation details page.", e);
			return false;
		}
	}

	public boolean clickApplicationListFromDetailsPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement applicationListButton = wait.until(ExpectedConditions.elementToBeClickable(APPLICATION_LIST_BUTTON));
			scrollScreen(applicationListButton);
			clickAndDraw(applicationListButton);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(ENTRY_VIEWER_CONTAINER));
			wait.until(ExpectedConditions.visibilityOfElementLocated(APPLICATION_TABLE_CONTAINER));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[clickApplicationListFromDetailsPage] Clicked Application List and returned to evaluation list view successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[clickApplicationListFromDetailsPage] Failed while clicking Application List from details page.", e);
			return false;
		}
	}

	public boolean verifyApplicationsListDisplayedAgainForTargetProgram(String expectedProgramName) {
		try {
			applicationManagement_pom.waitForApplicationsToLoad();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement evaluationHeaderTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(EVALUATION_HEADER_TITLE));
			WebElement tableContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(APPLICATION_TABLE_CONTAINER));
			int rowCount = driver.findElements(EVALUATION_TABLE_ROWS).size();
			String actualProgramName = evaluationHeaderTitle.getText().trim();
			boolean entryViewerPresent = !driver.findElements(ENTRY_VIEWER_CONTAINER).isEmpty();

			boolean result = tableContainer.isDisplayed()
					&& !entryViewerPresent
					&& expectedProgramName != null
					&& expectedProgramName.equalsIgnoreCase(actualProgramName)
					&& rowCount > 0;

			logger.info("[verifyApplicationsListDisplayedAgainForTargetProgram] expectedProgramName='" + expectedProgramName
					+ "' actualProgramName='" + actualProgramName
					+ "' rowCount=" + rowCount
					+ " entryViewerPresent=" + entryViewerPresent
					+ " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyApplicationsListDisplayedAgainForTargetProgram] Failed while verifying the applications list is displayed again for the target program.", e);
			return false;
		}
	}

	public boolean changeSelectedApplicationEvaluationStatusTo(String newStatus) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement entryStatusDropdown = wait.until(ExpectedConditions.elementToBeClickable(ENTRY_STATUS_DROPDOWN));
			scrollScreen(entryStatusDropdown);
			Select select = new Select(entryStatusDropdown);
			String currentStatus = select.getFirstSelectedOption().getText().trim();

			logger.info("[changeSelectedApplicationEvaluationStatusTo] currentStatus='" + currentStatus + "' newStatus='" + newStatus + "'");

			if (currentStatus.equals(newStatus)) {
				return true;
			}

			select.selectByVisibleText(newStatus);
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();

			boolean updated = wait.until(d -> {
				String selectedStatus = new Select(d.findElement(ENTRY_STATUS_DROPDOWN))
						.getFirstSelectedOption()
						.getText()
						.trim();
				return newStatus.equals(selectedStatus);
			});

			logger.info("[changeSelectedApplicationEvaluationStatusTo] END updated=" + updated);
			return updated;
		} catch (Exception e) {
			logger.error("[changeSelectedApplicationEvaluationStatusTo] Failed while changing the selected application evaluation status to '" + newStatus + "'.", e);
			return false;
		}
	}

	public boolean verifySelectedApplicationEvaluationStatusIs(String expectedStatus) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement entryStatusDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_STATUS_DROPDOWN));
			String actualStatus = new Select(entryStatusDropdown).getFirstSelectedOption().getText().trim();
			boolean result = expectedStatus.equals(actualStatus);

			logger.info("[verifySelectedApplicationEvaluationStatusIs] expectedStatus='" + expectedStatus
					+ "' actualStatus='" + actualStatus + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifySelectedApplicationEvaluationStatusIs] Failed while verifying the selected application evaluation status is '" + expectedStatus + "'.", e);
			return false;
		}
	}

	public boolean verifyApplicationEvaluationRatingTabSelected(String expectedTabName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement activeTab = wait.until(ExpectedConditions.visibilityOfElementLocated(MY_RATING_ACTIVE_TAB));
			String actualTabName = activeTab.getText().trim();
			boolean result = expectedTabName.equals(actualTabName);

			logger.info("[verifyApplicationEvaluationRatingTabSelected] expectedTabName='" + expectedTabName
					+ "' actualTabName='" + actualTabName + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyApplicationEvaluationRatingTabSelected] Failed while verifying the selected application evaluation rating tab is '" + expectedTabName + "'.", e);
			return false;
		}
	}

	public boolean selectApplicationEvaluationStarRating(int starNumber) {
		try {
			if (starNumber < 1 || starNumber > 5) {
				return false;
			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(d -> d.findElements(ENTRY_RATING_STAR_BUTTONS).size() == 5);

			List<WebElement> stars = driver.findElements(ENTRY_RATING_STAR_BUTTONS);
			WebElement targetStar = stars.get(starNumber - 1);
			scrollScreen(targetStar);
			clickAndDraw(targetStar);

			boolean result = wait.until(d -> {
				int selected = d.findElements(ENTRY_SELECTED_STARS).size();
				int unselected = d.findElements(ENTRY_UNSELECTED_STARS).size();
				return selected == starNumber && (selected + unselected) == 5;
			});

			logger.info("[selectApplicationEvaluationStarRating] starNumber=" + starNumber + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[selectApplicationEvaluationStarRating] Failed while selecting application evaluation star rating='" + starNumber + "'.", e);
			return false;
		}
	}

	public boolean enterApplicationEvaluationRatingComment(String ratingComment) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement commentTextarea = wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_RATING_COMMENT_TEXTAREA));
			scrollScreen(commentTextarea);
			commentTextarea.clear();
			commentTextarea.sendKeys(ratingComment);
			String actualComment = commentTextarea.getAttribute("value").trim();
			boolean result = ratingComment.equals(actualComment);

			logger.info("[enterApplicationEvaluationRatingComment] ratingComment='" + ratingComment + "' actualComment='" + actualComment + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[enterApplicationEvaluationRatingComment] Failed while entering the application evaluation rating comment.", e);
			return false;
		}
	}

	public boolean clickButtonFromApplicationEvaluationDetailsPage(String buttonLabel) {
		try {
			WebElement buttonToClick = null;
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			if ("Application List".equals(buttonLabel)) {
				buttonToClick = wait.until(ExpectedConditions.elementToBeClickable(APPLICATION_LIST_BUTTON));
			} else if ("Submit".equals(buttonLabel)) {
				buttonToClick = wait.until(ExpectedConditions.elementToBeClickable(ENTRY_SUBMIT_BUTTON));
			} else if ("All Ratings".equals(buttonLabel)) {
				buttonToClick = wait.until(ExpectedConditions.elementToBeClickable(ALL_RATINGS_TAB));
			}

			if (buttonToClick == null) {
				return false;
			}

			scrollScreen(buttonToClick);
			clickAndDraw(buttonToClick);
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[clickButtonFromApplicationEvaluationDetailsPage] buttonLabel='" + buttonLabel + "' clicked successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[clickButtonFromApplicationEvaluationDetailsPage] Failed while clicking buttonLabel='" + buttonLabel + "' from the application evaluation details page.", e);
			return false;
		}
	}

	public boolean verifyApplicationEvaluationRatingWasSubmittedSuccessfully(int expectedStars, String expectedComment) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			boolean result = wait.until(d -> {
				int selectedStars = d.findElements(ENTRY_SELECTED_STARS).size();
				boolean myRatingTabActive = !d.findElements(MY_RATING_ACTIVE_TAB).isEmpty();
				List<WebElement> commentTextareas = d.findElements(ENTRY_RATING_COMMENT_TEXTAREA);
				boolean commentStillVisible = !commentTextareas.isEmpty() && commentTextareas.get(0).isDisplayed();
				boolean commentMatchesIfVisible = !commentStillVisible
						|| expectedComment.equals(commentTextareas.get(0).getAttribute("value").trim());
				return selectedStars == expectedStars && myRatingTabActive && commentMatchesIfVisible;
			});

			List<WebElement> commentTextareas = driver.findElements(ENTRY_RATING_COMMENT_TEXTAREA);
			boolean commentStillVisible = !commentTextareas.isEmpty() && commentTextareas.get(0).isDisplayed();
			String actualComment = commentStillVisible ? commentTextareas.get(0).getAttribute("value").trim() : "<hidden-after-submit>";
			int selectedStars = driver.findElements(ENTRY_SELECTED_STARS).size();

			logger.info("[verifyApplicationEvaluationRatingWasSubmittedSuccessfully] expectedStars=" + expectedStars
					+ " actualSelectedStars=" + selectedStars
					+ " expectedComment='" + expectedComment + "' actualComment='" + actualComment
					+ "' commentStillVisible=" + commentStillVisible
					+ " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyApplicationEvaluationRatingWasSubmittedSuccessfully] Failed while verifying the application evaluation rating was submitted successfully.", e);
			return false;
		}
	}

	public boolean verifyAllRatingsViewDisplayedOnApplicationEvaluationDetailsPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement activeTab = wait.until(ExpectedConditions.visibilityOfElementLocated(ALL_RATINGS_ACTIVE_TAB));
			WebElement submittedRatingsContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMITTED_RATINGS_CONTAINER));
			boolean noSubmittedRatingsVisible = !driver.findElements(NO_SUBMITTED_RATINGS_MESSAGE).isEmpty();
			boolean result = activeTab.isDisplayed() && submittedRatingsContainer.isDisplayed();

			logger.info("[verifyAllRatingsViewDisplayedOnApplicationEvaluationDetailsPage] activeTab='"
					+ activeTab.getText().trim() + "' noSubmittedRatingsVisible=" + noSubmittedRatingsVisible
					+ " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyAllRatingsViewDisplayedOnApplicationEvaluationDetailsPage] Failed while verifying the All Ratings view on the application evaluation details page.", e);
			return false;
		}
	}

	public boolean verifyNoSubmittedRatingsEmptyStateDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement activeTab = wait.until(ExpectedConditions.visibilityOfElementLocated(ALL_RATINGS_ACTIVE_TAB));
			WebElement submittedRatingsContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMITTED_RATINGS_CONTAINER));
			WebElement emptyStateMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(NO_SUBMITTED_RATINGS_MESSAGE));
			boolean result = activeTab.isDisplayed()
					&& submittedRatingsContainer.isDisplayed()
					&& "No Submitted Ratings".equals(emptyStateMessage.getText().trim());

			logger.info("[verifyNoSubmittedRatingsEmptyStateDisplayed] activeTab='" + activeTab.getText().trim()
					+ "' emptyStateMessage='" + emptyStateMessage.getText().trim()
					+ "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyNoSubmittedRatingsEmptyStateDisplayed] Failed while verifying the No Submitted Ratings empty state.", e);
			return false;
		}
	}

	public boolean verifyRatingWasNotSubmittedWithoutSelectingStars() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			boolean result = wait.until(d -> {
				boolean myRatingTabActive = !d.findElements(MY_RATING_ACTIVE_TAB).isEmpty();
				boolean submitVisible = !d.findElements(ENTRY_SUBMIT_BUTTON).isEmpty();
				boolean commentVisible = !d.findElements(ENTRY_RATING_COMMENT_TEXTAREA).isEmpty();
				int selectedStars = d.findElements(ENTRY_SELECTED_STARS).size();
				int unselectedStars = d.findElements(ENTRY_UNSELECTED_STARS).size();
				return myRatingTabActive && submitVisible && commentVisible && selectedStars == 0 && unselectedStars == 5;
			});

			int selectedStars = driver.findElements(ENTRY_SELECTED_STARS).size();
			int unselectedStars = driver.findElements(ENTRY_UNSELECTED_STARS).size();
			boolean commentVisible = !driver.findElements(ENTRY_RATING_COMMENT_TEXTAREA).isEmpty();

			logger.info("[verifyRatingWasNotSubmittedWithoutSelectingStars] selectedStars=" + selectedStars
					+ " unselectedStars=" + unselectedStars
					+ " commentVisible=" + commentVisible
					+ " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyRatingWasNotSubmittedWithoutSelectingStars] Failed while verifying the rating was not submitted without selecting stars.", e);
			return false;
		}
	}

	public String getCurrentApplicationNameFromApplicationEvaluationDetailsPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement companyName = wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_VIEWER_COMPANY_NAME));
			String applicationName = companyName.getText().trim();
			logger.info("[getCurrentApplicationNameFromApplicationEvaluationDetailsPage] applicationName='" + applicationName + "'");
			return applicationName;
		} catch (Exception e) {
			logger.error("[getCurrentApplicationNameFromApplicationEvaluationDetailsPage] Failed while reading the current application name from the application evaluation details page.", e);
			return null;
		}
	}

	public String clickApplicationNavigationArrowAndGetDisplayedName(String direction, String currentApplicationName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			By arrowLocator = "next".equalsIgnoreCase(direction) ? NEXT_APPLICATION_ARROW : PREVIOUS_APPLICATION_ARROW;
			WebElement arrowIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(arrowLocator));
			WebElement arrowButton = arrowIcon.findElement(By.xpath("./ancestor::button"));
			scrollScreen(arrowButton);
			clickAndDraw(arrowButton);

			String updatedApplicationName = wait.until(d -> {
				List<WebElement> companyNames = d.findElements(ENTRY_VIEWER_COMPANY_NAME);
				if (companyNames.isEmpty()) {
					return null;
				}
				String candidateName = companyNames.get(0).getText().trim();
				return candidateName.isBlank() || candidateName.equals(currentApplicationName) ? null : candidateName;
			});

			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[clickApplicationNavigationArrowAndGetDisplayedName] direction='" + direction
					+ "' currentApplicationName='" + currentApplicationName
					+ "' updatedApplicationName='" + updatedApplicationName + "'");
			return updatedApplicationName;
		} catch (Exception e) {
			logger.error("[clickApplicationNavigationArrowAndGetDisplayedName] Failed while clicking the '" + direction + "' application arrow.", e);
			return null;
		}
	}

	public boolean clickEmailIconFromApplicationEvaluationDetailsPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement emailButton = wait.until(ExpectedConditions.elementToBeClickable(DETAILS_PAGE_EMAIL_BUTTON));
			scrollScreen(emailButton);
			clickAndDraw(emailButton);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-wrapper div.EmailForm")));
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			logger.info("[clickEmailIconFromApplicationEvaluationDetailsPage] Opened the email composer from the application evaluation details page successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[clickEmailIconFromApplicationEvaluationDetailsPage] Failed while clicking the email icon from the application evaluation details page.", e);
			return false;
		}
	}

	public boolean clickCopyLinkIconFromApplicationEvaluationDetailsPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement copyLinkButton = wait.until(ExpectedConditions.elementToBeClickable(COPY_LINK_BUTTON));
			scrollScreen(copyLinkButton);
			clickAndDraw(copyLinkButton);
			logger.info("[clickCopyLinkIconFromApplicationEvaluationDetailsPage] Clicked the copy link icon successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[clickCopyLinkIconFromApplicationEvaluationDetailsPage] Failed while clicking the copy link icon from the application evaluation details page.", e);
			return false;
		}
	}

	public boolean captureCopyLinkFeedbackOnApplicationEvaluationDetailsPage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			boolean result = wait.until(d -> {
				boolean copyButtonDisabled = !d.findElements(COPY_LINK_BUTTON_DISABLED).isEmpty();
				List<WebElement> feedbackElements = d.findElements(COPY_LINK_DOM_FEEDBACK);
				boolean copiedTextPresent = feedbackElements.stream()
						.anyMatch(element -> element.getText() != null
								&& !element.getText().trim().isBlank()
								&& element.getText().trim().toLowerCase().contains("copied"));
				return copyButtonDisabled || copiedTextPresent;
			});

			boolean copyButtonDisabled = !driver.findElements(COPY_LINK_BUTTON_DISABLED).isEmpty();
			String feedbackText = driver.findElements(COPY_LINK_DOM_FEEDBACK).stream()
					.map(WebElement::getText)
					.map(String::trim)
					.filter(text -> !text.isBlank())
					.findFirst()
					.orElse("<no-copied-text>");

			logger.info("[captureCopyLinkFeedbackOnApplicationEvaluationDetailsPage] copyButtonDisabled="
					+ copyButtonDisabled + " feedbackText='" + feedbackText + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[captureCopyLinkFeedbackOnApplicationEvaluationDetailsPage] Failed while capturing copy link feedback on the application evaluation details page.", e);
			return false;
		}
	}

	public boolean verifyCopyLinkFeedbackDisplayedOnApplicationEvaluationDetailsPage() {
		try {
			boolean copyButtonDisabled = !driver.findElements(COPY_LINK_BUTTON_DISABLED).isEmpty();
			String feedbackText = driver.findElements(COPY_LINK_DOM_FEEDBACK).stream()
					.map(WebElement::getText)
					.map(String::trim)
					.filter(text -> !text.isBlank())
					.findFirst()
					.orElse("<no-copied-text>");
			boolean result = copyButtonDisabled || feedbackText.toLowerCase().contains("copied");

			logger.info("[verifyCopyLinkFeedbackDisplayedOnApplicationEvaluationDetailsPage] copyButtonDisabled="
					+ copyButtonDisabled + " feedbackText='" + feedbackText + "' result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifyCopyLinkFeedbackDisplayedOnApplicationEvaluationDetailsPage] Failed while verifying copy link feedback on the application evaluation details page.", e);
			return false;
		}
	}

	public boolean refreshApplicationEvaluationDetailsPage() {
		try {
			driver.navigate().refresh();
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_VIEWER_CONTAINER));
			wait.until(ExpectedConditions.visibilityOfElementLocated(ENTRY_VIEWER_COMPANY_NAME));
			logger.info("[refreshApplicationEvaluationDetailsPage] Refreshed the application evaluation details page successfully.");
			return true;
		} catch (Exception e) {
			logger.error("[refreshApplicationEvaluationDetailsPage] Failed while refreshing the application evaluation details page.", e);
			return false;
		}
	}

	public boolean verifySubmittedRatingStillPersistsAfterRefresh(int expectedStars, String expectedComment) {
		try {
			boolean persistedInMyRating = isSavedRatingVisibleInMyRatingAfterRefresh(expectedStars, expectedComment);

			int selectedStars = driver.findElements(ENTRY_SELECTED_STARS).size();
			List<WebElement> commentTextareas = driver.findElements(ENTRY_RATING_COMMENT_TEXTAREA);
			boolean commentVisible = !commentTextareas.isEmpty() && commentTextareas.get(0).isDisplayed();
			String actualComment = commentVisible ? commentTextareas.get(0).getAttribute("value").trim() : "<hidden-after-refresh>";
			boolean persistedInAllRatings = false;

			if (!persistedInMyRating) {
				persistedInAllRatings = verifySavedRatingVisibleInAllRatingsAfterRefresh(expectedComment);
			}

			boolean result = persistedInMyRating || persistedInAllRatings;

			logger.info("[verifySubmittedRatingStillPersistsAfterRefresh] expectedStars=" + expectedStars
					+ " actualSelectedStars=" + selectedStars
					+ " expectedComment='" + expectedComment + "' actualComment='" + actualComment
					+ "' commentVisible=" + commentVisible
					+ " persistedInMyRating=" + persistedInMyRating
					+ " persistedInAllRatings=" + persistedInAllRatings
					+ " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifySubmittedRatingStillPersistsAfterRefresh] Failed while verifying submitted rating persistence after refresh.", e);
			return false;
		}
	}

	private boolean isSavedRatingVisibleInMyRatingAfterRefresh(int expectedStars, String expectedComment) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
			return wait.until(d -> {
				boolean myRatingTabActive = !d.findElements(MY_RATING_ACTIVE_TAB).isEmpty();
				int selectedStars = d.findElements(ENTRY_SELECTED_STARS).size();
				List<WebElement> commentTextareas = d.findElements(ENTRY_RATING_COMMENT_TEXTAREA);
				boolean commentVisible = !commentTextareas.isEmpty() && commentTextareas.get(0).isDisplayed();
				boolean commentMatchesIfVisible = !commentVisible
						|| expectedComment.equals(commentTextareas.get(0).getAttribute("value").trim());
				return myRatingTabActive && selectedStars == expectedStars && commentMatchesIfVisible;
			});
		} catch (Exception e) {
			logger.info("[isSavedRatingVisibleInMyRatingAfterRefresh] Saved rating not fully visible in My Rating after refresh: " + e.getMessage());
			return false;
		}
	}

	private boolean verifySavedRatingVisibleInAllRatingsAfterRefresh(String expectedComment) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement allRatingsTab = wait.until(ExpectedConditions.elementToBeClickable(ALL_RATINGS_TAB));
			scrollScreen(allRatingsTab);
			clickAndDraw(allRatingsTab);
			waitForPageAndAjaxToLoad();
			waitForNetworkIdle();

			WebElement submittedRatingsContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(SUBMITTED_RATINGS_CONTAINER));
			boolean emptyStateVisible = !driver.findElements(NO_SUBMITTED_RATINGS_MESSAGE).isEmpty();
			String pageSource = driver.getPageSource();
			boolean commentVisible = expectedComment != null && !expectedComment.isBlank() && pageSource.contains(expectedComment);
			boolean result = submittedRatingsContainer.isDisplayed() && (!emptyStateVisible || commentVisible);

			logger.info("[verifySavedRatingVisibleInAllRatingsAfterRefresh] emptyStateVisible=" + emptyStateVisible
					+ " commentVisible=" + commentVisible + " result=" + result);
			return result;
		} catch (Exception e) {
			logger.error("[verifySavedRatingVisibleInAllRatingsAfterRefresh] Failed while verifying saved rating in All Ratings after refresh.", e);
			return false;
		}
	}
}