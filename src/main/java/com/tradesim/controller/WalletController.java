package com.tradesim.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tradesim.model.UserWallet;
import com.tradesim.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance")
    public ResponseEntity<?> getWallet(@RequestParam UUID userId) {
        UserWallet wallet = walletService.getWallet(userId);
        if (wallet == null) return ResponseEntity.badRequest().body("❌ Wallet not found");
        return ResponseEntity.ok(wallet);
    }
}
