FROM openjdk:21
LABEL authors="Aleksandr Zemliakov"

COPY target/crypto-recommendation-loader-*.jar crypto-recommendation-loader.jar

ENTRYPOINT ["java", "-jar", "crypto-recommendation-loader.jar"]