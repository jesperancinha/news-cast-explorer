FROM jesperancinha/java-exercise-docker:0.0.2

ENV runningFolder /usr/local/bin/

WORKDIR ${runningFolder}

RUN mv /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.disabled

COPY java-exercise-fetcher/target/java-exercise-fetcher-1.0.0-SNAPSHOT.jar ${runningFolder}

COPY java-exercise-log-service/log-docker-server.py ${runningFolder}

COPY docker-entrypoint.sh ${runningFolder}

COPY docker-files/default.conf /etc/nginx/conf.d/default.conf

COPY docker-files/nginx.conf /etc/nginx/nginx.conf

COPY java-exercise-fe/dist /usr/share/nginx/html

RUN nginx -t

EXPOSE 5000

EXPOSE 4200

ENTRYPOINT ["docker-entrypoint.sh"]
