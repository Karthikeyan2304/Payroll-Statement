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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOG.info("AuthFilter initiated");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		LOG.info("AuthFilter doFilter started");

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		String path = req.getRequestURI();

		if (path.endsWith("login") || path.endsWith("logout") || path.contains("otpVerify") || path.contains("css")
				|| path.contains("js") || path.contains("images")) {

			chain.doFilter(request, response);
			return;
		}

		if (session == null || session.getAttribute("loggedInUser") == null) {
			LOG.warn("No active session, redirecting to login");
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		chain.doFilter(request, response);
		LOG.info("AuthFilter doFilter ended");
	}

	@Override
	public void destroy() {
		LOG.info("AuthFilter destroyed");
	}
}
