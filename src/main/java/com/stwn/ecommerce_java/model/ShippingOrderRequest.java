package com.stwn.ecommerce_java.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ShippingOrderRequest {
    private Long orderId;
    private Address fromAddress;
    private Address toAddress;
    private BigDecimal totalWeightInGrams;
    @Data
    @Builder
    public static class Address{
        private String streetAddress;
        private String city;
        private String state;
        private String postalCode;
    }
}
