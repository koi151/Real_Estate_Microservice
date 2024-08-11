package com.koi151.listing_services.model.request;

import com.koi151.listing_services.entity.PostServicePackage;
import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.enums.PackageType;
import com.koi151.listing_services.enums.Status;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record PropertyServicePackageCreateRequest (
    @NotNull(message = "Property service package type is mandatory")
    PackageType packageType,
    @Column(name = "posting_date", columnDefinition = "TIMESTAMP(0)")
    @Future(message = "Posting date must be in the future")
    LocalDateTime postingDate,
    Status status,
    @NotNull
    @NotEmpty
    List<Long> postServicePackageIds
) {}
