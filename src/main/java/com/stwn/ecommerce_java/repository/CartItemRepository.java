package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = """
            SELECT ci.* FROM cart_items ci
            JOIN carts c 
            ON ci.cart_item_id = c.cart_id
            WHERE c.user_id = :userId
            """, nativeQuery = true)
    List<CartItem> getUserCartItems(@Param("userId") Long userId);
    @Query(value = """
            SELECT * FROM cart_items AS ci
            WHERE cart_id = :cartId
            AND product_id = :productId
            """, nativeQuery = true)
    Optional<CartItem> findByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
    @Modifying /*digunkana untuk query perubahan data*/
    @Query(value = """
            DELETE FROM cart_items
            WHERE cart_id = :cartId
            """, nativeQuery = true)
    void deleteAllByCartId(@Param("cartId") Long cartId);
}
