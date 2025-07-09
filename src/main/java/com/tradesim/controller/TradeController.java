// package com.tradesim.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import com.tradesim.model.Trade;
// import com.tradesim.service.TradeService;

// import java.util.List;
// import java.util.UUID;

// @RestController
// @RequestMapping("/api/trade")
// public class TradeController {
//   @Autowired
//   private TradeService tradeService;

//   @PostMapping
//   public ResponseEntity<Trade> placeTrade(@RequestBody Trade trade) {
//     return ResponseEntity.ok(tradeService.executeTrade(trade));
//   }

//   @GetMapping("/wallet")
//   public ResponseEntity<Double> getWallet(@RequestParam UUID userId) {
//     return ResponseEntity.ok(tradeService.getWalletBalance(userId));
//   }

//   @GetMapping("/history")
//   public ResponseEntity<List<Trade>> tradeHistory() {
//     return ResponseEntity.ok(tradeService.getAllTrades());
//   }
// }
package com.tradesim.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tradesim.service.TradeService;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/place")
    public ResponseEntity<String> placeTrade(@RequestParam UUID userId,
                                             @RequestParam String symbol,
                                             @RequestParam double price,
                                             @RequestParam int quantity,
                                             @RequestParam String type) {
        String result = tradeService.placeTrade(userId, symbol, price, quantity, type);
        return ResponseEntity.ok(result);
    }
}
