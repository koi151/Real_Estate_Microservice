package com.koi151.listing_services.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceValidationFailedEvent {
    private Long propertyId;
    private String accountId;
    private String reason;
}
