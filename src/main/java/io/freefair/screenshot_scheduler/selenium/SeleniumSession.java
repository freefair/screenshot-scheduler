package io.freefair.screenshot_scheduler.selenium;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class SeleniumSession {
	@Getter
	private WebDriver driver;

	public SeleniumSession(int implicitlyWait, int height, int width) {
		var options = new ChromeOptions();
		options.addArguments("--headless");
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(width, height));
	}

	public void waitForBody() {
		driver.findElement(By.cssSelector("body"));
	}
}