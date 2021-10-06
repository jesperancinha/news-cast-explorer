package org.jesperancinha.newscast.repository;

import org.jesperancinha.newscast.model.explorer.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
}
