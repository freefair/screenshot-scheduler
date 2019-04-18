package io.freefair.screenshot_scheduler.controller;

import io.freefair.screenshot_scheduler.dto.SessionResponse;
import io.freefair.screenshot_scheduler.repositories.ScreenshotRepository;
import io.freefair.screenshot_scheduler.selenium.ScheduledSeleniumSession;
import io.freefair.screenshot_scheduler.selenium.SeleniumScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.UUID;

@RestController
@RequestMapping("/session")
public class SessionController {
	private final SeleniumScheduler scheduler;

	private final ScreenshotRepository screenshotRepository;

	@Autowired
	public SessionController(SeleniumScheduler scheduler, ScreenshotRepository screenshotRepository) {
		this.scheduler = scheduler;
		this.screenshotRepository = screenshotRepository;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Transactional
	public SessionResponse getSessionState(@PathVariable("id") UUID id) {
		ScheduledSeleniumSession session = scheduler.getSession(id);
		return new SessionResponse(session != null ? "running" : "stopped");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	@Transactional
	public SessionResponse startSession(@PathVariable("id") UUID id) {
		scheduler.createSession(screenshotRepository.getOne(id));
		return new SessionResponse("success");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Transactional
	public SessionResponse restartSession(@PathVariable("id") UUID id) {
		scheduler.restartSession(id);
		return new SessionResponse("success");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public SessionResponse stopSession(@PathVariable("id") UUID id) {
		scheduler.stopSession(id);
		return new SessionResponse("success");
	}
}
