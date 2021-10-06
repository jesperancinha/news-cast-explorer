package org.jesperancinha.newscast.orchestration.service

import org.jesperancinha.newscast.saga.domain.AuthorComment
import org.jesperancinha.newscast.saga.repository.AuthorCommentRepository
import org.springframework.stereotype.Service

/**
 * Created by jofisaes on 06/10/2021
 */
@Service
class NewsCastAuthorCommentService(
    val authorCommentRepository: AuthorCommentRepository,
) {
    fun save(authorComment: AuthorComment): AuthorComment {
        return authorCommentRepository.save(authorComment)
    }
}