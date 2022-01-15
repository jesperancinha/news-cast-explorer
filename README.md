# Newscast Explorer

---

[![Twitter URL](https://img.shields.io/twitter/url?logoColor=blue&style=social&url=https%3A%2F%2Fimg.shields.io%2Ftwitter%2Furl%3Fstyle%3Dsocial)](https://twitter.com/intent/tweet?text=Checkout%20this%20@bitbucket%20repo%20by%20@joaofse%20%F0%9F%91%A8%F0%9F%8F%BD%E2%80%8D%F0%9F%92%BB:%20https://bitbucket.org/jesperancinha/news-cast-explorer/src/master/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=BitBucket&message=Newscast%20Explorer&color=informational)](https://bitbucket.org/jesperancinha/news-cast-explorer)

[![Bitbucket Pipelines branch](https://img.shields.io/bitbucket/pipelines/jesperancinha/news-cast-explorer/master)](https://bitbucket.org/jesperancinha/news-cast-explorer/addon/pipelines/home#!/)
[![CircleCI](https://circleci.com/bb/jesperancinha/news-cast-explorer.svg?style=svg)](https://circleci.com/bb/jesperancinha/news-cast-explorer)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/f49ddf491c1c4eff8addac23e08b9a91)](https://www.codacy.com/bb/jesperancinha/news-cast-explorer/dashboard?utm_source=jesperancinha@bitbucket.org&amp;utm_medium=referral&amp;utm_content=jesperancinha/news-cast-explorer&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/768c571b-f241-4530-aeed-2d0e91e03eea)](https://codebeat.co/projects/bitbucket-org-jesperancinha-news-cast-explorer-master)

[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/f49ddf491c1c4eff8addac23e08b9a91)](https://www.codacy.com/bb/jesperancinha/news-cast-explorer/dashboard?utm_source=jesperancinha@bitbucket.org&utm_medium=referral&utm_content=jesperancinha/news-cast-explorer&utm_campaign=Badge_Coverage)
[![codecov](https://codecov.io/bb/jesperancinha/news-cast-explorer/branch/master/graph/badge.svg?token=ZZQIS9S1LR)](https://codecov.io/bb/jesperancinha/news-cast-explorer)
[![Coverage Status](https://coveralls.io/repos/bitbucket/jesperancinha/news-cast-explorer/badge.svg?branch=master)](https://coveralls.io/bitbucket/jesperancinha/news-cast-explorer?branch=master)

---

## Technologies used

---

[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/kotlin-50.png "Kotlin")](https://kotlinlang.org/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/java-50.png "Java")](https://www.oracle.com/java/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/docker-50.png)](https://www.docker.com/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/docker-compose-50.png)](https://docs.docker.com/compose/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/postgres-50.png "PostgreSQL")](https://www.postgresql.org/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/spring-50.png)](https://spring.io/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/spring-boot-50.png)](https://spring.io/projects/spring-boot)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/angular-50.png "Angular")](https://angular.io/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/python-50.png)](https://www.python.org/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/bash-50.png)](https://www.gnu.org/software/bash/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/jupiter5-50.png "Jupiter 5")](https://junit.org/junit5/docs/current/user-guide/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/kotest-50.png "Kotest 4.6.1")](https://kotest.io/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/mockk-50.png "MockK")](https://mockk.io/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-50/testcontainers-50.png "Test containers")](https://www.testcontainers.org/)

---

## Introduction

In this project are going to explore a known EIP known as [Saga](https://microservices.io/patterns/data/saga.html).

A saga is in its essence, a way to describe a sequence of processes that should occur sequentially in a transactional way.

There are two major [Saga](https://microservices.io/patterns/data/saga.html) types: A choreography and an orchestration Saga types.

A choreography Saga is based on event sourcing. We have multiple decoupled processes that wait for certain events in order to be triggered.

An orchestration Saga is also based on event sourcing, but instead of having the events scattered, it provides a series of rules on how processes should react to certain sub-process results.
An orchestration Saga has provisions for rollback processes for example. As soon as an error occurred, all provisioned methods to perform a rollback will be triggered from the point of failure. This follows the direct opposite direction of the successful route.
In this saga, we lay out a chain of processes and for each process we can optionally define a rollback process or any other process we want to trigger should a fail occur.

Sagas work in a different way than chained processes in the sense that they are thought out to be design to predict failures and act upon them. Whether we choose Choreography or Orchestration, we always get an out-of-the-box plan to go forward and go back.

In this project we have two steps:

#### Fetching data
We run a process to fetch data from a mock feeder. This is not directly related to Sagas but it is part of the example of the use case.
The data is sent in a message node containing an author node. The fetch process will run by default configured take maximum 30 seconds per run and a maximum of 100 messages under that run.

#### Sending comments
We will send comments via POST requests. If the id's of the page, author and message match, then the sagas will run normally.
If the any of the id's fail, the process will perform a reactive chain of processes which will mark these comments as not available in the database. It will perform this in this order in reverse.
So this means that if the id of a page does not match, there will be a comment of that page in the database as a dangling reference. It will be marked as not available. The comment for the author and the message will be ignored because the sagas will not allow the chain to continue.
If the author id does not match, both page and author comments will be marked as not available. There will be no record of message.

This repo is the official support article to my article on medium:

[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/medium-20.png "Medium")](https://medium.com/@jofisaes/newscast-using-sagas-in-choreography-and-orchestration-patterns-a-java-17-and-kotlin-example-e3d0ec17b910) [News Cast — Using Sagas in Choreography and Orchestration Patterns](https://medium.com/@jofisaes/newscast-using-sagas-in-choreography-and-orchestration-patterns-a-java-17-and-kotlin-example-e3d0ec17b910)


## Libraries

-   [news-cast-explorer-common](./news-cast-explorer-common) - Java libraries to support Page, Authors and Messages
-   [news-cast-explorer-saga-common](./news-cast-explorer-saga-common) - Kotlin library to support the comments for the previously mentioned entitites

## Running services

-   [news-cast-mock](./news-cast-mock) - News feeder simulation. This is where the data starts - Spring - Kotlin module - Port 8081
> [http://localhost:8081/api/newscast/messages](http://localhost:8081/api/newscast/messages)

-   [news-cast-explorer-fetcher](./news-cast-explorer-fetcher) - Makes different runs, scheduled or non schedule. Each run can fetch up to 100 messages in a maximum period of 30 seconds by default - Spring Java 17 module - Port 8080
> [http://localhost:8080/api/newscast/fetcher/pages](http://localhost:8080/api/newscast/fetcher/pages)

-   [news-cast-explorer-cdc](./news-cast-explorer-cdc) - A CDC mock service to support message exchange in the Kafka streams - Port 8085
-   [news-cast-explorer-saga-choreography](./news-cast-explorer-saga-choreography) - A choreography implementation of the Saga Architecture - Spring - Kotlin module - Port 8083

>Example request:
>
>```shell
>curl -X POST http://localhost:8083/api/saga/choreography -H 'Content-Type: application/json' --data '{ "idPage": 1, "pageComment": "I love this", "idAuthor": 2, "authorComment": "This is my favourite author", "idMessage": 3, "messageComment": "I agree", "authorRequestId":123,"pageRequestId":456,"messageRequestId":789 }'
>```

-   [news-cast-explorer-saga-orchestration](./news-cast-explorer-saga-orchestration) - An orchestration implementation of the Saga Architecture - Spring - Kotlin module - Port 8082

>Example request:
>
>```shell
>curl -X POST http://localhost:8082/api/saga/orchestration -H 'Content-Type: application/json' --data '{ "idPage": 1, "pageComment": "I love this", "idAuthor": 2, "authorComment": "This is my favourite author", "idMessage": 3, "messageComment": "I agree", "authorRequestId":123,"pageRequestId":456,"messageRequestId":789 }'
>```

## GUI

-   [news-cast-explorer-fe](./news-cast-explorer-fe) - A front end tool providing and interface to visualize the results of the news feed fetcher
> [http://localhost:9000/](http://localhost:9000/)

## Installation Notes

### Java version

```bash
sdk install java 17-open
sdk use java 17-open
```

## Local hosts configuration

Running the containers may require you to add this line to your `/etc/hosts` file:

```text
127.0.0.1       news_cast_kafka
```

This is mostly because of the way kafka operates in giving responses back

## Roadmap

For roadmap information and current developments please check document [Roadmap.md](./Roadmap.md)

## Buy me a coffee

I hope you enjoyed this repository. If you did please buy me a coffee which enables me to constantly improve and make new free content regularly for everyone. Thank you so much!

[![Buy me a coffee](https://img.buymeacoffee.com/button-api/?text=Buy%20me%20a%20coffee&emoji=&slug=jesperancinha&button_colour=046c46&font_colour=ffffff&font_family=Cookie&outline_colour=ffffff&coffee_colour=FFDD00 "title")](https://www.buymeacoffee.com/jesperancinha)

## References

-   [Eventuate.IO](https://eventuate.io/)
-   [Saga: How to implement complex business transactions without two phase commit](https://blog.bernd-ruecker.com/saga-how-to-implement-complex-business-transactions-without-two-phase-commit-e00aa41a1b1b)
-   [Managing data consistency in a microservice architecture using Sagas - Implementing an orchestration-based saga](https://chrisrichardson.net/post/sagas/2019/12/12/developing-sagas-part-4.html)
-   [Managing data consistency in a microservice architecture using Sagas - Implementing a choreography-based saga](https://chrisrichardson.net/post/sagas/2019/08/15/developing-sagas-part-3.html)
-   [Choreography pattern with Springboot](https://www.google.com/amp/s/www.vinsguru.com/choreography-saga-pattern-with-spring-boot/amp/)
-   [Spock Framework Reference Documentation](http://spockframework.org/spock/docs/1.1-rc-3/all_in_one.html#_helper_methods)
-   [Interaction Based Testing with Spock](http://spockframework.org/spock/docs/1.0/interaction_based_testing.html)
-   [Bash tips: Colors and formatting (ANSI/VT100 Control sequences)](https://misc.flogisoft.com/bash/tip_colors_and_formatting)
-   [JUnit 5 Parameter Resolution Example](https://howtoprogram.xyz/2016/10/28/junit-5-parameter-resolution-example/)

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
