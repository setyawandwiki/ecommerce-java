package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.common.OrderStateTransition;
import com.stwn.ecommerce_java.common.errors.ResourceNotFoundException;
import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.entity.OrderItem;
import com.stwn.ecommerce_java.entity.Product;
import com.stwn.ecommerce_java.model.*;
import com.stwn.ecommerce_java.repository.OrderItemRepository;
import com.stwn.ecommerce_java.repository.OrderRepository;
import com.stwn.ecommerce_java.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingServiceImpl implements ShippingService {

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(10000);
    private static final BigDecimal RATE_PER_KG = BigDecimal.valueOf(2500);
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;


    @Override
    public ShippingRateResponse calculateShippingRate(ShippingRateRequest request) {
        BigDecimal shippingFee = BASE_RATE.add(request.getTotalWeightInGrams()
                .divide(BigDecimal.valueOf(1000))
                .multiply(RATE_PER_KG))
                .setScale(2, RoundingMode.HALF_UP);

        String estimatedDeliveryFee = "3 - 5 hari kerja";
        return ShippingRateResponse
                .builder()
                .shippingFee(shippingFee)
                .estimatedDeliveryTime(estimatedDeliveryFee)
                .build();
    }

    @Override
    public ShippingOrderResponse createShippingOrder(ShippingOrderRequest request) {
        String awbNumber = generateAwbNumber(request.getOrderId());
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(()->
                        new ResourceNotFoundException("Order with id " + request.getOrderId() + " not found"));

        if(!OrderStateTransition.isValidTransition(order.getStatus(), OrderStatus.SHIPPED)){
            throw new IllegalStateException("Invalid order status transition fron " + order.getStatus()
                    + " to shupped");
        }

        order.setStatus(OrderStatus.SHIPPING);
        order.setAwbNumber(awbNumber);
        orderRepository.save(order);

        BigDecimal shippingFee = BASE_RATE.add(
                request.getTotalWeightInGrams().divide(BigDecimal.valueOf(1000)).multiply(RATE_PER_KG)
                        .setScale(2, RoundingMode.HALF_UP)
        );
        String estimatedDeliveryFee = "3 - 5 hari kerja";

        return ShippingOrderResponse.builder()
                .awbNumber(awbNumber)
                .shippingFee(shippingFee)
                .estimatedDeliveryTime(estimatedDeliveryFee)
                .build();
    }

    @Override
    public String generateAwbNumber(Long orderId) {
        Random random = new Random();
        String preFix = "AWB";
        return String.format("%s%011d", preFix, random.nextInt(100000000));
    }

    @Override
    public BigDecimal calculate(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return orderItems.stream()
                .map(orderItem -> {
                    Product product = productRepository.findById(orderItem.getProductId())
                            .orElseThrow(()-> new ResourceNotFoundException("product not found with id " + orderItem.getProductId()));
                    BigDecimal totalWeight = product.getWeight().multiply(BigDecimal.valueOf(orderId));
                    return totalWeight;
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
