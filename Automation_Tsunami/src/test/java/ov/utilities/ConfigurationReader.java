package ov.utilities;


import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
What this ConfigurationReader class does ???

It loads the correct .properties file once at startup and provides a global way to read configuration values.

When the class is first used, the JVM automatically runs the static block. It checks if a JVM argument exists: -Dprofile=Something

Based on that:

If no profile is given:
loads Configuration_RealBrowser.properties

If a profile is given:
loads <profile>.properties
 */
public class ConfigurationReader {

	private static Properties configFile;  // Declares a static Properties object and Holds all key–value pairs from the .properties file

	public static final Logger logger = LogManager.getLogger(ConfigurationReader.class);

	//	*******************************************************************************************************************************************************************
	static {

		try {
			//String path = "Configuration.properties";
			String path; // Declares a variable to hold the config file path and Value decided Dynamically
			String profile = System.getProperty("profile"); // Get active profile 
			logger.info(LogColor.Magenta+"****************** Current Profile: "+profile+"******************"+LogColor.RESET);



			if (profile == null) {
				// Default to configuration.properties if no profile is specified
				path = "Configuration.properties";
				logger.info("Current configFile ="+path);
			}
			else {
				// Load profile-specific configuration
				path = profile + ".properties";
				logger.info("Current configFile ="+path);
			}

			FileInputStream input = new FileInputStream(path); // Opens a stream to the .properties file

			configFile = new Properties(); // Instantiates the Properties container,, Empty at this moment
			configFile.load(input); // Reads file contents. Converts: key=value into: configFile.getProperty("key")
			//			After this line:  Config values are cached in memory and No more file access needed.

			input.close(); // Releases file handle,, Prevents memory leaks,, Required for Windows systems

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	***************************************************************************************************************************************************************
	public static String getProperty(String keyName) {  // Fetches value by key
		return configFile.getProperty(keyName);  // If properties file contains: browser=chrome,, Then: getProperty("browser"),, Returns: "chrome"

	}


}