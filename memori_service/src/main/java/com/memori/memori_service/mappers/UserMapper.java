package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.memori.memori_domain.User;
import com.memori.memori_service.dtos.UserDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface UserMapper {
    @InheritConfiguration(name = "dtoToEntityMapping")
    User dtoToEntity(UserDto dto);

    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    UserDto entityToDto(User entity);
}