package com.wonnapark.wnpserver.webtoon.infrastructure;

import com.wonnapark.wnpserver.webtoon.AgeRating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AgeRatingConverter implements AttributeConverter<AgeRating, String> {

    @Override
    public String convertToDatabaseColumn(AgeRating attribute) {
        if (attribute == null) return null;

        return attribute.getRatingName();
    }

    @Override
    public AgeRating convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        return AgeRating.from(dbData);
    }

}
