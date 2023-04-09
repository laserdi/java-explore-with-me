FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats.jar
ENTRYPOINT ["java","-jar","/stats.jar"]