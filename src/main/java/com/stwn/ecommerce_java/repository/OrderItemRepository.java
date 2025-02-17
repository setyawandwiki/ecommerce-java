package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.entity.OrderItem;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
    @Query(value = """
            SELECT oi.* FROM order_items oi
            JOIN orders o 
            ON o.order_id = oi.order_id
            WHERE o.user_id = :userId
            AND oi.product_id = :productId
            """, nativeQuery = true)
    List<OrderItem> findByUserAndProduct(@Param("userId") Long userId);
    @Query(value = """
            SELECT SUM(quantity * price) FROM order_items
            WHERE order_id = :orderId
            """, nativeQuery = true)
    Double calculateTotalOrder(@Param("orderId") Long orderId);
}
