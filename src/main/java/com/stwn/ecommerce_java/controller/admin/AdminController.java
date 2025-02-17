package com.stwn.ecommerce_java.controller.admin;

import com.stwn.ecommerce_java.common.PageUtil;
import com.stwn.ecommerce_java.entity.Order;
import com.stwn.ecommerce_java.entity.User;
import com.stwn.ecommerce_java.model.OrderResponse;
import com.stwn.ecommerce_java.model.PaginatedOrderResponse;
import com.stwn.ecommerce_java.model.UserInfo;
import com.stwn.ecommerce_java.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@SecurityRequirement(name = "Bearer")
@RequiredArgsConstructor
public class AdminController {
    private final OrderService orderService;
    public ResponseEntity<PaginatedOrderResponse> findOrdersByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "order_id,size") String[] sort
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        List<Sort.Order> sortOrder = PageUtil.parseSortOrderRequest(sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrder));

        Page<OrderResponse> userOrders = orderService.findOrdersByUserIdAndPage(userInfo.getUser().getUserId(), pageable);
        PaginatedOrderResponse paginatedOrderResponse = orderService.convertOrderPage(userOrders);
        return ResponseEntity.ok(paginatedOrderResponse);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId){
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        return orderService.findOrderById(id)
                .map(order -> {
                    OrderResponse orderResponse = OrderResponse.fromOrder(order);
                    return ResponseEntity.ok(orderResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
