FROM openjdk:17-slim-buster

WORKDIR /root

ENV LANG=C.UTF-8

COPY entrypoint.sh /root

COPY target/news-cast-explorer-fetcher-*.jar /root/news-cast-explorer-fetcher.jar

ENTRYPOINT ["/root/entrypoint.sh"]

EXPOSE 8080
