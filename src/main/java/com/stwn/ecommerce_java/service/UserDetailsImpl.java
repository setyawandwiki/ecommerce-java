package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.common.errors.UserNotFoundException;
import com.stwn.ecommerce_java.entity.Role;
import com.stwn.ecommerce_java.entity.User;
import com.stwn.ecommerce_java.model.UserInfo;
import com.stwn.ecommerce_java.repository.RoleRepository;
import com.stwn.ecommerce_java.repository.UserRepository;
import com.stwn.ecommerce_java.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByKeyword(username)
                .orElseThrow(()-> new UserNotFoundException("User not found with username : " + username));
        List<Role> roles = roleRepository.findByUserId(user.getUserId());
        return UserInfo.builder()
                .roles(roles)
                .user(user)
                .build();
    }
}
