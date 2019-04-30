package io.freefair.screenshot_scheduler.selenium.login;

import io.freefair.screenshot_scheduler.authentication.AuthenticationHelper;
import io.freefair.screenshot_scheduler.models.AuthenticationInformation;
import io.freefair.screenshot_scheduler.models.AuthenticationType;
import io.freefair.screenshot_scheduler.models.BasicAuthenticationInformation;
import io.freefair.screenshot_scheduler.selenium.SeleniumSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Scope("singleton")
public class BasicLoginHandler implements ILoginHandler {
	@Autowired
	private AuthenticationHelper authenticationHelper;

	@Override
	public void doLogin(SeleniumSession session, AuthenticationInformation authenticationInformation) {
		URI currentUri = URI.create(session.getDriver().getCurrentUrl());
		var info = ((BasicAuthenticationInformation)authenticationInformation);
		String newUrl = currentUri.getScheme() + "://" + info.getUsername() + ":" + authenticationHelper.getSecret(info) + "@" + currentUri.getHost();
		if(currentUri.getPort() > 0)
			newUrl += ":" + currentUri.getPort();
		newUrl += "/";
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
