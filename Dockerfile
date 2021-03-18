FROM openjdk:8-jdk-alpine3.7
COPY target/*.jar digital-log.jar
ENTRYPOINT ["java","-jar","digital-log.jar"]