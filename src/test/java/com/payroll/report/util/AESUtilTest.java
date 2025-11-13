package com.payroll.report.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;

class AESUtilTest {

	@Test
	void testDecrypt_ValidInput() throws Exception {
		// --- Arrange ---
		String originalText = "HelloWorld123";
		String base64Key = generateDummyBase64Key(); // 16 bytes key

		// Encrypt the original text for testing
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		byte[] decodedKey = Base64.getDecoder().decode(base64Key);
		SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedBytes = cipher.doFinal(originalText.getBytes());
		String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);

		// --- Act ---
		String decryptedText = AESUtil.decrypt(encryptedText, base64Key);

		// --- Assert ---
		assertNotNull(decryptedText);
		assertEquals(originalText, decryptedText);
	}

	@Test
	void testDecrypt_InvalidCipherText() {
		String fakeCipherText = "invalidCipher";
		String fakeKey = generateDummyBase64Key();

		String result = AESUtil.decrypt(fakeCipherText, fakeKey);

		assertNull(result, "Decryption should return null for invalid ciphertext");
	}

	@Test
	void testDecrypt_InvalidKey() {
		String fakeCipherText = "anyText";
		String invalidKey = Base64.getEncoder().encodeToString("shortKey".getBytes()); // not 16 bytes

		String result = AESUtil.decrypt(fakeCipherText, invalidKey);

		assertNull(result, "Decryption should return null for invalid key");
	}

	// Helper method to generate 16-byte Base64 key
	private String generateDummyBase64Key() {
		byte[] key = new byte[16];
		for (int i = 0; i < 16; i++) {
			key[i] = (byte) i;
		}
		return Base64.getEncoder().encodeToString(key);
	}
}
