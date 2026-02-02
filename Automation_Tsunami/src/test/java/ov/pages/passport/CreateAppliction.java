package ov.pages.passport;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;  
import io.netty.handler.timeout.TimeoutException;
import junit.framework.Assert;
import ov.utilities.CommonMethods;
import ov.utilities.Driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.io.File;



public class CreateAppliction extends CommonMethods{
	public WebDriver driver ;
	public WebDriverWait wait;

	public static final Logger logger = LogManager.getLogger(CreateAppliction.class);
	public CreateAppliction () { 
		this.driver=Driver.getDriver();
		PageFactory.initElements(driver, this);
		wait=new WebDriverWait(driver,Duration.ofSeconds(20 ));
		
	} 
	
   
	@FindBy(css="[name='Application Title']")
	private WebElement applicationTitle_Field;
	
	@FindBy(css="[type='radio']")
	private List<WebElement>types_off_radioBtn;
	
	
	@FindBy(css="[class='TBaseButton primary footer-action-btn footer-action-btn-save']")
	private WebElement saveBtn;
	
	@FindBy(css="[class='THoverText']>span")
	private List<WebElement>applicationBuilderHeader;
	
	@FindBy(xpath="//div[text()='Additional Questions']")
	private WebElement applicationBuilderAdditionalheader;
	
	@FindBy(xpath="//input[contains(@id,'question-label')]")
	private List<WebElement> questionBoxfield;
	
	@FindBy(css="[class='ql-editor ql-blank']")
	private List<WebElement> instructionfield;
	
	@FindBy(css="[class='required-question-input']")
	private List<WebElement> requiredCheckBoxField;
	
	@FindBy(css="[aria-label='SAVE']")
	private WebElement SaveButton;
	
	@FindBy(css="[class='circle-button primary section-add-question-btn']")
	private WebElement addNewQuestionButton;
	
	@FindBy(css="[class='TBaseButton primary footer-action-btn footer-action-btn-save']")
	private WebElement SaveAndContinueButton;
	
	@FindBy(css="[class='TBaseButton secondary footer-action-btn footer-action-btn-cancel']")
	private WebElement CancelButton;
	
	@FindBy(xpath = "//div[contains(text(),'Company Description')]/following::div[contains(@class,'t-toggle-switch')][1]")
	private WebElement companyDescriptionToggle;
	
	@FindBy(xpath = "(//div[@class='t-toggle-switch'])[2]")
	private WebElement yearOfFoundingToggle;
	
	@FindBy(xpath = "//*[contains(@class,'is-hidden-option-container active')]")
	private List<WebElement> StandardQuestionsToggleBtn;
	
	@FindBy(xpath = "//*[@class='collapse-btn']")
	private List<WebElement> expnadCollapseBtn;
	
	@FindBy(xpath = "//button[contains(@class,'preview-btn') and span[text()='Preview Application']]")
	public WebElement previewApplicationButton;
	
	 //Tc_17 _____________________
	 @FindBy(css = "a.TBaseButton.redirect-btn-link.primary.cohort-action-btn.is-link")
	 private WebElement applyNowBtn;
	 
	 @FindBy(xpath = "(//div[contains(@class,'ql-editor') and @contenteditable='true'])[1]")
	 private WebElement companyDescriptionTextField;
	 
	 @FindBy(xpath = "//input[@aria-label='Year of Founding']")
	 public WebElement yearOfFoundingInput;
	 
	 @FindBy(xpath = "//div[contains(@class,'multiselect') and @role='combobox']")
	 public WebElement fundingDropdown;

	 @FindBy(xpath = "//input[contains(@class,'multiselect__input')]")
	 public WebElement fundingInputField;
	 
	 @FindBy(xpath = "//input[@aria-label='Number of full-time employees']")
	 private WebElement numberOfEmployeesField;
	 
	 @FindBy(xpath = "(//div[contains(@class,'ql-editor') and @contenteditable='true'])[2]")
	 private WebElement milestonesTextBox;
	 
	 @FindBy(xpath = "//input[@type='file' and contains(@class, 'TInputFile')]")
	 private WebElement hiddenPitchDeckInput;
	 
	 @FindBy(xpath = "//span[contains(text(),'Save my responses')]/preceding-sibling::input[@type='checkbox']")
	 public WebElement saveMyResponsesCheckbox;
	 
//	 @FindBy(xpath = "//button[contains(@class,'save-form-btn') and @aria-label='Submit']")
//	 public WebElement submitButton;
     
