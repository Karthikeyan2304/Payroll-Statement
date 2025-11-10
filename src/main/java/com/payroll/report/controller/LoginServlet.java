package com.payroll.report.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.payroll.report.service.PayrollService;
import com.payroll.report.service.UserService;
import com.payroll.report.util.AESUtil;
import com.payroll.report.util.ClientProperties;
import com.payroll.report.util.DatabaseUtil;

public class LoginServlet extends javax.servlet.http.HttpServlet {

	private static final long serialVersionUID = 2562418503227548100L;

	private TemplateEngine templateEngine;
	private PayrollService payrollService;
	private UserService userService;

	@Override
	public void init() throws ServletException {
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(false);

		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(resolver);

		payrollService = new PayrollService();
		userService = new UserService();
	}

	// -------------------------- GET ----------------------------
	@Override

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		WebContext context = new WebContext(req, resp, getServletContext(), resp.getLocale());

		HttpSession session = req.getSession(false);

		// Already logged in → redirect
		if (session != null && session.getAttribute("loggedInUser") != null && session.getAttribute("pendingOTPUser")==null) {
			resp.sendRedirect(req.getContextPath() + "/welcome");
			return;
		}

		// Create session if not exists for AES key
		if (session == null) {
			session = req.getSession(true);
		}

		// Generate AES key for the session
		byte[] keyBytes = new byte[16];
		new SecureRandom().nextBytes(keyBytes);
		String sessionKey = Base64.getEncoder().encodeToString(keyBytes);
		session.setAttribute("aesKey", sessionKey);
		 context.setVariable("aesKey", sessionKey);
		System.out.println("sessionKey : " + sessionKey);

		// OTP Pending flow
		if (Boolean.TRUE.equals(session.getAttribute("otpPending"))) {
			context.setVariable("showOTPField", true);
			context.setVariable("username", session.getAttribute("pendingOTPUser"));
			context.setVariable("phoneNumber", session.getAttribute("pendingPhone"));
			session.removeAttribute("otpPending");
		}

		templateEngine.process("login", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		WebContext context = new WebContext(req, resp, getServletContext(), resp.getLocale());

		try {
			// --- STEP 1: Read encrypted username and password from request ---
			String encryptedUserName = req.getParameter("username");
			String encryptedPassword = req.getParameter("password");

			if (encryptedUserName == null || encryptedPassword == null) {
				context.setVariable("error", "Username and Password are required");
				templateEngine.process("login", context, resp.getWriter());
				return;
			}

			// --- STEP 2: Retrieve AES key from session (set during GET) ---
			HttpSession session = req.getSession(false);
			if (session == null || session.getAttribute("aesKey") == null) {
				context.setVariable("error", "Session expired. Please reload the page and try again.");
				templateEngine.process("login", context, resp.getWriter());
				return;
			}

			String base64Key = (String) session.getAttribute("aesKey");
			// byte[] decodedKey = Base64.getDecoder().decode(base64Key);
//			SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");

			// --- STEP 3: Decrypt the credentials ---
			String userName = AESUtil.decrypt(encryptedUserName, base64Key);
			String password = AESUtil.decrypt(encryptedPassword, base64Key);

			if (userName == null || password == null || userName.isBlank() || password.isBlank()) {
				context.setVariable("error", "Invalid credentials.");
				templateEngine.process("login", context, resp.getWriter());
				return;
			}

			// --- STEP 4: Validate credentials in DB ---
			boolean isValidUser = userService.checkValidUser(userName, password);
			boolean otpFlag = Boolean.parseBoolean(ClientProperties.getProperty("otp.flag").trim());
			String phoneNumber = userService.getUserMobile(userName);

			if (!isValidUser) {
				context.setVariable("error", "Invalid username or password");
				templateEngine.process("login", context, resp.getWriter());
				return;
			}

			// --- STEP 5: Create secure session and cookie ---
			session.setAttribute("loggedInUser", userName);

			// Generate AES key for the session
			byte[] keyBytes = new byte[16];
			new SecureRandom().nextBytes(keyBytes);
			String sessionKey = Base64.getEncoder().encodeToString(keyBytes);
			session.setAttribute("sessionKey", sessionKey);
			Cookie userCookie = new Cookie("sessionKey", sessionKey);
			userCookie.setHttpOnly(true);
			userCookie.setSecure(true);
			userCookie.setMaxAge(5 * 60);
			userCookie.setPath("/");
			resp.addCookie(userCookie);

			// --- STEP 6: Role check ---
			boolean isAdmin = userService.getUserType(userName);
			String userType = isAdmin ? "Admin" : "User";
			session.setAttribute("isAdmin", isAdmin);
			session.setAttribute("userType", userType);

			// --- STEP 7: Audit logging ---
			String userID = DatabaseUtil.getUserID(userName);
			Timestamp loginTime = Timestamp.from(Instant.now());
			String otpRequired = otpFlag ? "Y" : "N";
			DatabaseUtil.saveUserLoginInDB(userID, userName, userType, loginTime, otpRequired);

			// --- STEP 8: OTP handling ---
			if (otpFlag) {
				if (phoneNumber == null || phoneNumber.isBlank()) {
					phoneNumber = "+918608041997";
				}
				userService.sendOTPToUser(phoneNumber);
				session.setAttribute("otpPending", true);
				session.setAttribute("pendingOTPUser", userName);
				session.setAttribute("pendingPhone", phoneNumber);
				resp.sendRedirect(req.getContextPath() + "/login");
				return;
			}

			// --- STEP 9: Direct login ---
			resp.sendRedirect(req.getContextPath() + "/welcome");

		} catch (Exception e) {
			e.printStackTrace();
			context.setVariable("error", "Error while decrypting or processing login: " + e.getMessage());
			templateEngine.process("login", context, resp.getWriter());
		}
	}

}
