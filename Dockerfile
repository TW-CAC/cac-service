FROM maven:3.6.3-jdk-8 AS MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/
COPY config /build/
WORKDIR /build/
RUN mvn dependency:go-offline
RUN mvn package

FROM openjdk:8-jre-alpine
WORKDIR /app
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n
COPY --from=MAVEN_BUILD /build/target/*.jar /app/cac-service.jar
ENTRYPOINT ["sh", "-c", "java -Dmongo.connection.string=mongodb://$DB_HOST -jar /app/cac-service.jar"]