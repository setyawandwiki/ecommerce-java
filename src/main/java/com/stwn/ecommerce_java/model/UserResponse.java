package com.stwn.ecommerce_java.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.stwn.ecommerce_java.entity.Role;
import com.stwn.ecommerce_java.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    private Long userId;
    private String username;
    private String email;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> role;

    public static UserResponse fromUserAndRoles(User user, List<Role> roles){
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .role(roles.stream().map(Role::getName).toList())
                .build();
    }
}
