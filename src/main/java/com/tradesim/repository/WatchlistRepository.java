package com.tradesim.repository;

import com.tradesim.model.Watchlist;
import com.tradesim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUserOrderByAddedAtDesc(User user);
    List<Watchlist> findByUserAndExchange(User user, String exchange);
    boolean existsByUserAndSymbolAndExchange(User user, String symbol, String exchange);
    void deleteByUserAndSymbolAndExchange(User user, String symbol, String exchange);
} 