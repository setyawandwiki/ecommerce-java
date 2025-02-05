package com.stwn.ecommerce_java.repository;

import com.stwn.ecommerce_java.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByByUsername(String username);
    Boolean existsByEmail(String email);
    @Query(value = """
            SELECT * FROM users WHERE lower(username) LIKE :keyword 
            OR 
            lower(email) LIKE :keyword
            """, nativeQuery = true)
    Page<User> searchUser(@Param("keyword") String keyword, Pageable pageable);
}
