// package com.tradesim.service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.UUID;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.tradesim.model.Trade;
// import com.tradesim.model.UserWallet;
// import com.tradesim.repository.TradeRepository;
// import com.tradesim.repository.WalletRepository;

// @Service
// public class TradeService {
//   @Autowired
//   private TradeRepository tradeRepo;

//   @Autowired
//   private WalletRepository walletRepo;

//   public Trade executeTrade(Trade trade) {
//     UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000001");
//     UserWallet wallet = walletRepo.findById(userId).orElseGet(() -> {
//       UserWallet w = new UserWallet();
//       w.setUserId(userId);
//       return walletRepo.save(w);
//     });

//     double cost = trade.getPrice() * trade.getQuantity();
//     if (trade.getType().equalsIgnoreCase("BUY")) {
//       wallet.setBalance(wallet.getBalance() - cost);
//     } else {
//       wallet.setBalance(wallet.getBalance() + cost);
//     }

//     trade.setTimestamp(LocalDateTime.now());
//     trade.setPnl(0.0); // future logic can update this

//     walletRepo.save(wallet);
//     return tradeRepo.save(trade);
//   }

//   public double getWalletBalance(UUID userId) {
//     return walletRepo.findById(userId).map(UserWallet::getBalance).orElse(0.0);
//   }

//   public List<Trade> getAllTrades() {
//     return tradeRepo.findAll();
//   }
// }
package com.tradesim.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tradesim.model.Trade;
import com.tradesim.model.UserWallet;
import com.tradesim.repository.TradeRepository;
import com.tradesim.repository.WalletRepository;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepo;

    @Autowired
    private WalletRepository walletRepo;

    public String placeTrade(UUID userId, String symbol, double price, int quantity, String type) {
        UserWallet wallet = walletRepo.findById(userId).orElse(new UserWallet());
        wallet.setUserId(userId);
        double amount = price * quantity;

        if (type.equalsIgnoreCase("BUY") && wallet.getBalance() < amount) {
            return " Insufficient balance";
        }

        double pnl = 0;
        if (type.equalsIgnoreCase("SELL")) {
            // In real logic: calculate PnL using average buy price
            pnl = amount * 0.05; // fake 5% profit for now
        }

        if (type.equalsIgnoreCase("BUY")) {
            wallet.setBalance(wallet.getBalance() - amount);
        } else {
            wallet.setBalance(wallet.getBalance() + amount + pnl);
        }

        Trade trade = new Trade(symbol, price, quantity, type.toUpperCase(), LocalDateTime.now(), pnl);
        tradeRepo.save(trade);
        walletRepo.save(wallet);

        return " Trade Executed: " + type + " " + symbol + " x" + quantity;
    }
}