# Hints & Tricks

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

### Git tagging
```bash
git tag new-tag old-tag
git tag -d old-tag
git push origin :refs/tags/old-tag
git push --tags
git pull --prune --tags
```
