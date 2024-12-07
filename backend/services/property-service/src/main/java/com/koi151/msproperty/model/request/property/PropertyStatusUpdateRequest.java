package com.koi151.msproperty.model.request.property;

import com.koi151.msproperty.enums.StatusEnum;

public record PropertyStatusUpdateRequest(
    StatusEnum status
) {}
