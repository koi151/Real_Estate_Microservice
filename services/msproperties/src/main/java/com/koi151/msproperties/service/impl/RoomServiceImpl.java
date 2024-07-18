package com.koi151.msproperties.service.impl;

import com.koi151.msproperties.model.dto.RoomDTO;
import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.entity.RoomEntity;
import com.koi151.msproperties.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperties.repository.PropertyRepository;
import com.koi151.msproperties.repository.RoomRepository;
import com.koi151.msproperties.service.RoomService;
import com.koi151.msproperties.customExceptions.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Override
    public RoomDTO createRoom(Integer propertyId, RoomCreateUpdateRequest request) {
        PropertyEntity property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("No property found with id " + propertyId));

        RoomEntity roomEntity = RoomEntity.builder()
                .roomType(request.getRoomType())
                .quantity(request.getQuantity())
                .propertyEntity(property) // Associate the room with the property
                .build();

        RoomEntity savedRoomEntity = roomRepository.save(roomEntity);

        return RoomDTO.builder()
                .roomId(savedRoomEntity.getRoomId())
                .roomType(savedRoomEntity.getRoomType())
                .quantity(savedRoomEntity.getQuantity())
                .propertyId(propertyId)
                .build();
    }
}
