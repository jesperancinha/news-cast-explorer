FROM maven:3.8-openjdk-17-slim

ENV runningFolder /usr/local/bin/

WORKDIR ${runningFolder}

COPY entrypoint.sh ${runningFolder}

ENTRYPOINT ["entrypoint.sh"]
