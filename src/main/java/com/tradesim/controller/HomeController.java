package com.tradesim.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Fyers Trading Simulator API");
        response.put("status", "running");
        response.put("available_endpoints", Map.of(
            "health_check", "/api/public/health",
            "api_info", "/api/public/info",
            "authentication", "/api/auth/**",
            "trading", "/api/trading/**",
            "wallet", "/api/wallet/**",
            "watchlist", "/api/watchlist/**",
            "websocket", "/ws/**"
        ));
        response.put("documentation", "Use /api/public/info for detailed endpoint information");
        return ResponseEntity.ok(response);
    }
} 