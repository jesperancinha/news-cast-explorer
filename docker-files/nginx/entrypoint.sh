#!/bin/bash

ip=$(getent hosts news_cast_fetcher | awk '{ print $1 }')

sed -i 's/news_cast_fetcher/'"$ip"'/g' /etc/nginx/conf.d/default.conf

nginx -t

nginx

tail -f /dev/null
