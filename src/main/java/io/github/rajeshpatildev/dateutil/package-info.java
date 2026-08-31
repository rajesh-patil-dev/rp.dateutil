/**
 * A framework-independent, thread-safe Java date and time utility library built entirely on the
 * {@code java.time} (JSR-310) API.
 *
 * <h2>Design principles</h2>
 * <ul>
 *   <li>No Spring or other third-party runtime dependencies.</li>
 *   <li>No mutable shared/global state. Every class in this package is stateless.</li>
 *   <li>Every utility class is {@code final} with a private constructor and cannot be
 *       instantiated or subclassed.</li>
 *   <li>All public methods are safe to call concurrently from multiple threads.</li>
 * </ul>
 *
 * <h2>Null-handling policy</h2>
 * This library applies one consistent null-handling contract across every class:
 * <ol>
 *   <li><b>Conversion methods</b> ({@link io.github.rajeshpatildev.dateutil.DateConversionUtils},
 *       {@link io.github.rajeshpatildev.dateutil.TimezoneUtils} conversions) that return an object type
 *       return {@code null} when the primary date/time input is {@code null}. If the method also
 *       accepts a required {@link java.time.ZoneId} or similar parameter, that parameter must not
 *       be {@code null} even when the primary input is {@code null}; passing a {@code null}
 *       {@code ZoneId} always throws {@link java.lang.NullPointerException}, regardless of the
 *       primary input.</li>
 *   <li><b>Formatting methods</b> ({@link io.github.rajeshpatildev.dateutil.DateFormattingUtils#formatDate},
 *       {@code formatDateTime}, {@code formatZonedDateTime}) return {@code null} when the
 *       date/time input is {@code null}. A {@code null} pattern or formatter always throws
 *       {@link java.lang.NullPointerException}.</li>
 *   <li><b>Parsing methods</b> ({@code parseDate}, {@code parseDateTime},
 *       {@code parseZonedDateTime}) never silently return {@code null}. A {@code null} or blank
 *       input string, or a {@code null} pattern/zone, throws
 *       {@link java.lang.IllegalArgumentException}. A syntactically invalid date/time string
 *       throws {@link java.time.format.DateTimeParseException}.</li>
 *   <li><b>Arithmetic, comparison, and calendar methods</b> ({@link
 *       io.github.rajeshpatildev.dateutil.DateArithmeticUtils}, {@link
 *       io.github.rajeshpatildev.dateutil.DateComparisonUtils}) always require a non-null date/time
 *       argument and throw {@link java.lang.NullPointerException} if it is {@code null}, because
 *       there is no meaningful result (and, for primitive {@code long}/{@code boolean}/{@code int}
 *       return types, no sentinel value to represent "no answer").</li>
 *   <li>Methods that accept a {@link java.time.ZoneId} or a format pattern always reject a
 *       {@code null} value for that parameter with {@link java.lang.NullPointerException},
 *       independent of the above rules for the primary date/time argument.</li>
 * </ol>
 *
 * <h2>Timezone policy</h2>
 * Methods that operate on timezone-aware types, or that convert a timezone-less type (such as
 * {@link java.time.LocalDate} or {@link java.util.Date}) into a timezone-aware result, always
 * require an explicit {@link java.time.ZoneId}. A small number of convenience overloads that
 * operate on {@link java.util.Date} without a zone argument use {@link
 * java.time.ZoneId#systemDefault()} and say so explicitly in their JavaDoc; prefer the
 * zone-explicit overload whenever the timezone matters to your application.
 */
package io.github.rajeshpatildev.dateutil;
