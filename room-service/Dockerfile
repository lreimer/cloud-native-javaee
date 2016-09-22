FROM qaware-oss-docker-registry.bintray.io/base/alpine-k8s-ibmjava8:8.0-3.10
MAINTAINER M.-Leander Reimer <mario-leander.reimer@qaware.de>

RUN mkdir -p /app
ADD build/distributions/room-application-1.0.0.tar /app

WORKDIR /app/room-application-1.0.0
RUN chmod 755 bin/room-application

EXPOSE 18080

CMD ./bin/room-application