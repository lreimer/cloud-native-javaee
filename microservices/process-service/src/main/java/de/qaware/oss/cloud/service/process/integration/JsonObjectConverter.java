package de.qaware.oss.cloud.service.process.integration;

import org.postgresql.util.PGobject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;

/**
 * A custom JPA attribute converter to map from String to Postgres JSON columns.
 */
@Converter
public class JsonObjectConverter implements AttributeConverter<JsonObject, PGobject> {
    @Override
    public PGobject convertToDatabaseColumn(JsonObject attribute) {
        PGobject po = new PGobject();
        po.setType("json");
        try {
            StringWriter stringWriter = new StringWriter();
            try (JsonWriter writer = Json.createWriter(stringWriter)) {
                writer.writeObject(attribute);
            }
            po.setValue(stringWriter.toString());
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return po;
    }

    @Override
    public JsonObject convertToEntityAttribute(PGobject dbData) {
        try (JsonReader reader = Json.createReader(new StringReader(dbData.getValue()))) {
            return reader.readObject();
        }
    }
}
