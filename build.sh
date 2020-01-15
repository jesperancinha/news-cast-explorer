#!/usr/bin/env bash

mvn clean install

cd java-exercise-fe

npm install

npm run build

cd ..

docker-machine start dev

docker-machine env dev

eval $(docker-machine env dev)

docker stop jef-nginx

docker rm jef-nginx

docker build . -t jef-nginx-image

docker run --name jef-nginx -d -p 8080:80 jef-nginx-image