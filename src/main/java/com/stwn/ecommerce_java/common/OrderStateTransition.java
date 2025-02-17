package com.stwn.ecommerce_java.common;

import com.stwn.ecommerce_java.model.OrderStatus;
import jakarta.validation.Valid;

import java.util.EnumMap;
import java.util.Set;

public class OrderStateTransition {
    private static final EnumMap<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = new EnumMap<>(OrderStatus.class);
    static {
        VALID_TRANSITIONS.put(OrderStatus.PENDING, Set.of(OrderStatus.CANCELLED, OrderStatus.PAID, OrderStatus.PAYMENT_FAILED));
        VALID_TRANSITIONS.put(OrderStatus.PAID, Set.of(OrderStatus.SHIPPED));
        VALID_TRANSITIONS.put(OrderStatus.CANCELLED, Set.of());
        VALID_TRANSITIONS.put(OrderStatus.SHIPPED, Set.of());
        VALID_TRANSITIONS.put(OrderStatus.PAYMENT_FAILED, Set.of());
    }

    public static boolean isValidTransition(OrderStatus currentStatus, OrderStatus newStatus){
        Set<OrderStatus> validNextState = VALID_TRANSITIONS.get(currentStatus);
        if(validNextState == null){
            return false;
        }

        return validNextState.contains(newStatus);
    }
}
