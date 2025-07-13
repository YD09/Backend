package com.tradesim.controller;

import com.tradesim.model.User;
import com.tradesim.model.Watchlist;
import com.tradesim.service.UserService;
import com.tradesim.service.WatchlistService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/watchlist")
@CrossOrigin(origins = "*")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Watchlist>> getUserWatchlist(Authentication authentication) {
        User user = userService.getUserByMobile(authentication.getName());
        List<Watchlist> watchlist = watchlistService.getUserWatchlist(user);
        return ResponseEntity.ok(watchlist);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToWatchlist(Authentication authentication, @RequestBody AddWatchlistRequest request) {
        User user = userService.getUserByMobile(authentication.getName());
        Watchlist watchlist = watchlistService.addToWatchlist(user, request.getSymbol(), 
                request.getExchange(), request.getInstrumentType(), request.getDisplayName());
        return ResponseEntity.ok(watchlist);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromWatchlist(Authentication authentication, @RequestBody RemoveWatchlistRequest request) {
        User user = userService.getUserByMobile(authentication.getName());
        watchlistService.removeFromWatchlist(user, request.getSymbol(), request.getExchange());
        return ResponseEntity.ok(Map.of("message", "Removed from watchlist"));
    }

    @Data
    public static class AddWatchlistRequest {
        private String symbol;
        private String exchange;
        private String instrumentType;
        private String displayName;
    }

    @Data
    public static class RemoveWatchlistRequest {
        private String symbol;
        private String exchange;
    }
} 