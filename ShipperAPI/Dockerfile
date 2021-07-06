FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080
VOLUME /tmp
ADD ShipperAPI-0.0.1-SNAPSHOT.jar ShipperAPI-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ShipperAPI-0.0.1-SNAPSHOT.jar"]