package com.tradesim.service;

import com.tradesim.model.User;
import com.tradesim.repository.UserRepository;
import com.tradesim.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public User createUser(String mobileNumber, String name, String email) {
        if (userRepository.existsByMobileNumber(mobileNumber)) {
            throw new RuntimeException("User with this mobile number already exists");
        }

        User user = new User();
        user.setMobileNumber(mobileNumber);
        user.setName(name);
        user.setEmail(email);
        user.setLastLoginAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByMobile(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDetails loadUserByUsername(String username) {
        return userDetailsService.loadUserByUsername(username);
    }

    public void updateLastLogin(UUID userId) {
        User user = getUserById(userId);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }
} 