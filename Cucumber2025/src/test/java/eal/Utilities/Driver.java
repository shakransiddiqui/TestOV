package eal.Utilities;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Driver {

	//Assignment 1: Why do we create a Constructor?

	// Constructor is needed to create an object of the respective Class.
	// We can modify the default Constructor using different Input Parameters to pass specific different arguments through them.

	// Constructor:
	public Driver() {

	}

	private static WebDriver driver;
	public static final Logger logger = LogManager.getLogger(Driver.class);

	// Default download path:
	public static String downloadFolderPath = System.getProperty("user.dir")+"\\Downloads";

	public static String browser = System.getProperty("browser", "chrome");

//	************************************************************************************************************************************************************

	// Method to Launch Browser in Pipelines:
	public static WebDriver getDriver() {

		if (driver==null) {

			//		Backup Code if browser is not given in maven command / pipeline:
			if(browser==null || browser.isBlank()) {
				logger.info(LogColor.RED + "Current Browser is null, so Launching Edge by Default for Pipeline" + LogColor.RESET);
				browser = "edge";
			}
			
//			Initializing Options:
			EdgeOptions edgeOptions = new EdgeOptions();
			ChromeOptions chromeOptions = new ChromeOptions();
			
//			key–value containers to store browser preferences. These preferences control downloads, popups, zoom, notifications, etc.
			HashMap<String, Object> edgePrefs = new HashMap<>();
			HashMap<String, Object> chromePrefs = new HashMap<>();
			
	 		
			switch (browser) {
			
			case "edge": 
// Set up preferences
				edgePrefs.put("download.default_directory", downloadFolderPath); // Sets the download folder path.
				edgePrefs.put("profile.default_content_settings.popups", 0); // Disables popup dialogs.
				edgePrefs.put("profile.default_zoom_level", 0); // Forces default zoom (100%).
				edgePrefs.put("profile.default_content_setting_values.automatic_downloads", 1); // Allows multiple automatic downloads without prompts.
				edgePrefs.put("download.prompt_for_download", false); // Prevents download confirmation popups. Disables “Save as” dialog
				edgeOptions.setExperimentalOption("prefs", edgePrefs); // Injects all preferences into Edge before launch.
// Arguments: 				
				edgeOptions.addArguments("--inprivate"); // Launches Edge in private mode.
				edgeOptions.addArguments("disable-gpu"); // Disable GPU acceleration
				edgeOptions.addArguments("--disable-notifications"); // Blocks browser notifications.
				edgeOptions.addArguments("--no-download-notification"); // Suppresses download toast notifications.
				edgeOptions.addArguments("--window-size=1920,1080"); // Forces consistent viewport size.
				edgeOptions.addArguments("--force-device-scale-factor=1"); // Ensures correct/normal DPI scaling.
				edgeOptions.addArguments("--high-dpi-support=1"); // Prevents blurry rendering or element offset issues.

				// Generate a unique user data directory
				logger.info("creating temp directory in -Edge");
				String tempUserDataDir = System.getProperty("java.io.tmpdir") + "/edge-profile-" + UUID.randomUUID(); // Creates a unique temporary browser profile directory.
				
				edgeOptions.addArguments("--user-data-dir=" + tempUserDataDir); // Forces Edge to use that temp profile.
				
				// Save it for cleanup
				System.setProperty("edge.temp.profile", tempUserDataDir); // Saves the path so it can be cleaned up later.

				// launch Browser
				driver = new EdgeDriver(edgeOptions); // Starts Edge with all defined options.

				break;		
				
			case "chrome":
			// Set up preferences
			chromePrefs.put("download.default_directory", downloadFolderPath);
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("profile.default_zoom_level", 0);
			chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
			chromePrefs.put("download.prompt_for_download", false);

			chromeOptions.setExperimentalOption("prefs", chromePrefs); // Injects all preferences into chrome before launch.

			// Arguments
			chromeOptions.addArguments("--incognito"); // Chrome equivalent of Edge's inprivate
			chromeOptions.addArguments("--disable-gpu"); // Disable GPU acceleration . Pipeline stability
			chromeOptions.addArguments("--disable-notifications");
			chromeOptions.addArguments("--no-download-notification");
			chromeOptions.addArguments("--window-size=1920,1080");
			chromeOptions.addArguments("--force-device-scale-factor=1");
			chromeOptions.addArguments("--high-dpi-support=1");

			// Generate a unique user data directory
			logger.info("creating temp directory in -Chrome");
			String tempUserDataDirchrome = System.getProperty("java.io.tmpdir") + "/chrome-profile-" + UUID.randomUUID(); // Creates a unique temporary browser profile directory.
			chromeOptions.addArguments("--user-data-dir=" + tempUserDataDirchrome); // Forces Chrome to use that temp profile.

			// Save it for cleanup
			System.setProperty("chrome.temp.profile", tempUserDataDirchrome); // Saves the path so it can be cleaned up later.

			// Launch Browser
			driver = new ChromeDriver(chromeOptions); // Starts Edge with all defined options.

			break;
			
			case "chrome-headless":

			    // Set up preferences:
			    chromePrefs.put("download.default_directory", downloadFolderPath);
			    chromePrefs.put("profile.default_content_settings.popups", 0);
			    chromePrefs.put("profile.default_zoom_level", 0);
			    chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
			    chromePrefs.put("download.prompt_for_download", false);

			    chromeOptions.setExperimentalOption("prefs", chromePrefs);

			    // Arguments:
			    chromeOptions.addArguments("--headless=new"); // Enable headless mode (Uses new headless engine (Chrome 109+)).Runs Chrome without UI.
			    chromeOptions.addArguments("--incognito");    // Chrome equivalent of Edge's in-private
			    chromeOptions.addArguments("--disable-gpu");  // Disable GPU acceleration. GPU disabled even though UI isn’t visible.
			    chromeOptions.addArguments("--no-sandbox"); // Required in Docker and Jenkins Linux agents.
			    chromeOptions.addArguments("--disable-dev-shm-usage"); // Prevents /dev/shm memory crashes in containers.
			    chromeOptions.addArguments("--window-size=1920,1080");
			    chromeOptions.addArguments("--force-device-scale-factor=0.75"); // Headless rendering can overscale elements.
			    chromeOptions.addArguments("--high-dpi-support=0.75"); // These keep layouts consistent with real UI.

			    // Generate a unique user data directory
			    logger.info("creating temp directory in -chrome_headless");
			    String tempUserDataDirHeadless = System.getProperty("java.io.tmpdir") + "/chrome-profile-" + UUID.randomUUID(); // Creates a unique temporary browser profile directory.
			    chromeOptions.addArguments("--user-data-dir=" + tempUserDataDirHeadless);

			    // Save it for cleanup
			    System.setProperty("chrome.temp.profile", tempUserDataDirHeadless); // Saves the path so it can be cleaned up later.

			    // Launch Browser
			    driver = new ChromeDriver(chromeOptions);
			    break;
			    
			case "edge-headless":

// Set up preferences:
				edgePrefs.put("download.default_directory", downloadFolderPath);
				edgePrefs.put("profile.default_content_settings.popups", 0);
				edgePrefs.put("profile.default_zoom_level", 0);
				edgePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
				edgePrefs.put("download.prompt_for_download", false);
				edgeOptions.setExperimentalOption("prefs", edgePrefs);

//	Arguments:			
				edgeOptions.addArguments("--headless=new"); // Enable headless mode
				edgeOptions.addArguments("--inprivate");
				edgeOptions.addArguments("disable-gpu"); // Disable GPU acceleration
				edgeOptions.addArguments("--no-sandbox");
				edgeOptions.addArguments("--disable-dev-shm-usage");
				edgeOptions.addArguments("--window-size=1920,1080");
				edgeOptions.addArguments("--force-device-scale-factor=0.75");
				edgeOptions.addArguments("--high-dpi-support=0.75");

// Generate a unique user data directory
				logger.info("creating temp directory in -edge_headless");
				String tempUserDataDirheadless = System.getProperty("java.io.tmpdir") + "/edge-profile-"+ UUID.randomUUID();
				edgeOptions.addArguments("--user-data-dir=" + tempUserDataDirheadless);

// Save it for cleanup
				System.setProperty("edge.temp.profile", tempUserDataDirheadless);

				driver = new EdgeDriver(edgeOptions);

//				devicce matrics

				break;
			}
		}
		
		driver.manage().deleteAllCookies(); // Clears all cookies immediately. Guarantees clean session start regardless of browser mode.
	 
		return driver;  // Hands the fully configured browser to the test.
	} // ---getDriver() ends.

//	************************************************************************************************************************************************************

	//Method to Close Browser:
	public static void closeDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	} // ---closeDriver() ends.
	
//	************************************************************************************************************************************************************

	public static void BrowserSetup() {  // This is a global browser initializer.
		
		//launching URL:
		logger.info(LogColor.Purple + "Browser Setup" + LogColor.RESET);
		Driver.getDriver().manage().deleteAllCookies();  // Clears all cookies from the browser session
		Driver.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(15)); // Tells Selenium: “When searching for an element, wait up to 15 seconds before failing.”
		Driver.getDriver().manage().window().maximize(); // Maximizes the browser window
		String URL = ConfigurationReader.getProperty("url"); // Fetches the url value from a config file
		Driver.getDriver().get(URL); // Opens the browser. Navigates to the specified URL
		logger.info("URL Launched: " +ConfigurationReader.getProperty("url"));
		
//		CommonMethods.waitForPageAndAjaxToLoad();  // combo of: document.readyState === "complete",, jQuery AJAX checks,, Network idle conditions
		// This line ensures: Page loaded + background calls done + UI stable
	} // ---BrowserSetup() ends
	
}
