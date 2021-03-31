FROM 267352753410.dkr.ecr.us-east-1.amazonaws.com/openjdk:8
COPY target/*.jar digital-log.jar
ENTRYPOINT ["java","-jar","digital-log.jar"]
