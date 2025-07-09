package com.tradesim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tradesim.model.Trade;
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findBySymbol(String symbol);
}