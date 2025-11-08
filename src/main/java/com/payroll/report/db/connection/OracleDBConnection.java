package com.payroll.report.db.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class OracleDBConnection implements DBConnection {

	DataSource dataSource = null;
	Connection conn = null;

	@Override
	public Connection getConnection() throws SQLException {

		// TODO Auto-generated method stub
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Context initialContext = new InitialContext();
			dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/Payroll_Report_DB_Oracle");
			conn = dataSource.getConnection();
		}

		catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;

	}

	@Override
	public void closeConnection(Connection con, PreparedStatement ps, ResultSet rs) throws SQLException {
		if (con != null && ps != null && rs != null)
			con.close();
		ps.close();
		rs.close();

	}

	@Override
	public void closeConnection(Connection con, PreparedStatement ps) throws SQLException {
		// TODO Auto-generated method stub
		if (con != null && ps != null)
			con.close();
		ps.close();

	}

}
