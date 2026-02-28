package com.ricardo.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

public class DatabaseConfig {
    // This JdbcConnectionPool.creat() method takes three arguments: URL, User,
    // Password.
    private static final JdbcConnectionPool pool = JdbcConnectionPool.create(
            "jdbc:h2:" + System.getenv("DB_PATH") + ";DB_CLOSE_DELAY=-1",
            "sa",
            "");

    // Method to return a connection from the pool
    public static Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    // Close all unused pool connections
    // Disposes the pool cleanly — closes all physical connections. Called in Main.java when the server stops via a shutdown hoo
    public static void shutdownConnection() {
        pool.dispose();
    }

    // Init Schema
    public static void initSchema() throws SQLException {
        // Get into the Try block with the use of one of the pooled connections. Once the execution is finished this connection is returned to the pool
        try (Connection conn = getConnection()) {
            InputStream is = DatabaseConfig.class.getClassLoader().getResourceAsStream("schema.sql");

            if (is == null) throw new RuntimeException("schema.sql not found!");

            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            conn.createStatement().execute(sql);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read bytes from schema.sql. Error: " + e.getMessage());
        }
    }
}
