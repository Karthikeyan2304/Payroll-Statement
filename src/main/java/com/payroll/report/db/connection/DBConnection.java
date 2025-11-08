package com.payroll.report.db.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBConnection {

	public Connection getConnection() throws SQLException;

	void closeConnection(Connection con, PreparedStatement ps, ResultSet rs) throws SQLException;
	void closeConnection(Connection con, PreparedStatement ps) throws SQLException;

}

