package io.freefair.screenshot_scheduler.models;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
@Data
public class Screenshot {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "varchar(32)")
	private UUID id;

	private String url;

	private String loginUrl;

	private int intervalSeconds;

	@OneToOne(cascade = CascadeType.ALL)
	private AuthenticationInformation authenticationInformation;
}
