package com.tradesim.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "strategies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Strategy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    private String exchange;
    
    @Enumerated(EnumType.STRING)
    private StrategyType type;
    
    @Column(columnDefinition = "TEXT")
    private String conditions; // JSON string of strategy conditions
    
    @Column(columnDefinition = "TEXT")
    private String backtestResults; // JSON string of backtest results
    
    private boolean isActive = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime lastExecutedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum StrategyType {
        BREAKOUT, MEAN_REVERSION, MOMENTUM, OPTIONS_SPREAD, CUSTOM
    }
} 