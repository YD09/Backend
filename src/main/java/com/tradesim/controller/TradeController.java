package com.tradesim.controller;

import com.tradesim.model.Trade;
import com.tradesim.model.User;
import com.tradesim.service.TradeService;
import com.tradesim.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trade")
@CrossOrigin(origins = "*")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserService userService;

    @PostMapping("/place")
    public ResponseEntity<?> placeTrade(Authentication authentication, @RequestBody PlaceTradeRequest request) {
        User user = userService.getUserByMobile(authentication.getName());
        Trade trade = tradeService.placeTrade(user, request.getSymbol(), request.getExchange(), 
                request.getInstrumentType(), request.getQuantity(), request.getType(), request.getPrice());
        return ResponseEntity.ok(trade);
    }

    @PostMapping("/close")
    public ResponseEntity<?> closeTrade(Authentication authentication, @RequestBody CloseTradeRequest request) {
        User user = userService.getUserByMobile(authentication.getName());
        Trade trade = tradeService.closeTrade(user, request.getTradeId(), request.getExitPrice());
        return ResponseEntity.ok(trade);
    }

    @GetMapping("/open")
    public ResponseEntity<List<Trade>> getOpenTrades(Authentication authentication) {
        User user = userService.getUserByMobile(authentication.getName());
        List<Trade> trades = tradeService.getOpenTrades(user);
        return ResponseEntity.ok(trades);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Trade>> getTradeHistory(Authentication authentication, 
            @RequestParam(required = false) String symbol,
            @RequestParam(required = false) String status) {
        User user = userService.getUserByMobile(authentication.getName());
        List<Trade> trades = tradeService.getTradeHistory(user, symbol, status);
        return ResponseEntity.ok(trades);
    }

    @GetMapping("/pnl")
    public ResponseEntity<Map<String, Object>> getPnlSummary(Authentication authentication) {
        User user = userService.getUserByMobile(authentication.getName());
        Map<String, Object> pnl = tradeService.getPnlSummary(user);
        return ResponseEntity.ok(pnl);
    }

    @Data
    public static class PlaceTradeRequest {
        private String symbol;
        private String exchange;
        private String instrumentType;
        private int quantity;
        private Trade.TradeType type;
        private double price;
    }

    @Data
    public static class CloseTradeRequest {
        private Long tradeId;
        private double exitPrice;
    }
}
