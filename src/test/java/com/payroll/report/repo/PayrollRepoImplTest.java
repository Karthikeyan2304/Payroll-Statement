package com.payroll.report.repo;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.*;

public class PayrollRepoImplTest {

	private static JdbcConnectionPool cp;
	private Connection connection;

	@BeforeAll
	static void initAll() {
		// Initialize H2 in-memory database
		cp = JdbcConnectionPool.create("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
	}

	@BeforeEach
	void setUp() throws Exception {
		connection = cp.getConnection();

		try (Statement stmt = connection.createStatement()) {
			stmt.execute("""
					    CREATE TABLE IF NOT EXISTS payroll_statement (
					        id BIGINT AUTO_INCREMENT PRIMARY KEY,
					        emp_code VARCHAR(20),
					        name VARCHAR(50),
					        payroll_month DATE,
					        basic_pay DECIMAL(10,2)
					    );
					""");

			stmt.execute("DELETE FROM payroll_statement");
		}
	}

	@Test
	void testInsertPayrollRecord() throws Exception {
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO payroll_statement (emp_code, name, payroll_month, basic_pay) VALUES (?, ?, ?, ?)")) {
			ps.setString(1, "EMP001");
			ps.setString(2, "John Doe");
			ps.setDate(3, java.sql.Date.valueOf("2025-10-01"));
			ps.setBigDecimal(4, new java.math.BigDecimal("50000.00"));
			int rows = ps.executeUpdate();

			assertEquals(1, rows, "Should insert one record");
		}
	}

	@Test
	void testSelectPayrollRecord() throws Exception {
		try (Statement stmt = connection.createStatement()) {
			stmt.execute("INSERT INTO payroll_statement (emp_code, name, payroll_month, basic_pay) "
					+ "VALUES ('EMP002', 'Jane Doe', '2025-09-01', 45000.00)");
		}

		// Verify it exists
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM payroll_statement WHERE emp_code='EMP002'")) {

			assertTrue(rs.next(), "Record should exist");
			assertEquals("Jane Doe", rs.getString("name"));
			assertEquals(45000.00, rs.getDouble("basic_pay"));
		}
	}

	@AfterEach
	void tearDown() throws Exception {
		connection.close();
	}

	@AfterAll
	static void cleanUp() {
		cp.dispose();
	}
}
