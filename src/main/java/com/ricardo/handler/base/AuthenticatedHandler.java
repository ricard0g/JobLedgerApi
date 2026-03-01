package com.ricardo.handler.base;

import com.ricardo.util.JwtUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AuthenticatedHandler implements HttpHandler {
    @Override
    public final void handle(HttpExchange exchange) throws IOException { // Constant and immutable handle() method that will always be the one that handles
        // incoming protected requests
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            sendResponse(exchange, 401, "{\"error\":\"Missing or invalid Authorization header\"}");
            return;
        }

        String token = authHeader.substring(7);
        Claims claims = JwtUtil.validateToken(token); // returns null if invalid/expired

        if (claims == null) {
            sendResponse(exchange, 401, "{\"error\": \"Invalid or expired token\"}");
            return;
        }

        handleAuthenticated(exchange, claims);
    }

    // Subclasses will implmenet this instead of handle(), handle() will always act as "middleware"
    protected abstract void handleAuthenticated(HttpExchange exchange, Claims claims) throws IOException;

    // Shared helper all handlers will need to send responses
    protected void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);// We must send the length in bytes to the client
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}
