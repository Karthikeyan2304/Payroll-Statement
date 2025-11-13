package com.payroll.report.db.connection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class OracleTest {

	private OracleDBConnection oracleDBConnection;

	private DataSource mockDataSource;
	private Connection mockConnection;
	private PreparedStatement mockPreparedStatement;
	private ResultSet mockResultSet;

	@BeforeEach
	void setUp() throws Exception {
		oracleDBConnection = new OracleDBConnection();

		mockDataSource = mock(DataSource.class);
		mockConnection = mock(Connection.class);
		mockPreparedStatement = mock(PreparedStatement.class);
		mockResultSet = mock(ResultSet.class);
	}

	@Test
	void testCloseConnection_AllResources() throws SQLException {
		oracleDBConnection.closeConnection(mockConnection, mockPreparedStatement, mockResultSet);

		verify(mockConnection).close();
		verify(mockPreparedStatement).close();
		verify(mockResultSet).close();
	}

	@Test
	void testCloseConnection_ConnectionAndPreparedStatementOnly() throws SQLException {
		oracleDBConnection.closeConnection(mockConnection, mockPreparedStatement);

		verify(mockConnection).close();
		verify(mockPreparedStatement).close();
	}

	@Test
	void testCloseConnection_NullResources() throws SQLException {
		// Should not throw exception
		assertDoesNotThrow(() -> oracleDBConnection.closeConnection(null, null, null));
		assertDoesNotThrow(() -> oracleDBConnection.closeConnection(null, null));
	}
}
