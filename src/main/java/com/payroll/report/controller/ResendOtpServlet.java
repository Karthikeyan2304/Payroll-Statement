package com.payroll.report.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payroll.report.service.UserService;

public class ResendOtpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static UserService userService;
	private static final Logger LOG=LoggerFactory.getLogger(ResendOtpServlet.class);
	static {
		userService = new UserService();
	}

	public ResendOtpServlet() {
		super();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("application/json");
		HttpSession session = req.getSession(false);

		if (session == null || session.getAttribute("loggedInUser") == null) {
			resp.getWriter().write("{\"status\":\"error\", \"message\":\"Session expired. Please login again.\"}");
			return;
		}

		String userName = (String) session.getAttribute("loggedInUser");
		String phoneNumber = null;
		try {
			phoneNumber = userService.getUserMobile(userName);
			if (phoneNumber == null || phoneNumber.isEmpty()) {
				phoneNumber = "+918608041997"; // fallback for testing
			}

			userService.sendOTPToUser(phoneNumber);
			session.setAttribute("otpPending", true);
			session.setAttribute("pendingOTPUser", userName);
			session.setAttribute("pendingPhone", phoneNumber);
			resp.sendRedirect(req.getContextPath() + "/login");
			return;

		} catch (SQLException e) {
			LOG.error("SQLException in the doPost {} : ",e.getMessage(),e);
			session.setAttribute("otpPending", true);
			session.setAttribute("pendingOTPUser", userName);
			session.setAttribute("pendingPhone", phoneNumber);
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		} catch (Exception e) {
			LOG.error("Exception in the doPost {} : ",e.getMessage(),e);
			session.setAttribute("otpPending", true);
			session.setAttribute("pendingOTPUser", userName);
			session.setAttribute("pendingPhone", phoneNumber);
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
	}

}
