package com.payroll.report.filter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AESKeyFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		HttpSession session = req.getSession(true);
		if (session.getAttribute("aesKey") == null) {
			byte[] keyBytes = new byte[16];
			new SecureRandom().nextBytes(keyBytes);
			String sessionKey = Base64.getEncoder().encodeToString(keyBytes);
			session.setAttribute("aesKey", sessionKey);
		}

		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {

	}

}
