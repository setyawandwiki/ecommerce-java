package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = """
            SELECT * FROM product
            WHERE LOWER(name) LIKE :name
            """, nativeQuery = true)
    List<Product> findByName(@Param("name") String name);
    @Query(value = """
            SELECT DISTINCT p.* FROM product p
            JOIN product_category pc ON p.product_id = pc.product_id
            JOIN category c ON pc.category_id = c.category_id
            WHERE c.name = :categoryName
            """, nativeQuery = true)
    /*@param untuk menyesuaikan param pada querynya*/
    List<Product> findByCategory(@Param("categoryName") String categoryName);
}
