package io.freefair.screenshot_scheduler.configurations;

import io.freefair.screenshot_scheduler.authentication.AWSAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@Configuration
@ConditionalOnProperty(value = "screenshot.aws.enabled", havingValue = "true")
public class AwsConfiguration {
	@Bean
	public AWSAuthenticationProvider awsAuthenticationProvider() {
		return new AWSAuthenticationProvider();
	}

	@Bean
	public SecretsManagerClient secretsManagerClient(@Value("${screenshot.aws.key_id}") String awsKeyId, @Value("${screenshot.aws.access_key}") String awsAccessKey, @Value("${screenshot.aws.region}") String region, @Value("${screenshot.aws.use_iam_role}") boolean useIamRole) {
		Region regionEnum = Region.of(region);
		if(useIamRole)
			SecretsManagerClient
					.builder()
					.region(regionEnum)
					.credentialsProvider(InstanceProfileCredentialsProvider.create())
					.build();
		return SecretsManagerClient
				.builder()
				.region(regionEnum)
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsKeyId, awsAccessKey)))
				.build();
	}
}
