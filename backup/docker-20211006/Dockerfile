FROM jesperancinha/je-all-build-jdk-14:0.0.1

ENV runningFolder /usr/local/bin/

WORKDIR ${runningFolder}

RUN mv /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.disabled

COPY twitter-fetcher/target/twitter-fetcher*.jar ${runningFolder}/twitter-fetcher.jar

COPY ../news-cast-logger2021-10-08/log-docker-server.py ${runningFolder}

COPY docker-entrypoint.sh ${runningFolder}

COPY ../../docker-files/nginx/default.conf /etc/nginx/conf.d/default.conf

COPY ../../docker-files/nginx/nginx.conf /etc/nginx/nginx.conf

COPY ../../news-cast-explorer-fe/dist /usr/share/nginx/html

RUN nginx -t

EXPOSE 80

ENTRYPOINT ["docker-entrypoint.sh"]
