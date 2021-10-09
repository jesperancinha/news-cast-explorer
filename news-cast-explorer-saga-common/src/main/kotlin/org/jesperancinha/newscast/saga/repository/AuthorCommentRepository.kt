package org.jesperancinha.newscast.saga.repository

import org.jesperancinha.newscast.saga.domain.AuthorComment
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by jofisaes on 06/10/2021
 */
interface AuthorCommentRepository : JpaRepository<AuthorComment, Long> {
     fun findByRequestId(requestId: Long): List<AuthorComment>?
}