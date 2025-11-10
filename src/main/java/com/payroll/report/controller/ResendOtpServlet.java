package com.payroll.report.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.payroll.report.service.UserService;

/**
 * Servlet implementation class ResendOtpServlet
 */
public class ResendOtpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static UserService userService;
	static {
		userService = new UserService();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResendOtpServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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
			e.printStackTrace();
			session.setAttribute("otpPending", true);
			session.setAttribute("pendingOTPUser", userName);
			session.setAttribute("pendingPhone", phoneNumber);
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("otpPending", true);
			session.setAttribute("pendingOTPUser", userName);
			session.setAttribute("pendingPhone", phoneNumber);
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
	}

}
