package io.freefair.screenshot_scheduler.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
public class FormAuthenticationInformation extends AuthenticationInformation implements UsernamePasswordAuthenticationInformation {

	@Getter
	@Setter
	private String usernameSelector;
	@Getter
	@Setter
	private String passwordSelector;
	@Getter
	@Setter
	private String submitSelector;
	@Getter
	@Setter
	private String username;

	private String password;

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}
}
