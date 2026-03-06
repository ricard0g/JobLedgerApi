package com.ricardo.dto;

import java.sql.Timestamp;
import java.time.LocalDate;

public record ApplicationResponse(
        long applicationId,
        long userId,
        String company,
        String role,
        String status,
        float salary_min,
        float salary_max,
        String location,
        boolean isRemote,
        String logoUrl,
        String jobUrl,
        Timestamp createdAt,
        String applicationNote,
        LocalDate appliedDate
) {
}
