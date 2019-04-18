package io.freefair.screenshot_scheduler.selenium;

import io.freefair.screenshot_scheduler.models.Screenshot;
import io.freefair.screenshot_scheduler.repositories.ScreenshotRepository;
import io.freefair.screenshot_scheduler.selenium.login.ILoginHandler;
import io.freefair.screenshot_scheduler.selenium.login.LoginThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Scope("singleton")
@Slf4j
public class SeleniumScheduler {
	@Autowired
	private final SeleniumSessionFactory sessionFactory;

	@Autowired
	private ScreenshotRepository screenshotRepository;

	@Autowired
	private List<ILoginHandler> loginHandlers;

	@Value("${screenshot.outputDirectory}")
	private String outputDirectory;

	private final Map<UUID, ScheduledSeleniumSession> sessionMap = new HashMap<>();

	@Autowired
	public SeleniumScheduler(SeleniumSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Scheduled(cron = "*/1 * * * * *")
	public void doScreenshot() {
		List<Screenshot> all = screenshotRepository.findAll();
		updateSessions(all);
		execute(all);
	}

	public ScheduledSeleniumSession getSession(UUID id) {
		if(sessionMap.containsKey(id)) {
			return sessionMap.get(id);
		}
		return null;
	}

	private void execute(List<Screenshot> all) {
		for (UUID key : sessionMap.keySet()) {
			Screenshot screenshot = all.stream().filter(a -> a.getId().equals(key)).findFirst().orElse(null);
			ScheduledSeleniumSession scheduledSeleniumSession = sessionMap.get(key);
			if (screenshot == null) continue;
			if (!scheduledSeleniumSession.isReady()) continue;
			if ((System.currentTimeMillis() - scheduledSeleniumSession.getLastExecution()) < screenshot.getIntervalSeconds() * 1000)
				continue;
			log.info("Taking screenshot for {}", key.toString());
			new ScreenshotThread(scheduledSeleniumSession, screenshot, outputDirectory).start();
			scheduledSeleniumSession.setLastExecution(System.currentTimeMillis());
		}
	}

	private void updateSessions(List<Screenshot> all) {
		for (Screenshot s : all) {
			if (!sessionMap.containsKey(s.getId()) && s.isAutostart()) {
				createSession(s);
			}
		}
		for (UUID key : sessionMap.keySet()) {
			if (all.stream().noneMatch(s -> s.getId().equals(key))) {
				stopSession(key);
			}
		}
	}

	public void restartSession(UUID key) {
		synchronized (sessionMap) {
			if(!sessionMap.containsKey(key)) return;
			ScheduledSeleniumSession scheduledSeleniumSession = sessionMap.get(key);
			scheduledSeleniumSession.getSession().restart();
			doLogin(scheduledSeleniumSession, screenshotRepository.getOne(key));
		}
	}

	public void stopSession(UUID key) {
		synchronized (sessionMap) {
			log.info("Deleting screenshot session for {}", key.toString());
			sessionMap.get(key).delete();
			sessionMap.remove(key);
		}
	}

	public void createSession(Screenshot s) {
		synchronized (sessionMap) {
			var session = new ScheduledSeleniumSession(sessionFactory.getObject(), false, 0);
			log.info("Creating screenshot session for {}", s.getId().toString());
			sessionMap.put(s.getId(), session);
			doLogin(session, s);
		}
	}

	private void doLogin(ScheduledSeleniumSession session, Screenshot s) {
		new LoginThread(session, s, loginHandlers.stream().filter(h -> h.isCapableOf(s.getAuthenticationInformation())).findFirst().orElse(null), outputDirectory).start();
	}
}
