package com.ricardo;

import com.ricardo.config.DatabaseConfig;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;

public class App
{
    public static void main( String[] args ) throws IOException
    {
        try {
            DatabaseConfig.initSchema();
        } catch(SQLException e) {
            System.out.println("Error during DB schema initialization. Error: " + e.getMessage());
        }

//        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
//
//        // First route
//        server.createContext("/hello", exchange -> {
//            byte[] response = "Hello Brotha\n".getBytes(StandardCharsets.UTF_8);
//            exchange.sendResponseHeaders(200, response.length);
//            try (OutputStream os = exchange.getResponseBody()) {
//                os.write(response);
//            }
//        });
//
//        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
//        server.start();
//        System.out.println("Server is running on port 8080");

    }
}
