package de.qaware.oss.cloud.service.billing.integration;

import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;

/**
 * A custom JPA attribute converter to map from String to Postgres JSON columns.
 */
@Converter
public class JsonStringConverter implements AttributeConverter<String, PGobject> {
    @Override
    public PGobject convertToDatabaseColumn(String attribute) {
        PGobject po = new PGobject();
        po.setType("json");
        try {
            po.setValue(attribute);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return po;
    }

    @Override
    public String convertToEntityAttribute(PGobject dbData) {
        return dbData.getValue();
    }
}
