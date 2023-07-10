package cz.krvotok.tdf.adapter.out.persistance.h2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.io.IOException;
import java.util.List;

import cz.krvotok.tdf.domain.model.valueobject.Restpoint;

@Converter(autoApply = true)
public class IntArrayConverter implements AttributeConverter<int[], String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(int[] attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list of Routes to JSON", e);
        }
    }

    @Override
    public int[] convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<int[]>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to list of Routes", e);
        }
    }
}
