package com.payroll.report.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientProperties {
	private static final Logger LOG = LoggerFactory.getLogger(ClientProperties.class);
	static Properties properties = new java.util.Properties();

	static {
		InputStream input = ClientProperties.class.getClassLoader().getResourceAsStream("application.properties");
		if (input != null) {
			LOG.info("Client Properties is loading ..");
			try {
				properties.load(input);
				LOG.info("Client Properties is loaded");
			} catch (IOException e) {
				LOG.error("Error while loading the properties");

			}

		}
	}

	private ClientProperties() {

	}

	public static String getProperty(String propertyName) throws IOException {

		if (propertyName != null) {
			String prop = properties.getProperty(propertyName);
			if (prop != null) {
				LOG.info(propertyName, prop, " {} : {} found in the properties files");
				return prop;
			} else {
				LOG.error(propertyName, " {} : is not found in the properties files");
				return "Not Found!";
			}
		}
		return null;
	}

}
