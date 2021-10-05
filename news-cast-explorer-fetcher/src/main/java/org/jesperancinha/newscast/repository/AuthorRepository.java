package org.jesperancinha.newscast.repository;

import org.jesperancinha.newscast.model.db.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
