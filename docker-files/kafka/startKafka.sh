#!/usr/bin/env bash
/usr/bin/zookeeper-server-start /etc/kafka/zookeeper.properties &
sleep 4
/usr/bin/kafka-server-start /opt/kafka/config/server0.properties &
/usr/bin/kafka-server-start /opt/kafka/config/server1.properties &
sleep 4
/usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic org.jesperancinha.newscast.orchestration.saga.CreateCommentSaga-reply
/usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic pageChannel
/usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic authorChannel
/usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 2 --partitions 2 --topic messageChannel
