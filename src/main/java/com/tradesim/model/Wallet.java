package com.tradesim.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    private UUID userId;

    private double balance = 1000000.0; // Default ₹10,00,000

    private double totalInvested;
    private double totalPnl;
    private double totalPnlPercentage;

    private double marginUsed;
    private double availableMargin;

    private LocalDateTime lastUpdated;

    public Wallet() {}
    
    public Wallet(UUID userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public double getTotalInvested() {
        return totalInvested;
    }
    
    public void setTotalInvested(double totalInvested) {
        this.totalInvested = totalInvested;
    }
    
    public double getTotalPnl() {
        return totalPnl;
    }
    
    public void setTotalPnl(double totalPnl) {
        this.totalPnl = totalPnl;
    }
    
    public double getTotalPnlPercentage() {
        return totalPnlPercentage;
    }
    
    public void setTotalPnlPercentage(double totalPnlPercentage) {
        this.totalPnlPercentage = totalPnlPercentage;
    }
    
    public double getMarginUsed() {
        return marginUsed;
    }
    
    public void setMarginUsed(double marginUsed) {
        this.marginUsed = marginUsed;
    }
    
    public double getAvailableMargin() {
        return availableMargin;
    }
    
    public void setAvailableMargin(double availableMargin) {
        this.availableMargin = availableMargin;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
