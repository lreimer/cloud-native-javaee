FROM payara/micro:181

COPY build/libs/payara-hazelcast.war /opt/payara/deployments/
COPY build/hazelcast/* /opt/payara/libs/
RUN mkdir -p /opt/payara/config/

ENTRYPOINT ["java", "-server", "-XX:+UseG1GC", "-Xmx196m", "-Xss256k", "-XX:MaxMetaspaceSize=128m", "-XX:CompressedClassSpaceSize=32m", "-jar", "/opt/payara/payara-micro.jar"]
CMD ["--hzconfigfile", "/opt/payara/config/hazelcast.xml", "--deploy", "/opt/payara/deployments/payara-hazelcast.war", "--addjars", "/opt/payara/libs/"]
