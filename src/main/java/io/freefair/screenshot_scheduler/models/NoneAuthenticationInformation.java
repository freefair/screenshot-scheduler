package io.freefair.screenshot_scheduler.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class NoneAuthenticationInformation extends AuthenticationInformation {
}
