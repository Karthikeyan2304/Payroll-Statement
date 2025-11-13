package com.payroll.report.db.connection;

public class DBFactoryTest {
	private DBFactoryTest() {

	}

	public static DBConnectionTest getConnection(String dbType) {
		if (dbType.equalsIgnoreCase("ORACLE")) {
			return new OracleDBConnectionTest();
		} else {
			throw new IllegalArgumentException("Unsupported database type: " + dbType);
		}
	}

}
