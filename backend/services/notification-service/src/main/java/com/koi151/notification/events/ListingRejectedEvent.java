package com.koi151.notification.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingRejectedEvent {
    private Long propertyId;
    private String accountId;
    private String reason;
}
