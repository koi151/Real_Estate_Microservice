package com.koi151.msproperty.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingPublishedEvent {
    private Long propertyId;
    private String accountId;
}