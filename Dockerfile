FROM openjdk:17
COPY target/archiver-0.0.1.jar archiver-api.jar
ENTRYPOINT ["java", "-jar", "archiver-api.jar"]