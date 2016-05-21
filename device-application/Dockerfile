FROM qaware-oss-docker-registry.bintray.io/base/debian8-jre8
MAINTAINER QAware GmbH <qaware-oss@qaware.de>

RUN mkdir -p /opt
ADD build/distributions/device-application-1.0.0.tar /opt

RUN cd /opt/device-application; chmod 755 bin/device-application

EXPOSE 8080
ENTRYPOINT exec ./bin/device-application
