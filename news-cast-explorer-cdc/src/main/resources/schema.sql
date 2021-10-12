-- from:
-- https://github.com/eventuate-tram/eventuate-tram-sagas/blob/master/postgres/tram-saga-schema.sql

CREATE SCHEMA IF NOT EXISTS eventuate;

DROP Table IF Exists eventuate.saga_instance_participants;
DROP Table IF Exists eventuate.saga_instance;
DROP Table IF Exists eventuate.saga_lock_table;
DROP Table IF Exists eventuate.saga_stash_table;
drop table if exists eventuate.message;
drop table if exists eventuate.received_messages;
drop table if exists eventuate.cdc_monitoring ;

CREATE TABLE eventuate.saga_instance_participants
(
    saga_type   VARCHAR(255) NOT NULL,
    saga_id     VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    resource    VARCHAR(100) NOT NULL,
    PRIMARY KEY (saga_type, saga_id, destination, resource)
);

CREATE TABLE eventuate.saga_instance
(
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    state_name      VARCHAR(100)  NOT NULL,
    last_request_id VARCHAR(100),
    end_state       BOOLEAN,
    compensating    BOOLEAN,
    saga_data_type  VARCHAR(1000) NOT NULL,
    saga_data_json  VARCHAR(1000) NOT NULL,
    PRIMARY KEY (saga_type, saga_id)
);

create table eventuate.saga_lock_table
(
    target    VARCHAR(100) PRIMARY KEY,
    saga_type VARCHAR(255) NOT NULL,
    saga_Id   VARCHAR(100) NOT NULL
);

create table eventuate.saga_stash_table
(
    message_id      VARCHAR(100) PRIMARY KEY,
    target          VARCHAR(100)  NOT NULL,
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    message_headers VARCHAR(1000) NOT NULL,
    message_payload VARCHAR(1000) NOT NULL
);

-- from
-- https://github.com/eventuate-tram/eventuate-tram-core/blob/master/eventuate-tram-in-memory/src/main/resources/eventuate-tram-embedded-schema.sql


CREATE TABLE eventuate.message
(
    ID            VARCHAR(1000) PRIMARY KEY,
    DESTINATION   VARCHAR(1000) NOT NULL,
    HEADERS       VARCHAR(1000) NOT NULL,
    PAYLOAD       VARCHAR(1000) NOT NULL,
    CREATION_TIME BIGINT,
    PUBLISHED     BIGINT
);


CREATE TABLE eventuate.received_messages
(
    CONSUMER_ID   VARCHAR(1000),
    MESSAGE_ID    VARCHAR(1000),
    CREATION_TIME BIGINT,
    PRIMARY KEY (CONSUMER_ID, MESSAGE_ID)
);

create table eventuate.cdc_monitoring
(
    reader_id VARCHAR(1000) PRIMARY KEY,
    last_time BIGINT
);