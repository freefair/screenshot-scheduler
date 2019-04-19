package io.freefair.screenshot_scheduler.selenium;

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
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class SeleniumHelper {
	@Value("${screenshot.date-format}")
	private String dateFormat;

	@Value("${screenshot.date-font-size}")
	private float fontSize;

	public void createScreenshot(WebDriver driver, File outputFile, boolean withTimestamp) throws IOException {
		byte[] screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		if (withTimestamp) {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(screenshotAs));
			Graphics graphics = image.getGraphics();
			Font font;
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(SeleniumHelper.class.getClassLoader().getResourceAsStream("Helvetica.ttf")))
						.deriveFont(fontSize);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat));
			Rectangle2D stringBounds = graphics.getFontMetrics(font).getStringBounds(time, graphics);
			int stringWidth = (int) stringBounds.getWidth();
			int imageWidth = image.getWidth();
			int stringHeight = (int) stringBounds.getHeight();
			int imageHeight = image.getHeight();
			graphics.setColor(Color.WHITE);
			graphics.fillRect((imageWidth - stringWidth - 10), (imageHeight - stringHeight - 5), (stringWidth + 10), (stringHeight + 5));

			graphics.setColor(Color.BLACK);
			graphics.setFont(font);
			graphics.drawString(time, (imageWidth - stringWidth - 5), (imageHeight - 3));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", byteArrayOutputStream);
			screenshotAs = byteArrayOutputStream.toByteArray();
		}
		FileUtils.writeByteArrayToFile(outputFile, screenshotAs);
	}
}
