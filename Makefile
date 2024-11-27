SHELL=/bin/bash
GITHUB_RUN_ID ?=123

b: build
build: build-node build-maven
build-node:
	cd news-cast-explorer-fe && yarn && npm run build
build-test-node: build-node test-node
build-node-cypress:
	cd e2e && yarn
run-node:
	cd news-cast-explorer-fe; \
	npm start
build-node-docker:
	cd news-cast-explorer-fe && [ -d node_modules ] || mkdir node_modules
	cd news-cast-explorer-fe && [ -d dist ] || mkdir dist
	cd news-cast-explorer-fe && chmod 777 node_modules
	cd news-cast-explorer-fe && chmod 777 dist
	touch news-cast-explorer-fe/yarn.lock
	chmod 777 news-cast-explorer-fe
	chmod 777 news-cast-explorer-fe/yarn.lock
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml build gui-builder
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from gui-builder gui-builder
	cd news-cast-explorer-fe && [ -d node_modules ] && echo "dist has been created!!" || echo "dist has not been created!!"
build-node-cypress-docker:
	cd e2e && [ -d node_modules ] || mkdir node_modules
	cd e2e && chmod 777 node_modules
	touch e2e/yarn.lock
	chmod 777 e2e
	chmod 777 e2e/yarn.lock
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml build cypress-builder
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from cypress-builder cypress-builder
build-maven-docker:
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml build backend-builder
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from backend-builder backend-builder
qa-maven-docker:
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml build backend-qa
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from backend-qa backend-qa
report-maven-docker:
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml build backend-report
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml up --exit-code-from backend-report backend-report
build-maven:
	mvn clean install -DskipTests
build-test:
	mvn clean install
test: test-node test-maven
test-node:
	cd news-cast-explorer-fe && npm run jest
test-maven:
	mvn test
local: no-test
	mkdir -p bin
no-test:
	mvn clean install -DskipTests
docker-clean:
	docker-compose -p ${GITHUB_RUN_ID} rm -svf
docker-clean-local:
	docker-compose rm -svf
docker-stop-all:
	docker ps -a --format '{{.ID}}' | xargs -I {} docker stop {}
dist:
	cp -r news-cast-explorer-fe/dist docker-files/nginx
docker: dist
	docker-compose -p ${GITHUB_RUN_ID} up -d --build --remove-orphans
docker-local: dist
	docker-compose up -d --build --remove-orphans
docker-logs:
	cd e2e; \
	make docker-logs
docker-clean-build-start: docker-clean b docker
docker-clean-start: docker-clean docker
stop:
	docker-compose -p ${GITHUB_RUN_ID} down --remove-orphans
prune-all: stop
	docker ps -a --format '{{.ID}}' -q | xargs docker stop
	docker ps -a --format '{{.ID}}' -q | xargs docker rm
	docker system prune --all
	docker builder prune
	docker system prune --all --volumes
case:
	cd news-cast-demo && mkdir -p dst && yarn && node app.js
update-snyk:
	npm i -g snyk
update-node:
	npm install -g npm-check-updates
	cd news-cast-demo && ncu -u && yarn
	cd news-cast-explorer-fe && npx browserslist && ncu -u && yarn
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
	docker-compose -p ${GITHUB_RUN_ID} -f docker-compose.yml -f docker-compose.builder.yml up -d news-cast-postgres news-cast-kafka news-cast-mock news-cast-cdc news-cast-fetcher news-cast-choreography news-cast-orchestration news-cast-fe
nce-wait:
	bash nce_wait.sh
dcd: dc-migration
	docker-compose -p ${GITHUB_RUN_ID} down --remove-orphans
dcd-local:
	docker-compose down --remove-orphans
dcp:
	docker-compose -p ${GITHUB_RUN_ID} stop
dcup: dcd docker-clean docker nce-wait
dcup-full-action: dcd docker-clean no-test build-node docker nce-wait
dcup-full-local: dcd docker-clean-local no-test build-node docker nce-wait
dcup-action: dcp docker-action nce-wait
dcup-light: dcd
	docker-compose -p ${GITHUB_RUN_ID} up -d news-cast-postgres news-cast-kafka
build-kafka:
	docker-compose -p ${GITHUB_RUN_ID} stop news-cast-kafka
	docker-compose -p ${GITHUB_RUN_ID} rm news-cast-kafka
	docker-compose -p ${GITHUB_RUN_ID} build --no-cache news-cast-kafka
	docker-compose -p ${GITHUB_RUN_ID} up -d
build-nginx: build-node
	docker-compose -p ${GITHUB_RUN_ID} stop news-cast-fe
	docker-compose -p ${GITHUB_RUN_ID} rm news-cast-fe
	docker-compose -p ${GITHUB_RUN_ID} build --no-cache news-cast-fe
	docker-compose -p ${GITHUB_RUN_ID} up -d
end-logs:
	docker-compose -p ${GITHUB_RUN_ID} logs --tail 1000 news-cast-postgres
	docker-compose -p ${GITHUB_RUN_ID} logs --tail 1000 news-cast-mock
	docker-compose -p ${GITHUB_RUN_ID} logs --tail 1000 news-cast-fetcher
	docker-compose -p ${GITHUB_RUN_ID} logs --tail 1000 news-cast-cdc
	docker-compose -p ${GITHUB_RUN_ID} logs --tail 1000 news-cast-orchestration
	docker-compose -p ${GITHUB_RUN_ID} logs --tail 1000 news-cast-choreography
	docker-compose -p ${GITHUB_RUN_ID} logs --tail 1000 news-cast-fe
coverage-maven:
	mvn jacoco:prepare-agent package jacoco:report
coverage-node:
	cd news-cast-explorer-fe && npm run coverage
report:
	mvn omni-coveragereporter:report
local-pipeline: build-maven build-node test-maven test-node coverage-maven coverage-node report
update-browsers:
	npx update-browserslist-db@latest
update-all: update-snyk update-browsers
update: remove-lock-files
	git pull
	npm install caniuse-lite
	npm install -g npm-check-updates
	cd news-cast-explorer-fe; \
 		yarn; \
 		npx browserslist --update-db; \
 		ncu -u; \
 		yarn
remove-lock-files:
	find . -name "package-lock.json" | xargs -I {} rm {}; \
	find . -name "yarn.lock" | xargs -I {} rm {};
revert-deps-cypress-update:
	if [ -f  e2e/docker-composetmp.yml ]; then rm e2e/docker-composetmp.yml; fi
	if [ -f  e2e/packagetmp.json ]; then rm e2e/packagetmp.json; fi
	git checkout e2e/docker-compose.yml
	git checkout e2e/package.json
deps-cypress-update:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/cypressUpdateOne.sh | bash
deps-plugins-update:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/pluginUpdatesOne.sh | bash -s -- $(PARAMS)
deps-update: update
accept-prs:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/acceptPR.sh | bash
accept-repo-prs:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/accept-all-repo-prs.sh | bash
dc-migration:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/setupDockerCompose.sh | bash
