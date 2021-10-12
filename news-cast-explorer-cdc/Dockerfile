FROM openjdk:17-slim-buster

WORKDIR /root

ENV LANG=C.UTF-8

COPY entrypoint.sh /root

COPY target/news-cast-explorer-cdc-*.jar /root/news-cast-explorer-cdc.jar

ENTRYPOINT ["/root/entrypoint.sh"]

EXPOSE 8085
