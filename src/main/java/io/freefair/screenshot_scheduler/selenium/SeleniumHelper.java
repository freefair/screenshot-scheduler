package io.freefair.screenshot_scheduler.selenium;

import io.freefair.screenshot_scheduler.models.Screenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TimeZone;

@Component
public class SeleniumHelper {
    @Value("${screenshot.date-format}")
    private String dateFormat;

    @Value("${screenshot.date-font-size}")
    private float fontSize;

    @Value("${screenshot.time_zone}")
    private String timeZone;

    private void fillImageWithText(BufferedImage image, String text, CaptionPosition position) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(SeleniumHelper.class.getClassLoader().getResourceAsStream("Helvetica.ttf")))
                    .deriveFont(fontSize);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        Graphics graphics = image.getGraphics();

        Rectangle2D stringBounds = graphics.getFontMetrics(font).getStringBounds(text, graphics);
        int stringWidth = (int) stringBounds.getWidth();
        int imageWidth = image.getWidth();
        int stringHeight = (int) stringBounds.getHeight();
        int imageHeight = image.getHeight();
        graphics.setColor(Color.WHITE);

        switch (position) {
            case LOWER_RIGHT:
                graphics.fillRect((imageWidth - stringWidth - 10), (imageHeight - stringHeight - 5), (stringWidth + 10), (stringHeight + 5));
                break;
            case LOWER_LEFT:
                graphics.fillRect(0, (imageHeight - stringHeight - 5), (stringWidth + 10), (stringHeight + 5));
                break;
        }
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);

        switch (position) {
            case LOWER_RIGHT:
                graphics.drawString(text, (imageWidth - stringWidth - 5), (imageHeight - 3));
                break;
            case LOWER_LEFT:
                graphics.drawString(text, 5, (imageHeight - 3));
                break;
        }
    }

    public void createScreenshot(WebDriver driver, String outputDirectory, Screenshot screenshotConfiguration) throws
            IOException {
        byte[] screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        if (screenshotConfiguration.isTimestamp()) {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(screenshotAs));

            String time = ZonedDateTime.now(TimeZone.getTimeZone(timeZone).toZoneId()).format(DateTimeFormatter.ofPattern(dateFormat));

            fillImageWithText(image, time, CaptionPosition.LOWER_RIGHT);

            String humanName = screenshotConfiguration.getName();
            if (humanName != null && !humanName.isEmpty()) {
                fillImageWithText(image, humanName, CaptionPosition.LOWER_LEFT);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", byteArrayOutputStream);
            screenshotAs = byteArrayOutputStream.toByteArray();
        }
        FileUtils.writeByteArrayToFile(new File(new File(outputDirectory), screenshotConfiguration.getId().toString() + ".png"), screenshotAs);
    }

    enum CaptionPosition {
        LOWER_LEFT,
        LOWER_RIGHT
    }

}


