package com.team3.findex.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Converter
public class UtcInstantConverter implements AttributeConverter<Instant, Timestamp> {

    // Java(Instant) -> DB(Timestamp) 저장 시
    @Override
    public Timestamp convertToDatabaseColumn(Instant attribute) {
        if (attribute == null) {
            return null;
        }
        return Timestamp.valueOf(LocalDateTime.ofInstant(attribute, ZoneId.of("UTC")));
    }

    // DB(Timestamp) -> Java(Instant) 조회 시 (fetch 발생 지점)
    @Override
    public Instant convertToEntityAttribute(Timestamp dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant();
    }
}
