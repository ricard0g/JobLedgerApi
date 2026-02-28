package com.ricardo.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ApplicationsHandler implements HttpHandler {
    public void handle(HttpExchange exchange) {
        System.out.println("Hello from Appliction Handler!");
    }
}
