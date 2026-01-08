# Sadece çalışma ortamı (JRE) kullan
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Jenkins'in oluşturduğu JAR'ı direkt kopyala
# JAR ismi projenin pom.xml'indeki ismine göre değişebilir
COPY target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]