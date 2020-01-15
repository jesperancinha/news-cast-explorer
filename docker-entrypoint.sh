#!/usr/bin/env bash
nginx
cd /usr/local/bin
cat /etc/nginx/nginx.conf
python3 log-docker-server.py &
java -jar -Dspring.profiles.active=scheduler /usr/local/bin/java-exercise-fetcher-1.0.0-SNAPSHOT.jar consumerKey consumerSecret tokenKey tokenSecret rogerfederer 100
