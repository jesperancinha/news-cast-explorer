#!/usr/bin/env bash

docker stop jef-nginx

docker rm jef-nginx

docker rmi jef-nginx-image
