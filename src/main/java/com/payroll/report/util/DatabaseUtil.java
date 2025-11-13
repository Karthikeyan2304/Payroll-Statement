package com.payroll.report.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.constant.DBType;
import com.payroll.report.constant.QueryConstant;
import com.payroll.report.db.connection.DBFactory;
import com.payroll.report.service.UserService;

public class DatabaseUtil {
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseUtil.class);
	public static UserService userService;

	private DatabaseUtil() {

	}

	public static void setUserService(UserService service) {
		userService = service;
	}

	static {
		userService = new UserService();
	}

	public static void saveUserDetails(String userID, String userName, String mobileno, String hashedPassword,
			String userType) throws SQLException, IOException {
		Connection con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
		PreparedStatement ps = null;

		try {
			try {
				if (userService.duplicateUserCheck(userName, hashedPassword) == false && con != null) {
					ps = con.prepareStatement(
							"insert into app_user (ID,EMAIL,MOBILE,PASSWORD_HASH,ROLE)values(?,?,?,?,?)");
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
			} catch (IOException e) {
				LOG.error("SQLException in the saveUserDetails {} : ", e.getMessage(), e);

			}
		} catch (SQLException e) {
			LOG.error("SQLException in the saveUserDetails {} : ", e.getMessage(), e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps);
		}
	}

	public static String getUserID(String userName) throws SQLException {
		Connection con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
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
		} catch (SQLException e) {
			LOG.error("SQLException in the getUserID {} : ", e.getMessage(), e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps);
		}
		return result;
	}

	public static void saveUserLoginInDB(String userID, String userName, String userType, Timestamp lastLoginDTTM,
			String otpLoginFlag) throws SQLException {
		Connection con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
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
		} catch (SQLException e) {
			LOG.error("SQLException in the saveUserLoginInDB {} : ", e.getMessage(), e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps);
		}
	}

	public static void delOldUserAuditData() throws SQLException {
		Connection con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
		PreparedStatement ps = null;
		String query = QueryConstant.DEL_APPUSER_AUDIT_QUERY;

		try {
			if (con != null) {
				ps = con.prepareStatement(query.trim());

				ps.executeUpdate();
				LOG.info("Trunacted Appuser audit table successfully");
			}
		} catch (SQLException e) {
			LOG.error("SQLException in the saveUserLoginInDB: {}", e.getMessage(), e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps);
		}
	}

	public static void saveJobInDB(String jobName, Timestamp jobDTTM) throws SQLException {
		Connection con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
		PreparedStatement ps = null;

		try {
			if (StringUtils.isNoneBlank(jobName) && jobDTTM != null && con != null) {
				ps = con.prepareStatement("insert into job_log (job_name,job_triggered_at)values(?,?)");
				ps.setString(1, jobName);
				ps.setTimestamp(2, jobDTTM);
				ps.executeUpdate();
				LOG.info("Job Log successfully saved in the DB");
			}
		} catch (SQLException e) {
			LOG.error("SQLException in the saveJobInDB {} : ", e.getMessage(), e);
		} finally {
			DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps);
		}
	}
}
