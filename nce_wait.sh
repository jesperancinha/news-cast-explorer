#!/bin/bash
GITHUB_RUN_ID=${GITHUB_RUN_ID:-123}

function checkServiceByNameAndMessage() {
    name=$1
    message=$2
    docker-compose -p "${GITHUB_RUN_ID}" logs "$name" > "logs"
    string=$(cat logs)
    counter=0
    echo "Project $GITHUB_RUN_ID"
    echo -n "Starting service $name "
    while [[ "$string" != *"$message"* ]]
    do
      echo -e -n "\e[93m-\e[39m"
      docker-compose -p "${GITHUB_RUN_ID}" logs "$name" > "logs"
      string=$(cat logs)
      sleep 1
      counter=$((counter+1))
      if [ $counter -eq 200 ]; then
          echo -e "\e[91mFailed after $counter tries! Cypress tests may fail!!\e[39m"
          echo "$string"
          exit 1
      fi
    done
    counter=$((counter+1))
    echo -e "\e[92m Succeeded starting $name Service after $counter tries!\e[39m"
}

checkServiceByNameAndMessage news-cast-postgres 'database system is ready to accept connections'
checkServiceByNameAndMessage news-cast-fe 'test is successful'
checkServiceByNameAndMessage news-cast-mock 'Tomcat started on'
checkServiceByNameAndMessage news-cast-mock 'Start completed'
checkServiceByNameAndMessage news-cast-orchestration 'Tomcat started on'
checkServiceByNameAndMessage news-cast-orchestration 'Start completed'
checkServiceByNameAndMessage news-cast-fetcher 'Tomcat started on'
checkServiceByNameAndMessage news-cast-fetcher 'Start completed'
checkServiceByNameAndMessage news-cast-cdc 'Tomcat started on'
checkServiceByNameAndMessage news-cast-cdc 'Start completed'
checkServiceByNameAndMessage news-cast-choreography 'Tomcat started on'
checkServiceByNameAndMessage news-cast-choreography 'Start completed'
