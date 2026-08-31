package io.github.rajeshpatildev.dateutil;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Convenience facade over {@link DateConversionUtils}, {@link DateFormattingUtils}, {@link
 * DateArithmeticUtils}, {@link DateComparisonUtils}, and {@link TimezoneUtils} for the most
 * commonly used date/time operations.
 *
 * <p>Every method on this class simply delegates to the corresponding specialized utility class;
 * no behavior or null-handling policy is duplicated or reimplemented here. Use this class for
 * everyday convenience, or call the specialized classes directly for the full API surface (for
 * example less common type conversions, or {@link java.time.ZonedDateTime} arithmetic).
 *
 * <p>Typical usage:
 * <pre>{@code
 * LocalDate date = DateUtils.parseDate("30-08-2026", "dd-MM-yyyy");
 * String formatted = DateUtils.formatDate(date, "dd-MM-yyyy");
 * LocalDate nextWeek = DateUtils.addDays(date, 7);
 * boolean weekend = DateUtils.isWeekend(date);
 * }</pre>
 *
 * <p>This class is stateless, thread-safe, and cannot be instantiated.
 */
public final class DateUtils {

    private DateUtils() {
        throw new AssertionError("DateUtils is a utility class and cannot be instantiated");
    }

    // ------------------------------------------------------------------
    // Parsing / formatting
    // ------------------------------------------------------------------

    /** @see DateFormattingUtils#parseDate(String, String) */
    public static LocalDate parseDate(String value, String pattern) {
        return DateFormattingUtils.parseDate(value, pattern);
    }

    /** @see DateFormattingUtils#parseDateTime(String, String) */
    public static LocalDateTime parseDateTime(String value, String pattern) {
        return DateFormattingUtils.parseDateTime(value, pattern);
    }

    /** @see DateFormattingUtils#parseZonedDateTime(String, String, ZoneId) */
    public static ZonedDateTime parseZonedDateTime(String value, String pattern, ZoneId zoneId) {
        return DateFormattingUtils.parseZonedDateTime(value, pattern, zoneId);
    }

    /** @see DateFormattingUtils#formatDate(LocalDate, String) */
    public static String formatDate(LocalDate date, String pattern) {
        return DateFormattingUtils.formatDate(date, pattern);
    }

