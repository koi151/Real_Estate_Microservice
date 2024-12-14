package com.koi151.msproperty.converter;

import com.koi151.msproperty.customExceptions.InvalidEnumValueException;
import com.koi151.msproperty.enums.PropertyTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// conversion service of Spring
@Component
public class StringToPropertyTypeEnumConverter implements Converter<String, PropertyTypeEnum> {
    @Override
    public PropertyTypeEnum convert(String source) {
        try {
            return PropertyTypeEnum.fromString(source);
        } catch (Exception e) {
            throw new InvalidEnumValueException("Invalid PropertyType enum value. Allowed values are: RENT, SALE.");
        }
    }
}

