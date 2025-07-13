package com.tradesim.service;

import com.tradesim.model.User;
import com.tradesim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // In-memory storage for OTP (in production, use Redis or database)
    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, Long> otpTimestamp = new HashMap<>();
    // Store email for OTP requests
    private final Map<String, String> otpEmailStorage = new HashMap<>();

    // Accept email in sendOtp
    public void sendOtp(String mobileNumber, String email) {
        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(1000000));
        
        // Store OTP with timestamp
        otpStorage.put(mobileNumber, otp);
        otpTimestamp.put(mobileNumber, System.currentTimeMillis());
        if (email != null && !email.isEmpty()) {
            otpEmailStorage.put(mobileNumber, email);
            sendOtpEmail(email, otp);
        } else {
            // Try to send to existing user's email if available
            userRepository.findByMobileNumber(mobileNumber).ifPresent(user -> {
                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    sendOtpEmail(user.getEmail(), otp);
                }
            });
        }
        // In production, integrate with SMS service like Twilio, MSG91, etc.
        System.out.println("OTP for " + mobileNumber + ": " + otp);
    }

    private void sendOtpEmail(String to, String otp) {
        // Temporarily disabled email sending
        System.out.println("OTP Email would be sent to " + to + ": " + otp);
    }

    public User verifyOtp(String mobileNumber, String otp) {
        String storedOtp = otpStorage.get(mobileNumber);
        Long timestamp = otpTimestamp.get(mobileNumber);
        
        if (storedOtp == null || timestamp == null) {
            throw new RuntimeException("OTP not found or expired");
        }
        
        // Check if OTP is expired (5 minutes)
        if (System.currentTimeMillis() - timestamp > 5 * 60 * 1000) {
            otpStorage.remove(mobileNumber);
            otpTimestamp.remove(mobileNumber);
            otpEmailStorage.remove(mobileNumber);
            throw new RuntimeException("OTP expired");
        }
        
        if (!storedOtp.equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        
        // Clear OTP after successful verification
        otpStorage.remove(mobileNumber);
        otpTimestamp.remove(mobileNumber);
        
        // Get or create user, use stored email if new
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setMobileNumber(mobileNumber);
                    String email = otpEmailStorage.get(mobileNumber);
                    if (email != null) newUser.setEmail(email);
                    newUser.setName("User " + mobileNumber.substring(mobileNumber.length() - 4));
                    otpEmailStorage.remove(mobileNumber);
                    return userRepository.save(newUser);
                });
    }
} 