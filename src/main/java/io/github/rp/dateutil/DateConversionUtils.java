package io.github.rp.dateutil;

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
import java.util.Objects;

/**
 * Conversions between {@code java.util.Date}, {@code java.sql.Date}, and the {@code java.time}
 * types ({@link LocalDate}, {@link LocalDateTime}, {@link ZonedDateTime}, {@link OffsetDateTime},
 * {@link Instant}, {@link OffsetTime}), plus Unix epoch conversions.
 *
 * <p>See the {@link io.github.rp.dateutil package documentation} for this library's
 * null-handling policy. In short: every method here returns {@code null} when its primary
 * date/time argument is {@code null}, except methods returning a primitive ({@code long}), which
 * throw {@link NullPointerException} instead since there is no primitive null.
 *
 * <p>This class is stateless, thread-safe, and cannot be instantiated.
 */
public final class DateConversionUtils {

    private DateConversionUtils() {
        throw new AssertionError("DateConversionUtils is a utility class and cannot be instantiated");
    }

    // ------------------------------------------------------------------
    // java.util.Date <-> java.time
    // ------------------------------------------------------------------

    /**
     * Converts a {@link Date} to an {@link Instant}. This conversion is timezone-independent:
     * an {@code Instant} represents a point on the UTC timeline.
     *
     * @param date the date to convert, may be {@code null}
     * @return the equivalent instant, or {@code null} if {@code date} is {@code null}
     */
    public static Instant toInstant(Date date) {
        return date == null ? null : date.toInstant();
    }

    /**
     * Converts a {@link Date} to a {@link LocalDate} using the JVM's {@linkplain
     * ZoneId#systemDefault() default time zone}. Prefer {@link #toLocalDate(Date, ZoneId)} when
     * the timezone matters to your application, since this overload's result depends on the
     * environment the code runs in.
     *
     * @param date the date to convert, may be {@code null}
     * @return the equivalent local date, or {@code null} if {@code date} is {@code null}
     */
    public static LocalDate toLocalDate(Date date) {
        return toLocalDate(date, ZoneId.systemDefault());
    }

    /**
     * Converts a {@link Date} to a {@link LocalDate} in the given time zone.
     *
     * @param date   the date to convert, may be {@code null}
     * @param zoneId the zone used to interpret the instant, must not be {@code null}
     * @return the equivalent local date, or {@code null} if {@code date} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static LocalDate toLocalDate(Date date, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date == null ? null : date.toInstant().atZone(zoneId).toLocalDate();
    }

    /**
     * Converts a {@link Date} to a {@link LocalDateTime} using the JVM's {@linkplain
     * ZoneId#systemDefault() default time zone}. Prefer {@link #toLocalDateTime(Date, ZoneId)}
     * when the timezone matters to your application.
     *
     * @param date the date to convert, may be {@code null}
     * @return the equivalent local date-time, or {@code null} if {@code date} is {@code null}
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Converts a {@link Date} to a {@link LocalDateTime} in the given time zone.
     *
     * @param date   the date to convert, may be {@code null}
     * @param zoneId the zone used to interpret the instant, must not be {@code null}
     * @return the equivalent local date-time, or {@code null} if {@code date} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date == null ? null : date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * Converts a {@link Date} to a {@link ZonedDateTime} in the given time zone.
     *
     * @param date   the date to convert, may be {@code null}
     * @param zoneId the zone to attach to the resulting value, must not be {@code null}
     * @return the equivalent zoned date-time, or {@code null} if {@code date} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static ZonedDateTime toZonedDateTime(Date date, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date == null ? null : date.toInstant().atZone(zoneId);
    }

    /**
     * Converts an {@link Instant} to a {@link Date}.
     *
     * @param instant the instant to convert, may be {@code null}
     * @return the equivalent date, or {@code null} if {@code instant} is {@code null}
     */
    public static Date toDate(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    /**
     * Converts a {@link LocalDate} to a {@link Date} representing midnight (start of day) in the
     * given time zone.
     *
     * @param date   the local date to convert, may be {@code null}
     * @param zoneId the zone in which "midnight" is computed, must not be {@code null}
     * @return the equivalent date, or {@code null} if {@code date} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static Date toDate(LocalDate date, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date == null ? null : Date.from(date.atStartOfDay(zoneId).toInstant());
    }

    /**
     * Converts a {@link LocalDateTime} to a {@link Date} by attaching the given time zone.
     *
     * @param dateTime the local date-time to convert, may be {@code null}
     * @param zoneId   the zone used to resolve the local date-time to an instant, must not be
     *                 {@code null}
     * @return the equivalent date, or {@code null} if {@code dateTime} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static Date toDate(LocalDateTime dateTime, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return dateTime == null ? null : Date.from(dateTime.atZone(zoneId).toInstant());
    }

    /**
     * Converts a {@link ZonedDateTime} to a {@link Date}.
     *
     * @param dateTime the zoned date-time to convert, may be {@code null}
     * @return the equivalent date, or {@code null} if {@code dateTime} is {@code null}
     */
    public static Date toDate(ZonedDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.toInstant());
    }

