package org.jesperancinha.twitter.repository;

import org.jesperancinha.twitter.model.db.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
