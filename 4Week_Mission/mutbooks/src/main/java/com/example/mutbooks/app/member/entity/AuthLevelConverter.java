package com.example.mutbooks.app.member.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * AttributeConverter X, Y
 * X : 엔티티의 속성에 대응하는 타입
 * Y : DB 에 대응하는 타입
 */
@Converter
public class AuthLevelConverter implements AttributeConverter<AuthLevel, Integer> {

    // Enum -> db 데이터(code:Integer)
    @Override
    public Integer convertToDatabaseColumn(AuthLevel attribute) {
        if(attribute == null)
            return null;

        return attribute.getCode();
    }

    // db 데이터(code:Integer) -> Enum
    @Override
    public AuthLevel convertToEntityAttribute(Integer dbData) {
        return AuthLevel.ofCode(dbData);
    }
}
