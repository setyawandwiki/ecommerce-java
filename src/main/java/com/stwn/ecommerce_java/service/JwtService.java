package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.model.UserInfo;

public interface JwtService {
    String generateToken(UserInfo userInfo);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
