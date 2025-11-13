package com.payroll.report.filter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class AuthFilterTest {

	private AuthFilter authFilter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain filterChain;
	private HttpSession session;

	@BeforeEach
	void setUp() {
		authFilter = new AuthFilter();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		filterChain = mock(FilterChain.class);
		session = mock(HttpSession.class);
	}

	@Test
	void testDoFilter_AllowedUrls() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/app/login");
		when(request.getContextPath()).thenReturn("/app");

		authFilter.doFilter(request, response, filterChain);

		verify(filterChain, times(1)).doFilter(request, response);
		verify(response, never()).sendRedirect(anyString());
	}

	@Test
	void testDoFilter_NoSession() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/app/welcome");
		when(request.getContextPath()).thenReturn("/app");
		when(request.getSession(false)).thenReturn(null);

		authFilter.doFilter(request, response, filterChain);

		verify(response).sendRedirect("/app/login");
		verify(filterChain, never()).doFilter(request, response);
	}

	@Test
	void testDoFilter_InvalidSession_MissingAttributes() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/app/welcome");
		when(request.getContextPath()).thenReturn("/app");
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(null);
		when(session.getAttribute("sessionKey")).thenReturn(null);

		authFilter.doFilter(request, response, filterChain);

		verify(session).invalidate();
		verify(response).sendRedirect("/app/login");
		verify(filterChain, never()).doFilter(request, response);
	}

	@Test
	void testDoFilter_InvalidSession_KeyMismatch() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/app/welcome");
		when(request.getContextPath()).thenReturn("/app");
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn("testUser");
		when(session.getAttribute("sessionKey")).thenReturn("serverKey");

		Cookie cookie = new Cookie("sessionKey", "wrongKey");
		when(request.getCookies()).thenReturn(new Cookie[] { cookie });

		authFilter.doFilter(request, response, filterChain);

		verify(session).invalidate();
		verify(response).sendRedirect("/app/login");
		verify(filterChain, never()).doFilter(request, response);
	}

	@Test
	void testDoFilter_ValidSession() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("/app/welcome");
		when(request.getContextPath()).thenReturn("/app");
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn("testUser");
		when(session.getAttribute("sessionKey")).thenReturn("serverKey");

		Cookie cookie = new Cookie("sessionKey", "serverKey");
		when(request.getCookies()).thenReturn(new Cookie[] { cookie });

		authFilter.doFilter(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		verify(response, never()).sendRedirect(anyString());
	}
}
