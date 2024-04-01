package com.memori.memori_service.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.ReportingPolicy;

import com.memori.memori_domain.SyncEntity;
import com.memori.memori_service.dtos.SyncEntityDto;

@MapperConfig(
    uses = { 
        CardRatingMapper.class, 
        CardStateMapper.class, 
        DateTimeMapper.class, 
        DeckPriorityMapper.class, 
        DeckRatingMapper.class, 
        StudyOptionModeMapper.class,
        StudySortOptionMapper.class,
        UserTierMapper.class, 
        UserRoleEnumMapper.class }, 
    unmappedTargetPolicy = ReportingPolicy.ERROR, 
    mappingInheritanceStrategy = MappingInheritanceStrategy.EXPLICIT, 
    componentModel = MappingConstants.ComponentModel.SPRING, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SyncEntityMapperConfig {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deletedAt", defaultExpression = "java(null)")
    @Mapping(target = "syncedAt", defaultExpression = "java(null)")
    @Mapping(target = "entityType", ignore = true)
    @Mapping(target = "sortOrder", ignore = true)
    SyncEntity dtoToEntityMapping(SyncEntityDto dto);

    @Mapping(target = "action", ignore = true)
    @Mapping(target = "deletedAt", defaultExpression = "java(null)")
    SyncEntityDto entityToDtoMapping(SyncEntity entity);
}