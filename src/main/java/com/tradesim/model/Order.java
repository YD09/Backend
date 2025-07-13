package com.tradesim.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String exchange;

    @Column(nullable = false)
    private String instrumentType;

    @Enumerated(EnumType.STRING)
    private OrderType type; // MARKET, LIMIT, STOP_LOSS

    @Enumerated(EnumType.STRING)
    private OrderSide side; // BUY, SELL

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, EXECUTED, CANCELLED, REJECTED

    private double price;
    private double triggerPrice; // For stop loss orders
    private int quantity;
    private int executedQuantity;
    private double averagePrice;

    private LocalDateTime orderTime;
    private LocalDateTime executionTime;
    private LocalDateTime cancellationTime;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @ManyToOne
    @JoinColumn(name = "trade_id")
    private Trade trade;

    @ManyToOne
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;

    @PrePersist
    protected void onCreate() {
        if (orderTime == null) {
            orderTime = LocalDateTime.now();
        }
        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }

    public enum OrderType {
        MARKET, LIMIT, STOP_LOSS, STOP_LIMIT
    }

    public enum OrderSide {
        BUY, SELL
    }

    public enum OrderStatus {
        PENDING, EXECUTED, CANCELLED, REJECTED, PARTIALLY_EXECUTED
    }
} 