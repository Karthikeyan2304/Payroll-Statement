package com.payroll.report.db.connection;

public class DBFactory {
	public static DBConnection getConnection(String dbType) {
		if (dbType.equalsIgnoreCase("MYSQL")) {
			return new MySQLConnection();
		} else if (dbType.equalsIgnoreCase("ORACLE")) {
			return new OracleDBConnection();
		} else {
			throw new IllegalArgumentException("Unsupported database type: " + dbType);
		}
	}
}
