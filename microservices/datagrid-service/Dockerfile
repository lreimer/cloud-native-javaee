FROM qaware/zulu-centos-payara-micro:8u162-4.181

COPY build/hazelcast/* /opt/payara/libs/
RUN mkdir -p /opt/payara/config/

CMD ["--hzconfigfile", "/opt/payara/config/hazelcast.xml", "--deploy", "/opt/payara/deployments/datagrid-service.war", "--addjars", "/opt/payara/libs/"]

COPY build/libs/datagrid-service.war /opt/payara/deployments/
