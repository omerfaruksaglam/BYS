FROM openjdk:17-jdk-slim
WORKDIR /app
# Uygulamanın jar dosyası container içine kopyalanır
COPY ./target/Nursapp-0.0.1-SNAPSHOT.jar /app/app.jar

# Uygulama docker profili ile başlatılır, tam yolu ile
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]