    // ------------------------------------------------------------------
    // java.sql.Date
    // ------------------------------------------------------------------

    /**
     * Converts a {@link LocalDate} to a {@link java.sql.Date}.
     *
     * @param date the local date to convert, may be {@code null}
     * @return the equivalent SQL date, or {@code null} if {@code date} is {@code null}
     */
    public static java.sql.Date toSqlDate(LocalDate date) {
        return date == null ? null : java.sql.Date.valueOf(date);
    }

    /**
     * Converts a {@link java.sql.Date} to a {@link LocalDate}.
     *
     * @param sqlDate the SQL date to convert, may be {@code null}
     * @return the equivalent local date, or {@code null} if {@code sqlDate} is {@code null}
     */
    public static LocalDate toLocalDate(java.sql.Date sqlDate) {
        return sqlDate == null ? null : sqlDate.toLocalDate();
    }

    /**
     * Converts a {@link Date} to a {@link java.sql.Date}, truncating any time-of-day component
     * using the given time zone to determine the calendar date.
     *
     * @param date   the date to convert, may be {@code null}
     * @param zoneId the zone used to determine the calendar date, must not be {@code null}
     * @return the equivalent SQL date, or {@code null} if {@code date} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static java.sql.Date toSqlDate(Date date, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date == null ? null : java.sql.Date.valueOf(toLocalDate(date, zoneId));
    }

    // ------------------------------------------------------------------
    // Instant-centric conversions
    // ------------------------------------------------------------------

    /**
     * Converts an {@link Instant} to a {@link LocalDate} in the given time zone.
     *
     * @param instant the instant to convert, may be {@code null}
     * @param zoneId  the zone used to interpret the instant, must not be {@code null}
     * @return the equivalent local date, or {@code null} if {@code instant} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static LocalDate toLocalDate(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return instant == null ? null : instant.atZone(zoneId).toLocalDate();
    }

    /**
     * Converts an {@link Instant} to a {@link LocalDateTime} in the given time zone.
     *
     * @param instant the instant to convert, may be {@code null}
     * @param zoneId  the zone used to interpret the instant, must not be {@code null}
     * @return the equivalent local date-time, or {@code null} if {@code instant} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return instant == null ? null : instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * Converts a {@link LocalDateTime} to an {@link Instant} by resolving it in the given time
     * zone.
     *
     * @param dateTime the local date-time to convert, may be {@code null}
     * @param zoneId   the zone used to resolve the local date-time, must not be {@code null}
     * @return the equivalent instant, or {@code null} if {@code dateTime} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static Instant toInstant(LocalDateTime dateTime, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return dateTime == null ? null : dateTime.atZone(zoneId).toInstant();
    }

    /**
     * Converts a {@link LocalDate} to the {@link Instant} representing midnight (start of day)
     * in the given time zone.
     *
     * @param date   the local date to convert, may be {@code null}
     * @param zoneId the zone in which "midnight" is computed, must not be {@code null}
     * @return the equivalent instant, or {@code null} if {@code date} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static Instant toInstant(LocalDate date, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date == null ? null : date.atStartOfDay(zoneId).toInstant();
    }

    /**
     * Converts an {@link Instant} to a {@link ZonedDateTime} in the given time zone.
     *
     * @param instant the instant to convert, may be {@code null}
     * @param zoneId  the zone to attach to the resulting value, must not be {@code null}
     * @return the equivalent zoned date-time, or {@code null} if {@code instant} is {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static ZonedDateTime toZonedDateTime(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return instant == null ? null : instant.atZone(zoneId);
    }

    /**
     * Converts an {@link Instant} to an {@link OffsetDateTime} using the given fixed offset.
     *
     * @param instant the instant to convert, may be {@code null}
     * @param offset  the offset to attach to the resulting value, must not be {@code null}
     * @return the equivalent offset date-time, or {@code null} if {@code instant} is {@code null}
     * @throws NullPointerException if {@code offset} is {@code null}
     */
    public static OffsetDateTime toOffsetDateTime(Instant instant, ZoneOffset offset) {
        Objects.requireNonNull(offset, "offset must not be null");
        return instant == null ? null : instant.atOffset(offset);
    }

    /**
     * Converts an {@link OffsetDateTime} to an {@link Instant}. This conversion never loses
     * information because both types represent the same instant on the UTC timeline.
     *
     * @param offsetDateTime the offset date-time to convert, may be {@code null}
     * @return the equivalent instant, or {@code null} if {@code offsetDateTime} is {@code null}
     */
    public static Instant toInstant(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toInstant();
    }

    // ------------------------------------------------------------------
    // ZonedDateTime <-> OffsetDateTime
    // ------------------------------------------------------------------

