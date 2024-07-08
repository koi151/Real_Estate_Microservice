package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.RoomEntity;
import com.koi151.msproperties.model.request.RoomCreateUpdateRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS) // fields in the target object will retain their existing values if the corresponding fields in the source object are null
    RoomEntity updateRoomFromDto(RoomCreateUpdateRequest roomRequest, @MappingTarget RoomEntity roomEntity);
}
