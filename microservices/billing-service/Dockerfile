FROM qaware/zulu-centos-payara-micro:8u162-4.181

COPY build/postgresql/* /opt/payara/libs/
COPY build/hazelcast/* /opt/payara/libs/
COPY build/opentracing/* /opt/payara/libs/
COPY build/activemq/activemq-rar-5.15.3.rar /opt/payara/deployments/
COPY post-deploy.asadmin /opt/payara/post-deploy.asadmin

CMD ["--maxHttpThreads", "25", "--addjars", "/opt/payara/libs/", "--hzconfigfile", "/billing-service/config/hazelcast.xml", "--postdeploycommandfile", "/opt/payara/post-deploy.asadmin", "--name", "billing-service"]

COPY build/libs/billing-service.war /opt/payara/deployments/
