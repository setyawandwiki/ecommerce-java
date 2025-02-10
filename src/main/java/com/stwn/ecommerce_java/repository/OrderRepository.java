package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(String status);

    @Query(value = """
            SELECT * FROM orders
            WHERE user_id = :userId
            AND order_date BETWEEN :startDate 
            AND :endDate
            """, nativeQuery = true)
    List<Order> findByUserIdAndDateRange(@Param("userId") Long userId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
}
