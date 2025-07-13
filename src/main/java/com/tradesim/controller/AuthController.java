package com.tradesim.controller;

import com.tradesim.model.User;
import com.tradesim.security.JwtTokenUtil;
import com.tradesim.service.AuthService;
import com.tradesim.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        try {
            authService.sendOtp(request.getMobileNumber(), request.getEmail());
            return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        try {
            User user = authService.verifyOtp(request.getMobileNumber(), request.getOtp());
            
            // Generate JWT token
            UserDetails userDetails = userService.loadUserByUsername(user.getMobileNumber());
            String token = jwtTokenUtil.generateToken(userDetails);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            response.put("message", "Login successful");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.createUser(request.getMobileNumber(), request.getName(), request.getEmail());
            return ResponseEntity.ok(Map.of("message", "User registered successfully", "user", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Data
    public static class SendOtpRequest {
        private String mobileNumber;
        private String email;
    }

    @Data
    public static class VerifyOtpRequest {
        private String mobileNumber;
        private String otp;
    }

    @Data
    public static class RegisterRequest {
        private String mobileNumber;
        private String name;
        private String email;
    }
} 