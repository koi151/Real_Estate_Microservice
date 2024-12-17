package com.koi151.payment.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceValidatedEvent {
    private Long propertyId;
    private String accountId;
    private double totalAmount;
}
