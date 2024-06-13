package com.koi151.msproperties.service.imp;

import com.koi151.msproperties.dto.RoomDTO;
import com.koi151.msproperties.entity.payload.request.RoomCreateRequest;

public interface RoomService {

    RoomDTO createRoom(Integer id, RoomCreateRequest request);

}
