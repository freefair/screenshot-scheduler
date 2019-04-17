package io.freefair.screenshot_scheduler.selenium.login;

import io.freefair.screenshot_scheduler.models.AuthenticationInformation;
import io.freefair.screenshot_scheduler.models.AuthenticationType;
import io.freefair.screenshot_scheduler.models.FormAuthenticationInformation;
import io.freefair.screenshot_scheduler.selenium.SeleniumSession;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class FormLoginHandler implements ILoginHandler {
	@Override
	public void doLogin(SeleniumSession session, AuthenticationInformation authenticationInformation) {
		var info = ((FormAuthenticationInformation) authenticationInformation);
		WebElement element = session.getDriver().findElement(By.cssSelector(info.getUsernameSelector()));
		element.clear();
		element.sendKeys(info.getUsername());

		element = session.getDriver().findElement(By.cssSelector(info.getPasswordSelector()));
		element.clear();
		element.sendKeys(info.getPassword());

		element = session.getDriver().findElement(By.cssSelector(info.getSubmitSelector()));
		element.click();
	}

	@Override
	public boolean isCapableOf(AuthenticationInformation authenticationInformation) {
		return authenticationInformation.getAuthenticationType() == AuthenticationType.FORM;
	}
}
