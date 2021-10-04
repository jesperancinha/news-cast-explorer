package org.jesperancinha.twitter.repository;

import org.jesperancinha.twitter.model.db.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
}
