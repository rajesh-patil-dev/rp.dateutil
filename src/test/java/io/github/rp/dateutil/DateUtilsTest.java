package io.github.rp.dateutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Exercises {@link DateUtils} end-to-end using the exact usage pattern documented in the README,
 * confirming the facade delegates correctly to the specialized utility classes.
 */
class DateUtilsTest {

    @Test
    void facadeUsageExample_matchesReadme() {
        LocalDate date = DateUtils.parseDate("30-08-2026", "dd-MM-yyyy");
        assertEquals(LocalDate.of(2026, 8, 30), date);

        String formatted = DateUtils.formatDate(date, "dd-MM-yyyy");
        assertEquals("30-08-2026", formatted);

        LocalDate nextWeek = DateUtils.addDays(date, 7);
        assertEquals(LocalDate.of(2026, 9, 6), nextWeek);

        boolean weekend = DateUtils.isWeekend(date);
        assertTrue(weekend); // 2026-08-30 is a Sunday
    }

    @Test
    void calendarGetters() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals(DayOfWeek.SUNDAY, DateUtils.getDayOfWeek(date));
        assertEquals(30, DateUtils.getDayOfMonth(date));
        assertEquals(8, DateUtils.getMonth(date));
        assertEquals(2026, DateUtils.getYear(date));
        assertEquals(31, DateUtils.getDaysInMonth(date));
        assertFalse(DateUtils.isLeapYear(date));
        assertTrue(DateUtils.isLeapYear(2024));
    }

    @Test
    void differenceAndComparisonDelegation() {
        LocalDate start = LocalDate.of(2026, 8, 1);
        LocalDate end = LocalDate.of(2026, 8, 31);
        assertEquals(30, DateUtils.daysBetween(start, end));
        assertTrue(DateUtils.isBefore(start, end));
        assertTrue(DateUtils.isAfter(end, start));
    }

    @Test
    void startOfDay_endOfDay_delegation() {
        LocalDate date = LocalDate.of(2026, 8, 30);
        assertEquals(date.atStartOfDay(), DateUtils.startOfDay(date));
        assertEquals(date.atTime(23, 59, 59, 999_999_999), DateUtils.endOfDay(date));
    }
}
