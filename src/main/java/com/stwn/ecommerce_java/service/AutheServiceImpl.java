package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.common.errors.InvalidPasswordException;
import com.stwn.ecommerce_java.model.AuthRequest;
import com.stwn.ecommerce_java.model.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutheServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    @Override
    public UserInfo authenticate(AuthRequest request) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserInfo user = (UserInfo) authentication.getPrincipal();
            return user;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InvalidPasswordException("Invalid username or password");
        }
    }
}
