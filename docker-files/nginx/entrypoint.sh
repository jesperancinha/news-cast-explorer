#!/bin/bash

#function replaceIp() {
#    service=$1
#    ip=$(getent hosts "$service" | awk '{ print $1 }')
#    sed -i 's/'"$service"'/'"$ip"'/g' /etc/nginx/conf.d/default.conf
#}
#function replaceIp() {
#    service=$1
#    getent hosts "$service" >> /etc/hosts
#}
#
#replaceIp news_cast_fetcher
#replaceIp news_cast_orchestration
#replaceIp news_cast_choreography
#replaceIp news_cast_mock

nginx -t

nginx

tail -f /dev/null
