package org.jesperancinha.newscast.repository;

import org.jesperancinha.newscast.model.explorer.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findFirstByNewscastMessageIdAndAuthorId(String messagrId, Long authorId);
}
