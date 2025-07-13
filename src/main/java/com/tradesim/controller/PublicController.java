package com.tradesim.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Fyers Trading Simulator is running");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Fyers Trading Simulator");
        response.put("version", "1.0.0");
        response.put("description", "A trading simulator application with Fyers integration");
        response.put("endpoints", Map.of(
            "auth", "/api/auth/**",
            "trading", "/api/trading/**",
            "wallet", "/api/wallet/**",
            "watchlist", "/api/watchlist/**",
            "websocket", "/ws/**"
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Backend is running successfully!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("database", "H2 in-memory");
        return ResponseEntity.ok(response);
    }
} 