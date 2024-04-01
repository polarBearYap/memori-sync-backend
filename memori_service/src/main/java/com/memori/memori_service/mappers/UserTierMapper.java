package com.memori.memori_service.mappers;

import org.springframework.stereotype.Component;

import com.memori.memori_domain.User;

@Component
public class UserTierMapper {
    public User.Tier asTier(Integer value) {
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");
        return User.Tier.fromValue(value);
    }

    public Integer asInteger(User.Tier value) {
        return value.getValue();
    }
}
