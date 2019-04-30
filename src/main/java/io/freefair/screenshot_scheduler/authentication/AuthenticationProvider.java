package io.freefair.screenshot_scheduler.authentication;

import io.freefair.screenshot_scheduler.models.AuthenticationInformation;

public interface AuthenticationProvider {
	boolean isApplicable(AuthenticationInformation information);
	String getSecret(AuthenticationInformation information);
}
