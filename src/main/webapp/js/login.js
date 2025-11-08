if (window.history.replaceState) {
	window.history.replaceState(null, null, window.location.href);
}


document.addEventListener("DOMContentLoaded", () => {
	// Injected by Thymeleaf from server
	const aesKeyBase64 = AES_KEY_FROM_SERVER;
	console.log("aes key  " + aesKeyBase64);
	const form = document.querySelector("form[action='login']");
	form.addEventListener("submit", function(event) {
		event.preventDefault();

		const username = document.querySelector("input[name='username']").value;
		const password = document.querySelector("input[name='password']").value;

		// Decode Base64 key into CryptoJS WordArray
		const key = CryptoJS.enc.Base64.parse(aesKeyBase64);

		// Encrypt with AES/ECB/PKCS5Padding (Java-compatible)
		const encUsername = CryptoJS.AES.encrypt(username, key, {
			mode: CryptoJS.mode.ECB,
			padding: CryptoJS.pad.Pkcs7
		}).toString();

		const encPassword = CryptoJS.AES.encrypt(password, key, {
			mode: CryptoJS.mode.ECB,
			padding: CryptoJS.pad.Pkcs7
		}).toString();

		// Replace plaintext with ciphertext
		document.querySelector("input[name='username']").value = encUsername;
		document.querySelector("input[name='password']").value = encPassword;

		// Submit the encrypted form
		this.submit();

		//  Immediately remove key from memory after use
		setTimeout(() => { window.aesKeyBase64 = null; }, 500);
	});
});


//2 Minutes Timer
document.addEventListener("DOMContentLoaded", () => {
	let timeLeft = 50;
	const timerText = document.getElementById("timerText");
	const otpInput = document.getElementById("otpCode");
	const resentOtpBtn = document.getElementById("resentOtp");
	const verifyOtpBtn = document.getElementById("verifyOtp");

	const timer = setInterval(() => {
		timeLeft--;
		timerText.textContent = `Resend OTP in ${timeLeft}s`;

		if (timeLeft <= 0) {
			clearInterval(timer);
			timerText.textContent = "";
			checkOtpInput();
		}
	}, 1000);
	otpInput.addEventListener("input", checkOtpInput);

	function checkOtpInput() {
		if (timeLeft <= 0 && otpInput.value.trim() !== "") {
			//resentOtpBtn.disabled = false;

		} else {
			//resentOtpBtn.disabled = true;



		}
		if (otpInput.value.trim() !== "") {
			verifyOtpBtn.disabled = false;
		}
		else {
			verifyOtpBtn.disabled = true;
		}
	}


});



setTimeout(() => {
	document.getElementById("resentOtp").hidden = false;
}, 50000);

// When clicked, call backend to resend OTP
document.getElementById("resentOtp").addEventListener("click", () => {
	fetch("resendOtp", {
		method: "POST"
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {
				alert("New OTP has been sent to your registered number.");
				document.getElementById("resentOtp").hidden = true;

				// Show again after 2 mins
				setTimeout(() => {
					document.getElementById("resentOtp").hidden = false;
				}, 120000);
			} else {
				alert("Failed to send OTP. Try again.");
			}
		})
		.catch(err => console.error(err));
});






