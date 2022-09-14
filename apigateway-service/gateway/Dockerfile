FROM openjdk:11
ARG JAR_FILE=/build/libs/gateway-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} mbtiGateway.jar
ENTRYPOINT ["java","-jar","/mbtiGateway.jar"]

#docker build -t mbtiapigateway .
#docker run -d -p 5555:5555 --name mbtiapigateway mbtiapigateway