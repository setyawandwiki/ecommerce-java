package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUserId(Long userId);
    Optional<Cart> findByUserId(Long userId);
}
