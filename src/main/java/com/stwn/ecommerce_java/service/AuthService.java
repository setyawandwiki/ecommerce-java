package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.model.AuthRequest;
import com.stwn.ecommerce_java.model.UserInfo;

public interface AuthService {
    UserInfo authenticate(AuthRequest request);
}
