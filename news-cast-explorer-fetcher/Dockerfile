FROM openjdk:19-alpine

WORKDIR /root

ENV LANG=C.UTF-8

RUN apk --no-cache add curl

COPY entrypoint.sh /root

COPY target/news-cast-explorer-fetcher.jar /root/news-cast-explorer-fetcher.jar

ENTRYPOINT ["/root/entrypoint.sh"]

EXPOSE 8080
