package com.stwn.ecommerce_java.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.stwn.ecommerce_java.entity.CartItem;
import com.stwn.ecommerce_java.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartItemResponse implements Serializable {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quanity;
    private BigDecimal weight;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CartItemResponse fromCartItemAndProduct(CartItem cartItem, Product product){
        BigDecimal total = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        BigDecimal weight = product.getWeight().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        return CartItemResponse.builder()
                .cartItemId(cartItem.getCartItemId())
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .quanity(cartItem.getQuantity())
                .weight(weight)
                .totalPrice(total)
                .build();
    }
}
