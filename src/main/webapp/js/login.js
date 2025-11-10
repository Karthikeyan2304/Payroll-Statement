

//LOGIN ENCRYPTION HANDLER
document.addEventListener("DOMContentLoaded", () => {
	const form = document.querySelector("form[action='login']");
	if (!form) return;

	// AES key from backend
	const aesKeyBase64 = typeof AES_KEY_FROM_SERVER !== 'undefined' ? AES_KEY_FROM_SERVER : null;
	if (!aesKeyBase64) return;

	form.addEventListener("submit", function(event) {
		event.preventDefault();

		const username = document.querySelector("input[name='username']").value;
		const password = document.querySelector("input[name='password']").value;

		if (!username || !password) {
			alert("Please enter both username and password.");
			return;
		}

		// Encrypt fields
		const key = CryptoJS.enc.Base64.parse(aesKeyBase64);
		const encUsername = CryptoJS.AES.encrypt(username, key, {
			mode: CryptoJS.mode.ECB,
			padding: CryptoJS.pad.Pkcs7
		}).toString();

		const encPassword = CryptoJS.AES.encrypt(password, key, {
			mode: CryptoJS.mode.ECB,
			padding: CryptoJS.pad.Pkcs7
		}).toString();

		document.querySelector("input[name='username']").value = encUsername;
		document.querySelector("input[name='password']").value = encPassword;

		form.submit();

		// Clear sensitive data
		setTimeout(() => { window.aesKeyBase64 = null; }, 500);
	});
});

// ========== OTP + TIMER LOGIC ==========
document.addEventListener("DOMContentLoaded", () => {
	const otpInput = document.getElementById("otpCode");
	const resentOtpBtn = document.getElementById("resentOtp");
	const verifyOtpBtn = document.getElementById("verifyOtp");
	const timerText = document.getElementById("timerText");

	// If OTP section doesn't exist (login page only), skip this logic
	if (!otpInput || !resentOtpBtn) return;

	// Hide Resend button initially
	resentOtpBtn.hidden = true;
	let timeLeft = 50; // 50 secs

	const timer = setInterval(() => {
		timeLeft--;
		timerText.textContent = `Resend OTP in ${timeLeft}s`;

		if (timeLeft <= 0) {
			clearInterval(timer);
			timerText.textContent = "";
			resentOtpBtn.hidden = false; // show button only after timer
		}
	}, 1000);

	// Enable Verify OTP button only if input filled
	otpInput.addEventListener("input", () => {
		verifyOtpBtn.disabled = otpInput.value.trim() === "";
	});

	// ========== RESEND OTP BACKEND CALL ==========
	resentOtpBtn.addEventListener("click", () => {
		fetch("resendOtp", { method: "POST" })
			.then(response => response.json())
			.then(data => {
				if (data.status === "success") {
					alert("New OTP sent to your registered number.");
					resentOtpBtn.hidden = true;
					timeLeft = 50;
					timerText.textContent = `Resend OTP in ${timeLeft}s`;

					const newTimer = setInterval(() => {
						timeLeft--;
						timerText.textContent = `Resend OTP in ${timeLeft}s`;
						if (timeLeft <= 0) {
							clearInterval(newTimer);
							timerText.textContent = "";
							resentOtpBtn.hidden = false;
						}
					}, 1000);
				} else {
					alert("Failed to send OTP. Try again.");
				}
			})
			.catch(err => console.error(err));
	});
});

// ========== LOGIN BUTTON HANDLER ==========
function handleLoginForm() {
	const loginForm = document.querySelector('form[action="login"]');
	const usernameField = document.getElementById("exampleInputEmail1");
	const passwordField = document.getElementById("exampleInputPassword1");
	const loginBtn = document.getElementById("loginBtn");

	if (!loginForm || !usernameField || !passwordField || !loginBtn) return;

	loginForm.addEventListener("submit", () => {
		usernameField.disabled = true;
		passwordField.disabled = true;
		loginBtn.disabled = true;
		loginBtn.textContent = "Signing in...";
	});

	// Enable again if error message appears
	const errorMsg = document.querySelector("p[th\\:text='${error}'], p[style*='color: red']");
	if (errorMsg && errorMsg.textContent.trim() !== "") {
		usernameField.disabled = false;
		passwordField.disabled = false;
		loginBtn.disabled = false;
		loginBtn.textContent = "Login";
	}
}
document.addEventListener("DOMContentLoaded", handleLoginForm);

