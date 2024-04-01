# syntax=docker/dockerfile:1.4

FROM --platform=$BUILDPLATFORM maven:3.9.6-amazoncorretto-21 AS builder
WORKDIR /workdir
COPY . /workdir
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21.0.2_13-jre
EXPOSE 8080
VOLUME /tmp
COPY --from=builder /workdir/firebase-config.json /app/firebase-config.json
COPY --from=builder /workdir/memori/target/memori-0.0.1-SNAPSHOT.jar /app/memori.jar
# ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8080"
ENTRYPOINT ["java","-jar","/app/memori.jar"]
