package com.payroll.report.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.util.ClientProperties;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.VerificationCheck;

/**
 * Servlet implementation class OTPVerificationServlet
 */
public class OTPVerifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OTPVerifyServlet.class);
	private TemplateEngine templateEngine;
	private static final String ACC_SID;
	private static final String AUTH_TOKEN;
	private static final String VERIFY_SERVICE_SID;
	static {
		String accSid = null;
		String authToken = null;
		String verifySid = null;
		try {
			accSid = ClientProperties.getProperty("twilio.accsid").trim();
			authToken = ClientProperties.getProperty("twilio.auth.token").trim();
			verifySid = ClientProperties.getProperty("twilio.verfiyaccsid").trim();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ACC_SID = accSid;
		AUTH_TOKEN = authToken;
		VERIFY_SERVICE_SID = verifySid;
	}

	@Override
	public void init() throws ServletException {
		Twilio.init(ACC_SID, AUTH_TOKEN);
		ClassLoaderTemplateResolver classLoaderTemp = new ClassLoaderTemplateResolver();
		classLoaderTemp.setPrefix("templates/");
		classLoaderTemp.setSuffix(".html");
		classLoaderTemp.setTemplateMode("HTML");
		classLoaderTemp.setCharacterEncoding("UTF-8");
		classLoaderTemp.setCacheable(false);
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(classLoaderTemp);
		// payrollService = new PayrollService();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		String phoneNumber = request.getParameter("phoneNumber");
		String otpCode = request.getParameter("otpCode");
		HttpSession session = request.getSession(true);

		WebContext context = new WebContext(request, response, getServletContext(), response.getLocale());

		try {
			VerificationCheck verificationCheck = VerificationCheck.creator(VERIFY_SERVICE_SID).setTo(phoneNumber)
					.setCode(otpCode).create();

			String status = verificationCheck.getStatus();
			LOG.info("Verification check for {}: {}", phoneNumber, status);

			if ("approved".equalsIgnoreCase(status)) {
				LOG.info("OTP verified for {}", phoneNumber);
				String userName = (String) session.getAttribute("loggedInUser");
				session.setAttribute("loggedInUser", userName);
				response.sendRedirect("welcome");
				templateEngine.process("welcome", context, response.getWriter());
			} else {
				request.setAttribute("error", "Invalid OTP. Please try again.");
				request.setAttribute("showOTPField", true);
				request.setAttribute("phoneNumber", phoneNumber);
				response.sendRedirect("welcome");
				templateEngine.process("login", context, response.getWriter());
			}

		} catch (ApiException e) {
			LOG.error("Error verifying OTP: {}", e.getMessage(), e);
			request.setAttribute("error", "Verification failed: " + e.getMessage());
			request.setAttribute("showOTPField", false);
			request.setAttribute("phoneNumber", phoneNumber);
			templateEngine.process("login", context, response.getWriter());
			;
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use POST to verify OTP");
	}
}
