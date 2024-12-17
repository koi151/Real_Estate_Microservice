package com.koi151.msproperty.service;

import com.koi151.msproperty.model.dto.RoomDTO;
import com.koi151.msproperty.model.request.rooms.RoomCreateUpdateRequest;

public interface RoomService {

    RoomDTO createRoom(Long id, RoomCreateUpdateRequest request);

}
