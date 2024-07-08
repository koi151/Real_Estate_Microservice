package com.koi151.msproperties.service;

import com.koi151.msproperties.model.dto.RoomDTO;
import com.koi151.msproperties.model.request.RoomCreateUpdateRequest;

public interface RoomService {

    RoomDTO createRoom(Integer id, RoomCreateUpdateRequest request);

}
