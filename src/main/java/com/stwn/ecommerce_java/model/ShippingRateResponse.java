package com.stwn.ecommerce_java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRateResponse {
    private BigDecimal shippingFee;
    private String estimatedDeliveryTime;
}
