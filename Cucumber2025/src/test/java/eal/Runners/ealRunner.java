package eal.Runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import eal.Utilities.LogColor;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(
		plugin= {
				"pretty",
				"html:target/default-cucumber-reports/htmlReport.html",
				"json:target/cucumber.json",
				"rerun:traget/cucumber.txt",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"eal.Hooks.StepNameListener"
		},
		features="src/test/resources/features",
		glue= {"eal.step_definitions","eal.Hooks"},
		dryRun = false,
		
		tags = "@test",
		
		monochrome= true
	
		)

public class ealRunner{
	
	public static final Logger logger = LogManager.getLogger(ealRunner.class);

	@BeforeClass
	public static void globalSetup() {
		logger.info(LogColor.ThinnerPurple + "@BeforeClass - Cucumber2025 Runner" + LogColor.RESET);
		
	
	}
	
	@AfterClass
	public static void teardown() {
		logger.info(LogColor.ThinnerPurple + "@AfterClass - Cucumber2025 Runner" + LogColor.RESET);
		
	}
}
