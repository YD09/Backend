package com.tradesim.controller;

import com.tradesim.service.FyersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/fyers")
@CrossOrigin(origins = "*")
public class FyersController {

    @Autowired
    private FyersService fyersService;

    @GetMapping("/login-url")
    public ResponseEntity<Map<String, String>> getLoginUrl() {
        Map<String, String> response = new HashMap<>();
        response.put("loginUrl", fyersService.getLoginUrl());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/exchange-token")
    public ResponseEntity<Map<String, Object>> exchangeToken(@RequestBody Map<String, String> request) {
        try {
            String authCode = request.get("authCode");
            if (authCode == null || authCode.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Auth code is required"));
            }
            
            String accessToken = fyersService.exchangeForAccessToken(authCode);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "accessToken", accessToken,
                "message", "Token exchanged successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Token exchange failed: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/indices/nifty")
    public ResponseEntity<Object> getNiftyIndex() {
        try {
            return ResponseEntity.ok(fyersService.getNiftyIndex());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/indices/banknifty")
    public ResponseEntity<Object> getBankNiftyIndex() {
        try {
            return ResponseEntity.ok(fyersService.getBankNiftyIndex());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/indices/sensex")
    public ResponseEntity<Object> getSensexIndex() {
        try {
            return ResponseEntity.ok(fyersService.getSensexIndex());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/option-chains/nifty")
    public ResponseEntity<Object> getNiftyOptionChain() {
        try {
            return ResponseEntity.ok(fyersService.getNiftyOptionChain());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/option-chains/banknifty")
    public ResponseEntity<Object> getBankNiftyOptionChain() {
        try {
            return ResponseEntity.ok(fyersService.getBankNiftyOptionChain());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/option-chains/sensex")
    public ResponseEntity<Object> getSensexOptionChain() {
        try {
            return ResponseEntity.ok(fyersService.getSensexOptionChain());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/history/nifty")
    public ResponseEntity<Object> getNiftyHistory() {
        try {
            return ResponseEntity.ok(fyersService.getNiftyHistory());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/history/banknifty")
    public ResponseEntity<Object> getBankNiftyHistory() {
        try {
            return ResponseEntity.ok(fyersService.getBankNiftyHistory());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile() {
        try {
            return ResponseEntity.ok(fyersService.getUserProfile());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/account")
    public ResponseEntity<Object> getAccountInfo() {
        try {
            return ResponseEntity.ok(fyersService.getAccountInfo());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/holdings")
    public ResponseEntity<Object> getHoldings() {
        try {
            return ResponseEntity.ok(fyersService.getHoldings());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/positions")
    public ResponseEntity<Object> getPositions() {
        try {
            return ResponseEntity.ok(fyersService.getPositions());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // New endpoint to get comprehensive market data
    @GetMapping("/market-data")
    public ResponseEntity<Map<String, Object>> getMarketData() {
        try {
            Map<String, Object> marketData = new HashMap<>();
            
            // Get all indices
            marketData.put("nifty", fyersService.getNiftyIndex());
            marketData.put("banknifty", fyersService.getBankNiftyIndex());
            marketData.put("sensex", fyersService.getSensexIndex());
            
            // Get option chains
            marketData.put("niftyOptionChain", fyersService.getNiftyOptionChain());
            marketData.put("bankniftyOptionChain", fyersService.getBankNiftyOptionChain());
            
            // Get historical data
            marketData.put("niftyHistory", fyersService.getNiftyHistory());
            marketData.put("bankniftyHistory", fyersService.getBankNiftyHistory());
            
            return ResponseEntity.ok(marketData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // New endpoint to get account overview
    @GetMapping("/account-overview")
    public ResponseEntity<Map<String, Object>> getAccountOverview() {
        try {
            Map<String, Object> accountData = new HashMap<>();
            
            accountData.put("profile", fyersService.getUserProfile());
            accountData.put("accountInfo", fyersService.getAccountInfo());
            accountData.put("holdings", fyersService.getHoldings());
            accountData.put("positions", fyersService.getPositions());
            
            return ResponseEntity.ok(accountData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/quotes/{symbol}")
    public ResponseEntity<Object> getQuote(@PathVariable String symbol) {
        try {
            return ResponseEntity.ok(fyersService.getQuote(symbol));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
