package com.payroll.report.util;

public class DuplicateUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateUserException(String error) {
		super(error);
	}

}
