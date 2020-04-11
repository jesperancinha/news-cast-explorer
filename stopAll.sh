#!/usr/bin/env bash

docker stop twitter-explorer

docker rm twitter-explorer

docker rmi twitter-explorer-image
