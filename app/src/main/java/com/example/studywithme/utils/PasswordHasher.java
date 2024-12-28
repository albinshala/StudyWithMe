package com.example.studywithme.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public static String hashPassword(String password) {
        try {
            // Create a SHA-256 MessageDigest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert password to bytes using UTF-8 encoding
            byte[] hash = digest.digest(password.getBytes("UTF-8"));

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Add leading zero for single-character hex values
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            // Log and handle the exception properly
            e.printStackTrace();
            return ""; // Return an empty string to indicate an error
        }
    }

    // Optional: Password validation logic
    public static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }
}
