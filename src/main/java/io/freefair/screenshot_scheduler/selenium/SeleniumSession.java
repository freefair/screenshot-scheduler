package io.freefair.screenshot_scheduler.selenium;

import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

public class SeleniumSession {
	private SeleniumConfiguration seleniumConfiguration;

	@Getter
	private WebDriver driver;



	public SeleniumSession(SeleniumConfiguration configuration) {
		this.seleniumConfiguration = configuration;

		init();
	}

	private void init() {

		if (seleniumConfiguration.isUseGrid()) {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setPlatform(Platform.fromString(seleniumConfiguration.getPlatform()));
			capabilities.setBrowserName(seleniumConfiguration.getBrowser());
			driver = new RemoteWebDriver(seleniumConfiguration.getGridURL(), capabilities);
		} else {
			var options = new ChromeOptions();
			options.addArguments(seleniumConfiguration.getChromeArgs());

			driver = new ChromeDriver(options);
		}
		driver.manage().timeouts().implicitlyWait(seleniumConfiguration.getImplicitlyWait(), TimeUnit.SECONDS);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(seleniumConfiguration.getWidth(), seleniumConfiguration.getHeight()));
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
