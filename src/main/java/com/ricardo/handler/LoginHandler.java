package com.ricardo.handler;

import com.ricardo.dto.AuthResponse;
import com.ricardo.dto.LoginRequest;
import com.ricardo.service.AuthService;
import com.ricardo.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class LoginHandler extends Handler implements HttpHandler {
    private final AuthService authService = new AuthService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "{\"error\":\"Method not allowed. Only POST Reqeust\"}");
        }

        String body = new String(exchange.getRequestBody().readAllBytes());

        if (body.isBlank()) {
            sendResponse(exchange, 400, "{\"error\":\"Request Body is empty\"}");
            return;
        }

        LoginRequest req = JsonUtil.fromJson(body, LoginRequest.class);

        if (req.getUsername() == null || req.getPassword() == null) {
            sendResponse(exchange, 400, "{\"error\":\"username and password are required\"}");
            return;
        }

        try {
            AuthResponse response = authService.login(req);

            System.out.println(" User Successfully logged in, here his credentials:");
            System.out.println("Access Token -> " + response.accessToken());
            System.out.println("Refresh Token -> " + response.refreshToken());

            sendResponse(exchange, 200, JsonUtil.toJson(response));
        } catch (RuntimeException e) {
            if ("INVALID_CREDENTIALS".equals(e.getMessage())) {
                sendResponse(exchange, 401, "{\"error\":\"Invalid username or password\"}");
            } else {
                sendResponse(exchange, 500, "{\"error\":\"Internal Server Error\"}");
            }
        }
    }
}
