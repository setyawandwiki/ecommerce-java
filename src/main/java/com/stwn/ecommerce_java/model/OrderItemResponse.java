package com.stwn.ecommerce_java.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.stwn.ecommerce_java.entity.OrderItem;
import com.stwn.ecommerce_java.entity.Product;
import com.stwn.ecommerce_java.entity.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemResponse implements Serializable {
    private Long orderItemId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    private UserAddressResponse shippingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderItemResponse fromOrderItemProductAndAddress(
            OrderItem orderItem,
            Product product,
            UserAddress userAddress) {
        BigDecimal totalPrice = orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        return OrderItemResponse.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productId(product.getId())
                .productName(product.getName())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .totalPrice(totalPrice)
                .shippingAddress(UserAddressResponse.fromUserAddress(userAddress))
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .build();
    }
}