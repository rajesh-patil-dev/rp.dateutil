package io.github.rajeshpatildev.dateutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;

class DateFormattingUtilsTest {

    private static final ZoneId KOLKATA = ZoneId.of("Asia/Kolkata");

    @Test
    void formatDate_standardPattern() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals("30-08-2026", DateFormattingUtils.formatDate(date, "dd-MM-yyyy"));
        assertEquals("2026-08-30", DateFormattingUtils.formatDate(date, DateFormattingUtils.PATTERN_ISO_DATE));
        assertEquals("08/30/2026", DateFormattingUtils.formatDate(date, DateFormattingUtils.PATTERN_DATE_MDY_SLASH));
    }

    @Test
    void formatDate_nullDate_returnsNull() {
        assertNull(DateFormattingUtils.formatDate(null, "dd-MM-yyyy"));
    }

    @Test
    void formatDate_nullPattern_throws() {
        assertThrows(NullPointerException.class,
                () -> DateFormattingUtils.formatDate(LocalDate.now(), (String) null));
    }

    @Test
    void formatDateTime_standardPattern() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 8, 30, 14, 5, 9);
        assertEquals("2026-08-30 14:05:09",
                DateFormattingUtils.formatDateTime(dateTime, DateFormattingUtils.PATTERN_DATE_TIME));
    }

    @Test
    void formatDateTime_nullInput_returnsNull() {
        assertNull(DateFormattingUtils.formatDateTime(null, DateFormattingUtils.PATTERN_DATE_TIME));
    }

    @Test
    void formatZonedDateTime_customPattern() {
        ZonedDateTime zdt = ZonedDateTime.of(2026, 8, 30, 14, 5, 9, 0, KOLKATA);
        String formatted = DateFormattingUtils.formatZonedDateTime(zdt, "yyyy-MM-dd HH:mm:ss z");
        assertEquals("2026-08-30 14:05:09 IST", formatted);
    }

    @Test
    void formatZonedDateTime_nullInput_returnsNull() {
        assertNull(DateFormattingUtils.formatZonedDateTime(null, "yyyy-MM-dd"));
    }

    @Test
    void parseDate_validInput() {
        LocalDate date = DateFormattingUtils.parseDate("30-08-2026", "dd-MM-yyyy");
        assertEquals(LocalDate.of(2026, 8, 30), date);
    }

    @Test
    void parseDate_differentPatterns() {
        assertEquals(LocalDate.of(2026, 8, 30), DateFormattingUtils.parseDate("2026-08-30", DateFormattingUtils.PATTERN_ISO_DATE));
        assertEquals(LocalDate.of(2026, 8, 30), DateFormattingUtils.parseDate("08/30/2026", DateFormattingUtils.PATTERN_DATE_MDY_SLASH));
    }

    @Test
    void parseDate_invalidInput_throwsDateTimeParseException() {
        assertThrows(DateTimeParseException.class, () -> DateFormattingUtils.parseDate("not-a-date", "dd-MM-yyyy"));
    }

    @Test
    void parseDate_invalidCalendarDate_throws() {
        // 30th February does not exist, even though it matches the pattern syntactically.
        assertThrows(DateTimeParseException.class, () -> DateFormattingUtils.parseDate("30-02-2026", "dd-MM-yyyy"));
    }

    @Test
    void parseDate_nullInput_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> DateFormattingUtils.parseDate(null, "dd-MM-yyyy"));
    }

    @Test
    void parseDate_emptyInput_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> DateFormattingUtils.parseDate("", "dd-MM-yyyy"));
        assertThrows(IllegalArgumentException.class, () -> DateFormattingUtils.parseDate("   ", "dd-MM-yyyy"));
    }

    @Test
    void parseDate_nullPattern_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> DateFormattingUtils.parseDate("30-08-2026", (String) null));
    }

    @Test
    void parseDateTime_validInput() {
        LocalDateTime dateTime = DateFormattingUtils.parseDateTime("2026-08-30 14:05:09", DateFormattingUtils.PATTERN_DATE_TIME);
        assertEquals(LocalDateTime.of(2026, 8, 30, 14, 5, 9), dateTime);
    }

    @Test
    void parseDateTime_nullInput_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> DateFormattingUtils.parseDateTime(null, DateFormattingUtils.PATTERN_DATE_TIME));
    }

    @Test
    void parseZonedDateTime_assignsGivenZone() {
        ZonedDateTime zdt = DateFormattingUtils.parseZonedDateTime("2026-08-30 14:05:09",
                DateFormattingUtils.PATTERN_DATE_TIME, KOLKATA);
        assertEquals(LocalDateTime.of(2026, 8, 30, 14, 5, 9), zdt.toLocalDateTime());
        assertEquals(KOLKATA, zdt.getZone());
    }

    @Test
    void parseZonedDateTime_nullZone_throws() {
        assertThrows(NullPointerException.class, () ->
                DateFormattingUtils.parseZonedDateTime("2026-08-30 14:05:09", DateFormattingUtils.PATTERN_DATE_TIME, null));
    }

    @Test
    void formatterFor_cachesAndReusesInstances() {
        assertSame(DateFormattingUtils.formatterFor("dd-MM-yyyy"), DateFormattingUtils.formatterFor("dd-MM-yyyy"));
    }

    @Test
    void isoFormatterConstants_areUsable() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals("2026-08-30", DateFormattingUtils.formatDate(date, DateFormattingUtils.FORMATTER_ISO_DATE));
    }
}
