package com.yourcompany.dateutil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Date and time arithmetic (adding and subtracting days, weeks, months, years, hours, minutes,
 * and seconds) for {@link LocalDate}, {@link LocalDateTime}, {@link ZonedDateTime}, and
 * {@link Instant}.
 *
 * <p>All methods delegate to the corresponding {@code plusX}/{@code minusX} methods on the
 * underlying {@code java.time} type, which correctly handle month-length and leap-year overflow
 * (for example adding one month to January 31st yields the last valid day of February) and, for
 * {@link ZonedDateTime}, daylight-saving-time transitions.
 *
 * <p>Every method requires a non-null date/time argument and throws {@link NullPointerException}
 * if it is {@code null}; there is no meaningful result for "add N days to no date."
 *
 * <p>This class is stateless, thread-safe, and cannot be instantiated.
 */
public final class DateArithmeticUtils {

    private DateArithmeticUtils() {
        throw new AssertionError("DateArithmeticUtils is a utility class and cannot be instantiated");
    }

    // ------------------------------------------------------------------
    // LocalDate
    // ------------------------------------------------------------------

    /**
     * Returns a copy of {@code date} with the given number of days added.
     *
     * @param date the starting date, must not be {@code null}
     * @param days the number of days to add, may be negative
     * @return the resulting date, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDate addDays(LocalDate date, long days) {
        Objects.requireNonNull(date, "date must not be null");
        return date.plusDays(days);
    }

    /**
     * Returns a copy of {@code date} with the given number of days subtracted.
     *
     * @param date the starting date, must not be {@code null}
     * @param days the number of days to subtract, may be negative
     * @return the resulting date, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDate subtractDays(LocalDate date, long days) {
        Objects.requireNonNull(date, "date must not be null");
        return date.minusDays(days);
    }

    /**
     * Returns a copy of {@code date} with the given number of weeks added.
     *
     * @param date  the starting date, must not be {@code null}
     * @param weeks the number of weeks to add, may be negative
     * @return the resulting date, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDate addWeeks(LocalDate date, long weeks) {
        Objects.requireNonNull(date, "date must not be null");
        return date.plusWeeks(weeks);
    }

    /**
     * Returns a copy of {@code date} with the given number of weeks subtracted.
     *
     * @param date  the starting date, must not be {@code null}
     * @param weeks the number of weeks to subtract, may be negative
     * @return the resulting date, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDate subtractWeeks(LocalDate date, long weeks) {
        Objects.requireNonNull(date, "date must not be null");
        return date.minusWeeks(weeks);
    }

    /**
     * Returns a copy of {@code date} with the given number of months added. If the resulting
     * month has fewer days than the original day-of-month, the day is adjusted to the last valid
     * day of the resulting month (for example, January 31 + 1 month = February 28/29).
     *
     * @param date   the starting date, must not be {@code null}
     * @param months the number of months to add, may be negative
     * @return the resulting date, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDate addMonths(LocalDate date, long months) {
        Objects.requireNonNull(date, "date must not be null");
        return date.plusMonths(months);
    }

    /**
     * Returns a copy of {@code date} with the given number of months subtracted, using the same
     * month-end adjustment rules as {@link #addMonths(LocalDate, long)}.
     *
     * @param date   the starting date, must not be {@code null}
     * @param months the number of months to subtract, may be negative
     * @return the resulting date, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDate subtractMonths(LocalDate date, long months) {
        Objects.requireNonNull(date, "date must not be null");
        return date.minusMonths(months);
    }

    /**
     * Returns a copy of {@code date} with the given number of years added, adjusting February 29
     * to February 28 when the resulting year is not a leap year.
     *
     * @param date  the starting date, must not be {@code null}
     * @param years the number of years to add, may be negative
     * @return the resulting date, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDate addYears(LocalDate date, long years) {
        Objects.requireNonNull(date, "date must not be null");
        return date.plusYears(years);
    }

    /**
     * Returns a copy of {@code date} with the given number of years subtracted, using the same
     * leap-day adjustment as {@link #addYears(LocalDate, long)}.
     *
     * @param date  the starting date, must not be {@code null}
     * @param years the number of years to subtract, may be negative
     * @return the resulting date, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDate subtractYears(LocalDate date, long years) {
        Objects.requireNonNull(date, "date must not be null");
        return date.minusYears(years);
    }

    // ------------------------------------------------------------------
    // LocalDateTime
    // ------------------------------------------------------------------

    /** @see #addDays(LocalDate, long) */
    public static LocalDateTime addDays(LocalDateTime dateTime, long days) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.plusDays(days);
    }

    /** @see #subtractDays(LocalDate, long) */
    public static LocalDateTime subtractDays(LocalDateTime dateTime, long days) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.minusDays(days);
    }

    /** @see #addWeeks(LocalDate, long) */
    public static LocalDateTime addWeeks(LocalDateTime dateTime, long weeks) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.plusWeeks(weeks);
    }

    /** @see #subtractWeeks(LocalDate, long) */
    public static LocalDateTime subtractWeeks(LocalDateTime dateTime, long weeks) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.minusWeeks(weeks);
    }

    /** @see #addMonths(LocalDate, long) */
    public static LocalDateTime addMonths(LocalDateTime dateTime, long months) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.plusMonths(months);
    }

    /** @see #subtractMonths(LocalDate, long) */
    public static LocalDateTime subtractMonths(LocalDateTime dateTime, long months) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.minusMonths(months);
    }

    /** @see #addYears(LocalDate, long) */
    public static LocalDateTime addYears(LocalDateTime dateTime, long years) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.plusYears(years);
    }

    /** @see #subtractYears(LocalDate, long) */
    public static LocalDateTime subtractYears(LocalDateTime dateTime, long years) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.minusYears(years);
    }

    /**
     * Returns a copy of {@code dateTime} with the given number of hours added.
     *
     * @param dateTime the starting date-time, must not be {@code null}
     * @param hours    the number of hours to add, may be negative
     * @return the resulting date-time, never {@code null}
     * @throws NullPointerException if {@code dateTime} is {@code null}
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.plusHours(hours);
    }

    /**
     * Returns a copy of {@code dateTime} with the given number of hours subtracted.
     *
     * @param dateTime the starting date-time, must not be {@code null}
     * @param hours    the number of hours to subtract, may be negative
     * @return the resulting date-time, never {@code null}
     * @throws NullPointerException if {@code dateTime} is {@code null}
     */
    public static LocalDateTime subtractHours(LocalDateTime dateTime, long hours) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.minusHours(hours);
    }

    /**
     * Returns a copy of {@code dateTime} with the given number of minutes added.
     *
     * @param dateTime the starting date-time, must not be {@code null}
     * @param minutes  the number of minutes to add, may be negative
     * @return the resulting date-time, never {@code null}
     * @throws NullPointerException if {@code dateTime} is {@code null}
     */
    public static LocalDateTime addMinutes(LocalDateTime dateTime, long minutes) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.plusMinutes(minutes);
    }

    /**
     * Returns a copy of {@code dateTime} with the given number of minutes subtracted.
     *
     * @param dateTime the starting date-time, must not be {@code null}
     * @param minutes  the number of minutes to subtract, may be negative
     * @return the resulting date-time, never {@code null}
     * @throws NullPointerException if {@code dateTime} is {@code null}
     */
    public static LocalDateTime subtractMinutes(LocalDateTime dateTime, long minutes) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.minusMinutes(minutes);
    }

    /**
     * Returns a copy of {@code dateTime} with the given number of seconds added.
     *
     * @param dateTime the starting date-time, must not be {@code null}
     * @param seconds  the number of seconds to add, may be negative
     * @return the resulting date-time, never {@code null}
     * @throws NullPointerException if {@code dateTime} is {@code null}
     */
    public static LocalDateTime addSeconds(LocalDateTime dateTime, long seconds) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.plusSeconds(seconds);
    }

    /**
     * Returns a copy of {@code dateTime} with the given number of seconds subtracted.
     *
     * @param dateTime the starting date-time, must not be {@code null}
     * @param seconds  the number of seconds to subtract, may be negative
     * @return the resulting date-time, never {@code null}
     * @throws NullPointerException if {@code dateTime} is {@code null}
     */
    public static LocalDateTime subtractSeconds(LocalDateTime dateTime, long seconds) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.minusSeconds(seconds);
    }

    // ------------------------------------------------------------------
    // ZonedDateTime (DST-aware: plusX/minusX on ZonedDateTime correctly skip/repeat
    // wall-clock time across daylight-saving-time transitions)
    // ------------------------------------------------------------------

    /** @see #addDays(LocalDate, long) */
    public static ZonedDateTime addDays(ZonedDateTime dateTime, long days) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.plusDays(days);
    }

    /** @see #subtractDays(LocalDate, long) */
    public static ZonedDateTime subtractDays(ZonedDateTime dateTime, long days) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.minusDays(days);
    }

    /** @see #addHours(LocalDateTime, long) */
    public static ZonedDateTime addHours(ZonedDateTime dateTime, long hours) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.plusHours(hours);
    }

    /** @see #subtractHours(LocalDateTime, long) */
    public static ZonedDateTime subtractHours(ZonedDateTime dateTime, long hours) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        return dateTime.minusHours(hours);
    }

    // ------------------------------------------------------------------
    // Instant (time-line arithmetic; no calendar concepts such as months apply)
    // ------------------------------------------------------------------

    /**
     * Returns a copy of {@code instant} with the given number of hours added.
     *
     * @param instant the starting instant, must not be {@code null}
     * @param hours   the number of hours to add, may be negative
     * @return the resulting instant, never {@code null}
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static Instant addHours(Instant instant, long hours) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.plus(hours, ChronoUnit.HOURS);
    }

    /**
     * Returns a copy of {@code instant} with the given number of hours subtracted.
     *
     * @param instant the starting instant, must not be {@code null}
     * @param hours   the number of hours to subtract, may be negative
     * @return the resulting instant, never {@code null}
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static Instant subtractHours(Instant instant, long hours) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.minus(hours, ChronoUnit.HOURS);
    }

    /**
     * Returns a copy of {@code instant} with the given number of minutes added.
     *
     * @param instant the starting instant, must not be {@code null}
     * @param minutes the number of minutes to add, may be negative
     * @return the resulting instant, never {@code null}
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static Instant addMinutes(Instant instant, long minutes) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.plus(minutes, ChronoUnit.MINUTES);
    }

    /**
     * Returns a copy of {@code instant} with the given number of minutes subtracted.
     *
     * @param instant the starting instant, must not be {@code null}
     * @param minutes the number of minutes to subtract, may be negative
     * @return the resulting instant, never {@code null}
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static Instant subtractMinutes(Instant instant, long minutes) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.minus(minutes, ChronoUnit.MINUTES);
    }

    /**
     * Returns a copy of {@code instant} with the given number of seconds added.
     *
     * @param instant the starting instant, must not be {@code null}
     * @param seconds the number of seconds to add, may be negative
     * @return the resulting instant, never {@code null}
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static Instant addSeconds(Instant instant, long seconds) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.plusSeconds(seconds);
    }

    /**
     * Returns a copy of {@code instant} with the given number of seconds subtracted.
     *
     * @param instant the starting instant, must not be {@code null}
     * @param seconds the number of seconds to subtract, may be negative
     * @return the resulting instant, never {@code null}
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static Instant subtractSeconds(Instant instant, long seconds) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.minusSeconds(seconds);
    }
}
