//package com.koi151.msproperties.service.converter;
//
//import customExceptions.InvalidEnumValueException;
//import jakarta.persistence.EnumType;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CaseInsensitiveEnumConverter implements Converter<String, EnumType> {
//
//    @Override
//    public EnumType convert(String source) {
//        try {
//            return EnumType.valueOf(source.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new InvalidEnumValueException("Invalid enum value: " + source);
//        }
//    }
//}