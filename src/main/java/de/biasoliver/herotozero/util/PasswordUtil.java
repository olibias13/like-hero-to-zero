package de.biasoliver.herotozero.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

	private static final int ITERATIONS = 120_000;
	private static final int KEY_LENGTH = 256;

	private PasswordUtil() {
	}

	public static String hash(String password) {
		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		byte[] hash = pbkdf2(password.toCharArray(), salt);
		return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
	}

	public static boolean verify(String password, String stored) {
		String[] parts = stored.split(":");
		byte[] salt = Base64.getDecoder().decode(parts[0]);
		byte[] expected = Base64.getDecoder().decode(parts[1]);
		byte[] actual = pbkdf2(password.toCharArray(), salt);
		return MessageDigest.isEqual(expected, actual);
	}

	private static byte[] pbkdf2(char[] password, byte[] salt) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			return factory.generateSecret(new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH)).getEncoded();
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException(e);
		}
	}
}