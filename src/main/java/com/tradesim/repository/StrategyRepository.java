package com.tradesim.repository;

import com.tradesim.model.Strategy;
import com.tradesim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, UUID> {
    List<Strategy> findByUserOrderByCreatedAtDesc(User user);
    List<Strategy> findByUserAndIsActiveTrue(User user);
    List<Strategy> findByUserAndType(User user, Strategy.StrategyType type);
    List<Strategy> findByUserAndSymbol(User user, String symbol);
} 