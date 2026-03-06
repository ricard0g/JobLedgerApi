package com.ricardo.service;

import com.ricardo.dto.ApplicationResponse;
import com.ricardo.dto.CreateApplicationRequest;
import com.ricardo.model.Application;
import com.ricardo.repository.ApplicationsRepository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ApplicationsService {
    public final ApplicationsRepository applicationsRepository = new ApplicationsRepository();

    public ApplicationsService() {
    }

    ;

    public long createApplication(long userId, CreateApplicationRequest req) throws SQLException {
        Application application = toApplication(req, userId);
        System.out.println("\nInside Application serice this is what Application looks like: ");
        System.out.println("Company -> " + application.getCompany());
        System.out.println("Role -> " + application.getRole());
        System.out.println("And more things...\n");
        return applicationsRepository.createApplication(application);
    }

    public ApplicationResponse findApplication(long id) throws SQLException {
        Application application = applicationsRepository.findById(id);

        if (application == null) {
            throw new RuntimeException("Job Application Not Found!");
        }

        return new ApplicationResponse(application.getApplicationId(), application.getUserId(), application.getCompany(), application.getRole(),
                application.getStatus(), application.getSalary_min(), application.getSalary_max(), application.getLocation(), application.isRemote(),
                application.getLogoUrl(), application.getJobUrl(), application.getCreatedAt(), application.getApplicationNote(), application.getAppliedDate());
    }

    private Application toApplication(CreateApplicationRequest req, long userId) {
        Application application = new Application();

        application.setUserId(userId);
        application.setCompany(req.getCompany());
        application.setRole(req.getRole());
        application.setStatus(req.getStatus());
        application.setSalary_min(req.getSalary_min());
        application.setSalary_max(req.getSalary_max());
        application.setLocation(req.getLocation());
        application.setRemote(req.isRemote());
        application.setLogoUrl(req.getLogoUrl());
        application.setJobUrl(req.getJobUrl());
        application.setApplicationNote(req.getApplicationNote());
        application.setAppliedDate(req.getAppliedDate());

        return application;
    }
}
