package com.ricardo.handler;

import com.ricardo.handler.base.AuthenticatedHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Claims;

import java.io.IOException;

public class StatsHandler extends AuthenticatedHandler {
    @Override
    protected void handleAuthenticated(HttpExchange exchange, Claims claims) throws IOException {
        String path = exchange.getRequestURI().getPath();
    }
}
