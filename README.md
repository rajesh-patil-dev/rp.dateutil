# date-util — A Thread-Safe Java Date & Time Utility Library (java.time / JSR-310)

[![CI](https://github.com/rajesh-patil-dev/rp.dateutil/actions/workflows/ci.yml/badge.svg)](https://github.com/rajesh-patil-dev/rp.dateutil/actions/workflows/ci.yml)
[![Code Review](https://github.com/rajesh-patil-dev/rp.dateutil/actions/workflows/code-review.yml/badge.svg)](https://github.com/rajesh-patil-dev/rp.dateutil/actions/workflows/code-review.yml)
[![Security Scan](https://github.com/rajesh-patil-dev/rp.dateutil/actions/workflows/security-scan.yml/badge.svg)](https://github.com/rajesh-patil-dev/rp.dateutil/actions/workflows/security-scan.yml)
[![Java 17+](https://img.shields.io/badge/Java-17%2B-orange)](https://openjdk.org/projects/jdk/17/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](#license)
[![Maven Central compatible](https://img.shields.io/badge/artifact-io.github.rajesh--patil--dev%3Adate--util-brightgreen)](#maven-installation)

**date-util** is a production-ready, dependency-free **Java date and time utility library** built entirely on the modern **`java.time` (JSR-310) API**. It gives Java and **Spring Boot** applications a single, well-tested, thread-safe toolkit for date conversion, parsing, formatting, arithmetic, comparison, and **timezone-safe** date/time handling — without pulling in Joda-Time, Apache Commons Lang, or any other third-party runtime dependency.

If you have ever searched for "**java date utility class**", "**convert Date to LocalDate Java**", "**thread-safe DateTimeFormatter**", "**Spring Boot date utility library**", or "**java.time helper methods**", this library is built to be exactly that: a drop-in Maven dependency that replaces ad-hoc, copy-pasted date-handling code scattered across a codebase with one consistent, documented, tested API.

## Table of Contents

- [Why date-util?](#why-date-util)
- [Features at a Glance](#features-at-a-glance)
- [Maven Installation](#maven-installation)
- [Quick Start](#quick-start)
- [Package & Class Overview](#package--class-overview)
- [Usage Examples](#usage-examples)
  - [Parsing & Formatting](#parsing--formatting)
  - [Date Conversion (Date, LocalDate, Instant, ZonedDateTime, ...)](#date-conversion)
  - [Date Arithmetic](#date-arithmetic)
  - [Date Comparison & Differences](#date-comparison--differences)
  - [Timezone Conversion](#timezone-conversion)
  - [Start of Day / End of Day (DST-Safe)](#start-of-day--end-of-day-dst-safe)
  - [Calendar Utilities](#calendar-utilities)
  - [Epoch (Unix Timestamp) Conversion](#epoch-unix-timestamp-conversion)
- [Supported Date/Time Types](#supported-datetime-types)
- [Null-Handling Policy](#null-handling-policy)
- [Exception Behavior](#exception-behavior)
- [Timezone Behavior](#timezone-behavior)
- [Thread Safety](#thread-safety)
- [Using date-util in a Spring Boot Application](#using-date-util-in-a-spring-boot-application)
- [Build Instructions](#build-instructions)
- [Continuous Integration & Delivery](#continuous-integration--delivery)
- [Running with Docker](#running-with-docker)
- [Versioning](#versioning)
- [License](#license)

## Why date-util?

Every non-trivial Java project ends up writing the same date-handling helper methods over and over: converting `java.util.Date` to `LocalDate`, safely parsing a `dd-MM-yyyy` string, computing "is this date in the past", or converting a timestamp between `Asia/Kolkata` and `UTC`. Getting these right — especially around **null handling**, **daylight saving time (DST)**, and **thread safety** — is easy to get subtly wrong.

`date-util` solves this once, correctly, and packages it as a small, dependency-free JAR:

- **Zero runtime dependencies.** No Spring, no Joda-Time, no Apache Commons. Just the JDK's own `java.time` API.
- **Framework-independent.** Works in any Java 17+ application — plain Java, Spring Boot, Quarkus, Micronaut, batch jobs, Android (API 26+), or a CLI tool.
- **Thread-safe by design.** Every class is stateless and immutable; `DateTimeFormatter` instances are cached and reused instead of the legacy, mutable, non-thread-safe `SimpleDateFormat`.
- **Timezone-safe.** Methods that matter for correctness require an explicit `ZoneId` instead of silently trusting the JVM's default timezone, and end-of-day calculations are computed correctly across DST transitions instead of naively assuming `23:59:59`.
- **A single, consistent null-handling and exception-handling contract** documented once and applied everywhere (see [Null-Handling Policy](#null-handling-policy) and [Exception Behavior](#exception-behavior) below).
- **Fully tested**, including timezone-specific tests across `UTC`, `Asia/Kolkata`, `America/New_York`, and `Europe/London`, and edge cases like leap years, month-end rollovers, and DST spring-forward/fall-back transitions.

## Features at a Glance

| Capability | Class |
|---|---|
| Convert between `Date`, `java.sql.Date`, `LocalDate`, `LocalDateTime`, `ZonedDateTime`, `OffsetDateTime`, `Instant`, `OffsetTime`, and Unix epoch values | `DateConversionUtils` |
| Parse and format dates/times with cached, thread-safe `DateTimeFormatter`s | `DateFormattingUtils` |
| Add/subtract days, weeks, months, years, hours, minutes, seconds | `DateArithmeticUtils` |
| Compare dates/times, compute signed differences (days/weeks/months/years/hours/minutes/seconds) | `DateComparisonUtils` |
| Convert between timezones, compute DST-safe start-of-day/end-of-day | `TimezoneUtils` |
| Convenience facade exposing the most common operations from all of the above | `DateUtils` |

## Maven Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.rajesh-patil-dev</groupId>
    <artifactId>date-util</artifactId>
    <version>1.0.0</version>
</dependency>
```

Requires **Java 17 or later**. No other configuration, Spring beans, or auto-configuration is required — the library works as a plain library dependency in any Maven or Gradle project.

**Gradle (Kotlin DSL)**:

```kotlin
implementation("io.github.rajesh-patil-dev:date-util:1.0.0")
```

**Gradle (Groovy DSL)**:

```groovy
implementation 'io.github.rajesh-patil-dev:date-util:1.0.0'
```

> `date-util` is not (yet) published to Maven Central. Build it from source with `mvn clean install` (see [Build Instructions](#build-instructions)) to publish it to your local `~/.m2` repository, or point your build at a private/internal Maven repository hosting the JAR.

## Quick Start

```java
import io.github.rajeshpatildev.dateutil.DateUtils;
import java.time.LocalDate;

LocalDate date = DateUtils.parseDate("30-08-2026", "dd-MM-yyyy");

String formatted = DateUtils.formatDate(date, "dd-MM-yyyy"); // "30-08-2026"

LocalDate nextWeek = DateUtils.addDays(date, 7);

boolean weekend = DateUtils.isWeekend(date); // true - 2026-08-30 is a Sunday
```

No Spring beans, no configuration classes, no `@Autowired` — `DateUtils` and every other class in this library expose only `public static` methods.

## Package & Class Overview

Everything lives in a single package: `io.github.rajeshpatildev.dateutil`.

```
io.github.rajeshpatildev.dateutil
├── DateConversionUtils    // Date <-> java.time type conversions, epoch conversions
├── DateFormattingUtils    // Parsing and formatting via DateTimeFormatter
├── DateArithmeticUtils    // add/subtract days, weeks, months, years, hours, minutes, seconds
├── DateComparisonUtils    // isBefore/isAfter/isEqual, isToday/isPast/isFuture, differences,
│                          // isWeekend/isWeekday/isLeapYear
├── TimezoneUtils          // convertZone, assignZone, startOfDay/endOfDay (DST-safe)
└── DateUtils              // Facade delegating to the classes above for common operations
```

`DateUtils` is a convenience facade over the specialized classes — it does not duplicate any logic. Use it for everyday convenience, and call the specialized classes directly when you need the full API surface (for example, `ZonedDateTime` arithmetic, `OffsetDateTime`/`OffsetTime` conversions, or `Instant`-based comparisons that are not re-exposed on the facade).

Every public class is `final` with a private constructor: none of them can be instantiated or subclassed, and none of them hold any mutable state.

## Usage Examples

### Parsing & Formatting

```java
import io.github.rajeshpatildev.dateutil.DateFormattingUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Parsing
LocalDate date = DateFormattingUtils.parseDate("30-08-2026", "dd-MM-yyyy");
LocalDateTime dateTime = DateFormattingUtils.parseDateTime(
        "2026-08-30 14:05:09", DateFormattingUtils.PATTERN_DATE_TIME);

// Formatting
String iso = DateFormattingUtils.formatDate(date, DateFormattingUtils.PATTERN_ISO_DATE); // "2026-08-30"
String us = DateFormattingUtils.formatDate(date, DateFormattingUtils.PATTERN_DATE_MDY_SLASH); // "08/30/2026"

// Predefined pattern constants
DateFormattingUtils.PATTERN_ISO_DATE;      // "yyyy-MM-dd"
DateFormattingUtils.PATTERN_DATE_DMY;      // "dd-MM-yyyy"
DateFormattingUtils.PATTERN_DATE_MDY_SLASH;// "MM/dd/yyyy"
DateFormattingUtils.PATTERN_DATE_TIME;     // "yyyy-MM-dd HH:mm:ss"
DateFormattingUtils.FORMATTER_ISO_DATE;      // DateTimeFormatter.ISO_DATE
DateFormattingUtils.FORMATTER_ISO_DATE_TIME; // DateTimeFormatter.ISO_DATE_TIME
```

**Invalid dates are rejected, not silently coerced.** Pattern-based parsing uses `ResolverStyle.STRICT`, so a calendar-invalid string such as `"30-02-2026"` (February 30th) or `"29-02-2026"` (February 29th in a non-leap year) throws `DateTimeParseException` instead of silently rolling over to a nearby valid date:

```java
DateFormattingUtils.parseDate("30-02-2026", "dd-MM-yyyy"); // throws DateTimeParseException
DateFormattingUtils.parseDate("29-02-2024", "dd-MM-yyyy"); // OK: 2024 is a leap year -> 2024-02-29
```

Formatters built from a pattern string are compiled once and cached internally (keyed by pattern), so calling `formatDate`/`parseDate` repeatedly with the same pattern does not repeatedly pay the cost of parsing the pattern string.

### Date Conversion

```java
import io.github.rajeshpatildev.dateutil.DateConversionUtils;
import java.time.*;
import java.util.Date;

ZoneId zone = ZoneId.of("Asia/Kolkata");

// java.util.Date <-> java.time
LocalDate localDate = DateConversionUtils.toLocalDate(new Date(), zone);
LocalDateTime localDateTime = DateConversionUtils.toLocalDateTime(new Date(), zone);
Instant instant = DateConversionUtils.toInstant(new Date());
Date fromInstant = DateConversionUtils.toDate(instant);
Date fromLocalDateTime = DateConversionUtils.toDate(LocalDateTime.now(), zone);

// java.sql.Date
java.sql.Date sqlDate = DateConversionUtils.toSqlDate(localDate);

// Instant <-> LocalDate / LocalDateTime / ZonedDateTime, zone-explicit
LocalDate d = DateConversionUtils.toLocalDate(instant, zone);
ZonedDateTime zdt = DateConversionUtils.toZonedDateTime(instant, zone);

// OffsetDateTime / OffsetTime
OffsetDateTime odt = DateConversionUtils.toOffsetDateTime(instant, ZoneOffset.UTC);
```

### Date Arithmetic

```java
import io.github.rajeshpatildev.dateutil.DateArithmeticUtils;
import java.time.LocalDate;

LocalDate date = LocalDate.of(2026, 1, 31);

DateArithmeticUtils.addDays(date, 7);
DateArithmeticUtils.subtractWeeks(date, 2);
DateArithmeticUtils.addMonths(date, 1);   // -> 2026-02-28 (clamped to a valid day)
DateArithmeticUtils.addYears(date, 1);

// LocalDateTime also supports hours/minutes/seconds
DateArithmeticUtils.addHours(LocalDateTime.now(), 3);
```

Month and year arithmetic correctly clamps to the last valid day of the resulting month (`2026-01-31` + 1 month = `2026-02-28`, not an invalid `2026-02-31`), and correctly adjusts leap days (`2024-02-29` + 1 year = `2025-02-28`).

### Date Comparison & Differences

```java
import io.github.rajeshpatildev.dateutil.DateComparisonUtils;
import java.time.LocalDate;

LocalDate a = LocalDate.of(2026, 8, 1);
LocalDate b = LocalDate.of(2026, 8, 31);

DateComparisonUtils.isBefore(a, b);   // true
DateComparisonUtils.isAfter(b, a);    // true
DateComparisonUtils.isToday(a, ZoneId.of("UTC"));
DateComparisonUtils.isPast(a, ZoneId.of("UTC"));
DateComparisonUtils.isFuture(b, ZoneId.of("UTC"));

DateComparisonUtils.daysBetween(a, b);   // 30
DateComparisonUtils.weeksBetween(a, b);  // 4 (truncated, not rounded)
DateComparisonUtils.monthsBetween(a, b); // 0 (a full calendar month has not elapsed)

DateComparisonUtils.isWeekend(LocalDate.of(2026, 8, 30)); // true (Sunday)
DateComparisonUtils.isLeapYear(2024); // true
```

Difference calculations return a **signed** result: positive when the end date is after the start date, negative otherwise. Calendar-based differences (`weeksBetween`, `monthsBetween`, `yearsBetween`) return whole units only — a partial period is truncated toward zero, matching `java.time.temporal.ChronoUnit.between` semantics.

### Timezone Conversion

`date-util` deliberately distinguishes two operations that are easy to confuse:

```java
import io.github.rajeshpatildev.dateutil.TimezoneUtils;
import java.time.*;

ZoneId utc = ZoneId.of("UTC");
ZoneId kolkata = ZoneId.of("Asia/Kolkata");

// 1) convertZone: SAME instant, DIFFERENT wall-clock reading.
//    "What time is it right now, displayed in a different zone?"
ZonedDateTime utcTime = ZonedDateTime.of(2026, 8, 30, 12, 0, 0, 0, utc);
ZonedDateTime kolkataTime = TimezoneUtils.convertZone(utcTime, kolkata); // 2026-08-30T17:30+05:30

// 2) assignZone: SAME wall-clock reading, DIFFERENT instant.
//    "This timezone-less timestamp actually happened in zone X."
LocalDateTime naive = LocalDateTime.of(2026, 8, 30, 12, 0, 0);
ZonedDateTime asKolkata = TimezoneUtils.assignZone(naive, kolkata); // 2026-08-30T12:00+05:30
```

Conflating these two operations is one of the most common sources of timezone bugs; `date-util` gives them distinct method names so the intent is unambiguous at the call site.

### Start of Day / End of Day (DST-Safe)

```java
import io.github.rajeshpatildev.dateutil.TimezoneUtils;
import java.time.LocalDate;
import java.time.ZoneId;

LocalDate date = LocalDate.of(2026, 3, 8); // a DST spring-forward day in America/New_York
ZoneId newYork = ZoneId.of("America/New_York");

TimezoneUtils.startOfDay(date, newYork); // midnight, correctly handled even if it falls in a DST gap
TimezoneUtils.endOfDay(date, newYork);   // one nanosecond before the NEXT day's midnight
```

`endOfDay(LocalDate, ZoneId)` is deliberately **not** implemented as `date.atTime(23, 59, 59)`. On a day with a daylight-saving-time transition, that assumption is wrong: the day is either 23 or 25 hours long. Instead, it is computed as `date.plusDays(1).atStartOfDay(zoneId).minusNanos(1)`, which is correct on every day, DST transition or not.

### Calendar Utilities

```java
import io.github.rajeshpatildev.dateutil.DateUtils;
import java.time.LocalDate;

LocalDate date = LocalDate.of(2026, 8, 30);

DateUtils.getDayOfWeek(date);   // DayOfWeek.SUNDAY
DateUtils.getDayOfMonth(date);  // 30
DateUtils.getMonth(date);       // 8
DateUtils.getYear(date);        // 2026
DateUtils.getDaysInMonth(date); // 31
DateUtils.isLeapYear(date);     // false
```

### Epoch (Unix Timestamp) Conversion

```java
import io.github.rajeshpatildev.dateutil.DateConversionUtils;
import java.time.Instant;

Instant now = Instant.now();
long epochMillis = DateConversionUtils.toEpochMillis(now); // milliseconds since 1970-01-01T00:00:00Z (UTC)
Instant restored = DateConversionUtils.fromEpochMillis(epochMillis);

long epochSeconds = DateConversionUtils.toEpochSeconds(now);
Instant fromSeconds = DateConversionUtils.fromEpochSeconds(epochSeconds);
```

All epoch values are UTC-based, per the Unix epoch definition, and negative epoch values (instants before 1970-01-01T00:00:00Z) are fully supported.

## Supported Date/Time Types

| Type | Conversion support |
|---|---|
| `java.util.Date` | ✅ to/from `Instant`, `LocalDate`, `LocalDateTime`, `ZonedDateTime`, `java.sql.Date` |
| `java.sql.Date` | ✅ to/from `LocalDate`, `java.util.Date` |
| `java.time.LocalDate` | ✅ to/from `Date`, `Instant` (start of day), `java.sql.Date` |
| `java.time.LocalDateTime` | ✅ to/from `Date`, `Instant`, epoch millis |
| `java.time.ZonedDateTime` | ✅ to/from `Date`, `Instant`, `OffsetDateTime` |
| `java.time.OffsetDateTime` | ✅ to/from `Instant`, `ZonedDateTime` |
| `java.time.Instant` | ✅ to/from all of the above, plus Unix epoch millis/seconds |
| `java.time.OffsetTime` | ✅ to/from `LocalTime`, `OffsetDateTime` |

## Null-Handling Policy

`date-util` applies **one consistent null-handling contract** across every class (documented in full in the `io.github.rajeshpatildev.dateutil` package Javadoc):

| Method category | Behavior on `null` primary argument |
|---|---|
| Conversion methods (`DateConversionUtils`, `TimezoneUtils` conversions) returning an object | Return `null` |
| Formatting methods (`formatDate`, `formatDateTime`, `formatZonedDateTime`) | Return `null` |
| Parsing methods (`parseDate`, `parseDateTime`, `parseZonedDateTime`) | Throw `IllegalArgumentException` (never silently return `null`) |
| Arithmetic & comparison methods (`DateArithmeticUtils`, `DateComparisonUtils`) | Throw `NullPointerException` (no meaningful result exists) |
| A `null` `ZoneId`, pattern, or `DateTimeFormatter` argument, regardless of category | Always throws `NullPointerException` |

This means: converting or formatting a `null` date is a safe no-op that returns `null`, but parsing `null` or blank input, or performing arithmetic/comparison on a `null` date, is always treated as a programming error and fails fast and loudly.

## Exception Behavior

- Invalid or malformed date/time strings throw the standard, well-understood `java.time.format.DateTimeParseException` — never a silently-swallowed exception, and never a silently-returned `null`.
- Calendar-invalid dates (February 30th, February 29th in a non-leap year) are rejected the same way as syntactically malformed strings, because parsing uses `ResolverStyle.STRICT`.
- `null` or blank input to a parsing method throws `IllegalArgumentException`.
- A missing required `ZoneId`, pattern, or formatter always throws `NullPointerException`.
- No method in this library catches an exception merely to suppress it. No custom exception types are introduced where a standard JDK exception (`DateTimeParseException`, `NullPointerException`, `IllegalArgumentException`) already communicates the failure clearly.

## Timezone Behavior

- Methods that convert a timezone-aware value, or that need to interpret a timezone-less value (`LocalDate`, `LocalDateTime`, `java.util.Date`) as a point in time, **require an explicit `ZoneId` argument** wherever the timezone genuinely matters to the result.
- A small number of `java.util.Date`-based convenience overloads (for example `DateConversionUtils.toLocalDate(Date)`) fall back to `ZoneId.systemDefault()` and say so explicitly in their Javadoc. Prefer the zone-explicit overload in any code where the timezone matters, since the system-default zone depends on the environment the code happens to run in.
- `TimezoneUtils` deliberately separates **"convert this instant to a different zone's wall-clock display"** (`convertZone`) from **"this timezone-less timestamp actually occurred in zone X"** (`assignZone`) — see [Timezone Conversion](#timezone-conversion).
- Start-of-day and end-of-day computations are DST-safe: they never assume a day is exactly 24 hours long. The test suite explicitly covers `UTC`, `Asia/Kolkata` (no DST), `America/New_York` (DST), and `Europe/London` (DST), including spring-forward and fall-back transition days.

## Thread Safety

- Every class in `io.github.rajeshpatildev.dateutil` is `final`, has a private constructor, and holds **no mutable instance or static state** beyond an internal, thread-safe formatter cache (a `ConcurrentHashMap` of immutable `DateTimeFormatter` instances).
- All formatting and parsing goes through `java.time.format.DateTimeFormatter`, which — unlike the legacy `java.text.SimpleDateFormat` — is immutable and safe to share across threads.
- Because there is no shared mutable state anywhere in the library, every public method is safe to call concurrently from any number of threads without external synchronization.

## Using date-util in a Spring Boot Application

No Spring-specific integration is required. Add the Maven dependency and start calling the static methods directly — there are no beans, no `@Configuration` classes, and no auto-configuration to opt into or out of:

```java
import io.github.rajeshpatildev.dateutil.DateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
public class InvoiceController {

    @GetMapping("/invoices/due-date")
    public String dueDate() {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = DateUtils.addDays(today, 30);
        return DateUtils.formatDate(dueDate, "yyyy-MM-dd");
    }
}
```

Because the library has zero runtime dependencies, it will never introduce a version conflict with Spring Boot's own dependency management (Jackson, Spring Core, etc.).

## Build Instructions

Requires **JDK 17+** and **Maven 3.8+**.

```bash
# Run the full unit test suite
mvn clean test

# Build the JAR (also runs tests, and attaches sources + Javadoc JARs)
mvn clean package

# Install into your local ~/.m2 repository for use in other local projects
mvn clean install
```

A successful `mvn clean package` produces:

```
target/date-util-1.0.0.jar
target/date-util-1.0.0-sources.jar
target/date-util-1.0.0-javadoc.jar
```

Optional static-analysis checks (not part of the default build; used by the `code-review` GitHub Actions workflow):

```bash
mvn checkstyle:check
mvn pmd:check
```

## Continuous Integration & Delivery

This repository ships with a full GitHub Actions CI/CD pipeline under [`.github/workflows`](.github/workflows):

| Workflow | Purpose |
|---|---|
| [`ci.yml`](.github/workflows/ci.yml) | Builds and runs the full test suite on every push/PR against JDK 17 and 21, and uploads the built JARs as build artifacts. |
| [`code-review.yml`](.github/workflows/code-review.yml) | Automated code review on every pull request: Checkstyle, PMD, and a Javadoc completeness check. |
| [`security-scan.yml`](.github/workflows/security-scan.yml) | CodeQL static application security testing (SAST), OWASP Dependency-Check for known-vulnerable dependencies, and gitleaks secret scanning — on every push/PR and a weekly schedule. |
| [`docker-publish.yml`](.github/workflows/docker-publish.yml) | Builds a reproducible Docker image containing the built JARs and publishes it to GitHub Container Registry (GHCR). |
| [`release.yml`](.github/workflows/release.yml) | Triggered by pushing a `vX.Y.Z` tag: builds, tests, and publishes a GitHub Release with the JAR, sources JAR, and Javadoc JAR attached. |

> **Enabling full OWASP Dependency-Check scanning:** the NVD API used by `security-scan.yml` requires a registered API key for reliable access. Request a free key at [nvd.nist.gov/developers/request-an-api-key](https://nvd.nist.gov/developers/request-an-api-key) and add it as the repository secret `NVD_API_KEY` (Settings → Secrets and variables → Actions). Without it, that one step is skipped gracefully rather than failing the workflow; CodeQL and gitleaks still run on every push and pull request.

## Running with Docker

`date-util` is a library, not a service, so the Docker image is a **reproducible build / distribution artifact carrier** rather than something you run as a process. It is useful for extracting a build of the JAR without needing a local Maven/JDK setup, or without a private Maven repository:

```bash
docker build -t date-util:local .
docker create --name date-util-extract date-util:local
docker cp date-util-extract:/artifacts/. ./out
docker rm date-util-extract
```

`./out` will then contain `date-util-1.0.0.jar` (and any other JARs produced by the build).

## Versioning

`date-util` follows [Semantic Versioning 2.0.0](https://semver.org/) (`MAJOR.MINOR.PATCH`):

- **MAJOR** — incompatible public API changes (a method signature changes or a public method is removed).
- **MINOR** — backward-compatible new functionality (a new method or class is added).
- **PATCH** — backward-compatible bug fixes that do not change the public API.

The current version is **1.0.0**. To cut a release, update the `<version>` in `pom.xml`, commit, and push a matching `vX.Y.Z` git tag (for example `v1.0.0`) — the [`release.yml`](.github/workflows/release.yml) workflow verifies the tag matches `pom.xml` and publishes the GitHub Release automatically.

## License

Released under the [MIT License](https://opensource.org/licenses/MIT).
