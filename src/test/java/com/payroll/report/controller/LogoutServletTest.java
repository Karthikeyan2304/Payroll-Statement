package com.payroll.report.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
class LogoutServletTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private HttpSession session;

	@InjectMocks
	private LogoutServlet servlet;

	@BeforeEach
	void setUp() throws Exception {
		servlet.init(); // initialize template engine
	}

	@Test
	void testDoGet_WithSession_ShouldInvalidateAndRedirect() throws Exception {
		// Arrange
		when(request.getSession(false)).thenReturn(session);
		when(request.getContextPath()).thenReturn("/payroll");

		// Act
		servlet.doGet(request, response);

		// Assert
		verify(session).invalidate(); // session invalidated
		verify(request).logout(); // request.logout() called
		verify(response).setHeader(eq("Cache-Control"), anyString());
		verify(response).setHeader(eq("Pragma"), anyString());
		verify(response).setDateHeader(eq("Expires"), eq(0L));
		verify(response).sendRedirect("/payroll/login");
	}

	@Test
	void testDoGet_NoSession_ShouldStillRedirect() throws Exception {
		// Arrange
		when(request.getSession(false)).thenReturn(null);
		when(request.getContextPath()).thenReturn("/payroll");

		// Act
		servlet.doGet(request, response);

		// Assert
		verify(request).logout(); // logout still called
		verify(response).sendRedirect("/payroll/login");
	}

	@Test
	void testDoGet_LogoutThrowsException_ShouldStillRedirect() throws Exception {
		// Arrange
		when(request.getSession(false)).thenReturn(session);
		when(request.getContextPath()).thenReturn("/payroll");
		doThrow(new ServletException("Logout failed")).when(request).logout();

		// Act
		servlet.doGet(request, response);

		// Assert
		verify(session).invalidate();
		verify(response).sendRedirect("/payroll/login");
	}

	@Test
	void testDoPost_DelegatesToDoGet() throws Exception {
		// Arrange
		when(request.getSession(false)).thenReturn(session);
		when(request.getContextPath()).thenReturn("/payroll");

		// Act
		servlet.doPost(request, response);

		// Assert
		verify(session).invalidate();
		verify(request).logout();
		verify(response).sendRedirect("/payroll/login");
	}
}
