package org.jesperancinha.newscast.repository;

import org.jesperancinha.newscast.model.explorer.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findFirstByNewsCastAuthorIdAndPageId(String id, Long pageIg);
}
