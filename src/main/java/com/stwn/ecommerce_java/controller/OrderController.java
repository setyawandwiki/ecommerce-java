package com.stwn.ecommerce_java.controller;

import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.model.CheckOutRequest;
import com.stwn.ecommerce_java.model.OrderItemResponse;
import com.stwn.ecommerce_java.model.OrderResponse;
import com.stwn.ecommerce_java.model.UserInfo;
import com.stwn.ecommerce_java.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "orders")
@SecurityRequirement(name = "Bearer")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkOut(
            @Valid @RequestBody CheckOutRequest request
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        request.setUserId(userInfo.getUser().getUserId());
        Order order = orderService.checkOut(request);
        OrderResponse orderResponse = OrderResponse.fromOrder(order);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable("orderId") Long orderId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        return orderService.findOrderById(orderId)
                .map(order ->{
                    if(order.getUserId().equals(userInfo.getUser().getUserId())){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(OrderResponse.builder().build());
                    }
                    OrderResponse response = OrderResponse.fromOrder(order);
                    return ResponseEntity.ok(response);
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderResponse>> findOrdersById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        List<Order> userOrders = orderService.findOrdersByUserId(userInfo.getUser().getUserId());
        List<OrderResponse> orderResponses = userOrders.stream().map(OrderResponse::fromOrder).toList();

        return ResponseEntity.ok().body(orderResponses);
    }
    @PutMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemResponse>> findOrderItems(@PathVariable Long orderId){
        List<OrderItemResponse> orderItemResponses = orderService.findOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItemResponses);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                  @RequestParam String newStatus){
        orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/total")
    public ResponseEntity<Double> calculateOrderTotal(@PathVariable Long orderId){
        double total = orderService.calculateOrderTotal(orderId);
        return ResponseEntity.ok(total);
    }
}
