package com.yourcompany.dateutil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe formatting and parsing of {@code java.time} types using {@link DateTimeFormatter}.
 *
 * <p>{@code DateTimeFormatter} instances are immutable and inherently thread-safe, unlike the
 * legacy {@code java.text.SimpleDateFormat}. Formatters built from a pattern string are cached
 * internally and reused across calls so that repeated calls with the same pattern do not pay the
 * cost of re-parsing the pattern.
 *
 * <p>Pattern-based formatters use {@link ResolverStyle#STRICT} so that calendar-invalid input
 * (for example {@code "30-02-2026"}, or {@code "29-02-2026"} in a non-leap year) is rejected with
 * a {@link DateTimeParseException} rather than silently coerced to a nearby valid date, which is
 * {@link ResolverStyle#SMART}'s (the {@code DateTimeFormatter} default) behavior. Because {@link
 * ResolverStyle#STRICT} cannot resolve the pattern letter {@code y} (year-of-era) without an
 * accompanying era field, any {@code y} in a supplied pattern is treated as the equivalent
 * proleptic-year field ({@code u}) when the formatter is built; this produces identical output
 * for all common (post-epoch, CE) dates.
 *
 * <p><b>Null handling:</b> {@code formatX} methods return {@code null} when the date/time
 * argument is {@code null}. {@code parseX} methods never return {@code null}: a {@code null} or
 * blank input string throws {@link IllegalArgumentException}, and a non-null string that does not
 * match the pattern, or that does not represent a valid calendar date, throws {@link
 * DateTimeParseException}. A {@code null} pattern, formatter, or zone always throws {@link
 * NullPointerException}.
 *
 * <p>This class is stateless, thread-safe, and cannot be instantiated.
 */
public final class DateFormattingUtils {

    /** Pattern: {@code yyyy-MM-dd} (ISO-like, unambiguous, lexicographically sortable). */
    public static final String PATTERN_ISO_DATE = "yyyy-MM-dd";

    /** Pattern: {@code dd-MM-yyyy} (common day-month-year presentation format). */
    public static final String PATTERN_DATE_DMY = "dd-MM-yyyy";

    /** Pattern: {@code MM/dd/yyyy} (common US-style presentation format). */
    public static final String PATTERN_DATE_MDY_SLASH = "MM/dd/yyyy";

    /** Pattern: {@code yyyy-MM-dd HH:mm:ss} (sortable date-time with second precision). */
    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /** Reusable formatter for {@link DateTimeFormatter#ISO_DATE}. */
    public static final DateTimeFormatter FORMATTER_ISO_DATE = DateTimeFormatter.ISO_DATE;

    /** Reusable formatter for {@link DateTimeFormatter#ISO_DATE_TIME}. */
    public static final DateTimeFormatter FORMATTER_ISO_DATE_TIME = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Cache of pattern string to compiled {@link DateTimeFormatter}, avoiding repeated pattern
     * parsing for frequently used patterns. {@link DateTimeFormatter} is immutable, so caching
     * and sharing instances across threads is safe.
     */
    private static final Map<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<>();

    private DateFormattingUtils() {
        throw new AssertionError("DateFormattingUtils is a utility class and cannot be instantiated");
    }

    /**
     * Returns a cached, thread-safe {@link DateTimeFormatter} for the given pattern, compiling
     * and caching it on first use. The returned formatter uses {@link ResolverStyle#STRICT} so
     * that invalid calendar dates are rejected rather than silently adjusted; see the class
     * documentation for details.
     *
     * @param pattern the {@link DateTimeFormatter} pattern, must not be {@code null}
     * @return the formatter for the pattern, never {@code null}
     * @throws NullPointerException if {@code pattern} is {@code null}
     * @throws IllegalArgumentException if {@code pattern} is not a valid formatter pattern
     */
    public static DateTimeFormatter formatterFor(String pattern) {
        Objects.requireNonNull(pattern, "pattern must not be null");
        return FORMATTER_CACHE.computeIfAbsent(pattern, DateFormattingUtils::buildStrictFormatter);
    }

    private static DateTimeFormatter buildStrictFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(useProlepticYear(pattern)).withResolverStyle(ResolverStyle.STRICT);
    }

    /**
     * Replaces the pattern letter {@code y} (year-of-era) with {@code u} (proleptic year)
     * wherever it appears outside a quoted literal section. {@link ResolverStyle#STRICT} cannot
     * resolve {@code y} without an accompanying era field, so this substitution is required to
     * support the conventional {@code y}-based patterns used throughout this library's public API
     * (for example {@link #PATTERN_ISO_DATE}) while still rejecting invalid calendar dates.
     */
    private static String useProlepticYear(String pattern) {
        StringBuilder result = new StringBuilder(pattern.length());
        boolean inLiteral = false;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '\'') {
                inLiteral = !inLiteral;
                result.append(c);
            } else if (!inLiteral && c == 'y') {
                result.append('u');
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // ------------------------------------------------------------------
    // Formatting
    // ------------------------------------------------------------------

    /**
     * Formats a {@link LocalDate} using the given pattern.
     *
     * @param date    the date to format, may be {@code null}
     * @param pattern the {@link DateTimeFormatter} pattern, must not be {@code null}
     * @return the formatted string, or {@code null} if {@code date} is {@code null}
     * @throws NullPointerException if {@code pattern} is {@code null}
     */
    public static String formatDate(LocalDate date, String pattern) {
        return formatDate(date, formatterFor(pattern));
    }

    /**
     * Formats a {@link LocalDate} using the given formatter.
     *
     * @param date      the date to format, may be {@code null}
     * @param formatter the formatter to use, must not be {@code null}
     * @return the formatted string, or {@code null} if {@code date} is {@code null}
     * @throws NullPointerException if {@code formatter} is {@code null}
     */
    public static String formatDate(LocalDate date, DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null");
        return date == null ? null : date.format(formatter);
    }

    /**
     * Formats a {@link LocalDateTime} using the given pattern.
     *
     * @param dateTime the date-time to format, may be {@code null}
     * @param pattern  the {@link DateTimeFormatter} pattern, must not be {@code null}
     * @return the formatted string, or {@code null} if {@code dateTime} is {@code null}
     * @throws NullPointerException if {@code pattern} is {@code null}
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return formatDateTime(dateTime, formatterFor(pattern));
    }

    /**
     * Formats a {@link LocalDateTime} using the given formatter.
     *
     * @param dateTime  the date-time to format, may be {@code null}
     * @param formatter the formatter to use, must not be {@code null}
     * @return the formatted string, or {@code null} if {@code dateTime} is {@code null}
     * @throws NullPointerException if {@code formatter} is {@code null}
     */
    public static String formatDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null");
        return dateTime == null ? null : dateTime.format(formatter);
    }

    /**
     * Formats a {@link ZonedDateTime} using the given pattern.
     *
     * @param dateTime the zoned date-time to format, may be {@code null}
     * @param pattern  the {@link DateTimeFormatter} pattern, must not be {@code null}
     * @return the formatted string, or {@code null} if {@code dateTime} is {@code null}
     * @throws NullPointerException if {@code pattern} is {@code null}
     */
    public static String formatZonedDateTime(ZonedDateTime dateTime, String pattern) {
        return formatZonedDateTime(dateTime, formatterFor(pattern));
    }

    /**
     * Formats a {@link ZonedDateTime} using the given formatter.
     *
     * @param dateTime  the zoned date-time to format, may be {@code null}
     * @param formatter the formatter to use, must not be {@code null}
     * @return the formatted string, or {@code null} if {@code dateTime} is {@code null}
     * @throws NullPointerException if {@code formatter} is {@code null}
     */
    public static String formatZonedDateTime(ZonedDateTime dateTime, DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null");
        return dateTime == null ? null : dateTime.format(formatter);
    }

    /**
     * Formats an {@link OffsetDateTime} using the given pattern.
     *
     * @param dateTime the offset date-time to format, may be {@code null}
     * @param pattern  the {@link DateTimeFormatter} pattern, must not be {@code null}
     * @return the formatted string, or {@code null} if {@code dateTime} is {@code null}
     * @throws NullPointerException if {@code pattern} is {@code null}
     */
    public static String formatOffsetDateTime(OffsetDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = formatterFor(pattern);
        return dateTime == null ? null : dateTime.format(formatter);
    }

    // ------------------------------------------------------------------
    // Parsing
    // ------------------------------------------------------------------

    /**
     * Parses a date string using the given pattern.
     *
     * @param value   the string to parse, must not be {@code null} or blank
     * @param pattern the {@link DateTimeFormatter} pattern, must not be {@code null}
     * @return the parsed date, never {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null} or blank
     * @throws NullPointerException if {@code pattern} is {@code null}
     * @throws DateTimeParseException if {@code value} cannot be parsed with {@code pattern}
     */
    public static LocalDate parseDate(String value, String pattern) {
        return parseDate(value, formatterFor(pattern));
    }

    /**
     * Parses a date string using the given formatter.
     *
     * @param value     the string to parse, must not be {@code null} or blank
     * @param formatter the formatter to use, must not be {@code null}
     * @return the parsed date, never {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null} or blank
     * @throws NullPointerException if {@code formatter} is {@code null}
     * @throws DateTimeParseException if {@code value} cannot be parsed with {@code formatter}
     */
    public static LocalDate parseDate(String value, DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null");
        requireNonBlank(value);
        return LocalDate.parse(value, formatter);
    }

    /**
     * Parses a date-time string using the given pattern.
     *
     * @param value   the string to parse, must not be {@code null} or blank
     * @param pattern the {@link DateTimeFormatter} pattern, must not be {@code null}
     * @return the parsed date-time, never {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null} or blank
     * @throws NullPointerException if {@code pattern} is {@code null}
     * @throws DateTimeParseException if {@code value} cannot be parsed with {@code pattern}
     */
    public static LocalDateTime parseDateTime(String value, String pattern) {
        return parseDateTime(value, formatterFor(pattern));
    }

    /**
     * Parses a date-time string using the given formatter.
     *
     * @param value     the string to parse, must not be {@code null} or blank
     * @param formatter the formatter to use, must not be {@code null}
     * @return the parsed date-time, never {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null} or blank
     * @throws NullPointerException if {@code formatter} is {@code null}
     * @throws DateTimeParseException if {@code value} cannot be parsed with {@code formatter}
     */
    public static LocalDateTime parseDateTime(String value, DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null");
        requireNonBlank(value);
        return LocalDateTime.parse(value, formatter);
    }

    /**
     * Parses a date-time string that has no timezone information into a {@link ZonedDateTime} by
     * parsing it as a {@link LocalDateTime} and then assigning the given zone.
     *
     * @param value   the string to parse, must not be {@code null} or blank
     * @param pattern the {@link DateTimeFormatter} pattern, must not be {@code null}
     * @param zoneId  the zone to assign to the parsed value, must not be {@code null}
     * @return the parsed zoned date-time, never {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null} or blank
     * @throws NullPointerException if {@code pattern} or {@code zoneId} is {@code null}
     * @throws DateTimeParseException if {@code value} cannot be parsed with {@code pattern}
     */
    public static ZonedDateTime parseZonedDateTime(String value, String pattern, java.time.ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        LocalDateTime localDateTime = parseDateTime(value, pattern);
        return localDateTime.atZone(zoneId);
    }

    /**
     * Parses an offset date-time string using the given pattern. The pattern must include offset
     * fields (for example {@code XXX}) or this will throw {@link DateTimeParseException}.
     *
     * @param value   the string to parse, must not be {@code null} or blank
     * @param pattern the {@link DateTimeFormatter} pattern, must not be {@code null}
     * @return the parsed offset date-time, never {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null} or blank
     * @throws NullPointerException if {@code pattern} is {@code null}
     * @throws DateTimeParseException if {@code value} cannot be parsed with {@code pattern}
     */
    public static OffsetDateTime parseOffsetDateTime(String value, String pattern) {
        DateTimeFormatter formatter = formatterFor(pattern);
        requireNonBlank(value);
        return OffsetDateTime.parse(value, formatter);
    }

    private static void requireNonBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("value to parse must not be null or blank");
        }
    }
}