    /**
     * Converts a {@link ZonedDateTime} to an {@link OffsetDateTime}, preserving the same instant
     * and using the zoned value's resolved offset.
     *
     * @param zonedDateTime the zoned date-time to convert, may be {@code null}
     * @return the equivalent offset date-time, or {@code null} if {@code zonedDateTime} is
     *         {@code null}
     */
    public static OffsetDateTime toOffsetDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toOffsetDateTime();
    }

    /**
     * Converts an {@link OffsetDateTime} to a {@link ZonedDateTime}, using the offset itself as
     * a fixed-offset zone.
     *
     * @param offsetDateTime the offset date-time to convert, may be {@code null}
     * @return the equivalent zoned date-time, or {@code null} if {@code offsetDateTime} is
     *         {@code null}
     */
    public static ZonedDateTime toZonedDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toZonedDateTime();
    }

    // ------------------------------------------------------------------
    // OffsetTime
    // ------------------------------------------------------------------

    /**
     * Extracts the {@link OffsetTime} component of an {@link OffsetDateTime}.
     *
     * @param offsetDateTime the offset date-time to convert, may be {@code null}
     * @return the equivalent offset time, or {@code null} if {@code offsetDateTime} is
     *         {@code null}
     */
    public static OffsetTime toOffsetTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toOffsetTime();
    }

    /**
     * Combines a {@link LocalTime} with a fixed {@link ZoneOffset} to form an {@link OffsetTime}.
     *
     * @param time   the local time to convert, may be {@code null}
     * @param offset the offset to attach, must not be {@code null}
     * @return the equivalent offset time, or {@code null} if {@code time} is {@code null}
     * @throws NullPointerException if {@code offset} is {@code null}
     */
    public static OffsetTime toOffsetTime(LocalTime time, ZoneOffset offset) {
        Objects.requireNonNull(offset, "offset must not be null");
        return time == null ? null : OffsetTime.of(time, offset);
    }

    /**
     * Discards the offset of an {@link OffsetTime}, returning the local (wall-clock) time.
     *
     * @param offsetTime the offset time to convert, may be {@code null}
     * @return the equivalent local time, or {@code null} if {@code offsetTime} is {@code null}
     */
    public static LocalTime toLocalTime(OffsetTime offsetTime) {
        return offsetTime == null ? null : offsetTime.toLocalTime();
    }

    // ------------------------------------------------------------------
    // Epoch conversions
    // ------------------------------------------------------------------

    /**
     * Converts an {@link Instant} to milliseconds since the Unix epoch (1970-01-01T00:00:00Z),
     * i.e. UTC.
     *
     * @param instant the instant to convert, must not be {@code null}
     * @return the number of milliseconds since the epoch
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static long toEpochMillis(Instant instant) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.toEpochMilli();
    }

    /**
     * Converts milliseconds since the Unix epoch (UTC) to an {@link Instant}. Negative values
     * (instants before 1970-01-01T00:00:00Z) are supported.
     *
     * @param epochMillis milliseconds since the epoch, may be negative
     * @return the equivalent instant, never {@code null}
     */
    public static Instant fromEpochMillis(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis);
    }

    /**
     * Converts an {@link Instant} to whole seconds since the Unix epoch (UTC). Any sub-second
     * precision is truncated (not rounded).
     *
     * @param instant the instant to convert, must not be {@code null}
     * @return the number of seconds since the epoch
     * @throws NullPointerException if {@code instant} is {@code null}
     */
    public static long toEpochSeconds(Instant instant) {
        Objects.requireNonNull(instant, "instant must not be null");
        return instant.getEpochSecond();
    }

    /**
     * Converts seconds since the Unix epoch (UTC) to an {@link Instant}. Negative values
     * (instants before 1970-01-01T00:00:00Z) are supported.
     *
     * @param epochSeconds seconds since the epoch, may be negative
     * @return the equivalent instant, never {@code null}
     */
    public static Instant fromEpochSeconds(long epochSeconds) {
        return Instant.ofEpochSecond(epochSeconds);
    }

    /**
     * Converts a {@link LocalDateTime}, resolved in the given time zone, to milliseconds since
     * the Unix epoch (UTC).
     *
     * @param dateTime the local date-time to convert, must not be {@code null}
     * @param zoneId   the zone used to resolve the local date-time, must not be {@code null}
     * @return the number of milliseconds since the epoch
     * @throws NullPointerException if {@code dateTime} or {@code zoneId} is {@code null}
     */
    public static long toEpochMillis(LocalDateTime dateTime, ZoneId zoneId) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return dateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

    /**
     * Converts milliseconds since the Unix epoch (UTC) to a {@link LocalDateTime} in the given
     * time zone.
     *
     * @param epochMillis milliseconds since the epoch, may be negative
     * @param zoneId      the zone used to interpret the instant, must not be {@code null}
     * @return the equivalent local date-time, never {@code null}
     * @throws NullPointerException if {@code zoneId} is {@code null}
     */
    public static LocalDateTime fromEpochMillis(long epochMillis, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDateTime();
    }
}
