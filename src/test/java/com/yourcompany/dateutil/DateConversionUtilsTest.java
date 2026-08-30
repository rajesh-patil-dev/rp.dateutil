package com.yourcompany.dateutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import org.junit.jupiter.api.Test;

class DateConversionUtilsTest {

    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final ZoneId KOLKATA = ZoneId.of("Asia/Kolkata");

    @Test
    void toLocalDate_fromDate_withZone() {
        Instant instant = Instant.parse("2026-08-30T20:00:00Z");
        Date date = Date.from(instant);
        assertEquals(LocalDate.of(2026, 8, 30), DateConversionUtils.toLocalDate(date, UTC));
        // 20:00 UTC on Aug 30 is 01:30 Aug 31 in IST (UTC+5:30)
        assertEquals(LocalDate.of(2026, 8, 31), DateConversionUtils.toLocalDate(date, KOLKATA));
    }

    @Test
    void toLocalDate_nullInput_returnsNull() {
        assertNull(DateConversionUtils.toLocalDate((Date) null, UTC));
    }

    @Test
    void toLocalDate_nullZone_throws() {
        assertThrows(NullPointerException.class, () -> DateConversionUtils.toLocalDate(new Date(), null));
    }

    @Test
    void toLocalDateTime_fromDate() {
        Instant instant = Instant.parse("2026-08-30T12:30:00Z");
        Date date = Date.from(instant);
        assertEquals(LocalDateTime.of(2026, 8, 30, 12, 30, 0), DateConversionUtils.toLocalDateTime(date, UTC));
    }

    @Test
    void toInstant_fromDate() {
        Instant instant = Instant.parse("2026-08-30T12:30:00Z");
        Date date = Date.from(instant);
        assertEquals(instant, DateConversionUtils.toInstant(date));
    }

    @Test
    void toInstant_nullInput_returnsNull() {
        assertNull(DateConversionUtils.toInstant((Date) null));
    }

    @Test
    void toDate_fromInstant() {
        Instant instant = Instant.parse("2026-08-30T12:30:00Z");
        assertEquals(Date.from(instant), DateConversionUtils.toDate(instant));
    }

    @Test
    void toDate_fromInstant_nullInput_returnsNull() {
        assertNull(DateConversionUtils.toDate((Instant) null));
    }

    @Test
    void toDate_fromLocalDate_withZone() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        Date result = DateConversionUtils.toDate(date, UTC);
        assertEquals(Instant.parse("2026-08-30T00:00:00Z"), result.toInstant());
    }

    @Test
    void toDate_fromLocalDateTime_withZone() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 8, 30, 10, 15, 0);
        Date result = DateConversionUtils.toDate(dateTime, UTC);
        assertEquals(Instant.parse("2026-08-30T10:15:00Z"), result.toInstant());
    }

    @Test
    void toDate_fromZonedDateTime() {
        ZonedDateTime zdt = ZonedDateTime.of(2026, 8, 30, 10, 15, 0, 0, UTC);
        assertEquals(Date.from(zdt.toInstant()), DateConversionUtils.toDate(zdt));
    }

    @Test
    void sqlDate_roundTrip() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        java.sql.Date sqlDate = DateConversionUtils.toSqlDate(date);
        assertEquals(date, sqlDate.toLocalDate());
        assertEquals(date, DateConversionUtils.toLocalDate(sqlDate));
    }

    @Test
    void sqlDate_nullInput_returnsNull() {
        assertNull(DateConversionUtils.toSqlDate((LocalDate) null));
        assertNull(DateConversionUtils.toLocalDate((java.sql.Date) null));
    }

    @Test
    void instantConversions_withZone() {
        Instant instant = Instant.parse("2026-08-30T20:00:00Z");
        assertEquals(LocalDate.of(2026, 8, 31), DateConversionUtils.toLocalDate(instant, KOLKATA));
        assertEquals(LocalDateTime.of(2026, 8, 31, 1, 30), DateConversionUtils.toLocalDateTime(instant, KOLKATA));
        assertEquals(ZonedDateTime.ofInstant(instant, KOLKATA), DateConversionUtils.toZonedDateTime(instant, KOLKATA));
    }

    @Test
    void toInstant_fromLocalDateTime_withZone() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 8, 30, 10, 0, 0);
        Instant instant = DateConversionUtils.toInstant(dateTime, UTC);
        assertEquals(Instant.parse("2026-08-30T10:00:00Z"), instant);
    }

    @Test
    void toInstant_fromLocalDate_withZone_isStartOfDay() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals(Instant.parse("2026-08-30T00:00:00Z"), DateConversionUtils.toInstant(date, UTC));
    }

    @Test
    void offsetDateTime_roundTrip() {
        Instant instant = Instant.parse("2026-08-30T10:00:00Z");
        OffsetDateTime odt = DateConversionUtils.toOffsetDateTime(instant, ZoneOffset.UTC);
        assertEquals(instant, DateConversionUtils.toInstant(odt));
    }

    @Test
    void zonedDateTime_offsetDateTime_roundTrip() {
        ZonedDateTime zdt = ZonedDateTime.of(2026, 8, 30, 10, 0, 0, 0, UTC);
        OffsetDateTime odt = DateConversionUtils.toOffsetDateTime(zdt);
        assertEquals(zdt.toInstant(), DateConversionUtils.toZonedDateTime(odt).toInstant());
    }

    @Test
    void offsetTime_conversions() {
        LocalTime time = LocalTime.of(10, 30);
        OffsetTime offsetTime = DateConversionUtils.toOffsetTime(time, ZoneOffset.UTC);
        assertEquals(time, offsetTime.toLocalTime());
        assertEquals(time, DateConversionUtils.toLocalTime(offsetTime));
    }

    @Test
    void epochMillis_roundTrip() {
        Instant instant = Instant.parse("2026-08-30T10:00:00.123Z");
        long millis = DateConversionUtils.toEpochMillis(instant);
        assertEquals(instant, DateConversionUtils.fromEpochMillis(millis));
    }

    @Test
    void epochSeconds_roundTrip() {
        Instant instant = Instant.parse("2026-08-30T10:00:00Z");
        long seconds = DateConversionUtils.toEpochSeconds(instant);
        assertEquals(instant, DateConversionUtils.fromEpochSeconds(seconds));
    }

    @Test
    void epochMillis_negativeValue_beforeUnixEpoch() {
        // 1969-12-31T23:59:59Z is one second before the epoch.
        long epochMillis = -1000L;
        Instant instant = DateConversionUtils.fromEpochMillis(epochMillis);
        assertEquals(Instant.parse("1969-12-31T23:59:59Z"), instant);
        assertEquals(epochMillis, DateConversionUtils.toEpochMillis(instant));
    }

    @Test
    void epochSeconds_negativeValue_beforeUnixEpoch() {
        long epochSeconds = -86400L;
        Instant instant = DateConversionUtils.fromEpochSeconds(epochSeconds);
        assertEquals(Instant.parse("1969-12-31T00:00:00Z"), instant);
        assertEquals(epochSeconds, DateConversionUtils.toEpochSeconds(instant));
    }

    @Test
    void toEpochMillis_nullInstant_throws() {
        assertThrows(NullPointerException.class, () -> DateConversionUtils.toEpochMillis(null));
    }

    @Test
    void epochMillis_withZone_roundTrip() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 8, 30, 10, 0, 0);
        long millis = DateConversionUtils.toEpochMillis(dateTime, UTC);
        assertEquals(dateTime, DateConversionUtils.fromEpochMillis(millis, UTC));
    }
}
