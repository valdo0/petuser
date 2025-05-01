FROM openjdk:17-jdk-slim

WORKDIR /app
COPY target/petuser-0.0.1-SNAPSHOT.jar app.jar
COPY WALLET_BBDD_S1 /app/oracle_wallet
EXPOSE 8080

CMD [ "java", "-jar", "app.jar" ]