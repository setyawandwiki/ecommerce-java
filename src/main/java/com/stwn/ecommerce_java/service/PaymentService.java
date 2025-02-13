package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.model.PaymentNotification;
import com.stwn.ecommerce_java.model.PaymentResponse;

public interface PaymentService {
    PaymentResponse create(Order order);
    PaymentResponse findByPaymentId(String paymentId);
    boolean verifyByPaymentId(String paymentId);
    /*untuk menerima webhook dari xenditnya*/
    void handleNotification(PaymentNotification paymentNotification);
}
