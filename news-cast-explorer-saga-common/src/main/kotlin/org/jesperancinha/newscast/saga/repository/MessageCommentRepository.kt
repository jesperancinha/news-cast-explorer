package org.jesperancinha.newscast.saga.repository

import org.jesperancinha.newscast.saga.domain.MessageComment
import org.springframework.data.jpa.repository.JpaRepository

interface MessageCommentRepository : JpaRepository<MessageComment, Long> {
    fun findByRequestId(requestId: Long): MessageComment?
}