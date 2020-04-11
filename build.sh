#!/usr/bin/env bash

mvn clean install

cd twitter-explorer-fe

npm install

npm run build

cd ..

docker stop twitter-explorer

docker rm twitter-explorer

docker rmi twitter-explorer-image

docker build . -t twitter-explorer-image

docker run --name twitter-explorer -d -p 8080:80 twitter-explorer-image