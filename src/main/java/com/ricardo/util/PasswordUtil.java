package com.ricardo.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    private PasswordUtil() {};

    // Hash a plain-text password — call this on register
    // BCrypt.gensalt() generates a random salt with cost factor 10 (default)
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }


    // Compare a plain-text attempt against a stored hash — call this on login
    // Returns true if they match, false otherwise
    // NEVER compare hashes with .equals() — use this method always
    public static boolean verify(String plainPassword, String storedHash) {
        return BCrypt.checkpw(plainPassword, storedHash);
    }
}
