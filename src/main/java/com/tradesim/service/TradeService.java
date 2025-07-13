
package com.tradesim.service;

import com.tradesim.model.Trade;
import com.tradesim.model.User;
import com.tradesim.model.Wallet;
import com.tradesim.repository.TradeRepository;
import com.tradesim.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    public Trade placeTrade(User user, String symbol, String exchange, String instrumentType, 
                          int quantity, Trade.TradeType type, double price) {
        
        // Get or create wallet
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setUserId(user.getId());
                    return walletRepository.save(newWallet);
                });

        // Check if user has sufficient balance
        double tradeValue = price * quantity;
        if (type == Trade.TradeType.BUY && wallet.getBalance() < tradeValue) {
            throw new RuntimeException("Insufficient balance");
        }

        // Create trade
        Trade trade = new Trade();
        trade.setUser(user);
        trade.setSymbol(symbol);
        trade.setExchange(exchange);
        trade.setInstrumentType(instrumentType);
        trade.setQuantity(quantity);
        trade.setType(type);
        trade.setEntryPrice(price);
        trade.setStatus(Trade.TradeStatus.OPEN);

        // Update wallet
        if (type == Trade.TradeType.BUY) {
            wallet.setBalance(wallet.getBalance() - tradeValue);
        } else {
            wallet.setBalance(wallet.getBalance() + tradeValue);
        }
        walletRepository.save(wallet);

        return tradeRepository.save(trade);
    }

    public Trade closeTrade(User user, Long tradeId, double exitPrice) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("Trade not found"));

        if (!trade.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (trade.getStatus() != Trade.TradeStatus.OPEN) {
            throw new RuntimeException("Trade is already closed");
        }

        // Calculate P&L
        double pnl = calculatePnl(trade, exitPrice);
        double pnlPercentage = (pnl / (trade.getEntryPrice() * trade.getQuantity())) * 100;

        // Update trade
        trade.setExitPrice(exitPrice);
        trade.setExitTime(LocalDateTime.now());
        trade.setPnl(pnl);
        trade.setPnlPercentage(pnlPercentage);
        trade.setStatus(Trade.TradeStatus.CLOSED);

        // Update wallet
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (trade.getType() == Trade.TradeType.BUY) {
            wallet.setBalance(wallet.getBalance() + (exitPrice * trade.getQuantity()));
        } else {
            wallet.setBalance(wallet.getBalance() - (exitPrice * trade.getQuantity()));
        }
        walletRepository.save(wallet);

        return tradeRepository.save(trade);
    }

    public List<Trade> getOpenTrades(User user) {
        return tradeRepository.findByUserAndStatus(user, Trade.TradeStatus.OPEN);
    }

    public List<Trade> getTradeHistory(User user, String symbol, String status) {
        if (symbol != null && status != null) {
            return tradeRepository.findByUserAndSymbol(user, symbol);
        } else if (symbol != null) {
            return tradeRepository.findByUserAndSymbol(user, symbol);
        } else if (status != null) {
            return tradeRepository.findByUserAndStatus(user, Trade.TradeStatus.valueOf(status));
        } else {
            return tradeRepository.findByUserOrderByEntryTimeDesc(user);
        }
    }

    public Map<String, Object> getPnlSummary(User user) {
        List<Trade> closedTrades = tradeRepository.findByUserAndStatus(user, Trade.TradeStatus.CLOSED);
        List<Trade> openTrades = tradeRepository.findByUserAndStatus(user, Trade.TradeStatus.OPEN);

        double totalPnl = closedTrades.stream().mapToDouble(Trade::getPnl).sum();
        double unrealizedPnl = openTrades.stream().mapToDouble(trade -> 
            calculatePnl(trade, trade.getEntryPrice())).sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalPnl", totalPnl);
        summary.put("unrealizedPnl", unrealizedPnl);
        summary.put("totalTrades", closedTrades.size() + openTrades.size());
        summary.put("closedTrades", closedTrades.size());
        summary.put("openTrades", openTrades.size());

        return summary;
    }

    private double calculatePnl(Trade trade, double exitPrice) {
        if (trade.getType() == Trade.TradeType.BUY) {
            return (exitPrice - trade.getEntryPrice()) * trade.getQuantity();
        } else {
            return (trade.getEntryPrice() - exitPrice) * trade.getQuantity();
        }
    }
}