	 @FindBy(xpath = "//div[@class='collapse-btn']//span")
     public WebElement collapseButton;
	 
	


	//actions
	
	
	public void clickOnCancelApplicationButton()
	{
		wait.until(ExpectedConditions.elementToBeClickable(CancelButton));
		CancelButton.click();
	}
	
	public void enterApplicationTitle(String title)
	{
		wait.until(ExpectedConditions.elementToBeClickable(applicationTitle_Field));
		applicationTitle_Field.sendKeys(title);
	}
	

	
	
	public void selectApplicationType()
	{
		wait.until(ExpectedConditions.visibilityOfAllElements(types_off_radioBtn));
		types_off_radioBtn.get(0).click();
	}

	public void clickOnSaveButton()
	{
		wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
		saveBtn.click();
	}
	
	public void clickOnSaveAndContinueButton()
	{
		wait.until(ExpectedConditions.elementToBeClickable(SaveAndContinueButton));
		SaveAndContinueButton.click();
	}
	
	public boolean isUserOnApplicationBuilderPage(String uri)
	{
	  return wait.until(ExpectedConditions.urlContains(uri));
	}
	
	public boolean isApplicationBuilderHeaderDisplayed(String uri)
	{
		wait.until(ExpectedConditions.visibilityOfAllElements(applicationBuilderHeader));
		return applicationBuilderHeader.get(0).isDisplayed();
	
	}
	
	
	public boolean isApplicationBuilderAdditionalHeaderDisplayed(String uri)
	{
		wait.until(ExpectedConditions.elementToBeClickable(applicationBuilderAdditionalheader));
		return applicationBuilderAdditionalheader.isDisplayed();
	
	}
	
	public void fillAdditionalQuestionFormAndSave(String questionTitle,String questionInstruction,String anserType)
	{
		
		if(questionBoxfield.size()!=0 && instructionfield.size()!=0)
		{
			questionBoxfield.get(0).sendKeys(questionTitle);
			instructionfield.get(0).sendKeys(questionInstruction);
			WebElement dropdown = driver.findElement(By.cssSelector("[name='filterByTagTag']"));
	        Select select = new Select(dropdown);
	        select.selectByVisibleText(anserType);
	        requiredCheckBoxField.get(0).click();
	        SaveButton.click();
		}
		
		wait.until(ExpectedConditions.elementToBeClickable(addNewQuestionButton));
		addNewQuestionButton.click();
	}
	
	@SuppressWarnings("finally")
	public boolean isCompanyDescriptionExpanded()
	{
		boolean flag=false;
		try { 
	     wait.until(ExpectedConditions.visibilityOf(companyDescriptionTextField));
		}catch(Exception e) {
			flag=true;
		}finally {
			return flag;
		}
	}
	
	public void clickOnCollapseButton() {
		wait.until(ExpectedConditions.elementToBeClickable(collapseButton));
		collapseButton.click();
	}
	public void clickOnCompanyDescriptionToggleButton()
	{
//		wait.until(ExpectedConditions.visibilityOfAllElements(expnadCollapseBtn));
//		expnadCollapseBtn.get(0).click();
		wait.until(ExpectedConditions.visibilityOfAllElements(StandardQuestionsToggleBtn));
		StandardQuestionsToggleBtn.get(0).click();
	}
	
	public void clickOnYearOfFoundingToggleButton()
	{
		wait.until(ExpectedConditions.visibilityOfAllElements(StandardQuestionsToggleBtn));
		StandardQuestionsToggleBtn.get(1).click();
	}
	
	public void clickOnFundingToggleButton()
	{
		wait.until(ExpectedConditions.visibilityOfAllElements(StandardQuestionsToggleBtn));
		StandardQuestionsToggleBtn.get(2).click();
	}
	
	public void clickOnNumberOfFullTimeEmployeesToggleButton()
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block: 'center', inline:'center'});",StandardQuestionsToggleBtn.get(3));
		wait.until(ExpectedConditions.visibilityOfAllElements(StandardQuestionsToggleBtn));
		StandardQuestionsToggleBtn.get(3).click();
	}
	
	public void clickOnMilestonesToggleButton()
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].scrollIntoView({block: 'center', inline:'center'});",StandardQuestionsToggleBtn.get(4));
		wait.until(ExpectedConditions.visibilityOfAllElements(StandardQuestionsToggleBtn));
		StandardQuestionsToggleBtn.get(4).click();
	}
	
	
	public void clickOnPitchDeckToggleButton()
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block: 'center', inline:'center'});",StandardQuestionsToggleBtn.get(5));
		wait.until(ExpectedConditions.visibilityOfAllElements(StandardQuestionsToggleBtn));
		StandardQuestionsToggleBtn.get(5).click();
	}
	
	
