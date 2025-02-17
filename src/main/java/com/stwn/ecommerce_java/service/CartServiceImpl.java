package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.common.errors.BadRequestException;
import com.stwn.ecommerce_java.common.errors.ForbiddenAccessException;
import com.stwn.ecommerce_java.common.errors.InventoryException;
import com.stwn.ecommerce_java.common.errors.ResourceNotFoundException;
import com.stwn.ecommerce_java.entity.Cart;
import com.stwn.ecommerce_java.entity.CartItem;
import com.stwn.ecommerce_java.entity.Product;
import com.stwn.ecommerce_java.model.CartItemResponse;
import com.stwn.ecommerce_java.repository.CartItemRepository;
import com.stwn.ecommerce_java.repository.CartRepository;
import com.stwn.ecommerce_java.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public void addItemToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .build();
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findByIdPessimisticLock(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product not found with product id : " + productId));

        if (product.getUserId().equals(userId)) {
            throw new BadRequestException("Cannot add your own product to cart");
        }

        if(product.getStockQuantity() <= 0){
            throw new InventoryException("product is equal or below zero");
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(
                cart.getCartId(), productId);

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cartId(cart.getCartId())
                    .productId(productId)
                    .quantity(quantity)
                    .price(product.getPrice())
                    .build();
            cartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cart not found for user with id " + userId));

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(
                cart.getCartId(), productId);

        if (existingItemOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product " + productId + " is not yet added to cart");
        }

        CartItem item = existingItemOpt.get();
        if (!item.getCartId().equals(cart.getCartId())) {
            throw new ForbiddenAccessException("Cart item doesn't belong to user's cart");
        }

        if (quantity <= 0) {
            cartItemRepository.deleteById(item.getCartItemId());
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cart not found for user with id " + userId));

        Optional<CartItem> existingItemOpt = cartItemRepository.findById(cartItemId);

        if (existingItemOpt.isEmpty()) {
            throw new ResourceNotFoundException("Cart item not found");
        }

        CartItem item = existingItemOpt.get();

        if (!item.getCartId().equals(cart.getCartId())) {
            throw new ForbiddenAccessException("Cart item doesn't belong to user's cart");
        }

        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cart not found for user with id " + userId));
        cartItemRepository.deleteAllByCartId(cart.getCartId());
    }

    @Override
    public List<CartItemResponse> getCartItems(Long userId) {
        List<CartItem> cartItems = cartItemRepository.getUserCartItems(userId);
        if (cartItems.isEmpty()) {
            return Collections.emptyList();
        }
        log.error("TESSST 1");
        List<Long> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .toList();
        log.error("TESSST 2");
        List<Product> products = productRepository.findAllById(productIds);
        log.error("TESSST 3");
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        log.error("TESSST 4");
        return cartItems.stream()
                .map(cartItem -> {
                    Product product = productMap.get(cartItem.getProductId());
                    if (product == null) {
                        throw new ResourceNotFoundException(
                                "Product not found for ID: " + cartItem.getProductId());
                    }
                    return CartItemResponse.fromCartItemAndProduct(cartItem, product);
                })
                .toList();
    }
}
