package com.payroll.report.repo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.constant.QueryConstant;
import com.payroll.report.constant.DBType;
import com.payroll.report.db.connection.DBFactory;
import com.payroll.report.util.ClientProperties;
import com.payroll.report.util.PayrollReportHasher;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;

public class UserRepoImpl implements UserRepo {
	private static final Logger LOG = LoggerFactory.getLogger(UserRepoImpl.class);

	@Override
	public boolean checkValidUser(String userName, String passWord) throws SQLException {
		Connection con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = null;
		try {
			sql = ClientProperties.getProperty("check.user.query");
		} catch (IOException e) {
			LOG.error("IOException in the checkValidUser {} : " , e.getMessage(),e);
		}
		if (con != null && sql != null) {
			LOG.info("DB Connected");
			try {
				ps = con.prepareStatement(sql);
				ps.setString(1, userName.trim().toLowerCase());
				rs = ps.executeQuery();
				while (rs.next()) {
					String storedHash = rs.getString("password_hash");
					if (PayrollReportHasher.checkPassword(passWord, storedHash)) {
						{
							LOG.info("User Found");

							return true;
						}
					} else {
						LOG.info("User NOT Found");
						return false;
					}
				}

			} catch (SQLException e) {

				LOG.error("SQLException in the  checkValidUser : {}", e.getMessage(), e);
			} finally {
				DBFactory.getConnection("ORACLE").closeConnection(con, ps, rs);
			}

		}
		LOG.info("User NOT Found");
		return false;
	}

	@Override

	public String getUserMobile(String username) throws SQLException {

		String sql = QueryConstant.USERMOBNOQUERY;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String mobileNum = null;
		if (StringUtils.isNotBlank(username)) {

			try {
				con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
				ps = con.prepareStatement(sql);
				ps.setString(1, username.trim());

				rs = ps.executeQuery();
				while (rs.next()) {
					mobileNum = rs.getString("MOBILE");
				}
			} catch (SQLException e) {
				LOG.error("SQLException in the  getUserMobile : {}", e.getMessage(), e);
			} finally {
				DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps, rs);
			}

		} else {
			LOG.error("No Mobile Number");
		}
		return mobileNum;

	}

	@Override
	public boolean duplicateUserCheck(String userName) throws IOException, SQLException {
		Connection con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = ClientProperties.getProperty("check.dul.user.query");
		if (con != null && sql != null) {
			LOG.info("DB Connected");
			try {
				ps = con.prepareStatement(sql);
				ps.setString(1, userName);
				rs = ps.executeQuery();
				while (rs.next()) {
					LOG.info("User already present");
					return true;
				}
			} catch (Exception e) {
				LOG.error("Database error while checking duplicate user : {}", e);

			} finally {
				DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps, rs);
			}
		}
		return false;

	}

	public void sendOTPToUser(String phoneNumber) {
		try {

			String accSid = ClientProperties.getProperty("twilio.accsid").trim();
			String authToken = ClientProperties.getProperty("twilio.auth.token").trim();
			String verifySid = ClientProperties.getProperty("twilio.verfiyaccsid").trim();

			Twilio.init(accSid, authToken);

			if (StringUtils.isBlank(verifySid)) {
				LOG.error("Verify Service SID is missing or invalid");
				return;
			}
			Verification verification = Verification.creator(verifySid, phoneNumber, "sms").create();
			LOG.info("OTP Status: {}",verification.getStatus());
			LOG.info("OTP has been successfully generated and sent to {} at {}", phoneNumber, LocalDateTime.now());

		} catch (com.twilio.exception.ApiException e) {
			LOG.error("Twilio API Exception: Code={}, Message={}", e.getCode(), e.getMessage());

		} catch (Exception e) {
			LOG.error("Failed to connect the OTP API: {}", e.getMessage(), e);
		}
	}

	public boolean getUserType(String userName) throws SQLException {
		boolean adminRole = false;
		String roleType = null;
		if (StringUtils.isNoneBlank(userName)) {

			String sql = QueryConstant.USERTYPEQUERY;
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				con = DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
				ps = con.prepareStatement(sql);
				ps.setString(1, userName.trim());
				rs = ps.executeQuery();
				while (rs.next()) {
					roleType = rs.getString("role");
				}
				if (StringUtils.isNoneBlank(roleType)) {
					if (StringUtils.equalsAnyIgnoreCase(roleType, "ADMIN")) {
						adminRole = true;
					} else {
						adminRole = false;
					}
				}
			} catch (SQLException e) {
				LOG.error("SQLException in the getUserMobile {} : ", e.getMessage(), e);
			} finally {
				DBFactory.getConnection(DBType.ORACLE.name()).closeConnection(con, ps, rs);
			}

		}
		return adminRole;

	}

	public Connection getConnection() throws SQLException {
	    return DBFactory.getConnection(DBType.ORACLE.name()).getConnection();
	}

}
