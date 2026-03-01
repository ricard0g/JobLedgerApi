package com.ricardo.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class LoginHandler extends Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        if (!"POST".equals(exchange.getRequestMethod())) {
            try {
                sendResponse(exchange, 405, "{\"error\":\"Request is not allowed. Only GET Requests\"}");
            } catch (IOException e) {
                System.out.println("IOException during 405 response error. Error: " + e.getMessage());
            }
        }
    }
}
