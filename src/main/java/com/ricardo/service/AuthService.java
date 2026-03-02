package com.ricardo.service;

import com.ricardo.dto.AuthResponse;
import com.ricardo.dto.RegisterRequest;
import com.ricardo.model.User;
import com.ricardo.repository.UserRepository;
import com.ricardo.util.JwtUtil;
import com.ricardo.util.PasswordUtil;

import java.sql.SQLException;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    public AuthResponse register(RegisterRequest req) {
        try {
            // 1. Check if username is already taken
            User existing = userRepository.findByUserName(req.getUsername());
            if (existing != null) {
                throw new RuntimeException("USERNAME_TAKEN");
            }

            // 2. Hash the password - Never store plain text
            String passwordHash = PasswordUtil.hash(req.getPassword());

            // 3. Save new user to H2
            long userId = userRepository.createUser(req.getUsername(), passwordHash);

            // 4. Generate both tokens (auto-login or register)
            String accessToken = JwtUtil.generateAccessToken(userId);
            String refreshToken = JwtUtil.generateRefreshToken();

            // 5. Persist refresh token so it can be validated later
            userRepository.saveRefreshToken(userId, refreshToken);

            return new AuthResponse(accessToken, refreshToken);
        } catch (SQLException e) {
            throw new RuntimeException("DB_ERROR: " + e.getMessage());
        }
    }
}
