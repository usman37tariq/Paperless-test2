FROM openjdk:8
ADD target/paperless-backend-2.0.0-SNAPSHOT-2021-01-26-04-20-08.jar paperless-backend-2.0.0-SNAPSHOT-2021-01-26-04-20-08
ENTRYPOINT ["java","-jar","paperless-backend-2.0.0-SNAPSHOT-2021-01-26-04-20-08.jar"]