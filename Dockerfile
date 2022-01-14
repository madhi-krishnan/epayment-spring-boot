ARG VERSION=v1.0
FROM gradle:7.2.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble

FROM alpine as agent
USER root
RUN mkdir /opt/cdbg && \
     wget -qO- https://storage.googleapis.com/cloud-debugger/archive/java/2.27/cdbg_java_agent_gce.tar.gz | \
     tar xvz -C /opt/cdbg


FROM openjdk:11-jre-slim
LABEL key="madhi.krishnan@fyndna.com"
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/e-payment-0.0.1-SNAPSHOT.jar /app/e-payment.jar
COPY --from=agent /agent/cdbg_java_agent.so agent.so
EXPOSE 8080
ENTRYPOINT [ "java", "-agentpath:/opt/cdbg/cdbg_java_agent.so", "-Dcom.google.cdbg.breakpoints.enable_canary=false", "-jar", "/app/e-payment.jar"]