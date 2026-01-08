FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
# Bağımlılıkları önbelleğe al
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Run aşaması
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Uygulama JAR dosyasını kopyala
COPY --from=builder /app/target/*.jar app.jar
# Sağlık kontrolü için actuator endpoint'ini aç
EXPOSE 8080
# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]