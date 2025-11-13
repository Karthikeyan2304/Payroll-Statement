package com.payroll.report.util;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.payroll.report.constant.DBType;
import com.payroll.report.db.connection.DBConnection;
import com.payroll.report.db.connection.DBFactory;

public class DatabaseUtilTest {

	private DBConnection mockDBConnection;
	private Connection mockConnection;
	private PreparedStatement mockPreparedStatement;
	private ResultSet mockResultSet;

	@BeforeEach
	public void setup() throws Exception {
		mockDBConnection = mock(DBConnection.class);
		mockConnection = mock(Connection.class);
		mockPreparedStatement = mock(PreparedStatement.class);
		mockResultSet = mock(ResultSet.class);

		when(mockDBConnection.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
	}

	@Test
	public void testSaveUserLoginInDB() throws Exception {
		try (MockedStatic<DBFactory> mockedStatic = mockStatic(DBFactory.class)) {
			mockedStatic.when(() -> DBFactory.getConnection(DBType.ORACLE.name())).thenReturn(mockDBConnection);

			DatabaseUtil.saveUserLoginInDB("U001", "test@example.com", "ADMIN",
					new Timestamp(System.currentTimeMillis()), "Y");

			verify(mockPreparedStatement, times(1)).executeUpdate();
		}
	}

	@Test
	public void testGetUserID() throws Exception {
		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getString("ID")).thenReturn("U001");

		try (MockedStatic<DBFactory> mockedStatic = mockStatic(DBFactory.class)) {
			mockedStatic.when(() -> DBFactory.getConnection(DBType.ORACLE.name())).thenReturn(mockDBConnection);

			String userID = DatabaseUtil.getUserID("test@example.com");
			assert (userID.equals("U001"));
		}
	}

	@Test
	public void testDelOldUserAuditData() throws Exception {
		try (MockedStatic<DBFactory> mockedStatic = mockStatic(DBFactory.class)) {
			mockedStatic.when(() -> DBFactory.getConnection(DBType.ORACLE.name())).thenReturn(mockDBConnection);

			DatabaseUtil.delOldUserAuditData();
			verify(mockPreparedStatement, times(1)).executeUpdate();
		}
	}
}
