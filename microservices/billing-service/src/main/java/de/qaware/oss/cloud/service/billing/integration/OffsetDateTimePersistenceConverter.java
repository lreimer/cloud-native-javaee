package de.qaware.oss.cloud.service.billing.integration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Converts {@link OffsetDateTime} values to a database column entry in a portable way.
 * <p>
 * As SQL does not support date time types with time zone offsets natively we are forced to convert
 * the {@link OffsetDateTime} into a {@link String} when writing to the database and parsing
 * the {@link String} to an {@link OffsetDateTime} when reading from the database.
 * An example for such a column value would be "2007-12-03T10:15:30+01:00".
 * <p>
 * The implementation keeps {@code null} values that are written to or read from the database.
 * <p>
 * If value is read from the database that can not be converted to an {@link OffsetDateTime},
 * a {@link java.time.format.DateTimeParseException} is thrown which has to be handled by the application code.
 */
@Converter(autoApply = true)
public class OffsetDateTimePersistenceConverter implements AttributeConverter<OffsetDateTime, String> {

    @Override
    public String convertToDatabaseColumn(OffsetDateTime entityValue) {
        return Objects.toString(entityValue, null);
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        return OffsetDateTime.parse(databaseValue);
    }
}