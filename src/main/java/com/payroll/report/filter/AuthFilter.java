package com.payroll.report.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		String uri = req.getRequestURI();
		String contextPath = req.getContextPath();

		// Allow login, OTP, and static resource URLs
		if (uri.startsWith(contextPath + "/login") || uri.startsWith(contextPath + "/otpVerify")
				|| uri.startsWith(contextPath + "/css/") || uri.startsWith(contextPath + "/js/")
				|| uri.startsWith(contextPath + "/images/")) {
			chain.doFilter(request, response);
			return;
		}

		// No active session → redirect to login
		if (session == null) {
			redirectToLogin(resp, contextPath);
			return;
		}

		// Fetch values from session
		String loggedInUser = (String) session.getAttribute("loggedInUser");
		String serverSessionKey = (String) session.getAttribute("sessionKey");

		// Read the sessionKey cookie (set during successful login)
		String cookieSessionKey = null;
		if (req.getCookies() != null) {
			for (Cookie cookie : req.getCookies()) {
				if ("sessionKey".equals(cookie.getName())) {
					cookieSessionKey = cookie.getValue();
					break;
				}
			}
		}

		// 5️ Validate presence of user and session key
		if (loggedInUser == null || serverSessionKey == null || cookieSessionKey == null) {
			System.out.println("[AuthFilter] Missing authentication details. Redirecting to login.");
			invalidateSession(session);
			redirectToLogin(resp, contextPath);
			return;
		}

		// 6️⃣ Cross-check sessionKey from cookie and server
		if (!serverSessionKey.equals(cookieSessionKey)) {
			System.out.println("[AuthFilter] Session key mismatch. Possible hijack attempt detected.");
			invalidateSession(session);
			redirectToLogin(resp, contextPath);
			return;
		}

		// 7️⃣ Everything is valid → proceed with request
		chain.doFilter(request, response);
	}

	private void redirectToLogin(HttpServletResponse resp, String contextPath) throws IOException {
		resp.sendRedirect(contextPath + "/login");
	}

	private void invalidateSession(HttpSession session) {
		try {
			session.invalidate();
		} catch (IllegalStateException ignored) {
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
