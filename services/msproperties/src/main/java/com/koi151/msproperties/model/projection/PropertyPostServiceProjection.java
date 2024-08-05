package com.koi151.msproperties.model.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.koi151.msproperties.enums.PostingPackageEnum;

import java.time.LocalDateTime;

public record PropertyPostServiceProjection(
    PostingPackageEnum postingPackage,
    LocalDateTime postingDate
) {}
