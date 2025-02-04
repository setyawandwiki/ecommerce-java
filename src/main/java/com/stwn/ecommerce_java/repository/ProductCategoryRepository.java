package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/*product category tidak ada PK, jadi yang dimasukkan menggunakan productcategoryid, karena
* dari spring sudah di anotasikan embeddedid, jadi akan diasumsikan sebagai id oleh library spring
* */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategory.ProductCategoryId> {
}
