package com.tradesim.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String exchange; // NSE, BSE, NFO

    @Column(nullable = false)
    private String instrumentType; // EQ, OPT, FUT

    private double entryPrice;
    private double exitPrice;
    private int quantity;
    
    @Enumerated(EnumType.STRING)
    private TradeType type; // BUY, SELL

    @Enumerated(EnumType.STRING)
    private TradeStatus status; // OPEN, CLOSED, CANCELLED

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double pnl;
    private double pnlPercentage;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;

    public Trade() {}
    
    public Trade(String symbol, double price, int quantity, String type, LocalDateTime timestamp, double pnl) {
        this.symbol = symbol;
        this.entryPrice = price;
        this.quantity = quantity;
        this.type = TradeType.valueOf(type);
        this.entryTime = timestamp;
        this.pnl = pnl;
    }

    @PrePersist
    protected void onCreate() {
        if (entryTime == null) {
            entryTime = LocalDateTime.now();
        }
        if (status == null) {
            status = TradeStatus.OPEN;
        }
    }

    public enum TradeType {
        BUY, SELL
    }

    public enum TradeStatus {
        OPEN, CLOSED, CANCELLED
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getExchange() {
        return exchange;
    }
    
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    
    public String getInstrumentType() {
        return instrumentType;
    }
    
    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }
    
    public double getEntryPrice() {
        return entryPrice;
    }
    
    public void setEntryPrice(double entryPrice) {
        this.entryPrice = entryPrice;
    }
    
    public double getExitPrice() {
        return exitPrice;
    }
    
    public void setExitPrice(double exitPrice) {
        this.exitPrice = exitPrice;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public TradeType getType() {
        return type;
    }
    
    public void setType(TradeType type) {
        this.type = type;
    }
    
    public TradeStatus getStatus() {
        return status;
    }
    
    public void setStatus(TradeStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    
    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
    
    public LocalDateTime getExitTime() {
        return exitTime;
    }
    
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
    
    public double getPnl() {
        return pnl;
    }
    
    public void setPnl(double pnl) {
        this.pnl = pnl;
    }
    
    public double getPnlPercentage() {
        return pnlPercentage;
    }
    
    public void setPnlPercentage(double pnlPercentage) {
        this.pnlPercentage = pnlPercentage;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Strategy getStrategy() {
        return strategy;
    }
    
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}

