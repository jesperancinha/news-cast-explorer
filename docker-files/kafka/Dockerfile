FROM confluentinc/cp-kafka:latest

WORKDIR /usr/local/bin

ENV LANG=C.UTF-8

# Copy the modern KRaft start script
COPY ./startKRaftKafka.sh /usr/local/bin/

# Copy all server properties files (one per node)
COPY *.properties /opt/kafka/config/

# Expose the required ports for all three nodes (PLAINTEXT and CONTROLLER)
EXPOSE 9092 9093

# Use the robust KRaft entrypoint
ENTRYPOINT ["/usr/local/bin/startKRaftKafka.sh"]
