FROM amazoncorretto:20-alpine
LABEL authors="Mateusz Kolaczyk"

ENV JAVA_OPTS "-Dspring.profiles.active=DEV -Xms512m -Xmx1g"

WORKDIR producer

COPY ./target/producer-0.0.1-SNAPSHOT.jar ./

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "producer-0.0.1-SNAPSHOT.jar"]