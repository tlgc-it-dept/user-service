FROM amazoncorretto:17
EXPOSE 8081
COPY target/*.jar /user_service.jar
COPY .env /
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "user_service.jar"]


