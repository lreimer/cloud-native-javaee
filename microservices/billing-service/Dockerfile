FROM payara/server-full:181

ENV AS_ADMIN $PAYARA_PATH/bin/asadmin
ENV DOMAIN domain1

COPY build/activemq/activemq-rar-5.15.3.rar /tmp/
COPY build/postgresql/* ${PAYARA_PATH}/glassfish/domains/${PAYARA_DOMAIN}/lib/
COPY build/hazelcast/* ${PAYARA_PATH}/glassfish/lib/
COPY domain-config.asadmin /tmp/

RUN $AS_ADMIN start-domain $DOMAIN && \
    $AS_ADMIN --user admin --passwordfile=/opt/pwdfile multimode --file /tmp/domain-config.asadmin && \
    $AS_ADMIN stop-domain $DOMAIN

COPY build/libs/billing-service.war /opt/payara41/glassfish/domains/domain1/autodeploy/
