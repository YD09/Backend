package com.tradesim.repository;

import com.tradesim.model.Trade;
import com.tradesim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByUserOrderByEntryTimeDesc(User user);
    List<Trade> findByUserAndStatus(User user, Trade.TradeStatus status);
    List<Trade> findByUserAndEntryTimeBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Trade> findByUserAndSymbol(User user, String symbol);
    List<Trade> findByUserAndStatusIn(User user, List<Trade.TradeStatus> statuses);

    // P&L calculations
    @Query("SELECT COALESCE(SUM(t.pnl), 0) FROM Trade t WHERE t.user = :user AND t.status = :status")
    Double sumPnlByUserAndStatus(@Param("user") User user, @Param("status") Trade.TradeStatus status);

    @Query("SELECT COALESCE(SUM(t.pnl), 0) FROM Trade t WHERE t.user = :user AND t.symbol = :symbol")
    Double sumPnlByUserAndSymbol(@Param("user") User user, @Param("symbol") String symbol);
}