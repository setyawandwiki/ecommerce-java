package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.model.CheckOutRequest;
import com.stwn.ecommerce_java.model.OrderItemResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order checkOut(CheckOutRequest request);
    Optional<Order> findOrderById(Long id);
    List<Order> findOrdersByUserId(Long userId);
    List<Order> findOrderByStatus(String status);
    void cancelOrder(Long orderId);
    List<OrderItemResponse> findOrderItemsByOrderId(Long orderId);
    void updateOrderStatus(Long orderId, String newStatus);
    Double calculateOrderTotal(Long orderId);
}
