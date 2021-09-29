# Twitter Explorer

---

[![Twitter URL](https://img.shields.io/twitter/url?logoColor=blue&style=social&url=https%3A%2F%2Fimg.shields.io%2Ftwitter%2Furl%3Fstyle%3Dsocial)](https://twitter.com/intent/tweet?text=Checkout%20this%20@bitbucket%20repo%20by%20@joaofse%20%F0%9F%91%A8%F0%9F%8F%BD%E2%80%8D%F0%9F%92%BB:%20https://bitbucket.org/jesperancinha/twitter-explorer/src/master/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=BitBucket&message=Twitter%20Explorer&color=informational)](https://bitbucket.org/jesperancinha/twitter-explorer)

[![Bitbucket Pipelines branch](https://img.shields.io/bitbucket/pipelines/jesperancinha/twitter-explorer/master)](https://bitbucket.org/jesperancinha/twitter-explorer/addon/pipelines/home#!/)
[![CircleCI](https://circleci.com/bb/jesperancinha/twitter-explorer.svg?style=svg)](https://circleci.com/bb/jesperancinha/twitter-explorer)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/f49ddf491c1c4eff8addac23e08b9a91)](https://www.codacy.com/bb/jesperancinha/twitter-explorer/dashboard?utm_source=jesperancinha@bitbucket.org&amp;utm_medium=referral&amp;utm_content=jesperancinha/twitter-explorer&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/d02c6697-4f3f-486a-9297-c45fc0f46402)](https://codebeat.co/projects/bitbucket-org-jesperancinha-twitter-explorer-master)

[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/f49ddf491c1c4eff8addac23e08b9a91)](https://www.codacy.com/bb/jesperancinha/twitter-explorer/dashboard?utm_source=jesperancinha@bitbucket.org&utm_medium=referral&utm_content=jesperancinha/twitter-explorer&utm_campaign=Badge_Coverage)
[![codecov](https://codecov.io/bb/jesperancinha/twitter-explorer/branch/master/graph/badge.svg?token=ZZQIS9S1LR)](https://codecov.io/bb/jesperancinha/twitter-explorer)
[![Coverage Status](https://coveralls.io/repos/bitbucket/jesperancinha/twitter-explorer/badge.svg?branch=master)](https://coveralls.io/bitbucket/jesperancinha/twitter-explorer?branch=master)

---

[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/docker-50.png)](https://www.docker.com/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/docker-compose-50.png)](https://docs.docker.com/compose/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/cassandra-50.png "Cassandra")](http://cassandra.apache.org/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/spring-50.png)](https://spring.io/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/spring-boot-50.png)](https://spring.io/projects/spring-boot)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/java-50.png "Java")](https://www.oracle.com/java/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/spock-50.png "Spock")](http://spockframework.org/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/groovy-50.png "Groovy")](https://groovy-lang.org/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/angular-50.png "Angular")](https://angular.io/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/python-50.png)](https://www.python.org/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/bash-50.png)](https://www.gnu.org/software/bash/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/jupiter5-50.png "Jupiter 5")](https://junit.org/junit5/docs/current/user-guide/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/mockito-50.png "Mockito")](https://site.mockito.org/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/assertj-50.png "AssertJ")](https://assertj.github.io/doc/)

---
What we want to do is to get the last x tweets about a certain word in the last y seconds or just all the tweets over that something that we could get in y seconds. 

In the case of a tweet, all that we are interested in is trendy subjects. Without tweets, our test case will just not be a very interesting one. 

For this reason we should pick something trending on Twitter like politics, successful artists like Madonna, Cher and Kylie Minogue, climate change issues and any other theme that we know is popular.

Just because we don't want to keep trying to find the best theme to work on, please use the mock demo provided.

It is about food, and it generates new examples everytime you run it

All data will finally be visualized in a webpage.

In order to handle data, we'll use the [Saga](https://microservices.io/patterns/data/saga.html) pattern.

There are three modules:

- [twitter-explorer-fe](./twitter-explorer-fe) - This is the front end of the application - Angular 9
- [twitter-fetcher](./twitter-fetcher) - This is the java Spring Boot command line runner - Java 17
- [twitter-logger](./twitter-logger)- These are python scripts to serve the logs - Python 3.8
- [twitter-explorer-demo](./twitter-explorer-demo) - The Payload generation for the mock twitter service - Python 3.8
- [twitter-aggregator](./twitter-aggregator) - An aggregator module - Java 17

Logs are placed in the workspace of the execution environment of the working jar

These logs are read using two scripts

-   logserver.py - For local runs
-   log-docker-server.py - To be used in the docker image builds

The front end runs with NPM

I have provided a few utilities:

-   docker-files - Here live the files used to make the docker image
-   docker-files/pushed-image - This is the docker file for the image I've created in Docker up.
-   Dockerfile - This is another docker image which uses the image jesperancinha/twitter-docker:0.0.2, that I have created as a starting point
-   build.sh - In this bashscript you can find all the commands used to build the project, package it and mount everything in the docker container
-   docker-init.sh - Does almost the same thing, but only executes docker commands.

The image used is available on [dockerhub](https://hub.docker.com/r/jesperancinha/je-all-build-jdk-14).

[![dockeri.co](https://dockeri.co/image/jesperancinha/je-all-build-jdk-14)](https://hub.docker.com/r/jesperancinha/je-all-build-jdk-14)

The repo of the source code for this image has moved. All code for my docker images will now reside in a separate repo:

[https://bitbucket.org/jesperancinha/docker-images/](https://bitbucket.org/jesperancinha/docker-images/)

The reason for this is that I could not find any image suited for what I wanted.	I needed:

-   NGINX
-   A fully compatible java distribution (12 or 13)
-   Cron tabs

The command line runner has the option to choose the profile "scheduler".	Starting the application with this profile will cause it to never stop and it will launch the Twitter fetcher in the interval specified in org.jesperancinha.twitter.cron of the application.properties file.

This project is also the official support project of my article on medium:   

👇👇👇👇👇👇🚧 Under Construction 🚧👇👇👇👇👇👇   

[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/medium-20.png "Medium")](https://medium.com/@jofisaes/what-did-they-say-tweeting-with-hosebird-client-d15b1e22058b) [What did they say? — Using Sagas in Choreography and Orchestration modes](https://medium.com/@jofisaes/what-did-they-say-tweeting-with-hosebird-client-d15b1e22058b)


## Run locally

Run the TwitterFetcherLauncher with the following parameters:

Please replace AAAAAAAAAAAAAAAAAAAAAAAAA accordingly:

```shell
--org.jesperancinha.twitter.consumerKey=AAAAAAAAAAAAAAAAAAAAAAAAA \ 
--org.jesperancinha.twitter.consumerSecret=AAAAAAAAAAAAAAAAAAAAAAAAA \ 
--org.jesperancinha.twitter.token=AAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAA \ 
--org.jesperancinha.twitter.tokenSecret=AAAAAAAAAAAAAAAAAAAAAAAAA \ 
--org.jesperancinha.twitter.searchTerm=AAAAAAAAAAAAAAAAAAAAAAAAA
```

```bash
curl http://localhost:8080/api/twitter/explorer/
```

## Installation Notes

### Java version

```bash
sdk install java 17-open
sdk use java 17-open
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

This is a Spring Boot application and you can customize paramters via the command line.	This is an example:

```text
java -jar 
-Dspring.profiles.active=scheduler 
/usr/local/bin/twitter-fetcher.jar 
--org.jesperancinha.twitter.consumerKey=<consumerKey> 
--org.jesperancinha.twitter.consumerSecret=<consumerSecret> 
--org.jesperancinha.twitter.token=<token> 
--org.jesperancinha.twitter.tokenSecret=<tokenSecret>
--org.jesperancinha.twitter.searchTerm=<searchTerm>
--org.jesperancinha.twitter.capacity=<capacity>
--org.jesperancinha.twitter.timeToWaitSeconds=<timeToWaitSeconds>
--org.jesperancinha.twitter.searchTerm=<searchWord>
```

## Roadmap

For roadmap information and current developments please check document [Roadmap.md](./Roadmap.md)

## References

- [Managing data consistency in a microservice architecture using Sagas - Implementing an orchestration-based saga](https://chrisrichardson.net/post/sagas/2019/12/12/developing-sagas-part-4.html)
- [Managing data consistency in a microservice architecture using Sagas - Implementing a choreography-based saga](https://chrisrichardson.net/post/sagas/2019/08/15/developing-sagas-part-3.html)
- [Choreography pattern with Springboot](https://www.google.com/amp/s/www.vinsguru.com/choreography-saga-pattern-with-spring-boot/amp/)
- [Spock Framework Reference Documentation](http://spockframework.org/spock/docs/1.1-rc-3/all_in_one.html#_helper_methods)
- [Interaction Based Testing with Spock](http://spockframework.org/spock/docs/1.0/interaction_based_testing.html)
- [Hosebird Client (hbc)](https://github.com/twitter/hbc)
- [Wikipedia Twitter](https://en.wikipedia.org/wiki/Twitter)
- [Bash tips: Colors and formatting (ANSI/VT100 Control sequences)](https://misc.flogisoft.com/bash/tip_colors_and_formatting)
- [JUnit 5 Parameter Resolution Example](https://howtoprogram.xyz/2016/10/28/junit-5-parameter-resolution-example/)

## About me 👨🏽‍💻🚀🏳️‍🌈

[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/JEOrgLogo-20.png "João Esperancinha Homepage")](http://joaofilipesabinoesperancinha.nl)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/medium-20.png "Medium")](https://medium.com/@jofisaes)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/credly-20.png "Credly")](https://www.credly.com/users/joao-esperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=joaofilipesabinoesperancinha.nl&color=6495ED "João Esperancinha Homepage")](https://joaofilipesabinoesperancinha.nl/)
[![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=jesperancinha&style=social "GitHub")](https://github.com/jesperancinha)
[![Twitter Follow](https://img.shields.io/twitter/follow/joaofse?label=João%20Esperancinha&style=social "Twitter")](https://twitter.com/joaofse)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=JEsperancinhaOrg&color=yellow "jesperancinha.org dependencies")](https://github.com/JEsperancinhaOrg)   
[![Generic badge](https://img.shields.io/static/v1.svg?label=Articles&message=Across%20The%20Web&color=purple)](https://github.com/jesperancinha/project-signer/blob/master/project-signer-templates/Articles.md)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Webapp&message=Image%20Train%20Filters&color=6495ED)](http://itf.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=All%20Badges&message=Badges&color=red "All badges")](https://joaofilipesabinoesperancinha.nl/badges)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Status&message=Project%20Status&color=red "Project statuses")](https://github.com/jesperancinha/project-signer/blob/master/project-signer-quality/Build.md)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/coursera-20.png "Coursera")](https://www.coursera.org/user/da3ff90299fa9297e283ee8e65364ffb)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/google-apps-20.png "Google Apps")](https://play.google.com/store/apps/developer?id=Joao+Filipe+Sabino+Esperancinha)   
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/sonatype-20.png "Sonatype Search Repos")](https://search.maven.org/search?q=org.jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/docker-20.png "Docker Images")](https://hub.docker.com/u/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/stack-overflow-20.png)](https://stackoverflow.com/users/3702839/joao-esperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/reddit-20.png "Reddit")](https://www.reddit.com/user/jesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/devto-20.png "Dev To")](https://dev.to/jofisaes)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/hackernoon-20.jpeg "Hackernoon")](https://hackernoon.com/@jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codeproject-20.png "Code Project")](https://www.codeproject.com/Members/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/github-20.png "GitHub")](https://github.com/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/bitbucket-20.png "BitBucket")](https://bitbucket.org/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/gitlab-20.png "GitLab")](https://gitlab.com/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/bintray-20.png "BinTray")](https://bintray.com/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/free-code-camp-20.jpg "FreeCodeCamp")](https://www.freecodecamp.org/jofisaes)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/hackerrank-20.png "HackerRank")](https://www.hackerrank.com/jofisaes)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codeforces-20.png "Code Forces")](https://codeforces.com/profile/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codebyte-20.png "Codebyte")](https://coderbyte.com/profile/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codewars-20.png "CodeWars")](https://www.codewars.com/users/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/codepen-20.png "Code Pen")](https://codepen.io/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/hacker-news-20.png "Hacker News")](https://news.ycombinator.com/user?id=jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/infoq-20.png "InfoQ")](https://www.infoq.com/profile/Joao-Esperancinha.2/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/linkedin-20.png "LinkedIn")](https://www.linkedin.com/in/joaoesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/xing-20.png "Xing")](https://www.xing.com/profile/Joao_Esperancinha/cv)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/tumblr-20.png "Tumblr")](https://jofisaes.tumblr.com/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/pinterest-20.png "Pinterest")](https://nl.pinterest.com/jesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/quora-20.png "Quora")](https://nl.quora.com/profile/Jo%C3%A3o-Esperancinha)

## Achievements

[![VMware Spring Professional 2021](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/vmware-spring-professional-2021.png "VMware Spring Professional 2021")](https://www.credly.com/badges/762fa7a4-9cf4-417d-bd29-7e072d74cdb7)
[![Oracle Certified Professional, JEE 7 Developer](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/oracle-certified-professional-java-ee-7-application-developer-100.png "Oracle Certified Professional, JEE7 Developer")](https://www.credly.com/badges/27a14e06-f591-4105-91ca-8c3215ef39a2)
[![Oracle Certified Professional, Java SE 11 Programmer](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/oracle-certified-professional-java-se-11-developer-100.png "Oracle Certified Professional, Java SE 11 Programmer")](https://www.credly.com/badges/87609d8e-27c5-45c9-9e42-60a5e9283280)
[![IBM Cybersecurity Analyst Professional](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/ibm-cybersecurity-analyst-professional-certificate-100.png "IBM Cybersecurity Analyst Professional")](https://www.credly.com/badges/ad1f4abe-3dfa-4a8c-b3c7-bae4669ad8ce)
[![Certified Advanced JavaScript Developer](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/cancanit-badge-1462-100.png "Certified Advanced JavaScript Developer")](https://cancanit.com/certified/1462/)
[![Certified Neo4j Professional](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/professional_neo4j_developer-100.png "Certified Neo4j Professional")](https://graphacademy.neo4j.com/certificates/c279afd7c3988bd727f8b3acb44b87f7504f940aac952495ff827dbfcac024fb.pdf)
[![Deep Learning](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/badges/deep-learning-100.png "Deep Learning")](https://www.credly.com/badges/8d27e38c-869d-4815-8df3-13762c642d64)
