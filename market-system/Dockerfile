FROM gradle:8.7-jdk17 AS builder

WORKDIR /app

COPY settings.gradle.kts ./
COPY gradle.properties ./
COPY gradle ./gradle
COPY app/build.gradle.kts ./app/build.gradle.kts

RUN gradle :app:dependencies --no-daemon || true

COPY . .
RUN gradle :app:installDist --no-daemon

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=builder /app/app/build/install/app /app

CMD ["/app/bin/app"]