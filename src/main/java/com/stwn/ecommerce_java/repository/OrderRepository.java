package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status);
    @Query(value = """
            SELECT * FROM orders 
            WHERE user_id = :userId
            """, nativeQuery = true)
    Page<Order> findUserIdByPageable(@Param("userId") Long userId, Pageable pageable);
    @Query(value = """
            SELECT * FROM orders
            WHERE user_id = :userId
            AND order_date BETWEEN :startDate 
            AND :endDate
            """, nativeQuery = true)
    List<Order> findByUserIdAndDateRange(@Param("userId") Long userId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    Optional<Order> findByXenditInvoiceId(String xenditInvoiceId);
    List<Order> findByStatusAndOrderDateBefore(OrderStatus orderStatus, LocalDateTime dateTime);
}
