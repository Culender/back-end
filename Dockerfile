FROM openjdk:17-alpine

ARG JAR_FILE=/build/libs/back-end-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} goorm-project

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "goorm-project"]