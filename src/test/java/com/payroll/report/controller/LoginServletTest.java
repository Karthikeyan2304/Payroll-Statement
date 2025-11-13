package com.payroll.report.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.payroll.report.service.UserService;
import com.payroll.report.util.AESUtil;

class LoginServletTest {

	private LoginServlet servlet;

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpSession session;
	@Mock
	private TemplateEngine templateEngineMock;
	@Mock
	private UserService userServiceMock;

	private StringWriter responseWriter;

	@Mock
	private ServletConfig servletConfig;
	@Mock
	private ServletContext servletContext;

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.openMocks(this);
		servlet = new LoginServlet();

		// inject mocks
		servlet.templateEngine = templateEngineMock;
		servlet.userService = userServiceMock;

		// ServletConfig/ServletContext
		when(servletConfig.getServletContext()).thenReturn(servletContext);
		servlet.init(servletConfig);

		// session / writer mocks
		responseWriter = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
		when(request.getSession(false)).thenReturn(session);
		when(request.getSession(true)).thenReturn(session);

		// TemplateEngine mock
		doAnswer(inv -> {
			PrintWriter writer = inv.getArgument(2);
			writer.write("Rendered login page");
			return null;
		}).when(templateEngineMock).process(anyString(), any(WebContext.class), any(PrintWriter.class));
	}

	@Test
	void testDoGet_AlreadyLoggedIn_ShouldRedirect() throws Exception {
		when(session.getAttribute("loggedInUser")).thenReturn("john");

		servlet.doGet(request, response);

		verify(response).sendRedirect(contains("/welcome"));
	}

}
