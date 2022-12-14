package org.jesperancinha.newscast.saga.service

import org.jesperancinha.newscast.saga.domain.AuthorComment
import org.jesperancinha.newscast.saga.repository.AuthorCommentRepository
import org.springframework.stereotype.Service

/**
 * Created by jofisaes on 06/10/2021
 */
@Service
class NewsCastAuthorCommentService(
    private val authorCommentRepository: AuthorCommentRepository,
) {
    fun save(authorComment: AuthorComment): AuthorComment = authorCommentRepository.save(authorComment)

    fun getByRequestId(requestId: Long): List<AuthorComment>? = authorCommentRepository.findByRequestId(requestId)
}