package com.payroll.report.db.connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.util.ClientProperties;

public class OracleDBConnectionTest implements DBConnectionTest {
	private static final Logger LOG = LoggerFactory.getLogger(OracleDBConnection.class);

	DataSource dataSource = null;
	Connection conn = null;

	private static final String DB_URL = "oracle.db.url";
	private static final String DB_USERNAME = "oracle.db.username";
	private static final String DB_PASSWORD = "oracle.db.password";
	static String url;
	static String userName;
	static String passWord;
	static {
		try {
			url = ClientProperties.getProperty(DB_URL);
			userName = ClientProperties.getProperty(DB_USERNAME);
			passWord = ClientProperties.getProperty(DB_PASSWORD);
		} catch (IOException e) {
			LOG.error("IOException : {}", e.getMessage());
		}
	}

	@Override
	public Connection getConnection() {

		Connection con = null;
		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(passWord) && StringUtils.isNoneBlank(url)) {
			try {
				con = DriverManager.getConnection(url, userName, passWord);
			} catch (SQLException e) {
				LOG.error("SQLException : {}", e.getMessage());

			}
		}
		return con;
	}

	@Override
	public void closeConnection(Connection con, PreparedStatement ps, ResultSet rs) throws SQLException {
		if (con != null && ps != null && rs != null) {
			con.close();
			ps.close();
			rs.close();
		}
	}

	@Override
	public void closeConnection(Connection con, PreparedStatement ps) throws SQLException {

		if (con != null && ps != null) {
			con.close();
			ps.close();
		}
	}

}
