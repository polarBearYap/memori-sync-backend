package com.memori.memori_data.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.memori.memori_domain.SyncEntity;

@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.ERROR, 
    mappingInheritanceStrategy = MappingInheritanceStrategy.EXPLICIT, 
    componentModel = MappingConstants.ComponentModel.SPRING, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface EntityMapperConfig {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    // lastModified is updated
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "syncedAt", ignore = true)
    // modifiedByDeviceId is updated
    @Mapping(target = "sortOrder", ignore = true)
    void updateEntity(SyncEntity source, @MappingTarget SyncEntity target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    // lastModified is updated
    @Mapping(target = "version", ignore = true)
    // deletedAt is updated
    @Mapping(target = "syncedAt", ignore = true)
    // modifiedByDeviceId is updated
    @Mapping(target = "sortOrder", ignore = true)
    void deleteEntity(SyncEntity source, @MappingTarget SyncEntity target);
}