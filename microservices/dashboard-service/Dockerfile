FROM qaware/zulu-centos-payara-micro:8u162-4.181

COPY build/activemq/activemq-rar-5.15.3.rar /opt/payara/deployments/
COPY post-deploy.asadmin /opt/payara/post-deploy.asadmin

CMD ["--postdeploycommandfile", "/opt/payara/post-deploy.asadmin"]

COPY build/libs/dashboard-service.war /opt/payara/deployments/
