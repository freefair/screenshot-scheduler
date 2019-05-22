package io.freefair.screenshot_scheduler.selenium;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class SeleniumSessionFactory implements FactoryBean<SeleniumSession>, EnvironmentAware {
    private Environment environment;

    @SuppressWarnings("ConstantConditions")
    @Override
    public SeleniumSession getObject() {
        return new SeleniumSession(environment.getProperty("selenium.implicitWait", Integer.class),
                environment.getProperty("selenium.browser.height", Integer.class),
                environment.getProperty("selenium.browser.width", Integer.class),
                Objects.requireNonNull(environment.getProperty("selenium.chrome_args", String.class)).trim().split(" "));
    }

    @Override
    public Class<?> getObjectType() {
        return SeleniumSession.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
