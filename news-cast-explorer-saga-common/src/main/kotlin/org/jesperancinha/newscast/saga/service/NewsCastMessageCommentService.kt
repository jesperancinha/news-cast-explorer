package org.jesperancinha.newscast.saga.service

import org.jesperancinha.newscast.saga.domain.MessageComment
import org.jesperancinha.newscast.saga.repository.MessageCommentRepository
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by jofisaes on 06/10/2021
 */
@Service
class NewsCastMessageCommentService(
    private val messageCommentRepository: MessageCommentRepository,
) {
    fun save(messageComment: MessageComment): MessageComment = messageCommentRepository.save(messageComment)

    fun getByRequestId(requestId: Long): List<MessageComment>? = messageCommentRepository.findByRequestId(requestId)
}