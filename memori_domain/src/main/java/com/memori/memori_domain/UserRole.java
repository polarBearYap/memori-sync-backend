package com.memori.memori_domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "app_user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public enum Role {
        ADMIN(1);

        private final int value;

        Role(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Role fromValue(int value) {
            for (Role role : Role.values()) {
                if (role.getValue() == value) {
                    return role;
                }
            }
            throw new IllegalArgumentException("No matching role for value: " + value);
        }

        public static boolean isValidCode(int value) {
            for (Role role : Role.values())
                if (role.getValue() == value)
                    return true;
            return false;
        }
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.ADMIN;

    public void setRole(int role) {
        this.role = Role.fromValue(role);
    }

    public int getRole() {
        return role.getValue();
    }

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    private String userId;
}
