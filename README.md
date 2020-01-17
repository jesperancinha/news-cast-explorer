# Java exercise jofisaes
[![Bitbucket Pipelines branch](https://img.shields.io/bitbucket/pipelines/jesperancinha/java-exercise-jofisaes/master)](https://bitbucket.org/jesperancinha/java-exercise-jofisaes/addon/pipelines/home#!/)
[![CircleCI](https://circleci.com/bb/jesperancinha/java-exercise-jofisaes.svg?style=svg)](https://circleci.com/bb/jesperancinha/java-exercise-jofisaes)

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

### Dockerfile notes

```bash
# If exists
docker-machine rm dev 

docker-machine create --driver virtualbox dev

docker-machine env dev

eval $(docker-machine env dev)

# If not started
docker-machine start dev

docker build .

docker-machine ssh dev

docker ps -a

#Container access
docker exec -it <container ID> /bin/bash

docker run -p 80:80 "<image ID>"

docker run -d -v /webroot:/var/www/html -p 80:80 --name "<name of container>" "<image ID>"

docker run -it ubuntu sh

docker run ubuntu

docker-machine ip dev

docker build . -t "<image ID>"

docker run "<container ID>" -d -p 8080:80 -p 5000:5000 "<image ID>"

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


## Description
This development test is used as part of selection process for Development Engineers. You are requested to develop a simple application that covers all the requirements listed below. To have an indication of the criteria that will be used to judge your submission, all the following are considered as metrics of good development:

- Correctness of the implementation
- Decent test coverage
- Code cleanliness
- Efficiency of the solution
- Careful choice of tools and data formats
- Use of production-ready approaches

While no specific time limit is mandated to complete the exercise, you will be asked to provide your code within a given deadline from your HR/hiring manager. You are free to choose any library. 
Task
We would like you to write code that will cover the functionality explained below and provide us with the source, instructions to build and run the application as well as a sample output of an execution:
- Connect to the Twitter Streaming API  (https://developer.twitter.com/en/docs )
     - Use the following values:
        - Consumer Key: <you receive a consumer key>
        - Consumer Secret: <your receive a secret>
     - The app name will be java-exercise
     - You will need to login with Twitter
- Filter messages that track on the trending topic of today or " rogerfederer"
- Retrieve the incoming messages for 30 seconds or up to 100 messages, whichever happens first.
- Your application should return the messages grouped by user (users sorted chronologically, ascending)
- The messages per user should also be sorted chronologically, ascending
- For each message, we will need the following:
    - The message ID
    - The creation date of the message as epoch value
    - The text of the message
    - The author of the message
- For each author, we will need the following:
    - The user ID
    - The creation date of the user as epoch value
    - The name of the user
    - The screen name of the user

## Attention points for true Backend Engineer’s:
- All the above information is provided in either SDTOUT or a log file
- You are free to choose the output format, provided that it makes it easy to parse and process by a machine

## Attention points for Frontend:
- Display all this information on an engaging front end (Choose your own favourite framework/library). 
- Make sure the frontend application can run with a mocking framework

## Bonus points for:
- Keep track of messages per second statistics across multiple runs of the application
- The application can run as a Docker container.
