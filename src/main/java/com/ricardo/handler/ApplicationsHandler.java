package com.ricardo.handler;

import com.ricardo.dto.ApplicationResponse;
import com.ricardo.dto.CreateApplicationRequest;
import com.ricardo.dto.GetApplicationRequest;
import com.ricardo.handler.base.AuthenticatedHandler;
import com.ricardo.model.Application;
import com.ricardo.service.ApplicationsService;
import com.ricardo.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.sql.SQLException;

public class ApplicationsHandler extends AuthenticatedHandler {
    private final ApplicationsService applicationsService = new ApplicationsService();

    @Override
    protected void handleAuthenticated(HttpExchange exchange, Claims claims) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());

        if (body.isBlank()) {
            sendResponse(exchange, 400, "{\"error\":\"Body cannot be empty!\"}");
        }

        System.out.println("\nGot the Request! This is the body we got:\n");
        System.out.println(body);

        if ("POST".equals(exchange.getRequestMethod())) {
            CreateApplicationRequest req = JsonUtil.fromJson(body, CreateApplicationRequest.class);

            System.out.println("\nWe now have our CreateApplicationRequest Object!");
            System.out.println(req.toString());

            System.out.println("\nCreating the Job Application...\n");
            long newApplicatioId = create(req, claims.get("userId", Long.class));
            sendResponse(exchange, 201, JsonUtil.toJson(newApplicatioId));
        } else if ("GET".equals(exchange.getRequestMethod())) {
            try {
                GetApplicationRequest req = JsonUtil.fromJson(body, GetApplicationRequest.class);

                System.out.println("\nWe now have our GetApplicationRequest Object!");
                System.out.println(req.toString());

                ApplicationResponse applicationResponse = getApplication(req);

                sendResponse(exchange, 200, JsonUtil.toJson(applicationResponse));
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 400, "{\"error\":\"Invalid JSON body\"}");
            }
        }
    }

    private long create(CreateApplicationRequest req, long userId) {
        try {
            System.out.println("\nContacting with AppService...\n");
            return applicationsService.createApplication(userId, req);
        } catch (SQLException e) {
            throw new RuntimeException("Error Creating the Job Application. Error: " + e.getMessage());
        }
    }


    private ApplicationResponse getApplication(GetApplicationRequest req) {
        try {
            System.out.println("Getting the application with ID -> " + req.getApplicationId());
            return applicationsService.findApplication(req.getApplicationId());
        } catch(SQLException e) {
            throw new RuntimeException("Error during retrieval of the Application. Error: " + e.getMessage());
        }
    }

}
