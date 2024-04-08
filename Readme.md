# Newscast Explorer

---

[![Twitter URL](https://img.shields.io/twitter/url?logoColor=blue&style=social&url=https%3A%2F%2Fimg.shields.io%2Ftwitter%2Furl%3Fstyle%3Dsocial)](https://twitter.com/intent/tweet?text=Checkout%20this%20@github%20repo%20by%20@joaofse%20%F0%9F%91%A8%F0%9F%8F%BD%E2%80%8D%F0%9F%92%BB:%20https://github.com/jesperancinha/news-cast-explorer/src/master/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=Newscast%20Explorer&color=informational)](https://github.com/jesperancinha/news-cast-explorer)

[![news-cast-app](https://github.com/jesperancinha/news-cast-explorer/actions/workflows/news-cast-app.yml/badge.svg)](https://github.com/jesperancinha/news-cast-explorer/actions/workflows/news-cast-app.yml)
[![e2e-news-cast-app](https://github.com/jesperancinha/news-cast-explorer/actions/workflows/news-cast-app-e2e.yml/badge.svg)](https://github.com/jesperancinha/news-cast-explorer/actions/workflows/news-cast-app-e2e.yml)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/36b6433e36f04595924a3bdd4a43f9dd)](https://www.codacy.com/gh/jesperancinha/news-cast-explorer/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jesperancinha/news-cast-explorer&amp;utm_campaign=Badge_Grade)

[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/36b6433e36f04595924a3bdd4a43f9dd)](https://www.codacy.com/gh/jesperancinha/news-cast-explorer/dashboard?utm_source=github.com&utm_medium=referral&utm_content=jesperancinha/news-cast-explorer&utm_campaign=Badge_Coverage)
[![codecov](https://codecov.io/gh/jesperancinha/news-cast-explorer/branch/main/graph/badge.svg?token=vh9xgm8i8l)](https://codecov.io/gh/jesperancinha/news-cast-explorer)
[![Coverage Status](https://coveralls.io/repos/github/jesperancinha/news-cast-explorer/badge.svg?branch=main)](https://coveralls.io/github/jesperancinha/news-cast-explorer?branch=main)

[![GitHub language count](https://img.shields.io/github/languages/count/jesperancinha/news-cast-explorer.svg)](#)
[![GitHub top language](https://img.shields.io/github/languages/top/jesperancinha/news-cast-explorer.svg)](#)
[![GitHub top language](https://img.shields.io/github/languages/code-size/jesperancinha/news-cast-explorer.svg)](#)

---

## Technologies used

Please check the [TechStack.md](TechStack.md) file for details.

## Introduction

In this project are going to explore a known EIP known as [Saga](https://microservices.io/patterns/data/saga.html).

A saga is in its essence, a way to describe a sequence of processes that should occur sequentially in a transactional way.

There are two major [Saga](https://microservices.io/patterns/data/saga.html) types: A choreography and an orchestration Saga types.

A choreography Saga is based on event sourcing. We have multiple decoupled processes that wait for certain events in order to be triggered.

An orchestration Saga is also based on event sourcing, but instead of having the events scattered, it provides a series of rules on how processes should react to certain sub-process results.
An orchestration Saga has provisions for rollback processes for example. As soon as an error occurred, all provisioned methods to perform a rollback will be triggered from the point of failure. This follows the direct opposite direction of the successful route.
In this saga, we lay out a chain of processes and for each process we can optionally define a rollback process or any other process we want to trigger should a fail occur.

Sagas work in a different way than chained processes in the sense that they are thought out to be design to predict failures and act upon them. Whether we choose Choreography or Orchestration, we always get an out-of-the-box plan to go forward and go back.

<details>
<summary><b>Data flow steps</b></summary>

---
In this project we have two steps:

#### Fetching data
We run a process to fetch data from a mock feeder. This is not directly related to Sagas but it is part of the example of the use case.
The data is sent in a message node containing an author node. The fetch process will run by default configured take maximum 30 seconds per run and a maximum of 100 messages under that run.

#### Sending comments
We will send comments via POST requests. If the id's of the page, author and message match, then the sagas will run normally.
If the any of the id's fail, the process will perform a reactive chain of processes which will mark these comments as not available in the database. It will perform this in this order in reverse.
So this means that if the id of a page does not match, there will be a comment of that page in the database as a dangling reference. It will be marked as not available. The comment for the author and the message will be ignored because the sagas will not allow the chain to continue.
If the author id does not match, both page and author comments will be marked as not available. There will be no record of message.

---
</details>
<details>
<summary><b>Stable releases</b></summary>

---

-   [1.0.0](https://github.com/jesperancinha/news-cast-explorer/tree/1.0.0) - [e986fadced9dd48a0ce2711764097f18927ff0ba](https://github.com/jesperancinha/news-cast-explorer/tree/1.0.0)

---
</details>


## Project Layout

-   [news-cast-explorer-common](./news-cast-explorer-common) - Java libraries to support Page, Authors and Messages
-   [news-cast-explorer-saga-common](./news-cast-explorer-saga-common) - Kotlin library to support the comments for the previously mentioned entitites
-   [news-cast-mock](./news-cast-mock) - News feeder simulation. This is where the data starts - Spring - Kotlin module - Port 8081
> [http://localhost:8081/api/newscast/messages](http://localhost:8081/api/newscast/messages)
-   [news-cast-explorer-fetcher](./news-cast-explorer-fetcher) - Makes different runs, scheduled or non schedule. Each run can fetch up to 100 messages in a maximum period of 30 seconds by default - Spring Java 17 module - Port 8080
> [http://localhost:8080/api/newscast/fetcher/pages](http://localhost:8080/api/newscast/fetcher/pages)
-   [news-cast-explorer-cdc](./news-cast-explorer-cdc) - A CDC mock service to support message exchange in the Kafka streams - Port 8085
-   [news-cast-explorer-saga-choreography](./news-cast-explorer-saga-choreography) - A choreography implementation of the Saga Architecture - Spring - Kotlin module - Port 8083

## How to run

<details>
<summary><b>Request examples with cUrl</b></summary>

---
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

---
</details>

<details>
<summary><b>GUI Endpoints</b></summary>

-   [news-cast-explorer-fe](./news-cast-explorer-fe) - A front end tool providing and interface to visualize the results of the news feed fetcher
> [http://localhost:9000/](http://localhost:9000/)

</details>

<details>
<summary><b>Swagger UI Endpoints</b></summary>

---
-   Local run

1.  [news-cast-explorer-fetcher](http://localhost:8080/api/newscast/fetcher/swagger-ui/index.html)
2.  [news-cast-explorer-saga-choreography](http://localhost:8083/api/saga/swagger-ui/index.html)
3.  [news-cast-explorer-saga-orchestration](http://localhost:8082/api/saga/swagger-ui/index.html)
4.  [news-cast-mock](http://localhost:8081/swagger-ui/index.html)

-   Local Docker run

1.  [news-cast-explorer-fetcher](http://localhost:9000/api/newscast/fetcher/swagger-ui/index.html)
2.  [news-cast-explorer-saga-choreography](http://localhost:9000/api/saga/choreography/swagger-ui/index.html)
3.  [news-cast-explorer-saga-orchestration](http://localhost:9000/api/saga/orchestration/swagger-ui/index.html)
4.  [news-cast-mock](http://localhost:9000/api/mock/swagger-ui/index.html)

---
</details>

<details>
<summary><b>How to run</b></summary>

---
```shell
make dcup-full-action
```

---
</details>

<details>
<summary><b>How to run Cypress</b></summary>

---
#### Against Nginx

```shell
make cypress-open-docker
```

#### Directly against the service ports

```shell
make cypress-open
```
---
</details>

<details>
<summary><b>Java version</b></summary>

---
```bash
sdk install java 17-open
sdk use java 17-open
```
---
</details>

<details>
<summary><b>Local hosts configuration</b></summary>

---
Running the containers may require you to add this line to your `/etc/hosts` file:

```text
127.0.0.1       news-cast-kafka
```

This is mostly because of the way kafka operates in giving responses back

---
</details>

## Roadmap

<details>
<summary><b>Roadmap</b></summary>

---
For roadmap information and current developments please check document [Roadmap.md](./Roadmap.md)

---
</details>

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

## About me

[![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=Jesperancinha&style=for-the-badge&logo=github&color=grey "GitHub")](https://github.com/jesperancinha)
