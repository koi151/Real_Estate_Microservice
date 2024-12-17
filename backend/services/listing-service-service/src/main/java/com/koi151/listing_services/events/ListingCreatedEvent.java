package com.koi151.listing_services.events;

import java.util.List;
import com.koi151.listing_services.enums.PackageType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingCreatedEvent {
    private Long propertyId;
    private List<Long> postServiceIds;
    private PackageType packageType;
}
