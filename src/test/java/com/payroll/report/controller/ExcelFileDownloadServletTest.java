package com.payroll.report.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.service.PayrollService;
import com.payroll.report.util.ExportExcelUtil;



class ExcelFileDownloadServletTest {

	private ExcelFileDownloadServlet servlet;
	private HttpServletRequest requestMock;
	private HttpServletResponse responseMock;
	private PayrollService payrollServiceMock;
	private TemplateEngine templateEngine;

	@BeforeEach
	void setUp() throws Exception {
		servlet = new ExcelFileDownloadServlet();
		requestMock = mock(HttpServletRequest.class);
		responseMock = mock(HttpServletResponse.class);
		payrollServiceMock = mock(PayrollService.class);
		servlet.setPayrollService(payrollServiceMock);

		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(false);
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(resolver);
	}

	@Test
	void testDoGet_withMonthParameter_staticExportMocked() throws Exception {
		when(requestMock.getParameter("month")).thenReturn("2025-11");
		when(requestMock.getParameter("allPayroll")).thenReturn(null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		when(responseMock.getOutputStream()).thenReturn(new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				baos.write(b);
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setWriteListener(WriteListener listener) {
			}
		});

		try (MockedStatic<ExportExcelUtil> exportMock = mockStatic(ExportExcelUtil.class)) {

			exportMock.when(() -> ExportExcelUtil.exportExcel(any(), any(), anyBoolean(), any()))
					.thenAnswer(invocation -> null);

			// Run servlet
			servlet.doGet(requestMock, responseMock);

			exportMock.verify(() -> ExportExcelUtil.exportExcel(eq("2025-11"), any(), eq(false), eq(responseMock)),
					times(1));
		}
	}

	@Test
	void testDoGet_withAllPayrollFlag_staticExportMocked() throws Exception {
		when(requestMock.getParameter("month")).thenReturn(null);
		when(requestMock.getParameter("allPayroll")).thenReturn("true");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		when(responseMock.getOutputStream()).thenReturn(new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				baos.write(b);
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setWriteListener(WriteListener listener) {
			}
		});

		try (MockedStatic<ExportExcelUtil> exportMock = mockStatic(ExportExcelUtil.class)) {
			exportMock.when(() -> ExportExcelUtil.exportExcel(any(), any(), anyBoolean(), any()))
					.thenAnswer(invocation -> null);

			servlet.doGet(requestMock, responseMock);
			
		}
	}
}
