//package ov.step_definitions.passport;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.asserts.SoftAssert;
//import java.io.File;
//
//import org.testng.asserts.SoftAssert;
//import groovy.time.Duration;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import static org.junit.Assert.assertTrue;
//
//import io.cucumber.java.en.*;
//import junit.framework.Assert;
//import ov.pages.passport.*;
//import ov.utilities.CommonMethods;
//import ov.utilities.ConfigurationReader;
//import ov.utilities.Driver;
//
//
//
//public class StartupProgramApplicationSteps extends CommonMethods  {
//
//	//use logger after class
//	public static final Logger logger = LogManager.getLogger(SignUpStepDefinition.class);
//    HomePage_Passport homePage=new HomePage_Passport();
//    RoleSelectionPage roleSelectionPage=new RoleSelectionPage();
//    StartupPage startupPage=new StartupPage();
//    SearchProgramPage searchProgramPage=new SearchProgramPage();
//    CreateAppliction createApplication=new CreateAppliction();
//
//
//
//
//	public String generateUniqueEmail() {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
//		String timestamp = LocalDateTime.now().format(formatter);
//		return "QA_TESTER+tests_" + timestamp + "@theonevalley.com";
//	}
//
//		@Given("User completes initial startup setup successfully")
//		public void user_completes_initial_startup_setup_successfully() {
//			homePage.click_on_signup_button();
//			String uniqueEmail = generateUniqueEmail();
//			logger.info("Generated unique email: " + uniqueEmail);
//			homePage.Enter_a_valid_email_address_on_Signup(uniqueEmail);
//			String Password=ConfigurationReader.getProperty("password");
//			logger.info("password is filled up");
//			homePage.enter_valid_password_meeting_all_requirement(Password);
//			String fullName = "Test User";
//			logger.info("Entering full name: " + fullName);
//			homePage.enterFullName(fullName);
//			String jobTitle = "QA Automation Engineer";
//			homePage.enterJobTitle(jobTitle);
//			homePage.termsCheckboxLabel.click();
//			try {
//				  System.out.println(" DEBUG: About to click the Sign Up button.");
//				logger.info("Clicking the Sign Up button.");
//				homePage.submitButton.click();
//
//			} catch (Exception e) {
//				 System.out.println("DEBUG: Exception caught - " + e.getMessage());
//				logger.info(e);
//			}
//			waitFor(10);
//			try {
//				roleSelectionPage.clickStartupLeader();
//
//				softAssert.assertTrue(
//						roleSelectionPage.StartUpLeader.isSelected(),
//						"Startup Leader radio button should be selected"
//						);
//
//				logger.info("Startup Leader option is selected successfully.");
//			} catch (Exception e) {
//				logger.info("Exception caught while selecting Startup Leader: " + e.getMessage());
//				throw e;
//			}
//			try {
//				softAssert.assertTrue(
//						roleSelectionPage.continueButton.isEnabled(),
//						"Continue button should be enabled"
//						);
//
//				roleSelectionPage.clickContinueButton();
//				logger.info("Continue button clicked successfully.");
//			} catch (Exception e) {
//				logger.info("Exception while clicking Continue button: " + e.getMessage());
//				throw e;
//			}
//            waitFor(5);
//			softAssert.AssertAll();
//			try {
//				Assert.assertTrue(startupPage.isStartupPageDisplayed());
//
//
//			} catch (AssertionError e) {
//
//
//				logger.info("Assertion failed: " + e.getMessage());
//				throw e;
//			}
//			waitFor(5);
//			try {
//				boolean isVisible = startupPage.isOnStartupInformationPage();
//				softAssert.assertTrue(isVisible, "Startup Information Page is NOT displayed!");
//				logger.info("User is on the Startup Information page.");
//			} catch (Exception e) {
//				logger.error("Startup Information page check failed: " + e.getMessage());
//				softAssert.fail("Exception occurred while checking the Startup Information page.");
//			}
//			waitFor(5);
//			try {
//				 String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
//			        String uniqueStartupName = "TestStartup_" + timestamp;
//
//			        startupPage.enterStartupName(uniqueStartupName);
//			        logger.info(" Successfully entered startup name: " + uniqueStartupName);
//
//
//			        boolean isNameAccepted = true;
//			        softAssert.assertTrue(isNameAccepted, "Startup name should be accepted");
//
//			    } catch (Exception e) {
//			       logger.error("Failed to enter startup name: " + e.getMessage());
//			        softAssert.fail("Startup name input failed: " + e.getMessage());
//			}
//			try {
//				Thread.sleep(5000);
//				startupPage.enterLocation("San Francisco ,CA, USA");
//				softAssert.assertTrue(true, "Location entered successfully");
//			} catch (Exception e) {
//				softAssert.fail("Failed to enter location: " + e.getMessage());
//			}
//		 softAssert.AssertAll();
//          waitFor(5);
//		  try {
//
//		        logger.info("Clicked finish button.");
//
//		        startupPage.clickFinishButton();
//		        Thread.sleep(10000);
//		        logger.info("URL after clicking finish button: " + driver.getCurrentUrl());
//		    } catch (Exception e) {
//		        logger.error("Error clicking finish button: " + e.getMessage());
//		    }
//		  waitFor(5);
//		  try {
//		        waitForUrlContains("/searchprogram");
//		        String currentUrl = driver.getCurrentUrl();
//		        logger.info("Current URL is: " + currentUrl);
//
//		        softAssert.assertTrue(
//		            currentUrl.contains("/searchprogram"),
//		            "User is not redirected to search program page. Current URL: " + currentUrl);
//
//		        logger.info("User is successfully redirected to the search program page.");
//		    } catch (Exception e) {
//		        softAssert.fail("Failed to verify redirection to search page: " + e.getMessage());
//		    }
//		}
//
//		@And("Click on \"See More\" button for Startup Program")
//		public void click_on_see_more_button_for_startup_program() {
//		    try {
//		        logger.info("STEP: Click on 'See More' button for Startup Program");
//
//		        // Action from Page Object
//		        searchProgramPage.clickOnSeeMoreButton();
//
//		        // Optional validation after clicking
//		        logger.info("'See More' button clicked successfully and next section loaded.");
//		    }
//		    catch (Exception e) {
//		        logger.error("Error while clicking 'See More' button for Startup Program: " + e.getMessage());
//		        softAssert.fail("Exception occurred while clicking 'See More' button for Startup Program.");
//		    }
//
//		}
//
//		@When("Click on \"Apply Now\" button for Startup Program")
//		public void click_on_apply_now_button_for_startup_program() {
//			try {
//				waitFor(15);
//		        createApplication.clickApplyNowButton();
//		        waitFor(5);
//		        String currentWindow=Driver.getDriver().getWindowHandle();
//		        logger.info("Clicked on Apply Now button successfully.");
//		        for (String handle : Driver.getDriver().getWindowHandles()) {
//		        	if(!currentWindow.equalsIgnoreCase(handle)) {
//		            Driver.getDriver().switchTo().window(handle);
//		        	}
//		        }
//		    } catch (Exception e) {
//		        logger.error("Failed to click Apply Now button: " + e.getMessage());
//		        Assert.fail("Error clicking Apply Now button");
//		    }
//		}
//
//
//
//		@And("User fills the application form using existing reusable method")
//		public void user_fills_the_application_form_using_existing_reusable_method() {
//			    createApplication.companyDescriptionTextField("This is tester");
//			    createApplication.enterYearOfFounding("browser");
//			    createApplication.selectFundingOption("None");
//			    createApplication.enterNumberOfEmployees("34");
//			    createApplication.enterMilestones("Launched MVP, onboarded first 10 users, and raised pre-seed funding.");
//			}
//
//
//
//		@And("Upload pitch deck file")
//		public void upload_pitch_deck_file() {
//			try {
//		        logger.info("Uploading pitch deck file...");
//
//		      String filePath = System.getProperty("user.dir")
//		                + File.separator + "test-data"
//		                + File.separator + "pitchdeck.jpg";
//
//		        logger.info("File path used for upload: " + filePath);
//
//		         createApplication.uploadFile();
//
//		        logger.info("Pitch deck file uploaded successfully.");
//
//		    } catch (Exception e) {
//		        logger.error("Failed to upload pitch deck file: " + e.getMessage());
//		        Assert.fail("Pitch deck file upload failed due to exception: " + e.getMessage());
//		    }
//		}
//
//
//
//		@And("Check {string} checkbox")
//		public void check_checkbox(String string) {
//			 try {
//			        logger.info("Checking 'Save my responses' checkbox...");
//
//			       createApplication.checkSaveMyResponsesCheckbox();
//
//			        logger.info("'Save my responses' checkbox checked successfully.");
//
//			    } catch (Exception e) {
//			        logger.error("Failed to check 'Save my responses' checkbox: " + e.getMessage());
//			        Assert.fail("Could not check 'Save my responses' checkbox due to exception: " + e.getMessage());
//			    }
//			}
//
//
//		@And("Click {string} button")
//		public void click_button(String string) {
//			 try {
//			        logger.info("Clicking on 'Submit' button...");
//
//			       createApplication.clickSubmitButton();
//
//			        logger.info("'Submit' button clicked successfully.");
//
//			    } catch (Exception e) {
//			        logger.error("Failed to click on 'Submit' button: " + e.getMessage());
//			        Assert.fail("Could not click 'Submit' button due to exception: " + e.getMessage());
//			    }
//			}
//
//
//
//		@And("Verify that the success message {string} is displayed")
//		public void verify_that_the_success_message_is_displayed(String string) {
//			logger.info("Checcking Confimation Dialouge message as {} ...",string);
//            waitFor(10);
//	        String message=createApplication.getTheConFirmationDialougeMessage();
//
//	        logger.info("Getting Confimation Dialouge message as {}",message);
//
//	        Assert.assertEquals(string, message);
//
//		}
//
//
//
//
//
//}