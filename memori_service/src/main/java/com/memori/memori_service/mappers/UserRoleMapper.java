package com.memori.memori_service.mappers;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.memori.memori_domain.UserRole;
import com.memori.memori_service.dtos.UserRoleDto;

@Mapper(config = SyncEntityMapperConfig.class)
public interface UserRoleMapper {
    @InheritConfiguration(name = "dtoToEntityMapping")
    @Mapping(target = "user", ignore = true)
    UserRole dtoToEntity(UserRoleDto dto);

    @InheritInverseConfiguration(name = "dtoToEntity")
    @InheritConfiguration(name = "entityToDtoMapping")
    UserRoleDto entityToDto(UserRole entity);
}
