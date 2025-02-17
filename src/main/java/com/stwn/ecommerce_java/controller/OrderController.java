package com.stwn.ecommerce_java.controller;

import com.stwn.ecommerce_java.common.PageUtil;
import com.stwn.ecommerce_java.common.errors.BadRequestException;
import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.model.*;
import com.stwn.ecommerce_java.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@RequestMapping("orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkOut(
            @Valid @RequestBody CheckOutRequest request
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        request.setUserId(userInfo.getUser().getUserId());
        OrderResponse order = orderService.checkOut(request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable("orderId") Long orderId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        return orderService.findOrderById(orderId)
                .map(order ->{
                    if (!order.getUserId().equals(userInfo.getUser().getUserId())) {
                        return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(OrderResponse.builder().build());
                    }
                    OrderResponse response = OrderResponse.fromOrder(order);
                    return ResponseEntity.ok(response);
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<PaginatedOrderResponse> findOrdersById(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "order_id,desc") String[] sort
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        List<Sort.Order> orders = PageUtil.parseSortOrderRequest(sort);

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        Page<OrderResponse> userOrders = orderService.findOrdersByUserIdAndPage(userInfo.getUser().getUserId(), pageable);

        PaginatedOrderResponse paginatedOrderResponse = orderService.convertOrderPage(userOrders);

        return ResponseEntity.ok().body(paginatedOrderResponse);
    }
    @PutMapping("/{orderId}/cancel")
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
        OrderStatus status;
        try{
            status = OrderStatus.valueOf(newStatus);
        }catch (IllegalArgumentException e){
            throw new BadRequestException("unrecognize status");
        }
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}/total")
    public ResponseEntity<Double> calculateOrderTotal(@PathVariable Long orderId){
        double total = orderService.calculateOrderTotal(orderId);
        return ResponseEntity.ok(total);
    }
}
