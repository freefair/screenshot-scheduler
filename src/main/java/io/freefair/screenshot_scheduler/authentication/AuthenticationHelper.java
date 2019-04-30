package io.freefair.screenshot_scheduler.authentication;

import io.freefair.screenshot_scheduler.models.AuthenticationInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationHelper {
	@Autowired
	private List<AuthenticationProvider> authenticationProviderList;

	public String getSecret(AuthenticationInformation information) {
		return authenticationProviderList.stream().filter(a -> a.isApplicable(information)).findFirst().orElseThrow()
				.getSecret(information);
	}
}