//	public void clickPreviewApplicationButton() {
//	    try {
//	        JavascriptExecutor js = (JavascriptExecutor) driver;
//
//	        // Wait until button exists in DOM (safer for headless)
//	        wait.until(ExpectedConditions.presenceOfElementLocated(
//	                By.xpath("//button[contains(@class,'preview-btn') and span[text()='Preview Application']]")
//	        ));
//
//	        // Scroll into view to avoid navbar blocking in Linux headless
//	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", previewApplicationButton);
//
//	        // Small pause for rendering stability
//	        Thread.sleep(500);
//
//	        // Click with JS to bypass 'element click intercepted'
//	        js.executeScript("arguments[0].click();", previewApplicationButton);
//
//	        logger.info("Preview Application button clicked using JavaScript Executor.");
//
//	    } catch (Exception e) {
//	        logger.error("Failed to click Preview Application button: " + e.getMessage());
//	        Assert.fail("Could not click Preview Application button due to exception: " + e.getMessage());
//	    }
//	}
	
	public void clickPreviewApplicationButton() {
	    try {
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        // 1 — Wait until DOM contains the button
	        By locator = By.xpath("//button[contains(@class,'preview-btn') and span[text()='Preview Application']]");
	        wait.until(ExpectedConditions.presenceOfElementLocated(locator));

	        WebElement button = driver.findElement(locator);

	        // 2 — Allow headless UI to finish rendering
	        Thread.sleep(1000);

	        // 3 — Scroll into center view
	        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline:'center'});", button);

	        Thread.sleep(500);

	        // ⭐ 4 — EXTRA SCROLL DOWN (fix for pipeline navbar blocking)
	        js.executeScript("window.scrollBy(0, 300);");

	        Thread.sleep(500);

	        // 5 — JS click (bypasses all interception)
	        js.executeScript("arguments[0].click();", button);

	        logger.info("Preview Application button clicked using JavaScript Executor (pipeline safe).");

	    } catch (Exception e) {
	        logger.error("Failed to click Preview Application button: " + e.getMessage());
	        Assert.fail("Could not click Preview Application button due to exception: " + e.getMessage());
	    }
	}



	
	 public void clickApplyNowButton() {
		    wait.until(ExpectedConditions.elementToBeClickable(applyNowBtn));
		    applyNowBtn.click();
		}
	 public void companyDescriptionTextField(String text) {
		    try {
		        logger.info("Entering Company Description...");
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		        wait.until(ExpectedConditions.visibilityOf(companyDescriptionTextField));
		        companyDescriptionTextField.clear();
		        companyDescriptionTextField.sendKeys(text);
		        logger.info(" Company Description entered: " + text);
		    } catch (Exception e) {
		        logger.error(" Failed to enter Company Description: " + e.getMessage());
		        Assert.fail("Could not enter Company Description.");
		    }
		}
	 public void enterYearOfFounding(String year) {
		    try {
		        logger.info("Entering Year of Founding: " + year);
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		        wait.until(ExpectedConditions.visibilityOf(yearOfFoundingInput));
		        yearOfFoundingInput.clear();
		        yearOfFoundingInput.sendKeys("1975");
		        logger.info("Year of Founding entered successfully.");
		    } catch (Exception e) {
		        logger.error(" Failed to enter Year of Founding: " + e.getMessage());
		        Assert.fail("Could not enter Year of Founding field.");
		    }
	 }
		    
	 public void selectFundingOption(String desiredOption)
	 {
		 WebElement fundingOptionInput=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='select-container']")));
		 
		 
		 //Click on Funding Options 
		 fundingOptionInput.click();
		 //wait
		 try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 
		 WebElement dropDown=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[contains(@class,'multiselect__content')]")));
		 
		 List<WebElement> options=dropDown.findElements(By.xpath(".//li//span[contains(@class , 'multiselect__option')]"));
		 int optionsSize=options.size();
		 for(int i=0; i<optionsSize; i++)
		 {
			 System.out.println("********************* "+ optionsSize +" Options ******************* ");
			 WebElement option=options.get(i);
			 String optionText=option.findElement(By.xpath(".//span")).getText();
			 if(optionText.equalsIgnoreCase(desiredOption)) {

				 ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
				 option.click();
				 break;
			 }
			 
		 }
		 
		 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				
			}
		  
	 }

	 
		    public void enterNumberOfEmployees(String number) {
		        try {
		            logger.info("Entering Number of Full-time Employees...");
		            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		          wait.until(ExpectedConditions.visibilityOf(numberOfEmployeesField));

		          numberOfEmployeesField.clear();

		          numberOfEmployeesField.sendKeys(number);
		            logger.info(" Entered Number of Employees successfully: " + number);

		        } catch (Exception e) {
		            logger.error(" Failed to enter Number of Employees: " + e.getMessage());
		            Assert.fail("Could not fill Number of Employees field: " + e.getMessage());
		        }
		    
		    
		    
		    
	 }
		    
		    public void enterMilestones(String text) {
		        try {
		            logger.info("Entering Milestones text...");
		            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		             wait.until(ExpectedConditions.visibilityOf(milestonesTextBox));

		         milestonesTextBox.clear();

		         milestonesTextBox.sendKeys(text);
		            logger.info(" Milestones entered successfully: " + text);
		        } catch (Exception e) {
		            logger.error(" Failed to enter milestones: " + e.getMessage());
		            Assert.fail("Could not fill Milestones field: " + e.getMessage());
		        }
		    }
		    
