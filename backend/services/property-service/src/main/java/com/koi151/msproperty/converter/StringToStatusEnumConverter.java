package com.koi151.msproperty.converter;

import com.koi151.msproperty.customExceptions.InvalidEnumValueException;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.utils.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStatusEnumConverter implements Converter<String, StatusEnum> {

    @Override
    public StatusEnum convert(String source) {
        if (!StringUtils.checkString(source)) {
            return null;
        }
        try {
            return StatusEnum.fromString(source);
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumValueException("Invalid StatusEnum enum value: ");
        }
    }
}