    /** @see DateFormattingUtils#formatDateTime(LocalDateTime, String) */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return DateFormattingUtils.formatDateTime(dateTime, pattern);
    }

    /** @see DateFormattingUtils#formatZonedDateTime(ZonedDateTime, String) */
    public static String formatZonedDateTime(ZonedDateTime dateTime, String pattern) {
        return DateFormattingUtils.formatZonedDateTime(dateTime, pattern);
    }

    // ------------------------------------------------------------------
    // Conversion
    // ------------------------------------------------------------------

    /** @see DateConversionUtils#toLocalDate(Date, ZoneId) */
    public static LocalDate toLocalDate(Date date, ZoneId zoneId) {
        return DateConversionUtils.toLocalDate(date, zoneId);
    }

    /** @see DateConversionUtils#toLocalDateTime(Date, ZoneId) */
    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        return DateConversionUtils.toLocalDateTime(date, zoneId);
    }

    /** @see DateConversionUtils#toInstant(Date) */
    public static Instant toInstant(Date date) {
        return DateConversionUtils.toInstant(date);
    }

    /** @see DateConversionUtils#toDate(Instant) */
    public static Date toDate(Instant instant) {
        return DateConversionUtils.toDate(instant);
    }

    /** @see DateConversionUtils#toDate(LocalDate, ZoneId) */
    public static Date toDate(LocalDate date, ZoneId zoneId) {
        return DateConversionUtils.toDate(date, zoneId);
    }

    /** @see DateConversionUtils#toDate(LocalDateTime, ZoneId) */
    public static Date toDate(LocalDateTime dateTime, ZoneId zoneId) {
        return DateConversionUtils.toDate(dateTime, zoneId);
    }

    /** @see DateConversionUtils#toEpochMillis(Instant) */
    public static long toEpochMillis(Instant instant) {
        return DateConversionUtils.toEpochMillis(instant);
    }

    /** @see DateConversionUtils#fromEpochMillis(long) */
    public static Instant fromEpochMillis(long epochMillis) {
        return DateConversionUtils.fromEpochMillis(epochMillis);
    }

    // ------------------------------------------------------------------
    // Arithmetic
    // ------------------------------------------------------------------

    /** @see DateArithmeticUtils#addDays(LocalDate, long) */
    public static LocalDate addDays(LocalDate date, long days) {
        return DateArithmeticUtils.addDays(date, days);
    }

    /** @see DateArithmeticUtils#subtractDays(LocalDate, long) */
    public static LocalDate subtractDays(LocalDate date, long days) {
        return DateArithmeticUtils.subtractDays(date, days);
    }

    /** @see DateArithmeticUtils#addWeeks(LocalDate, long) */
    public static LocalDate addWeeks(LocalDate date, long weeks) {
        return DateArithmeticUtils.addWeeks(date, weeks);
    }

    /** @see DateArithmeticUtils#subtractWeeks(LocalDate, long) */
    public static LocalDate subtractWeeks(LocalDate date, long weeks) {
        return DateArithmeticUtils.subtractWeeks(date, weeks);
    }

    /** @see DateArithmeticUtils#addMonths(LocalDate, long) */
    public static LocalDate addMonths(LocalDate date, long months) {
        return DateArithmeticUtils.addMonths(date, months);
    }

    /** @see DateArithmeticUtils#subtractMonths(LocalDate, long) */
    public static LocalDate subtractMonths(LocalDate date, long months) {
        return DateArithmeticUtils.subtractMonths(date, months);
    }

    /** @see DateArithmeticUtils#addYears(LocalDate, long) */
    public static LocalDate addYears(LocalDate date, long years) {
        return DateArithmeticUtils.addYears(date, years);
    }

    /** @see DateArithmeticUtils#subtractYears(LocalDate, long) */
    public static LocalDate subtractYears(LocalDate date, long years) {
        return DateArithmeticUtils.subtractYears(date, years);
    }

    /** @see DateArithmeticUtils#addHours(LocalDateTime, long) */
    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        return DateArithmeticUtils.addHours(dateTime, hours);
    }

    /** @see DateArithmeticUtils#subtractHours(LocalDateTime, long) */
    public static LocalDateTime subtractHours(LocalDateTime dateTime, long hours) {
        return DateArithmeticUtils.subtractHours(dateTime, hours);
    }

    /** @see DateArithmeticUtils#addMinutes(LocalDateTime, long) */
    public static LocalDateTime addMinutes(LocalDateTime dateTime, long minutes) {
        return DateArithmeticUtils.addMinutes(dateTime, minutes);
    }

    /** @see DateArithmeticUtils#subtractMinutes(LocalDateTime, long) */
    public static LocalDateTime subtractMinutes(LocalDateTime dateTime, long minutes) {
        return DateArithmeticUtils.subtractMinutes(dateTime, minutes);
    }

    /** @see DateArithmeticUtils#addSeconds(LocalDateTime, long) */
    public static LocalDateTime addSeconds(LocalDateTime dateTime, long seconds) {
        return DateArithmeticUtils.addSeconds(dateTime, seconds);
    }

    /** @see DateArithmeticUtils#subtractSeconds(LocalDateTime, long) */
    public static LocalDateTime subtractSeconds(LocalDateTime dateTime, long seconds) {
        return DateArithmeticUtils.subtractSeconds(dateTime, seconds);
    }

    // ------------------------------------------------------------------
    // Comparison / difference
    // ------------------------------------------------------------------

    /** @see DateComparisonUtils#isBefore(LocalDate, LocalDate) */
    public static boolean isBefore(LocalDate first, LocalDate second) {
        return DateComparisonUtils.isBefore(first, second);
    }

    /** @see DateComparisonUtils#isAfter(LocalDate, LocalDate) */
    public static boolean isAfter(LocalDate first, LocalDate second) {
        return DateComparisonUtils.isAfter(first, second);
    }

    /** @see DateComparisonUtils#isEqual(LocalDate, LocalDate) */
    public static boolean isEqual(LocalDate first, LocalDate second) {
        return DateComparisonUtils.isEqual(first, second);
    }

    /** @see DateComparisonUtils#isToday(LocalDate) */
    public static boolean isToday(LocalDate date) {
        return DateComparisonUtils.isToday(date);
    }

    /** @see DateComparisonUtils#isPast(LocalDate) */
    public static boolean isPast(LocalDate date) {
        return DateComparisonUtils.isPast(date);
    }

    /** @see DateComparisonUtils#isFuture(LocalDate) */
    public static boolean isFuture(LocalDate date) {
        return DateComparisonUtils.isFuture(date);
    }

    /** @see DateComparisonUtils#daysBetween(LocalDate, LocalDate) */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return DateComparisonUtils.daysBetween(start, end);
    }

    /** @see DateComparisonUtils#weeksBetween(LocalDate, LocalDate) */
    public static long weeksBetween(LocalDate start, LocalDate end) {
        return DateComparisonUtils.weeksBetween(start, end);
    }

    /** @see DateComparisonUtils#monthsBetween(LocalDate, LocalDate) */
    public static long monthsBetween(LocalDate start, LocalDate end) {
        return DateComparisonUtils.monthsBetween(start, end);
    }

    /** @see DateComparisonUtils#yearsBetween(LocalDate, LocalDate) */
    public static long yearsBetween(LocalDate start, LocalDate end) {
        return DateComparisonUtils.yearsBetween(start, end);
    }

    /** @see DateComparisonUtils#isWeekend(LocalDate) */
    public static boolean isWeekend(LocalDate date) {
        return DateComparisonUtils.isWeekend(date);
    }

    /** @see DateComparisonUtils#isWeekday(LocalDate) */
    public static boolean isWeekday(LocalDate date) {
        return DateComparisonUtils.isWeekday(date);
    }

    /** @see DateComparisonUtils#isLeapYear(int) */
    public static boolean isLeapYear(int year) {
        return DateComparisonUtils.isLeapYear(year);
    }

    /** @see DateComparisonUtils#isLeapYear(LocalDate) */
    public static boolean isLeapYear(LocalDate date) {
        return DateComparisonUtils.isLeapYear(date);
    }

    // ------------------------------------------------------------------
    // Timezone
    // ------------------------------------------------------------------

    /** @see TimezoneUtils#convertZone(ZonedDateTime, ZoneId) */
    public static ZonedDateTime convertZone(ZonedDateTime dateTime, ZoneId targetZone) {
        return TimezoneUtils.convertZone(dateTime, targetZone);
    }

    /** @see TimezoneUtils#convertZone(LocalDateTime, ZoneId, ZoneId) */
    public static LocalDateTime convertZone(LocalDateTime dateTime, ZoneId sourceZone, ZoneId targetZone) {
        return TimezoneUtils.convertZone(dateTime, sourceZone, targetZone);
    }

    /** @see TimezoneUtils#startOfDay(LocalDate) */
    public static LocalDateTime startOfDay(LocalDate date) {
        return TimezoneUtils.startOfDay(date);
    }

    /** @see TimezoneUtils#endOfDay(LocalDate) */
    public static LocalDateTime endOfDay(LocalDate date) {
        return TimezoneUtils.endOfDay(date);
    }

    /** @see TimezoneUtils#startOfDay(LocalDate, ZoneId) */
    public static ZonedDateTime startOfDay(LocalDate date, ZoneId zoneId) {
        return TimezoneUtils.startOfDay(date, zoneId);
    }

    /** @see TimezoneUtils#endOfDay(LocalDate, ZoneId) */
    public static ZonedDateTime endOfDay(LocalDate date, ZoneId zoneId) {
        return TimezoneUtils.endOfDay(date, zoneId);
    }

    // ------------------------------------------------------------------
    // Calendar getters (trivial, direct java.time delegation; not duplicated elsewhere)
    // ------------------------------------------------------------------

    /**
     * Returns the day of the week of {@code date}.
     *
     * @param date the date, must not be {@code null}
     * @return the day of the week, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static DayOfWeek getDayOfWeek(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.getDayOfWeek();
    }

    /**
     * Returns the day-of-month field of {@code date} (1-31).
     *
     * @param date the date, must not be {@code null}
     * @return the day of month
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static int getDayOfMonth(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.getDayOfMonth();
    }

    /**
     * Returns the month-of-year field of {@code date} (1-12), using {@link Month#getValue()}
     * numbering.
     *
     * @param date the date, must not be {@code null}
     * @return the month of year, from 1 (January) to 12 (December)
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static int getMonth(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.getMonthValue();
    }

    /**
     * Returns the year field of {@code date}.
     *
     * @param date the date, must not be {@code null}
     * @return the year
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static int getYear(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.getYear();
    }

    /**
     * Returns the number of days in the month of {@code date}, accounting for leap years.
     *
     * @param date the date, must not be {@code null}
     * @return the length of the month in days (28-31)
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static int getDaysInMonth(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.lengthOfMonth();
    }
}
