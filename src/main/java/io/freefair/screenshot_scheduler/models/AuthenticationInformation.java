package io.freefair.screenshot_scheduler.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = BasicAuthenticationInformation.class, name = "basic"),
		@JsonSubTypes.Type(value = FormAuthenticationInformation.class, name = "form"),
		@JsonSubTypes.Type(value = NoneAuthenticationInformation.class, name = "none")
})
public class AuthenticationInformation {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "varchar(32)")
	private UUID id;

	@Column(nullable = false)
	private AuthenticationType authenticationType;
}
