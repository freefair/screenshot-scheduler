package io.freefair.screenshot_scheduler.selenium.login;

import io.freefair.screenshot_scheduler.models.AuthenticationInformation;
import io.freefair.screenshot_scheduler.models.AuthenticationType;
import io.freefair.screenshot_scheduler.models.BasicAuthenticationInformation;
import io.freefair.screenshot_scheduler.selenium.SeleniumSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Scope("singleton")
public class BasicLoginHandler implements ILoginHandler {
	@Override
	public void doLogin(SeleniumSession session, AuthenticationInformation authenticationInformation) {
		URI currentUri = URI.create(session.getDriver().getCurrentUrl());
		var info = ((BasicAuthenticationInformation)authenticationInformation);
		String newUrl = currentUri.getScheme() + "://" + info.getUsername() + ":" + info.getPassword() + "@" + currentUri.getHost() + ":" + currentUri.getPort() + "/";
		if(currentUri.getRawPath() != null)
			newUrl += currentUri.getRawPath();
		if(currentUri.getRawQuery() != null)
			newUrl += "?" + currentUri.getRawQuery();
		session.getDriver().navigate().to(newUrl);
	}

	@Override
	public boolean isCapableOf(AuthenticationInformation authenticationInformation) {
		return authenticationInformation.getAuthenticationType() == AuthenticationType.BASIC;
	}
}
