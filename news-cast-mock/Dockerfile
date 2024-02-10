FROM eclipse-temurin:21.0.2_13-jre-alpine

WORKDIR /root

ENV LANG=C.UTF-8

COPY entrypoint.sh /root

COPY target/news-cast-mock-*.jar /root/news-cast-mock.jar

ENTRYPOINT ["/root/entrypoint.sh"]

EXPOSE 8081

