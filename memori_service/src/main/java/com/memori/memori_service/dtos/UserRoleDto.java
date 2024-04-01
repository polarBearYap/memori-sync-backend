package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleDto {
    private Long id;

    private Integer role;

    private String userId;

    // private UserDto user;
}
