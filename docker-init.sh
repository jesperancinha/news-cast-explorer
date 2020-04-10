#!/usr/bin/env bash

docker-machine startdocker-machine enveval $(docker-machine env dev)

docker stop jef-nginx

docker rm jef-nginx

docker build . -t jef-nginx-image

docker run --name jef-nginx -d -p 8080:80 jef-nginx-image