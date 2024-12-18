package com.koi151.msproperty.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyStatusMessage {
    private Long propertyId;
    private String status;
    private String paymentUrl;
    private String reason;
}