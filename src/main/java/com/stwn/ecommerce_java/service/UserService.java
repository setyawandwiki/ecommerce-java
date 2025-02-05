package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.model.UserRegisterRequest;
import com.stwn.ecommerce_java.model.UserResponse;
import com.stwn.ecommerce_java.model.UserUpdateRequest;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    UserResponse findById(Long id);
    UserResponse findByKeyword(String keyword);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
