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

#Make directory and download java agent to it of your version
RUN mkdir /opt/cdbg && \
     wget -qO- https://storage.googleapis.com/cloud-debugger/archive/java/2.27/cdbg_java_agent_gce.tar.gz | \
     tar xvz -C /opt/cdbg

# create a directory for cloud profile. 
RUN mkdir -p /opt/cprof && \
  wget -q -O- https://storage.googleapis.com/cloud-profiler/java/latest/profiler_java_agent.tar.gz \
  | tar xzv -C /opt/cprof

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/e-payment-0.0.1-SNAPSHOT.jar /app/e-payment.jar
WORKDIR /
#Add Java agent to while starting application
ENTRYPOINT ["java","-agentpath:/opt/cdbg/cdbg_java_agent.so,/opt/cprof/profiler_java_agent.so=-logtostderr","-Dcom.google.cdbg.module=e-payment-gke","-Dcom.google.cdbg.version=2.0","-Dcom.google.cdbg.breakpoints.enable_canary=true","-cprof_enable_heap_sampling=true","-cprof_service=epayment-gke","-cprof_service_version=1.0","-jar","/app/e-payment.jar"]