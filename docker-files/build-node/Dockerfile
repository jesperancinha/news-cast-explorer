FROM node:18

RUN npm install -g npm

RUN npm install typescript -g

RUN npm install -g @angular/cli

ENV runningFolder /usr/local/bin/

WORKDIR ${runningFolder}

COPY entrypoint.sh ${runningFolder}

ENTRYPOINT ["entrypoint.sh"]