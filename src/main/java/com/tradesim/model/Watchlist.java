package com.tradesim.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "watchlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Watchlist {
    
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
    
    private String displayName;
    
    private LocalDateTime addedAt;
    
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
} 