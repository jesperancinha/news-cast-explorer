package org.jesperancinha.twitter.repository;

import org.jesperancinha.twitter.model.db.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
