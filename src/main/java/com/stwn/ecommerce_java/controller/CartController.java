package com.stwn.ecommerce_java.controller;

import com.stwn.ecommerce_java.entity.Cart;
import com.stwn.ecommerce_java.entity.CartItem;
import com.stwn.ecommerce_java.model.AddToCartRequest;
import com.stwn.ecommerce_java.model.CartItemResponse;
import com.stwn.ecommerce_java.model.UpdateCartItemRequest;
import com.stwn.ecommerce_java.model.UserInfo;
import com.stwn.ecommerce_java.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/carts")
@SecurityRequirement(name = "Bearer")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @PostMapping("/items")
    public ResponseEntity<Void> addItemToCart(@Valid @RequestBody AddToCartRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        cartService.addItemToCart(userInfo.getUser().getUserId(), request.getProductId(), request.getQuantity());

        return ResponseEntity.ok().build();
    }
    @PutMapping("/items")
    public ResponseEntity<Void> updateCartItemQuantity(@Valid @RequestBody UpdateCartItemRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        cartService.updateCartItemQuantity(userInfo.getUser().getUserId(), request.getProductId(), request.getQuantity());

        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        cartService.removeItemFromCart(userInfo.getUser().getUserId(), id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        List<CartItemResponse> items = cartService.getCartItems(userInfo.getUser().getUserId());
        return ResponseEntity.ok().body(items);
    }
    @DeleteMapping("/items")
    public ResponseEntity<Void> clearCart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        cartService.clearCart(userInfo.getUser().getUserId());
        return ResponseEntity.noContent().build();
    }
}
