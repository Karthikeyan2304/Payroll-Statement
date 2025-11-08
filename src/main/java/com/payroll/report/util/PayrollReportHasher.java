package com.payroll.report.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PayrollReportHasher {
	private static final BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder(10);

	private PayrollReportHasher() {

	}

	public static String passwordHasher(String plainPassword) {
		return passEncoder.encode(plainPassword);
	}

	public static boolean checkPassword(String plainPassword, String hashedPassword) {
		return passEncoder.matches(plainPassword, hashedPassword);
	}
}
