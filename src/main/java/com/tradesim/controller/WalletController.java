package com.tradesim.controller;

import com.tradesim.model.Wallet;
import com.tradesim.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "http://localhost:5173")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance")
    public Wallet getBalance(@RequestParam String userId) {
        return walletService.getWalletByUserId(userId);
    }
}
