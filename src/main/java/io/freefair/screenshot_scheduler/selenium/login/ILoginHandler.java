package io.freefair.screenshot_scheduler.selenium.login;

import io.freefair.screenshot_scheduler.models.AuthenticationInformation;
import io.freefair.screenshot_scheduler.models.Screenshot;
import io.freefair.screenshot_scheduler.selenium.SeleniumSession;

public interface ILoginHandler {
	void doLogin(SeleniumSession session, AuthenticationInformation authenticationInformation);
	boolean isCapableOf(AuthenticationInformation authenticationInformation);
}
