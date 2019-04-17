package io.freefair.screenshot_scheduler.selenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class SeleniumHelper {
	public static void createScreenshot(WebDriver driver, File outputFile) throws IOException {
		byte[] screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		FileUtils.writeByteArrayToFile(outputFile, screenshotAs);
	}
}
