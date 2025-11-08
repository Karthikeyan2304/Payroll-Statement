package com.payroll.report.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.db.connection.DBFactory;
import com.payroll.report.service.UserService;

public class DatabaseUtil {
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseUtil.class);
	public static UserService userService;

	private DatabaseUtil() {

	}

	static {
		userService = new UserService();
	}

	public static void saveUserDetails(String userID, String userName, String mobileno, String hashedPassword,
			String userType) throws Exception {
		Connection con = DBFactory.getConnection("ORACLE").getConnection();
		PreparedStatement ps = null;

		try {
			if (userService.duplicateUserCheck(userName, hashedPassword) == false && con != null) {
				ps = con.prepareStatement("insert into app_user (ID,EMAIL,MOBILE,PASSWORD_HASH,ROLE)values(?,?,?,?,?)");
				ps.setString(1, userID);
				ps.setString(2, userName);
				ps.setString(3, mobileno);
				ps.setString(4, hashedPassword);
				ps.setString(5, userType);
				ps.executeUpdate();
				LOG.info("User successfully saved in the DB");
			} else {
				throw new DuplicateUserException("User already created");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBFactory.getConnection("ORACLE").closeConnection(con, ps);
		}
	}

	public static String getUserID(String userName) throws Exception {
		Connection con = DBFactory.getConnection("ORACLE").getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select ID from app_user  where email=?";
		String result = null;
		try {
			if (con != null) {
				ps = con.prepareStatement(query);
				ps.setString(1, userName);
				rs = ps.executeQuery();
				while (rs.next()) {
					result = rs.getString("ID");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBFactory.getConnection("ORACLE").closeConnection(con, ps);
		}
		return result;
	}

	public static void saveUserLoginInDB(String userID, String userName, String userType, Timestamp lastLoginDTTM,
			String otpLoginFlag) throws Exception {
		Connection con = DBFactory.getConnection("ORACLE").getConnection();
		PreparedStatement ps = null;

		try {
			if (StringUtils.isNoneBlank(userID) && StringUtils.isNoneBlank(userName)
					&& StringUtils.isNoneBlank(userType) && lastLoginDTTM != null
					&& StringUtils.isNoneBlank(otpLoginFlag) && con != null) {
				ps = con.prepareStatement(
						"insert into app_user_login_audit (ID,EMAIL,ROLE,LAST_LOGIN_DTTM,OTP_LOGIN_FLAG)values(?,?,?,?,?)");
				ps.setString(1, userID);
				ps.setString(2, userName);
				ps.setString(3, userType);
				ps.setTimestamp(4, lastLoginDTTM);
				ps.setString(5, otpLoginFlag);
				ps.executeUpdate();
				LOG.info("User successfully saved in the DB");
			} else {
				throw new DuplicateUserException("User already created");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBFactory.getConnection("ORACLE").closeConnection(con, ps);
		}
	}
}
