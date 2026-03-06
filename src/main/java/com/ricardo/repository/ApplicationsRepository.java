package com.ricardo.repository;

import com.ricardo.config.DatabaseConfig;
import com.ricardo.model.Application;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ApplicationsRepository extends Mapper<Application> {

    public long createApplication(Application application) throws SQLException {
        String sql = "INSERT INTO applications (user_id, company, role, status, salary_min, salary_max, location, is_remote, logo_url, job_url, created_at, " +
                "application_note, applied_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("\nInside Repository, SQL statement Ready!\n");

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, application.getUserId());
            stmt.setString(2, application.getCompany());
            stmt.setString(3, application.getRole());
            stmt.setString(4, application.getStatus());
            stmt.setFloat(5, application.getSalary_min());
            stmt.setFloat(6, application.getSalary_max());
            stmt.setString(7, application.getLocation());
            stmt.setBoolean(8, application.isRemote());
            stmt.setString(9, application.getLogoUrl());
            stmt.setString(10, application.getJobUrl());

            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

            stmt.setTimestamp(11, timestamp);
            stmt.setString(12, application.getApplicationNote());
            stmt.setDate(13, java.sql.Date.valueOf(application.getAppliedDate()));

            long applicationId = stmt.executeUpdate();

            System.out.println("\nThis is the application ID we just created -> " + applicationId);

            return applicationId;
        }
    }

    public Application findById(long id) throws SQLException {
        String sql = "SELECT * FROM applications WHERE application_id = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                return mapRow(rs);
            }

           return null;
        }
    }

    @Override
    protected Application mapRow(ResultSet rs) throws SQLException {
        Application application = new Application();
        application.setApplicationId(rs.getLong("application_id"));
        application.setUserId(rs.getLong("user_id"));
        application.setCompany(rs.getString("company"));
        application.setRole(rs.getString("role"));
        application.setStatus(rs.getString("status"));
        application.setSalary_min(rs.getFloat("salary_min"));
        application.setSalary_max(rs.getFloat("salary_max"));
        application.setLocation(rs.getString("location"));
        application.setRemote(rs.getBoolean("is_remote"));
        application.setLogoUrl(rs.getString("logo_url"));
        application.setJobUrl(rs.getString("job_url"));
        application.setCreatedAt(rs.getTimestamp("created_at"));
        application.setApplicationNote(rs.getString("application_note"));
        LocalDate appliedDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        application.setAppliedDate(appliedDate);

        return application;
    }
}
