package org.jesperancinha.newscast.repository;

import org.jesperancinha.newscast.model.db.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
