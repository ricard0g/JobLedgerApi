package com.ricardo.dto;

import java.time.LocalDate;

public class CreateApplicationRequest {
    private String company;
    private String role;
    private String status;
    private float salary_min;
    private float salary_max;
    private String location;
    private boolean isRemote;
    private String logoUrl;
    private String jobUrl;
    private String applicationNote;
    private LocalDate appliedDate;

    public String getCompany() {
        return company;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public float getSalary_min() {
        return salary_min;
    }

    public float getSalary_max() {
        return salary_max;
    }

    public String getLocation() {
        return location;
    }

    public boolean isRemote() {
        return isRemote;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public String getApplicationNote() {
        return applicationNote;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }
}
