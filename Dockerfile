FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

COPY target/sql2entity-1.0.0.jar app.jar

EXPOSE 8080

ENV PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]
