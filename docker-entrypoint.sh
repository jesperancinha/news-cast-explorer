#!/usr/bin/env bash
nginx
cd /usr/local/bin
cat /etc/nginx/nginx.conf
python3 log-docker-server.py &
java -jar -Dspring.profiles.active=scheduler --org.jesperancinha.twitter.consumerKey=<consumerKey> --org.jesperancinha.twitter.consumerSecret=<consumerSecret> --org.jesperancinha.twitter.token=<token> --org.jesperancinha.twitter.tokenSecret=<tokenSecret>