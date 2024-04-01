package com.memori.memori_service.mappers;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DateTimeMapper {
    public String offsetDateTimeToString(OffsetDateTime value) {
        ZonedDateTime utcDateTime = value.atZoneSameInstant(ZoneOffset.UTC);
        return utcDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public OffsetDateTime asOffsetDateTime(String value) {
        List<DateTimeFormatter> acceptedFormats = Arrays.asList(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                DateTimeFormatter.ISO_INSTANT);
        for (DateTimeFormatter formatter : acceptedFormats) {
            try {
                formatter.parse(value);
                return OffsetDateTime.parse(value);
            } catch (DateTimeParseException e) {
            }
        }
        throw new DateTimeParseException(
                "The date time can only either in DateTimeFormatter.ISO_OFFSET_DATE_TIME or DateTimeFormatter.ISO_INSTANT",
                value, 0);
    }

    public String instantToString(Instant value) {
        return value.toString();
    }

    public Instant asInstant(String value) {
        List<DateTimeFormatter> acceptedFormats = Arrays.asList(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                DateTimeFormatter.ISO_INSTANT);
        for (DateTimeFormatter formatter : acceptedFormats) {
            try {
                formatter.parse(value);
                return Instant.parse(value);
            } catch (DateTimeParseException e) {
            }
        }
        throw new DateTimeParseException(
                "The date time can only either in DateTimeFormatter.ISO_OFFSET_DATE_TIME or DateTimeFormatter.ISO_INSTANT",
                value, 0);
    }
}
