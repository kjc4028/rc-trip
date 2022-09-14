FROM openjdk:11
ARG JAR_FILE=/build/libs/discovery-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} mbtiDiscovery.jar
ENTRYPOINT ["java","-jar","/mbtiDiscovery.jar"]

#docker build -t mbtidiscovery .
##docker run -d -p 8761:8761 --name mbtidiscovery mbtidiscovery