#!/usr/bin/env bash

mvn clean install

cd twitter-explorer-fe

npm install

npm run build

cd ..

docker stop jef-nginx

docker rm jef-nginx

docker rmi jef-nginx-image

docker build . -t jef-nginx-image

docker run --name jef-nginx -d -p 8080:80 jef-nginx-image