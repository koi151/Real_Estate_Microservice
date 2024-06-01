package com.koi151.msproperties.service;

import com.koi151.msproperties.dto.RoomDTO;
import com.koi151.msproperties.entity.Properties;
import com.koi151.msproperties.entity.Room;
import com.koi151.msproperties.entity.payload.request.RoomCreateRequest;
import com.koi151.msproperties.repository.PropertiesRepository;
import com.koi151.msproperties.repository.RoomRepository;
import com.koi151.msproperties.service.imp.RoomServiceImp;
import customExceptions.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService implements RoomServiceImp {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    PropertiesRepository propertiesRepository;

    @Override
    public RoomDTO createRoom(Integer propertyId, RoomCreateRequest request) {
        Properties property = propertiesRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("No property found with id " + propertyId));

        Room room = Room.builder()
                .roomType(request.getRoomType())
                .quantity(request.getQuantity())
                .properties(property) // Associate the room with the property
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
