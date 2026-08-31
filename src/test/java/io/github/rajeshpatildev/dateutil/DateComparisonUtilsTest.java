package io.github.rajeshpatildev.dateutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class DateComparisonUtilsTest {

    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final ZoneId KOLKATA = ZoneId.of("Asia/Kolkata");
    private static final ZoneId NEW_YORK = ZoneId.of("America/New_York");
    private static final ZoneId LONDON = ZoneId.of("Europe/London");

    @Test
    void isBefore_isAfter_isEqual_localDate() {
        LocalDate d1 = LocalDate.of(2026, 8, 30);
        LocalDate d2 = LocalDate.of(2026, 9, 1);
        assertTrue(DateComparisonUtils.isBefore(d1, d2));
        assertTrue(DateComparisonUtils.isAfter(d2, d1));
        assertTrue(DateComparisonUtils.isEqual(d1, LocalDate.of(2026, 8, 30)));
        assertFalse(DateComparisonUtils.isEqual(d1, d2));
    }

    @Test
    void isBefore_isAfter_isEqual_instant() {
        Instant i1 = Instant.parse("2026-08-30T10:00:00Z");
        Instant i2 = Instant.parse("2026-08-30T11:00:00Z");
        assertTrue(DateComparisonUtils.isBefore(i1, i2));
        assertTrue(DateComparisonUtils.isAfter(i2, i1));
        assertTrue(DateComparisonUtils.isEqual(i1, Instant.parse("2026-08-30T10:00:00Z")));
    }

    @Test
    void isBefore_isAfter_isEqual_zonedDateTime_comparesInstant() {
        ZonedDateTime utc = ZonedDateTime.of(2026, 8, 30, 15, 0, 0, 0, UTC);
        ZonedDateTime kolkataSameInstant = utc.withZoneSameInstant(KOLKATA);
        assertTrue(DateComparisonUtils.isEqual(utc, kolkataSameInstant));
        ZonedDateTime later = utc.plusHours(1);
        assertTrue(DateComparisonUtils.isBefore(utc, later));
        assertTrue(DateComparisonUtils.isAfter(later, utc));
    }

    @Test
    void nullArguments_throw() {
        assertThrows(NullPointerException.class, () -> DateComparisonUtils.isBefore((LocalDate) null, LocalDate.now()));
        assertThrows(NullPointerException.class, () -> DateComparisonUtils.isAfter(LocalDate.now(), (LocalDate) null));
    }

    @Test
    void isToday_isPast_isFuture_withZone() {
        LocalDate today = LocalDate.now(UTC);
        assertTrue(DateComparisonUtils.isToday(today, UTC));
        assertTrue(DateComparisonUtils.isPast(today.minusDays(1), UTC));
        assertTrue(DateComparisonUtils.isFuture(today.plusDays(1), UTC));
        assertFalse(DateComparisonUtils.isPast(today, UTC));
        assertFalse(DateComparisonUtils.isFuture(today, UTC));
    }

    @Test
    void isPast_isFuture_instant() {
        assertTrue(DateComparisonUtils.isPast(Instant.EPOCH));
        assertTrue(DateComparisonUtils.isFuture(Instant.now().plusSeconds(3600)));
    }

    @Test
    void daysBetween_signed() {
        LocalDate start = LocalDate.of(2026, 8, 1);
        LocalDate end = LocalDate.of(2026, 8, 31);
        assertEquals(30, DateComparisonUtils.daysBetween(start, end));
        assertEquals(-30, DateComparisonUtils.daysBetween(end, start));
    }

    @Test
    void weeksBetween_truncates() {
        LocalDate start = LocalDate.of(2026, 8, 1);
        LocalDate end = LocalDate.of(2026, 8, 20);
        // 19 days = 2 whole weeks, remainder truncated
        assertEquals(2, DateComparisonUtils.weeksBetween(start, end));
    }

    @Test
    void monthsBetween_partialMonthNotCounted() {
        LocalDate jan31 = LocalDate.of(2026, 1, 31);
        LocalDate feb28 = LocalDate.of(2026, 2, 28);
        assertEquals(0, DateComparisonUtils.monthsBetween(jan31, feb28));
        LocalDate mar1 = LocalDate.of(2026, 3, 1);
        assertEquals(1, DateComparisonUtils.monthsBetween(jan31, mar1));
    }

    @Test
    void yearsBetween_basic() {
        assertEquals(5, DateComparisonUtils.yearsBetween(LocalDate.of(2020, 8, 30), LocalDate.of(2025, 8, 30)));
        assertEquals(4, DateComparisonUtils.yearsBetween(LocalDate.of(2020, 8, 30), LocalDate.of(2025, 8, 29)));
    }

    @Test
    void hoursMinutesSecondsBetween_localDateTime() {
        LocalDateTime start = LocalDateTime.of(2026, 8, 30, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 8, 30, 13, 30, 45);
        assertEquals(3, DateComparisonUtils.hoursBetween(start, end));
        assertEquals(210, DateComparisonUtils.minutesBetween(start, end));
        assertEquals(12645, DateComparisonUtils.secondsBetween(start, end));
    }

    @Test
    void hoursMinutesSecondsBetween_instant() {
        Instant start = Instant.parse("2026-08-30T10:00:00Z");
        Instant end = Instant.parse("2026-08-30T12:00:00Z");
        assertEquals(2, DateComparisonUtils.hoursBetween(start, end));
        assertEquals(120, DateComparisonUtils.minutesBetween(start, end));
        assertEquals(7200, DateComparisonUtils.secondsBetween(start, end));
    }

    @Test
    void isWeekend_isWeekday() {
        LocalDate saturday = LocalDate.of(2026, 8, 29);
        LocalDate sunday = LocalDate.of(2026, 8, 30);
        LocalDate monday = LocalDate.of(2026, 8, 31);
        assertTrue(DateComparisonUtils.isWeekend(saturday));
        assertTrue(DateComparisonUtils.isWeekend(sunday));
        assertFalse(DateComparisonUtils.isWeekend(monday));
        assertTrue(DateComparisonUtils.isWeekday(monday));
        assertFalse(DateComparisonUtils.isWeekday(saturday));
    }

    @Test
    void isLeapYear_variousYears() {
        assertTrue(DateComparisonUtils.isLeapYear(2024));
        assertFalse(DateComparisonUtils.isLeapYear(2026));
        assertFalse(DateComparisonUtils.isLeapYear(1900)); // divisible by 100 but not 400
        assertTrue(DateComparisonUtils.isLeapYear(2000)); // divisible by 400
        assertTrue(DateComparisonUtils.isLeapYear(LocalDate.of(2024, 2, 29)));
        assertFalse(DateComparisonUtils.isLeapYear(LocalDate.of(2026, 2, 28)));
    }

    @Test
    void timezoneAware_todayCanDifferAcrossZones() {
        // Choose an instant that is late evening in Kolkata but still the previous day in
        // New York, to demonstrate that "today" is zone-dependent.
        Instant lateEveningKolkata = Instant.parse("2026-08-30T19:00:00Z"); // 00:30 IST on Aug 31
        LocalDate kolkataDate = lateEveningKolkata.atZone(KOLKATA).toLocalDate();
        LocalDate newYorkDate = lateEveningKolkata.atZone(NEW_YORK).toLocalDate();
        assertTrue(DateComparisonUtils.isAfter(kolkataDate, newYorkDate));
    }

    @Test
    void londonZone_usableForComparisons() {
        LocalDate today = LocalDate.now(LONDON);
        assertTrue(DateComparisonUtils.isToday(today, LONDON));
    }
}
