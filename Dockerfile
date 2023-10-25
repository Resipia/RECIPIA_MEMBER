# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-alpine

# Make port 8081 available to the world outside this container
EXPOSE 8081

# Specify the built JAR file path and add it to the container as app.jar
ARG JAR_FILE=build/libs/member-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} member-api.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/member-api.jar"]