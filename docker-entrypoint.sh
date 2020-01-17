#!/usr/bin/env bash
nginx
cd /usr/local/bin
cat /etc/nginx/nginx.conf
python3 log-docker-server.py &
java -jar -Dspring.profiles.active=scheduler /usr/local/bin/java-exercise-fetcher-1.0.0-SNAPSHOT.jar --org.jesperancinha.twitter.consumerKey=<consumerKey> --org.jesperancinha.twitter.consumerSecret=<consumerSecret> --org.jesperancinha.twitter.token=<token> --org.jesperancinha.twitter.tokenSecret=<tokenSecret>