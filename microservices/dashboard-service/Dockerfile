FROM qaware/zulu-centos-payara-micro:8u162-5.181

COPY build/activemq/activemq-rar-5.15.3.rar /opt/payara/deployments/
COPY build/opentracing/* /opt/payara/libs/
COPY post-deploy.asadmin /opt/payara/post-deploy.asadmin

CMD ["--addjars", "/opt/payara/libs/", "--noCluster", "--postdeploycommandfile", "/opt/payara/post-deploy.asadmin"]

COPY build/libs/dashboard-service.war /opt/payara/deployments/
