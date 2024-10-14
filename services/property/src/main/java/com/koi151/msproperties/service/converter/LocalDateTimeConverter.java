//package com.koi151.msproperties.service.converter;
//
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//
//@Converter // marked this class is an AttributeConverter of JPA, inform JPA that this class will be used for auto convert between 2 data types
//public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> { // AttributeConverter<Source data type, target data type>
//
//    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    @Override // method is called when JPA needs to save a LocalDateTime value into the database.
//    public String convertToDatabaseColumn(LocalDateTime localDateTime) {
//        return localDateTime != null ? localDateTime.format(FORMATTER) : null;
//    }
//
//    @Override // method is called when JPA retrieves data from the DB and needs to convert it back into LocalDateTime
//    public LocalDateTime convertToEntityAttribute(String databaseValue) {
//        return databaseValue != null ? LocalDateTime.parse(databaseValue, FORMATTER) : null;
//    }
//}
