package com.tradesim.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tradesim.model.UserWallet;
import com.tradesim.repository.WalletRepository;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepo;

    public UserWallet getWallet(UUID userId) {
        return walletRepo.findById(userId).orElse(null);
    }
} 
