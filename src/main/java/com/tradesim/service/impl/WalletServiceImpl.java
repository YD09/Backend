package com.tradesim.service.impl;

import com.tradesim.model.Wallet;
import com.tradesim.repository.WalletRepository;
import com.tradesim.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getWalletByUserId(String userId) {
        UUID uuid = UUID.fromString(userId);
        Optional<Wallet> walletOpt = walletRepository.findByUserId(uuid);

        return walletOpt.orElseGet(() -> {
            Wallet wallet = new Wallet(uuid, 100000.0); // Default ₹1L if not exists
            return walletRepository.save(wallet);
        });
    }

    @Override
    public Wallet updateWalletBalance(String userId, double newBalance) {
        Wallet wallet = getWalletByUserId(userId);
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> findByUserId(UUID userId) {
        return walletRepository.findByUserId(userId);
    }
} 