package com.tradesim.service;

import com.tradesim.model.Wallet;
import java.util.Optional;
import java.util.UUID;

public interface WalletService {
    
    Wallet getWalletByUserId(String userId);
    
    Wallet updateWalletBalance(String userId, double newBalance);
    
    Optional<Wallet> findByUserId(UUID userId);
}
