package com.payroll.report.service;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.repo.UserRepoImpl;

public class UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	public static UserRepoImpl userRepo;
	static {
		userRepo = new UserRepoImpl();
	}

	public boolean checkValidUser(String username, String passWord) throws IOException, SQLException {
		if (StringUtils.isNotBlank(passWord) && StringUtils.isNotBlank(username)) {
			return userRepo.checkValidUser(username, passWord);
		}
		return false;
	}

	public void sendOTPToUser(String mobileNumber) throws IOException, SQLException {
		if (StringUtils.isNotBlank(mobileNumber)) {

			userRepo.sendOTPToUser(mobileNumber);
			LOG.info("Sending OTP to the mobile mumber {} " + mobileNumber);
		} else {
			LOG.error("No Mobile Number  " + mobileNumber);
		}
	}

	public String getUserMobile(String username) throws IOException, SQLException {
		String number = null;
		if (StringUtils.isNotBlank(username)) {

			number = userRepo.getUserMobile(username);
		}
		return number;
	}

	public boolean duplicateUserCheck(String username, String passWord) throws IOException, SQLException {
		if (StringUtils.isNotBlank(passWord) && StringUtils.isNotBlank(username)) {
			return userRepo.duplicateUserCheck(username);
		}
		return false;
	}

	public boolean getUserType(String userName) throws SQLException {
		boolean flag = false;
		if (StringUtils.isNoneBlank(userName)) {

			flag = userRepo.getUserType(userName);
		}
		return flag;

	}
}
