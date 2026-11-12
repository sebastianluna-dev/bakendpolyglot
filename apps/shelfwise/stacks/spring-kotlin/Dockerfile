FROM gradle:8.14-jdk21 AS build
WORKDIR /src
COPY settings.gradle.kts build.gradle.kts ./
RUN gradle --no-daemon -q dependencies --configuration runtimeClasspath > /dev/null
COPY src ./src
RUN gradle --no-daemon -q bootJar

FROM eclipse-temurin:21-jre
RUN useradd --system --no-create-home app
WORKDIR /app
COPY --from=build /src/build/libs/shelfwise.jar ./shelfwise.jar
USER app
EXPOSE 3000
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/shelfwise.jar"]
