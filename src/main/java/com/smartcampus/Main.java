package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.smartcampus.api.SmartCampusApplication;
import java.net.URI;

public class Main {
    
    public static void main(String[] args) throws Exception {
        String baseUri = "http://localhost:8080/";
        
        try {
            ResourceConfig config = ResourceConfig.forApplication(new SmartCampusApplication());
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(baseUri), config);
            
            System.out.println("========================================");
            System.out.println("Smart Campus API Server Started");
            System.out.println("========================================");
            System.out.println("Base URL: " + baseUri);
            System.out.println("API URL: " + baseUri + "api/v1");
            System.out.println("Discovery: " + baseUri + "api/v1/");
            System.out.println("========================================");
            
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}