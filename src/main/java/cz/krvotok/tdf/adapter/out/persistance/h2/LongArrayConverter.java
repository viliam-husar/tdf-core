package cz.krvotok.tdf.adapter.out.persistance.h2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = true)
public class LongArrayConverter implements AttributeConverter<long[], String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(long[] attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list of Routes to JSON", e);
        }
    }

    @Override
    public long[] convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<long[]>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to list of Routes", e);
        }
    }
}
