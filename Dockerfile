# syntax=docker/dockerfile:1
#
# date-util is a Java library (a JAR), not a runnable service, so this image does not run an
# application. It exists to (1) provide a fully reproducible, containerized build of the library
# independent of the host machine's local Maven/JDK setup, and (2) act as a distributable
# artifact container: the built date-util-1.0.0.jar (plus -sources.jar and -javadoc.jar) can be
# extracted from the final image with `docker create` + `docker cp` (see README.md), which is
# useful in environments without a private Maven repository.

# ---- Build stage -----------------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

# Leverage Docker layer caching: resolve dependencies before copying sources.
COPY pom.xml checkstyle.xml pmd-ruleset.xml ./
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package

# ---- Artifact stage ---------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine AS artifact

LABEL org.opencontainers.image.title="date-util" \
      org.opencontainers.image.description="Framework-independent java.time date/time utility library (JAR distribution image)" \
      org.opencontainers.image.source="https://github.com/rajesh-patil-dev/rp.dateutil" \
      org.opencontainers.image.licenses="MIT"

WORKDIR /artifacts

COPY --from=build /build/target/date-util-*.jar ./

# No ENTRYPOINT/CMD is provided: this image is a build/distribution artifact carrier, not a
# runnable service. Extract the JARs with:
#   docker create --name date-util-extract <image> && \
#   docker cp date-util-extract:/artifacts/. ./out && \
#   docker rm date-util-extract
