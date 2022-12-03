#!/bin/bash

function checkServiceByNameAndMessage() {
    name=$1
    message=$2
    docker-compose logs "$name" > "logs"
    string=$(cat logs)
    counter=0
    while [[ "$string" != *"$message"* ]]
    do
      printf "."
      docker-compose logs "$name" > "logs"
      string=$(cat logs)
      sleep 1
      counter=$((counter+1))
      if [ $counter -eq 200 ]; then
          echo "Failed after $counter tries! Cypress tests may fail!!"
          echo "$string"
          exit 1
      fi
    done
    counter=$((counter+1))
    echo "Succeeded $name Service after $counter tries!"
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
