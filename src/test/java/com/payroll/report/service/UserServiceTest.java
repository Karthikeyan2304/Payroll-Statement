package com.payroll.report.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import com.payroll.report.repo.UserRepoImpl;

class UserServiceTest {

	private Connection h2Connection;

	@Spy
	private UserRepoImpl userRepoSpy;

	private UserService userService;

	@BeforeEach
	void setUp() throws Exception {
		// H2 in-memory DB setup
		Class.forName("org.h2.Driver");
		h2Connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");

		// Create user table and insert sample data
		try (PreparedStatement stmt = h2Connection
				.prepareStatement("CREATE TABLE users (" + "username VARCHAR(50) PRIMARY KEY,"
						+ "password_hash VARCHAR(255)," + "mobile VARCHAR(20)," + "role VARCHAR(20)" + ")")) {
			stmt.execute();
		}

		try (PreparedStatement stmt = h2Connection.prepareStatement(
				"INSERT INTO users (username, password_hash, mobile, role) VALUES ('john', 'hashedPass', '+911234567890', 'USER')")) {
			stmt.execute();
		}

		// Spy the UserRepoImpl and override getConnection()
		userRepoSpy = spy(new UserRepoImpl());
		doReturn(h2Connection).when(userRepoSpy).getConnection();

		// Inject spy into UserService
		userService = new UserService();
		UserService.userRepo = userRepoSpy;
	}

	@AfterEach
	void tearDown() throws Exception {
		if (h2Connection != null && !h2Connection.isClosed()) {
			try (PreparedStatement stmt = h2Connection.prepareStatement("DROP ALL OBJECTS")) {
				stmt.execute();
			}
			h2Connection.close();
		}
	}

	@Test
	void testCheckValidUser_ReturnsTrue() throws Exception {
		// Mock the password check
		doReturn(true).when(userRepoSpy).checkValidUser("john", "password");

		boolean valid = userService.checkValidUser("john", "password");
		assertTrue(valid);
	}

	@Test
	void testGetUserMobile_ReturnsMobileNumber() throws Exception {
		doReturn("+911234567890").when(userRepoSpy).getUserMobile("john");

		String mobile = userService.getUserMobile("john");
		assertEquals("+911234567890", mobile);
	}

	@Test
	void testDuplicateUserCheck_ReturnsTrue() throws Exception {
		doReturn(true).when(userRepoSpy).duplicateUserCheck("john");

		boolean duplicate = userService.duplicateUserCheck("john", "password");
		assertTrue(duplicate);
	}

	@Test
	void testGetUserType_AdminRole() throws Exception {
		doReturn(true).when(userRepoSpy).getUserType("admin");

		boolean isAdmin = userService.getUserType("admin");
		assertTrue(isAdmin);
	}

	@Test
	void testSendOTPToUser_DoesNotThrow() throws Exception {
		// Mock the Twilio OTP method to do nothing
		doNothing().when(userRepoSpy).sendOTPToUser("+911234567890");

		assertDoesNotThrow(() -> userService.sendOTPToUser("+911234567890"));
	}
}
