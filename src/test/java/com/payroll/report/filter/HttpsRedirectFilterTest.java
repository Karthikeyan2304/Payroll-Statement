package com.payroll.report.filter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class HttpsRedirectFilterTest {

	private HttpsRedirectFilter filter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain chain;

	@BeforeEach
	void setUp() {
		filter = new HttpsRedirectFilter();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		chain = mock(FilterChain.class);
	}

	@Test
	void testDoFilter_SecureRequest() throws IOException, ServletException {
		when(request.isSecure()).thenReturn(true);
		when(request.getMethod()).thenReturn("GET");

		filter.doFilter(request, response, chain);

		verify(response).setHeader("Access-Control-Allow-Origin", "https://localhost:8443");
		verify(response).setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		verify(response).setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		verify(response).setHeader("Access-Control-Allow-Credentials", "true");
		verify(chain).doFilter(request, response);
		verify(response, never()).sendRedirect(anyString());
	}

	@Test
	void testDoFilter_OptionsMethod() throws IOException, ServletException {
		when(request.getMethod()).thenReturn("OPTIONS");

		filter.doFilter(request, response, chain);

		verify(response).setStatus(HttpServletResponse.SC_OK);
		verify(chain, never()).doFilter(request, response);
		verify(response, never()).sendRedirect(anyString());
	}

	@Test
	void testDoFilter_InsecureRequest() throws IOException, ServletException {
		when(request.isSecure()).thenReturn(false);
		when(request.getServerName()).thenReturn("localhost");
		when(request.getRequestURI()).thenReturn("/test");
		when(request.getQueryString()).thenReturn("param=value");

		filter.doFilter(request, response, chain);

		verify(response).sendRedirect("https://localhost:8443/test?param=value");
		verify(chain, never()).doFilter(request, response);
	}

	@Test
	void testDoFilter_InsecureRequest_NoQuery() throws IOException, ServletException {
		when(request.isSecure()).thenReturn(false);
		when(request.getServerName()).thenReturn("localhost");
		when(request.getRequestURI()).thenReturn("/test");
		when(request.getQueryString()).thenReturn(null);

		filter.doFilter(request, response, chain);

		verify(response).sendRedirect("https://localhost:8443/test");
		verify(chain, never()).doFilter(request, response);
	}
}
