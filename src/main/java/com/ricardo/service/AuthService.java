package com.ricardo.service;

import com.ricardo.dto.AuthResponse;
import com.ricardo.dto.LoginRequest;
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

    public AuthResponse login(LoginRequest req) {
        try {
            System.out.println("Login Process Started!");
            // 1. Lookup fot the user - same username check as register, different outcome
            User user = userRepository.findByUserName(req.getUsername());

            if (user == null) {
                throw new RuntimeException("INVALID_CREDENTIALS");
            }

            System.out.println("User Fetched! Username --> " + user.getUserName() + " | Password Hash --> " + user.getPasswordHash());
            // 2. Verify the password against the stored BCrypt hash
            if (!PasswordUtil.verify(req.getPassword(), user.getPasswordHash())) {
                throw new RuntimeException("INVALID_CREDENTIALS");
            }

            // 3. Both checks passed - generate fresh tokens
            String accessToken = JwtUtil.generateAccessToken(user.getUserId());
            String refreshToken = JwtUtil.generateRefreshToken();

            System.out.println("Generated Access and Refresh Token:");
            System.out.println(accessToken);
            System.out.println(refreshToken);

            // 4. Persist Refresh token in DB
            userRepository.saveRefreshToken(user.getUserId(), refreshToken);


            System.out.println("Successfully Saved refresh token in DB");

            return new AuthResponse(accessToken, refreshToken);
        } catch (SQLException e) {
            throw new RuntimeException("DB_ERROR: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
