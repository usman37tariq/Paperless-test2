FROM 422193507691.dkr.ecr.us-east-1.amazonaws.com/openjdk:8-jdk-alpine3.8
COPY target/*.jar digital-log.jar
ENTRYPOINT ["java","-jar","digital-log.jar"]