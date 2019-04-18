package io.freefair.screenshot_scheduler.selenium;

import io.freefair.screenshot_scheduler.models.Screenshot;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

@Slf4j
public class ScreenshotThread implements Runnable {
	private final ScheduledSeleniumSession scheduledSeleniumSession;
	private final Screenshot screenshot;
	private String outputDirectory;
	private Thread thread;

	public ScreenshotThread(ScheduledSeleniumSession scheduledSeleniumSession, Screenshot screenshot, String outputDirectory) {
		this.scheduledSeleniumSession = scheduledSeleniumSession;
		this.screenshot = screenshot;
		this.outputDirectory = outputDirectory;
		thread = new Thread(this);
	}

	public void start() {
		thread.start();
	}

	public void join() throws InterruptedException {
		thread.join();
	}

	@Override
	public void run() {
		try {
			((RemoteWebDriver)scheduledSeleniumSession.getSession().getDriver()).executeScript("window.scrollTo(0, " + scheduledSeleniumSession.getYScroll() + ")");
			SeleniumHelper.createScreenshot(scheduledSeleniumSession.getSession().getDriver(), new File(new File(outputDirectory), screenshot.getId().toString() + ".png"));
		} catch (IOException e) {
			log.error("Error while creating screenshot", e);
		}
	}
}
