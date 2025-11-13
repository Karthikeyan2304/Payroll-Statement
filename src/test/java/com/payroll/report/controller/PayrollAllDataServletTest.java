package com.payroll.report.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.payroll.report.model.PayrollStatement;
import com.payroll.report.service.PayrollService;



@ExtendWith(MockitoExtension.class)
class PayrollAllDataServletTest {

	private PayrollAllDataServlet servlet;

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpSession session;
	@Mock
	private ServletConfig servletConfig;
	@Mock
	private ServletContext servletContext;
	@Mock
	private PayrollService payrollServiceMock;
	@Mock
	private TemplateEngine templateEngineMock;

	private StringWriter responseWriter;

	@BeforeEach
	void setUp() throws Exception {
		servlet = new PayrollAllDataServlet();

		// Initialize servlet to avoid IllegalStateException
		when(servletConfig.getServletContext()).thenReturn(servletContext);
		servlet.init(servletConfig);

		// Inject mocks
		servlet.payrollService = payrollServiceMock;
		servlet.templateEngine = templateEngineMock;

		// Prepare response writer
		responseWriter = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
		when(response.getLocale()).thenReturn(Locale.ENGLISH);

		// Session attributes
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn("john");
		when(session.getAttribute("isAdmin")).thenReturn("true");
		when(session.getAttribute("userType")).thenReturn("Admin");
	}

	@Test
	void testDoGet_WithData_ShouldRenderAllPayrolls() throws Exception {
		// Mock data
		PayrollStatement ps = new PayrollStatement();
		when(payrollServiceMock.getAllPayrolls(null, null)).thenReturn(Arrays.asList(ps));

		// Mock Thymeleaf rendering
		doAnswer(inv -> {
			PrintWriter writer = inv.getArgument(2);
			writer.write("Rendered all payrolls");
			return null;
		}).when(templateEngineMock).process(anyString(), any(WebContext.class), any(PrintWriter.class));

		servlet.doGet(request, response);

		verify(templateEngineMock, times(1)).process(eq("allpayroll"), any(WebContext.class), any(PrintWriter.class));

		String output = responseWriter.toString();
		assert output.contains("Rendered all payrolls");
	}

	@Test
	void testDoGet_NoData_ShouldShowNoDataMessage() throws Exception {
		// Return an empty list
		when(payrollServiceMock.getAllPayrolls(null, null)).thenReturn(Arrays.asList());

		doAnswer(inv -> {
			PrintWriter writer = inv.getArgument(2);
			writer.write("No data found");
			return null;
		}).when(templateEngineMock).process(anyString(), any(WebContext.class), any(PrintWriter.class));

		servlet.doGet(request, response);

		verify(templateEngineMock, times(1)).process(eq("allpayroll"), any(WebContext.class), any(PrintWriter.class));

		String output = responseWriter.toString();
		assert output.contains("No data found");
	}

	@Test
	void testDoPost_DelegatesToDoGet() throws Exception {
		// Mock data
		PayrollStatement ps = new PayrollStatement();
		when(payrollServiceMock.getAllPayrolls(null, null)).thenReturn(Arrays.asList(ps));

		doAnswer(inv -> {
			PrintWriter writer = inv.getArgument(2);
			writer.write("Post handled as Get");
			return null;
		}).when(templateEngineMock).process(anyString(), any(WebContext.class), any(PrintWriter.class));

		servlet.doPost(request, response);

		verify(templateEngineMock, times(1)).process(eq("allpayroll"), any(WebContext.class), any(PrintWriter.class));

		assert responseWriter.toString().contains("Post handled as Get");
	}
}
