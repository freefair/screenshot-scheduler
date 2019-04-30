package io.freefair.screenshot_scheduler.authentication;

import io.freefair.screenshot_scheduler.models.AuthenticationInformation;
import io.freefair.screenshot_scheduler.models.UsernamePasswordAuthenticationInformation;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

public class AWSAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SecretsManagerClient secretsManagerClient;

	@Override
	public boolean isApplicable(AuthenticationInformation information) {
		if(information instanceof UsernamePasswordAuthenticationInformation) {
			return ((UsernamePasswordAuthenticationInformation) information).getPassword().toLowerCase().startsWith("aws:");
		}
		return false;
	}

	@Override
	public String getSecret(AuthenticationInformation information) {
		if(information instanceof UsernamePasswordAuthenticationInformation) {
			String password = ((UsernamePasswordAuthenticationInformation) information).getPassword();
			password = password.substring(4);
			secretsManagerClient.getSecretValue(GetSecretValueRequest.builder().secretId(password).build());
		}
		return null;
	}
}
