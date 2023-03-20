FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /opt
COPY build/libs/challenge-0.0.1-SNAPSHOT.jar ./app.jar
CMD java $JAVA_OPTIONS -jar app.jar
