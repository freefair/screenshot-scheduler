package io.freefair.screenshot_scheduler.selenium.login;

import io.freefair.screenshot_scheduler.models.AuthenticationInformation;
import io.freefair.screenshot_scheduler.models.AuthenticationType;
import io.freefair.screenshot_scheduler.selenium.SeleniumSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class NoneLoginHandler implements ILoginHandler {
	@Override
	public void doLogin(SeleniumSession session, AuthenticationInformation authenticationInformation) {

	}

	@Override
	public boolean isCapableOf(AuthenticationInformation authenticationInformation) {
		return authenticationInformation.getAuthenticationType() == AuthenticationType.NONE;
	}
}
