package com.tradesim.service;

import com.tradesim.model.User;
import com.tradesim.model.Watchlist;
import com.tradesim.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    public List<Watchlist> getUserWatchlist(User user) {
        return watchlistRepository.findByUserOrderByAddedAtDesc(user);
    }

    public Watchlist addToWatchlist(User user, String symbol, String exchange, String instrumentType, String displayName) {
        if (watchlistRepository.existsByUserAndSymbolAndExchange(user, symbol, exchange)) {
            throw new RuntimeException("Symbol already in watchlist");
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setSymbol(symbol);
        watchlist.setExchange(exchange);
        watchlist.setInstrumentType(instrumentType);
        watchlist.setDisplayName(displayName != null ? displayName : symbol);

        return watchlistRepository.save(watchlist);
    }

    public void removeFromWatchlist(User user, String symbol, String exchange) {
        watchlistRepository.deleteByUserAndSymbolAndExchange(user, symbol, exchange);
    }

    public List<Watchlist> getWatchlistByExchange(User user, String exchange) {
        return watchlistRepository.findByUserAndExchange(user, exchange);
    }
} 