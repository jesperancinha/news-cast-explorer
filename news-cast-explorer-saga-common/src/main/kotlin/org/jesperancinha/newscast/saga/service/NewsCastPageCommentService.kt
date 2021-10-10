package org.jesperancinha.newscast.saga.service

import org.jesperancinha.newscast.saga.domain.PageComment
import org.jesperancinha.newscast.saga.repository.PageCommentRepository
import org.springframework.stereotype.Service

/**
 * Created by jofisaes on 06/10/2021
 */
@Service
class NewsCastPageCommentService(
    private val pageCommentRepository: PageCommentRepository,
) {
    fun save(pageComment: PageComment): PageComment = pageCommentRepository.save(pageComment)
    fun getByRequestId(requestId: Long): List<PageComment>? = pageCommentRepository.findByRequestId(requestId)
}