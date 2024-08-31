package com.koi151.notification.controller;

import com.koi151.notification.model.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/${application.config.api.prefix}/notifications")
@RequiredArgsConstructor
public class NotificationController {

    @GetMapping("/")
    public ResponseEntity<ResponseData> getNotifications() {
        return ResponseEntity.ok(ResponseData.builder()
            .data(true)
            .description("OK")
            .build());
    }
}







