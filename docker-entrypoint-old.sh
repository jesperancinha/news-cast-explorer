#!/usr/bin/env bash
nginx
cd /usr/local/bin
cat /etc/nginx/nginx.conf
python3 log-docker-server.py &
java -jar -Dspring.profiles.active=scheduler \
  /usr/local/bin/twitter-fetcher-1.0.0-SNAPSHOT.jar \
  --org.jesperancinha.twitter.consumerKey=AAAAAAAAAAAAAAAAAAAAAAAAA \
  --org.jesperancinha.twitter.consumerSecret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA \
  --org.jesperancinha.twitter.token=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA \
  --org.jesperancinha.twitter.tokenSecret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA \
  --org.jesperancinha.twitter.searchTerm=aaaaaaa
tail -f /dev/null
