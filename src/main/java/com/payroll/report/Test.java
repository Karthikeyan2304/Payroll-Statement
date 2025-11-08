package com.payroll.report;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	private static final Logger LOG = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) throws IOException, SQLException {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
//		LocalDateTime currentDateTime = LocalDateTime.now();
//		String formattedDateTime = currentDateTime.format(formatter);
//		LOG.info("Test Time : " + formattedDateTime.toString());
//
//		String urlName = ClientProperties.getProperty("oracle.db.url");
//		System.out.println(urlName);
		LocalDate today = LocalDate.now();
		LocalDate startOfYear = today.withDayOfYear(1);
		LocalDate endOfYearToDate = today;
		
	//	SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
		System.out.println(startOfYear.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		System.out.println(endOfYearToDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		// Start of the year (Jan 1)

//		Calendar cal = Calendar.getInstance();
//		String currentMonth = new SimpleDateFormat("YYYY-MM").format(cal.getTime());
		// Source - https://stackoverflow.com/questions/23068676/how-to-get-current-timestamp-in-string-format-in-java-yyyy-mm-dd-hh-mm-ss
		// Posted by Jigar Joshi
		// Retrieved 2025-11-06, License - CC BY-SA 4.0

		// Get the current date and time
		LocalDateTime now = LocalDateTime.now();

		// Define the format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// Format the current date and time
		String formattedNow = now.format(formatter);
		Timestamp currentLoginDTTM=Timestamp.from(Instant.now());
		// Print the formatted date and time
		System.out.println("Current Timestamp:" + currentLoginDTTM);

	}

}
