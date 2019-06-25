package io.freefair.screenshot_scheduler.selenium;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class SeleniumSessionFactory implements FactoryBean<SeleniumSession>, EnvironmentAware {
    private Environment environment;

    @Autowired
    private SeleniumConfiguration seleniumConfiguration;

    @SuppressWarnings("ConstantConditions")
    @Override
    public SeleniumSession getObject() {
        return new SeleniumSession(seleniumConfiguration);
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
