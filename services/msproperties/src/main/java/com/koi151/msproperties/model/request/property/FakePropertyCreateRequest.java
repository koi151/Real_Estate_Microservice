package com.koi151.msproperties.model.request.property;

import com.koi151.msproperties.model.request.property.PropertyCreateRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FakePropertyCreateRequest extends PropertyCreateRequest {
    private int view = 0;
}

