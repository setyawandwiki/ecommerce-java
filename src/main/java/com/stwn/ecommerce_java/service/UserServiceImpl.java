package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.common.errors.*;
import com.stwn.ecommerce_java.entity.Role;
import com.stwn.ecommerce_java.entity.User;
import com.stwn.ecommerce_java.entity.UserRole;
import com.stwn.ecommerce_java.model.UserRegisterRequest;
import com.stwn.ecommerce_java.model.UserResponse;
import com.stwn.ecommerce_java.model.UserUpdateRequest;
import com.stwn.ecommerce_java.repository.RoleRepository;
import com.stwn.ecommerce_java.repository.UserRepository;
import com.stwn.ecommerce_java.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest request) {
        if(existsByUsername(request.getUsername())){
            throw new UsernameAlreadyExistsException("username already taken");
        }
        if(existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("email already taken");
        }
        if(!request.getPassword().equals(request.getPasswordConfirmation())){
            throw new BadRequestException("password confirmation doesn't match");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .enabled(true)
                .password(encodedPassword)
                .build();

        userRepository.save(user);
        Role userRole = roleRepository.findByName("ROLE_USEr")
                .orElseThrow(()-> new RoleNotFoundException("default role not found"));
        UserRole userRoleRelation = UserRole.builder()
                .id(new UserRole.UserRoleId(user.getUserId(), userRole.getRoleId()))
                .build();
        userRoleRepository.save(userRoleRelation);
        return UserResponse.fromUserAndRoles(user, List.of(userRole));
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user not found with id " + id));
        List<Role> roles = roleRepository.findByUserId(id);
        return UserResponse.fromUserAndRoles(user, roles);
    }

    @Override
    public UserResponse findByKeyword(String keyword) {
        User user = userRepository.findByKeyword(keyword)
                .orElseThrow(()-> new ResourceNotFoundException("user not found with username or email : " + keyword));
        List<Role> roles = roleRepository.findByUserId(user.getUserId());
        return UserResponse.fromUserAndRoles(user, roles);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user not found with id " + id));
        if(request.getCurrentPassword() != null && request.getNewPassword() != null){
            if(passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
                throw new InvalidPasswordException("Current password is incorrect");
            }
            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(encodedPassword);
        }
        if(request.getUsername() != null && !request.getUsername().equals(user.getUsername())){
            if(existsByUsername(request.getUsername())){
                throw new UsernameAlreadyExistsException("username with name : " + request.getUsername() + " is already exist");
            }
            user.setUsername(request.getUsername());
        }

        if(request.getEmail() != null && !request.getEmail().equals(user.getEmail())){
            if(existsByEmail(request.getEmail())){
                throw new UsernameAlreadyExistsException("email with name : " + request.getEmail() + " is already exist");
            }
            user.setUsername(request.getEmail());
        }
        userRepository.save(user);
        List<Role> roles = roleRepository.findByUserId(user.getUserId());
        return UserResponse.fromUserAndRoles(user, roles);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user not found with id " + id));
        userRoleRepository.deleteByUserId(user.getUserId());
        userRepository.delete(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
