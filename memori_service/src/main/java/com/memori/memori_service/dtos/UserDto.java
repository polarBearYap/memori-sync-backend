package com.memori.memori_service.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private String id;

    private String email;

    private String username;

    private Boolean isEmailVerified;

    private Integer tier;

    private Long storageSizeInByte;

    private String timezone;

    private Integer dailyResetTime;
}
