package com.tradesim.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tradesim.service.FyersService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/fyers")
public class FyersController {

    @Autowired
    private FyersService fyersService;

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String loginUrl = fyersService.getLoginUrl();
        System.out.println("Redirecting to Fyers login: " + loginUrl);
        response.sendRedirect(loginUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam(required = false) String code) {
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(" Missing auth code in callback");
        }
        return ResponseEntity.ok(" Auth code received: " + code);
    }

    @PostMapping("/token")
    public ResponseEntity<String> getAccessToken(@RequestParam String code) {
        return ResponseEntity.ok(fyersService.exchangeForAccessToken(code));
    }
}
