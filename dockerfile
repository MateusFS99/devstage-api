# Usa a imagem oficial do OpenJDK 21
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/devstage-api-0.0.1-SNAPSHOT.jar"]
