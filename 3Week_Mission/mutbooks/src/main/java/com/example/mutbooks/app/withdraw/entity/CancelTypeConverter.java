package com.example.mutbooks.app.withdraw.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * AttributeConverter X, Y
 * X : 엔티티의 속성에 대응하는 타입
 * Y : DB 에 대응하는 타입
 */
@Converter
public class CancelTypeConverter implements AttributeConverter<CancelType, String> {

    // Enum -> db 데이터(value:String)
    @Override
    public String convertToDatabaseColumn(CancelType attribute) {
        if(attribute == null)
            return null;

        return attribute.getValue();
    }

    // db 데이터(value:String) -> Enum
    @Override
    public CancelType convertToEntityAttribute(String dbData) {
        return CancelType.ofCode(dbData);
    }
}
