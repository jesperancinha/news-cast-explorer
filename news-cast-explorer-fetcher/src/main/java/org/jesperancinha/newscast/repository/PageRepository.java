package org.jesperancinha.newscast.repository;

import org.jesperancinha.newscast.model.db.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
}
