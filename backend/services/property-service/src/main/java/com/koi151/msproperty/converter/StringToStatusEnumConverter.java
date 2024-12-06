package com.koi151.msproperty.converter;

import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.utils.StringUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStatusEnumConverter implements Converter<String, StatusEnum> {

    @Override
    public StatusEnum convert(String source) {
        if (!StringUtil.checkString(source)) {
            return null;
        }
        try {
            return StatusEnum.fromString(source);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + source, e);
        }
    }
}
