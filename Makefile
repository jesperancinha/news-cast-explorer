SHELL=/bin/bash

b: build
build: build-npm build-maven
build-npm:
	cd news-cast-explorer-fe && yarn install && npm run build
build-npm-docker:
	cd news-cast-explorer-fe && [ -d node_modules ] || mkdir node_modules
	cd news-cast-explorer-fe && [ -d dist ] || mkdir dist
	cd news-cast-explorer-fe && chmod 777 node_modules
	cd news-cast-explorer-fe && chmod 777 dist
	touch news-cast-explorer-fe/yarn.lock
	chmod 777 news-cast-explorer-fe
	chmod 777 news-cast-explorer-fe/yarn.lock
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml build gui-builder
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from gui-builder gui-builder
	cd news-cast-explorer-fe && [ -d node_modules ] && echo "dist has been created!!" || echo "dist has not been created!!"
build-npm-cypress-docker:
	cd e2e && [ -d node_modules ] || mkdir node_modules
	cd e2e && chmod 777 node_modules
	touch e2e/yarn.lock
	chmod 777 e2e
	chmod 777 e2e/yarn.lock
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml build cypress-builder
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from cypress-builder cypress-builder
build-maven-docker:
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml build backend-builder
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from backend-builder backend-builder
qa-maven-docker:
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml build backend-qa
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from backend-qa backend-qa
report-maven-docker:
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml build backend-report
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from backend-report backend-report
build-maven:
	mvn clean install -DskipTests
build-test:
	mvn clean install
test: test-node test-maven
test-node:
	cd news-cast-explorer-fe ;\
	yarn install ;\
	npm run test ;\
	cd ..
test-maven:
	mvn test
local: no-test
	mkdir -p bin
no-test:
	mvn clean install -DskipTests
docker-clean:
	docker-compose rm -svf
dist:
	cp -r news-cast-explorer-fe/dist docker-files/nginx
docker: dist
	docker-compose up -d --build --remove-orphans
docker-clean-build-start: docker-clean b docker
docker-clean-start: docker-clean docker
stop:
	docker-compose down --remove-orphans
prune-all: stop
	docker ps -a --format '{{.ID}}' -q | xargs docker stop
	docker ps -a --format '{{.ID}}' -q | xargs docker rm
	docker system prune --all
	docker builder prune
	docker system prune --all --volumes
case:
	cd news-cast-demo && mkdir -p dst && yarn install && node app.js
update-snyk: update
	npm i -g snyk
update:
	npm install -g npm-check-updates
	cd news-cast-demo && ncu -u && yarn
audit:
	cd news-cast-demo && npm audit fix && yarn
cypress-install:
	cd e2e && make build
cypress-open:
	cd e2e && make cypress-open
cypress-open-docker:
	cd e2e && make cypress-open-docker
cypress-electron:
	cd e2e && make cypress-electron
cypress-chrome:
	cd e2e && make cypress-chrome
cypress-firefox:
	cd e2e && make cypress-firefox
cypress-firefox-full:
	cd e2e && make cypress-firefox-full
cypress-edge:
	cd e2e && make cypress-edge
docker-action: dist
	docker-compose -f docker-compose.yml -f docker-compose.builder.yml up -d news_cast_postgres news_cast_kafka news_cast_mock news_cast_cdc news_cast_fetcher news_cast_choreography news_cast_orchestration news_cast_fe
nce-wait:
	bash nce_wait.sh
dcd:
	docker-compose down --remove-orphans
dcp:
	docker-compose stop
dcup: dcd docker-clean docker nce-wait
dcup-full-action: dcd docker-clean no-test build-npm docker nce-wait
dcup-action: dcp docker-action nce-wait
dcup-light: dcd
	docker-compose up -d news_cast_postgres
update:
	npm install -g npm-check-updates
	cd news-cast-explorer-fe && npx browserslist && ncu -u && yarn
build-kafka:
	docker-compose stop news_cast_kafka
	docker-compose rm news_cast_kafka
	docker-compose build --no-cache news_cast_kafka
	docker-compose up -d
end-logs:
	docker-compose logs --tail 1000 news_cast_postgres
	docker-compose logs --tail 1000 news_cast_mock
	docker-compose logs --tail 1000 news_cast_fetcher
	docker-compose logs --tail 1000 news_cast_cdc
	docker-compose logs --tail 1000 news_cast_orchestration
	docker-compose logs --tail 1000 news_cast_choreography
	docker-compose logs --tail 1000 news_cast_fe
report-coverage:
	mvn omni-coveragereporter:report
