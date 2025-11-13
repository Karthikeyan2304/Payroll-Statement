package com.payroll.report.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsRedirectFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(HttpsRedirectFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		LOG.info("HttpsRedirectFilter Started {} : 	", request.getRequestURI());
		if (request.isSecure()) {
			response.setHeader("Access-Control-Allow-Origin", "https://localhost:8443");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
			response.setHeader("Access-Control-Allow-Credentials", "true");
		}

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		}
		if (!request.isSecure()) {
			String redirectUrl = "https://" + request.getServerName() + ":8443" + request.getRequestURI();
			String query = request.getQueryString();
			if (query != null)
				redirectUrl += "?" + query;
			response.sendRedirect(redirectUrl);
			return;
		}

		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
