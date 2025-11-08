package com.payroll.report.repo;

import java.io.IOException;
import java.sql.SQLException;

public interface UserRepo {
	public boolean checkValidUser(String username, String passWord) throws IOException, SQLException;
	public String getUserMobile(String username) throws IOException, SQLException;
	public boolean getUserType(String userName) throws SQLException;
	boolean duplicateUserCheck(String userName) throws IOException, SQLException;
	public void sendOTPToUser(String phoneNumber);
	
}
