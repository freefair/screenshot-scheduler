package io.freefair.screenshot_scheduler.authentication;

import io.freefair.screenshot_scheduler.models.AuthenticationInformation;
import io.freefair.screenshot_scheduler.models.UsernamePasswordAuthenticationInformation;
import org.springframework.stereotype.Component;

@Component
public class DefaultAuthenticationProvider implements AuthenticationProvider {

	@Override
	public boolean isApplicable(AuthenticationInformation information) {
		return !(information instanceof UsernamePasswordAuthenticationInformation) ||
				((UsernamePasswordAuthenticationInformation) information).getPassword().toLowerCase().charAt(3) != ':';
	}

	@Override
	public String getSecret(AuthenticationInformation information) {
		if(information instanceof UsernamePasswordAuthenticationInformation)
			return ((UsernamePasswordAuthenticationInformation) information).getPassword();
		return null;
	}
}
