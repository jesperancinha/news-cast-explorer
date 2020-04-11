#!/usr/bin/env bash

echo -e '\e[34m'--- Welcome to Twitter-Explorer ---'\e[0m'
echo -e '\e[96m'I am your TE Wizard 🧙'\e[0m'
echo -e '\e[96m'I will generate your docker startup file with everything you need to run this application demo.'\e[0m'
echo -e '\e[96m'In order to continue, I need to know a few things'\e[0m'
echo -e '\e[91m'*** WARNING. Make sure you provide the right answers. Your answers will potentially link to your account ***'\e[0m'
echo -e '\e[91m'*** Remember to check the generated script before you run it ***'\e[0m'
echo -e '\e[96m'From your twitter accout'\e[0m'
echo -e '\e[33m'What is your application key?'\e[0m'
echo -e -n '\e[95m'">>>"'\e[0m'
read applicationkey
echo -e '\e[33m'What is your application secret?'\e[0m'
echo -e -n '\e[95m'">>>"'\e[0m'
read applicationSecret
echo -e '\e[33m'What is your token?'\e[0m'
echo -e -n '\e[95m'">>>"'\e[0m'
read tokenKey
echo -e '\e[33m'What is your token secret?'\e[0m'
echo -e -n '\e[95m'">>>"'\e[0m'
read tokenSecret
echo -e '\e[33m'What is your search word'\e[0m'
echo -e -n '\e[95m'">>>"'\e[0m'
read searchWord
echo -e '\e[33m'Generating script...'\e[0m'

BUILD_FILE="docker-entrypoint.sh"

echo "#!/usr/bin/env bash" > ${BUILD_FILE}
echo "nginx" >> ${BUILD_FILE}
echo "cd /usr/local/bin" >> ${BUILD_FILE}
echo "cat /etc/nginx/nginx.conf" >> ${BUILD_FILE}
echo "python3 log-docker-server.py &" >> ${BUILD_FILE}
echo "java -jar -Dspring.profiles.active=scheduler \\" >> ${BUILD_FILE}
echo "/usr/local/bin/twitter-fetcher-1.0.0-SNAPSHOT.jar \\" >> ${BUILD_FILE}
echo "--org.jesperancinha.twitter.searchTerm \\" >> ${BUILD_FILE}
echo "--org.jesperancinha.twitter.consumerKey=${applicationkey} \\" >> ${BUILD_FILE}
echo "--org.jesperancinha.twitter.consumerSecret=${applicationSecret} \\" >> ${BUILD_FILE}
echo "--org.jesperancinha.twitter.token=${tokenKey} \\" >> ${BUILD_FILE}
echo "--org.jesperancinha.twitter.tokenSecret=${tokenSecret} \\" >> ${BUILD_FILE}
echo "--org.jesperancinha.twitter.searchTerm=${searchWord}" >> ${BUILD_FILE}
echo "tail -f /dev/null" >> ${BUILD_FILE}

chmod +x docker-entrypoint.sh

echo -e '\e[33m'Done!'\e[0m'
echo -e '\e[96m'You can now start your '\e[34m'build.sh'\e[96m' script using file '\e[34m'docker-entrypoint.sh'\e[0m'

