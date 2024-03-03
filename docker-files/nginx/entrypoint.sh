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
#replaceIp news-cast-fetcher
#replaceIp news-cast-orchestration
#replaceIp news-cast-choreography
#replaceIp news-cast-mock

nginx -t

nginx

tail -f /dev/null
