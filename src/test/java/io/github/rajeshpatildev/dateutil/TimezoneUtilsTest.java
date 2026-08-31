package io.github.rajeshpatildev.dateutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class TimezoneUtilsTest {

    private static final ZoneId UTC = TimezoneUtils.UTC;
    private static final ZoneId KOLKATA = TimezoneUtils.ASIA_KOLKATA;
    private static final ZoneId NEW_YORK = TimezoneUtils.AMERICA_NEW_YORK;
    private static final ZoneId LONDON = TimezoneUtils.EUROPE_LONDON;

    @Test
    void convertZone_zonedDateTime_preservesInstant() {
        ZonedDateTime utc = ZonedDateTime.of(2026, 8, 30, 12, 0, 0, 0, UTC);
        ZonedDateTime converted = TimezoneUtils.convertZone(utc, KOLKATA);
        assertEquals(utc.toInstant(), converted.toInstant());
        assertEquals(LocalDateTime.of(2026, 8, 30, 17, 30, 0), converted.toLocalDateTime());
    }

    @Test
    void convertZone_localDateTime_sourceAndTarget() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 8, 30, 12, 0, 0);
        LocalDateTime result = TimezoneUtils.convertZone(dateTime, UTC, KOLKATA);
        assertEquals(LocalDateTime.of(2026, 8, 30, 17, 30, 0), result);
    }

    @Test
    void assignZone_keepsWallClockButChangesInstant() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 8, 30, 12, 0, 0);
        ZonedDateTime utcAssigned = TimezoneUtils.assignZone(dateTime, UTC);
        ZonedDateTime kolkataAssigned = TimezoneUtils.assignZone(dateTime, KOLKATA);
        assertEquals(dateTime, utcAssigned.toLocalDateTime());
        assertEquals(dateTime, kolkataAssigned.toLocalDateTime());
        // Same wall-clock time, different zones -> different instants.
        assertNotEquals(utcAssigned.toInstant(), kolkataAssigned.toInstant());
    }

    @Test
    void convertZone_vs_assignZone_areDistinctOperations() {
        ZonedDateTime utc = ZonedDateTime.of(2026, 8, 30, 12, 0, 0, 0, UTC);
        // convertZone: same instant, different wall clock.
        ZonedDateTime converted = TimezoneUtils.convertZone(utc, KOLKATA);
        assertEquals(utc.toInstant(), converted.toInstant());

        // assignZone: same wall clock, different instant.
        ZonedDateTime assigned = TimezoneUtils.assignZone(utc.toLocalDateTime(), KOLKATA);
        assertEquals(utc.toLocalDateTime(), assigned.toLocalDateTime());
    }

    @Test
    void startOfDay_localDate_noZone() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals(LocalDateTime.of(2026, 8, 30, 0, 0, 0), TimezoneUtils.startOfDay(date));
    }

    @Test
    void endOfDay_localDate_noZone() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals(LocalDateTime.of(2026, 8, 30, 23, 59, 59, 999_999_999), TimezoneUtils.endOfDay(date));
    }

    @Test
    void startOfDay_withZone() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        ZonedDateTime result = TimezoneUtils.startOfDay(date, KOLKATA);
        assertEquals(LocalTime.MIDNIGHT, result.toLocalTime());
        assertEquals(KOLKATA, result.getZone());
    }

    @Test
    void endOfDay_withZone_isOneNanoBeforeNextDay() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        ZonedDateTime endOfDay = TimezoneUtils.endOfDay(date, UTC);
        ZonedDateTime startOfNextDay = TimezoneUtils.startOfDay(date.plusDays(1), UTC);
        assertEquals(startOfNextDay.toInstant().minusNanos(1), endOfDay.toInstant());
    }

    @Test
    void endOfDay_withZone_dstSpringForward_dayIsShorterThan24Hours() {
        // America/New_York springs forward on 2026-03-08: the day has only 23 hours.
        LocalDate dstDay = LocalDate.of(2026, 3, 8);
        ZonedDateTime start = TimezoneUtils.startOfDay(dstDay, NEW_YORK);
        ZonedDateTime end = TimezoneUtils.endOfDay(dstDay, NEW_YORK);
        long secondsInDay = java.time.Duration.between(start.toInstant(), end.toInstant()).plusNanos(1).toSeconds();
        assertEquals(23 * 3600, secondsInDay);
    }

    @Test
    void endOfDay_withZone_dstFallBack_dayIsLongerThan24Hours() {
        // America/New_York falls back on 2026-11-01: the day has 25 hours.
        LocalDate dstDay = LocalDate.of(2026, 11, 1);
        ZonedDateTime start = TimezoneUtils.startOfDay(dstDay, NEW_YORK);
        ZonedDateTime end = TimezoneUtils.endOfDay(dstDay, NEW_YORK);
        long secondsInDay = java.time.Duration.between(start.toInstant(), end.toInstant()).plusNanos(1).toSeconds();
        assertEquals(25 * 3600, secondsInDay);
    }

    @Test
    void londonZone_dstTransition_startOfDayIsSafe() {
        // Europe/London springs forward on 2026-03-29.
        LocalDate dstDay = LocalDate.of(2026, 3, 29);
        ZonedDateTime start = TimezoneUtils.startOfDay(dstDay, LONDON);
        assertEquals(LocalTime.MIDNIGHT, start.toLocalTime());
    }

    @Test
    void nullArguments_throw() {
        assertThrows(NullPointerException.class, () -> TimezoneUtils.startOfDay(null, UTC));
        assertThrows(NullPointerException.class, () -> TimezoneUtils.startOfDay(LocalDate.now(), null));
        assertThrows(NullPointerException.class, () -> TimezoneUtils.convertZone((ZonedDateTime) null, UTC));
    }

    @Test
    void zoneConstants_resolveToExpectedIds() {
        assertEquals("UTC", UTC.getId());
        assertEquals("Asia/Kolkata", KOLKATA.getId());
        assertEquals("America/New_York", NEW_YORK.getId());
        assertEquals("Europe/London", LONDON.getId());
    }
}
