package com.koi151.msproperty.controller;

import com.koi151.msproperty.model.dto.RoomDTO;
import com.koi151.msproperty.model.reponse.ResponseData;
import com.koi151.msproperty.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperty.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/properties/")
public class RoomController {

    @Autowired
    RoomService roomService;

    @PostMapping(value = "/{propertyId}/rooms")
    public ResponseEntity<ResponseData> createRoom(
            @PathVariable("propertyId") Integer propertyId,
            @RequestBody RoomCreateUpdateRequest request
    ) {


        RoomDTO room = roomService.createRoom(propertyId, request);

        ResponseData responseData = new ResponseData();
        responseData.setData(room);
        responseData.setDesc("Success");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }
}
