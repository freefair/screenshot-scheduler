package io.freefair.screenshot_scheduler.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class FormAuthenticationInformation extends AuthenticationInformation implements UsernamePasswordAuthenticationInformation {

	private String usernameSelector;
	private String passwordSelector;
	private String submitSelector;

	private String username;

	@JsonIgnore
	private String password;
}
