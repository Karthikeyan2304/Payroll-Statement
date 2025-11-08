package com.payroll.report.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.Test;

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
				// TODO Auto-generated catch block
				LOG.error("Error while loading the properties");
				e.printStackTrace();
			}

		}
	}

	private ClientProperties() {

	}

	public static String getProperty(String propertyName) throws IOException {

		if (propertyName != null) {
			String prop = properties.getProperty(propertyName);
			if (prop != null) {
				LOG.info("PropertyName " + propertyName + " value " + prop + " is found in the properties files");
				return prop;
			} else {
				LOG.error(propertyName + " is not found in the properties files");
				return "Not Found!";
			}
		}
		return null;
	}

}
