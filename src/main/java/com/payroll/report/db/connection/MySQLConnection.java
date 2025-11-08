package com.payroll.report.db.connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.payroll.report.util.ClientProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MySQLConnection implements DBConnection {

//	private static final String DB_URL = "mysql.db.url";
//	private static final String DB_USERNAME = "mysql.db.username";
//	private static final String DB_PASSWORD = "mysql.db.password";
//	static String url;
	//static String userName;
	//static String passWord;
	//private static HikariDataSource dataSource;
//	static {
//		try {
////			Class.forName("oracle.jdbc.driver.OracleDriver");
//			url = ClientProperties.getProperty(DB_URL);
//			userName = ClientProperties.getProperty(DB_USERNAME);
//			passWord = ClientProperties.getProperty(DB_PASSWORD);
//			HikariConfig config = new HikariConfig();
//			config.setJdbcUrl(url);
//			config.setUsername(DB_USERNAME);
//			config.setPassword(DB_PASSWORD);
//			config.addDataSourceProperty("cachePrepStmts", "true");
//			config.addDataSourceProperty("prepStmtCacheSize", "250");
//			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//			config.setMaximumPoolSize(15);
//			config.setMinimumIdle(3);
//			config.setIdleTimeout(60000);
//			config.setConnectionTimeout(30000);
//			// config.setLeakDetectionThreshold(20000);
//			dataSource = new HikariDataSource(config);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	DataSource dataSource = null;
	Connection conn = null;
	public Connection getConnection() throws SQLException {

		
		// TODO Auto-generated method stub
		try {
			Context initialContext = new InitialContext();
			dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/Payroll_Report_DB_My_SQL");
			conn = dataSource.getConnection();
		}

		catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;

	}

	public void closeConnection(Connection con, PreparedStatement ps, ResultSet rs) throws SQLException {
		if (con != null && ps != null && rs != null) {
			con.close();
			ps.close();
			rs.close();
	}}

	@Override
	public void closeConnection(Connection con, PreparedStatement ps) throws SQLException {
		if(con!=null &&ps!=null)
		{
			con.close();
			ps.close();
		}

	}

}
