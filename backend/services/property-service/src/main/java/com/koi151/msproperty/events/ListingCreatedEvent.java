package com.koi151.msproperty.events;

import com.koi151.msproperty.enums.PackageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingCreatedEvent {
    private Long propertyId;
    private List<Long> postServiceIds;
    private PackageType packageType;
}
