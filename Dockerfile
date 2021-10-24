FROM gradle:7.2.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim
LABEL key="madhi.krishnan@fyndna.com"
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/e-payment-0.0.1-SNAPSHOT.jar /app/e-payment.jar
ENTRYPOINT ["java","-jar","/app/e-payment.jar"]