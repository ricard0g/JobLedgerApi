package com.ricardo.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Handler {
    // Shared helper all handlers will need to send responses
    protected void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException  {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);// We must send the length in bytes to the client
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}
