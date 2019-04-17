package io.freefair.screenshot_scheduler.selenium;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledSeleniumSession {
	private SeleniumSession session;
	private boolean ready;
	private long lastExecution;

	public void delete() {
		session.getDriver().close();
	}
}
