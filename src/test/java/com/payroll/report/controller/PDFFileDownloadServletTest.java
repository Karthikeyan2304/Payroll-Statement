package com.payroll.report.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.payroll.report.util.ExportPDFUtil;



class PDFFileDownloadServletTest {

	private PDFFileDownloadServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;

	@BeforeEach
	void setUp() throws ServletException {
		servlet = new PDFFileDownloadServlet();
		servlet.init(); // initialize TemplateEngine and PayrollService

		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
	}

	@Test
	void testDoGet_success() throws ServletException, IOException {
		// Mock request parameters
		when(request.getParameter("month")).thenReturn("2025-11");
		when(request.getParameter("allPayroll")).thenReturn("false");

		// Mock the static ExportPDFUtil.exportPDF method
		try (MockedStatic<ExportPDFUtil> pdfMock = Mockito.mockStatic(ExportPDFUtil.class)) {
			pdfMock.when(() -> ExportPDFUtil.exportPDF(anyString(), any(String[].class), eq(response), anyString(),
					anyBoolean(), anyString())).thenAnswer(invocation -> null); // do nothing

			// Call the servlet
			servlet.doGet(request, response);

			// Verify the static method was called
			pdfMock.verify(() -> ExportPDFUtil.exportPDF(anyString(), any(String[].class), eq(response), anyString(),
					anyBoolean(), anyString()), times(1));

			// Verify response content type is set to PDF
			verify(response).setContentType("application/pdf");
		}
	}

	@Test
	void testDoPost_callsDoGet() throws ServletException, IOException {
		// Simply check that doPost calls doGet
		PDFFileDownloadServlet spyServlet = spy(servlet);

		spyServlet.doPost(request, response);

		verify(spyServlet, times(1)).doGet(request, response);
	}
}
