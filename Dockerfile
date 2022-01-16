FROM gradle:7.2.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim
LABEL key="madhi.krishnan@fyndna.com"
EXPOSE 8080

RUN  apt-get update \
  && apt-get install -y wget \
  && rm -rf /var/lib/apt/lists/*

RUN mkdir /opt/cdbg && \
     wget -qO- https://storage.googleapis.com/cloud-debugger/archive/java/2.27/cdbg_java_agent_gce.tar.gz | \
     tar xvz -C /opt/cdbg

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/e-payment-0.0.1-SNAPSHOT.jar /app/e-payment.jar
WORKDIR /

ENTRYPOINT ["java","-agentpath:/opt/cdbg/cdbg_java_agent.so","-Dcom.google.cdbg.module=e-payment","-Dcom.google.cdbg.version=1.0","-Dcom.google.cdbg.breakpoints.enable_canary=true","-jar","/app/e-payment.jar"]