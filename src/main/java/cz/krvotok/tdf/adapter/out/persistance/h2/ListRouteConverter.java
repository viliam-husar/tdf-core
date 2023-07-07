package cz.krvotok.tdf.adapter.out.persistance.h2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.krvotok.tdf.domain.model.valueobject.Route;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter(autoApply = true)
public class ListRouteConverter implements AttributeConverter<List<Route>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Route> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list of Routes to JSON", e);
        }
    }

    @Override
    public List<Route> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Route>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to list of Routes", e);
        }
    }
}
