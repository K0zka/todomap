package org.todomap.integrations.hitman.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.todomap.integrations.hitman.Destination;

public class FileDestination implements Destination {

	File destinationDir;
	String destination;

	private final static Logger logger = LoggerFactory
			.getLogger(FileDestination.class);

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public void send(final String message) {
		final File outputFile = new File(destinationDir, System
				.currentTimeMillis()
				+ ".xml");
		try {
			IOUtils.write(message, new FileWriter(outputFile));
		} catch (IOException e) {
			logger.error("Could not save message: " + message, e);
		}
	}

	public void init() {
		destinationDir = new File(destination);
		if (!destinationDir.exists()) {
			destinationDir.mkdirs();
		}
	}

}
