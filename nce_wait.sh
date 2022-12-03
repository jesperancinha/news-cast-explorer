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

checkServiceByNameAndMessage news_cast_postgres 'database system is ready to accept connections'
checkServiceByNameAndMessage news_cast_fe 'test is successful'
checkServiceByNameAndMessage news_cast_mock 'Tomcat started on'
checkServiceByNameAndMessage news_cast_mock 'Start completed'
checkServiceByNameAndMessage news_cast_orchestration 'Tomcat started on'
checkServiceByNameAndMessage news_cast_orchestration 'Start completed'
checkServiceByNameAndMessage news_cast_fetcher 'Tomcat started on'
checkServiceByNameAndMessage news_cast_fetcher 'Start completed'
checkServiceByNameAndMessage news_cast_cdc 'Tomcat started on'
checkServiceByNameAndMessage news_cast_cdc 'Start completed'
checkServiceByNameAndMessage news_cast_choreography 'Tomcat started on'
checkServiceByNameAndMessage news_cast_choreography 'Start completed'
