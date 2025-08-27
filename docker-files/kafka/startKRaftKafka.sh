#!/usr/bin/env bash
set -e

# Usage:
#   Set KAFKA_CONFIG to the path of this node's server properties file
#   Set KAFKA_LOGDIR to the log directory for this node
#   Set KAFKA_CLUSTER_ID to the SAME value for all nodes (generate with: kafka-storage.sh random-uuid)
#
# Example:
#   export KAFKA_CONFIG=/opt/kafka/config/server0.properties
#   export KAFKA_LOGDIR=/home/appuser/kafka-logs/02
#   export KAFKA_CLUSTER_ID=your-cluster-id
#   ./startKRaftKafka.sh

CONFIG=${KAFKA_CONFIG:-/opt/kafka/config/server.properties}
LOGDIR=${KAFKA_LOGDIR:-/home/appuser/kafka-logs}
KAFKA_CLUSTER_ID=${KAFKA_CLUSTER_ID:-replace-with-your-cluster-id}

META_FILE="$LOGDIR/meta.properties"

# Find kafka-storage.sh or kafka-storage in common locations
KAFKA_STORAGE_SH=""
if command -v kafka-storage.sh >/dev/null 2>&1; then
  KAFKA_STORAGE_SH="$(command -v kafka-storage.sh)"
elif command -v kafka-storage >/dev/null 2>&1; then
  KAFKA_STORAGE_SH="$(command -v kafka-storage)"
elif [ -x "/usr/bin/kafka-storage.sh" ]; then
  KAFKA_STORAGE_SH="/usr/bin/kafka-storage.sh"
elif [ -x "/usr/bin/kafka-storage" ]; then
  KAFKA_STORAGE_SH="/usr/bin/kafka-storage"
elif [ -x "/opt/kafka/bin/kafka-storage.sh" ]; then
  KAFKA_STORAGE_SH="/opt/kafka/bin/kafka-storage.sh"
elif [ -x "/opt/kafka/bin/kafka-storage" ]; then
  KAFKA_STORAGE_SH="/opt/kafka/bin/kafka-storage"
else
  echo "ERROR: kafka-storage(.sh) not found in PATH, /usr/bin, or /opt/kafka/bin."
  echo "Current PATH: $PATH"
  echo "Files in /usr/bin: $(ls /usr/bin | grep kafka-storage)"
  echo "Files in /opt/kafka/bin: $(ls /opt/kafka/bin 2>/dev/null | grep kafka-storage)"
  exit 1
fi

if [ ! -f "$META_FILE" ]; then
  if [ "$(ls -A $LOGDIR 2>/dev/null | grep -v meta.properties | wc -l)" -ne 0 ]; then
    echo "WARNING: $LOGDIR is not empty and meta.properties is missing."
    echo "         Please ensure this is intentional to avoid data loss."
  fi
  echo "Formatting storage for KRaft node (logdir: $LOGDIR, config: $CONFIG, cluster ID: $KAFKA_CLUSTER_ID) ..."
  "$KAFKA_STORAGE_SH" format -t $KAFKA_CLUSTER_ID -c $CONFIG
fi

# Find kafka-server-start.sh or kafka-server-start in common locations
KAFKA_SERVER_START=""
if command -v kafka-server-start.sh >/dev/null 2>&1; then
  KAFKA_SERVER_START="$(command -v kafka-server-start.sh)"
elif command -v kafka-server-start >/dev/null 2>&1; then
  KAFKA_SERVER_START="$(command -v kafka-server-start)"
elif [ -x "/usr/bin/kafka-server-start.sh" ]; then
  KAFKA_SERVER_START="/usr/bin/kafka-server-start.sh"
elif [ -x "/usr/bin/kafka-server-start" ]; then
  KAFKA_SERVER_START="/usr/bin/kafka-server-start"
elif [ -x "/opt/kafka/bin/kafka-server-start.sh" ]; then
  KAFKA_SERVER_START="/opt/kafka/bin/kafka-server-start.sh"
elif [ -x "/opt/kafka/bin/kafka-server-start" ]; then
  KAFKA_SERVER_START="/opt/kafka/bin/kafka-server-start"
else
  echo "ERROR: kafka-server-start(.sh) not found in PATH, /usr/bin, or /opt/kafka/bin."
  echo "Current PATH: $PATH"
  echo "Files in /usr/bin: $(ls /usr/bin | grep kafka-server-start)"
  echo "Files in /opt/kafka/bin: $(ls /opt/kafka/bin 2>/dev/null | grep kafka-server-start)"
  exit 1
fi

# Start the broker
exec "$KAFKA_SERVER_START" $CONFIG
