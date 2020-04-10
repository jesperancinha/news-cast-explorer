FROM jesperancinha/je-all-build-jdk-14:0.0.1

ENV runningFolder /usr/local/bin/

WORKDIR ${runningFolder}

RUN mv /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.disabled

COPY twitter-fetcher/target/twitter-fetcher-1.0.0-SNAPSHOT.jar ${runningFolder}

COPY twitter-logger/log-docker-server.py ${runningFolder}

COPY docker-entrypoint.sh ${runningFolder}

COPY docker-files/default.conf /etc/nginx/conf.d/default.conf

COPY docker-files/nginx.conf /etc/nginx/nginx.conf

COPY twitter-explorer-fe/dist /usr/share/nginx/html

RUN nginx -t

EXPOSE 5000

EXPOSE 4200

ENTRYPOINT ["docker-entrypoint.sh"]
