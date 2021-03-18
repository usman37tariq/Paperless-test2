FROM openjdk:8
COPY target/*.jar digital-log.jar
ENTRYPOINT ["java","-jar","digital-log.jar"]
