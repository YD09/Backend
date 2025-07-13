package com.tradesim.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Base64;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.time.Instant;

@Service
public class FyersService {
    @Value("${fyers.client_id}")
    private String clientId;
    @Value("${fyers.secret_key}")
    private String secretKey;
    @Value("${fyers.redirect_uri}")
    private String redirectUri;
    @Value("${fyers.fy_id}")
    private String fyId;
    @Value("${fyers.totp_key}")
    private String totpKey;
    @Value("${fyers.pin}")
    private String pin;
    @Value("${fyers.access_token:}")
    private String configuredAccessToken;

    private final String BASE_URL = "https://api.fyers.in/api/v2";
    private final RestTemplate restTemplate = new RestTemplate();

    // Remove in-memory token store
    // private String accessToken = null;
    // private Instant tokenExpiry = Instant.EPOCH;

    // Helper method to encode string to base64
    private String getEncodedString(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    // Generate TOTP (placeholder - implement with actual TOTP library)
    private String generateTOTP() {
        if (totpKey == null || totpKey.isEmpty()) {
            throw new RuntimeException("TOTP key not configured. Please set fyers.totp_key in application.properties");
        }
        // For now, return a placeholder - you can implement actual TOTP generation
        // using a library like Google Authenticator or similar
        return "123456"; // Placeholder - replace with actual TOTP generation
    }

    // Enhanced authentication flow similar to Python script
    public String authenticateAndGetAuthCode() {
        try {
            // Step 1: Send login OTP
            String sendOtpUrl = "https://api-t2.fyers.in/vagator/v2/send_login_otp_v2";
            String sendOtpPayload = String.format(
                "{\"fy_id\":\"%s\",\"app_id\":\"2\"}", 
                getEncodedString(fyId)
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(sendOtpPayload, headers);
            
            ResponseEntity<Map> otpResponse = restTemplate.postForEntity(sendOtpUrl, entity, Map.class);
            Map otpBody = otpResponse.getBody();
            
            if (otpBody == null || !"200".equals(otpBody.get("s"))) {
                throw new RuntimeException("Failed to send OTP: " + otpBody);
            }
            
            String requestKey = (String) otpBody.get("request_key");
            
            // Step 2: Verify OTP
            String verifyOtpUrl = "https://api-t2.fyers.in/vagator/v2/verify_otp";
            String totp = generateTOTP();
            String verifyOtpPayload = String.format(
                "{\"request_key\":\"%s\",\"otp\":\"%s\"}", 
                requestKey, totp
            );
            
            entity = new HttpEntity<>(verifyOtpPayload, headers);
            ResponseEntity<Map> verifyResponse = restTemplate.postForEntity(verifyOtpUrl, entity, Map.class);
            Map verifyBody = verifyResponse.getBody();
            
            if (verifyBody == null || !"200".equals(verifyBody.get("s"))) {
                throw new RuntimeException("Failed to verify OTP: " + verifyBody);
            }
            
            String verifyRequestKey = (String) verifyBody.get("request_key");
            
            // Step 3: Verify PIN
            String verifyPinUrl = "https://api-t2.fyers.in/vagator/v2/verify_pin_v2";
            String verifyPinPayload = String.format(
                "{\"request_key\":\"%s\",\"identity_type\":\"pin\",\"identifier\":\"%s\"}", 
                verifyRequestKey, getEncodedString(pin)
            );
            
            entity = new HttpEntity<>(verifyPinPayload, headers);
            ResponseEntity<Map> pinResponse = restTemplate.postForEntity(verifyPinUrl, entity, Map.class);
            Map pinBody = pinResponse.getBody();
            
            if (pinBody == null || !"200".equals(pinBody.get("s"))) {
                throw new RuntimeException("Failed to verify PIN: " + pinBody);
            }
            
            String sessionToken = (String) ((Map) pinBody.get("data")).get("access_token");
            
            // Step 4: Get auth code
            String tokenUrl = "https://api-t1.fyers.in/api/v3/token";
            String tokenPayload = String.format(
                "{\"fyers_id\":\"%s\",\"app_id\":\"%s\",\"redirect_uri\":\"%s\",\"appType\":\"100\",\"code_challenge\":\"\",\"state\":\"None\",\"scope\":\"\",\"nonce\":\"\",\"response_type\":\"code\",\"create_cookie\":true}",
                fyId, clientId.substring(0, clientId.length() - 4), redirectUri
            );
            
            headers.set("Authorization", "Bearer " + sessionToken);
            entity = new HttpEntity<>(tokenPayload, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, entity, Map.class);
            Map tokenBody = tokenResponse.getBody();
            
            if (tokenBody == null || !"200".equals(tokenBody.get("s"))) {
                throw new RuntimeException("Failed to get auth URL: " + tokenBody);
            }
            
            String authUrl = (String) tokenBody.get("Url");
            // Extract auth_code from URL
            String authCode = extractAuthCodeFromUrl(authUrl);
            
            return authCode;
            
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }
    
    private String extractAuthCodeFromUrl(String url) {
        // Simple extraction - you might want to use a proper URL parser
        if (url.contains("auth_code=")) {
            String[] parts = url.split("auth_code=");
            if (parts.length > 1) {
                String authCode = parts[1];
                if (authCode.contains("&")) {
                    authCode = authCode.split("&")[0];
                }
                return authCode;
            }
        }
        throw new RuntimeException("Could not extract auth code from URL: " + url);
    }

    // Exchange auth code for access token
    public synchronized void refreshAccessToken(String authCode) {
        try {
            String url = "https://api.fyers.in/api/v3/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String payload = String.format(
                "{\"grant_type\":\"authorization_code\",\"appIdHash\":\"%s\",\"code\":\"%s\",\"secretKey\":\"%s\",\"redirect_uri\":\"%s\"}",
                clientId, authCode, secretKey, redirectUri);
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            Map body = response.getBody();
            if (body != null && body.get("access_token") != null) {
                configuredAccessToken = (String) body.get("access_token");
                // Set expiry to 12 hours from now (simulate, adjust as per actual API)
                // tokenExpiry = Instant.now().plusSeconds(12 * 60 * 60); // This line is removed
            } else {
                throw new RuntimeException("Failed to get access token: " + body);
            }
        } catch (Exception e) {
            throw new RuntimeException("Token refresh failed", e);
        }
    }

    // Ensure valid token
    private void ensureToken() {
        if ((configuredAccessToken == null || configuredAccessToken.isEmpty())) {
            throw new RuntimeException("Access token not available. Please set fyers.access_token in application.properties");
        }
    }

    // Generic GET with token
    private Object getWithToken(String url) {
        ensureToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + configuredAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        return response.getBody();
    }

    // Generic quote fetcher for any symbol
    public Object getQuote(String symbol) {
        String url = "https://api.fyers.in/api/v3/quotes";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + configuredAccessToken);
        Map<String, Object> payload = Map.of("symbols", symbol);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        return response.getBody();
    }

    // Fix Nifty endpoint to use correct symbol
    public Object getNiftyIndex() {
        return getQuote("NSE:NIFTY50-INDEX");
    }
    public Object getBankNiftyIndex() {
        return getWithToken(BASE_URL + "/data-rest/v2/quotes?symbols=NSE:BANKNIFTY-INDEX");
    }
    public Object getSensexIndex() {
        return getWithToken(BASE_URL + "/data-rest/v2/quotes?symbols=BSE:SENSEX-INDEX");
    }

    // Option Chains
    public Object getNiftyOptionChain() {
        return getWithToken(BASE_URL + "/data-rest/v2/options-chain?symbol=NSE:NIFTY50-INDEX");
    }
    public Object getBankNiftyOptionChain() {
        return getWithToken(BASE_URL + "/data-rest/v2/options-chain?symbol=NSE:BANKNIFTY-INDEX");
    }
    public Object getSensexOptionChain() {
        return getWithToken(BASE_URL + "/data-rest/v2/options-chain?symbol=BSE:SENSEX-INDEX");
    }

    // Historical candles for NIFTY (default: 5min interval, last 1 day)
    public Object getNiftyHistory() {
        String url = BASE_URL + "/data-rest/v2/history?symbol=NSE:NIFTY50-INDEX&resolution=5&date_format=1&range_from=2024-01-01&range_to=2024-12-31&cont_flag=1";
        return getWithToken(url);
    }
    // Historical candles for BANKNIFTY
    public Object getBankNiftyHistory() {
        String url = BASE_URL + "/data-rest/v2/history?symbol=NSE:BANKNIFTY-INDEX&resolution=5&date_format=1&range_from=2024-01-01&range_to=2024-12-31&cont_flag=1";
        return getWithToken(url);
    }

    public String getLoginUrl() {
        return "https://api.fyers.in/api/v3/generate-authcode?client_id=" + clientId +
                "&redirect_uri=" + redirectUri + "&response_type=code&state=none";
    }

    public String exchangeForAccessToken(String code) {
        try {
            String url = "https://api.fyers.in/api/v3/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Debug logging
            System.out.println("FYERS Token Exchange Debug:");
            System.out.println("URL: " + url);
            System.out.println("Client ID: " + clientId);
            System.out.println("Secret Key: " + secretKey);
            System.out.println("Redirect URI: " + redirectUri);
            System.out.println("Auth Code: " + code.substring(0, Math.min(50, code.length())) + "...");
            
            String payload = String.format(
                "{\"grant_type\":\"authorization_code\",\"appIdHash\":\"%s\",\"code\":\"%s\",\"secretKey\":\"%s\",\"redirect_uri\":\"%s\"}",
                clientId, code, secretKey, redirectUri);
            
            System.out.println("Payload: " + payload);
            
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            Map body = response.getBody();
            
            System.out.println("FYERS Response: " + body);
            
            if (body != null && body.get("access_token") != null) {
                configuredAccessToken = (String) body.get("access_token");
                // tokenExpiry = Instant.now().plusSeconds(12 * 60 * 60); // This line is removed
                System.out.println("Access token obtained successfully");
                return configuredAccessToken;
            } else {
                throw new RuntimeException("Failed to get access token: " + body);
            }
        } catch (Exception e) {
            System.err.println("Token exchange error: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getSimpleName());
            e.printStackTrace();
            throw new RuntimeException("Token exchange failed: " + e.getMessage(), e);
        }
    }

    // New method to get user profile
    public Object getUserProfile() {
        return getWithToken(BASE_URL + "/user-profile");
    }

    // New method to get account information
    public Object getAccountInfo() {
        return getWithToken(BASE_URL + "/account-info");
    }

    // New method to get holdings
    public Object getHoldings() {
        return getWithToken(BASE_URL + "/holdings");
    }

    // New method to get positions
    public Object getPositions() {
        return getWithToken(BASE_URL + "/positions");
    }
}
