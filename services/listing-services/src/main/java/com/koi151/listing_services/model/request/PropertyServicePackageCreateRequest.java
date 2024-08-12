package com.koi151.listing_services.model.request;

import com.koi151.listing_services.enums.PackageType;
import com.koi151.listing_services.enums.Status;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record PropertyServicePackageCreateRequest (
    Long propertyId,
    @NotNull(message = "Property service package type is mandatory")
    PackageType packageType,
    @Column(name = "posting_date", columnDefinition = "TIMESTAMP(0)")
    @Future(message = "Posting date must be in the future")
    LocalDateTime postingDate,
    Status status,
    @NotNull
    @NotEmpty
    Set<Long> postServicePackageIds
) {}
