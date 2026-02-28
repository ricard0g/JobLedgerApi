package com.ricardo.config;

import com.ricardo.handler.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ServerConfig {

    public static void start() throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        registerRoutes(server);

        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();
        System.out.println(" JobLedger Server running on port " + port);
    }

    private static void registerRoutes(HttpServer server) {
        // Auth (public)
        server.createContext("/api/auth/register", new RegisterHandler());
        server.createContext("/api/auth/login", new LoginHandler());
        server.createContext("/api/auth/refresh", new RefreshHandler());
        server.createContext("/api/auth/logout", new LogoutHandler());


        // Applications (protected)
        server.createContext("/api/applications", new ApplicationsHandler());

        // Stats (protected)
        server.createContext("/api/stats", new StatsHandler());
    }


}