//		       
//		        public void uploadFile() {
//		        	String filepath= System.getProperty("user.dir")+"\\test-data\\pitchdeck.jpg";
//		        	logger.info("file path is this *********************** "+filepath);
//		        	WebElement element=driver.findElement(By.xpath("//span[contains(text(),'Pitch')]"));
//		        	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
//		        	WebElement pitchdeckInput=driver.findElement(By.xpath("//input[@type='file' and @aria-label='Pitch Deck']"));
//		        	pitchdeckInput.sendKeys(filepath);
//		        	
//		        	;
		      //  }
		    
		    public void uploadFile() {
		        try {
		            logger.info("Uploading pitch deck file...");

		            // Cross-platform file path
		            File file = new File(System.getProperty("user.dir") + File.separator + "test-data" + File.separator + "pitchdeck.jpg");
		            String absolutePath = file.getAbsolutePath();
		            logger.info("File path resolved to: " + absolutePath);

		            // Scroll to Pitch Deck section
		            WebElement element = driver.findElement(By.xpath("//span[contains(text(),'Pitch')]"));
		            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

		            // Find the hidden input
		            WebElement input = driver.findElement(By.xpath("//input[@type='file' and @aria-label='Pitch Deck']"));

		            // Upload the file
		            input.sendKeys(absolutePath);

		            logger.info("Pitch deck uploaded successfully.");

		        } catch (Exception e) {
		            logger.error("Failed to upload file: " + e.getMessage());
		            Assert.fail("Could not upload pitch deck due to: " + e.getMessage());
		        }
		    }

		        
		            public void checkSaveMyResponsesCheckbox() {
		                WebElement checkbox=driver.findElement(By.xpath("//input[@type='checkbox']"));
		                wait.until(ExpectedConditions.elementToBeClickable(checkbox));
		                checkbox.click();
		                
		            }
		            
		            public void clickSubmitButton() {
		                try {
		                    logger.info("Clicking on Submit button...");

		                    // Wait for the button to appear in the DOM first
		                    By submitBtnLocator = By.xpath("//button[@aria-label='Submit']");
		                    Thread.sleep(15000);
		                    wait.until(ExpectedConditions.presenceOfElementLocated(submitBtnLocator));

		                    // Then wait for it to be visible
		                    WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(submitBtnLocator));

		                    // Then wait for it to be clickable
		                    wait.until(ExpectedConditions.elementToBeClickable(submitButton));

		                    // Try normal click first
		                    try {
		                        submitButton.click();
		                        logger.info("Submit button clicked successfully.");
		                    } catch (Exception e) {
		                        logger.warn("Normal click failed. Trying JS click... " + e.getMessage());
		                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
		                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
		                        logger.info("Submit button clicked using JavaScriptExecutor.");
		                    }

		                } catch (Exception e) {
		                    logger.error("Failed to click Submit button: " + e.getMessage(), e);
		                    Assert.fail("Could not click Submit button due to exception: " + e.getMessage());
		                }
		            }
		            
		       public String getTheConFirmationDialougeMessage() {
		    	   WebElement elementBtn=driver.findElement(By.xpath("//span[@class='confirmation-msg-text']"));
	               wait.until(ExpectedConditions.visibilityOf(elementBtn));
	               return elementBtn.getText();
		    	   
		       }
}