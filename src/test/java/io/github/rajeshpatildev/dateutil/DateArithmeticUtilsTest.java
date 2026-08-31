package io.github.rajeshpatildev.dateutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class DateArithmeticUtilsTest {

    private static final ZoneId NEW_YORK = ZoneId.of("America/New_York");

    @Test
    void addDays_and_subtractDays() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals(LocalDate.of(2026, 9, 6), DateArithmeticUtils.addDays(date, 7));
        assertEquals(LocalDate.of(2026, 8, 23), DateArithmeticUtils.subtractDays(date, 7));
    }

    @Test
    void addWeeks_and_subtractWeeks() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals(LocalDate.of(2026, 9, 13), DateArithmeticUtils.addWeeks(date, 2));
        assertEquals(LocalDate.of(2026, 8, 16), DateArithmeticUtils.subtractWeeks(date, 2));
    }

    @Test
    void addMonths_monthEndBehavior() {
        // January 31 + 1 month must clamp to the last valid day of February.
        LocalDate jan31 = LocalDate.of(2026, 1, 31);
        assertEquals(LocalDate.of(2026, 2, 28), DateArithmeticUtils.addMonths(jan31, 1));
    }

    @Test
    void addMonths_leapYearBehavior() {
        LocalDate jan31LeapYear = LocalDate.of(2024, 1, 31);
        assertEquals(LocalDate.of(2024, 2, 29), DateArithmeticUtils.addMonths(jan31LeapYear, 1));
    }

    @Test
    void subtractMonths_monthEndBehavior() {
        LocalDate mar31 = LocalDate.of(2026, 3, 31);
        assertEquals(LocalDate.of(2026, 2, 28), DateArithmeticUtils.subtractMonths(mar31, 1));
    }

    @Test
    void addYears_leapDayAdjustsToNonLeapYear() {
        LocalDate feb29 = LocalDate.of(2024, 2, 29);
        assertEquals(LocalDate.of(2025, 2, 28), DateArithmeticUtils.addYears(feb29, 1));
    }

    @Test
    void subtractYears_basic() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals(LocalDate.of(2020, 8, 30), DateArithmeticUtils.subtractYears(date, 6));
    }

    @Test
    void localDateTime_hoursMinutesSeconds() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 8, 30, 10, 0, 0);
        assertEquals(LocalDateTime.of(2026, 8, 30, 13, 0, 0), DateArithmeticUtils.addHours(dateTime, 3));
        assertEquals(LocalDateTime.of(2026, 8, 30, 7, 0, 0), DateArithmeticUtils.subtractHours(dateTime, 3));
        assertEquals(LocalDateTime.of(2026, 8, 30, 10, 30, 0), DateArithmeticUtils.addMinutes(dateTime, 30));
        assertEquals(LocalDateTime.of(2026, 8, 30, 9, 30, 0), DateArithmeticUtils.subtractMinutes(dateTime, 30));
        assertEquals(LocalDateTime.of(2026, 8, 30, 10, 0, 45), DateArithmeticUtils.addSeconds(dateTime, 45));
        assertEquals(LocalDateTime.of(2026, 8, 30, 9, 59, 15), DateArithmeticUtils.subtractSeconds(dateTime, 45));
    }

    @Test
    void localDateTime_dayWeekMonthYear() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 1, 31, 10, 0, 0);
        assertEquals(LocalDateTime.of(2026, 2, 28, 10, 0, 0), DateArithmeticUtils.addMonths(dateTime, 1));
        assertEquals(LocalDateTime.of(2026, 2, 7, 10, 0, 0), DateArithmeticUtils.addWeeks(dateTime, 1));
        assertEquals(LocalDateTime.of(2027, 1, 31, 10, 0, 0), DateArithmeticUtils.addYears(dateTime, 1));
        assertEquals(LocalDateTime.of(2026, 1, 30, 10, 0, 0), DateArithmeticUtils.subtractDays(dateTime, 1));
    }

    @Test
    void instant_arithmetic() {
        Instant instant = Instant.parse("2026-08-30T10:00:00Z");
        assertEquals(Instant.parse("2026-08-30T13:00:00Z"), DateArithmeticUtils.addHours(instant, 3));
        assertEquals(Instant.parse("2026-08-30T07:00:00Z"), DateArithmeticUtils.subtractHours(instant, 3));
        assertEquals(Instant.parse("2026-08-30T10:30:00Z"), DateArithmeticUtils.addMinutes(instant, 30));
        assertEquals(Instant.parse("2026-08-30T09:30:00Z"), DateArithmeticUtils.subtractMinutes(instant, 30));
        assertEquals(Instant.parse("2026-08-30T10:00:45Z"), DateArithmeticUtils.addSeconds(instant, 45));
        assertEquals(Instant.parse("2026-08-30T09:59:15Z"), DateArithmeticUtils.subtractSeconds(instant, 45));
    }

    @Test
    void zonedDateTime_addHours_acrossDstSpringForward() {
        // America/New_York springs forward on 2026-03-08 at 02:00 local -> 03:00 local.
        ZonedDateTime beforeTransition = ZonedDateTime.of(2026, 3, 8, 1, 0, 0, 0, NEW_YORK);
        ZonedDateTime result = DateArithmeticUtils.addHours(beforeTransition, 2);
        // Wall-clock advances by 2 hours of elapsed time, but the local clock jumps the gap,
        // landing at 4:00 local (1:00 + 1h to reach 2:00 skipped straight to 3:00, + 1h = 4:00).
        assertEquals(LocalDateTime.of(2026, 3, 8, 4, 0, 0), result.toLocalDateTime());
        assertEquals(beforeTransition.toInstant().plusSeconds(2 * 3600), result.toInstant());
    }

    @Test
    void zonedDateTime_addDays_preservesLocalWallClockAcrossDst() {
        ZonedDateTime beforeTransition = ZonedDateTime.of(2026, 3, 7, 9, 30, 0, 0, NEW_YORK);
        ZonedDateTime result = DateArithmeticUtils.addDays(beforeTransition, 1);
        // plusDays preserves local time-of-day where possible, even across a DST boundary.
        assertEquals(LocalDateTime.of(2026, 3, 8, 9, 30, 0), result.toLocalDateTime());
    }

    @Test
    void nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> DateArithmeticUtils.addDays((LocalDate) null, 1));
        assertThrows(NullPointerException.class, () -> DateArithmeticUtils.addHours((LocalDateTime) null, 1));
        assertThrows(NullPointerException.class, () -> DateArithmeticUtils.addSeconds((Instant) null, 1));
    }
}
