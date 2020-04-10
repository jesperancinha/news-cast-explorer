# Twitter Explorer
[![Bitbucket Pipelines branch](https://img.shields.io/bitbucket/pipelines/jesperancinha/twitter-explorer/master)](https://bitbucket.org/jesperancinha/twitter-explorer/addon/pipelines/home#!/)
[![CircleCI](https://circleci.com/bb/jesperancinha/twitter-explorer.svg?style=svg)](https://circleci.com/bb/jesperancinha/twitter-explorer)

This is an exercise made with a combination of different languages.

There are three modules:

- twitter-explorer-fe - This is the front end of the application in Angular 8
- twitter-fetcher - This is the java Spring Boot command line runner developed in Java 12
- twitter-logger - These are python scripts to serve the logs.

Logs are placed in the workspace of the execution environment of the working jar

These logs are read using two scripts

- logserver.py - For local runs
- log-docker-server.py - To be used in the docker image builds

The front end runs with NPM

I have provided a few utilities:

- docker-files - Here live the files used to make the docker image
- docker-files/pushed-image - This is the docker file for the image I've created in Docker up.
- Dockerfile - This is another docker image which uses the image jesperancinha/twitter-docker:0.0.2, that I have created as a starting point
- build.sh - In this bashscript you can find all the commands used to build the project, package it and mount everything in the docker container
- docker-init.sh - Does almost the same thing, but only executes docker commands.

The image I creaged it's generic and it's available on [dockerhub](https://hub.docker.com/r/jesperancinha/twitter-docker).

[![dockeri.co](https://dockeri.co/image/jesperancinha/twitter-docker)](https://hub.docker.com/r/jesperancinha/twitter-docker)

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
/usr/local/bin/twitter-fetcher-1.0.0-SNAPSHOT.jar 
--org.jesperancinha.twitter.consumerKey=<consumerKey> 
--org.jesperancinha.twitter.consumerSecret=<consumerSecret> 
--org.jesperancinha.twitter.token=<token> 
--org.jesperancinha.twitter.tokenSecret=<tokenSecret>
--org.jesperancinha.twitter.searchTerm=<searchTerm>
--org.jesperancinha.twitter.capacity=<capacity>
--org.jesperancinha.twitter.timeToWaitSeconds=<timeToWaitSeconds>
--org.jesperancinha.twitter.searchTerm=<searchWord>
```

## Hints & Tricks

-   Remove Docker-machine

NOTE: This process will remove old docker-machine installations.
User [Docker-Desktop](https://www.docker.com/products/docker-desktop) instead.

```bash
brew uninstall docker-machine-driver-vmware
brew uninstall --force docker-machine
docker system prune -a
```


-   [SDKMAN!](https://sdkman.io/install)

-   Install java versions with [SDKMan](https://sdkman.io/) for MAC-OS and Linux based systems

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 8.0.242.hs-adpt
sdk install java 11.0.6.hs-adpt
sdk install java 12.0.2.hs-adpt
sdk install java 13.0.2.hs-adpt
sdk install java 14.0.0.hs-adpt
```

-   Install java versions without [SDKMan](https://sdkman.io/) for [ubuntu prompt for windows](https://www.microsoft.com/en-us/p/ubuntu/9nblggh4msv6?activetab=pivot:overviewtab).

```bash
apt-get -y update
apt-get -y upgrade
apt -y install apt-transport-https ca-certificates wget dirmngr gnupg software-properties-common
wget -qO - https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public | apt-key add -
add-apt-repository --yes https://adoptopenjdk.jfrog.io/adoptopenjdk/deb/
apt -y update
sudo apt -y install openjdk-11-jdk
sudo apt install openjdk-13-jdk
sudo apt -y install adoptopenjdk-8-hotspot
sudo apt -y autoremove
```

- .bashrc file to get Gradle, GitPrompt, [SDKMAN](https://sdkman.io/) and some handy aliases in a Windows environment with [MinGW](http://www.mingw.org/).

```bash
if [ -f "/root/.bash-git-prompt/gitprompt.sh" ]; then
    GIT_PROMPT_ONLY_IN_REPO=1
    source /root/.bash-git-prompt/gitprompt.sh
fi

alias java8="sdk use java 8.0.242.hs-adpt"
alias java11="sdk use java  11.0.6.hs-adpt"
alias java12="sdk use java 12.0.2.hs-adpt"
alias java13="sdk use java 13.0.2.hs-adpt"
alias java14="sdk use java 14.0.0.hs-adpt"
alias m2disable="rm ~/.m2/settings.xml"
alias m2enable="cp /your_repo_folder/settings.xml ~/.m2/"

#THIS MUST BE AT THE END OF THE FILE FOR SDKMAN TO WORK!!!
export SDKMAN_DIR="/root/.sdkman"
[[ -s "/root/.sdkman/bin/sdkman-init.sh" ]] && source "/root/.sdkman/bin/sdkman-init.sh"
```

- .bashrc file to get Gradle, GitPrompt and some handy aliases in a Windows environment with [ubuntu prompt for windows](https://www.microsoft.com/en-us/p/ubuntu/9nblggh4msv6?activetab=pivot:overviewtab).

```bash
if [ -f "/root/.bash-git-prompt/gitprompt.sh" ]; then
    GIT_PROMPT_ONLY_IN_REPO=1
    source /root/.bash-git-prompt/gitprompt.sh
fi

alias java8="export JAVA_HOME=/usr/lib/jvm/adoptopenjdk-8-hotspot-amd64 && update-java-alternatives -s adoptopenjdk-8-hotspot-amd64"
alias java11="export JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64 && update-java-alternatives -s java-1.11.0-openjdk-amd64"
alias java12="echo \"Java 12 is not available. Setting up 13\" && export JAVA_HOME=/usr/lib/jvm/java-13-oracle && update-java-alternatives -s java-13-oracle"
alias java13="export JAVA_HOME=/usr/lib/jvm/java-13-oracle && update-java-alternatives -s java-13-oracle"
```

## About me 👨🏽‍💻🚀

[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/JEOrgLogo-20.png)](http://joaofilipesabinoesperancinha.nl)
[![Twitter Follow](https://img.shields.io/twitter/follow/joaofse?label=João%20Esperancinha&style=social)](https://twitter.com/joaofse)
[![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=jesperancinha&style=social)](https://github.com/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Articles&message=On%20Medium&color=purple)](https://medium.com/@jofisaes)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Articles&message=On%20The%20Web&color=purple)](https://github.com/jesperancinha/project-signer/blob/master/project-signer-templates/LossArticles.md)
[![Generic badge](https://img.shields.io/static/v1.svg?label=BitBucket&message=jesperancinha&color=navy)](https://bitbucket.org/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitLab&message=jesperancinha&color=navy)](https://gitlab.com/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=joaofilipesabinoesperancinha.nl&color=6495ED)](http://joaofilipesabinoesperancinha.nl)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Time%20Disruption%20Studios&color=6495ED)](http://tds.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Image%20Train%20Filters&color=6495ED)](http://itf.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=MancalaJE&color=6495ED)](http://mancalaje.joaofilipesabinoesperancinha.nl/)
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
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/medium-20.png)](https://medium.com/@jofisaes)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/stack-overflow-20.png)](https://stackoverflow.com/users/3702839/joao-esperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/reddit-20.png)](https://www.reddit.com/user/jesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/linkedin-20.png)](https://www.linkedin.com/in/joaoesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/xing-20.png)](https://www.xing.com/profile/Joao_Esperancinha/cv)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/instagram-20.png)](https://www.instagram.com/jesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/tumblr-20.png)](https://jofisaes.tumblr.com/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/pinterest-20.png)](https://nl.pinterest.com/jesperancinha/)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/quora-20.png)](https://nl.quora.com/profile/Jo%C3%A3o-Esperancinha)
