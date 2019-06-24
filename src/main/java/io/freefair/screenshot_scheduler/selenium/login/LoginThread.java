package io.freefair.screenshot_scheduler.selenium.login;

import io.freefair.screenshot_scheduler.models.Screenshot;
import io.freefair.screenshot_scheduler.selenium.ScheduledSeleniumSession;
import io.freefair.screenshot_scheduler.selenium.SeleniumHelper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

@Slf4j
public class LoginThread implements Runnable {
	private final ScheduledSeleniumSession scheduledSeleniumSession;
	private final Screenshot screenshot;
	private ILoginHandler loginHandler;
	private String outputDirectory;
	private SeleniumHelper helper;
	private Thread thread;

	public LoginThread(ScheduledSeleniumSession scheduledSeleniumSession, Screenshot screenshot, ILoginHandler loginHandler, String outputDirectory, SeleniumHelper seleniumHelper) {
		this.scheduledSeleniumSession = scheduledSeleniumSession;
		this.screenshot = screenshot;
		this.loginHandler = loginHandler;
		this.outputDirectory = outputDirectory;
		this.helper = seleniumHelper;
		thread = new Thread(this);
	}

	@Override
	public void run() {
		log.info("Do login for {}", screenshot.getId().toString());
		scheduledSeleniumSession.getSession().getDriver().navigate().to(screenshot.getLoginUrl());
		scheduledSeleniumSession.getSession().waitForBody();
		doScreenshot();
		loginHandler.doLogin(scheduledSeleniumSession.getSession(), screenshot.getAuthenticationInformation());
		doScreenshot();
		scheduledSeleniumSession.getSession().waitForBody();
		scheduledSeleniumSession.getSession().getDriver().navigate().to(screenshot.getUrl());
		doScreenshot();
		scheduledSeleniumSession.setReady(true);
		log.info("Login successful for {}", screenshot.getId().toString());
	}

	private void doScreenshot() {
		try {
			var zoomLevel = screenshot.getZoomLevel() * 10;
			((RemoteWebDriver)scheduledSeleniumSession.getSession().getDriver()).executeScript("document.body.style.zoom = '" + zoomLevel + "%'");
			helper.createScreenshot(scheduledSeleniumSession.getSession().getDriver(), outputDirectory, screenshot);
		} catch (Exception e) {
			log.error("Error while creating screenshot", e);
		}
	}

	public void start() {
		thread.start();
	}

	public void join() throws InterruptedException {
		thread.join();
	}
}
