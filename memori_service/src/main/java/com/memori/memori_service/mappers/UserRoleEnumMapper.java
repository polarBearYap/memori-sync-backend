package com.memori.memori_service.mappers;

import org.springframework.stereotype.Component;

import com.memori.memori_domain.UserRole;

@Component
public class UserRoleEnumMapper {
    public UserRole.Role asRole(Integer value) {
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");
        return UserRole.Role.fromValue(value);
    }

    public Integer asInteger(UserRole.Role value) {
        return value.getValue();
    }
}