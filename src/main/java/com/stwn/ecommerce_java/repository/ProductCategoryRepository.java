package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/*product category tidak ada PK, jadi yang dimasukkan menggunakan productcategoryid, karena
* dari spring sudah di anotasikan embeddedid, jadi akan diasumsikan sebagai id oleh library spring
* */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategory.ProductCategoryId> {
    /*mencari relasi product & category*/
    @Query(value = """
            SELECT * FROM product_category
            WHERE product_id = :productId
            """, nativeQuery = true)
    List<ProductCategory> findCategoriesByProductId(@Param("productId") Long productId);
}
