FROM openjdk:21
LABEL authors="Aleksandr Zemliakov"

COPY target/crypto-recommendation-app-*.jar crypto-recommendation-app.jar

ENTRYPOINT ["java", "-jar", "crypto-recommendation-app.jar"]