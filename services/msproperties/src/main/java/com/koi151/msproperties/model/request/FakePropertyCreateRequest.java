package com.koi151.msproperties.model.request;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FakePropertyCreateRequest extends PropertyCreateRequest{
    private int view = 0;
}

