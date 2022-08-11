FROM openjdk:20-slim-buster

WORKDIR /root

ENV LANG=C.UTF-8

COPY entrypoint.sh /root

COPY target/news-cast-mock-*.jar /root/news-cast-mock.jar

ENTRYPOINT ["/root/entrypoint.sh"]

EXPOSE 8081

