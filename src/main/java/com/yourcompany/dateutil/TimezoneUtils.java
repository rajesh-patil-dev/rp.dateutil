package com.yourcompany.dateutil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Timezone conversion and DST-safe start-of-day / end-of-day utilities.
 *
 * <p>Timezone conversion is deliberately split into two distinctly named operations, because
 * conflating them is a common source of bugs:
 * <ul>
 *   <li>{@link #convertZone(ZonedDateTime, ZoneId)} changes the <b>displayed</b> zone of an
 *       already zone-aware value while preserving the same instant on the timeline (for example,
 *       09:00 in {@code America/New_York} becomes 18:30 in {@code Asia/Kolkata} &mdash; same
 *       moment, different wall-clock reading).</li>
 *   <li>{@link #assignZone(LocalDateTime, ZoneId)} attaches a zone to a timezone-<b>less</b>
 *       {@link LocalDateTime}, keeping the wall-clock reading unchanged and thereby selecting a
 *       new (and generally different) instant.</li>
 * </ul>
 *
 * <p>This class is stateless, thread-safe, and cannot be instantiated.
 */
public final class TimezoneUtils {

    /** The UTC time zone. */
    public static final ZoneId UTC = ZoneId.of("UTC");

    /** The {@code Asia/Kolkata} time zone (India Standard Time, UTC+05:30, no DST). */
    public static final ZoneId ASIA_KOLKATA = ZoneId.of("Asia/Kolkata");

    /** The {@code America/New_York} time zone (US Eastern Time, observes DST). */
    public static final ZoneId AMERICA_NEW_YORK = ZoneId.of("America/New_York");

    /** The {@code Europe/London} time zone (UK time, observes DST). */
    public static final ZoneId EUROPE_LONDON = ZoneId.of("Europe/London");

    private TimezoneUtils() {
        throw new AssertionError("TimezoneUtils is a utility class and cannot be instantiated");
    }

    /**
     * Converts {@code dateTime} to the given target zone, preserving the same instant on the
     * timeline. The wall-clock time changes to reflect the target zone's offset; the point in
     * time referred to does not change.
     *
     * @param dateTime   the zoned date-time to convert, must not be {@code null}
     * @param targetZone the zone to convert to, must not be {@code null}
     * @return the equivalent instant expressed in {@code targetZone}, never {@code null}
     * @throws NullPointerException if {@code dateTime} or {@code targetZone} is {@code null}
     */
    public static ZonedDateTime convertZone(ZonedDateTime dateTime, ZoneId targetZone) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(targetZone, "targetZone must not be null");
        return dateTime.withZoneSameInstant(targetZone);
    }

    /**
     * Converts a timezone-less {@code dateTime} from a source zone to a target zone, preserving
     * the same instant on the timeline. This is equivalent to first assigning {@code sourceZone}
     * to {@code dateTime} and then converting the result to {@code targetZone}, returning only
     * the local (wall-clock) date-time. The resulting wall-clock value generally differs from the
     * input because it now represents the same instant in a different offset.
     *
     * @param dateTime   the local date-time to convert, must not be {@code null}
     * @param sourceZone the zone in which {@code dateTime} should be interpreted, must not be
     *                   {@code null}
     * @param targetZone the zone to convert to, must not be {@code null}
     * @return the equivalent wall-clock date-time in {@code targetZone}, never {@code null}
     * @throws NullPointerException if any argument is {@code null}
     */
    public static LocalDateTime convertZone(LocalDateTime dateTime, ZoneId sourceZone, ZoneId targetZone) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(sourceZone, "sourceZone must not be null");
        Objects.requireNonNull(targetZone, "targetZone must not be null");
        return dateTime.atZone(sourceZone).withZoneSameInstant(targetZone).toLocalDateTime();
    }

    /**
     * Assigns a time zone to a timezone-less {@code dateTime} without changing its wall-clock
     * reading. This selects a specific instant on the timeline: the same wall-clock time means a
     * different instant in a different zone. This is distinct from {@link
     * #convertZone(ZonedDateTime, ZoneId)}, which preserves the instant and changes the wall-clock
     * reading instead.
     *
     * @param dateTime the local date-time to assign a zone to, must not be {@code null}
     * @param zoneId   the zone to assign, must not be {@code null}
     * @return {@code dateTime} combined with {@code zoneId}, never {@code null}
     * @throws NullPointerException if {@code dateTime} or {@code zoneId} is {@code null}
     */
    public static ZonedDateTime assignZone(LocalDateTime dateTime, ZoneId zoneId) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return dateTime.atZone(zoneId);
    }

    /**
     * Returns midnight (00:00:00) at the start of {@code date}, as a timezone-less value.
     *
     * @param date the date, must not be {@code null}
     * @return the start-of-day date-time, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.atStartOfDay();
    }

    /**
     * Returns the last representable nanosecond of {@code date} ({@code 23:59:59.999999999}), as
     * a timezone-less value. This is a calendar-only approximation; use {@link
     * #endOfDay(LocalDate, ZoneId)} when the exact timezone-aware end-of-day instant matters, since
     * a fixed wall-clock time is not DST-safe.
     *
     * @param date the date, must not be {@code null}
     * @return the end-of-day date-time, never {@code null}
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.atTime(java.time.LocalTime.MAX);
    }

    /**
     * Returns the timezone-aware start of {@code date} (midnight) in {@code zoneId}. Uses {@link
     * LocalDate#atStartOfDay(ZoneId)}, which correctly advances past a daylight-saving-time gap
     * if local midnight does not exist on that date in that zone.
     *
     * @param date   the date, must not be {@code null}
     * @param zoneId the zone, must not be {@code null}
     * @return the start-of-day zoned date-time, never {@code null}
     * @throws NullPointerException if {@code date} or {@code zoneId} is {@code null}
     */
    public static ZonedDateTime startOfDay(LocalDate date, ZoneId zoneId) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date.atStartOfDay(zoneId);
    }

    /**
     * Returns the timezone-aware, DST-safe end of {@code date} in {@code zoneId}: the instant one
     * nanosecond before the start of the following day. This is computed as {@code
     * date.plusDays(1).atStartOfDay(zoneId).minusNanos(1)} rather than by assuming a fixed
     * wall-clock time of {@code 23:59:59}, because a fixed wall-clock time is incorrect on days
     * with a daylight-saving-time transition (a day can be shorter or longer than 24 hours).
     *
     * @param date   the date, must not be {@code null}
     * @param zoneId the zone, must not be {@code null}
     * @return the end-of-day zoned date-time, never {@code null}
     * @throws NullPointerException if {@code date} or {@code zoneId} is {@code null}
     */
    public static ZonedDateTime endOfDay(LocalDate date, ZoneId zoneId) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return date.plusDays(1).atStartOfDay(zoneId).minusNanos(1);
    }
}
