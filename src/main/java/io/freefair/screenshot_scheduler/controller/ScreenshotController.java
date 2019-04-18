package io.freefair.screenshot_scheduler.controller;

import io.freefair.screenshot_scheduler.models.Screenshot;
import io.freefair.screenshot_scheduler.repositories.ScreenshotRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/screenshot")
public class ScreenshotController {

	private final ScreenshotRepository screenshotRepository;

	@Value("${screenshot.outputDirectory}")
	private String outputDirectory;

	@Autowired
	public ScreenshotController(ScreenshotRepository screenshotRepository) {
		this.screenshotRepository = screenshotRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Screenshot> getAll() {
		return screenshotRepository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Screenshot getById(@PathVariable("id") UUID id) {
		return screenshotRepository.findById(id).orElse(null);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	public Screenshot create(@RequestBody Screenshot screenshot) {
		return screenshotRepository.saveAndFlush(screenshot);
	}

	@RequestMapping(method = RequestMethod.PUT)
	@Transactional
	public Screenshot update(@RequestBody Screenshot screenshot) {
		return screenshotRepository.saveAndFlush(screenshot);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Transactional
	public void delete(@PathVariable("id") UUID id) {
		screenshotRepository.deleteById(id);
	}

	@RequestMapping(value = "/{id}/image", method = RequestMethod.GET, produces = "image/png")
	@ResponseBody
	public byte[] getImage(@PathVariable("id") UUID id) throws IOException {
		File file = new File(new File(outputDirectory), id.toString() + ".png");
		if(!file.exists())
			throw new NotFoundException("File " + file.getName() + " not found!");
		return FileUtils.readFileToByteArray(file);
	}
}
