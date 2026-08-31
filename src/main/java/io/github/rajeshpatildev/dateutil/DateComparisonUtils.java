package io.github.rajeshpatildev.dateutil;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Comparisons, elapsed-time (difference) calculations, and calendar predicates for
 * {@link LocalDate}, {@link LocalDateTime}, {@link Instant}, and {@link ZonedDateTime}.
 *
 * <p>Every method requires non-null date/time arguments and throws {@link NullPointerException}
 * if any is {@code null}; there is no meaningful boolean, {@code long}, or {@code int} result for
 * "no date."
 *
 * <p><b>Difference methods</b> ({@code daysBetween}, {@code weeksBetween}, {@code monthsBetween},
 * {@code yearsBetween}, {@code hoursBetween}, {@code minutesBetween}, {@code secondsBetween})
 * return a <em>signed</em> value: positive when {@code end} is after {@code start}, negative when
 * {@code end} is before {@code start}, matching {@link Period#between} / {@link ChronoUnit#between}
 * semantics. Calendar-based differences ({@code weeksBetween}, {@code monthsBetween},
 * {@code yearsBetween}) return whole, truncated units (a partial unit does not round up); for
 * example, {@code monthsBetween(2026-01-31, 2026-02-28)} is {@code 0} because a full calendar
 * month has not elapsed.
 *
 * <p>This class is stateless, thread-safe, and cannot be instantiated.
 */
public final class DateComparisonUtils {

    private DateComparisonUtils() {
        throw new AssertionError("DateComparisonUtils is a utility class and cannot be instantiated");
    }

    // ------------------------------------------------------------------
    // Before / after / equal
    // ------------------------------------------------------------------

    /** Returns {@code true} if {@code first} is strictly before {@code second}. */
    public static boolean isBefore(LocalDate first, LocalDate second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isBefore(second);
    }

    /** Returns {@code true} if {@code first} is strictly after {@code second}. */
    public static boolean isAfter(LocalDate first, LocalDate second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isAfter(second);
    }

    /** Returns {@code true} if {@code first} represents the same date as {@code second}. */
    public static boolean isEqual(LocalDate first, LocalDate second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isEqual(second);
    }

    /** @see #isBefore(LocalDate, LocalDate) */
    public static boolean isBefore(LocalDateTime first, LocalDateTime second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isBefore(second);
    }

    /** @see #isAfter(LocalDate, LocalDate) */
    public static boolean isAfter(LocalDateTime first, LocalDateTime second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isAfter(second);
    }

    /** @see #isEqual(LocalDate, LocalDate) */
    public static boolean isEqual(LocalDateTime first, LocalDateTime second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isEqual(second);
    }

    /** @see #isBefore(LocalDate, LocalDate) */
    public static boolean isBefore(Instant first, Instant second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isBefore(second);
    }

    /** @see #isAfter(LocalDate, LocalDate) */
    public static boolean isAfter(Instant first, Instant second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isAfter(second);
    }

    /** @see #isEqual(LocalDate, LocalDate) */
    public static boolean isEqual(Instant first, Instant second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.equals(second);
    }

    /** Returns {@code true} if {@code first} represents an instant strictly before {@code second}. */
    public static boolean isBefore(ZonedDateTime first, ZonedDateTime second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isBefore(second);
    }

    /** Returns {@code true} if {@code first} represents an instant strictly after {@code second}. */
    public static boolean isAfter(ZonedDateTime first, ZonedDateTime second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isAfter(second);
    }

    /** Returns {@code true} if {@code first} represents the same instant as {@code second}. */
    public static boolean isEqual(ZonedDateTime first, ZonedDateTime second) {
        Objects.requireNonNull(first, "first must not be null");
        Objects.requireNonNull(second, "second must not be null");
        return first.isEqual(second);
    }

    // ------------------------------------------------------------------
    // Today / past / future
    // ------------------------------------------------------------------

    /**
     * Returns {@code true} if {@code date} equals today's date in the JVM's default time zone.
     * Prefer {@link #isToday(LocalDate, ZoneId)} when the timezone matters to your application.
     */
    public static boolean isToday(LocalDate date) {
        return isToday(date, ZoneId.systemDefault());
    }

    /** Returns {@code true} if {@code date} equals today's date in the given time zone. */
    public static boolean isToday(LocalDate date, ZoneId zoneId) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date.isEqual(LocalDate.now(zoneId));
    }

    /**
     * Returns {@code true} if {@code date} is strictly before today's date in the JVM's default
     * time zone. Prefer {@link #isPast(LocalDate, ZoneId)} when the timezone matters.
     */
    public static boolean isPast(LocalDate date) {
        return isPast(date, ZoneId.systemDefault());
    }

    /** Returns {@code true} if {@code date} is strictly before today's date in the given zone. */
    public static boolean isPast(LocalDate date, ZoneId zoneId) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date.isBefore(LocalDate.now(zoneId));
    }

    /**
     * Returns {@code true} if {@code date} is strictly after today's date in the JVM's default
     * time zone. Prefer {@link #isFuture(LocalDate, ZoneId)} when the timezone matters.
     */
    public static boolean isFuture(LocalDate date) {
        return isFuture(date, ZoneId.systemDefault());
    }

    /** Returns {@code true} if {@code date} is strictly after today's date in the given zone. */
    public static boolean isFuture(LocalDate date, ZoneId zoneId) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date.isAfter(LocalDate.now(zoneId));
    }

    /**
     * Returns {@code true} if {@code dateTime} is strictly before the current moment in the
     * given time zone.
     */
    public static boolean isPast(LocalDateTime dateTime, ZoneId zoneId) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return dateTime.isBefore(LocalDateTime.now(zoneId));
    }

    /**
     * Returns {@code true} if {@code dateTime} is strictly after the current moment in the given
     * time zone.
     */
    public static boolean isFuture(LocalDateTime dateTime, ZoneId zoneId) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return dateTime.isAfter(LocalDateTime.now(zoneId));
    }

    /** Returns {@code true} if {@code instant} is strictly before the current instant. */
    public static boolean isPast(Instant instant) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.isBefore(Instant.now());
    }

    /** Returns {@code true} if {@code instant} is strictly after the current instant. */
    public static boolean isFuture(Instant instant) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.isAfter(Instant.now());
    }

    // ------------------------------------------------------------------
    // Differences
    // ------------------------------------------------------------------

    /**
     * Returns the number of whole days between {@code start} and {@code end}. Positive when
     * {@code end} is after {@code start}, negative when {@code end} is before {@code start}.
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Returns the number of whole weeks between {@code start} and {@code end}, truncated toward
     * zero. Positive when {@code end} is after {@code start}, negative otherwise.
     */
    public static long weeksBetween(LocalDate start, LocalDate end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.WEEKS.between(start, end);
    }

    /**
     * Returns the number of whole calendar months between {@code start} and {@code end}, as
     * computed by {@link Period#between}. A partial month does not count; for example the
     * difference between January 31 and February 28 is {@code 0} months.
     */
    public static long monthsBetween(LocalDate start, LocalDate end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.MONTHS.between(start, end);
    }

    /**
     * Returns the number of whole calendar years between {@code start} and {@code end}. A
     * partial year does not count.
     */
    public static long yearsBetween(LocalDate start, LocalDate end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.YEARS.between(start, end);
    }

    /**
     * Returns the number of whole hours between {@code start} and {@code end}. Positive when
     * {@code end} is after {@code start}, negative otherwise.
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.HOURS.between(start, end);
    }

    /** Returns the number of whole minutes between {@code start} and {@code end}. */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.MINUTES.between(start, end);
    }

    /** Returns the number of whole seconds between {@code start} and {@code end}. */
    public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.SECONDS.between(start, end);
    }

    /** Returns the number of whole hours between two instants. */
    public static long hoursBetween(Instant start, Instant end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.HOURS.between(start, end);
    }

    /** Returns the number of whole minutes between two instants. */
    public static long minutesBetween(Instant start, Instant end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.MINUTES.between(start, end);
    }

    /** Returns the number of whole seconds between two instants. */
    public static long secondsBetween(Instant start, Instant end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.SECONDS.between(start, end);
    }

    // ------------------------------------------------------------------
    // Calendar predicates
    // ------------------------------------------------------------------

    /** Returns {@code true} if {@code date} falls on a Saturday or Sunday. */
    public static boolean isWeekend(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /** Returns {@code true} if {@code date} falls on Monday through Friday. */
    public static boolean isWeekday(LocalDate date) {
        return !isWeekend(date);
    }

    /** Returns {@code true} if {@code year} is a leap year in the ISO calendar system. */
    public static boolean isLeapYear(int year) {
        return java.time.Year.isLeap(year);
    }

    /** Returns {@code true} if the year of {@code date} is a leap year. */
    public static boolean isLeapYear(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.isLeapYear();
    }
}
