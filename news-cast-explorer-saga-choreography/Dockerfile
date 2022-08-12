FROM openjdk:20-slim-buster

WORKDIR /root

ENV LANG=C.UTF-8

COPY entrypoint.sh /root

COPY target/news-cast-explorer-saga-choreography-*.jar /root/news-cast-explorer-saga-choreography.jar

ENTRYPOINT ["/root/entrypoint.sh"]

EXPOSE 8083
