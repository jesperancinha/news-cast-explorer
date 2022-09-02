#!/usr/bin/env sh

java -jar -Dspring.profiles.active=docker,scheduler news-cast-explorer-fetcher.jar
