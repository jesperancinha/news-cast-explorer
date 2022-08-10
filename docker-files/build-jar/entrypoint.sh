#!/bin/bash
echo "$MAVEN_HOME"
echo "$MAVEN_CONFIG"
cd /opt/news-cast-explorer && mvn clean install -DskipTests
