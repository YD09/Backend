package com.tradesim.repository;

import com.tradesim.model.Order;
import com.tradesim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderTimeDesc(User user);
    List<Order> findByUserAndStatus(User user, Order.OrderStatus status);
    List<Order> findByUserAndOrderTimeBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Order> findByUserAndSymbol(User user, String symbol);
    List<Order> findByUserAndStatusIn(User user, List<Order.OrderStatus> statuses);
} 