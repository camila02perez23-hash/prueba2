FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/sigprodparcial-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8217
ENTRYPOINT ["java", "-jar", "app.jar"]

