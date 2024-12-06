package org.sasha.dependencies;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

    public static String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] saltBytes = new byte[16];
        secureRandom.nextBytes(saltBytes);
        StringBuilder saltString = new StringBuilder();
        for (byte b : saltBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) saltString.append('0');
            saltString.append(hex);
        }
        return saltString.toString();
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;
            byte[] hashedBytes = messageDigest.digest(saltedPassword.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка хеширования пароля: " + e.getMessage(), e);
        }
    }

    public static boolean verifyPassword(String rawPassword, String salt, String storedHash) {
        String calculatedHash = hashPassword(rawPassword, salt);
        return MessageDigest.isEqual(calculatedHash.getBytes(), storedHash.getBytes());
    }
}
