package com.tradesim.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FyersService {
    @Value("${fyers.base_url}")
    private String baseUrl;

    @Value("${fyers.client_id}")
    private String clientId;

    @Value("${fyers.secret_key}")
    private String secretKey;

    @Value("${fyers.redirect_uri}")
    private String redirectUri;

    public String getLoginUrl() {
        return "https://api.fyers.in/api/v3/generate-authcode?client_id=" + clientId +
                "&redirect_uri=" + redirectUri + "&response_type=code&state=none";
    }

    public String exchangeForAccessToken(String code) {
        try {
            URL url = new URL("https://api.fyers.in/api/v3/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String payload = String.format(
                    "{ \"grant_type\": \"authorization_code\", \"appId\": \"%s\", \"code\": \"%s\", \"secretKey\": \"%s\", \"redirect_uri\": \"%s\" }",
                    clientId, code, secretKey, redirectUri);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return in.lines().collect(Collectors.joining());
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"token exchange failed\"}";
        }
    }
}
