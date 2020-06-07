FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=./backend/build/libs/backend-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} backend-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/backend-0.0.1-SNAPSHOT.jar"]