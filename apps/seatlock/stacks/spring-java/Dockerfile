FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

FROM eclipse-temurin:21-jre
RUN useradd --system --no-create-home app
WORKDIR /app
COPY --from=build /src/target/seatlock.jar ./seatlock.jar
USER app
EXPOSE 3000
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/seatlock.jar"]
