package com.ricardo;

import com.ricardo.config.DatabaseConfig;
import com.ricardo.config.ServerConfig;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        DatabaseConfig.initSchema();    // 1. Connect with DB
        ServerConfig.start();           // 2. Start Http Server

        Runtime.getRuntime().addShutdownHook(
                new Thread(DatabaseConfig::shutdownConnection)  // 3. Cleanup DB Pool on exit
        );
    }
}
