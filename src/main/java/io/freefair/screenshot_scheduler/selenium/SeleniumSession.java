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
	private final int implicitlyWait;

	@Getter
	private final int height;

	@Getter
	private final int width;

	@Getter
	private WebDriver driver;

	public SeleniumSession(int implicitlyWait, int height, int width) {
		this.implicitlyWait = implicitlyWait;
		this.height = height;
		this.width = width;
		init();
	}

	private void init() {
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

	public void stop() {
		if(driver != null) {
			driver.quit();
		}
	}

	public void restart() {
		stop();
		init();
	}
}
