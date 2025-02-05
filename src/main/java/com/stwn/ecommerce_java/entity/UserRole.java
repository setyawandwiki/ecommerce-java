package com.stwn.ecommerce_java.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "user_role")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRole {
    @EmbeddedId
    private UserRoleId id;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRoleId implements Serializable{
        @Column(name = "userId")
        private Long userId;
        @Column(name = "role_id")
        private Long roleId;
    }
}
