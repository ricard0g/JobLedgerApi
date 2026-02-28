package com.ricardo.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LoginHandler implements HttpHandler {

    public void handle(HttpExchange exchange) {
        System.out.println("Hello from Login Handler!");
    }
}
