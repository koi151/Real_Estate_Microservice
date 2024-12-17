package com.koi151.listing_services.model.request;

import com.koi151.listing_services.enums.PackageType;
import com.koi151.listing_services.enums.Status;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PropertyServicePackageCreateRequest (
    @NotNull(message = "Property id is mandatory")
    Long propertyId,
    @NotNull(message = "Property service package type is mandatory")
    PackageType packageType,
//    @Column(name = "posting_date", columnDefinition = "TIMESTAMP(0)")
//    @Future(message = "Posting date must be in the future")
//    LocalDateTime postingDate,
//    Status status,
    @NotNull(message = "Post service ids used for that property service package is mandatory")
    @NotEmpty(message = "Post service ids used for that property service package is mandatory")
    List<Long> postServiceIds
) {}
