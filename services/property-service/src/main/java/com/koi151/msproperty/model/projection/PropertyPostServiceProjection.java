package com.koi151.msproperty.model.projection;

import com.koi151.msproperty.enums.PostingPackageEnum;

import java.time.LocalDateTime;

public record PropertyPostServiceProjection(
    PostingPackageEnum postingPackage,
    LocalDateTime postingDate
) {}
