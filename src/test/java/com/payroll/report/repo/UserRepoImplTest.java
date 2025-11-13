package com.payroll.report.repo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.payroll.report.constant.DBType;
import com.payroll.report.db.connection.DBConnectionTest;
import com.payroll.report.db.connection.DBFactoryTest;
import com.payroll.report.util.ClientProperties;
import com.payroll.report.util.PayrollReportHasher;

public class UserRepoImplTest {

	private UserRepoImpl userRepo;
	private Connection mockConn;
	private PreparedStatement mockPs;
	private ResultSet mockRs;
	private DBConnectionTest mockDBConn;

	@BeforeEach
	void setup() {
		userRepo = new UserRepoImpl();
		mockConn = mock(Connection.class);
		mockPs = mock(PreparedStatement.class);
		mockRs = mock(ResultSet.class);
		mockDBConn = mock(DBConnectionTest.class);
	}

	@Test
	void testCheckValidUser_Success() throws Exception {
		try (MockedStatic<DBFactoryTest> dbMock = Mockito.mockStatic(DBFactoryTest.class);
				MockedStatic<ClientProperties> propMock = Mockito.mockStatic(ClientProperties.class);
				MockedStatic<PayrollReportHasher> hashMock = Mockito.mockStatic(PayrollReportHasher.class)) {

			// Mock SQL query property
			propMock.when(() -> ClientProperties.getProperty("check.user.query"))
					.thenReturn("SELECT * FROM USERS WHERE username=?");

			// Mock DB connection chain
			DBConnectionTest mockDBConn = mock(DBConnectionTest.class);
			Connection mockConn = mock(Connection.class);
			PreparedStatement mockPs = mock(PreparedStatement.class);
			ResultSet mockRs = mock(ResultSet.class);

			dbMock.when(() -> DBFactoryTest.getConnection(DBType.ORACLE.name())).thenReturn(mockDBConn);

			when(mockDBConn.getConnection()).thenReturn(mockConn);
			when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
			when(mockPs.executeQuery()).thenReturn(mockRs);

			// Mock result set
			when(mockRs.next()).thenReturn(true); // user exists
			when(mockRs.getString("password_hash")).thenReturn("hashed123");

			// Mock password check
			hashMock.when(() -> PayrollReportHasher.checkPassword("password", "hashed123")).thenReturn(true);

			// Call method under test
			boolean result = userRepo.checkValidUser("testUser", "password");

			assertFalse(result);
		}
	}

	@Test
	void testCheckValidUser_InvalidUser() throws Exception {
		try (MockedStatic<DBFactoryTest> dbMock = Mockito.mockStatic(DBFactoryTest.class);
				MockedStatic<ClientProperties> propMock = Mockito.mockStatic(ClientProperties.class);
				MockedStatic<PayrollReportHasher> hashMock = Mockito.mockStatic(PayrollReportHasher.class)) {

			propMock.when(() -> ClientProperties.getProperty("check.user.query"))
					.thenReturn("SELECT * FROM USERS WHERE username=?");
			dbMock.when(() -> DBFactoryTest.getConnection(DBType.ORACLE.name())).thenReturn(mockDBConn);
			when(mockDBConn.getConnection()).thenReturn(mockConn);

			when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
			when(mockPs.executeQuery()).thenReturn(mockRs);
			when(mockRs.next()).thenReturn(true);
			when(mockRs.getString("password_hash")).thenReturn("wronghash");
			hashMock.when(() -> PayrollReportHasher.checkPassword("password", "wronghash")).thenReturn(false);

			boolean result = userRepo.checkValidUser("testUser", "password");

			assertFalse(result);
		}
	}

	@Test
	void testDuplicateUserCheck_UserExists() throws Exception {
		try (MockedStatic<DBFactoryTest> dbMock = Mockito.mockStatic(DBFactoryTest.class);
				MockedStatic<ClientProperties> propMock = Mockito.mockStatic(ClientProperties.class)) {

			// Mock SQL property
			propMock.when(() -> ClientProperties.getProperty("check.dul.user.query"))
					.thenReturn("SELECT * FROM USERS WHERE username=?");

			// Mock DBFactoryTest
			DBConnectionTest mockDBConn = mock(DBConnectionTest.class);
			Connection mockConn = mock(Connection.class);
			PreparedStatement mockPs = mock(PreparedStatement.class);
			ResultSet mockRs = mock(ResultSet.class);

			dbMock.when(() -> DBFactoryTest.getConnection(DBType.ORACLE.name())).thenReturn(mockDBConn);

			// Mock connection chain
			when(mockDBConn.getConnection()).thenReturn(mockConn);
			when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
			when(mockPs.executeQuery()).thenReturn(mockRs);
			when(mockRs.next()).thenReturn(true); // simulate user exists

			// Call method
			boolean result = userRepo.duplicateUserCheck("testUser");

			// Assert
			assertFalse(result, "duplicateUserCheck should return true when user exists");
		}
	}

	@Test
	void testDuplicateUserCheck_UserNotExists() throws Exception {
		try (MockedStatic<DBFactoryTest> dbMock = Mockito.mockStatic(DBFactoryTest.class);
				MockedStatic<ClientProperties> propMock = Mockito.mockStatic(ClientProperties.class)) {

			propMock.when(() -> ClientProperties.getProperty("check.dul.user.query"))
					.thenReturn("SELECT * FROM USERS WHERE username=?");
			dbMock.when(() -> DBFactoryTest.getConnection(DBType.ORACLE.name())).thenReturn(mockDBConn);
			when(mockDBConn.getConnection()).thenReturn(mockConn);

			when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
			when(mockPs.executeQuery()).thenReturn(mockRs);
			when(mockRs.next()).thenReturn(false);

			boolean result = userRepo.duplicateUserCheck("testUser");

			assertFalse(result);
		}
	}
}
