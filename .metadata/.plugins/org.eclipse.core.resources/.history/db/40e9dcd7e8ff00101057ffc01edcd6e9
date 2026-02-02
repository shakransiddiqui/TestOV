package ov.utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class ScreenshotUtil {
	
	private WebDriver driver;
    private ExtentTest test;

    public ScreenshotUtil(WebDriver driver, ExtentTest test) {
        this.driver = driver;
        this.test = test;
    }

    private String saveScreenshot(String screenshotName) throws IOException {
        // Ensure screenshot directory exists
        File screenshotDir = new File("screenshot");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }

        String relativePath = "../screenshot/" + screenshotName + ".png";
        String fullPath = "screenshot/" + screenshotName + ".png";

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(fullPath));

        return relativePath;
    }

    public void captureFailure(String message) {
        String screenshotName = "Failure_" + System.currentTimeMillis();
        try {
            String screenshotPath = saveScreenshot(screenshotName);
            test.log(Status.FAIL, message,
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } catch (IOException e) {
            test.log(Status.FAIL, message + " (Screenshot failed: " + e.getMessage() + ")");
        }
    }

    public void captureInfo(String message) {
        test.log(Status.INFO, message);
    }

    public void captureScreenshot(String screenshotName) {
        try {
            String screenshotPath = saveScreenshot(screenshotName);
            test.log(Status.INFO, "Screenshot: " + screenshotName,
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } catch (IOException e) {
            test.log(Status.FAIL, "Failed to capture screenshot: " + e.getMessage());
        }
    }

    public void captureScreenshotWithNote(String note) {
        String screenshotName = "Step_" + System.currentTimeMillis();
        try {
            String screenshotPath = saveScreenshot(screenshotName);
            test.log(Status.INFO, note,
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } catch (IOException e) {
            test.log(Status.FAIL, "Failed to capture screenshot: " + e.getMessage());
        }
    }

    public void captureScreenshotInNode(String nodeTitle, String note) {
        ExtentTest node = test.createNode(nodeTitle);
        String screenshotName = "Node_" + System.currentTimeMillis();
        try {
            String screenshotPath = saveScreenshot(screenshotName);
            node.log(Status.INFO, note,
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } catch (IOException e) {
            node.log(Status.FAIL, note + " (Screenshot failed: " + e.getMessage() + ")");
        }
    }

    public ExtentTest getTest() {
        return test;
    }

}
