package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.model.ShippingOrderRequest;
import com.stwn.ecommerce_java.model.ShippingOrderResponse;
import com.stwn.ecommerce_java.model.ShippingRateRequest;
import com.stwn.ecommerce_java.model.ShippingRateResponse;

import java.math.BigDecimal;

public interface ShippingService {
    ShippingRateResponse calculateShippingRate(ShippingRateRequest request);
    ShippingOrderResponse createShippingOrder(ShippingOrderRequest request);
    String generateAwbNumber(Long orderId);
    BigDecimal calculate(Long orderId);
}
