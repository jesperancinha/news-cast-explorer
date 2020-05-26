package org.jesperancinha.twitter.repository;

import org.jesperancinha.twitter.model.db.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
