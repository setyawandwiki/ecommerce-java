package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = """
            SELECT * FROM category
            WHERE name LOWER(name) LIKE :name
            """, nativeQuery = true)
    List<Category> findByName(@Param("name") String name);
}
