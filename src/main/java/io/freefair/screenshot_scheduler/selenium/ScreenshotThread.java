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
	private SeleniumHelper helper;
	private Thread thread;

	public ScreenshotThread(ScheduledSeleniumSession scheduledSeleniumSession, Screenshot screenshot, String outputDirectory, SeleniumHelper helper) {
		this.scheduledSeleniumSession = scheduledSeleniumSession;
		this.screenshot = screenshot;
		this.outputDirectory = outputDirectory;
		this.helper = helper;
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
			int zoomInPercent = screenshot.getZoomLevel() * 10;
			((RemoteWebDriver)scheduledSeleniumSession.getSession().getDriver()).executeScript("document.body.style.zoom = '" + zoomInPercent + "%';");
			((RemoteWebDriver)scheduledSeleniumSession.getSession().getDriver()).executeScript("window.scrollTo(0, " + scheduledSeleniumSession.getYScroll() + ")");
			helper.createScreenshot(scheduledSeleniumSession.getSession().getDriver(), new File(new File(outputDirectory), screenshot.getId().toString() + ".png"), screenshot.isTimestamp());
		} catch (IOException e) {
			log.error("Error while creating screenshot", e);
		}
	}
}
