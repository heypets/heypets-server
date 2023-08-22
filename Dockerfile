FROM openjdk:17-jdk
LABEL authors="changmin.kim"
EXPOSE 8080

COPY build/libs/heypetsserver-0.0.1-SNAPSHOT.jar /app/heypetsserver.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "heypetsserver.jar"]