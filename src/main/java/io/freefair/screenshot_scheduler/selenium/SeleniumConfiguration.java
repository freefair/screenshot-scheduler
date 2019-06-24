package io.freefair.screenshot_scheduler.selenium;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Getter
@Slf4j
@Component
public class SeleniumConfiguration {
    @Value("${selenium.useGrid}")
    private boolean useGrid;

    @Value("${selenium.gridURL}")
    private String gridURL;

    @Value("${selenium.browser}")
    private String browser;

    @Value("${selenium.platform}")
    private String platform;

    @Value("${selenium.implicitWait}")
    private int implicitlyWait;

    @Value("${selenium.browser.height}")
    private int height;

    @Value("${selenium.browser.width}")
    private int width;

    @Value("${selenium.chrome_args}")
    private String[] chromeArgs;

    public URL getGridURL() {
        try {
            return new URL(gridURL);
        } catch (MalformedURLException e) {
            log.error("GridURL is invalid", e);
            return null;
        }
    }

}
