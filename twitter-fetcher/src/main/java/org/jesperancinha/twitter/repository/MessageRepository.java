package org.jesperancinha.twitter.repository;

import org.jesperancinha.twitter.model.db.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
