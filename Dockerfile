FROM openjdk:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /cac-service.jar
ENTRYPOINT ["java","-jar","/cac-service.jar"]