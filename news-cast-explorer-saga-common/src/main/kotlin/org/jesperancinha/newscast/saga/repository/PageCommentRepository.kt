package org.jesperancinha.newscast.saga.repository

import org.jesperancinha.newscast.saga.domain.PageComment
import org.springframework.data.jpa.repository.JpaRepository

interface PageCommentRepository: JpaRepository<PageComment, Long> {
    fun findByRequestId(requestId: Long): List<PageComment>?
}