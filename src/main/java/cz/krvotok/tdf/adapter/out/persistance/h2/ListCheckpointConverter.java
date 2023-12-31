package cz.krvotok.tdf.adapter.out.persistance.h2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.io.IOException;
import java.util.List;

import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;

@Converter(autoApply = true)
public class ListCheckpointConverter implements AttributeConverter<List<Checkpoint>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Checkpoint> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list of checkpoints to JSON", e);
        }
    }

    @Override
    public List<Checkpoint> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Checkpoint>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to list of checkpoints", e);
        }
    }
}