#!/usr/bin/env bash
nginx
cd /usr/local/bin
cat /etc/nginx/nginx.conf
python3 log-docker-server.py &
java -jar -Dspring.profiles.active=scheduler \
  /usr/local/bin/twitter-fetcher-1.0.0-SNAPSHOT.jar \
  --org.jesperancinha.twitter.searchTerm \
  --org.jesperancinha.twitter.consumerKey=Jyrj0HRFWwmE6mbxuJTSwjcJ7 \
  --org.jesperancinha.twitter.consumerSecret=jyYtPCauiNawrBNoK56nAMUVK6LHIlN2wiItnhgd5LdKZgK2fW \
  --org.jesperancinha.twitter.token=1200086940604928005-8CVE1ZMl198Vqyxqcuol6qSsItQogZ \
  --org.jesperancinha.twitter.tokenSecret=KxEbWs4oqhc8RvoBm7HYJSTQXrBqbooXewgv9mSoJDNFQ \
  --org.jesperancinha.twitter.searchTerm=ritaora
tail -f /dev/null
