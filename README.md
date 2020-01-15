# Java exercise jofisaes

I created an image to help me with this exercise. I made it generic and it's available on [dockerhub](https://hub.docker.com/r/jesperancinha/java-exercise-docker).

[![dockeri.co](https://dockeri.co/image/jesperancinha/java-exercise-docker)](https://hub.docker.com/r/jesperancinha/java-exercise-docker)


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
