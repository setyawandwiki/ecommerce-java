package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderResponse checkOut(CheckOutRequest request);
    Optional<Order> findOrderById(Long id);
    List<Order> findOrdersByUserId(Long userId);
    Page<OrderResponse> findOrdersByUserIdAndPage(Long userId, Pageable pageable);
    List<Order> findOrderByStatus(OrderStatus status);
    void cancelOrder(Long orderId);
    List<OrderItemResponse> findOrderItemsByOrderId(Long orderId);
    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    Double calculateOrderTotal(Long orderId);
    PaginatedOrderResponse convertOrderPage(Page<OrderResponse> orderResponses);
}
