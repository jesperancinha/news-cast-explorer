# Twitter Explorer
[![Bitbucket Pipelines branch](https://img.shields.io/bitbucket/pipelines/jesperancinha/twitter-explorer/master)](https://bitbucket.org/jesperancinha/twitter-explorer/addon/pipelines/home#!/)
[![CircleCI](https://circleci.com/bb/jesperancinha/twitter-explorer.svg?style=svg)](https://circleci.com/bb/jesperancinha/twitter-explorer)

This is an exercise made with a combination of different languages.

There are three modules:

- java-exercise-fe - This is the front end of the application in Angular 8
- java-exercise-fetcher - This is the java Spring Boot command line runner developed in Java 12
- java-exercise-log-service - These are python scripts to serve the logs.

Logs are placed in the workspace of the execution environment of the working jar

These logs are read using two scripts

- logserver.py - For local runs
- log-docker-server.py - To be used in the docker image builds

The front end runs with NPM

I have provided a few utilities:

- docker-files - Here live the files used to make the docker image
- docker-files/pushed-image - This is the docker file for the image I've created in Docker up.
- Dockerfile - This is another docker image which uses the image jesperancinha/java-exercise-docker:0.0.2, that I have created as a starting point
- build.sh - In this bashscript you can find all the commands used to build the project, package it and mount everything in the docker container
- docker-init.sh - Does almost the same thing, but only executes docker commands.

The image I creaged it's generic and it's available on [dockerhub](https://hub.docker.com/r/jesperancinha/java-exercise-docker).

[![dockeri.co](https://dockeri.co/image/jesperancinha/java-exercise-docker)](https://hub.docker.com/r/jesperancinha/java-exercise-docker)

The repo of the source code for this image has moved. All code for my docker images will now reside in a separate repo:

[https://bitbucket.org/jesperancinha/docker-images/](https://bitbucket.org/jesperancinha/docker-images/)

The reason for this is that I could not find any image suited for what I wanted.
I needed:

- NGINX
- A fully compatible java distribution (12 or 13)
- Cron tabs

The command line runner has the option to choose the profile "scheduler".
Starting the application with this profile will cause it to never stop and it will launch the Twitter fetcher in the interval specified in org.jesperancinha.twitter.cron of the application.properties file.

## Installation Notes

### Java version

```bash
sdk use java 13.0.1.hs-adpt
```

### Python

```bash
sudo pip install flask
```

### SSH Docker Container

```bash
docker exec -it jef-nginx /bin/bash
```

## Starting the application

This is a Spring Boot application and you can customize paramters via the command line.
This is an example:

```text
java -jar 
-Dspring.profiles.active=scheduler 
/usr/local/bin/java-exercise-fetcher-1.0.0-SNAPSHOT.jar 
--org.jesperancinha.twitter.consumerKey=<consumerKey> 
--org.jesperancinha.twitter.consumerSecret=<consumerSecret> 
--org.jesperancinha.twitter.token=<token> 
--org.jesperancinha.twitter.tokenSecret=<tokenSecret>
--org.jesperancinha.twitter.searchTerm=<searchTerm>
--org.jesperancinha.twitter.capacity=<capacity>
--org.jesperancinha.twitter.timeToWaitSeconds=<timeToWaitSeconds>
```

## About me

[![Twitter Follow](https://img.shields.io/twitter/follow/joaofse?label=João%20Esperancinha&style=social)](https://twitter.com/joaofse)
[![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=jesperancinha&style=social)](https://github.com/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=BitBucket&message=jesperancinha&color=navy)](https://bitbucket.org/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=jesperancinha&color=navy)](https://gitlab.com/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=joaofilipesabinoesperancinha.nl&color=6495ED)](http://joaofilipesabinoesperancinha.nl)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Time%20Disruption%20Studios&color=6495ED)](http://tds.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Image%20Train%20Filters&color=6495ED)](http://itf.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=MancalaJE&color=6495ED)](http://mancalaje.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Articles&message=On%20The%20Web&color=purple)](https://github.com/jesperancinha/project-signer/blob/master/project-signer-templates/LossArticles.md)
[![Generic badge](https://img.shields.io/static/v1.svg?label=DEV&message=Profile&color=green)](https://dev.to/jofisaes)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Medium&message=@jofisaes&color=green)](https://medium.com/@jofisaes)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Hackernoon&message=@jesperancinha&color=green)](https://hackernoon.com/@jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Free%20Code%20Camp&message=jofisaes&color=008000)](https://www.freecodecamp.org/jofisaes)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Hackerrank&message=jofisaes&color=008000)](https://www.hackerrank.com/jofisaes)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Code%20Forces&message=jesperancinha&color=008000)](https://codeforces.com/profile/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Coder%20Byte&message=jesperancinha&color=008000)](https://coderbyte.com/profile/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Code%20Wars&message=jesperancinha&color=008000)](https://www.codewars.com/users/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Acclaim%20Badges&message=joao-esperancinha&color=red)](https://www.youracclaim.com/users/joao-esperancinha/badges)
[![Generic badge](https://img.shields.io/static/v1.svg?label=All%20Badges&message=Badges&color=red)](https://github.com/jesperancinha/project-signer/blob/master/project-signer-templates/Badges.md)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Status&message=Project%20Status&color=red)](https://github.com/jesperancinha/project-signer/blob/master/project-signer-templates/Status.md)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Google%20Apps&message=Joao+Filipe+Sabino+Esperancinha&color=orange)](https://play.google.com/store/apps/developer?id=Joao+Filipe+Sabino+Esperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Code%20Pen&message=jesperancinha&color=orange)](https://codepen.io/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=ITF%20Chartizate%20Android&color=yellow)](https://github.com/JEsperancinhaOrg/itf-chartizate-android)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=ITF%20Chartizate%20Java&color=yellow)](https://github.com/JEsperancinhaOrg/itf-chartizate-modules/tree/master/itf-chartizate-java)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=ITF%20Chartizate%20API&color=yellow)](https://github.com/JEsperancinhaOrg/itf-chartizate/tree/master/itf-chartizate-api)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=Markdowner%20Core&color=yellow)](https://github.com/jesperancinha/markdowner/tree/master/markdowner-core)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=Markdowner%20Filter&color=yellow)](https://github.com/jesperancinha/markdowner/tree/master/markdowner-filter)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Docker%20Images&message=jesperanciha&color=099CEC)](https://github.com/jesperancinha/project-signer/blob/master/project-signer-templates/DockerImages.md)
