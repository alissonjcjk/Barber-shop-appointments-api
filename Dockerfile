FROM eclipse-temurin:21-jdk AS builder
WORKDIR /barber-dev-api
COPY . .
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /barber-dev-api
COPY --from=builder /barber-dev-api/build/libs/*.jar app.jar
EXPOSE 8080 5005
ENTRYPOINT ["java", "-jar", "app.jar"]
