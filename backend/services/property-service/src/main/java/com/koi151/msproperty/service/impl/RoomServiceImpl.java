package com.koi151.msproperty.service.impl;

import com.koi151.msproperty.entity.Rooms;
import com.koi151.msproperty.model.dto.RoomDTO;
import com.koi151.msproperty.entity.Property;
import com.koi151.msproperty.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperty.repository.PropertyRepository;
import com.koi151.msproperty.repository.RoomRepository;
import com.koi151.msproperty.service.RoomService;
import com.koi151.msproperty.customExceptions.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Override
    public RoomDTO createRoom(Long propertyId, RoomCreateUpdateRequest request) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("No property found with id " + propertyId));

        Rooms rooms = Rooms.builder()
                .roomType(request.roomType())
                .quantity(request.quantity())
                .property(property) // Associate the room with the property
                .build();

        Rooms savedRooms = roomRepository.save(rooms);

        return RoomDTO.builder()
                .roomId(savedRooms.getRoomId())
                .roomType(String.valueOf(savedRooms.getRoomType()))
                .quantity(savedRooms.getQuantity())
                .propertyId(propertyId)
                .build();
    }
}
