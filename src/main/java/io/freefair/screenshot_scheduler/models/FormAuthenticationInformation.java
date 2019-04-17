package io.freefair.screenshot_scheduler.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class FormAuthenticationInformation extends AuthenticationInformation {

	private String usernameSelector;
	private String passwordSelector;
	private String submitSelector;

	private String username;
	private String password;
}
