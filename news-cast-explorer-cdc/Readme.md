# Newscast Explorer CDC

This module simulated the [CDC service](https://eventuate.io/docs/manual/eventuate-tram/latest/cdc-configuration.html) provided by the eventuate team.

What this service basically does is fetch the message provided in table message and then sets them in the kafka streams.

```sql
CREATE TABLE eventuate.message
(
    ID            VARCHAR(1000) PRIMARY KEY,
    DESTINATION   VARCHAR(1000) NOT NULL,
    HEADERS       VARCHAR(1000) NOT NULL,
    PAYLOAD       VARCHAR(1000) NOT NULL,
    CREATION_TIME BIGINT,
    PUBLISHED     BIGINT
);
```

The purpose of it is simply to show how the Saga architecture works. It is not the goal of this project to explore de CDC servce in details. This is why a simple mock version is enough to explain the mechanism. 
I do recommend, however to usew the [CDC service](https://eventuate.io/docs/manual/eventuate-tram/latest/cdc-configuration.html), given that it is though to work enterprise wise and not in a standalone simmple example.

In the [eventuate-tram](https://github.com/eventuate-tram) repo you can find more [documentation](https://github.com/eventuate-tram/eventuate-tram-sagas/blob/master/README.adoc).
The whole cdc service can be found in the [eventuate-cdc source code](https://github.com/eventuate-foundation/eventuate-cdc)

## References

- [eventuate-tram-embedded-schema.sql](https://github.com/eventuate-tram/eventuate-tram-core/blob/master/eventuate-tram-in-memory/src/main/resources/eventuate-tram-embedded-schema.sql)
- [eventuate-tram](https://github.com/eventuate-tram)
- [CDC service](https://eventuate.io/docs/manual/eventuate-tram/latest/cdc-configuration.html)
