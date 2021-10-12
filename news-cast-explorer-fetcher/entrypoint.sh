#!/usr/bin/env bash

java -jar -Dspring.profiles.active=docker,scheduler news-cast-explorer-fetcher.jar
