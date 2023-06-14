FROM openjdk:13-alpine3.10
EXPOSE 8081
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-Djdk.tls.client.protocols=TLSv1.2", "-jar","/app.jar"]
