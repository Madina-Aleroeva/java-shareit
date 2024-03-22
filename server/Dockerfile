FROM amazoncorretto:11
WORKDIR /opt

COPY target/*.jar /opt/app.jar
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]