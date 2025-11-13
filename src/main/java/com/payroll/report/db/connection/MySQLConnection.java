package com.payroll.report.db.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLConnection implements DBConnection {
private static final Logger LOG=LoggerFactory.getLogger(MySQLConnection.class);
	DataSource dataSource = null;
	Connection conn = null;

	public Connection getConnection() throws SQLException {

		try {
			Context initialContext = new InitialContext();
			dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/Payroll_Report_DB_My_SQL");
			conn = dataSource.getConnection();
		}

		catch (NamingException e) {
			
			LOG.error("NamingException in the getConnection {} : ",e.getMessage(),e);
		} catch (SQLException e) {
			LOG.error("SQLException in the getConnection {} : ",e.getMessage(),e);
		}
		return conn;

	}

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
