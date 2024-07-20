package com.koi151.msproperties.service.impl;

import com.koi151.msproperties.entity.Room;
import com.koi151.msproperties.model.dto.RoomDTO;
import com.koi151.msproperties.entity.Property;
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
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("No property found with id " + propertyId));

        Room room = Room.builder()
                .roomType(request.roomType())
                .quantity(request.quantity())
                .property(property) // Associate the room with the property
                .build();

        Room savedRoom = roomRepository.save(room);

        return RoomDTO.builder()
                .roomId(savedRoom.getRoomId())
                .roomType(savedRoom.getRoomType())
                .quantity(savedRoom.getQuantity())
                .propertyId(propertyId)
                .build();
    }
}
