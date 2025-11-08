# Payroll-Statement
🧾 Payroll Statement Module

This module provides a secure and efficient solution for managing employee payroll statements with detailed reporting and inquiry features. It includes server-side encryption for sensitive data, session and cookie-based authentication, and OTP verification for added security.

🔹 Key Features

Employee Payroll Inquiry & Reporting
View YTD, current, and previous month payroll details with dynamic filtering.

Individual Employee Details Modal
View detailed salary breakdown and deductions in a responsive modal window.

Secure Login System
AES encryption for credentials, session-based authentication, and cookie validation.

OTP Verification Flow
Configurable OTP-based login for enhanced user security.

PDF Export & Reporting
Generate and download payroll statements in PDF format.

Servlet Filters for Access Control
Implements authentication filter to restrict unauthorized access.

🔹 Tech Stack

Backend: Java Servlet, Thymeleaf, JDBC, Oracle

Security: AES Encryption, Secure Cookies, OTP Verification

Frontend: HTML5, Bootstrap 5, JavaScript, jQuery

Tools: Apache Tomcat, Maven

🔹 Highlights

Implements Post-Redirect-Get (PRG) pattern to prevent form resubmission.

Uses session + cookie validation for secure user session management.

Follows clean MVC separation between Controller, Service, and DAO layers.s
