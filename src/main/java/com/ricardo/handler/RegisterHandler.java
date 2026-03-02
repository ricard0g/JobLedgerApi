package com.ricardo.handler;

import com.ricardo.dto.AuthResponse;
import com.ricardo.dto.RegisterRequest;
import com.ricardo.service.AuthService;
import com.ricardo.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class RegisterHandler extends Handler implements HttpHandler {
    private final AuthService authService = new AuthService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "{\"error\":\"Method not allowed. Only POST Reqeust\"}");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes());

        if (body.isBlank()) {
            sendResponse(exchange, 400, "{\"error\":\"Request Body is empty\"}");
            return;
        }

        RegisterRequest req = JsonUtil.fromJson(body, RegisterRequest.class);

        if (req.getUsername() == null || req.getPassword() == null) {
            sendResponse(exchange, 400, "{\"error\":\"username and password are required\"}");
            return;
        }

        AuthResponse authResponse = authService.register(req);
        sendResponse(exchange, 201, JsonUtil.toJson(authResponse));
    }
}
