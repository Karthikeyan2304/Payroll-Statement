package com.payroll.report.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	public static String decrypt(String cipherText, String base64Key) {
		try {
			byte[] decodedKey = Base64.getDecoder().decode(base64Key);
			SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] decodedValue = Base64.getDecoder().decode(cipherText);
			byte[] decrypted = cipher.doFinal(decodedValue);
			return new String(decrypted);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
