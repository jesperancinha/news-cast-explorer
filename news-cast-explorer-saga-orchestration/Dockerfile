FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /root

ENV LANG=C.UTF-8

COPY entrypoint.sh /root

COPY target/news-cast-explorer-saga-orchestration-*.jar /root/news-cast-explorer-saga-orchestration.jar

ENTRYPOINT ["/root/entrypoint.sh"]

EXPOSE 8082
