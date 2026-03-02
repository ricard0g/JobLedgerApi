package com.ricardo.repository;

import com.ricardo.config.DatabaseConfig;
import com.ricardo.model.User;

import java.sql.*;

public class UserRepository {

    // Check for duplicate names for example
    public User findByUserName(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return mapRow(rs);
            }

            return null;
        }
    }

    // Returns the created user_id
    public long createUser(String username, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash) VALUES (? , ?)";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            stmt.executeUpdate();

            // H2 gives back the AUTO_INCREMENT id — we need it to generate the JWT
            ResultSet keys = stmt.getGeneratedKeys();
            while (keys.next()) {
                return keys.getLong(1);
            }
            throw new SQLException("Failed to retrieve generated user_id after INSERT");
        }
    }

    // Save refresh token
    public void saveRefreshToken(long userId, String refreshToken) throws SQLException {
        String sql = "UPDATE users SET refresh_token = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, refreshToken);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        }
    }


    // Maps one ResultSet row → one User object
    // Private because only this class ever reads from the users table
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getLong("user_id")); // Use of String inputs for column label should be reserved for queries that use aliases "AS" in SQL
        // statement, Index are more efficient if no aliases are being used. In this case let's leave it as it is just for better understanding of the code
        user.setUserName(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setRefreshToken(rs.getString("refresh_token"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
